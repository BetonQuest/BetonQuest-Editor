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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.Editable;
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
	
	@FXML private ComboBox<Conversation> conversation;
	
	@FXML private TextField npc;
	@FXML private CheckBox stop;
	@FXML private ChoiceBox<NpcOption> startingOptionsChoice;
	@FXML private ComboBox<NpcOption> startingOptionsCombo;
	@FXML private ChoiceBox<Event> finalEventsChoice;
	@FXML private ComboBox<Event> finalEventsCombo;
	
	@FXML private ListView<NpcOption> npcList;
	@FXML private TextField npcField;
	@FXML private ListView<PlayerOption> playerList;
	@FXML private TextField playerField;
	
	@FXML private TextField option;
	@FXML private ChoiceBox<Event> eventChoice;
	@FXML private ComboBox<Event> eventCombo;
	@FXML private ChoiceBox<Condition> conditionChoice;
	@FXML private ComboBox<Condition> conditionCombo;
	
	@FXML private ListView<ConversationOption> pointsToList;
	@FXML private ComboBox<ConversationOption> pointsToCombo;
	@FXML private ListView<ConversationOption> pointedByList;
	
	private Conversation currentConversation;
	private ConversationOption currentOption;
	private boolean firstSelection = true;
	
	public ConversationController() {
		instance = this;
	}
	
	public static void setConversations(ObservableList<Conversation> conversations) {
		instance.firstSelection = true;
		instance.conversation.setItems(null);
		instance.conversation.setItems(conversations);
		if (instance.currentConversation != null && conversations.contains(instance.currentConversation)) {
			instance.displayConversation(instance.currentConversation);
		} else {
			instance.displayConversation(conversations.get(0));
		}
	}
	
	public synchronized void displayConversation(Conversation conversation) {
		if (firstSelection) {
			firstSelection = false;
			this.conversation.getSelectionModel().select(conversation);
			return;
		}
		if (currentConversation != null) {
			npc.textProperty().unbindBidirectional(currentConversation.getNPC().get(
					currentConversation.getPack().getDefLang()));
			stop.selectedProperty().unbindBidirectional(currentConversation.getStop());
		}
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
		startingOptionsCombo.setItems(notStartingOptions);
		finalEventsChoice.setItems(conversation.getFinalEvents());
		if (currentConversation.getFinalEvents().size() > 0) {
			finalEventsChoice.getSelectionModel().select(0);
		}
		ObservableList<Event> notFinalEvents = FXCollections.observableArrayList(conversation.getPack().getEvents());
		for (Event event : conversation.getFinalEvents()) {
			notFinalEvents.remove(event);
		}
		finalEventsCombo.setItems(notFinalEvents);
		startingOptionsCombo.setItems(notStartingOptions);
		npcList.setItems(conversation.getNpcOptions());
		playerList.setItems(conversation.getPlayerOptions());
		if (currentOption != null && (conversation.getNpcOptions().contains(currentOption) || conversation.getPlayerOptions().contains(currentOption))) {
			displayOption(currentOption);
		} else {
			if (conversation.getNpcOptions().isEmpty()) {
				NpcOption option = conversation.newNpcOption("start");
				conversation.getStartingOptions().add(option);
				
			}
			displayOption(conversation.getNpcOptions().get(0));
		}
	}
	
	public void displayOption(ConversationOption option) {
		if (option instanceof NpcOption) {
			playerList.getSelectionModel().clearSelection();
			playerField.clear();
			npcList.getSelectionModel().select((NpcOption) option);
			npcField.setText(option.getId().get());
		} else {
			npcList.getSelectionModel().clearSelection();
			npcField.clear();
			playerList.getSelectionModel().select((PlayerOption) option);
			playerField.setText(option.getId().get());
		}
		String lang = currentConversation.getPack().getDefLang();
		if (currentOption != null) {
			this.option.textProperty().unbindBidirectional(currentOption.getText().get(lang));
		}
		currentOption = option;
		this.option.textProperty().bindBidirectional(option.getText().get(lang));
		eventChoice.setItems(option.getEvents());
		if (option.getEvents().size() > 0) {
			eventChoice.getSelectionModel().select(0);
		}
		ObservableList<Event> notEvents = FXCollections.observableArrayList(currentConversation.getPack().getEvents()); 
		for (Event event : option.getEvents()) {
			notEvents.remove(event);
		}
		eventCombo.setItems(notEvents);
		conditionChoice.setItems(option.getConditions());
		if (option.getConditions().size() > 0) {
			conditionChoice.getSelectionModel().select(0);
		}
		ObservableList<Condition> notConditions = FXCollections.observableArrayList(currentConversation.getPack().getConditions());
		for (Condition condition : option.getConditions()) {
			notConditions.remove(condition);
		}
		conditionCombo.setItems(notConditions);
		pointsToList.setItems(option.getPointers());
		ObservableList<? extends ConversationOption> oppositeOptions;
		if (option instanceof NpcOption) {
			oppositeOptions = currentConversation.getPlayerOptions();
		} else {
			oppositeOptions = currentConversation.getNpcOptions();
		}
		pointsToCombo.setItems(FXCollections.observableArrayList(oppositeOptions));
		ObservableList<ConversationOption> pointedByOptions = FXCollections.observableArrayList();
		for (ConversationOption opposite : oppositeOptions) {
			if (opposite.getPointers().contains(option)) {
				pointedByOptions.add(opposite);
			}
		}
		pointedByList.setItems(pointedByOptions);
	}
	
	@FXML private void selectConversation() {
		Conversation selected = conversation.getSelectionModel().getSelectedItem();
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
	
	@FXML private void clickPointsTo() {
		ConversationOption option = pointsToList.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void clickPointedBy() {
		ConversationOption option = pointedByList.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void clickStartingOptions() {
		ConversationOption option = startingOptionsChoice.getSelectionModel().getSelectedItem();
		if (option == null) {
			return;
		}
		displayOption(option);
	}
	
	@FXML private void addStartingOption() {
		NpcOption option = getObjectFromComboBox(startingOptionsCombo, e -> currentConversation.newNpcOption(e));
		if (option != null && !startingOptionsChoice.getItems().contains(option)) {
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
		Event event = getObjectFromComboBox(finalEventsCombo, e -> currentConversation.getPack().newEvent(e));
		if (event != null && !finalEventsChoice.getItems().contains(event)) {
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
		Event event = getObjectFromComboBox(eventCombo, e -> currentConversation.getPack().newEvent(e));
		if (event != null && !eventChoice.getItems().contains(event)) {
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
		Condition condition = getObjectFromComboBox(conditionCombo, e -> currentConversation.getPack().newCondition(e));
		if (condition != null && !conditionChoice.getItems().contains(condition)) {
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
		ConversationOption option;
		if (currentOption instanceof NpcOption) {
			option = getObjectFromComboBox(pointsToCombo, e -> currentConversation.newPlayerOption(e));
		} else {
			option = getObjectFromComboBox(pointsToCombo, e -> currentConversation.newNpcOption(e));
		}
		if (option != null) {
			pointsToList.getItems().add(option);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delPointer() {
		ConversationOption option = pointsToCombo.getValue();
		if (option != null) {
			pointsToList.getItems().remove(option);
		}
	}
	
	@FXML private void addNpcOption() {
		String name = npcField.getText();
		if (currentConversation.getNpcOption(name) == null) {
			NpcOption option = currentConversation.newNpcOption(name);
			BetonQuestEditor.refresh();
			displayOption(option);
		}
	}
	
	@FXML private void renameNpcOption() {
		NpcOption option = npcList.getSelectionModel().getSelectedItem();
		String text = npcField.getText();
		if (option != null && text != null) {
			option.getId().set(text);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delNpcOption() {
		NpcOption option = npcList.getSelectionModel().getSelectedItem();
		if (option != null) {
			if (npcList.getItems().size() == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Cannot delete last NPC option");
				alert.show();
				return;
			}
			npcList.getItems().remove(option);
			displayOption((npcList.getItems().get(0)));
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addPlayerOption() {
		String name = playerField.getText();
		if (currentConversation.getPlayerOption(name) == null) {
			PlayerOption option = currentConversation.newPlayerOption(name);
			BetonQuestEditor.refresh();
			displayOption(option);
		}
	}
	
	@FXML private void renamePlayerOption() {
		PlayerOption option = playerList.getSelectionModel().getSelectedItem();
		String text = playerField.getText();
		if (option != null && text != null) {
			option.getId().set(text);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delPlayerOption() {
		PlayerOption option = playerList.getSelectionModel().getSelectedItem();
		if (option != null) {
			playerList.getItems().remove(option);
			displayOption((playerList.getItems().get(0))); // TODO fix error when the list is empty
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void addConversation() {
		Conversation conversation = getObjectFromComboBox(this.conversation,
				e -> currentConversation.getPack().newConversation(e));
		if (conversation != null) {
			displayConversation(conversation);
		}
	}
	
	@FXML private void renameConversation() {
		String name = conversation.getEditor().getText();
		if (name != null) {
			currentConversation.getId().set(name);
			BetonQuestEditor.refresh();
		}
	}
	
	@FXML private void delConversation() {
		Conversation conv = conversation.getValue();
		if (conv != null) {
			conversation.getItems().remove(conv); // TODO add confirmation dialog
			displayConversation(conversation.getItems().get(0)); // TODO fix error when the list is empty
			BetonQuestEditor.refresh();
		}
	}
	
	private <T> T getObjectFromComboBox(ComboBox<T> combo, Converter<T> converter) {
		T object = combo.getValue();
		String text = combo.getEditor().getText();
		if (text == null) {
			return null;
		}
		if (object == null || !object.toString().equals(text)) {
			object = converter.convert(text);
			if (object instanceof Editable) {
				((Editable) object).edit();
			}
		}
		return object;
	}
	
	private interface Converter<T> {
		T convert(String id);
	}

}
