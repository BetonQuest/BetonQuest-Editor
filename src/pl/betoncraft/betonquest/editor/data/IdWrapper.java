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

package pl.betoncraft.betonquest.editor.data;

import javafx.beans.property.StringProperty;

/**
 * Wraps an ID so the index can be modified without touching the underlying object.
 *
 * @author Jakub Sapalski
 */
public class IdWrapper<T extends ID> implements ID {
	
	private T object;
	private int index = -1;
	
	public IdWrapper(T object) {
		this.object = object;
	}
	
	public T get() {
		return object;
	}
	
	public void set(T object) {
		this.object = object;
	}

	@Override
	public StringProperty getId() {
		return object.getId();
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return object.toString();
	}

	@Override
	public EditResult edit() {
		return object.edit();
	}

}
