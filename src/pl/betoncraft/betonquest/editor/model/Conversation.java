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
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a conversation.
 *
 * @author Jakub Sapalski
 */
public class Conversation implements ID {

	private QuestPackage pack;
	private StringProperty convName;
	private int index = -1;
	private TranslatableText npc = new TranslatableText();
	private BooleanProperty stop = new SimpleBooleanProperty();
	private ObservableList<NpcOption> npcOptions = FXCollections.observableArrayList();
	private ObservableList<PlayerOption> playerOptions = FXCollections.observableArrayList();
	private ObservableList<NpcOption> startingOptions = FXCollections.observableArrayList();
	private ObservableList<Event> finalEvents = FXCollections.observableArrayList();

	public Conversation(QuestPackage pack, String convName) {
		this.pack = pack;
		this.convName = new SimpleStringProperty(convName);
	}
	
	public StringProperty getId() {
		return convName;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
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
	
	private static <T extends ConversationOption> T getByID(ObservableList<T> list, String id) {
		for (T object : list) {
			if (object.getId().get().equals(id)) {
				return object;
			}
		}
		return null;
	}

	public ObservableList<NpcOption> getNpcOptions() {
		return npcOptions;
	}
	
	public NpcOption newNpcOption(String id) {
		NpcOption option = getByID(npcOptions, id);
		if (option == null) {
			option = new NpcOption(id);
			npcOptions.add(option);
		}
		return option;
	}

	public ObservableList<PlayerOption> getPlayerOptions() {
		return playerOptions;
	}
	
	public PlayerOption newPlayerOption(String id) {
		PlayerOption option = getByID(playerOptions, id);
		if (option == null) {
			option = new PlayerOption(id);
			playerOptions.add(option);
		}
		return option;
	}

	public ObservableList<NpcOption> getStartingOptions() {
		return startingOptions;
	}

	public ObservableList<Event> getFinalEvents() {
		return finalEvents;
	}
	
	public NpcOption getNpcOption(String id) {
		return getByID(npcOptions, id);
	}
	
	public PlayerOption getPlayerOption(String id) {
		return getByID(playerOptions, id);
	}
	
	@Override
	public String toString() {
		return convName.get();
	}

}
