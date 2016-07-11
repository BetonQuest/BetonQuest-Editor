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

package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a single line on main page in a journal.
 *
 * @author Jakub Sapalski
 */
public class MainPageLine {
	
	private StringProperty id;
	private TranslatableText text = new TranslatableText();
	private ObservableList<String> conditions = FXCollections.observableArrayList();
	private IntegerProperty priority = new SimpleIntegerProperty();
	
	public MainPageLine(String id) {
		this.id = new SimpleStringProperty(id);
	}
	
	public StringProperty getId() {
		return id;
	}

	public TranslatableText getText() {
		return text;
	}
	public ObservableList<String> getConditions() {
		return conditions;
	}
	public IntegerProperty getPriority() {
		return priority;
	}
	
	@Override
	public String toString() {
		return id.get();
	}

}
