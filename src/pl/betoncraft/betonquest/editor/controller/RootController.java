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

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.PackageSet;
import pl.betoncraft.betonquest.editor.model.QuestPackage;
import javafx.stage.WindowEvent;

/**
 * Controls root pane, specifically package choosing.
 *
 * @author Jakub Sapalski
 */
public class RootController {
	
	private static RootController instace;

	@FXML private HBox packages;
	@FXML private TreeView<String> tree;
	@FXML private ContextMenu menu;
	
	private TreeItem<String> local;

	public RootController() {
		instace = this;
	}
	
	public static void setPackageSets(List<PackageSet> packages) {
		TreeItem<String> root = new TreeItem<>();
		instace.tree.setRoot(root);
		instace.tree.setShowRoot(false);
		// fill local packages
		instace.local = new TreeItem<>("Local");
		instace.local.setExpanded(true);
		for (PackageSet set : packages) {
			TreeItem<String> setItem = new TreeItem<>(set.toString());
			setItem.setExpanded(true);
			for (QuestPackage pack : set.getPackages()) {
				TreeItem<String> packItem = new TreeItem<>(pack.toString());
				setItem.getChildren().add(packItem);
			}
			instace.local.getChildren().add(setItem);
		}
		root.getChildren().add(instace.local);
	}

	@FXML public void select(MouseEvent event) {
		TreeItem<String> selected = tree.getSelectionModel().getSelectedItem();
		if (event.getClickCount() != 2 || selected == null || !selected.isLeaf()) {
			return;
		}
		BetonQuestEditor instance = BetonQuestEditor.getInstance();
		PackageSet set = instance.getSet(selected.getParent().getValue());
		if (set == null) {
			return;
		}
		QuestPackage pack = set.getPackage(selected.getValue());
		if (pack == null) {
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

	@FXML public void delete() {
		TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
		if (item != null && !item.equals(local)) {
			if (BetonQuestEditor.confirm("confirm-action")) {
				if (item.isLeaf()) {
					PackageSet set = BetonQuestEditor.getInstance().getSet(item.getParent().getValue());
					if (set.getPackages().size() == 1) {
						BetonQuestEditor.showError("cannot-delete-last-package");
						return;
					}
					QuestPackage pack = set.getPackage(item.getValue());
					set.getPackages().remove(pack);
					if (BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack)) {
						BetonQuestEditor.getInstance().display(set);
					}
				} else {
					PackageSet set = BetonQuestEditor.getInstance().getSet(item.getValue());
					BetonQuestEditor.getInstance().getSets().remove(set);
					if (BetonQuestEditor.getInstance().getDisplayedPackage().getSet().equals(set)) {
						BetonQuestEditor.getInstance().clearView();
					}
				}
				BetonQuestEditor.getInstance().refresh();
			}
		}
	}

	@FXML public void edit() {
		TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
		if (item != null && !item.equals(local)) {
			if (item.isLeaf()) {
				PackageSet set = BetonQuestEditor.getInstance().getSet(item.getParent().getValue());
				QuestPackage pack = set.getPackage(item.getValue());
				NameEditController.display(pack.getName());
			} else {
				PackageSet set = BetonQuestEditor.getInstance().getSet(item.getValue());
				NameEditController.display(set.getName());
			}
			BetonQuestEditor.getInstance().refresh();
		}
	}

	@FXML public void check(WindowEvent event) {
		TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
		if (item.equals(local)) {
			try {
				menu.hide();
			} catch (NullPointerException e) {
				// TODO create custom cell factory in package tree
			}
		}
	}

}
