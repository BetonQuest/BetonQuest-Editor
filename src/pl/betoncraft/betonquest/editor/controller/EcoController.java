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
		try {
			Event event = new Event(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (event.edit()) {
				event.setIndex(eventsList.getItems().size());
				eventsList.getItems().add(event);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addCondition() {
		try {
			Condition condition = new Condition(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (condition.edit()) {
				condition.setIndex(conditionsList.getItems().size());
				conditionsList.getItems().add(condition);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addObjective() {
		try {
			Objective objective = new Objective(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (objective.edit()) {
				objective.setIndex(objectivesList.getItems().size());
				objectivesList.getItems().add(objective);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editEvent() {
		try {
			Event event = eventsList.getSelectionModel().getSelectedItem();
			if (event != null) {
				event.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editCondition() {
		try {
			Condition condition = conditionsList.getSelectionModel().getSelectedItem();
			if (condition != null) {
				condition.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editObjective() {
		try {
			Objective objective = objectivesList.getSelectionModel().getSelectedItem();
			if (objective != null) {
				objective.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delEvent() {
		try {
			Event event = eventsList.getSelectionModel().getSelectedItem();
			if (event != null) {
				eventsList.getItems().remove(event);
			}
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delCondition() {
		try {
			Condition condition = conditionsList.getSelectionModel().getSelectedItem();
			if (condition != null) {
				conditionsList.getItems().remove(condition);
			}
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delObjective() {
		try {
			Objective objective = objectivesList.getSelectionModel().getSelectedItem();
			if (objective != null) {
				objectivesList.getItems().remove(objective);
			}
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
}
