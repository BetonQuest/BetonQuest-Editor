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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import pl.betoncraft.betonquest.editor.controller.ErrorController;
import pl.betoncraft.betonquest.editor.controller.ExceptionController;
import pl.betoncraft.betonquest.editor.controller.RootController;

/**
 * Represents a set of packages on a single server.
 *
 * @author Jakub Sapalski
 */
public class PackageSet {
	
	private StringProperty name;
	private List<QuestPackage> packages = new LinkedList<>();
	private SaveType saveType;
	private File file;
	private boolean root = false; // tells if the package was found directly in selected file
	
	public enum SaveType {
		DIR, ZIP, NONE
	}
	
	public PackageSet(File file, SaveType saveType, String setName) {
		this.file = file;
		this.saveType = saveType;
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
	
	public SaveType getSaveType() {
		return saveType;
	}

	public void setSaveType(SaveType saveType) {
		this.saveType = saveType;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return name.get();
	}
	
	public static PackageSet loadFromZip(File file) throws IOException {
		ZipFile zip = new ZipFile(file);
		HashMap<String, HashMap<String, InputStream>> setMap = new HashMap<>();
		Enumeration<? extends ZipEntry> entries = zip.entries();
		String setName = zip.getName().substring(zip.getName().lastIndexOf(File.separatorChar) + 1).replace(".zip", "");
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
					packMap.put("conversations." + ymlName, zip.getInputStream(entry));
				} else {
					if (allowedNames.contains(ymlName)) {
						packMap.put(ymlName, zip.getInputStream(entry));
					}
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
		PackageSet set = new PackageSet(file, SaveType.ZIP, setName);
		parseStreams(set, setMap);
		zip.close();
		BetonQuestEditor.getInstance().getSets().add(set);
		RootController.setPackageSets(BetonQuestEditor.getInstance().getSets());
		return set;
	}

	public static PackageSet loadFromDirectory(File file) throws IOException {
		HashMap<String, HashMap<String, InputStream>> setMap = new HashMap<>();
		String setName = file.getName().substring(file.getName().lastIndexOf(File.separatorChar) + 1);
		PackageSet set = new PackageSet(file, SaveType.DIR, setName);
		searchFiles("", file.listFiles(), setMap, set);
		parseStreams(set, setMap);
		if (set.getPackages().isEmpty()) {
			BetonQuestEditor.showError("no-package-in-directory");
			return null;
		}
		BetonQuestEditor.getInstance().getSets().add(set);
		RootController.setPackageSets(BetonQuestEditor.getInstance().getSets());
		return set;
	}
	
	private static void searchFiles(String prefix, File[] files, HashMap<String, HashMap<String, InputStream>> setMap, PackageSet set) throws IOException {
		for (File file : files) {
			if (file.isDirectory()) {
				searchFiles(prefix + file.getName() + '-', file.listFiles(), setMap, set);
			} else {
				if (!file.getName().endsWith(".yml")) {
					return;
				}
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				String packName = null;
				if (file.getParentFile().getName().equals("conversations")) {
					prefix = prefix.replace("conversations-", "");
					fileName = "conversations." + fileName;
				} else {
					List<String> allowedNames = Arrays.asList(new String[]{"main", "events", "conditions", "objectives", "journal", "items"});
					if (!allowedNames.contains(fileName)) {
						return;
					}
				}
				if (prefix.isEmpty()) {
					// prefix is empty when the package files are directly in the selected directory
					prefix = file.getParentFile().getName() + "-";
					set.root = true;
				}
				packName = prefix.substring(0, prefix.length() - 1);
				HashMap<String, InputStream> packMap = setMap.get(packName);
				if (packMap == null) {
					packMap = new HashMap<>();
					setMap.put(packName, packMap);
				}
				packMap.put(fileName, new FileInputStream(file));
			}
		}
	}
	
	private static PackageSet parseStreams(PackageSet set, HashMap<String, HashMap<String, InputStream>> streamMap) throws IOException {
		HashMap<QuestPackage, HashMap<String, LinkedHashMap<String, String>>> data = new HashMap<>();
		// parse the data of all packages
		for (Entry<String, HashMap<String, InputStream>> entry : streamMap.entrySet()) {
			QuestPackage pack = new QuestPackage(set, entry.getKey());
			HashMap<String, LinkedHashMap<String, String>> values = new LinkedHashMap<>();
			data.put(pack, values);
			for (Entry<String, InputStream> subEntry : entry.getValue().entrySet()) {
				String name = subEntry.getKey();
				InputStream stream = subEntry.getValue();
				YAMLParser parser = new YAMLFactory().createParser(new InputStreamReader(stream));
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
						String value = parser.getText();
						if (!value.isEmpty()) {
							map.put(key, value);
						}
					default:
						// do nothing
					}
				}
			}
			set.packages.add(pack);
		}
		// set the data once all packages are created
		for (Entry<QuestPackage, HashMap<String, LinkedHashMap<String, String>>> entry : data.entrySet()) {
			entry.getKey().setData(entry.getValue());
		}
		// display errors found during loading
		StringBuilder errors = new StringBuilder();
		for (QuestPackage pack : data.keySet()) {
			if (pack.getErrorManager().hasErrors()) {
				errors.append(pack.getErrorManager().getErrors()); 
			}
		}
		if (errors.length() > 0 ) {
			ErrorController.display(errors.toString().trim());
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

	public void saveToDirectory(File file) {
		try {
			// if the package was directly in the directory the real root is the parent
			if (root) {
				file = file.getParentFile();
			}
			// save to directory
			for (QuestPackage pack : packages) {
				String path = pack.getName().get().replace('-', File.separatorChar);
				File dir = new File(file, path);
				dir.mkdirs();
				// save main file
				File main = new File(dir, "main.yml");
				main.createNewFile();
				OutputStream mainOut = new FileOutputStream(main);
				pack.printMainYAML(mainOut);
				mainOut.close();
				// save events file
				File events = new File(dir, "events.yml");
				events.createNewFile();
				OutputStream eventsOut = new FileOutputStream(events);
				pack.printEventsYaml(eventsOut);
				eventsOut.close();
				// save conditions file
				File conditions = new File(dir, "conditions.yml");
				conditions.createNewFile();
				OutputStream conditionsOut = new FileOutputStream(conditions);
				pack.printConditionsYaml(conditionsOut);
				conditionsOut.close();
				// save objectives file
				File objectives = new File(dir, "objectives.yml");
				objectives.createNewFile();
				OutputStream objectivesOut = new FileOutputStream(objectives);
				pack.printObjectivesYaml(objectivesOut);
				objectivesOut.close();
				// save items file
				File items = new File(dir, "items.yml");
				items.createNewFile();
				OutputStream itemsOut = new FileOutputStream(items);
				pack.printItemsYaml(itemsOut);
				itemsOut.close();
				// save journal file
				File journal = new File(dir, "journal.yml");
				journal.createNewFile();
				OutputStream journalOut = new FileOutputStream(journal);
				pack.printJournalYaml(journalOut);
				journalOut.close();
				// save conversations
				File convDir = new File(dir, "conversations");
				convDir.mkdir();
				for (Conversation conv : pack.getConversations()) {
					File convFile = new File(convDir, conv.getId().get() + ".yml");
					convFile.createNewFile();
					OutputStream convOut = new FileOutputStream(convFile);
					pack.printConversationYaml(convOut, conv);
					convOut.close();
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
