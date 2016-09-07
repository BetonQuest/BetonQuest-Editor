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

package pl.betoncraft.betonquest.editor.custom;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.ConditionListCellController;
import pl.betoncraft.betonquest.editor.controller.ExceptionController;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;

/**
 * List cell capable of displaying 
 *
 * @author Jakub Sapalski
 */
public class ConditionListCell extends DraggableListCell<ConditionWrapper> {
	
	private static List<ConditionListCell> list = new LinkedList<>();
	
	private HBox node;
	private ConditionListCellController controller;
	
	public ConditionListCell() {
		try {
			list.add(this);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			URL location = BetonQuestEditor.class.getResource("view/other/ConditionListCell.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			node = (HBox) fxmlLoader.load();
			controller = (ConditionListCellController) fxmlLoader.getController();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@Override
	public void updateItem(ConditionWrapper item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			controller.clear();
		} else {
			controller.setCondition(item);
			setGraphic(node);
		}
	}
	
	public static void invert(ConditionWrapper item) {
		for (ConditionListCell cell : list) {
			ConditionWrapper wrapper = cell.controller.getCondition();
			if (wrapper != null && wrapper.equals(item)) {
				cell.controller.invert();
				return;
			}
		}
	}

}
