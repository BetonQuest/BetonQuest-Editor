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
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Abstract class representing an option in the conversation.
 *
 * @author Jakub Sapalski
 */
public abstract class ConversationOption implements ID {
	
	private StringProperty id;
	private TranslatableText text = new TranslatableText();
	private ObservableList<Event> events = FXCollections.observableArrayList();
	private ObservableList<Condition> conditions = FXCollections.observableArrayList();
	private ObservableList<ConversationOption> pointers = FXCollections.observableArrayList();

	public ConversationOption(String id) {
		this.id = new SimpleStringProperty(id);
	}

	public StringProperty getId() {
		return id;
	}

	public TranslatableText getText() {
		return text;
	}

	public ObservableList<Event> getEvents() {
		return events;
	}

	public ObservableList<Condition> getConditions() {
		return conditions;
	}

	public ObservableList<ConversationOption> getPointers() {
		return pointers;
	}
	
	@Override
	public String toString() {
		return id.get();
	}

}
