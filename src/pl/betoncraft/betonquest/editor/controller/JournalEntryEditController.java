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
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.JournalEntry;

/**
 * Controlls the pop-up window used for editing journal entries.
 *
 * @author Jakub Sapalski
 */
public class JournalEntryEditController {
	
	private Stage stage;
	private JournalEntry data;
	
	@FXML private TextField id;
	@FXML private TextArea text;
	
	private void setData(Stage stage, JournalEntry data) {
		this.stage = stage;
		this.data = data;
		id.textProperty().bindBidirectional(data.getId());
		text.textProperty().bindBidirectional(data.getText().get(BetonQuestEditor.getInstance().getDisplayedPackage().getDefLang()));
	}
	
	@FXML private void ok() {
		if (data.getId().get().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ID must be specified");
			alert.showAndWait();
		} else {
			BetonQuestEditor.refresh();
			stage.close();
		}
	}
	
	public static void display(JournalEntry data) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/JournalEditWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			GridPane root = (GridPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("edit-entry"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(300);
			window.setWidth(500);
			window.setResizable(false);
			JournalEntryEditController controller = (JournalEntryEditController) fxmlLoader.getController();
			controller.setData(window, data);
			window.show();
		} catch (IOException e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}

}
