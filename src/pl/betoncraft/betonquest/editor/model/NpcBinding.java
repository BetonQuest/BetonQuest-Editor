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
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.data.ID;

/**
 * Represents an NPC binding with a conversation.
 *
 * @author Jakub Sapalski
 */
public class NpcBinding implements ID {
	
	private StringProperty id;
	private ObjectProperty<Conversation> conversation = new SimpleObjectProperty<>();
	
	public NpcBinding(String id, Conversation conversation) {
		this(id);
		this.conversation.set(conversation);
	}
	
	public NpcBinding(String id) {
		this.id = new SimpleStringProperty(id);
	}

	@Override
	public StringProperty getId() {
		return id;
	}

	public ObjectProperty<Conversation> getConversation() {
		return conversation;
	}

}
