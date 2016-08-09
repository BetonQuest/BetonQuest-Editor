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

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.Editable.EditResult;
import pl.betoncraft.betonquest.editor.model.JournalEntry;

/**
 * Controls the pop-up window used for editing journal entries.
 *
 * @author Jakub Sapalski
 */
public class JournalEntryEditController {
	
	private Stage stage;
	private JournalEntry data;
	private StringProperty entry;
	private EditResult result = EditResult.CANCEL;
	
	@FXML private TextField id;
	@FXML private TextArea text;
	
	/**
	 * Sets the data in the window.
	 * 
	 * @param stage the pop-up window
	 * @param data JournalEntry to display in text fields
	 */
	private void setData(Stage stage, JournalEntry data) {
		this.stage = stage;
		this.data = data;
		id.setText(data.getId().get());
		entry = data.getText().get(BetonQuestEditor.getInstance().getDisplayedPackage().getDefLang());
		text.setText(entry.get());
	}
	
	/**
	 * Checks if the name is inserted and changes the StringProperties of the JournalEntry object.
	 */
	@FXML private void ok() {
		try {
			String idString = id.getText();
			if (idString == null || idString.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(BetonQuestEditor.getInstance().getLanguage().getString("name-not-null"));
				alert.showAndWait();
				return;
			}
			data.getId().set(idString.trim());
			String entryText = text.getText();
			if (entryText != null && !entryText.isEmpty()) {
				entry.set(entryText);
			} else {
				entry.set(new String());
			}
			BetonQuestEditor.refresh();
			result = EditResult.SUCCESS;
			stage.close();
		} catch (Exception e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}
	
	/**
	 * Closes the window without changing StringProperties in the JournalEntry object.
	 */
	@FXML private void cancel() {
		stage.close();
	}
	
	/**
	 * Displays a window in which the user can edit a journal entry object.
	 * 
	 * @param data the JournalEntry object to edit
	 */
	public static EditResult display(JournalEntry data) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/JournalEntryEditWindow.fxml");
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
			window.showAndWait();
			return controller.result;
		} catch (IOException e) {
			BetonQuestEditor.showStackTrace(e);
			return EditResult.CANCEL;
		}
	}

}
