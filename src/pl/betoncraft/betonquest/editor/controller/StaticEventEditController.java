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

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.StaticEvent;

/**
 * Controls static event editing window.
 *
 * @author Jakub Sapalski
 */
public class StaticEventEditController {
	
	private Stage stage;
	private StaticEvent event;
	private boolean result = false;
	
	@FXML private Pane root;
	@FXML private ChoiceBox<Integer> hours;
	@FXML private ChoiceBox<Integer> minutes;
	@FXML private ChoiceBox<Event> events;
	
	@FXML private void add() {
		String time = hours.getValue() + ":" + minutes.getValue();
		Event chosen = events.getValue();
		if (time == null || time.isEmpty()) {
			BetonQuestEditor.showError("name-not-null");
			return;
		}
		event.getId().set(time);
		event.getEvent().set(chosen);
		BetonQuestEditor.getInstance().refresh();
		result = true;
		stage.close();
	}
	
	@FXML private void cancel() {
		stage.close();
	}
	
	public static boolean display(StaticEvent event) {
		try {
			StaticEventEditController controller = (StaticEventEditController) BetonQuestEditor
					.createWindow("view/window/StaticEventEditWindow.fxml", "edit-static-event", 300, 175);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.event = event;
			String time = event.getId().get();
			int hour = 0;
			int minute = 0;
			if (time.contains(":")) {
				String[] parts = time.split(":");
				if (parts.length == 2) {
					try {
						hour = Integer.parseInt(parts[0]);
						minute = Integer.parseInt(parts[1]);
						if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						hour = 0;
						minute = 0;
					}
				}
			}
			for (int i = 0; i < 24; i++) {
				controller.hours.getItems().add(i);
			}
			for (int i = 0; i < 60; i++) {
				controller.minutes.getItems().add(i);
			}
			controller.hours.getSelectionModel().select(hour);
			controller.minutes.getSelectionModel().select(minute);
			controller.events.getItems().addAll(BetonQuestEditor.getInstance().getAllEvents());
			if (controller.events.getItems().contains(event.getEvent().get())) {
				controller.events.getSelectionModel().select(event.getEvent().get());
			}
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
