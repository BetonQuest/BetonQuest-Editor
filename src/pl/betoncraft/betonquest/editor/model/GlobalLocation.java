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
import pl.betoncraft.betonquest.editor.data.Editable;

/**
 * Represents a location objective which is in the "global locations" list. 
 *
 * @author Jakub Sapalski
 */
public class GlobalLocation implements Editable {
	
	private ObjectProperty<Objective> objective = new SimpleObjectProperty<>(); // TODO use IdWrapper in global locations
	
	public GlobalLocation(Objective objective) {
		this.objective.set(objective);
	}

	@Override
	public EditResult edit() {
		return null; // TODO edit global location in a custom window
	}
	
	public ObjectProperty<Objective> getObjective() {
		return objective;
	}
	
	@Override
	public String toString() {
		return objective.get().getId().get();
	}

}
