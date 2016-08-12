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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.controller.ConversationController;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.data.OptionID;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Abstract class representing an option in the conversation.
 *
 * @author Jakub Sapalski
 */
public abstract class ConversationOption extends SimpleID implements OptionID {

	private Conversation conversation;
	private TranslatableText text = new TranslatableText();
	private ObservableList<IdWrapper<Event>> events = FXCollections.observableArrayList();
	private ObservableList<ConditionWrapper> conditions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<ConversationOption>> pointers = FXCollections.observableArrayList();

	public ConversationOption(Conversation conv, String id) {
		this.conversation = OptionID.parseConversation(conv, id);
		this.id = new SimpleStringProperty(OptionID.parseId(id));
	}

	@Override
	public boolean edit() {
		return NameEditController.display(id);
	}
	
	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public QuestPackage getPack() {
		return conversation.getPack();
	}

	public TranslatableText getText() {
		return text;
	}

	public ObservableList<IdWrapper<Event>> getEvents() {
		return events;
	}

	public ObservableList<ConditionWrapper> getConditions() {
		return conditions;
	}

	public ObservableList<IdWrapper<ConversationOption>> getPointers() {
		return pointers;
	}
	
	@Override
	public String toString() {
		return ConversationController.getDisplayedConversation().equals(conversation) ? id.get() : conversation.getId().get() + "." + id.get();
	}

}
