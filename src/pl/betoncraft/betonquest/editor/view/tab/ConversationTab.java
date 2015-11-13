/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
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
package pl.betoncraft.betonquest.editor.view.tab;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import pl.betoncraft.betonquest.editor.model.ConversationOption;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.NpcOption;
import pl.betoncraft.betonquest.editor.model.PlayerOption;
import pl.betoncraft.betonquest.editor.view.Tabs;

/**
 * The "conversation" tab of the package.
 *
 * @author Jakub Sapalski
 */
public class ConversationTab extends Tab {
	
	public final ComboBox<String> convName;
	public final Button addConv;
	public final Button editConv;
	public final Button deleteConv;
	
	public final TextField npcName;
	public final CheckBox stopOption;
	public final ChoiceBox<String> startOptions;
	public final ComboBox<String> availableOptions;
	public final Button addStartOption;
	public final Button delStartOption;
	public final ChoiceBox<String> finalEvents;
	public final ComboBox<String> availableFinalEvents;
	public final Button addFinEvent;
	public final Button delFinEvent;
	
	public final ListView<PlayerOption> playerOptions;
	public final TextField playerOptionField;
	public final Button addPlayerOption;
	public final Button editPlayerOption;
	public final Button delPlayerOption;
	public final ListView<NpcOption> npcOptions;
	public final TextField npcOptionField;
	public final Button addNpcOption;
	public final Button editNpcOption;
	public final Button delNpcOption;
	
	public final Label optionLabel;
	public final TextField textField;
	public final ChoiceBox<Event> eventList;
	public final ComboBox<String> eventChoice;
	public final Button addEvent;
	public final Button delEvent;
	public final ChoiceBox<Event> conditionList;
	public final ComboBox<String> conditionChoice;
	public final Button addCondition;
	public final Button delCondition;
	
	public final ListView<ConversationOption> pointerList;
	public final ComboBox<String> pointerName;
	public final Button addPointer;
	public final Button delPointer;
	public final ListView<ConversationOption> pointedList;
	

