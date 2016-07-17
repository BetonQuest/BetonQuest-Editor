/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.controller.InstructionEditController;
import pl.betoncraft.betonquest.editor.data.Editable;
import pl.betoncraft.betonquest.editor.data.Instruction;

/**
 * Represents an item.
 *
 * @author Jakub Sapalski
 */
public class Item implements Instruction, Editable {
	
	private StringProperty id;
	private StringProperty instruction = new SimpleStringProperty();
	
	public Item(String id, String instruction) {
		this(id);
		this.instruction.set(instruction);
	}
	
	public Item(String id) {
		this.id = new SimpleStringProperty(id);
	}

	@Override
	public StringProperty getId() {
		return id;
	}

	@Override
	public StringProperty getInstruction() {
		return instruction;
	}
	
	@Override
	public void edit() {
		InstructionEditController.display(this);
	}
	
	@Override
	public String toString() {
		return id.get();
	}

}
