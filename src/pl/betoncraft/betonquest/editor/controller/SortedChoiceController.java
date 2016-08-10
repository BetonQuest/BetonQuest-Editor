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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.AutoCompleteTextField;
import pl.betoncraft.betonquest.editor.data.Editable.EditResult;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;

/**
 * Controls a pop-up window in which the user can edit a list.
 *
 * @author Jakub Sapalski
 */
public class SortedChoiceController<O extends ID, W extends IdWrapper<O>, F extends ListCell<W>> {
	
	private String labelText;
	private ObservableList<W> chosen;
	private ObservableList<O> available;
	private Creator<O> creator;
	private CellFactory<F> cellFactory;
	private Wrapper<O, W> wrapper;

	@FXML private Label label;
	@FXML private ListView<W> list;
	@FXML private AutoCompleteTextField field;
	
	public void setData(String labelText, ObservableList<W> chosen, ObservableList<O> available, Creator<O> creator, CellFactory<F> cellFactory, Wrapper<O, W> wrapper) {
		this.labelText = labelText;
		this.chosen = chosen;
		this.available = available;
		this.creator = creator;
		this.cellFactory = cellFactory;
		this.wrapper = wrapper;
		// fill the view
		refresh();
	}
	
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
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(BetonQuestEditor.getInstance().getLanguage().getString("name-not-null"));
				alert.showAndWait();
				return;
			}
			// check if it's not already there
			for (W wrapped : chosen) {
				if (wrapped.get().toString().equals(name)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(BetonQuestEditor.getInstance().getLanguage().getString("already-exists"));
					alert.showAndWait();
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
				if (object.edit() == EditResult.SUCCESS) {
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
			refresh();
		} catch (Exception e) {
			BetonQuestEditor.showStackTrace(e);
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
			BetonQuestEditor.showStackTrace(e);
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
			BetonQuestEditor.showStackTrace(e);
		}
	}
	
	public static <O extends ID, W extends IdWrapper<O>, F extends ListCell<W>> void display(String labelText,
			ObservableList<W> chosen, ObservableList<O> available, Creator<O> creator, CellFactory<F> cellFactory,
			Wrapper<O, W> wrapper) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/SortedChoiceWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			VBox root = (VBox) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("choose-objects"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(500);
			window.setWidth(400);
			window.setResizable(false);
			window.setOnCloseRequest(event -> {
				BetonQuestEditor.getInstance().refresh();
			});
			@SuppressWarnings("unchecked")
			SortedChoiceController<O, W, F> controller = (SortedChoiceController<O, W, F>) fxmlLoader.getController();
			controller.setData(labelText, chosen, available, creator, cellFactory, wrapper);
			window.showAndWait();
		} catch (Exception e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}
	
	public static interface Creator<O> {
		public O create(String name);
	}
	
	public static interface CellFactory<F> {
		public F getListCell();
	}
	
	public static interface Wrapper<O, W> {
		public W wrap(O item);
	}

}
