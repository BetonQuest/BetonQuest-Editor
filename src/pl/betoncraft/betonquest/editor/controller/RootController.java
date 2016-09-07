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
import pl.betoncraft.betonquest.editor.custom.PackageTreeCell;
import pl.betoncraft.betonquest.editor.custom.PackageTreeItem;
import pl.betoncraft.betonquest.editor.custom.PackageTreeItem.Type;
import pl.betoncraft.betonquest.editor.model.PackageSet;
import pl.betoncraft.betonquest.editor.model.QuestPackage;

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
	
	private PackageTreeItem local;

	public RootController() {
		instace = this;
	}
	
	public static void setPackageSets(List<PackageSet> packages) {
		instace.tree.setCellFactory(event -> {
			return new PackageTreeCell();
		});
		PackageTreeItem root = new PackageTreeItem(Type.ROOT);
		instace.tree.setRoot(root);
		instace.tree.setShowRoot(false);
		// fill local packages
		instace.local = new PackageTreeItem(Type.TYPE, "Local");
		instace.local.setExpanded(true);
		for (PackageSet set : packages) {
			PackageTreeItem setItem = new PackageTreeItem(Type.SET, set.toString());
			instace.local.getChildren().add(setItem);
			setItem.setExpanded(true);
			for (QuestPackage pack : set.getPackages()) {
				PackageTreeItem packItem = new PackageTreeItem(Type.PACKAGE, pack.toString());
				setItem.getChildren().add(packItem);
			}
		}
		root.getChildren().add(instace.local);
	}

	@FXML public void select(MouseEvent event) {
		try {
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
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML public void show() {
		try {
			packages.setVisible(true);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML public void hide() {
		try {
			packages.setVisible(false);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static RootController getInstance() {
		return instace;
	}

	public void delete(PackageTreeItem item) {
		try {
			if (item != null) {
				if (BetonQuestEditor.confirm("confirm-action")) {
					switch (item.getType()) {
					case PACKAGE:
						PackageSet set1 = BetonQuestEditor.getInstance().getSet(item.getParent().getValue());
						if (set1.getPackages().size() == 1) {
							BetonQuestEditor.showError("cannot-delete-last-package");
							return;
						}
						QuestPackage pack = set1.getPackage(item.getValue());
						set1.getPackages().remove(pack);
						if (BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack)) {
							BetonQuestEditor.getInstance().display(set1);
						}
						break;
					case SET:
						PackageSet set2 = BetonQuestEditor.getInstance().getSet(item.getValue());
						BetonQuestEditor.getInstance().getSets().remove(set2);
						if (BetonQuestEditor.getInstance().getDisplayedPackage().getSet().equals(set2)) {
							BetonQuestEditor.getInstance().clearView();
						}
						break;
					default:
						break;
					}
					BetonQuestEditor.getInstance().refresh();
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	public void edit(PackageTreeItem item) {
		try {
			if (item != null) {
				switch (item.getType()) {
				case PACKAGE:
					PackageSet set1 = BetonQuestEditor.getInstance().getSet(item.getParent().getValue());
					QuestPackage pack = set1.getPackage(item.getValue());
					NameEditController.display(pack.getName());
					break;
				case SET:
					PackageSet set2 = BetonQuestEditor.getInstance().getSet(item.getValue());
					NameEditController.display(set2.getName());
					break;
				default:
					break;
				}
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	public static void clear() {
		instace.tree.setRoot(null);
	}

}
