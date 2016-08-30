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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.Conversation;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.GlobalLocation;
import pl.betoncraft.betonquest.editor.model.GlobalVariable;
import pl.betoncraft.betonquest.editor.model.MainPageLine;
import pl.betoncraft.betonquest.editor.model.NpcBinding;
import pl.betoncraft.betonquest.editor.model.QuestCanceler;
import pl.betoncraft.betonquest.editor.model.StaticEvent;

/**
 * Controls "Main" tab.
 *
 * @author Jakub Sapalski
 */
public class MainController {
	
	private static MainController instance;
	
	@FXML private TableView<NpcBinding> npcTable;
	@FXML private TableColumn<NpcBinding, String> npcName;
	@FXML private TableColumn<NpcBinding, Conversation> conversation;
	@FXML private TableView<GlobalVariable> globVarTable;
	@FXML private TableColumn<GlobalVariable, String> varName;
	@FXML private TableColumn<GlobalVariable, String> varValue;
	@FXML private TableView<StaticEvent> staticEventsTable;
	@FXML private TableColumn<StaticEvent, String> time;
	@FXML private TableColumn<StaticEvent, Event> event;
	@FXML private ListView<GlobalLocation> globLocList;
	@FXML private ListView<QuestCanceler> cancelList;
	@FXML private ListView<MainPageLine> mainPageList;
	
	public MainController() {
		instance = this;
	}

	public static void setNpcBindings(ObservableList<NpcBinding> bindings) {
		instance.npcTable.setItems(null);
		instance.npcTable.setItems(bindings);
		instance.npcName.setCellValueFactory(cell -> cell.getValue().getId());
		instance.conversation.setCellValueFactory(cell -> cell.getValue().getConversation());
	}
	
	public static void setGlobVariables(ObservableList<GlobalVariable> globalVariables) {
		instance.globVarTable.setItems(null);
		instance.globVarTable.setItems(globalVariables);
		instance.varName.setCellValueFactory(cell -> cell.getValue().getId());
		instance.varValue.setCellValueFactory(cell -> cell.getValue().getInstruction());
	}
	
	public static void setStaticEvents(ObservableList<StaticEvent> staticEvents) {
		instance.staticEventsTable.setItems(null);
		instance.staticEventsTable.setItems(staticEvents);
		instance.time.setCellValueFactory(cell -> cell.getValue().getId());
		instance.event.setCellValueFactory(cell -> cell.getValue().getEvent());
	}
	
	public static void setGlobalLocations(ObservableList<GlobalLocation> globalLocations) {
		instance.globLocList.setItems(null);
		instance.globLocList.setItems(globalLocations);
	}
	
	public static void setQuestCancelers(ObservableList<QuestCanceler> questCancelers) {
		instance.cancelList.setItems(null);
		instance.cancelList.setItems(questCancelers);
	}
	
	public static void setMainPageLines(ObservableList<MainPageLine> mainPageLines) {
		instance.mainPageList.setItems(null);
		instance.mainPageList.setItems(mainPageLines);
	}
	
	@FXML private void addNpcBinding() {
		try {
			NpcBinding binding = new NpcBinding(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (binding.edit()) {
				binding.setIndex(npcTable.getItems().size());
				npcTable.getItems().add(binding);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editNpcBinding() {
		try {
			NpcBinding binding = npcTable.getSelectionModel().getSelectedItem();
			if (binding != null) {
				binding.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delNpcBinding() {
		try {
			NpcBinding binding = npcTable.getSelectionModel().getSelectedItem();
			if (binding != null) {
				npcTable.getItems().remove(binding);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addVariable() {
		try {
			GlobalVariable variable = new GlobalVariable(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (variable.edit()) {
				variable.setIndex(globVarTable.getItems().size());
				globVarTable.getItems().add(variable);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editVariable() {
		try {
			GlobalVariable variable = globVarTable.getSelectionModel().getSelectedItem();
			if (variable != null) {
				variable.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delVariable() {
		try {
			GlobalVariable variable = globVarTable.getSelectionModel().getSelectedItem();
			if (variable != null) {
				globVarTable.getItems().remove(variable);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addStaticEvent() {
		try {
			StaticEvent staticEvent = new StaticEvent(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (staticEvent.edit()) {
				staticEvent.setIndex(staticEventsTable.getItems().size());
				staticEventsTable.getItems().add(staticEvent);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editStaticEvent() {
		try {
			StaticEvent staticEvent = staticEventsTable.getSelectionModel().getSelectedItem();
			if (staticEvent != null) {
				staticEvent.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delStaticEvent() {
		try {
			StaticEvent staticEvent = staticEventsTable.getSelectionModel().getSelectedItem();
			if (staticEvent != null) {
				staticEventsTable.getItems().remove(staticEvent);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addGlobalLocation() {
		try {
			GlobalLocation globalLocation = new GlobalLocation(null);
			if (globalLocation.edit()) {
				globLocList.getItems().add(globalLocation);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editGlobalLocation() {
		try {
			GlobalLocation globalLocation = globLocList.getSelectionModel().getSelectedItem();
			if (globalLocation != null) {
				globalLocation.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delGlobalLocation() {
		try {
			GlobalLocation globalLocation = globLocList.getSelectionModel().getSelectedItem();
			if (globalLocation != null) {
				globLocList.getItems().remove(globalLocation);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addQuestCanceler() {
		try {
			QuestCanceler questCanceler = new QuestCanceler(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (questCanceler.edit()) {
				cancelList.getItems().add(questCanceler);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editQuestCanceler() {
		try {
			QuestCanceler questCanceler = cancelList.getSelectionModel().getSelectedItem();
			if (questCanceler != null) {
				questCanceler.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delQuestCanceler() {
		try {
			QuestCanceler questCanceler = cancelList.getSelectionModel().getSelectedItem();
			if (questCanceler != null) {
				cancelList.getItems().remove(questCanceler);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addMainPageLine() {
		try {
			MainPageLine mainPageLine = new MainPageLine(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (mainPageLine.edit()) {
				mainPageList.getItems().add(mainPageLine);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editMainPageLine() {
		try {
			MainPageLine mainPageLine = mainPageList.getSelectionModel().getSelectedItem();
			if (mainPageLine != null) {
				mainPageLine.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delMainPageLine() {
		try {
			MainPageLine mainPageLine = mainPageList.getSelectionModel().getSelectedItem();
			if (mainPageLine != null) {
				mainPageList.getItems().remove(mainPageLine);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void npcKey(KeyEvent event) {
		keyAction(event, () -> addNpcBinding(), () -> editNpcBinding(), () -> delNpcBinding());
	}

	@FXML public void varKey(KeyEvent event) {
		keyAction(event, () -> addVariable(), () -> editVariable(), () -> delVariable());
	}

	@FXML public void statKey(KeyEvent event) {
		keyAction(event, () -> addStaticEvent(), () -> editStaticEvent(), () -> delStaticEvent());
	}

	@FXML public void locKey(KeyEvent event) {
		keyAction(event, () -> addGlobalLocation(), () -> editGlobalLocation(), () -> delGlobalLocation());
	}

	@FXML public void cancKey(KeyEvent event) {
		keyAction(event, () -> addQuestCanceler(), () -> editQuestCanceler(), () -> delQuestCanceler());
	}

	@FXML public void mplKey(KeyEvent event) {
		keyAction(event, () -> addMainPageLine(), () -> editMainPageLine(), () -> delMainPageLine());
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
	
}
