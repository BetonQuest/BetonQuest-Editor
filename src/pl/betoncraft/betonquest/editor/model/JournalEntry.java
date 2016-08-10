/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.JournalEntryEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents an entry in the journal.
 *
 * @author Jakub Sapalski
 */
public class JournalEntry implements ID {
	
	private StringProperty id;
	private QuestPackage pack;
	private int index = -1;
	private TranslatableText text = new TranslatableText();
	
	public JournalEntry(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}

	@Override
	public EditResult edit() {
		return JournalEntryEditController.display(this);
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
	public ObservableList<JournalEntry> getList() {
		return pack.getJournal();
	}

	public TranslatableText getText() {
		return text;
	}

	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}

}
