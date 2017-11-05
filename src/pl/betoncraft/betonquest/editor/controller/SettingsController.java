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

import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.persistence.Persistence;
import pl.betoncraft.betonquest.editor.persistence.Settings;

/**
 * Controls "Settings" window.
 *
 * @author Jakub Sapalski
 */
public class SettingsController {

	private Stage stage;

	@FXML
	private VBox root;

	@FXML
	private ChoiceBox<Locale> language;

	@FXML
	private void ok() {
		try {

			// prepare settings
			Settings settings = Persistence.getSettings();

			// set values
			settings.setLanguage(language.getValue());

			// apply and close window
			Persistence.getSettings().apply();
			stage.close();
			
			// reload the window
			BetonQuestEditor.reload();

		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML
	private void restore() {
		try {
			Persistence.getSettings().restoreDefaults();
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML
	private void cancel() {
		try {
			Persistence.getSettings().load();
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	/**
	 * Displays "About" window with information about the editor.
	 */
	public static void display() {
		try {

			// create window
			SettingsController controller = (SettingsController) BetonQuestEditor
					.createWindow("view/window/SettingsWindow.fxml", "settings", 400, 600);
			controller.stage = (Stage) controller.root.getScene().getWindow();

			// get settings
			Settings settings = Persistence.getSettings();

			// bind language settings
			controller.language.getItems().addAll(settings.getAvailableLocales());
			controller.language.getSelectionModel().select(settings.getLanguage());

			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
