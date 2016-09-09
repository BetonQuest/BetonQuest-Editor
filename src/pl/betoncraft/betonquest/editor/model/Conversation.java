/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.controller.NameEditController;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.data.SimpleID;
import pl.betoncraft.betonquest.editor.data.Translatable;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Represents a conversation.
 *
 * @author Jakub Sapalski
 */
public class Conversation extends SimpleID implements Translatable {

	private TranslatableText npc;
	private BooleanProperty stop = new SimpleBooleanProperty();
	private ObservableList<NpcOption> npcOptions = FXCollections.observableArrayList();
	private ObservableList<PlayerOption> playerOptions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<NpcOption>> startingOptions = FXCollections.observableArrayList();
	private ObservableList<IdWrapper<Event>> finalEvents = FXCollections.observableArrayList();

	public Conversation(QuestPackage pack, String id) throws PackageNotFoundException {
		if (pack == null) {
			throw new PackageNotFoundException();
		}
		this.pack = ID.parsePackage(pack, id);
		this.id = new SimpleStringProperty(ID.parseId(id));
		npc = new TranslatableText(this);
	}

	@Override
	public boolean edit() {
		return NameEditController.display(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<Conversation> getList() {
		return pack.getConversations();
	}

	public BooleanProperty getStop() {
		return stop;
	}

	@Override
	public TranslatableText getText() {
		return npc;
	}
	
	@Override
	public String getType() {
		return BetonQuestEditor.getInstance().getLanguage().getString("conversation");
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
			option.getConversation().getNpcOptions().add(option);
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

}
