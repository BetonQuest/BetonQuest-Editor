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
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleID;

/**
 * Represents a static event, which is fired at exact hour each day.
 *
 * @author Jakub Sapalski
 */
public class StaticEvent extends SimpleID {

	private ObjectProperty<Event> event = new SimpleObjectProperty<>();
	
	public StaticEvent(QuestPackage pack, String time) {
		this.pack = ID.parsePackage(pack, time);
		this.id = new SimpleStringProperty(ID.parseId(time));
	}

	@Override
	public boolean edit() {
		return NameEditController.display(id); // TODO edit static event in a custom window
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<StaticEvent> getList() {
		return pack.getStaticEvents();
	}

	public ObjectProperty<Event> getEvent() {
		return event;
	}
	
	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}

}
