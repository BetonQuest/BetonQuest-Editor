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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
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
	
	@FXML private ChoiceBox<Integer> hours;
	@FXML private ChoiceBox<Integer> minutes;
	@FXML private ChoiceBox<Event> events;
	
	private void setData(Stage stage, StaticEvent event) {
		this.stage = stage;
		this.event = event;
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
			hours.getItems().add(i);
		}
		for (int i = 0; i < 60; i++) {
			minutes.getItems().add(i);
		}
		hours.getSelectionModel().select(hour);
		minutes.getSelectionModel().select(minute);
		events.getItems().addAll(BetonQuestEditor.getInstance().getAllEvents());
		if (events.getItems().contains(event.getEvent().get())) {
			events.getSelectionModel().select(event.getEvent().get());
		}
	}
	
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
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/StaticEventEditWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			GridPane root = (GridPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("edit-static-event"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(175);
			window.setWidth(300);
			window.setResizable(false);
			StaticEventEditController controller = (StaticEventEditController) fxmlLoader.getController();
			controller.setData(window, event);
			window.showAndWait();
			return controller.result;
		} catch (IOException e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
