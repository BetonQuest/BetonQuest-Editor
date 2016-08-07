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
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.AutoCompleteTextField;
import pl.betoncraft.betonquest.editor.data.ID;
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
	@FXML private VBox optionPane;
	
	@FXML private TextField npc;
	@FXML private CheckBox stop;
	@FXML private ChoiceBox<NpcOption> startingOptionsChoice;
	@FXML private AutoCompleteTextField startingOptionsField;
	@FXML private ChoiceBox<Event> finalEventsChoice;
	@FXML private AutoCompleteTextField finalEventsField;
	
	@FXML private ListView<NpcOption> npcList;
	@FXML private ListView<PlayerOption> playerList;
	
	@FXML private TextField option;
	@FXML private ChoiceBox<Event> eventChoice;
	@FXML private AutoCompleteTextField eventField;
	@FXML private ChoiceBox<Condition> conditionChoice;
	@FXML private AutoCompleteTextField conditionField;
	
	@FXML private ListView<ConversationOption> pointsToList;
	@FXML private AutoCompleteTextField pointsToField;
	@FXML private ListView<ConversationOption> pointedByList;
	
	private Conversation currentConversation;
	private ConversationOption currentOption;
	
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
	public synchronized void displayConversation(Conversation conversation) {
		// this selects the current conversation in the ChoiceBox if it's not already selected
		if (instance.conversation.getSelectionModel().isEmpty() || !instance.conversation.getValue().equals(conversation)) {
			instance.conversation.getSelectionModel().select(conversation);
			// selecting a conversation triggers this method again, return to prevent selecting twice
			return;
		}
		clearConversation();
		currentConversation = conversation;
		String lang = conversation.getPack().getDefLang();
		npc.textProperty().bindBidirectional(conversation.getNPC().get(lang));
		stop.selectedProperty().bindBidirectional(conversation.getStop());
		startingOptionsChoice.setItems(conversation.getStartingOptions());
		if (currentConversation.getStartingOptions().size() > 0) {
			startingOptionsChoice.getSelectionModel().select(0);
		}
		ObservableList<NpcOption> notStartingOptions = FXCollections.observableArrayList(conversation.getNpcOptions());
		for (NpcOption option : conversation.getStartingOptions()) {
			notStartingOptions.remove(option);
		}
		SortedSet<String> notStartingOptionsSet = new TreeSet<>();
		for (NpcOption option : notStartingOptions) {
			notStartingOptionsSet.add(option.getId().get());
		}
		startingOptionsField.getEntries().addAll(notStartingOptionsSet);
		finalEventsChoice.setItems(conversation.getFinalEvents());
		if (currentConversation.getFinalEvents().size() > 0) {
			finalEventsChoice.getSelectionModel().select(0);
		}
		ObservableList<Event> notFinalEvents = FXCollections.observableArrayList(conversation.getPack().getEvents());
		for (Event event : conversation.getFinalEvents()) {
			notFinalEvents.remove(event);
		}
		SortedSet<String> notFinalEventsSet = new TreeSet<>();
		for (Event event : notFinalEvents) {
			notFinalEventsSet.add(event.getId().get());
		}
		finalEventsField.getEntries().addAll(notFinalEventsSet);
		npcList.setItems(conversation.getNpcOptions());
		playerList.setItems(conversation.getPlayerOptions());
		// make sure there's at least a single NPC option
		if (currentOption != null && (conversation.getNpcOptions().contains(currentOption)
				|| conversation.getPlayerOptions().contains(currentOption))) {
			displayOption(currentOption);
		} else {
			if (conversation.getNpcOptions().isEmpty()) {
				NpcOption option = conversation.newNpcOption("start");
				conversation.getStartingOptions().add(option);
				
			}
			displayOption(conversation.getNpcOptions().get(0));
		}
		conversationPane.setDisable(false);
	}
	
	/**
	 * Removes current conversation from the view.
	 */
	public void clearConversation() {
		if (currentConversation == null) {
			return;
		}
		npc.textProperty().unbindBidirectional(currentConversation.getNPC().get(currentConversation.getPack().getDefLang()));
		npc.clear();
		stop.selectedProperty().unbindBidirectional(currentConversation.getStop());
		stop.setSelected(false);
		startingOptionsChoice.setItems(FXCollections.observableArrayList());
		startingOptionsField.getEntries().clear();
		finalEventsChoice.setItems(FXCollections.observableArrayList());
		finalEventsField.getEntries().clear();
		npcList.setItems(FXCollections.observableArrayList());
		playerList.setItems(FXCollections.observableArrayList());
		clearOption();
		currentConversation = null;
		conversationPane.setDisable(true);
	}
	
	/**
	 * Displays the option on the option side.
	 * 
	 * @param option ConversationOption to display
	 */
	public void displayOption(ConversationOption option) {
		clearOption();
		if (option instanceof NpcOption) {
			npcList.getSelectionModel().select((NpcOption) option);
		} else {
			playerList.getSelectionModel().select((PlayerOption) option);
		}
		currentOption = option;
		this.option.textProperty().bindBidirectional(option.getText().get(currentConversation.getPack().getDefLang()));
		eventChoice.setItems(option.getEvents());
		if (option.getEvents().size() > 0) {
			eventChoice.getSelectionModel().select(0);
		}
		ObservableList<Event> notEvents = FXCollections.observableArrayList(currentConversation.getPack().getEvents()); 
		for (Event event : option.getEvents()) {
			notEvents.remove(event);
		}
		SortedSet<String> notEventsSet = new TreeSet<>();
		for (Event event : notEvents) {
			notEventsSet.add(event.getId().get());
		}
		eventField.getEntries().addAll(notEventsSet);
		conditionChoice.setItems(option.getConditions());
		if (option.getConditions().size() > 0) {
			conditionChoice.getSelectionModel().select(0);
		}
		ObservableList<Condition> notConditions = FXCollections.observableArrayList(currentConversation.getPack().getConditions());
		for (Condition condition : option.getConditions()) {
			notConditions.remove(condition);
		}
		SortedSet<String> notConditionsSet = new TreeSet<>();
		for (Condition condition : notConditions) {
			notConditionsSet.add(condition.getId().toString());
		}
		conditionField.getEntries().addAll(notConditionsSet);
		pointsToList.setItems(option.getPointers());
		ObservableList<? extends ConversationOption> oppositeOptions;
		if (option instanceof NpcOption) {
			oppositeOptions = currentConversation.getPlayerOptions();
		} else {
			oppositeOptions = currentConversation.getNpcOptions();
		}
		SortedSet<String> oppositeOptionsSet = new TreeSet<>();
		for (ConversationOption o : oppositeOptions) {
			oppositeOptionsSet.add(o.getId().get());
		}
		pointsToField.getEntries().addAll(oppositeOptionsSet);
		ObservableList<ConversationOption> pointedByOptions = FXCollections.observableArrayList();
		for (ConversationOption opposite : oppositeOptions) {
			if (opposite.getPointers().contains(option)) {
				pointedByOptions.add(opposite);
			}
		}
		pointedByList.setItems(pointedByOptions);
		optionPane.setDisable(false);
	}
	
	public void clearOption() {
		if (currentOption == null) {
			return;
		}
		option.textProperty().unbindBidirectional(currentOption.getText().get(currentConversation.getPack().getDefLang()));
		option.clear();
		eventChoice.setItems(FXCollections.observableArrayList());
		eventField.getEntries().clear();
		eventField.clear();
		conditionChoice.setItems(FXCollections.observableArrayList());
		conditionField.getEntries().clear();
		conditionField.clear();
		pointedByList.setItems(FXCollections.observableArrayList());
		pointsToList.setItems(FXCollections.observableArrayList());
		playerList.getSelectionModel().clearSelection();
		npcList.getSelectionModel().clearSelection();
		currentOption = null;
		optionPane.setDisable(true);
	}
	
	@FXML private void selectConversation() {
		Conversation selected = conversation.getValue();
		if (selected == null) {
			return;
		}
		displayConversation(selected);
	}
	
	@FXML private void clickNpcOption() {
		NpcOption option = npcList.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void clickPlayerOption() {
		PlayerOption option = playerList.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void clickPointsTo(MouseEvent event) {
		if (event.getClickCount() == 2) {
			ConversationOption option = pointsToList.getSelectionModel().getSelectedItem();
			if (option == null) {
				return;
			}
			displayOption(option);
		}
	}
	
	@FXML private void clickPointedBy(MouseEvent event) {
		if (event.getClickCount() == 2) {
			ConversationOption option = pointedByList.getSelectionModel().getSelectedItem();
			if (option == null) {
				return;
			}
			displayOption(option);
		}
	}
	
	@FXML private void clickStartingOptions() {
		ConversationOption option = startingOptionsChoice.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void addStartingOption() {
		String name = startingOptionsField.getText();
		NpcOption option = getById(currentConversation.getNpcOptions(), name);
		if (option == null) {
			option = currentConversation.newNpcOption(name);
		}
		if (!startingOptionsChoice.getItems().contains(option)) {
			startingOptionsChoice.getItems().add(option);
			BetonQuestEditor.refresh();
			displayOption(option);
		}
	}
	
	@FXML private void delStartingOption() {
		NpcOption option = startingOptionsChoice.getValue();
		if (option != null) {
			if (startingOptionsChoice.getItems().size() == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Cannot delete last starting option");
				alert.show();
				return;
			}
			startingOptionsChoice.getItems().remove(option);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addFinalEvent() {
		String name = finalEventsField.getText();
		Event event = getById(currentConversation.getPack().getEvents(), name);
		if (event == null) {
			event = currentConversation.getPack().newEvent(name);
		}
		if (!finalEventsChoice.getItems().contains(event)) {
			finalEventsChoice.getItems().add(event);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delFinalEvent() {
		Event event = finalEventsChoice.getValue();
		if (event != null) {
			finalEventsChoice.getItems().remove(event);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addEvent() {
		String name = eventField.getText();
		Event event = getById(currentConversation.getPack().getEvents(), name);
		if (event == null) {
			event = currentConversation.getPack().newEvent(name);
		}
		if (!eventChoice.getItems().contains(event)) {
			eventChoice.getItems().add(event);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delEvent() {
		Event event = eventChoice.getValue();
		if (event != null) {
			eventChoice.getItems().remove(event);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addCondition() {
		String name = conditionField.getText();
		Condition condition = getById(currentConversation.getPack().getConditions(), name);
		if (condition == null) {
			condition = currentConversation.getPack().newCondition(name);
		}
		if (!conditionChoice.getItems().contains(condition)) {
			conditionChoice.getItems().add(condition);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delCondition() {
		Condition condition = conditionChoice.getValue();
		if (condition != null) {
			conditionChoice.getItems().remove(condition);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addPointer() {
		String name = pointsToField.getText();
		ConversationOption option;
		if (currentOption instanceof NpcOption) {
			option = getById(currentConversation.getPlayerOptions(), name);
			if (option == null) {
				option = currentConversation.newPlayerOption(name);
			}
		} else {
			option = getById(currentConversation.getNpcOptions(), name);
			if (option == null) {
				option = currentConversation.newNpcOption(name);
			}
		}
		pointsToList.getItems().add(option);
		BetonQuestEditor.refresh();
	}
	
	@FXML private void delPointer() {
		ConversationOption option = getById(currentOption.getPointers(), pointsToField.getText());
		if (option != null) {
			pointsToList.getItems().remove(option);
		}
	}
	
	@FXML private void addNpcOption() {
		StringProperty name = new SimpleStringProperty();
		NameEditController.display(name);
		if (name.get() == null || name.get().isEmpty()) {
			return;
		}
		if (currentConversation.getNpcOption(name.get()) == null) {
			NpcOption option = currentConversation.newNpcOption(name.get());
			BetonQuestEditor.refresh();
			displayOption(option);
		}
	}
	
	@FXML private void renameNpcOption() {
		NpcOption option = npcList.getSelectionModel().getSelectedItem();
		if (option != null) {
			NameEditController.display(option.getId());
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delNpcOption() {
		NpcOption option = npcList.getSelectionModel().getSelectedItem();
		if (option != null) {
			if (npcList.getItems().size() == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(BetonQuestEditor.getInstance().getLanguage().getString("cannot-delete-last-option"));
				alert.show();
				return;
			}
			npcList.getItems().remove(option);
			displayOption((npcList.getItems().get(0)));
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addPlayerOption() {
		StringProperty name = new SimpleStringProperty();
		NameEditController.display(name);
		if (name.get() == null || name.get().isEmpty()) {
			return;
		}
		if (currentConversation.getPlayerOption(name.get()) == null) {
			PlayerOption option = currentConversation.newPlayerOption(name.get());
			BetonQuestEditor.refresh();
			displayOption(option);
		}
	}
	
	@FXML private void renamePlayerOption() {
		PlayerOption option = playerList.getSelectionModel().getSelectedItem();
		if (option != null) {
			NameEditController.display(option.getId());
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delPlayerOption() {
		PlayerOption option = playerList.getSelectionModel().getSelectedItem();
		if (option != null) {
			playerList.getItems().remove(option);
			if (playerList.getItems().size() > 0) {
				displayOption((playerList.getItems().get(0)));
			} else {
				clearOption();
			}
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addConversation() {
		StringProperty string = new SimpleStringProperty();
		NameEditController.display(string);
		String name = string.get();
		if (name == null || name.isEmpty()) {
			return;
		}
//		for (Conversation conv : currentConversation.getPack().getConversations()) {
//			if (name.equals(conv.getId().getName())) {
//				// TODO alert about name conflict
//			}
//		}
		Conversation conv = new Conversation(BetonQuestEditor.getInstance().getDisplayedPackage(), name);
		
		BetonQuestEditor.getInstance().getDisplayedPackage().getConversations().add(conv);
		displayConversation(conv);
		BetonQuestEditor.refresh();
	}
	
	@FXML private void renameConversation() {
		NameEditController.display(currentConversation.getId());
		BetonQuestEditor.refresh();
	}
	
	@FXML private void delConversation() {
		Conversation conv = conversation.getValue();
		if (conv != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText(BetonQuestEditor.getInstance().getLanguage().getString("confirm-action"));
			Optional<ButtonType> action = alert.showAndWait();
			if (action.isPresent() && action.get() == ButtonType.OK) {
				conversation.getItems().remove(conv);
				if (conversation.getItems().size() > 0) {
					displayConversation(conversation.getItems().get(0));
				} else {
					clearConversation();
				}
				BetonQuestEditor.refresh();
			}
		}
	}
	
	private <T extends ID> T getById(Collection<T> collection, String id) {
		for (T object : collection) {
			if (object.getId().get().equals(id)) {
				return object;
			}
		}
		return null;
	}

}
