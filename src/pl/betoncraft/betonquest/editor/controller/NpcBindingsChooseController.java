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

import java.util.Iterator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.Conversation;
import pl.betoncraft.betonquest.editor.model.NpcBinding;

public class NpcBindingsChooseController {
	
	private static final KeyCombination newKey = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);

	private Stage stage;
	private Conversation conv;
	@FXML Pane root;
	@FXML ListView<String> list;

	@FXML private void add() {
		try {
			StringProperty name = new SimpleStringProperty();
			if (NameEditController.display(name, true)) {
				if (list.getItems().contains(name.get())) {
					BetonQuestEditor.showError("already-exists");
					return;
				}
				list.getItems().add(name.get());
				NpcBinding binding = new NpcBinding(conv.getPack(), name.get());
				binding.getConversation().set(conv);
				conv.getPack().getNpcBindings().add(binding);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void del() {
		try {
			String binding = list.getSelectionModel().getSelectedItem();
			if (binding != null) {
				for (Iterator<NpcBinding> i = conv.getPack().getNpcBindings().iterator(); i.hasNext();) {
					if (i.next().getId().get().equals(binding)) {
						list.getItems().remove(binding);
						i.remove();
						return;
					}
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void close() {
		try {
			stage.close();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static void display(Conversation conv) {
		try {
			NpcBindingsChooseController controller = (NpcBindingsChooseController) BetonQuestEditor
					.createWindow("view/window/NpcBindingsChooseWindow.fxml", "npc-bindings", 300, 300);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.conv = conv;
			for (NpcBinding binding : conv.getPack().getNpcBindings()) {
				if (binding.getConversation().get().equals(conv)) {
					controller.list.getItems().add(binding.getId().get());
				}
			}
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					controller.close();
				} else if (newKey.match(event)) {
					controller.add();
				} else if (event.getCode() == KeyCode.DELETE) {
					controller.del();
				}
			});
			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
