/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.controller.JournalEntryEditController;
import pl.betoncraft.betonquest.editor.data.Editable;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents an entry in the journal.
 *
 * @author Jakub Sapalski
 */
public class JournalEntry implements ID, Editable {
	
	private StringProperty id;
	private int index = -1;
	private TranslatableText text = new TranslatableText();
	
	public JournalEntry(String id) {
		this.id = new SimpleStringProperty(id);
	}
	
	public StringProperty getId() {
		return id;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public TranslatableText getText() {
		return text;
	}

	public void edit() {
		JournalEntryEditController.display(this);
	}

	@Override
	public String toString() {
		return id.get();
	}

}
