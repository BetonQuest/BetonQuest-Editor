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

import java.util.LinkedList;

import pl.betoncraft.betonquest.editor.model.LoadingError.ErrorType;

public class ErrorManager {
	
	private LinkedList<LoadingError> errors = new LinkedList<>();
	private QuestPackage pack;
	
	public ErrorManager(QuestPackage pack) {
		this.pack = pack;
	}
	
	public void addError(ErrorType type, String address) {
		errors.add(new LoadingError(pack, type, address));
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public String getErrors() {
		StringBuilder string = new StringBuilder();
		for (LoadingError error : errors) {
			string.append(error.getMessage() + System.lineSeparator());
		}
		return string.toString();
	}

}