	/**
	 * Creates conversation tab.
	 * 
	 * @param tabs
	 *            instance of Tabs pane
	 */
	public ConversationTab(Tabs tabs) {
		setText("Conversations");
		
		// general setup
		VBox root = new VBox();
		HBox mainSplit = new HBox();
		VBox settingOptions = new VBox();
		HBox playerNPC = new HBox();
		VBox option = new VBox();
		HBox pointers = new HBox();
		
		// main conversation settings
		GridPane settingsGrid = new GridPane();
			// conversation choosing
		HBox convChoosing = new HBox();
		Label conversation = new Label("Conversation");
		convName = new ComboBox<>();
		addConv = new Button("Add");
		editConv = new Button("Rename");
		deleteConv = new Button("Delete");
		convChoosing.getChildren().addAll(conversation, convName, addConv, editConv, deleteConv);
			// NPC name
		Label name = new Label("NPC Name");
		npcName = new TextField();
		settingsGrid.add(name, 0, 1);
		settingsGrid.add(npcName, 1, 1);
			// stop option
		Label stop = new Label("Stop");
		stopOption = new CheckBox();
		settingsGrid.add(stop, 0, 2);
		settingsGrid.add(stopOption, 1, 2);
			// starting options
		Label start = new Label("Starting options");
		HBox startOptionBox = new HBox();
		startOptions = new ChoiceBox<>();
		availableOptions = new ComboBox<>();
		addStartOption = new Button("Add");
		delStartOption = new Button("Delete");
		startOptionBox.getChildren().addAll(startOptions, availableOptions, addStartOption, delStartOption);
		settingsGrid.add(start, 0, 3);
		settingsGrid.add(startOptionBox, 1, 3);
			// final events
		Label fin = new Label("Final events");
		HBox finalBox = new HBox();
		finalEvents = new ChoiceBox<>();
		availableFinalEvents = new ComboBox<>();
		addFinEvent = new Button("Add");
		delFinEvent = new Button("Delete");
		finalBox.getChildren().addAll(finalEvents, availableFinalEvents, addFinEvent, delFinEvent);
		settingsGrid.add(fin, 0, 4);
		settingsGrid.add(finalBox, 1, 4);
		
		// player and npc options
			// player options
		VBox playerOptionsBox = new VBox();
		Label playerOptionsLabel = new Label("Player Options");
		ButtonBar playerOptionButtons = new ButtonBar();
		playerOptions = new ListView<>();
		playerOptionField = new TextField();
		addPlayerOption = new Button("Add");
		editPlayerOption = new Button("Edit");
		delPlayerOption = new Button("Delete");
		playerOptionButtons.getButtons().addAll(addPlayerOption, editPlayerOption, delPlayerOption);
		playerOptionsBox.getChildren().addAll(playerOptionsLabel, playerOptions, playerOptionField, playerOptionButtons);
			// npc options
		VBox npcOptionsBox = new VBox();
		Label npcOptionsLabel = new Label("NPC Options");
		ButtonBar npcOptionButtons = new ButtonBar();
		npcOptions = new ListView<>();
		npcOptionField = new TextField();
		addNpcOption = new Button("Add");
		editNpcOption = new Button("Edit");
		delNpcOption = new Button("Delete");
		npcOptionButtons.getButtons().addAll(addNpcOption, editNpcOption, delNpcOption);
		npcOptionsBox.getChildren().addAll(npcOptionsLabel, npcOptions, npcOptionField, npcOptionButtons);
			// add it to the HBox
		playerNPC.getChildren().addAll(playerOptionsBox, npcOptionsBox);
		HBox.setHgrow(playerOptionsBox, Priority.ALWAYS);
		HBox.setHgrow(npcOptionsBox, Priority.ALWAYS);
		
		// add settings and playerNPC options to settingOptions
		settingOptions.getChildren().addAll(settingsGrid, playerNPC);
		
		// general option configuration
			// option settings
		optionLabel = new Label("Option");
		optionLabel.setId("option-label");
		GridPane optionSettings = new GridPane();
		Label textLabel = new Label("Text");
		textField = new TextField();
		optionSettings.add(textLabel, 0, 0);
		optionSettings.add(textField, 1, 0);
		Label eventLabel = new Label("Event");
		HBox eventBox = new HBox();
		eventList = new ChoiceBox<>();
		eventChoice = new ComboBox<>();
		addEvent = new Button("Add");
		delEvent = new Button("Delete");
		eventBox.getChildren().addAll(eventList, eventChoice, addEvent, delEvent);
		optionSettings.add(eventLabel, 0, 1);
		optionSettings.add(eventBox, 1, 1);
		Label condLabel = new Label("Conditions");
		HBox condBox = new HBox();
		conditionList = new ChoiceBox<>();
		conditionChoice = new ComboBox<>();
		addCondition = new Button("Add");
		delCondition = new Button("Delete");
		condBox.getChildren().addAll(conditionList, conditionChoice, addCondition, delCondition);
		optionSettings.add(condLabel, 0, 2);
		optionSettings.add(condBox, 1, 2);
			// pointers
		VBox pointerListBox = new VBox();
		Label pointerLabel = new Label("Pointers");
		pointerList = new ListView<>();
		pointerName = new ComboBox<String>();
		addPointer = new Button("Add");
		delPointer = new Button("Delete");
		pointerListBox.getChildren().addAll(pointerLabel, pointerList, pointerName, addPointer, delPointer);
		VBox pointedListBox = new VBox();
		Label pointedLabel = new Label("Pointed by");
		pointedList = new ListView<>();
		pointedListBox.getChildren().addAll(pointedLabel, pointedList);
		pointers.getChildren().addAll(pointerListBox, pointedListBox);
		HBox.setHgrow(pointerListBox, Priority.ALWAYS);
		HBox.setHgrow(pointedListBox, Priority.ALWAYS);
			// add it to the VBox
		option.getChildren().addAll(optionLabel, optionSettings, pointers);
		
		// add settingsOptions and option to mainSplit
		mainSplit.getChildren().addAll(settingOptions, new Separator(Orientation.VERTICAL), option);
		HBox.setHgrow(settingOptions, Priority.ALWAYS);
		HBox.setHgrow(option, Priority.ALWAYS);
		
		// add everything to root
		root.getChildren().addAll(convChoosing, new Separator(Orientation.HORIZONTAL), mainSplit);
		
		// make all lists expand to maximum height
		VBox.setVgrow(playerOptions, Priority.ALWAYS);
		VBox.setVgrow(npcOptions, Priority.ALWAYS);
		VBox.setVgrow(pointerList, Priority.ALWAYS);
		VBox.setVgrow(pointedList, Priority.ALWAYS);
		VBox.setVgrow(mainSplit, Priority.ALWAYS);
		VBox.setVgrow(playerNPC, Priority.ALWAYS);
		VBox.setVgrow(pointers, Priority.ALWAYS);
		
		// style everything
		settingsGrid.setHgap(10);
		settingsGrid.setVgap(5);
		optionSettings.setHgap(10);
		optionSettings.setVgap(5);
		for (HBox p : new HBox[]{convChoosing, startOptionBox, finalBox, eventBox, condBox}) {
			for (Node c : p.getChildren()) {
				HBox.setMargin(c, new Insets(0, 5, 0, 0));
			}
		}
		for (Pane p : new Pane[]{convChoosing, settingOptions, option, npcOptionsBox, playerOptionsBox, pointerListBox,
				pointedListBox}) {
			p.setPadding(new Insets(5));
		}
		for (VBox p : new VBox[]{npcOptionsBox, playerOptionsBox, pointerListBox, pointedListBox}) {
			for (Node c : p.getChildren()) {
				VBox.setMargin(c, new Insets(0, 0, 5, 0));
			}
		}
		for (Control c : new Control[]{convName, startOptions, availableOptions, finalEvents, availableFinalEvents,
				eventList, eventChoice, conditionList, conditionChoice}) {
			c.setPrefWidth(150);
		}
		for (ComboBox<?> c : new ComboBox[]{convName, availableOptions, availableFinalEvents, eventChoice,
				conditionChoice, pointerName}) {
			c.setEditable(true);
		}
		for (Button b : new Button[]{addConv, editConv, deleteConv, addStartOption, delStartOption, addFinEvent,
				delFinEvent, addEvent, delEvent, addCondition, delCondition}) {
			b.setPrefWidth(80);
		}
		pointerListBox.setFillWidth(true);
		for (Control c : new Control[]{pointerName, addPointer, delPointer}) {
			c.setMaxWidth(Double.MAX_VALUE);
		}
		setContent(root);
	}

}
