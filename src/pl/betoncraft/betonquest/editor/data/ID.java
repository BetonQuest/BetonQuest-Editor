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
 * Represents a model object with ID.
 *
 * @author Jakub Sapalski
 */
public interface ID {
	
	/**
	 * @return the ID of this object
	 */
	public StringProperty getId();
	
	/**
	 * @return the index of this object, as ordered in the file
	 */
	public int getIndex();
	
	/**
	 * Sets index of this object, so it can be saved to the file in order.
	 * 
	 * @param index index of the object related to other objects in the same file
	 */
	public void setIndex(int index);
	
}
