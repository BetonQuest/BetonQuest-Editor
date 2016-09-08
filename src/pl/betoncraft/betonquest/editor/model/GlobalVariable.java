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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.SimpleInstruction;

/**
 * Represents a variable defined in main.yml file.
 *
 * @author Jakub Sapalski
 */
public class GlobalVariable extends SimpleInstruction {
	
	public GlobalVariable(QuestPackage pack, String id) {
		this.pack = pack;
		this.id = new SimpleStringProperty(id);
	}
	
	public GlobalVariable(QuestPackage pack, String id, String value) {
		this(pack, id);
		instruction.set(value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<GlobalVariable> getList() {
		return pack.getVariables();
	}
	
}
