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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.Editable.EditResult;
import pl.betoncraft.betonquest.editor.data.Instruction;

/**
 * Controls the pop-up window used for editing instruction strings.
 *
 * @author Jakub Sapalski
 */
public class InstructionEditController {
	
	private Stage stage;
	private Instruction data;
	private EditResult result = EditResult.CANCEL;;
	
	@FXML private TextField id;
	@FXML private TextField instruction;
	
	/**
	 * Sets the data in the window.
	 * 
	 * @param stage the pop-up window
	 * @param data Instruction to display in text fields
	 */
	private void setData(Stage stage, Instruction data) {
		this.stage = stage;
		this.data = data;
		id.setText(data.getId().get());
		instruction.setText(data.getInstruction().get());
	}
	
	/**
	 * Checks if the name is inserted and changes the StringProperties of the Instruction object.
	 */
	@FXML private void ok() {
		String idString = id.getText();
		if (idString == null || idString.isEmpty()) {
			BetonQuestEditor.showError("name-not-null");
			return;
		}
		data.getId().set(idString.trim());
		String instructionString = instruction.getText();
		if (instructionString != null) {
			data.getInstruction().set(instructionString.trim());
		} else {
			data.getInstruction().set(new String());
		}
		BetonQuestEditor.getInstance().refresh();
		result = EditResult.SUCCESS;
		stage.close();
	}
	
	/**
	 * Closes the window without changing StringProperties in the Instruction object.
	 */
	@FXML private void cancel() {
		stage.close();
	}
	
	/**
	 * Displays a window in which the user can edit the Instruction object.
	 * 
	 * @param data the Instruction object to edit
	 */
	public static EditResult display(Instruction data) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/InstructionEditWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			GridPane root = (GridPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("edit-instruction"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(150);
			window.setWidth(500);
			window.setResizable(false);
			InstructionEditController controller = (InstructionEditController) fxmlLoader.getController();
			controller.setData(window, data);
			window.showAndWait();
			return controller.result;
		} catch (IOException e) {
			ExceptionController.display(e);
			return EditResult.CANCEL;
		}
	}

}
