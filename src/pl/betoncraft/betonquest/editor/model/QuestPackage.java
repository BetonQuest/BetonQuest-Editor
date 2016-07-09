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

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Keeps all data about the quest package.
 *
 * @author Jakub Sapalski
 */
public class QuestPackage {

	private final StringProperty packName;
	private String defLang;
	private final HashMap<String, Integer> languages = new HashMap<>();
	private final ObservableList<Conversation> conversations = FXCollections.observableArrayList();
	private final ObservableList<Event> events = FXCollections.observableArrayList();
	private final ObservableList<Condition> conditions = FXCollections.observableArrayList();
	private final ObservableList<Objective> objectives = FXCollections.observableArrayList();
	private final ObservableList<JournalEntry> journal = FXCollections.observableArrayList();
	private final ObservableList<Item> items = FXCollections.observableArrayList();
	private final ObservableList<GlobalVariable> variables = FXCollections.observableArrayList();
	private final ObservableList<GlobalLocation> locations = FXCollections.observableArrayList();
	private final ObservableList<StaticEvent> staticEvents = FXCollections.observableArrayList();
	private final ObservableList<QuestCanceler> cancelers = FXCollections.observableArrayList();
	private final ObservableList<NpcBinding> npcBindings = FXCollections.observableArrayList();

