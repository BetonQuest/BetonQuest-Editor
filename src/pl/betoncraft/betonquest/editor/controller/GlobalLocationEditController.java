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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.GlobalLocation;
import pl.betoncraft.betonquest.editor.model.Objective;

/**
 * Controls the pop-up window used for global location editing.
 *
 * @author Jakub Sapalski
 */
public class GlobalLocationEditController {
	
	private Stage stage;
	private boolean result = false;
	private GlobalLocation data;

	@FXML private ChoiceBox<Objective> objective;
	
	private void setData(Stage stage, GlobalLocation data) {
		this.stage = stage;
		this.data = data;
		objective.getItems().setAll(BetonQuestEditor.getInstance().getAllObjectives());
		if (objective.getItems().contains(data.getObjective().get())) {
			objective.getSelectionModel().select(data.getObjective().get());
		}
	}
	
	@FXML private void ok() {
		Objective chosen = objective.getValue();
		if (chosen == null) {
			BetonQuestEditor.showError("select-objective");
		}
		data.getObjective().set(chosen);
		BetonQuestEditor.getInstance().refresh();
		result = true;
		stage.close();
	}
	
	@FXML private void cancel() {
		stage.close();
	}
	
	public static boolean display(GlobalLocation data) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/GlobalLocationEditWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			VBox root = (VBox) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("edit-global-location"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(135);
			window.setWidth(300);
			window.setResizable(false);
			GlobalLocationEditController controller = (GlobalLocationEditController) fxmlLoader.getController();
			controller.setData(window, data);
			window.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
