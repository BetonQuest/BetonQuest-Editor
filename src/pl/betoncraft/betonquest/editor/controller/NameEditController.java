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

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Controls the pop-up window for editing a StringProperty.
 *
 * @author Jakub Sapalski
 */
public class NameEditController {
	
	private Stage stage;
	private StringProperty name;
	private boolean result = false;
	
	@FXML private Pane root;
	@FXML private TextField field;
	
	/**
	 * Checks if the name is inserted and changes the StringProperty.
	 */
	@FXML private void ok() {
		try {
			String text = field.getText();
			if (text == null || text.isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			name.set(text.trim());
			BetonQuestEditor.getInstance().refresh();
			result = true;
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	/**
	 * Closes the window without changing StringProperty object.
	 */
	@FXML private void cancel() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	/**
	 * Displays a window in which the user can edit the StringProperty object.
	 * 
	 * @param data StringProperty to edit
	 */
	public static boolean display(StringProperty data) {
		try {
			NameEditController controller = (NameEditController) BetonQuestEditor
					.createWindow("view/window/NameEditWindow.fxml", "edit-name", 500, 100);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.name = data;
			controller.field.setText(data.get());
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
