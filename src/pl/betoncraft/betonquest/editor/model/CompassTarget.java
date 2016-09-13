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
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.CompassTargetEditController;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.Translatable;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

public class CompassTarget extends SimpleID implements Translatable {
	
	private TranslatableText name;
	private ObservableList<ConditionWrapper> conditions = FXCollections.observableArrayList();
	private String location;
	private ObjectProperty<Item> item = new SimpleObjectProperty<>();
	
	public CompassTarget(QuestPackage pack, String id) throws PackageNotFoundException {
		if (pack == null) {
			throw new PackageNotFoundException();
		}
		super.pack = pack;
		super.id = new SimpleStringProperty(id);
		name = new TranslatableText(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<CompassTarget> getList() {
		return pack.getCompassTargets();
	}

	@Override
	public boolean edit() {
		return CompassTargetEditController.display(this);
	}

	@Override
	public TranslatableText getText() {
		return name;
	}

	@Override
	public String getType() {
		return BetonQuestEditor.getInstance().getLanguage().getString("compass-target");
	}
	
	public ObservableList<ConditionWrapper> getConditions() {
		return conditions;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public ObjectProperty<Item> getItem() {
		return item;
	}

}
