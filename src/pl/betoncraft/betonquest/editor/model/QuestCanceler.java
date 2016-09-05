/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.controller.QuestCancelerEditController;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.Translatable;

/**
 * Represents a quest canceler.
 *
 * @author Jakub Sapalski
 */
public class QuestCanceler extends SimpleID implements Translatable {

	private TranslatableText name;
	private ObservableList<ConditionWrapper> conditions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<Event>> events = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<Objective>> objectives = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<Tag>> tags = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<PointCategory>> points = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<JournalEntry>> journal = FXCollections.observableArrayList();
	private StringProperty location = new SimpleStringProperty();
	
	public QuestCanceler(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
		name = new TranslatableText(this);
	}

	@Override
	public boolean edit() {
		return QuestCancelerEditController.display(this);
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

	@Override
	public TranslatableText getText() {
		return name;
	}

	public ObservableList<ConditionWrapper> getConditions() {
		return conditions;
	}

	public ObservableList<IdWrapper<Event>> getEvents() {
		return events;
	}

	public ObservableList<IdWrapper<Objective>> getObjectives() {
		return objectives;
	}

	public ObservableList<IdWrapper<Tag>> getTags() {
		return tags;
	}

	public ObservableList<IdWrapper<PointCategory>> getPoints() {
		return points;
	}

	public ObservableList<IdWrapper<JournalEntry>> getJournal() {
		return journal;
	}

}
