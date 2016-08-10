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
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleID;

/**
 * Represents an NPC binding with a conversation.
 *
 * @author Jakub Sapalski
 */
public class NpcBinding extends SimpleID {

	private ObjectProperty<Conversation> conversation = new SimpleObjectProperty<>();
	
	public NpcBinding(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}
	
	public NpcBinding(QuestPackage pack, String id, Conversation conversation) {
		this(pack, id);
		this.conversation.set(conversation);
	}

	@Override
	public EditResult edit() {
		return NameEditController.display(id); // TODO edit npc binding in a custom window
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<NpcBinding> getList() {
		return pack.getNpcBindings();
	}

	public ObjectProperty<Conversation> getConversation() {
		return conversation;
	}
	
	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}

}
