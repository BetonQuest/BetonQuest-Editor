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
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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

	@FXML private Pane root;
	@FXML private ChoiceBox<Objective> objective;
	
	@FXML private void ok() {
		try {
			Objective chosen = objective.getValue();
			if (chosen == null) {
				BetonQuestEditor.showError("select-objective");
			}
			data.getObjective().set(chosen);
			BetonQuestEditor.getInstance().refresh();
			result = true;
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void cancel() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	/**
	 * Displays global location in a window. Returns true if editing was successful, false if not.
	 * 
	 * @param data GlobalLocation to display
	 * @return true if editing was successful, false otherwise
	 */
	public static boolean display(GlobalLocation data) {
		try {
			GlobalLocationEditController controller = (GlobalLocationEditController) BetonQuestEditor
					.createWindow("view/window/GlobalLocationEditWindow.fxml", "edit-global-location", 300, 135);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.data = data;
			controller.objective.getItems().setAll(BetonQuestEditor.getInstance().getAllObjectives());
			if (controller.objective.getItems().contains(data.getObjective().get())) {
				controller.objective.getSelectionModel().select(data.getObjective().get());
			}
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					controller.cancel();
					event.consume();
					return;
				}
				if (event.getCode() == KeyCode.ENTER) {
					controller.ok();
					event.consume();
					return;
				}
			});
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
