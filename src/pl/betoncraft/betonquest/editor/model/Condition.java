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
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.InstructionEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.Instruction;

/**
 * Represents a BetonQuest condition. 
 *
 * @author Jakub Sapalski
 */
public class Condition implements Instruction {
	
	private StringProperty id;
	private QuestPackage pack;
	private int index = -1;
	private StringProperty instruction = new SimpleStringProperty();
	
	public Condition(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}
	
	public Condition(QuestPackage pack, String id, String instruction) {
		this(pack, id);
		this.instruction.set(instruction);
	}
	
	@Override
	public EditResult edit() {
		return InstructionEditController.display(this);
	}

	@Override
	public StringProperty getId() {
		return id;
	}

	@Override
	public QuestPackage getPack() {
		return pack;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<Condition> getList() {
		return pack.getConditions();
	}

	@Override
	public StringProperty getInstruction() {
		return instruction;
	}

	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}
	
}
