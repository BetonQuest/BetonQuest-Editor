/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a quest canceler.
 *
 * @author Jakub Sapalski
 */
public class QuestCanceler extends SimpleID {

	private TranslatableText name = new TranslatableText();
	private ObservableList<Condition> conditions = FXCollections.observableArrayList();
	private ObservableList<Event> events = FXCollections.observableArrayList();
	private ObservableList<Objective> objectives = FXCollections.observableArrayList();
	private ObservableList<String> tags = FXCollections.observableArrayList();
	private ObservableList<String> points = FXCollections.observableArrayList();
	private ObservableList<JournalEntry> journal = FXCollections.observableArrayList();
	private StringProperty location = new SimpleStringProperty();
	
	public QuestCanceler(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}

	@Override
	public EditResult edit() {
		return NameEditController.display(id); // TODO edit a quest canceler in a custom window
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<QuestCanceler> getList() {
		return pack.getCancelers();
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location.set(location);
	}

	public TranslatableText getName() {
		return name;
	}

	public ObservableList<Condition> getConditions() {
		return conditions;
	}

	public ObservableList<Event> getEvents() {
		return events;
	}

	public ObservableList<Objective> getObjectives() {
		return objectives;
	}

	public ObservableList<String> getTags() {
		return tags;
	}

	public ObservableList<String> getPoints() {
		return points;
	}

	public ObservableList<JournalEntry> getJournal() {
		return journal;
	}
	
	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}

}
