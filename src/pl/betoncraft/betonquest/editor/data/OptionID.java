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

import pl.betoncraft.betonquest.editor.model.Conversation;

/**
 * Represents an ID object, which also has a conversation.
 *
 * @author Jakub Sapalski
 */
public interface OptionID extends ID {
	
	public Conversation getConversation();
	
	public String getRelativeOptionName(Conversation conv);
	
	public String getAbsoluteOptionName();
	
	/**
	 * Parses the ID string to get name of the conversation. If no conversation
	 * is defined in the ID, it will return the conversation passed to the
	 * method. It will return null if defined conversation does not exist.
	 * 
	 * @param def
	 *            default conversation to return in case there is none defined
	 * @param id
	 *            ID string to parse
	 * @return the conversation defined in the ID string, "def" conversation or
	 *         null.
	 */
	public static Conversation parseConversation(Conversation def, String id) {
		if (id.contains(".")) {
			String packName = id.split("\\.")[0];
			for (Conversation conv : def.getPack().getConversations()) {
				if (conv.getId().get().equals(packName)) {
					return conv;
				}
			}
			return def.getPack().newByID(packName, name -> new Conversation(def.getPack(), name));
		} else {
			return def;
		}
	}
	
	/**
	 * Parses the ID string to get name of the object, without conversation name
	 * if it's defined.
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
