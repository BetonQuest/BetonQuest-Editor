/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2016  Jakub "Co0sh" Sapalski
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

package pl.betoncraft.betonquest.editor.controller;

import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.ConditionListCell;
import pl.betoncraft.betonquest.editor.custom.DraggableListCell;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.model.Condition;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.JournalEntry;
import pl.betoncraft.betonquest.editor.model.Objective;
import pl.betoncraft.betonquest.editor.model.PointCategory;
import pl.betoncraft.betonquest.editor.model.QuestCanceler;
import pl.betoncraft.betonquest.editor.model.Tag;

/**
 * Controls the quest canceler edit window.
 *
 * @author Jakub Sapalski
 */
public class QuestCancelerEditController {
	
	private Stage stage;
	private boolean result = false;
	private QuestCanceler canceler;
	
	private ObservableList<ConditionWrapper> conditionList;
	private ObservableList<IdWrapper<Event>> eventList;
	private ObservableList<IdWrapper<Tag>> tagList;
	private ObservableList<IdWrapper<PointCategory>> pointList;
	private ObservableList<IdWrapper<Objective>> objectiveList;
	private ObservableList<IdWrapper<JournalEntry>> entryList;

	@FXML private Pane root;
	@FXML private TextField name;
	@FXML private TextField text;
	@FXML private Button conditions;
	@FXML private Button events;
	@FXML private Button tags;
	@FXML private Button points;
	@FXML private Button objectives;
	@FXML private Button entries;
	@FXML private TextField teleport;
	
	private void refresh() {
		ResourceBundle lang = BetonQuestEditor.getInstance().getLanguage();
		conditions.setText(lang.getString("conditions") + " (" + conditionList.size() + ")");
		events.setText(lang.getString("events") + " (" + eventList.size() + ")");
		tags.setText(lang.getString("tags") + " (" + tagList.size() + ")");
		points.setText(lang.getString("point-categories") + " (" + pointList.size() + ")");
		objectives.setText(lang.getString("objectives") + " (" + objectiveList.size() + ")");
		entries.setText(lang.getString("journal-entries") + " (" + entryList.size() + ")");
	}

	@FXML private void condition() {
		try {
			SortedChoiceController.display("conditions", conditionList, BetonQuestEditor.getInstance().getAllConditions(),
					name -> new Condition(canceler.getPack(), name), () -> new ConditionListCell(),
					item -> new ConditionWrapper(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void event() {
		try {
			SortedChoiceController.display("events", eventList, BetonQuestEditor.getInstance().getAllEvents(),
					name -> new Event(canceler.getPack(), name), () -> new DraggableListCell<>(),
					item -> new IdWrapper<>(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void tag() {
		try {
			SortedChoiceController.display("tags", tagList, BetonQuestEditor.getInstance().getAllTags(),
					name -> new Tag(canceler.getPack(), name), () -> new DraggableListCell<>(),
					item -> new IdWrapper<>(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void point() {
		try {
			SortedChoiceController.display("point-categories", pointList, BetonQuestEditor.getInstance().getAllPoints(),
					name -> new PointCategory(canceler.getPack(), name), () -> new DraggableListCell<>(),
					item -> new IdWrapper<>(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void objective() {
		try {
			SortedChoiceController.display("objectives", objectiveList, BetonQuestEditor.getInstance().getAllObjectives(),
					name -> new Objective(canceler.getPack(), name), () -> new DraggableListCell<>(),
					item -> new IdWrapper<>(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void entry() {
		try {
			SortedChoiceController.display("journal-entries", entryList, BetonQuestEditor.getInstance().getAllEntries(),
					name -> new JournalEntry(canceler.getPack(), name), () -> new DraggableListCell<>(),
					item -> new IdWrapper<>(canceler.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void ok() {
		try {
			String idString = name.getText();
			if (idString == null || idString.isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			String text = this.text.getText();
			if (text == null) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			canceler.getId().set(idString.trim());
			canceler.getText().get().set(text);
			canceler.getConditions().setAll(conditionList);
			canceler.getEvents().setAll(eventList);
			canceler.getTags().setAll(tagList);
			canceler.getPoints().setAll(pointList);
			canceler.getObjectives().setAll(objectiveList);
			canceler.getJournal().setAll(entryList);
			String loc = teleport.getText();
			canceler.setLocation(loc == null || loc.isEmpty() ? null : loc);
			BetonQuestEditor.getInstance().refresh();
			result = true;
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void cancel() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	public static boolean display(QuestCanceler canceler) {
		try {
			QuestCancelerEditController controller = (QuestCancelerEditController) BetonQuestEditor
					.createWindow("view/window/QuestCancelerEditWindow.fxml", "edit-quest-canceler", 400, 400);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.canceler = canceler;
			controller.conditionList = FXCollections.observableArrayList();
			canceler.getConditions().forEach(condition -> {
				ConditionWrapper wrapper = new ConditionWrapper(condition.getPack(), condition.get());
				wrapper.setIndex(controller.conditionList.size());
				controller.conditionList.add(wrapper);
			});
			controller.eventList = FXCollections.observableArrayList();
			canceler.getEvents().forEach(object -> {
				IdWrapper<Event> wrapper = new IdWrapper<>(object.getPack(), object.get());
				wrapper.setIndex(controller.eventList.size());
				controller.eventList.add(wrapper);
			});
			controller.tagList = FXCollections.observableArrayList();
			canceler.getTags().forEach(object -> {
				IdWrapper<Tag> wrapper = new IdWrapper<>(object.getPack(), object.get());
				wrapper.setIndex(controller.tagList.size());
				controller.tagList.add(wrapper);
			});
			controller.pointList = FXCollections.observableArrayList();
			canceler.getPoints().forEach(object -> {
				IdWrapper<PointCategory> wrapper = new IdWrapper<>(object.getPack(), object.get());
				wrapper.setIndex(controller.pointList.size());
				controller.pointList.add(wrapper);
			});
			controller.objectiveList = FXCollections.observableArrayList();
			canceler.getObjectives().forEach(object -> {
				IdWrapper<Objective> wrapper = new IdWrapper<>(object.getPack(), object.get());
				wrapper.setIndex(controller.objectiveList.size());
				controller.objectiveList.add(wrapper);
			});
			controller.entryList = FXCollections.observableArrayList();
			canceler.getJournal().forEach(object -> {
				IdWrapper<JournalEntry> wrapper = new IdWrapper<>(object.getPack(), object.get());
				wrapper.setIndex(controller.entryList.size());
				controller.entryList.add(wrapper);
			});
			controller.name.setText(canceler.toString());
			controller.text.setText(canceler.getText().get().get());
			controller.teleport.setText(canceler.getLocation());
			controller.refresh();
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					controller.cancel();
					event.consume();
					return;
				}
				if (event.getCode() == KeyCode.ENTER) {
					controller.ok();
					event.consume();
					return;
				}
			});
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
