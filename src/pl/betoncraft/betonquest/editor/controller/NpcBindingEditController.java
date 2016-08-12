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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
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
	
	@FXML private TextField field;
	@FXML private ChoiceBox<Conversation> conversation;
	
	private void setData(Stage stage, NpcBinding data) {
		this.stage = stage;
		this.data = data;
		field.setText(data.getId().get());
		conversation.setItems(BetonQuestEditor.getInstance().getAllConversations());
		if (data.getConversation() != null && data.getConversation().get() != null
				&& conversation.getItems().contains(data.getConversation().get())) {
			conversation.getSelectionModel().select(data.getConversation().get());
		}
	}
	
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
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/NpcBindingEditWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			VBox root = (VBox) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("edit-npc-binding"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(170);
			window.setWidth(300);
			window.setResizable(false);
			NpcBindingEditController controller = (NpcBindingEditController) fxmlLoader.getController();
			controller.setData(window, data);
			window.showAndWait();
			return controller.result;
		} catch (IOException e) {
			ExceptionController.display(e);
			return false;
		}
	}

}
