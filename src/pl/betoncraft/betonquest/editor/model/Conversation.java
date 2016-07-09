/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a conversation.
 *
 * @author Jakub Sapalski
 */
public class Conversation {

	private QuestPackage pack;
	private StringProperty convName;
	private TranslatableText npc = new TranslatableText();
	private BooleanProperty stop = new SimpleBooleanProperty();
	private ObservableList<NpcOption> npcOptions = FXCollections.observableArrayList();
	private ObservableList<PlayerOption> playerOptions = FXCollections.observableArrayList();
	private ObservableList<String> startingOptions = FXCollections.observableArrayList();
	private ObservableList<String> finalEvents = FXCollections.observableArrayList();

	public Conversation(QuestPackage pack, String convName) {
		this.pack = pack;
		this.convName = new SimpleStringProperty(convName);
	}
	
	public String getName() {
		return convName.get();
	}
	
	public void setName(String name) {
		convName.set(name);
	}

	public QuestPackage getPack() {
		return pack;
	}

	public BooleanProperty getStop() {
		return stop;
	}

	public TranslatableText getNPC() {
		return npc;
	}

	public ObservableList<NpcOption> getNpcOptions() {
		return npcOptions;
	}

	public ObservableList<PlayerOption> getPlayerOptions() {
		return playerOptions;
	}

	public ObservableList<String> getStartingOptions() {
		return startingOptions;
	}

	public ObservableList<String> getFinalEvents() {
		return finalEvents;
	}
	
	public NpcOption getNpcOption(String id) {
		for (NpcOption option : npcOptions) {
			if (option.getId().equals(id)) {
				return option;
			}
		}
		return null;
	}
	
	public PlayerOption getPlayerOption(String id) {
		for (PlayerOption option : playerOptions) {
			if (option.getId().equals(id)) {
				return option;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return convName.get();
	}

}
