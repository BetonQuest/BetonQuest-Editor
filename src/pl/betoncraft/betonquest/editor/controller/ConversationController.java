/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2016  Jakub "Co0sh" Sapalski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.betoncraft.betonquest.editor.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.AutoCompleteTextField;
import pl.betoncraft.betonquest.editor.custom.ConditionListCell;
import pl.betoncraft.betonquest.editor.custom.DraggableListCell;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.model.Condition;
import pl.betoncraft.betonquest.editor.model.Conversation;
import pl.betoncraft.betonquest.editor.model.ConversationOption;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.NpcOption;
import pl.betoncraft.betonquest.editor.model.PlayerOption;

/**
 * Controls the "Conversation" tab.
 *
 * @author Jakub Sapalski
 */
public class ConversationController {
	
	private static ConversationController instance;
	
	@FXML private ChoiceBox<Conversation> conversation;
	@FXML private GridPane conversationPane;
	@FXML private HBox stopPane;
	@FXML private GridPane optionPane;
	
	@FXML private TextField npc;
	@FXML private CheckBox stop;
	@FXML private Button startingOptionsButton;
	@FXML private Button finalEventsButton;
	
	@FXML private ListView<NpcOption> npcList;
	@FXML private ListView<PlayerOption> playerList;
	
	@FXML private Label optionType;
	@FXML private TextArea option;
	@FXML private Button eventsButton;
	@FXML private Button conditionsButton;
	
	@FXML private Label pointsToLabel;
	@FXML private Label pointedByLabel;
	@FXML private ListView<IdWrapper<ConversationOption>> pointsToList;
	@FXML private AutoCompleteTextField pointsToField;
	@FXML private ListView<IdWrapper<ConversationOption>> pointedByList;
	
	private Conversation currentConversation;
	private ConversationOption currentOption;
	
	private StringProperty boundNPC;
	private BooleanProperty boundStop;
	private StringProperty boundText;
	
	public ConversationController() {
		instance = this;
	}
	
	/**
	 * Sets the list of conversation which can be displayed in this tab.
	 * 
	 * @param conversations list of conversations
	 */
	public static void setConversations(ObservableList<Conversation> conversations) {
		instance.conversation.setItems(conversations);
		if (instance.currentConversation != null && conversations.contains(instance.currentConversation)) {
			instance.displayConversation(instance.currentConversation);
		} else {
			if (conversations.size() > 0) {
				instance.displayConversation(conversations.get(0));
			}
		}
	}
	
