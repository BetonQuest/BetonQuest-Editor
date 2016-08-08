/**
 * 
 */
package pl.betoncraft.betonquest.editor.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.DraggableListCell;
import pl.betoncraft.betonquest.editor.model.Condition;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.Objective;

/**
 * Controls "Events, conditions and objectives" tab.
 *
 * @author Jakub Sapalski
 */
public class EcoController {
	
	private static EcoController instance;
	
	@FXML private ListView<Event> eventsList;
	@FXML private ListView<Condition> conditionsList;
	@FXML private ListView<Objective> objectivesList;
	
	public EcoController() {
		instance = this;
	}

	public static void setEvents(ObservableList<Event> events) {
		instance.eventsList.setCellFactory(param -> new DraggableListCell<>());
		instance.eventsList.setItems(events);
	}
	
	public static void setConditions(ObservableList<Condition> conditions) {
		instance.conditionsList.setCellFactory(param -> new DraggableListCell<>());
		instance.conditionsList.setItems(conditions);
	}
	
	public static void setObjectives(ObservableList<Objective> objectives) {
		instance.objectivesList.setCellFactory(param -> new DraggableListCell<>());
		instance.objectivesList.setItems(objectives);
	}
	
	@FXML private void addEvent() {
		Event event = new Event("new_event");
		event.setIndex(eventsList.getItems().size());
		eventsList.getItems().add(event);
		InstructionEditController.display(event);
	}
	
	@FXML private void addCondition() {
		Condition condition = new Condition("new_condition");
		condition.setIndex(conditionsList.getItems().size());
		conditionsList.getItems().add(condition);
		InstructionEditController.display(condition);
	}
	
	@FXML private void addObjective() {
		Objective objective = new Objective("new_objective");
		objective.setIndex(objectivesList.getItems().size());
		objectivesList.getItems().add(objective);
		InstructionEditController.display(objective);
	}
	
	@FXML private void editEvent() {
		Event event = eventsList.getSelectionModel().getSelectedItem();
		if (event != null) {
			InstructionEditController.display(event);
		}
	}
	
	@FXML private void editCondition() {
		Condition condition = conditionsList.getSelectionModel().getSelectedItem();
		if (condition != null) {
			InstructionEditController.display(condition);
		}
	}
	
	@FXML private void editObjective() {
		Objective objective = objectivesList.getSelectionModel().getSelectedItem();
		if (objective != null) {
			InstructionEditController.display(objective);
		}
	}
	
	@FXML private void delEvent() {
		Event event = eventsList.getSelectionModel().getSelectedItem();
		if (event != null) {
			eventsList.getItems().remove(event);
		}
		BetonQuestEditor.refresh();
	}
	
	@FXML private void delCondition() {
		Condition condition = conditionsList.getSelectionModel().getSelectedItem();
		if (condition != null) {
			conditionsList.getItems().remove(condition);
		}
		BetonQuestEditor.refresh();
	}
	
	@FXML private void delObjective() {
		Objective objective = objectivesList.getSelectionModel().getSelectedItem();
		if (objective != null) {
			objectivesList.getItems().remove(objective);
		}
		BetonQuestEditor.refresh();
	}
	
}
