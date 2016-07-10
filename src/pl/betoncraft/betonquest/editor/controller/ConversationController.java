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

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.betoncraft.betonquest.editor.model.Conversation;
import pl.betoncraft.betonquest.editor.model.ConversationOption;
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
	@FXML private ChoiceBox<String> startingOptionsChoice;
	@FXML private ComboBox<String> startingOptionsCombo;
	@FXML private ChoiceBox<String> finalEventsChoice;
	@FXML private ComboBox<String> finalEventsCombo;
	
	@FXML private ListView<NpcOption> npcList;
	@FXML private TextField npcField;
	@FXML private ListView<PlayerOption> playerList;
	@FXML private TextField playerField;
	
	@FXML private TextField option;
	@FXML private ChoiceBox<String> eventChoice;
	@FXML private ComboBox<String> eventCombo;
	@FXML private ChoiceBox<String> conditionChoice;
	@FXML private ComboBox<String> conditionCombo;
	
	@FXML private ListView<String> pointsToList;
	@FXML private ComboBox<String> pointsToCombo;
	@FXML private ListView<String> pointedByList;
	
	private Conversation currentConversation;
	// private ConversationOption currentOption;
	
	public ConversationController() {
		instance = this;
	}
	
	public static void setConversations(ObservableList<Conversation> conversations) {
		instance.conversation.setItems(conversations);
		instance.displayConversation(conversations.get(0));
	}
	
	public void displayConversation(Conversation conversation) {
		currentConversation = conversation;
		this.conversation.getSelectionModel().select(conversation);
		npc.textProperty().bindBidirectional(conversation.getNPC().get(conversation.getPack().getDefLang()));
		stop.selectedProperty().bindBidirectional(conversation.getStop());
		startingOptionsChoice.setItems(conversation.getStartingOptions());
		// TODO list of choosable starting options
		finalEventsChoice.setItems(conversation.getFinalEvents());
		// TODO list of choosable final events
		npcList.setItems(conversation.getNpcOptions());
		//Update displayed option if user selects one
		npcList.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> displayOption(newValue)
		);
		playerList.setItems(conversation.getPlayerOptions());
		//Update displayed option if user selects one
		playerList.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> displayOption(newValue)
		);
		if(conversation.getNpcOptions().size()>0) //Bugfix empty Conversation
			displayOption(conversation.getNpcOptions().get(0));
	}
	
	public void displayOption(ConversationOption option) {
		// currentOption = option;
		this.option.textProperty().bindBidirectional(option.getText().get(currentConversation.getPack().getDefLang()));
		eventChoice.setItems(option.getEvents());
		conditionChoice.setItems(option.getConditions());
		pointsToList.setItems(option.getPointers());
	}

}
