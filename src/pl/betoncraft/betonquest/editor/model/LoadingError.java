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

import pl.betoncraft.betonquest.editor.BetonQuestEditor;

public class LoadingError {
	
	private String message;
	
	public enum ErrorType {
		PACKAGE_NOT_FOUND("error-no-package"),
		LANGUAGE_FORMAT("error-language-format"),
		FORMAT_INCORRECT("error-list"),
		STOP_WRONG("error-stop-value"),
		PRIORITY_WRONG("error-priority-number");
		
		private String address;
		
		private ErrorType(String address) {
			this.address = address;
		}
		
		@Override
		public String toString() {
			return address;
		}
	}
	
	public LoadingError(QuestPackage pack, ErrorType type, String address) {
		message = BetonQuestEditor.getInstance().getLanguage().getString(type.toString());
		message = message.replace("{pack}", pack.toString());
		message = message.replace("{address}", address);
	}
	
	public String getMessage() {
		return message;
	}

}
