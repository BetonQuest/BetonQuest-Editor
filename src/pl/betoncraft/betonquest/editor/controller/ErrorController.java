/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

public class ErrorController {

	private Stage stage;

	@FXML private VBox root;
	@FXML private TextArea error;
	
	@FXML private void close() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	/**
	 * Displays the errors in a window.
	 * 
	 * @param exception exception to display
	 */
	public static void display(String errors) {
		try {
			ErrorController controller = (ErrorController) BetonQuestEditor
					.createWindow("view/window/ErrorWindow.fxml", "errors-found", 700, 500);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.error.setText(errors);
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
					controller.close();
				}
			});
			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
