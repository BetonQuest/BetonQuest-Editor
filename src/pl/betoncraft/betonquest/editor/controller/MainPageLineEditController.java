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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.ConditionListCell;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.model.Condition;
import pl.betoncraft.betonquest.editor.model.MainPageLine;

/**
 * Controls main page line editing window.
 *
 * @author Jakub Sapalski
 */
public class MainPageLineEditController {
	
	private Stage stage;
	private MainPageLine line;
	private boolean result = false;
	private ObservableList<ConditionWrapper> conditionList;

	@FXML private VBox root;
	@FXML private TextField id;
	@FXML private TextArea text;
	@FXML private Button conditions;
	@FXML private TextField priority;
	
	private void refresh() {
		conditions.setText(BetonQuestEditor.getInstance().getLanguage().getString("conditions")
				+ " (" + conditionList.size() + ")");
	}
	
	@FXML private void conditions() {
		try {
			SortedChoiceController.display("conditions", conditionList, BetonQuestEditor.getInstance().getAllConditions(),
					name -> new Condition(line.getPack(), name), () -> new ConditionListCell(),
					item -> new ConditionWrapper(line.getPack(), item), () -> refresh());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void ok() {
		try {
			String id = this.id.getText();
			if (id == null || id.isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			if (!ID.validate(id)) {
				BetonQuestEditor.showError("id-format-incorrect");
				return;
			}
			if (priority.getText() == null || priority.getText().isEmpty() || !priority.getText().matches("\\d+")) {
				BetonQuestEditor.showError("priority-number");
				return;
			}
			int priorityInt = Integer.parseInt(priority.getText());
			line.getId().set(id.trim());
			line.getText().get().set(text.getText());
			line.getConditions().setAll(conditionList);
			line.getPriority().set(priorityInt);
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
	
	public static boolean display(MainPageLine line) {
		try {
			MainPageLineEditController controller = (MainPageLineEditController) BetonQuestEditor
					.createWindow("view/window/MainPageLineEditWindow.fxml", "edit-main-page-line", 400, 300);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.line = line;
			controller.id.setText(line.getId().get());
			controller.text.setText(line.getText().get().get());
			controller.conditionList = FXCollections.observableArrayList(line.getConditions());
			controller.priority.setText(String.valueOf(line.getPriority().get()));
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
			controller.refresh();
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
