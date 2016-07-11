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
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;
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
	private final ObservableList<MainPageLine> mainPage = FXCollections.observableArrayList();

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
						locations.add(new GlobalLocation(newObjective(globLoc)));
					}
				}
				// handling static events
				else if (key.startsWith("static_events.")) {
					StaticEvent staticEvent = new StaticEvent(key.substring(14));
					staticEvent.getEvent().set(newEvent(value));
					staticEvents.add(staticEvent);
				}
				// handling NPC-conversation bindings
				else if (key.startsWith("npcs.")) {
					npcBindings.add(new NpcBinding(key.substring(5), newConversation(value)));
				}
				// handling quest cancelers
				else if (key.startsWith("cancel.")) {
					String[] parts = key.split("\\.");
					if (parts.length > 1) {
						// getting the right canceler or creating new one
						QuestCanceler canceler = newQuestCanceler(parts[1]);
						// handling canceler properties
						if (parts.length > 2) {
							switch (parts[2]) {
							case "name":
								if (parts.length > 3) {
									String lang = parts[3];
									if (!languages.containsKey(lang)) {
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
								String[] eventNames = value.split("\\.");
								Event[] events = new Event[eventNames.length];
								for (int i = 0; i < eventNames.length; i++) {
									events[i] = newEvent(eventNames[i].trim());
								}
								canceler.getEvents().addAll(events);
								break;
							case "conditions":
								String[] conditionNames = value.split("\\.");
								Condition[] conditions = new Condition[conditionNames.length];
								for (int i = 0; i < conditionNames.length; i++) {
									conditions[i] = newCondition(conditionNames[i].trim());
								}
								canceler.getConditions().addAll(conditions);
								break;
							case "objectives":
								String[] objectiveNames = value.split("\\.");
								Objective[] objectives = new Objective[objectiveNames.length];
								for (int i = 0; i < objectiveNames.length; i++) {
									objectives[i] = newObjective(objectiveNames[i].trim());
								}
								canceler.getObjectives().addAll(objectives);
								break;
							case "tags":
								canceler.getTags().addAll(value.split("\\."));
								break;
							case "points":
								canceler.getPoints().addAll(value.split("\\."));
								break;
							case "journal":
								String[] journalNames = value.split("\\.");
								JournalEntry[] journal = new JournalEntry[journalNames.length];
								for (int i = 0; i < journalNames.length; i++) {
									journal[i] = newJournalEntry(journalNames[i].trim());
								}
								canceler.getJournal().addAll(journal);
								break;
							case "loc":
								canceler.setLocation(value);
								break;
							}
						}
					}
				}
				// handling journal main page
				else if (key.startsWith("journal_main_page")) {
					String[] parts = key.split("\\.");
					if (parts.length > 1) {
						MainPageLine line = newMainPageLine(parts[1]);
						if (parts.length > 2) {
							switch (parts[2]) {
							case "text":
								if (parts.length > 3) {
									String lang = parts[3];
									if (!languages.containsKey(lang)) {
										languages.put(lang, 1);
									} else {
										languages.put(lang, languages.get(lang) + 1);
									}
									line.getText().addLang(lang, value);
								} else {
									line.getText().setDef(value);
								}
								break;
							case "priority":
								try {
									line.getPriority().set(Integer.parseInt(value));
								} catch (NumberFormatException e) {
									// TODO error, need a number
								}
								break;
							case "conditions":
								String[] conditionNames = value.split(",");
								Condition[] conditions = new Condition[conditionNames.length];
								for (int i = 0; i < conditionNames.length; i++) {
									conditions[i] = newCondition(conditionNames[i].trim());
								}
								line.getConditions().addAll(conditions);
								break;
							}
						}
					}
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
				JournalEntry journalEntry = newJournalEntry(parts[0]);
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
					Conversation conv = newConversation(convName);
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
							String[] pointerNames = subValue.split(",");
							NpcOption[] options = new NpcOption[pointerNames.length];
							for (int i = 0; i < pointerNames.length; i++) {
								options[i] = conv.newNpcOption(pointerNames[i].trim());
							}
							conv.getStartingOptions().addAll(options);
						}
						// reading final events
						else if (subKey.equals("final")) {
							String[] eventNames = subValue.split(",");
							Event[] events = new Event[eventNames.length];
							for (int i = 0; i < eventNames.length; i++) {
								events[i] = newEvent(eventNames[i].trim());
							}
							conv.getFinalEvents().addAll(events);
						}
						// reading NPC options
						else if (subKey.startsWith("NPC_options.")) {
							String[] parts = subKey.split("\\.");
							if (parts.length > 1) {
								String optionName = parts[1];
								// resolving an option
								NpcOption option = conv.newNpcOption(optionName);
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length > 3) {
											String lang = parts[3];
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
										String[] eventNames = subValue.split(",");
										Event[] events = new Event[eventNames.length];
										for (int i = 0; i < eventNames.length; i++) {
											events[i] = newEvent(eventNames[i].trim());
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditionNames = subValue.split(",");
										Condition[] conditions = new Condition[conditionNames.length];
										for (int i = 0; i < conditionNames.length; i++) {
											conditions[i] = new Condition(conditionNames[i].trim());
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointerNames = subValue.split(",");
										PlayerOption[] options = new PlayerOption[pointerNames.length];
										for (int i = 0; i < pointerNames.length; i++) {
											options[i] = conv.newPlayerOption(pointerNames[i].trim());
										}
										option.getPointers().addAll(options);
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
								PlayerOption option = conv.newPlayerOption(optionName);
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length > 3) {
											String lang = parts[3];
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
										String[] eventNames = subValue.split(",");
										Event[] events = new Event[eventNames.length];
										for (int i = 0; i < eventNames.length; i++) {
											events[i] = newEvent(eventNames[i].trim());
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditionNames = subValue.split(",");
										Condition[] conditions = new Condition[conditionNames.length];
										for (int i = 0; i < conditionNames.length; i++) {
											conditions[i] = new Condition(conditionNames[i].trim());
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointerNames = subValue.split(",");
										NpcOption[] options = new NpcOption[pointerNames.length];
										for (int i = 0; i < pointerNames.length; i++) {
											options[i] = conv.newNpcOption(pointerNames[i].trim());
										}
										option.getPointers().addAll(options);
										break;
									}
								}
							}
						}
					}
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

//	/**
//	 * Updates the data across this package.
//	 * 
//	 * @param oldId ID of this data before it was changed
//	 * @param data updated data instance
//	 */
//	public void update(String oldId, Instructionable data) {
//		switch (data.getClass().getSimpleName()) {
//		case "Event":
//			for (Conversation conv : conversations) {
//				ArrayList<ConversationOption> options = new ArrayList<>(conv.getNpcOptions());
//				options.addAll(conv.getPlayerOptions());
//				for (ConversationOption option : options) {
//					if (option.getEvents().contains(oldId)) {
//						option.getEvents().remove(oldId);
//						option.getEvents().add(data.getId().get());
//					}
//				}
//				if (conv.getFinalEvents().contains(oldId)) {
//					conv.getFinalEvents().remove(oldId);
//					conv.getFinalEvents().add(data.getId().get());
//				}
//			}
//			break;
//		case "Condition":
//			for (Conversation conv : conversations) {
//				ArrayList<ConversationOption> options = new ArrayList<>(conv.getNpcOptions());
//				options.addAll(conv.getPlayerOptions());
//				for (ConversationOption option : options) {
//					if (option.getConditions().contains(oldId)) {
//						option.getConditions().remove(oldId);
//						option.getConditions().add(data.getId().get());
//					}
//				}
//			}
//			break;
//		}
//	}
	
	public StringProperty getName() {
		return packName;
	}
	
	public String getDefLang() {
		return defLang;
	}
	
	public void setDefLang(String defLang) {
		this.defLang = defLang;
	}
	
	public static <T extends ID> T getByID(ObservableList<T> list, String id) {
		for (T object : list) {
			if (object.getId().get().equals(id)) {
				return object;
			}
		}
		return null;
	}

	public ObservableList<Conversation> getConversations() {
		return conversations;
	}
	
	public Conversation newConversation(String id) {
		Conversation conv = getByID(conversations, id);
		if (conv == null) {
			conv = new Conversation(this, id);
			conversations.add(conv);
		}
		return conv;
	}

	public ObservableList<Event> getEvents() {
		return events;
	}
	
	public Event newEvent(String id) {
		Event event = getByID(events, id);
		if (event == null) {
			event = new Event(id);
			events.add(event);
		}
		return event;
	}

	public ObservableList<Condition> getConditions() {
		return conditions;
	}
	
	public Condition newCondition(String id) {
		Condition condition = getByID(conditions, id);
		if (condition == null) {
			condition = new Condition(id);
			conditions.add(condition);
		}
		return condition;
	}

	public ObservableList<Objective> getObjectives() {
		return objectives;
	}
	
	public Objective newObjective(String id) {
		Objective objective = getByID(objectives, id);
		if (objective == null) {
			objective = new Objective(id);
			objectives.add(objective);
		}
		return objective;
	}

	public ObservableList<JournalEntry> getJournal() {
		return journal;
	}
	
	public JournalEntry newJournalEntry(String id) {
		JournalEntry journalEntry = getByID(journal, id);
		if (journalEntry == null) {
			journalEntry = new JournalEntry(id);
			journal.add(journalEntry);
		}
		return journalEntry;
	}

	public ObservableList<Item> getItems() {
		return items;
	}
	
	public Item newItem(String id) {
		Item item = getByID(items, id);
		if (item == null) {
			item = new Item(id);
			items.add(item);
		}
		return item;
	}

	public ObservableList<GlobalVariable> getVariables() {
		return variables;
	}
	
	public GlobalVariable newGlobalVariable(String id) {
		GlobalVariable globalVariable = getByID(variables, id);
		if (globalVariable == null) {
			globalVariable = new GlobalVariable(id);
			variables.add(globalVariable);
		}
		return globalVariable;
	}

	public ObservableList<GlobalLocation> getLocations() {
		return locations;
	}

	public ObservableList<StaticEvent> getStaticEvents() {
		return staticEvents;
	}
	
	public StaticEvent newStaticEvent(String id) {
		StaticEvent staticEvent = getByID(staticEvents, id);
		if (staticEvent == null) {
			staticEvent = new StaticEvent(id);
			staticEvents.add(staticEvent);
		}
		return staticEvent;
	}

	public ObservableList<QuestCanceler> getCancelers() {
		return cancelers;
	}
	
	public QuestCanceler newQuestCanceler(String id) {
		QuestCanceler questCanceler = getByID(cancelers, id);
		if (questCanceler == null) {
			questCanceler = new QuestCanceler(id);
			cancelers.add(questCanceler);
		}
		return questCanceler;
	}

	public ObservableList<NpcBinding> getNpcBindings() {
		return npcBindings;
	}
	
	public NpcBinding newNpcBinding(String id) {
		NpcBinding npcBinding = getByID(npcBindings, id);
		if (npcBinding == null) {
			npcBinding = new NpcBinding(id);
			npcBindings.add(npcBinding);
		}
		return npcBinding;
	}
	
	public ObservableList<MainPageLine> getMainPage() {
		return mainPage;
	}
	
	public MainPageLine newMainPageLine(String id) {
		MainPageLine mainPageLine = getByID(mainPage, id);
		if (mainPageLine == null) {
			mainPageLine = new MainPageLine(id);
			mainPage.add(mainPageLine);
		}
		return mainPageLine;
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
	
	public void printMainYAML(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		// save NPCs
		if (!npcBindings.isEmpty()) {
			ObjectNode npcs = mapper.createObjectNode();
			for (NpcBinding binding : npcBindings) {
				npcs.put(binding.getId().get(), binding.getConversation().get().getId().get());
			}
			root.set("npcs", npcs);
		}
		// save global variables
		if (!variables.isEmpty()) {
			ObjectNode variables = mapper.createObjectNode();
			for (GlobalVariable var : this.variables) {
				variables.put(var.getId().get(), var.getInstruction().get());
			}
			root.set("variables", variables);
		}
		// save static events
		if (!staticEvents.isEmpty()) {
			ObjectNode staticEvents = mapper.createObjectNode();
			for (StaticEvent event : this.staticEvents) {
				staticEvents.put(event.getId().get(), event.getEvent().get().getId().get());
			}
			root.set("static", staticEvents);
		}
		// save global locations
		if (!locations.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (GlobalLocation loc : locations) {
				builder.append(loc.getObjective() + ",");
			}
			root.put("global_locations", builder.toString().substring(0, builder.length() - 1));
		}
		// save quest cancelers
		if (!cancelers.isEmpty()) {
			ObjectNode cancelers = mapper.createObjectNode();
			for (QuestCanceler canceler : this.cancelers) {
				ObjectNode cancelerNode = mapper.createObjectNode();
				addTranslatedNode(mapper, cancelerNode, "name", canceler.getName());
				if (!canceler.getEvents().isEmpty()) {
					StringBuilder events = new StringBuilder();
					for (Event event : canceler.getEvents()) {
						events.append(event.toString() + ',');
					}
					cancelerNode.put("events", events.toString().substring(0, events.length() - 1));
				}
				if (!canceler.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (Condition condition : canceler.getConditions()) {
						conditions.append(condition.toString() + ',');
					}
					cancelerNode.put("conditions", conditions.toString().substring(0, conditions.length() - 1));
				}
				if (!canceler.getObjectives().isEmpty()) {
					StringBuilder objectives = new StringBuilder();
					for (Objective objective : canceler.getObjectives()) {
						objectives.append(objective.toString() + ',');
					}
					cancelerNode.put("objectives", objectives.toString().substring(0, objectives.length() - 1));
				}
				if (!canceler.getTags().isEmpty()) {
					StringBuilder tags = new StringBuilder();
					for (String tag : canceler.getTags()) {
						tags.append(tag + ',');
					}
					cancelerNode.put("tags", tags.toString().substring(0, tags.length() - 1));
				}
				if (!canceler.getPoints().isEmpty()) {
					StringBuilder points = new StringBuilder();
					for (String point : canceler.getPoints()) {
						points.append(point + ',');
					}
					cancelerNode.put("points", points.toString().substring(0, points.length() - 1));
				}
				if (!canceler.getJournal().isEmpty()) {
					StringBuilder journals = new StringBuilder();
					for (JournalEntry journal : canceler.getJournal()) {
						journals.append(journal.toString() + ',');
					}
					cancelerNode.put("journals", journals.toString().substring(0, journals.length() - 1));
				}
				if (canceler.getLocation() != null) {
					cancelerNode.put("loc", canceler.getLocation());
				}
				cancelers.set(canceler.getId().get(), cancelerNode);
			}
			root.set("cancel", cancelers);
		}
		// save main page
		if (!mainPage.isEmpty()) {
			ObjectNode lines = mapper.createObjectNode();
			for (MainPageLine line : mainPage) {
				ObjectNode node = mapper.createObjectNode();
				addTranslatedNode(mapper, node, "text", line.getText());
				node.put("priority", line.getPriority().get());
				StringBuilder conditions = new StringBuilder();
				for (Condition condition : line.getConditions()) {
					conditions.append(condition.toString() + ',');
				}
				node.put("conditions", conditions.substring(0, conditions.length() - 1));
				lines.set(line.getId().get(), node);
			}
			root.set("journal_main_page", lines);
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	public void printEventsYaml(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		for (Event event : events) {
			root.put(event.getId().get(), event.getInstruction().get());
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	public void printConditionsYaml(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		for (Condition condition : conditions) {
			root.put(condition.getId().get(), condition.getInstruction().get());
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	public void printObjectivesYaml(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		for (Objective objective : objectives) {
			root.put(objective.getId().get(), objective.getInstruction().get());
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	public void printItemsYaml(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		for (Item item : items) {
			root.put(item.getId().get(), item.getInstruction().get());
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	public void printJournalYaml(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		for (JournalEntry entry : journal) {
			addTranslatedNode(mapper, root, entry.getId().get(), entry.getText());
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}

	public void printConversationYaml(OutputStream out, Conversation conv) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		addTranslatedNode(mapper, root, "quester", conv.getNPC());
		root.put("stop", String.valueOf(conv.getStop().get()));
		StringBuilder first = new StringBuilder();
		for (NpcOption option : conv.getStartingOptions()) {
			first.append(option.toString() + ',');
		}
		root.put("first", first.substring(0, first.length() - 1));
		if (!conv.getFinalEvents().isEmpty()) {
			StringBuilder finalEvents = new StringBuilder();
			for (Event event : conv.getFinalEvents()) {
				finalEvents.append(event.toString() + ',');
			}
			root.put("final", finalEvents.substring(0, finalEvents.length() - 1));
		}
		if (!conv.getNpcOptions().isEmpty()) {
			ObjectNode npcOptions = mapper.createObjectNode();
			for (NpcOption option : conv.getNpcOptions()) {
				ObjectNode npcOption = mapper.createObjectNode();
				addTranslatedNode(mapper, npcOption, "text", option.getText());
				if (!option.getEvents().isEmpty()) {
					StringBuilder events = new StringBuilder();
					for (Event event : option.getEvents()) {
						events.append(event.toString() + ',');
					}
					npcOption.put("events", events.substring(0, events.length() - 1));
				}
				if (!option.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (Condition condition : option.getConditions()) {
						conditions.append(condition.toString() + ',');
					}
					npcOption.put("conditions", conditions.substring(0, conditions.length() - 1));
				}
				if (!option.getPointers().isEmpty()) {
					StringBuilder pointers = new StringBuilder();
					for (ConversationOption pointer : option.getPointers()) {
						pointers.append(pointer.toString() + ',');
					}
					npcOption.put("pointers", pointers.substring(0, pointers.length() - 1));
				}
				npcOptions.set(option.getId().get(), npcOption);
			}
			root.set("NPC_options", npcOptions);
		}
		if (!conv.getPlayerOptions().isEmpty()) {
			ObjectNode playerOptions = mapper.createObjectNode();
			for (PlayerOption option : conv.getPlayerOptions()) {
				ObjectNode playerOption = mapper.createObjectNode();
				addTranslatedNode(mapper, playerOption, "text", option.getText());
				if (!option.getEvents().isEmpty()) {
					StringBuilder events = new StringBuilder();
					for (Event event : option.getEvents()) {
						events.append(event.toString() + ',');
					}
					playerOption.put("events", events.substring(0, events.length() - 1));
				}
				if (!option.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (Condition condition : option.getConditions()) {
						conditions.append(condition.toString() + ',');
					}
					playerOption.put("conditions", conditions.substring(0, conditions.length() - 1));
				}
				if (!option.getPointers().isEmpty()) {
					StringBuilder pointers = new StringBuilder();
					for (ConversationOption pointer : option.getPointers()) {
						pointers.append(pointer.toString() + ',');
					}
					playerOption.put("pointers", pointers.substring(0, pointers.length() - 1));
				}
				playerOptions.set(option.getId().get(), playerOption);
			}
			root.set("player_options", playerOptions);
		}
		yf.createGenerator(out).setCodec(mapper).writeObject(root);
	}
	
	private void addTranslatedNode(YAMLMapper mapper, ObjectNode root, String name, TranslatableText text) {
		if (text.getDef() != null) {
			root.put(name, text.getDef().get());
		} else {
			ObjectNode node = mapper.createObjectNode();
			for (String lang : text.getLanguages()) {
				node.put(lang, text.get(lang).get());
			}
			root.set(name, node);
		}
	}

	/**
	 * Saves the package to a .zip file.
	 * 
	 * @param zipFile
	 */
	public void saveToZip(File zip) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
			String prefix = packName.get() + File.separator;
			// save main.yml file
			ZipEntry main = new ZipEntry(prefix + "main.yml");
			out.putNextEntry(main);
			printMainYAML(out);
			out.closeEntry();
			// save conversation files
			for (Conversation conv : conversations) {
				ZipEntry conversation = new ZipEntry(prefix + "conversations" + File.separator + conv.getId().get() + ".yml");
				out.putNextEntry(conversation);
				printConversationYaml(out, conv);
				out.closeEntry();
			}
			// save events.yml file
			ZipEntry events = new ZipEntry(prefix + "events.yml");
			out.putNextEntry(events);
			printEventsYaml(out);
			out.closeEntry();
			// save conditions.yml file
			ZipEntry conditions = new ZipEntry(prefix + "conditions.yml");
			out.putNextEntry(conditions);
			printConditionsYaml(out);
			out.closeEntry();
			// save objectives.yml file
			ZipEntry objectives = new ZipEntry(prefix + "objectives.yml");
			out.putNextEntry(objectives);
			printObjectivesYaml(out);
			out.closeEntry();
			// save items.yml file
			ZipEntry items = new ZipEntry(prefix + "items.yml");
			out.putNextEntry(items);
			printItemsYaml(out);
			out.closeEntry();
			// save journal.yml file
			ZipEntry journal = new ZipEntry(prefix + "journal.yml");
			out.putNextEntry(journal);
			printJournalYaml(out);
			out.closeEntry();
			// done
			out.close();
		} catch (Exception e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}

}
