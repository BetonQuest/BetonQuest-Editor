/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents an item.
 *
 * @author Jakub Sapalski
 */
public class Item {
	
	private StringProperty id;
	private StringProperty instruction;
	
	public Item(String id, String instruction) {
		this.id = new SimpleStringProperty(id);
		this.instruction = new SimpleStringProperty(instruction);
	}
	
	public String getId() {
		return id.get();
	}
	public void setId(String id) {
		this.id.set(id);
	}
	public String getInstruction() {
		return instruction.get();
	}
	public void setInstruction(String instruction) {
		this.instruction.set(instruction);
	}
	
	@Override
	public String toString() {
		return id.get();
	}

}
