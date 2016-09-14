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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.controller.ConversationController;
import pl.betoncraft.betonquest.editor.controller.ExceptionController;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.Editable;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.data.Translatable;
import pl.betoncraft.betonquest.editor.model.LoadingError.ErrorType;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Keeps all data about the quest package.
 *
 * @author Jakub Sapalski
 */
public class QuestPackage implements Editable {

	private final StringProperty packName;
	private final PackageSet set;
	private final TranslationManager translationManager = new TranslationManager(this);
	private final ErrorManager errorManager = new ErrorManager(this);
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
	private final ObservableList<CompassTarget> compass = FXCollections.observableArrayList();
	private final ObservableList<Tag> tags = FXCollections.observableArrayList();
	private final ObservableList<PointCategory> points = FXCollections.observableArrayList();

	/**
	 * Creates new, empty package.
	 * 
	 * @param set
	 * @param id
	 */
	public QuestPackage(PackageSet set, String id) {
		this.set = set;
		packName = new SimpleStringProperty(id);
	}
	
	/**
	 * Loads the package using a hashmap containing all data. The key is a file
	 * name, the value is a hashmap containing keys and values of that YAML
	 * file.
	 * 
	 * @param map
	 */
	public void setData(HashMap<String, LinkedHashMap<String, String>> data) {
		try {
			// handling journal.yml
			HashMap<String, String> journalMap = data.get("journal");
			if (journalMap != null) {
				int journalIndex = 0;
				for (Entry<String, String> entry : journalMap.entrySet()) {
					String value = entry.getValue();
					String[] parts = entry.getKey().split("\\.");
					if (parts.length < 0 || parts.length > 2) {
						errorManager.addError(ErrorType.LANGUAGE_FORMAT, "journal." + entry.getKey());
						continue;
					}
					// getting the right entry
					JournalEntry journalEntry = newByID(parts[0], name -> new JournalEntry(this, name));
					if (journalEntry.getIndex() < 0)
						journalEntry.setIndex(journalIndex++);
					// handling entry data
					if (parts.length == 2) {
						journalEntry.getText().setLang(parts[1], value);
					} else if (parts.length == 1) {
						journalEntry.getText().setDef(value);
					}
				}
			}
			// handling items.yml
			HashMap<String, String> itemsMap = data.get("items");
			if (itemsMap != null) {
				int itemIndex = 0;
				for (String key : itemsMap.keySet()) {
					if (key.contains(".")) {
						errorManager.addError(ErrorType.FORMAT_INCORRECT, "items." + key);
						continue;
					}
					Item item = newByID(key, name -> new Item(this, name));
					item.getInstruction().set(itemsMap.get(key));
					if (item.getIndex() < 0) {
						item.setIndex(itemIndex++);
					}
				}
			}
			// handling conditions.yml
			HashMap<String, String> conditionsMap = data.get("conditions");
			if (conditionsMap != null) {
				int conditionIndex = 0;
				for (String key : conditionsMap.keySet()) {
					if (key.contains(".")) {
						errorManager.addError(ErrorType.FORMAT_INCORRECT, "conditions." + key);
						continue;
					}
					Condition condition = newByID(key, name -> new Condition(this, name));
					condition.getInstruction().set(conditionsMap.get(key));
					if (condition.getIndex() < 0)
						condition.setIndex(conditionIndex++);
				}
			}
			// handling event.yml
			HashMap<String, String> eventsMap = data.get("events");
			if (eventsMap != null) {
				int eventIndex = 0;
				for (String key : eventsMap.keySet()) {
					if (key.contains(".")) {
						errorManager.addError(ErrorType.FORMAT_INCORRECT, "events." + key);
						continue;
					}
					Event event = newByID(key, name -> new Event(this, name));
					event.getInstruction().set(eventsMap.get(key));
					if (event.getIndex() < 0)
						event.setIndex(eventIndex++);
				}
			}
			// handling objectives.yml
			HashMap<String, String> objectivesMap = data.get("objectives");
			if (objectivesMap != null) {
				int objectiveIndex = 0;
				for (String key : objectivesMap.keySet()) {
					if (key.contains(".")) {
						errorManager.addError(ErrorType.FORMAT_INCORRECT, "objectives." + key);
						continue;
					}
					Objective objective = newByID(key, name -> new Objective(this, name));
					objective.getInstruction().set(objectivesMap.get(key));
					if (objective.getIndex() < 0)
						objective.setIndex(objectiveIndex++);
				} 
			}
			// handling conversations
			for (Entry<String, LinkedHashMap<String, String>> entry : data.entrySet()) {
				String key = entry.getKey();
				HashMap<String, String> value = entry.getValue();
				if (key.startsWith("conversations.")) {
					HashMap<String, String> convData = value;
					String convName = key.substring(14);
					Conversation conv = newByID(convName, name -> new Conversation(this, name));
					int playerIndex = 0;
					int npcIndex = 0;
					// handling conversation.yml
					for (Entry<String, String> subEntry : convData.entrySet()) {
						String subKey = subEntry.getKey();
						String subValue = subEntry.getValue();
						// reading NPC name, optionally in multiple languages
						if (subKey.equals("quester")) {
							conv.getText().setDef(subValue);
						} else if (subKey.startsWith("quester.")) {
							String lang = subKey.substring(8);
							if (lang.contains(".")) {
								errorManager.addError(ErrorType.LANGUAGE_FORMAT, key + "." + subKey);
								continue;
							}
							conv.getText().setLang(lang, subValue);
						}
						// reading the stop option
						else if (subKey.equals("stop")) {
							if (!subValue.equalsIgnoreCase("true") && !subValue.equalsIgnoreCase("false")) {
								errorManager.addError(ErrorType.STOP_WRONG, key);
							}
							conv.getStop().set(subValue.equalsIgnoreCase("true"));
						}
						// reading starting options
						else if (subKey.equals("first")) {
							String[] pointerNames = subValue.split(",");
							ArrayList<IdWrapper<NpcOption>> options = new ArrayList<>(pointerNames.length);
							for (int i = 0; i < pointerNames.length; i++) {
								IdWrapper<NpcOption> startingOption = new IdWrapper<>(this, conv.newNpcOption(pointerNames[i].trim()));
								options.add(i, startingOption);
								startingOption.setIndex(i); 
							}
							conv.getStartingOptions().addAll(options);
						}
						// reading final events
						else if (subKey.equals("final")) {
							String[] eventNames = subValue.split(",");
							ArrayList<IdWrapper<Event>> events = new ArrayList<>(eventNames.length);
							for (int i = 0; i < eventNames.length; i++) {
								try {
									IdWrapper<Event> finalEvent = new IdWrapper<>(this, newByID(eventNames[i].trim(), name -> new Event(this, name)));
									events.add(i, finalEvent);
									finalEvent.setIndex(i);
								} catch (PackageNotFoundException e) {
									errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + "." + subKey + ": '" + eventNames[i].trim() + "' (" + e.getPackage() + ")");
									continue;
								}
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
								if (option.getIndex() < 0) option.setIndex(npcIndex++);
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length == 4) {
											option.getText().setLang(parts[3], subValue);
										} else if (parts.length == 3){
											option.getText().setDef(subValue);
										} else {
											errorManager.addError(ErrorType.LANGUAGE_FORMAT, key + "." + subKey);
											continue;
										}
										break;
									case "event":
									case "events":
										String[] eventNames = subValue.split(",");
										ArrayList<IdWrapper<Event>> events = new ArrayList<>(eventNames.length);
										for (int i = 0; i < eventNames.length; i++) {
											try {
												IdWrapper<Event> event = new IdWrapper<>(this, newByID(eventNames[i].trim(), name -> new Event(this, name)));
												events.add(i, event);
												event.setIndex(i);
											} catch (PackageNotFoundException e) {
												errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + "." + subKey + ": '" + eventNames[i].trim() + "' (" + e.getPackage() + ")");
												continue;
											}
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditionNames = subValue.split(",");
										ArrayList<ConditionWrapper> conditions = new ArrayList<>(conditionNames.length);
										for (int i = 0; i < conditionNames.length; i++) {
											String name = conditionNames[i].trim();
											boolean negated = false;
											while (name.startsWith("!")) {
												name = name.substring(1, name.length());
												negated = true;
											}
											try {
												ConditionWrapper condition = new ConditionWrapper(this, newByID(name, idString -> new Condition(this, idString)));
												condition.setNegated(negated);
												conditions.add(i, condition);
												condition.setIndex(i);
											} catch (PackageNotFoundException e) {
												errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + "." + subKey + ": '" + conditionNames[i].trim() + "' (" + e.getPackage() + ")");
												continue;
											}
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointerNames = subValue.split(",");
										ArrayList<IdWrapper<ConversationOption>> options = new ArrayList<>(pointerNames.length);
										for (int i = 0; i < pointerNames.length; i++) {
											IdWrapper<ConversationOption> pointer = new IdWrapper<>(this, conv.newPlayerOption(pointerNames[i].trim()));
											options.add(i, pointer);
											pointer.setIndex(i);
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
								if (option.getIndex() < 0) {
									option.setIndex(playerIndex++);
								}
								if (parts.length > 2) {
									// getting specific values
									switch (parts[2]) {
									case "text":
										if (parts.length == 4) {
											option.getText().setLang(parts[3], subValue);
										} else if (parts.length == 3) {
											option.getText().setDef(subValue);
										} else {
											errorManager.addError(ErrorType.LANGUAGE_FORMAT, key + "." + subKey);
											continue;
										}
										break;
									case "event":
									case "events":
										String[] eventNames = subValue.split(",");
										ArrayList<IdWrapper<Event>> events = new ArrayList<>(eventNames.length);
										for (int i = 0; i < eventNames.length; i++) {
											try {
												IdWrapper<Event> event = new IdWrapper<>(this, newByID(eventNames[i].trim(), name -> new Event(this, name)));
												events.add(i, event);
												event.setIndex(i);
											} catch (PackageNotFoundException e) {
												errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + "." + subKey + ": '" + eventNames[i].trim() + "' (" + e.getPackage() + ")");
												continue;
											}
										}
										option.getEvents().addAll(events);
										break;
									case "condition":
									case "conditions":
										String[] conditionNames = subValue.split(",");
										ArrayList<ConditionWrapper> conditions = new ArrayList<>(conditionNames.length);
										for (int i = 0; i < conditionNames.length; i++) {
											String name = conditionNames[i].trim();
											boolean negated = false;
											while (name.startsWith("!")) {
												name = name.substring(1, name.length());
												negated = true;
											}
											try {
												ConditionWrapper condition = new ConditionWrapper(this, newByID(name, idString -> new Condition(this, idString)));
												condition.setNegated(negated);
												conditions.add(i, condition);
												condition.setIndex(i);
											} catch (PackageNotFoundException e) {
												errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + "." + subKey + ": '" + conditionNames[i].trim() + "' (" + e.getPackage() + ")");
												continue;
											}
										}
										option.getConditions().addAll(conditions);
										break;
									case "pointer":
									case "pointers":
										String[] pointerNames = subValue.split(",");
										ArrayList<IdWrapper<ConversationOption>> options = new ArrayList<>(pointerNames.length);
										for (int i = 0; i < pointerNames.length; i++) {
											IdWrapper<ConversationOption> pointer = new IdWrapper<>(this, conv.newNpcOption(pointerNames[i].trim()));
											options.add(i, pointer);
											pointer.setIndex(i);
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
			// handling main.yml
			LinkedHashMap<String, String> config = data.get("main");
			if (config != null) {
				for (Entry<String, String> entry : config.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					// handling variables
					if (key.startsWith("variables.")) {
						variables.add(new GlobalVariable(this, key.substring(10), value));
					}
					// handling global locations
					else if (key.equals("global_locations")) {
						for (String globLoc : value.split(",")) {
							try {
								locations.add(new GlobalLocation(newByID(globLoc, name -> new Objective(this, name))));
							} catch (PackageNotFoundException e) {
								errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + globLoc + "' (" + e.getPackage() + ")");
							}
						}
					}
					// handling static events
					else if (key.startsWith("static_events.")) {
						String time = key.substring(14);
						if (time.contains(".")) {
							errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
							continue;
						}
						StaticEvent staticEvent = new StaticEvent(this, time);
						try {
							staticEvent.getEvent().set(newByID(value, name -> new Event(this, name)));
							staticEvents.add(staticEvent);
						} catch (PackageNotFoundException e) {
							errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + value + "' (" + e.getPackage() + ")");
							continue;
						}
					}
					// handling NPC-conversation bindings
					else if (key.startsWith("npcs.")) {
						String npc = key.substring(5);
						if (npc.contains(".")) {
							errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
							continue;
						}
						try {
							npcBindings.add(new NpcBinding(this, npc,
									newByID(value, name -> new Conversation(this, name))));
						} catch (PackageNotFoundException e) {
							errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + value + "' (" + e.getPackage() +")");
							continue;
						}
					}
					// handling quest cancelers
					else if (key.startsWith("cancel.")) {
						String[] parts = key.split("\\.");
						if (parts.length > 1) {
							// getting the right canceler or creating new one
							QuestCanceler canceler = newByID(parts[1], name -> new QuestCanceler(this, name));
							// handling canceler properties
							if (parts.length > 2) {
								switch (parts[2]) {
								case "name":
									if (parts.length == 4) {
										canceler.getText().setLang(parts[3], value);
									} else if (parts.length == 3) {
										canceler.getText().setDef(value);
									} else {
										errorManager.addError(ErrorType.LANGUAGE_FORMAT, "main." + key);
										continue;
									}
									break;
								case "events":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] eventNames = value.split(",");
									ArrayList<IdWrapper<Event>> events = new ArrayList<>(eventNames.length);
									for (int i = 0; i < eventNames.length; i++) {
										try {
											IdWrapper<Event> event = new IdWrapper<>(this,
													newByID(eventNames[i].trim(), name -> new Event(this, name)));
											events.add(i, event);
											event.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + eventNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getEvents().addAll(events);
									break;
								case "conditions":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] conditionNames = value.split(",");
									ArrayList<ConditionWrapper> conditions = new ArrayList<>(conditionNames.length);
									for (int i = 0; i < conditionNames.length; i++) {
										String name = conditionNames[i].trim();
										boolean negated = false;
										while (name.startsWith("!")) {
											name = name.substring(1, name.length());
											negated = true;
										}
										try {
											ConditionWrapper condition = new ConditionWrapper(this,
													newByID(name, idString -> new Condition(this, idString)));
											condition.setNegated(negated);
											conditions.add(i, condition);
											condition.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + conditionNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getConditions().addAll(conditions);
									break;
								case "objectives":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] objectiveNames = value.split(",");
									ArrayList<IdWrapper<Objective>> objectives = new ArrayList<>(objectiveNames.length);
									for (int i = 0; i < objectiveNames.length; i++) {
										try {
											IdWrapper<Objective> wrapper = new IdWrapper<>(this,
													newByID(objectiveNames[i].trim(), name -> new Objective(this, name)));
											objectives.add(i, wrapper);
											wrapper.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + objectiveNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getObjectives().addAll(objectives);
									break;
								case "tags":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] tagNames = value.split(",");
									ArrayList<IdWrapper<Tag>> tags = new ArrayList<>(tagNames.length);
									for (int i = 0; i < tagNames.length; i++) {
										try {
											IdWrapper<Tag> wrapper = new IdWrapper<>(this,
													newByID(tagNames[i].trim(), name -> new Tag(this, name)));
											tags.add(i, wrapper);
											wrapper.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + tagNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getTags().addAll(tags);
									break;
								case "points":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] pointNames = value.split(",");
									ArrayList<IdWrapper<PointCategory>> points = new ArrayList<>(pointNames.length);
									for (int i = 0; i < pointNames.length; i++) {
										try {
											IdWrapper<PointCategory> wrapper = new IdWrapper<>(this,
													newByID(pointNames[i].trim(), name -> new PointCategory(this, name)));
											points.add(i, wrapper);
											wrapper.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + pointNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getPoints().addAll(points);
									break;
								case "journal":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] journalNames = value.split(",");
									ArrayList<IdWrapper<JournalEntry>> journal = new ArrayList<>(journalNames.length);
									for (int i = 0; i < journalNames.length; i++) {
										try {
											IdWrapper<JournalEntry> wrapper = new IdWrapper<>(this,
													newByID(journalNames[i].trim(), name -> new JournalEntry(this, name)));
											journal.add(i, wrapper);
											wrapper.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, "main." + key + ": '" + journalNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									canceler.getJournal().addAll(journal);
									break;
								case "loc":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									canceler.setLocation(value);
									break;
								}
							}
						}
					}
					// handling journal main page
					else if (key.startsWith("journal_main_page.")) {
						String[] parts = key.split("\\.");
						if (parts.length > 1) {
							MainPageLine line = newByID(parts[1], name -> new MainPageLine(this, name));
							if (parts.length > 2) {
								switch (parts[2]) {
								case "text":
									if (parts.length == 4) {
										line.getText().setLang(parts[3], value);
									} else if (parts.length == 3) {
										line.getText().setDef(value);
									} else {
										errorManager.addError(ErrorType.LANGUAGE_FORMAT, "main." + key);
										continue;
									}
									break;
								case "priority":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									try {
										line.getPriority().set(Integer.parseInt(value));
									} catch (NumberFormatException e) {
										errorManager.addError(ErrorType.PRIORITY_WRONG, "main." + key);
										continue;
									}
									break;
								case "conditions":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									String[] conditionNames = value.split(",");
									ArrayList<ConditionWrapper> conditions = new ArrayList<>(conditionNames.length);
									for (int i = 0; i < conditionNames.length; i++) {
										String name = conditionNames[i].trim();
										boolean negated = false;
										while (name.startsWith("!")) {
											name = name.substring(1, name.length());
											negated = true;
										}
										try {
											ConditionWrapper condition = new ConditionWrapper(this,
													newByID(name, idString -> new Condition(this, idString)));
											condition.setNegated(negated);
											conditions.add(i, condition);
											condition.setIndex(i);
										} catch (PackageNotFoundException e) {
											errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + ": '" + conditionNames[i].trim() + "' (" + e.getPackage() + ")");
											continue;
										}
									}
									line.getConditions().addAll(conditions);
									break;
								}
							}
						}
					}
					// handle compass targets
					else if (key.startsWith("compass.")) {
						String[] parts = key.split("\\.");
						if (parts.length > 1) {
							CompassTarget target = newByID(parts[1], name -> new CompassTarget(this, name));
							if (parts.length > 2) {
								switch (parts[2]) {
								case "name":
									if (parts.length == 4) {
										target.getText().setLang(parts[3], value);
									} else if (parts.length == 3) {
										target.getText().setDef(value);
									} else {
										errorManager.addError(ErrorType.LANGUAGE_FORMAT, "main." + key);
										continue;
									}
									break;
								case "location":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									target.setLocation(value);
									break;
								case "item":
									if (parts.length > 3) {
										errorManager.addError(ErrorType.FORMAT_INCORRECT, "main." + key);
										continue;
									}
									try {
										Item item = newByID(value, name -> new Item(this, name));
										target.getItem().set(item);
									} catch (PackageNotFoundException e) {
										errorManager.addError(ErrorType.PACKAGE_NOT_FOUND, key + ": '" + value + "' (" + e.getPackage() + ")");
										continue;
									}
									break;
								}
							}
						}
					}
					// handling default language
					else if (key.equalsIgnoreCase("default_language")) {
						translationManager.setDefault(value);
					}
				} 
			}
			extractTagsAndPoints();
			// get language
			translationManager.inferDefaultLanguage();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public void extractTagsAndPoints() {
		// extract tags and points
		for (Condition condition : conditions) {
			if (condition.getInstruction().get() == null) {
				continue;
			}
			if (condition.getInstruction().get().startsWith("tag ")) {
				String[] parts = condition.getInstruction().get().split(" ");
				if (parts.length > 1) {
					try {
						newByID(parts[1], name -> new Tag(this, name));
					} catch (PackageNotFoundException e) {
						continue;
					}
				}
			} else if (condition.getInstruction().get().startsWith("point ")) {
				String[] parts = condition.getInstruction().get().split(" ");
				if (parts.length > 1) {
					try {
						newByID(parts[1], name -> new PointCategory(this, name));
					} catch (PackageNotFoundException e) {
						continue;
					}
				}
			}
		}
		for (Event event : events) {
			if (event.getInstruction().get() == null) {
				continue;
			}
			if (event.getInstruction().get().startsWith("tag ")) {
				String[] parts = event.getInstruction().get().split(" ");
				if (parts.length > 2) {
					try {
						newByID(parts[2], name -> new Tag(this, name));
					} catch (PackageNotFoundException e) {
						continue;
					}
				}
			} else if (event.getInstruction().get().startsWith("point ")) {
				String[] parts = event.getInstruction().get().split(" ");
				if (parts.length > 1) {
					try {
						newByID(parts[1], name -> new PointCategory(this, name));
					} catch (PackageNotFoundException e) {
						continue;
					}
				}
			}
		}
	}

	@Override
	public boolean edit() {
		return NameEditController.display(packName);
	}

	@Override
	public boolean needsEditing() {
		return packName == null || packName.get() == null || packName.get().isEmpty();
	}
	
	public PackageSet getSet() {
		return set;
	}

	public StringProperty getName() {
		return packName;
	}
	
	public TranslationManager getTranslationManager() {
		return translationManager;
	}
	
	public ErrorManager getErrorManager() {
		return errorManager;
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

	public ObservableList<MainPageLine> getMainPage() {
		return mainPage;
	}

	public ObservableList<CompassTarget> getCompassTargets() {
		return compass;
	}
	
	public ObservableList<Tag> getTags() {
		return tags;
	}
	
	public ObservableList<PointCategory> getPoints() {
		return points;
	}
	
	/**
	 * @return all NpcOptions from loaded conversations in this package, the ones from current conversation first
	 */
	public ObservableList<NpcOption> getAllNpcOptions() {
		ObservableList<NpcOption> list = FXCollections.observableArrayList();
		list.addAll(ConversationController.getDisplayedConversation().getNpcOptions());
		conversations.forEach(conv -> {
			if (!conv.equals(ConversationController.getDisplayedConversation())) {
				list.addAll(conv.getNpcOptions());
			}
		});
		return list;
	}
	
	/**
	 * @return all PlayerOptions from loaded conversations in this package, the ones from current conversation first
	 */
	public ObservableList<PlayerOption> getAllPlayerOptions() {
		ObservableList<PlayerOption> list = FXCollections.observableArrayList();
		list.addAll(ConversationController.getDisplayedConversation().getPlayerOptions());
		conversations.forEach(conv -> {
			if (!conv.equals(ConversationController.getDisplayedConversation())) {
				list.addAll(conv.getPlayerOptions());
			}
		});
		return list;
	}

	public void sort() {
		// sort lists
		ArrayList<ObservableList<? extends ID>> lists = new ArrayList<>();
		lists.add(conversations);
		lists.add(events);
		lists.add(conditions);
		lists.add(objectives);
		lists.add(items);
		lists.add(journal);
		lists.add(staticEvents);
		lists.add(variables);
		lists.add(npcBindings);
		lists.add(mainPage);
		for (Conversation conv : conversations) {
			lists.add(conv.getNpcOptions());
			lists.add(conv.getPlayerOptions());
			lists.add(conv.getStartingOptions());
			lists.add(conv.getFinalEvents());
			ArrayList<ConversationOption> list = new ArrayList<>(conv.getNpcOptions());
			list.addAll(conv.getPlayerOptions());
			for (ConversationOption option : list) {
				lists.add(option.getConditions());
				lists.add(option.getEvents());
				lists.add(option.getPointers());
			}
		}
		for (ObservableList<? extends ID> list : lists) {
			list.sort((ID o1, ID o2) -> o1.getIndex() - o2.getIndex());
			int index = 0;
			for (ID object : list) {
				object.setIndex(index++);
			}
		}
	}

	@Override
	public String toString() {
		return packName.get();
	}
	
	public <T extends ID> T newByID(String id, Generator<T> generator) throws PackageNotFoundException {
		T object = generator.generate(id);
		ObservableList<T> list = object.getList();
		T existing = null;
		for (T check : list) {
			if (check.getId().get().equals(id)) {
				existing = check;
				break;
			}
		}
		if (existing == null) {
			existing = object;
			list.add(existing);
		} else {
			if (object instanceof Translatable) {
				translationManager.getTranslations().remove(((Translatable) object).getText());
			}
		}
		return existing;
	}
	
	public interface Generator<T> {
		public T generate(String id) throws PackageNotFoundException;
	}
	
	public void printMainYAML(OutputStream out) throws IOException {
		YAMLFactory yf = new YAMLFactory();
		YAMLMapper mapper = new YAMLMapper();
		ObjectNode root = mapper.createObjectNode();
		// save NPCs
		if (!npcBindings.isEmpty()) {
			ObjectNode npcs = mapper.createObjectNode();
			for (NpcBinding binding : npcBindings) {
				npcs.put(binding.getId().get(), binding.getConversation().get().getRelativeName(this));
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
				staticEvents.put(event.getId().get(), event.getEvent().get().getRelativeName(this));
			}
			root.set("static", staticEvents);
		}
		// save global locations
		if (!locations.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (GlobalLocation loc : locations) {
				builder.append(loc.getObjective().get().getRelativeName(this) + ",");
			}
			root.put("global_locations", builder.toString().substring(0, builder.length() - 1));
		}
		// save quest cancelers
		if (!cancelers.isEmpty()) {
			ObjectNode cancelers = mapper.createObjectNode();
			for (QuestCanceler canceler : this.cancelers) {
				ObjectNode cancelerNode = mapper.createObjectNode();
				addTranslatedNode(mapper, cancelerNode, "name", canceler.getText());
				if (!canceler.getEvents().isEmpty()) {
					StringBuilder events = new StringBuilder();
					for (IdWrapper<Event> event : canceler.getEvents()) {
						events.append(event.getRelativeName(this) + ',');
					}
					cancelerNode.put("events", events.toString().substring(0, events.length() - 1));
				}
				if (!canceler.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (ConditionWrapper condition : canceler.getConditions()) {
						conditions.append(condition.getRelativeName(this) + ',');
					}
					cancelerNode.put("conditions", conditions.toString().substring(0, conditions.length() - 1));
				}
				if (!canceler.getObjectives().isEmpty()) {
					StringBuilder objectives = new StringBuilder();
					for (IdWrapper<Objective> objective : canceler.getObjectives()) {
						objectives.append(objective.getRelativeName(this) + ',');
					}
					cancelerNode.put("objectives", objectives.toString().substring(0, objectives.length() - 1));
				}
				if (!canceler.getTags().isEmpty()) {
					StringBuilder tags = new StringBuilder();
					for (IdWrapper<Tag> tag : canceler.getTags()) {
						tags.append(tag.getRelativeName(this) + ',');
					}
					cancelerNode.put("tags", tags.toString().substring(0, tags.length() - 1));
				}
				if (!canceler.getPoints().isEmpty()) {
					StringBuilder points = new StringBuilder();
					for (IdWrapper<PointCategory> point : canceler.getPoints()) {
						points.append(point.getRelativeName(this) + ',');
					}
					cancelerNode.put("points", points.toString().substring(0, points.length() - 1));
				}
				if (!canceler.getJournal().isEmpty()) {
					StringBuilder journals = new StringBuilder();
					for (IdWrapper<JournalEntry> journal : canceler.getJournal()) {
						journals.append(journal.getRelativeName(this) + ',');
					}
					cancelerNode.put("journal", journals.toString().substring(0, journals.length() - 1));
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
				for (ConditionWrapper condition : line.getConditions()) {
					conditions.append(condition.getRelativeName(this) + ',');
				}
				node.put("conditions", conditions.substring(0, conditions.length() - 1));
				lines.set(line.getId().get(), node);
			}
			root.set("journal_main_page", lines);
		}
		// save compass targets
		if (!compass.isEmpty()) {
			ObjectNode targets = mapper.createObjectNode();
			for (CompassTarget target : compass) {
				ObjectNode node = mapper.createObjectNode();
				addTranslatedNode(mapper, node, "name", target.getText());
				node.put("location", target.getLocation());
				node.put("item", target.getItem().get().getRelativeName(this));
				targets.set(target.getId().get(), node);
			}
			root.set("compass", targets);
		}
		// save default language
		if (translationManager.getDefault() != null) {
			root.put("default_language", translationManager.getDefault());
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
		addTranslatedNode(mapper, root, "quester", conv.getText());
		root.put("stop", String.valueOf(conv.getStop().get()));
		StringBuilder first = new StringBuilder();
		for (IdWrapper<NpcOption> option : conv.getStartingOptions()) {
			first.append(option.get().getRelativeOptionName(conv) + ',');
		}
		if (first.length() > 0) {
			root.put("first", first.substring(0, first.length() - 1));
		}
		if (!conv.getFinalEvents().isEmpty()) {
			StringBuilder finalEvents = new StringBuilder();
			for (IdWrapper<Event> event : conv.getFinalEvents()) {
				finalEvents.append(event.getRelativeName(this) + ',');
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
					for (IdWrapper<Event> event : option.getEvents()) {
						events.append(event.getRelativeName(this) + ',');
					}
					npcOption.put("events", events.substring(0, events.length() - 1));
				}
				if (!option.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (ConditionWrapper condition : option.getConditions()) {
						conditions.append(condition.getRelativeNegatedName(this) + ',');
					}
					npcOption.put("conditions", conditions.substring(0, conditions.length() - 1));
				}
				if (!option.getPointers().isEmpty()) {
					StringBuilder pointers = new StringBuilder();
					for (IdWrapper<ConversationOption> pointer : option.getPointers()) {
						pointers.append(pointer.get().getRelativeOptionName(conv) + ',');
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
					for (IdWrapper<Event> event : option.getEvents()) {
						events.append(event.getRelativeName(this) + ',');
					}
					playerOption.put("events", events.substring(0, events.length() - 1));
				}
				if (!option.getConditions().isEmpty()) {
					StringBuilder conditions = new StringBuilder();
					for (ConditionWrapper condition : option.getConditions()) {
						conditions.append(condition.getRelativeNegatedName(this) + ',');
					}
					playerOption.put("conditions", conditions.substring(0, conditions.length() - 1));
				}
				if (!option.getPointers().isEmpty()) {
					StringBuilder pointers = new StringBuilder();
					for (IdWrapper<ConversationOption> pointer : option.getPointers()) {
						pointers.append(pointer.get().getRelativeOptionName(conv) + ',');
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
		if (!text.hasMultipleLanguages()) {
			root.put(name, text.getDef().get());
		} else {
			ObjectNode node = mapper.createObjectNode();
			for (String lang : text.getLanguages()) {
				if (lang == null) {
					continue;
				}
				node.put(lang, text.getLang(lang).get());
			}
			root.set(name, node);
		}
	}

}
