/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.editor.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.ExceptionController;
import pl.betoncraft.betonquest.editor.controller.RootController;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Represents a set of packages on a single server.
 *
 * @author Jakub Sapalski
 */
public class PackageSet {
	
	private StringProperty name;
	private List<QuestPackage> packages = new LinkedList<>();
	
	private PackageSet(String setName) {
		name = new SimpleStringProperty(setName);
	}
	
	public StringProperty getName() {
		return name;
	}
	
	public List<QuestPackage> getPackages() {
		return packages;
	}
	
	public QuestPackage getPackage(String packName) {
		for (QuestPackage pack : packages) {
			if (pack.getName().get().equals(packName)) {
				return pack;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name.get();
	}
	
	public static PackageSet loadFromZip(ZipFile file) throws IOException, PackageNotFoundException {
		HashMap<String, HashMap<String, InputStream>> setMap = new HashMap<>();
		Enumeration<? extends ZipEntry> entries = file.entries();
		String setName = file.getName().substring(file.getName().lastIndexOf(File.separatorChar) + 1).replace(".zip", "");
		// extract correct entries from the zip file
		while (true) {
			try {
				ZipEntry entry = entries.nextElement();
				String entryName = entry.getName();
				// get the correct path separator (both can be used)
				char separator = (entryName.contains("/") ? '/' : '\\');
				// index of first separator used to get set name
				int index = entryName.indexOf(separator);
				// skip entry if there are no path separators (files in zip root like license, readme etc.)
				if (index < 0) {
					continue;
				}
				if (!entryName.endsWith(".yml")) {
					continue;
				}
				String[] parts = entryName.split(Pattern.quote(String.valueOf(separator)));
				String ymlName = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 4);
				StringBuilder builder = new StringBuilder();
				int length = parts.length - 1;
				boolean conversation = false;
				if (parts[length - 1].equals("conversations")) {
					length--;
					conversation = true;
				}
				for (int i = 0; i < length; i++) {
					builder.append(parts[i] + '-');
				}
				String packName = builder.substring(0, builder.length() - 1);
				HashMap<String, InputStream> packMap = setMap.get(packName);
				if (packMap == null) {
					packMap = new HashMap<>();
					setMap.put(packName, packMap);
				}
				List<String> allowedNames = Arrays.asList(new String[]{"main", "events", "conditions", "objectives", "journal", "items"});
				if (conversation) {
					packMap.put("conversations." + ymlName, file.getInputStream(entry));
				} else {
					if (allowedNames.contains(ymlName)) {
						packMap.put(ymlName, file.getInputStream(entry));
					}
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
		PackageSet set = parseStreams(setName, setMap);
		BetonQuestEditor.getInstance().getSets().add(set);
		RootController.setPackages(BetonQuestEditor.getInstance().getSets());
		return set;
	}
	
	private static PackageSet parseStreams(String setName, HashMap<String, HashMap<String, InputStream>> streamMap) throws IOException {
		PackageSet set = new PackageSet(setName);
		for (Entry<String, HashMap<String, InputStream>> entry : streamMap.entrySet()) {
			String packName = entry.getKey();
			HashMap<String, LinkedHashMap<String, String>> values = new LinkedHashMap<>();
			for (Entry<String, InputStream> subEntry : entry.getValue().entrySet()) {
				String name = subEntry.getKey();
				InputStream stream = subEntry.getValue();
				YAMLParser parser = new YAMLFactory().createParser(stream);
				String currentPath = "";
				String fieldName = "";
				while (true) {
					JsonToken token = parser.nextToken();
					if (token == null)
						break;
					switch (token) {
					case START_OBJECT:
						currentPath = currentPath + fieldName + ".";
						break;
					case FIELD_NAME:
						fieldName = parser.getText();
						break;
					case END_OBJECT:
						currentPath = currentPath.substring(0, currentPath.substring(0, currentPath.length() - 1).lastIndexOf(".") + 1);
						break;
					case VALUE_STRING:
					case VALUE_NUMBER_INT:
					case VALUE_NUMBER_FLOAT:
					case VALUE_FALSE:
					case VALUE_TRUE:
						String key = (currentPath + fieldName).substring(1, currentPath.length() + fieldName.length());
						LinkedHashMap<String, String> map = values.get(name);
						if (map == null) {
							map = new LinkedHashMap<>();
							values.put(name, map);
						}
						map.put(key, parser.getText());
					default:
						// do nothing
					}
				}
				parser.close();
				stream.close();
			}
			QuestPackage pack = new QuestPackage(set, packName, values);
			set.packages.add(pack);
		}
		return set;
	}

	/**
	 * Saves the package to a .zip file.
	 * 
	 * @param zipFile
	 */
	public void saveToZip(File zip) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
			for (QuestPackage pack : packages) {
				String prefix = pack.getName().get().replace("-", File.separator) + File.separator;
				// save main.yml file
				ZipEntry main = new ZipEntry(prefix + "main.yml");
				out.putNextEntry(main);
				pack.printMainYAML(out);
				out.closeEntry();
				// save conversation files
				for (Conversation conv : pack.getConversations()) {
					ZipEntry conversation = new ZipEntry(
							prefix + "conversations" + File.separator + conv.getId().get() + ".yml");
					out.putNextEntry(conversation);
					pack.printConversationYaml(out, conv);
					out.closeEntry();
				}
				// save events.yml file
				ZipEntry events = new ZipEntry(prefix + "events.yml");
				out.putNextEntry(events);
				pack.printEventsYaml(out);
				out.closeEntry();
				// save conditions.yml file
				ZipEntry conditions = new ZipEntry(prefix + "conditions.yml");
				out.putNextEntry(conditions);
				pack.printConditionsYaml(out);
				out.closeEntry();
				// save objectives.yml file
				ZipEntry objectives = new ZipEntry(prefix + "objectives.yml");
				out.putNextEntry(objectives);
				pack.printObjectivesYaml(out);
				out.closeEntry();
				// save items.yml file
				ZipEntry items = new ZipEntry(prefix + "items.yml");
				out.putNextEntry(items);
				pack.printItemsYaml(out);
				out.closeEntry();
				// save journal.yml file
				ZipEntry journal = new ZipEntry(prefix + "journal.yml");
				out.putNextEntry(journal);
				pack.printJournalYaml(out);
				out.closeEntry();
			}
			// done
			out.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
