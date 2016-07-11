/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents an entry in the journal.
 *
 * @author Jakub Sapalski
 */
public class JournalEntry implements ID {
	
	private StringProperty id;
	private TranslatableText text = new TranslatableText();
	
	public JournalEntry(String id) {
		this.id = new SimpleStringProperty(id);
	}
	
	public StringProperty getId() {
		return id;
	}

	public TranslatableText getText() {
		return text;
	}

	@Override
	public String toString() {
		return id.get();
	}

}
