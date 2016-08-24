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

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.Instruction;

/**
 * Controls the pop-up window used for editing instruction strings.
 *
 * @author Jakub Sapalski
 */
public class InstructionEditController {
	
	private Stage stage;
	private Instruction data;
	private boolean result = false;
	
	@FXML private Pane root;
	@FXML private TextField id;
	@FXML private TextField instruction;
	
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
		result = true;
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
	public static boolean display(Instruction data) {
		try {
			InstructionEditController controller = (InstructionEditController) BetonQuestEditor
					.createWindow("view/window/InstructionEditWindow.fxml", "edit-instruction", 500, 150);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.data = data;
			controller.id.setText(data.getId().get());
			controller.instruction.setText(data.getInstruction().get());
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
