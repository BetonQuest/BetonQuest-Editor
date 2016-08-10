/**
 * 
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
 * Represents an item.
 *
 * @author Jakub Sapalski
 */
public class Item implements Instruction {
	
	private StringProperty id;
	private QuestPackage pack;
	private int index = -1;
	private StringProperty instruction = new SimpleStringProperty();
	
	public Item(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}
	
	public Item(QuestPackage pack, String id, String instruction) {
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
	public ObservableList<Item> getList() {
		return pack.getItems();
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