	/**
	 * Loads a package using a hashmap containing all data. The key is a file
	 * name, the value is a hashmap containing keys and values of that YAML
	 * file.
	 * 
	 * @param map
	 */
	private QuestPackage(String id, HashMap<String, HashMap<String, String>> data) {
		packName = new SimpleStringProperty(id);
		try {
			// handling main.yml
			HashMap<String, String> config = data.get("main");
			for (Entry<String, String> entry : config.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				// handling variables
				if (key.startsWith("variables.")) {
					variables.add(new GlobalVariable(key.substring(10), value));
				}
				// handling global locations
				else if (key.startsWith("global_locations")) {
					for (String globLoc : value.split(",")) {
						locations.add(new GlobalLocation(globLoc));
					}
				}
				// handling static events
				else if (key.startsWith("static_events.")) {
					staticEvents.add(new StaticEvent(key.substring(14), value));
				}
				// handling NPC-conversation bindings
				else if (key.startsWith("npcs.")) {
					npcBindings.add(new NpcBinding(key.substring(5), value));
				}
				// handling quest cancelers
				else if (key.startsWith("cancel.")) {
					String[] parts = key.split("\\.");
					if (parts.length > 1) {
						// getting the right canceler or creating new one
						String cancelerName = parts[1];
						QuestCanceler canceler = null;
						for (QuestCanceler test : cancelers) {
							if (test.getId().equals(cancelerName)) {
								canceler = test;
								break;
							}
						}
						if (canceler == null) {
							canceler = new QuestCanceler(cancelerName);
							cancelers.add(canceler);
						}
						// handling canceler properties
						if (parts.length > 2) {
							switch (parts[2]) {
							case "name":
								if (parts.length > 3) {
									String lang = parts[2];
									if (languages.containsKey(lang)) {
										languages.put(lang, 1);
									} else {
										languages.put(lang, languages.get(lang) + 1);
									}
									canceler.getName().addLang(lang, value);
								} else {
									canceler.getName().setDef(value);
								}
								break;
							case "events":
								canceler.getEvents().addAll(value.split("\\."));
								break;
							case "conditions":
								canceler.getConditions().addAll(value.split("\\."));
								break;
							case "objectives":
								canceler.getObjectives().addAll(value.split("\\."));
								break;
							case "tags":
								canceler.getTags().addAll(value.split("\\."));
								break;
							case "points":
								canceler.getPoints().addAll(value.split("\\."));
								break;
							case "journal":
								canceler.getJournal().addAll(value.split("\\."));
								break;
							case "loc":
								canceler.setLocation(value);
								break;
							}
						}
					}
					// cancelers.add(new QuestCanceler(key.substring(7), config.get(key))); TODO add cancelers
				}
				// handling default language
				else if (key.equalsIgnoreCase("default_language")) {
					defLang = value;
				}
			}
			// handling event.yml
			HashMap<String, String> eventsMap = data.get("events");
			for (String key : eventsMap.keySet()) {
				events.add(new Event(key, eventsMap.get(key)));
			}
			// handling conditions.yml
			HashMap<String, String> conditionsMap = data.get("conditions");
			for (String key : conditionsMap.keySet()) {
				conditions.add(new Condition(key, conditionsMap.get(key)));
			}
			// handling objectives.yml
			HashMap<String, String> objectivesMap = data.get("objectives");
			for (String key : objectivesMap.keySet()) {
				objectives.add(new Objective(key, objectivesMap.get(key)));
			}
			// handling items.yml
			HashMap<String, String> itemsMap = data.get("items");
			for (String key : itemsMap.keySet()) {
				items.add(new Item(key, itemsMap.get(key)));
			}
			// handling journal.yml
			HashMap<String, String> journalMap = data.get("journal");
			for (Entry<String, String> entry : journalMap.entrySet()) {
				String value = entry.getValue();
				String[] parts = entry.getKey().split("\\.");
				// getting the right entry
				String entryName = parts[0];
				JournalEntry journalEntry = null;
				for (JournalEntry test : journal) {
					if (test.getId().equals(entryName)) {
						journalEntry = test;
						break;
					}
				}
				if (journalEntry == null) {
					journalEntry = new JournalEntry(entryName);
					journal.add(journalEntry);
				}
				// handling entry data
				if (parts.length > 1) {
					String lang = parts[1];
					if (!languages.containsKey(lang)) {
						languages.put(lang, 1);
					} else {
						languages.put(lang, languages.get(lang) + 1);
					}
					journalEntry.getText().addLang(lang, value);
				} else {
					journalEntry.getText().setDef(value);
				}
			}
			// handling conversations/
			for (Entry<String, HashMap<String, String>> entry : data.entrySet()) {
				String key = entry.getKey();
				HashMap<String, String> value = entry.getValue();
				if (key.startsWith("conversations.")) {
					HashMap<String, String> convData = value;
					String convName = key.substring(14);
					Conversation conv = new Conversation(this, convName);
					// handling conversation.yml
					for (Entry<String, String> subEntry : convData.entrySet()) {
						String subKey = subEntry.getKey();
						String subValue = subEntry.getValue();
						// reading NPC name, optionally in multiple languages
						if (subKey.equals("quester")) {
							conv.getNPC().setDef(subValue);
						} else if (subKey.startsWith("quester.")) {
							String lang = subKey.substring(8);
							if (!languages.containsKey(lang)) {
								languages.put(lang, 1);
							} else {
								languages.put(lang, languages.get(lang) + 1);
							}
							conv.getNPC().addLang(lang, subValue);
						}
						// reading the stop option
						else if (subKey.equals("stop")) {
							conv.getStop().set(subValue.equalsIgnoreCase("true"));
						}
						// reading starting options
						else if (subKey.equals("first")) {
							String[] options = subValue.split(",");
							for (int i = 0; i < options.length; i++) {
								options[i] = options[i].trim();
							}
							conv.getStartingOptions().addAll(options);
						}
						// reading final events
						else if (subKey.equals("final")) {
							String[] events = subValue.split(",");
							for (int i = 0; i < events.length; i++) {
								events[i] = events[i].trim();
							}
							conv.getFinalEvents().addAll(events);
						}
						// reading NPC options
						else if (subKey.startsWith("NPC_options.")) {
							String[] parts = subKey.split("\\.");
							if (parts.length > 1) {
								String optionName = parts[1];
								// resolving an option
								NpcOption option = conv.getNpcOption(optionName);
								if (option == null) {
									option = new NpcOption(optionName);
									conv.getNpcOptions().add(option);
								}
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length > 3) {
											String lang = parts[2];
											if (!languages.containsKey(lang)) {
												languages.put(lang, 1);
											} else {
												languages.put(lang, languages.get(lang) + 1);
											}
											option.getText().addLang(lang, subValue);
										} else {
											option.getText().setDef(subValue);
										}
										break;
									case "event":
									case "events":
										String[] events = subValue.split(",");
										for (int i = 0; i < events.length; i++) {
											events[i] = events[i].trim();
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditions = subValue.split(",");
										for (int i = 0; i < conditions.length; i++) {
											conditions[i] = conditions[i].trim();
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointers = subValue.split(",");
										for (int i = 0; i < pointers.length; i++) {
											pointers[i] = pointers[i].trim();
										}
										option.getPointers().addAll(pointers);
										break;
									}
								}
							}
						}
						// reading player options
						else if (subKey.startsWith("player_options.")) {
							String[] parts = subKey.split("\\.");
							if (parts.length > 1) {
								String optionName = parts[1];
								// resolving an option
								PlayerOption option = conv.getPlayerOption(optionName);
								if (option == null) {
									option = new PlayerOption(optionName);
									conv.getPlayerOptions().add(option);
								}
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length > 3) {
											String lang = parts[2];
											if (!languages.containsKey(lang)) {
												languages.put(lang, 1);
											} else {
												languages.put(lang, languages.get(lang) + 1);
											}
											option.getText().addLang(lang, subValue);
										} else {
											option.getText().setDef(subValue);
										}
										break;
									case "event":
									case "events":
										String[] events = subValue.split(",");
										for (int i = 0; i < events.length; i++) {
											events[i] = events[i].trim();
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditions = subValue.split(",");
										for (int i = 0; i < conditions.length; i++) {
											conditions[i] = conditions[i].trim();
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointers = subValue.split(",");
										for (int i = 0; i < pointers.length; i++) {
											pointers[i] = pointers[i].trim();
										}
										option.getPointers().addAll(pointers);
										break;
									}
								}
							}
						}
					}
					conversations.add(conv);
				}
			}
			// check which language is used most widely and set it as default
			if (defLang == null) {
				int max = 0;
				String maxLang = null;
				for (Entry<String, Integer> entry : languages.entrySet()) {
					if (entry.getValue() > max) {
						max = entry.getValue();
						maxLang = entry.getKey();
					}
				}
				defLang = maxLang;
			}
		} catch (Exception e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}
	
	public String getName() {
		return packName.get();
	}
	
	public void setName(String name) {
		packName.set(name);
	}
	
	public String getDefLang() {
		return defLang;
	}
	
	public void setDefLang(String defLang) {
		this.defLang = defLang;
	}

	public ObservableList<Conversation> getConversations() {
		return conversations;
	}

	public ObservableList<Event> getEvents() {
		return events;
	}

	public ObservableList<Condition> getConditions() {
		return conditions;
	}

	public ObservableList<Objective> getObjectives() {
		return objectives;
	}

	public ObservableList<JournalEntry> getJournal() {
		return journal;
	}

	public ObservableList<Item> getItems() {
		return items;
	}

	public ObservableList<GlobalVariable> getVariables() {
		return variables;
	}

	public ObservableList<GlobalLocation> getLocations() {
		return locations;
	}

	public ObservableList<StaticEvent> getStaticEvents() {
		return staticEvents;
	}

	public ObservableList<QuestCanceler> getCancelers() {
		return cancelers;
	}

	public ObservableList<NpcBinding> getNpcBindings() {
		return npcBindings;
	}
	
	@Override
	public String toString() {
		return packName.get();
	}

	public static QuestPackage loadFromZip(ZipFile file) throws IOException, PackageNotFoundException {
		HashMap<String, ZipEntry> zipEntries = new HashMap<>();
		Enumeration<? extends ZipEntry> entries = file.entries();
		String packName = null;
		// extract correct entries from the zip file
		while (true) {
			try {
				ZipEntry entry = entries.nextElement();
				String entryName = entry.getName();
				packName = entryName.substring(0, entryName.indexOf('/'));
				if (!entryName.endsWith(".yml"))
					continue;
				if (entryName.contains("conversations/")) {
					String convName = entryName.substring(entryName.lastIndexOf('/') + 1, entryName.length() - 4);
					zipEntries.put("conversations." + convName, entry);
				} else {
					if (entryName.endsWith("main.yml")) {
						zipEntries.put("main", entry);
					} else if (entryName.endsWith("events.yml")) {
						zipEntries.put("events", entry);
					} else if (entryName.endsWith("conditions.yml")) {
						zipEntries.put("conditions", entry);
					} else if (entryName.endsWith("objectives.yml")) {
						zipEntries.put("objectives", entry);
					} else if (entryName.endsWith("journal.yml")) {
						zipEntries.put("journal", entry);
					} else if (entryName.endsWith("items.yml")) {
						zipEntries.put("items", entry);
					}
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
		// check if everything is loaded
		if (!zipEntries.containsKey("main") || !zipEntries.containsKey("events")
				|| !zipEntries.containsKey("conditions") || !zipEntries.containsKey("objectives")
				|| !zipEntries.containsKey("journal") || !zipEntries.containsKey("items")) {
			file.close();
			throw new PackageNotFoundException("Package does not contain required files");
		}
		// parse the yaml into hashmaps
		HashMap<String, HashMap<String, String>> values = new HashMap<>();
		for (String name : zipEntries.keySet()) {
			values.put(name, new HashMap<>());
			YAMLParser parser = new YAMLFactory().createParser(file.getInputStream(zipEntries.get(name)));
			String currentPath = "";
			String fieldName = "";
			while (true) {
				JsonToken token = parser.nextToken();
				if (token == null)
					break;
				if (token == JsonToken.START_OBJECT) {
					currentPath = currentPath + fieldName + ".";
				} else if (token == JsonToken.FIELD_NAME) {
					fieldName = parser.getText();
				} else if (token == JsonToken.VALUE_STRING || token == JsonToken.VALUE_NUMBER_INT
						|| token == JsonToken.VALUE_NUMBER_FLOAT || token == JsonToken.VALUE_TRUE
						|| token == JsonToken.VALUE_FALSE) {
					values.get(name).put(
							(currentPath + fieldName).substring(1, currentPath.length() + fieldName.length()),
							parser.getText());
				} else if (token == JsonToken.END_OBJECT) {
					currentPath = currentPath.substring(0,
							currentPath.substring(0, currentPath.length() - 1).lastIndexOf(".") + 1);
				}
			}
		}
		return new QuestPackage(packName, values);
	}

}
