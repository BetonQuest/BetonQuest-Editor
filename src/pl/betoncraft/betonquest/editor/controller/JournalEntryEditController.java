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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
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
	private boolean result = false;
	
	@FXML private Pane root;
	@FXML private TextField id;
	@FXML private TextArea text;
	
	/**
	 * Checks if the name is inserted and changes the StringProperties of the JournalEntry object.
	 */
	@FXML private void ok() {
		try {
			String idString = id.getText();
			if (idString == null || idString.isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			data.getId().set(idString.trim());
			String entryText = text.getText();
			if (entryText != null && !entryText.isEmpty()) {
				entry.set(entryText);
			} else {
				entry.set(new String());
			}
			BetonQuestEditor.getInstance().refresh();
			result = true;
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
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
	public static boolean display(JournalEntry data) {
		try {
			JournalEntryEditController controller = (JournalEntryEditController) BetonQuestEditor
					.createWindow("view/window/JournalEntryEditWindow.fxml", "edit-entry", 500, 300);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.data = data;
			controller.id.setText(data.getId().get());
			controller.entry = data.getText().get(BetonQuestEditor.getInstance().getDisplayedPackage().getDefLang());
			controller.text.setText(controller.entry.get());
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
