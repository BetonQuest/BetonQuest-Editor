/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2017  Jakub "Co0sh" Sapalski
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Controls the "Changelog" window.
 *
 * @author Jakub Sapalski
 */
public class ChangelogController {
	
	private Stage stage;
	
	@FXML
	private VBox root;
	
	@FXML
	private TextArea text;
	
	@FXML
	private void close() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static void display() {
		try {

			// create window
			ChangelogController controller = (ChangelogController) BetonQuestEditor
					.createWindow("view/window/ChangelogWindow.fxml", "changelog", 600, 600);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			
			// load the changelog text
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					BetonQuestEditor.class.getResourceAsStream("resource/changelog.txt")));
			StringBuilder builder = new StringBuilder();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				builder.append(temp);
				builder.append(System.lineSeparator());
			}
			String changelog = builder.toString();
			controller.text.setText(changelog);

			// display the window
			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
