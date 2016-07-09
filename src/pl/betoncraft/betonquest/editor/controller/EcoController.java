/**
 * 
 */
package pl.betoncraft.betonquest.editor.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
		instance.eventsList.setItems(events);
	}
	
	public static void setConditions(ObservableList<Condition> conditions) {
		instance.conditionsList.setItems(conditions);
	}
	
	public static void setObjectives(ObservableList<Objective> objectives) {
		instance.objectivesList.setItems(objectives);
	}
	
}
