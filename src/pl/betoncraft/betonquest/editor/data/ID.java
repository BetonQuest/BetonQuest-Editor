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
 * Represents a model object with ID.
 *
 * @author Jakub Sapalski
 */
public interface ID extends Editable {
	
	/**
	 * @return the ID of this object
	 */
	public StringProperty getId();
	
	/**
	 * @return the QuestPackage in which this object is defined
	 */
	public QuestPackage getPack();
	
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
	
	/**
	 * This method <b>MUST</b> return implementing type or there will be errors.
	 * 
	 * @return the main ObservableList containing this object in the package.
	 */
	public <T extends ID> ObservableList<T> getList();
	
	/**
	 * Parses the ID string to get name of the package. If no package is defined
	 * in the ID, it will return the package passed to the method. It will
	 * return null if defined package does not exist.
	 * 
	 * @param def
	 *            default package to return in case there is none defined
	 * @param id
	 *            ID string to parse
	 * @return the package defined in the ID string, "def" package or null.
	 */
	public static QuestPackage parsePackage(QuestPackage def, String id) {
		if (id.contains(".")) {
			String packName = id.split("\\.")[0];
			for (QuestPackage pack : def.getSet().getPackages()) {
				if (pack.getName().get().equals(packName)) {
					return pack;
				}
			}
			return null;
		} else {
			return def;
		}
	}
	
	/**
	 * Parses the ID string to get name of the object, without package name if
	 * it's defined.
	 * 
	 * @param id
	 *            ID string to parse
	 * @return the name of the object
	 */
	public static String parseId(String id) {
		if (id.contains(".")) {
			return id.split("\\.")[1];
		} else {
			return id;
		}
	}
	
}