	/**
	 * Displays this conversation in the tab.
	 * 
	 * @param conversation Conversation to display
	 */
	public void displayConversation(Conversation conversation) {
		// this selects the current conversation in the ChoiceBox if it's not already selected
		if (instance.conversation.getSelectionModel().isEmpty() || !instance.conversation.getValue().equals(conversation)) {
			instance.conversation.getSelectionModel().select(conversation);
			// selecting a conversation triggers this method again, return to prevent selecting twice
			return;
		}
		clearConversation();
		currentConversation = conversation;
		boundNPC = conversation.getText().get();
		npc.textProperty().bindBidirectional(boundNPC);
		boundStop = conversation.getStop();
		stop.selectedProperty().bindBidirectional(boundStop);
		startingOptionsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("starting-options") + " (" + conversation.getStartingOptions().size() + ")");
		finalEventsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("final-events") + " (" + conversation.getFinalEvents().size() + ")");
		npcList.setCellFactory(param -> new DraggableListCell<>());
		npcList.setItems(conversation.getNpcOptions());
		playerList.setCellFactory(param -> new DraggableListCell<>());
		playerList.setItems(conversation.getPlayerOptions());
		// make sure there's at least a single NPC option
		if (currentOption != null && (conversation.getNpcOptions().contains(currentOption)
				|| conversation.getPlayerOptions().contains(currentOption))) {
			displayOption(currentOption);
		} else {
			if (conversation.getNpcOptions().isEmpty()) {
				NpcOption option = conversation.newNpcOption("start");
				conversation.getStartingOptions().add(new IdWrapper<>(conversation.getPack(), option));
			}
			displayOption(conversation.getNpcOptions().get(0));
		}
		conversationPane.setDisable(false);
		stopPane.setDisable(false);
	}
	
	/**
	 * Displays the first available conversation or clears the view if there are no conversations.
	 */
	public void displayConversation() {
		if (conversation.getItems().size() > 0) {
			displayConversation(conversation.getItems().get(0));
		} else {
			clearConversation();
		}
	}
	
	/**
	 * Removes the current conversation from the view.
	 */
	public static void clearConversation() {
		if (instance.currentConversation == null) {
			return;
		}
		if (instance.boundNPC != null) {
			instance.npc.textProperty().unbindBidirectional(instance.boundNPC);
		}
		instance.boundNPC = null;
		instance.npc.clear();
		if (instance.boundStop != null) {
			instance.stop.selectedProperty().unbindBidirectional(instance.boundStop);
		}
		instance.boundStop = null;
		instance.stop.setSelected(false);
		instance.startingOptionsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("starting-options"));
		instance.finalEventsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("final-events"));
		instance.npcList.setItems(FXCollections.observableArrayList());
		instance.playerList.setItems(FXCollections.observableArrayList());
		instance.clearOption();
		instance.conversationPane.setDisable(true);
		instance.stopPane.setDisable(true);
	}
	
	/**
	 * Displays the option on the option side.
	 * 
	 * @param option ConversationOption to display
	 */
	public void displayOption(ConversationOption option) {
		clearOption();
		currentOption = option;
		// if the option belongs to another conversation, display that conversation
		if (!option.getConversation().equals(currentConversation)) {
			displayConversation(option.getConversation());
			return;
		}
		if (option instanceof NpcOption) {
			npcList.getSelectionModel().select((NpcOption) option);
		} else {
			playerList.getSelectionModel().select((PlayerOption) option);
		}
		if (option instanceof NpcOption) {
			optionType.setText(BetonQuestEditor.getInstance().getLanguage().getString("npc-option") + " \"" + currentOption.getId().get() + "\"");
			pointsToLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("points-to-player"));
			pointedByLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("pointed-by-player"));
		} else {
			optionType.setText(BetonQuestEditor.getInstance().getLanguage().getString("player-option") + " \"" + currentOption.getId().get() + "\"");
			pointsToLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("points-to-npc"));
			pointedByLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("pointed-by-npc"));
		}
		boundText = option.getText().get();
		this.option.textProperty().bindBidirectional(boundText);
		conditionsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("conditions") + " (" + option.getConditions().size() + ")");
		eventsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("events") + " (" + option.getEvents().size() + ")");
		pointsToList.setCellFactory(param -> new DraggableListCell<>());
		pointsToList.setItems(option.getPointers());
		ObservableList<? extends ConversationOption> oppositeOptions;
		if (option instanceof NpcOption) {
			oppositeOptions = currentConversation.getPack().getAllPlayerOptions();
		} else {
			oppositeOptions = currentConversation.getPack().getAllNpcOptions();
		}
		ObservableList<? extends ConversationOption> notPointers = FXCollections.observableArrayList(oppositeOptions);
		for (IdWrapper<ConversationOption> o : option.getPointers()) {
			notPointers.remove(o.get());
		}
		SortedSet<String> notPointersSet = new TreeSet<>();
		for (ConversationOption o : notPointers) {
			notPointersSet.add(o.toString());
		}
		pointsToField.getEntries().addAll(notPointersSet);
		ObservableList<IdWrapper<ConversationOption>> pointedByOptions = FXCollections.observableArrayList();
		for (ConversationOption opposite : oppositeOptions) {
			for (IdWrapper<ConversationOption> pointer : opposite.getPointers()) {
				if (pointer.get().equals(option)) {
					pointedByOptions.add(new IdWrapper<>(currentConversation.getPack(), opposite));
				}
			}
		}
		pointedByList.setCellFactory(param -> new DraggableListCell<>());
		pointedByList.setItems(pointedByOptions);
		optionPane.setDisable(false);
	}
	
	/**
	 * Removes the current option from the view.
	 */
	public void clearOption() {
		if (currentOption == null) {
			return;
		}
		optionType.setText(BetonQuestEditor.getInstance().getLanguage().getString("option"));
		if (boundText != null) {
			option.textProperty().unbindBidirectional(boundText);
		}
		boundText = null;
		option.clear();
		pointsToField.getEntries().clear();
		conditionsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("conditions"));
		eventsButton.setText(BetonQuestEditor.getInstance().getLanguage().getString("events"));
		pointsToLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("points-to"));
		pointedByLabel.setText(BetonQuestEditor.getInstance().getLanguage().getString("pointed-by"));
		pointedByList.setItems(FXCollections.observableArrayList());
		pointsToList.setItems(FXCollections.observableArrayList());
		playerList.getSelectionModel().clearSelection();
		npcList.getSelectionModel().clearSelection();
		optionPane.setDisable(true);
	}
	
	@FXML private void selectConversation() {
		try {
			Conversation selected = conversation.getValue();
			if (selected == null) {
				return;
			}
			displayConversation(selected);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void clickNpcOption() {
		try {
			NpcOption option = npcList.getSelectionModel().getSelectedItem();
			if (option == null) {
				return;
			}
			displayOption(option);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void clickPlayerOption() {
		try {
			PlayerOption option = playerList.getSelectionModel().getSelectedItem();
			if (option == null) {
				return;
			}
			displayOption(option);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void clickPointsTo(MouseEvent event) {
		try {
			if (event.getClickCount() == 2) {
				selectOption(pointsToList.getSelectionModel().getSelectedItem());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void clickPointedBy(MouseEvent event) {
		try {
			if (event.getClickCount() == 2) {
				selectOption(pointedByList.getSelectionModel().getSelectedItem());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	private void selectOption(IdWrapper<ConversationOption> option) {
		if (option == null) {
			return;
		}
		displayOption(option.get());
	}
	
	@FXML private void editStartingOptions() {
		try {
			SortedChoiceController.display("starting-options", currentConversation.getStartingOptions(),
					currentConversation.getPack().getAllNpcOptions(), name -> new NpcOption(currentConversation, name),
					() -> new DraggableListCell<>(), item -> new IdWrapper<>(currentConversation.getPack(), item));
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void editFinalEvents() {
		try {
			SortedChoiceController.display("final-events", currentConversation.getFinalEvents(),
					BetonQuestEditor.getInstance().getAllEvents(), name -> new Event(currentConversation.getPack(), name),
					() -> new DraggableListCell<>(), item -> new IdWrapper<>(currentConversation.getPack(), item));
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void editConditions() {
		try {
			SortedChoiceController.display("conditions", currentOption.getConditions(),
					BetonQuestEditor.getInstance().getAllConditions(), name -> new Condition(currentConversation.getPack(), name),
					() -> new ConditionListCell(), item -> new ConditionWrapper(currentConversation.getPack(), item));
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void editEvents() {
		try {
			SortedChoiceController.display("events", currentOption.getEvents(),
					BetonQuestEditor.getInstance().getAllEvents(), name -> new Event(currentConversation.getPack(), name),
					() -> new DraggableListCell<>(), item -> new IdWrapper<>(currentConversation.getPack(), item));
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addPointer() {
		try {
			String name = pointsToField.getText();
			pointsToField.clear();
			ConversationOption option;
			if (currentOption instanceof NpcOption) {
				option = getById(currentConversation.getPack().getAllPlayerOptions(), name);
				if (option == null) {
					option = currentConversation.newPlayerOption(name);
				}
			} else {
				option = getById(currentConversation.getPack().getAllNpcOptions(), name);
				if (option == null) {
					option = currentConversation.newNpcOption(name);
				}
			}
			IdWrapper<ConversationOption> wrapped = new IdWrapper<>(currentConversation.getPack(), option);
			wrapped.setIndex(pointsToList.getItems().size());
			pointsToList.getItems().add(wrapped);
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delPointer() {
		try {
			IdWrapper<ConversationOption> option = pointsToList.getSelectionModel().getSelectedItem();
			if (option != null) {
				pointsToList.getItems().remove(option);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addNpcOption() {
		try {
			StringProperty name = new SimpleStringProperty();
			NameEditController.display(name);
			if (name.get() == null || name.get().isEmpty()) {
				return;
			}
			if (currentConversation.getNpcOption(name.get()) == null) {
				NpcOption option = currentConversation.newNpcOption(name.get());
				option.setIndex(currentConversation.getNpcOptions().size() - 1);
				BetonQuestEditor.getInstance().refresh();
				displayOption(option);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void renameNpcOption() {
		try {
			NpcOption option = npcList.getSelectionModel().getSelectedItem();
			if (option != null) {
				NameEditController.display(option.getId());
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delNpcOption() {
		try {
			NpcOption option = npcList.getSelectionModel().getSelectedItem();
			if (option != null) {
				if (npcList.getItems().size() == 1) {
					BetonQuestEditor.showError("cannot-delete-last-option");
					return;
				}
				npcList.getItems().remove(option);
				for (PlayerOption o : currentConversation.getPlayerOptions()) {
					o.getPointers().remove(option);
				}
				for (Iterator<IdWrapper<NpcOption>> iterator = currentConversation.getStartingOptions().iterator(); iterator.hasNext();) {
					if (iterator.next().get().equals(option)) {
						iterator.remove();
					}
				}
				displayOption((npcList.getItems().get(0)));
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addPlayerOption() {
		try {
			StringProperty name = new SimpleStringProperty();
			NameEditController.display(name);
			if (name.get() == null || name.get().isEmpty()) {
				return;
			}
			if (currentConversation.getPlayerOption(name.get()) == null) {
				PlayerOption option = currentConversation.newPlayerOption(name.get());
				option.setIndex(currentConversation.getPlayerOptions().size() - 1);
				BetonQuestEditor.getInstance().refresh();
				displayOption(option);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void renamePlayerOption() {
		try {
			PlayerOption option = playerList.getSelectionModel().getSelectedItem();
			if (option != null) {
				NameEditController.display(option.getId());
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delPlayerOption() {
		try {
			PlayerOption option = playerList.getSelectionModel().getSelectedItem();
			if (option != null) {
				playerList.getItems().remove(option);
				if (playerList.getItems().size() > 0) {
					displayOption((playerList.getItems().get(0)));
				} else {
					clearOption();
				}
				for (NpcOption o : currentConversation.getNpcOptions()) {
					o.getPointers().remove(option); // TODO fix removing wrapped value
				}
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addConversation() {
		try {
			StringProperty string = new SimpleStringProperty();
			NameEditController.display(string);
			String name = string.get();
			if (name == null || name.isEmpty()) {
				return;
			}
//			for (Conversation conv : currentConversation.getPack().getConversations()) {
//				if (name.equals(conv.getId().getName())) {
//					// TODO alert about name conflict
//				}
//			}
			Conversation conv = new Conversation(BetonQuestEditor.getInstance().getDisplayedPackage(), name);
			
			BetonQuestEditor.getInstance().getDisplayedPackage().getConversations().add(conv);
			displayConversation(conv);
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void renameConversation() {
		try {
			NameEditController.display(currentConversation.getId());
			BetonQuestEditor.getInstance().refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delConversation() {
		try {
			Conversation conv = conversation.getValue();
			if (conv != null && BetonQuestEditor.confirm("confirm-action")) {
				conversation.getItems().remove(conv);
				displayConversation();
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	private <T extends ID> T getById(Collection<T> collection, String id) {
		for (T object : collection) {
			if (object.toString().equals(id)) {
				return object;
			}
		}
		return null;
	}
	
	public static Conversation getDisplayedConversation() {
		return instance.currentConversation;
	}
	
	private void keyAction(KeyEvent event, Action add, Action edit, Action delete) {
		if (event.getCode() == KeyCode.DELETE) {
			if (delete != null) {
				delete.act();
			}
			event.consume();
			return;
		}
		if (event.getCode() == KeyCode.ENTER) {
			if (edit != null) {
				edit.act();
			}
			event.consume();
			return;
		}
		KeyCombination combintation = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
		if (combintation.match(event)) {
			if (add != null) {
				add.act();
			}
			event.consume();
			return;
		}
	}
	
	private interface Action {
		void act();
	}

	@FXML public void pointerKey(KeyEvent event) {
		keyAction(event, () -> addPointer(), () -> selectOption(pointsToList.getSelectionModel().getSelectedItem()), () -> delPointer());
	}

	@FXML public void playerKey(KeyEvent event) {
		keyAction(event, () -> addPlayerOption(), () -> renamePlayerOption(), () -> delPlayerOption());
	}

	@FXML public void npcKey(KeyEvent event) {
		keyAction(event, () -> addNpcOption(), () -> renameNpcOption(), () -> delNpcOption());
	}

}
