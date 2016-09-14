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
import pl.betoncraft.betonquest.editor.controller.NpcBindingEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.Validatable;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Represents an NPC binding with a conversation.
 *
 * @author Jakub Sapalski
 */
public class NpcBinding extends SimpleID implements Validatable {

	private ObjectProperty<Conversation> conversation = new SimpleObjectProperty<>();
	
	public NpcBinding(QuestPackage pack, String id) throws PackageNotFoundException {
		if (pack == null) {
			throw new PackageNotFoundException();
		}
		this.pack = ID.parsePackage(pack, id.replace(" ", "_"));
		this.id = new SimpleStringProperty(ID.parseId(id.replace(" ", "_")));
	}
	
	public NpcBinding(QuestPackage pack, String id, Conversation conversation) throws PackageNotFoundException {
		this(pack, id);
		this.conversation.set(conversation);
	}

	@Override
	public boolean edit() {
		return NpcBindingEditController.display(this);
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
	public boolean isValid() {
		return conversation.get() != null;
	}

}
