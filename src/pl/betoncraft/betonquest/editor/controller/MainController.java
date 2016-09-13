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
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.CompassTarget;
import pl.betoncraft.betonquest.editor.model.GlobalLocation;
import pl.betoncraft.betonquest.editor.model.GlobalVariable;
import pl.betoncraft.betonquest.editor.model.MainPageLine;
import pl.betoncraft.betonquest.editor.model.QuestCanceler;
import pl.betoncraft.betonquest.editor.model.StaticEvent;

/**
 * Controls "Main" tab.
 *
 * @author Jakub Sapalski
 */
public class MainController {
	
	private static MainController instance;
	
	@FXML private ListView<GlobalVariable> globVarList;
	@FXML private ListView<StaticEvent> staticEventsList;
	@FXML private ListView<GlobalLocation> globLocList;
	@FXML private ListView<QuestCanceler> cancelList;
	@FXML private ListView<MainPageLine> mainPageList;
	@FXML private ListView<CompassTarget> compassList;
	
	public MainController() {
		instance = this;
	}
	
	public static void setGlobVariables(ObservableList<GlobalVariable> globalVariables) {
		instance.globVarList.setItems(null);
		instance.globVarList.setItems(globalVariables);
	}
	
	public static void setStaticEvents(ObservableList<StaticEvent> staticEvents) {
		instance.staticEventsList.setItems(null);
		instance.staticEventsList.setItems(staticEvents);
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

	public static void setCompassTarget(ObservableList<CompassTarget> targets) {
		instance.compassList.setItems(null);
		instance.compassList.setItems(targets);
	}
	
//	@FXML private void addNpcBinding() {
//		try {
//			NpcBinding binding = new NpcBinding(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
//			if (binding.edit()) {
//				binding.setIndex(npcTable.getItems().size());
//				npcTable.getItems().add(binding);
//			}
//		} catch (Exception e) {
//			ExceptionController.display(e);
//		}
//	}
//	
//	@FXML private void editNpcBinding() {
//		try {
//			NpcBinding binding = npcTable.getSelectionModel().getSelectedItem();
//			if (binding != null) {
//				binding.edit();
//			}
//		} catch (Exception e) {
//			ExceptionController.display(e);
//		}
//	}
//	
//	@FXML private void delNpcBinding() {
//		try {
//			NpcBinding binding = npcTable.getSelectionModel().getSelectedItem();
//			if (binding != null) {
//				npcTable.getItems().remove(binding);
//			}
//		} catch (Exception e) {
//			ExceptionController.display(e);
//		}
//	}
//
//	@FXML public void npcKey(KeyEvent event) {
//		try {
//			keyAction(event, () -> addNpcBinding(), () -> editNpcBinding(), () -> delNpcBinding());
//		} catch (Exception e) {
//			ExceptionController.display(e);
//		}
//	}
	
	@FXML private void addVariable() {
		try {
			GlobalVariable variable = new GlobalVariable(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (variable.edit()) {
				variable.setIndex(globVarList.getItems().size());
				globVarList.getItems().add(variable);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editVariable() {
		try {
			GlobalVariable variable = globVarList.getSelectionModel().getSelectedItem();
			if (variable != null) {
				variable.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delVariable() {
		try {
			GlobalVariable variable = globVarList.getSelectionModel().getSelectedItem();
			if (variable != null) {
				globVarList.getItems().remove(variable);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void addStaticEvent() {
		try {
			StaticEvent staticEvent = new StaticEvent(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (staticEvent.edit()) {
				staticEvent.setIndex(staticEventsList.getItems().size());
				staticEventsList.getItems().add(staticEvent);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void editStaticEvent() {
		try {
			StaticEvent staticEvent = staticEventsList.getSelectionModel().getSelectedItem();
			if (staticEvent != null) {
				staticEvent.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delStaticEvent() {
		try {
			StaticEvent staticEvent = staticEventsList.getSelectionModel().getSelectedItem();
			if (staticEvent != null) {
				staticEventsList.getItems().remove(staticEvent);
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

	@FXML public void addCompassTarget() {
		try {
			CompassTarget target = new CompassTarget(BetonQuestEditor.getInstance().getDisplayedPackage(), new String());
			if (target.edit()) {
				compassList.getItems().add(target);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void editCompassTarget() {
		try {
			CompassTarget target = compassList.getSelectionModel().getSelectedItem();
			if (target != null) {
				target.edit();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void delCompassTarget() {
		try {
			CompassTarget target = compassList.getSelectionModel().getSelectedItem();
			if (target != null) {
				compassList.getItems().remove(target);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void varKey(KeyEvent event) {
		try {
			keyAction(event, () -> addVariable(), () -> editVariable(), () -> delVariable());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void statKey(KeyEvent event) {
		try {
			keyAction(event, () -> addStaticEvent(), () -> editStaticEvent(), () -> delStaticEvent());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void locKey(KeyEvent event) {
		try {
			keyAction(event, () -> addGlobalLocation(), () -> editGlobalLocation(), () -> delGlobalLocation());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void cancKey(KeyEvent event) {
		try {
			keyAction(event, () -> addQuestCanceler(), () -> editQuestCanceler(), () -> delQuestCanceler());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void mplKey(KeyEvent event) {
		try {
			keyAction(event, () -> addMainPageLine(), () -> editMainPageLine(), () -> delMainPageLine());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void compassKey(KeyEvent event) {
		try {
			keyAction(event, () -> addCompassTarget(), () -> editCompassTarget(), () -> delCompassTarget());
		} catch (Exception e) {
			ExceptionController.display(e);
		}
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

	public static void clear() {
		instance.globVarList.setItems(FXCollections.observableArrayList());
		instance.staticEventsList.setItems(FXCollections.observableArrayList());
		instance.cancelList.setItems(FXCollections.observableArrayList());
		instance.globLocList.setItems(FXCollections.observableArrayList());
		instance.mainPageList.setItems(FXCollections.observableArrayList());
		instance.compassList.setItems(FXCollections.observableArrayList());
	}
	
}
