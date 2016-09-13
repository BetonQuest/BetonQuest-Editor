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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.CompassTarget;
import pl.betoncraft.betonquest.editor.model.Item;

/**
 * Controls compass target editing window.
 *
 * @author Jakub Sapalski
 */
public class CompassTargetEditController {

	private CompassTarget target;
	private boolean result = false;
	private Stage stage;
	@FXML Pane root;
	@FXML TextField id;
	@FXML TextField name;
	@FXML TextField loc;
	@FXML ChoiceBox<Item> item;
	
	@FXML private void ok() {
		try {
			if (id.getText() == null || id.getText().isEmpty() || name.getText() == null || name.getText().isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			target.getId().set(id.getText());
			target.getText().set(name.getText());
			target.setLocation(loc.getText());
			target.getItem().set(item.getValue());
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
	
	public static boolean display(CompassTarget target) {
		try {
			CompassTargetEditController controller = (CompassTargetEditController) BetonQuestEditor
					.createWindow("view/window/CompassTargetEditWindow.fxml", "npc-bindings", 400, 250);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.target = target;
			controller.id.setText(target.getId().get());
			controller.name.setText(target.getText().get().get());
			controller.loc.setText(target.getLocation());
			controller.item.setItems(target.getPack().getItems());
			controller.item.getSelectionModel().select(target.getItem().get());
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					controller.cancel();
				} else if (event.getCode() == KeyCode.ENTER) {
					controller.ok();
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
