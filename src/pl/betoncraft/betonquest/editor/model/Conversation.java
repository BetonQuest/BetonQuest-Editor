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
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.data.TranslatableText;

/**
 * Represents a conversation.
 *
 * @author Jakub Sapalski
 */
public class Conversation implements ID {

	private QuestPackage pack;
	private StringProperty id;
	private int index = -1;
	private TranslatableText npc = new TranslatableText();
	private BooleanProperty stop = new SimpleBooleanProperty();
	private ObservableList<NpcOption> npcOptions = FXCollections.observableArrayList();
	private ObservableList<PlayerOption> playerOptions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<NpcOption>> startingOptions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<Event>> finalEvents = FXCollections.observableArrayList();

	public Conversation(QuestPackage pack, String id) {
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
	}

	@Override
	public EditResult edit() {
		return NameEditController.display(id);
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
	public ObservableList<Conversation> getList() {
		return pack.getConversations();
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
	
	public NpcOption getNpcOption(String id) {
		return getByID(npcOptions, id);
	}
	
	public PlayerOption getPlayerOption(String id) {
		return getByID(playerOptions, id);
	}
	
	public NpcOption newNpcOption(String id) {
		NpcOption option = getByID(npcOptions, id);
		if (option == null) {
			option = new NpcOption(this, id);
			npcOptions.add(option);
		}
		return option;
	}
	
	public PlayerOption newPlayerOption(String id) {
		PlayerOption option = getByID(playerOptions, id);
		if (option == null) {
			option = new PlayerOption(this, id);
			playerOptions.add(option);
		}
		return option;
	}

	public ObservableList<IdWrapper<NpcOption>> getStartingOptions() {
		return startingOptions;
	}

	public ObservableList<IdWrapper<Event>> getFinalEvents() {
		return finalEvents;
	}
	
	private <T extends ConversationOption> T getByID(ObservableList<T> list, String id) {
		for (T object : list) {
			if (object.getId().get().equals(id)) {
				return object;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return BetonQuestEditor.getInstance().getDisplayedPackage().equals(pack) ? id.get() : pack.getName().get() + "." + id.get();
	}

}
