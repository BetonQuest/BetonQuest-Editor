/**
 * 
 */
package pl.betoncraft.betonquest.editor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.DraggableListCell;
import pl.betoncraft.betonquest.editor.data.Instruction;
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

	@FXML Label instruction;
	
	public EcoController() {
		instance = this;
	}

	/**
	 * Sets the events in the ListView
	 * 
	 * @param events list of events in the package
	 */
	public static void setEvents(ObservableList<Event> events) {
		instance.eventsList.setCellFactory(param -> new DraggableListCell<>());
		instance.eventsList.setItems(events);
		instance.eventsList.getSelectionModel().selectedItemProperty().addListener(event -> {
			instance.update();
		});
		instance.eventsList.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				instance.editEvent();
			}
		});
		instance.update();
	}
	
	/**
	 * Sets the conditions in the ListView
	 * 
	 * @param conditions list of conditions in the package
	 */
	public static void setConditions(ObservableList<Condition> conditions) {
		instance.conditionsList.setCellFactory(param -> new DraggableListCell<>());
		instance.conditionsList.setItems(conditions);
		instance.conditionsList.getSelectionModel().selectedItemProperty().addListener(event -> {
			instance.update();
		});
		instance.conditionsList.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				instance.editCondition();
			}
		});
		instance.update();
	}
	
	/**
	 * Sets the objectives in the ListView
	 * 
	 * @param objectives list of objectives in the package
	 */
	public static void setObjectives(ObservableList<Objective> objectives) {
		instance.objectivesList.setCellFactory(param -> new DraggableListCell<>());
		instance.objectivesList.setItems(objectives);
		instance.objectivesList.getSelectionModel().selectedItemProperty().addListener(event -> {
			instance.update();
		});
		instance.objectivesList.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				instance.editObjective();
			}
		});
		instance.update();
	}
	
	@FXML private void update() {
		try {
			Node focused = instruction.getScene().getFocusOwner();
			if (focused instanceof ListView<?>) {
				ListView<?> list = (ListView<?>) focused;
				Object object = list.getSelectionModel().getSelectedItem();
				if (object != null && object instanceof Instruction) {
					Instruction item = (Instruction) object;
					String name = null;
					switch (item.getClass().getSimpleName()) {
					case "Event":
						name = BetonQuestEditor.getInstance().getLanguage().getString("event");
						break;
					case "Condition":
						name = BetonQuestEditor.getInstance().getLanguage().getString("condition");
						break;
					case "Objective":
						name = BetonQuestEditor.getInstance().getLanguage().getString("objective");
						break;
					default:
						name = null;
						break;
					}
					if (name != null) {
						instruction.setText(name + " '" + item.getId().get() + "': " + item.getInstruction().get());
					} else {
						instruction.setText(BetonQuestEditor.getInstance().getLanguage().getString("instruction"));
					}
				}
			} else {
				instruction.setText(BetonQuestEditor.getInstance().getLanguage().getString("instruction"));
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addEvent() {
		try {
			Event event = new Event(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (event.edit()) {
				for (Event other : eventsList.getItems()) {
					if (event.getId().get().equals(other.getId().get())) {
						BetonQuestEditor.showError("event-exists");
						return;
					}
				}
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
				for (Condition other : conditionsList.getItems()) {
					if (condition.getId().get().equals(other.getId().get())) {
						BetonQuestEditor.showError("condition-exists");
						return;
					}
				}
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
				for (Objective other : objectivesList.getItems()) {
					if (objective.getId().get().equals(other.getId().get())) {
						BetonQuestEditor.showError("objective-exists");
						return;
					}
				}
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
				eventsList.getSelectionModel().select(event);
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
				conditionsList.getSelectionModel().select(condition);
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
				objectivesList.getSelectionModel().select(objective);
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
	
	private void keyAction(KeyEvent event, Action add, Action edit, Action delete) {
		if (event.getCode() == KeyCode.DELETE) {
			if (delete != null) {
				delete.act();
			}
			event.consume();
			return;
		}
		if (event.getCode() == KeyCode.ENTER) {
			if (edit != null) {
				edit.act();
			}
			event.consume();
			return;
		}
		KeyCombination combintation = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
		if (combintation.match(event)) {
			if (add != null) {
				add.act();
			}
			event.consume();
			return;
		}
	}
	
	private interface Action {
		void act();
	}

	@FXML public void eventKey(KeyEvent event) {
		try {
			keyAction(event, () -> addEvent(), () -> editEvent(), () -> delEvent());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void conditionKey(KeyEvent event) {
		try {
			keyAction(event, () -> addCondition(), () -> editCondition(), () -> delCondition());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void objectiveKey(KeyEvent event) {
		try {
			keyAction(event, () -> addObjective(), () -> editObjective(), () -> delObjective());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	/**
	 * Clears the tab.
	 */
	public static void clear() {
		instance.eventsList.setItems(FXCollections.observableArrayList());
		instance.conditionsList.setItems(FXCollections.observableArrayList());
		instance.objectivesList.setItems(FXCollections.observableArrayList());
	}
	
}
