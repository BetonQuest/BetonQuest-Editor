/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a quest canceler.
 *
 * @author Jakub Sapalski
 */
public class QuestCanceler {
	
	private StringProperty id;
	private TranslatableText name = new TranslatableText();
	private ObservableList<String> conditions = FXCollections.observableArrayList();
	private ObservableList<String> events = FXCollections.observableArrayList();
	private ObservableList<String> objectives = FXCollections.observableArrayList();
	private ObservableList<String> tags = FXCollections.observableArrayList();
	private ObservableList<String> points = FXCollections.observableArrayList();
	private ObservableList<String> journal = FXCollections.observableArrayList();
	private StringProperty location = new SimpleStringProperty();
	
	public QuestCanceler(String id) {
		this.id = new SimpleStringProperty(id);
	}

	public String getId() {
		return id.get();
	}

	public void setId(String id) {
		this.id.set(id);
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

	public ObservableList<String> getConditions() {
		return conditions;
	}

	public ObservableList<String> getEvents() {
		return events;
	}

	public ObservableList<String> getObjectives() {
		return objectives;
	}

	public ObservableList<String> getTags() {
		return tags;
	}

	public ObservableList<String> getPoints() {
		return points;
	}

	public ObservableList<String> getJournal() {
		return journal;
	}
	
	@Override
	public String toString() {
		return id.get();
	}

}
