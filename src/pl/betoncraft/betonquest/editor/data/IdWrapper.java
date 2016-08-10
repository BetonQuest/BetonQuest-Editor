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
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.model.QuestPackage;

/**
 * Wraps an ID so the index can be modified without touching the underlying object.
 *
 * @author Jakub Sapalski
 */
public class IdWrapper<T extends ID> implements ID {
	
	private T object;
	private QuestPackage pack;
	private int index = -1;
	
	public IdWrapper(QuestPackage pack, T object) {
		this.pack = pack;
		this.object = object;
	}

	@Override
	public EditResult edit() {
		return object.edit();
	}

	@Override
	public StringProperty getId() {
		return object.getId();
	}

	@Override
	public QuestPackage getPack() {
		return pack;
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
	@SuppressWarnings("unchecked")
	public ObservableList<IdWrapper<T>> getList() {
		return null; // wrappers do not have their own list in the package
	}
	
	public T get() {
		return object;
	}
	
	public void set(T object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		return object.toString();
	}

}
