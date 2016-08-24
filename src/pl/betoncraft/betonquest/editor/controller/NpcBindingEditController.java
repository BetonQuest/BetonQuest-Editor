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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.Conversation;
import pl.betoncraft.betonquest.editor.model.NpcBinding;

/**
 * Controls the pop-up window used for editing NPC bindings.
 *
 * @author Jakub Sapalski
 */
public class NpcBindingEditController {
	
	private Stage stage;
	private NpcBinding data;
	private boolean result = false;

	@FXML private Pane root;
	@FXML private TextField field;
	@FXML private ChoiceBox<Conversation> conversation;
	
	@FXML private void ok() {
		String idString = field.getText();
		if (idString == null || idString.isEmpty()) {
			BetonQuestEditor.showError("identifier-not-null");
			return;
		}
		Conversation conv = conversation.getValue();
		if (conv == null) {
			BetonQuestEditor.showError("binding-not-null");
			return;
		}
		data.getId().set(idString.trim());
		data.getConversation().set(conv);
		BetonQuestEditor.getInstance().refresh();
		result = true;
		stage.close();
	}
	
	@FXML private void cancel() {
		stage.close();
	}
	
	public static boolean display(NpcBinding data) {
		try {
			NpcBindingEditController controller = (NpcBindingEditController) BetonQuestEditor
					.createWindow("view/window/NpcBindingEditWindow.fxml", "edit-npc-binding", 300, 170);
			if (controller == null) {
				return false;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.data = data;
			controller.field.setText(data.getId().get());
			controller.conversation.setItems(BetonQuestEditor.getInstance().getAllConversations());
			if (data.getConversation() != null && data.getConversation().get() != null
					&& controller.conversation.getItems().contains(data.getConversation().get())) {
				controller.conversation.getSelectionModel().select(data.getConversation().get());
			}
			controller.stage.showAndWait();
			return controller.result;
		} catch (Exception e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
