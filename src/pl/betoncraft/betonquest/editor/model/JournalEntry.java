/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.controller.JournalEntryEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.Translatable;

/**
 * Represents an entry in the journal.
 *
 * @author Jakub Sapalski
 */
public class JournalEntry extends SimpleID implements Translatable {
	
	private TranslatableText text;
	
	public JournalEntry(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
		text = new TranslatableText(this);
	}

	@Override
	public boolean edit() {
		return JournalEntryEditController.display(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<JournalEntry> getList() {
		return pack.getJournal();
	}

	@Override
	public TranslatableText getText() {
		return text;
	}

}
