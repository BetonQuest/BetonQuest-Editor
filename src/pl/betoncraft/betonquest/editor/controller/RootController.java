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

import java.util.Collection;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.QuestPackage;
import javafx.scene.input.MouseEvent;

/**
 * Controls root pane, specifically package choosing.
 *
 * @author Jakub Sapalski
 */
public class RootController {
	
	private static RootController instace;

	@FXML private HBox packages;
	@FXML private TreeView<String> tree;

	public RootController() {
		instace = this;
	}
	
	public static void setPackages(Collection<QuestPackage> packages) {
		TreeItem<String> root = new TreeItem<>();
		instace.tree.setRoot(root);
		instace.tree.setShowRoot(false);
		// fill local packages
		TreeItem<String> local = new TreeItem<>("Local");
		for (QuestPackage pack : packages) {
			TreeItem<String> item = new TreeItem<>(pack.toString());
			local.getChildren().add(item);
		}
		local.setExpanded(true);
		root.getChildren().add(local);
	}

	@FXML public void select(MouseEvent event) {
		TreeItem<String> selected = tree.getSelectionModel().getSelectedItem();
		if (event.getClickCount() != 2 || selected == null || !selected.isLeaf()) {
			return;
		}
		BetonQuestEditor instance = BetonQuestEditor.getInstance();
		QuestPackage pack = instance.getPackages().get(selected.getValue());
		if (pack == null) {
			System.out.println(selected);
			return;
		}
		instance.display(pack);
		hide();
	}
	
	@FXML public void show() {
		packages.setVisible(true);
	}
	
	@FXML public void hide() {
		packages.setVisible(false);
	}

}
