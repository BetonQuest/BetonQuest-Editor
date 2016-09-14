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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.AutoCompleteTextField;
import pl.betoncraft.betonquest.editor.custom.ConditionListCell;
import pl.betoncraft.betonquest.editor.data.ConditionWrapper;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Controls a pop-up window in which the user can edit a list.
 *
 * @author Jakub Sapalski
 */
public class SortedChoiceController<O extends ID, W extends IdWrapper<O>, F extends ListCell<W>> {
	
	@FXML private Pane root;
	private Stage stage;
	
	private String labelText;
	private ObservableList<W> chosen;
	private ObservableList<O> available;
	private Creator<O> creator;
	private CellFactory<F> cellFactory;
	private Wrapper<O, W> wrapper;
	private Refresher refresher;

	private final KeyCombination moveUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_DOWN);
	private final KeyCombination moveDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);
	private final KeyCombination invert = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);

	@FXML private Label label;
	@FXML private ListView<W> list;
	@FXML private AutoCompleteTextField field;
	
	private void refresh() {
		chosen.sort((ID o1, ID o2) -> o1.getIndex() - o2.getIndex());
		label.setText(BetonQuestEditor.getInstance().getLanguage().getString(labelText));
		list.setCellFactory(param -> cellFactory.getListCell());
		list.getItems().setAll(chosen);
		ObservableList<ID> toChoose = FXCollections.observableArrayList(available);
		for (W wrapped : chosen) {
			toChoose.remove(wrapped.get());
		}
		field.getEntries().clear();
		for (ID id : toChoose) {
			field.getEntries().add(id.toString());
		}
	}

	@FXML private void add() {
		try {
			String name = field.getText();
			field.clear();
			// check if name is not null
			if (name == null || name.isEmpty()) {
				BetonQuestEditor.showError("name-not-null");
				return;
			}
			// check if it's not already there
			for (W wrapped : chosen) {
				if (wrapped.get().toString().equals(name)) {
					BetonQuestEditor.showError("already-exists");
					return;
				}
			}
			// add it
			O object = null;
			// check if it already exists
			for (O id : available) {
				if (id.toString().equals(name)) {
					object = id;
					break;
				}
			}
			// create one if not
			if (object == null) {
				object = (O) creator.create(name);
				if (!object.needsEditing() || object.edit()) {
					ObservableList<O> list = object.getList();
					object.setIndex(list.size());
					list.add(object);
				} else {
					return;
				}
			}
			W wrapped = wrapper.wrap(object);
			wrapped.setIndex(chosen.size());
			chosen.add(wrapped);
			if (!available.contains(object)) {
				available.add(object);
			}
			refresh();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void edit() {
		try {
			IdWrapper<O> object = list.getSelectionModel().getSelectedItem();
			if (object != null) {
				object.edit();
				refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void delete() {
		try {
			IdWrapper<O> object = list.getSelectionModel().getSelectedItem();
			if (object != null) {
				chosen.remove(object);
				refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void key(KeyEvent event) {
		try {
			if (event.getCode() == KeyCode.ESCAPE) {
				stage.close();
				refresher.refresh();
				return;
			}
			if (event.getCode() == KeyCode.DELETE && list.isFocused()) {
				delete();
				return;
			}
			W item = list.getSelectionModel().getSelectedItem();
			if (item != null) {
				int index = list.getItems().indexOf(item);
				if (moveUp.match(event)) {
					if (index > 0) {
						list.getItems().set(index, list.getItems().get(index - 1));
						list.getItems().set(index - 1, item);
					}
					return;
				} else if (moveDown.match(event)) {
					if (index + 1 < list.getItems().size()) {
						list.getItems().set(index, list.getItems().get(index + 1));
						list.getItems().set(index + 1, item);
					}
					return;
				}
				list.getSelectionModel().select(item);
			}
			if (item instanceof ConditionWrapper && invert.match(event)) {
				ConditionListCell.invert((ConditionWrapper) item);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static <O extends ID, W extends IdWrapper<O>, F extends ListCell<W>> void display(String labelText,
			ObservableList<W> chosen, ObservableList<O> available, Creator<O> creator, CellFactory<F> cellFactory,
			Wrapper<O, W> wrapper) {
		display(labelText, chosen, available, creator, cellFactory, wrapper, () -> {
			BetonQuestEditor.getInstance().refresh();
		}); 
	}
	
	public static <O extends ID, W extends IdWrapper<O>, F extends ListCell<W>> void display(String labelText,
			ObservableList<W> chosen, ObservableList<O> available, Creator<O> creator, CellFactory<F> cellFactory,
			Wrapper<O, W> wrapper, Refresher refresher) {
		try {
			@SuppressWarnings("unchecked")
			SortedChoiceController<O, W, F> controller = (SortedChoiceController<O, W, F>) BetonQuestEditor
					.createWindow("view/window/SortedChoiceWindow.fxml", "choose-objects", 400, 500);
			if (controller == null) {
				return;
			}
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.labelText = labelText;
			controller.chosen = chosen;
			controller.available = available;
			controller.creator = creator;
			controller.cellFactory = cellFactory;
			controller.wrapper = wrapper;
			controller.refresher = refresher;
			controller.stage.setOnCloseRequest(event -> {
				refresher.refresh();
			});
			controller.list.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					controller.edit();
				}
			});
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> controller.key(event));
			controller.refresh(); // fill the view
			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static interface Creator<O> {
		// this shouldn't throw any exceptions, packages exist
		public O create(String name) throws PackageNotFoundException;
	}
	
	public static interface CellFactory<F> {
		public F getListCell();
	}
	
	public static interface Wrapper<O, W> {
		public W wrap(O item);
	}
	
	public static interface Refresher {
		public void refresh();
	}

}
