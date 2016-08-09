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

import java.io.IOException;
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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.custom.AutoCompleteTextField;
import pl.betoncraft.betonquest.editor.custom.DraggableListCell;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.IdWrapper;

/**
 * Controls a pop-up window in which the user can edit a list.
 *
 * @author Jakub Sapalski
 */
public class SortedChoiceController<T extends ID> {
	
	private String labelText;
	private ObservableList<IdWrapper<T>> chosen;
	private ObservableList<T> available;
	private Creator<T> creator;

	@FXML private Label label;
	@FXML private ListView<IdWrapper<T>> list;
	@FXML private AutoCompleteTextField field;
	
	public void setData(String labelText, ObservableList<IdWrapper<T>> chosen, ObservableList<T> available, Creator<T> creator) {
		this.labelText = labelText;
		this.chosen = chosen;
		this.available = available;
		this.creator = creator;
		// fill the view
		refresh();
	}
	
	private void refresh() {
		chosen.sort((ID o1, ID o2) -> o1.getIndex() - o2.getIndex());
		available.sort((ID o1, ID o2) -> o1.getIndex() - o2.getIndex());
		label.setText(BetonQuestEditor.getInstance().getLanguage().getString(labelText));
		list.setCellFactory(param -> new DraggableListCell<IdWrapper<T>>());
		list.getItems().setAll(chosen);
		ObservableList<ID> toChoose = FXCollections.observableArrayList(available);
		for (IdWrapper<T> wrapped : chosen) {
			toChoose.remove(wrapped.get());
		}
		field.getEntries().clear();
		for (ID id : toChoose) {
			field.getEntries().add(id.toString());
		}
	}

	@FXML private void add() {
		String name = field.getText();
		// check if name is not null
		if (name == null || name.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(BetonQuestEditor.getInstance().getLanguage().getString("name-not-null"));
			alert.showAndWait();
			return;
		}
		// check if it's not already there
		for (IdWrapper<T> wrapped : chosen) {
			if (wrapped.getId().get().equals(name)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(BetonQuestEditor.getInstance().getLanguage().getString("already-exists"));
				alert.showAndWait();
				return;
			}
		}
		// add it
		T object = null;
		// check if it already exists
		for (T id : available) {
			if (id.getId().get().equals(name)) {
				object = id;
				break;
			}
		}
		// create one if not
		if (object == null) {
			object = (T) creator.create(name);
			object.edit();
			object.setIndex(available.size());
			available.add(object);
		}
		IdWrapper<T> wrapped = new IdWrapper<T>(object);
		wrapped.setIndex(chosen.size());
		chosen.add(wrapped);
		refresh();
	}
	
	@FXML private void edit() {
		IdWrapper<T> object = list.getSelectionModel().getSelectedItem();
		if (object != null) {
			object.edit();
			refresh();
		}
	}
	
	@FXML private void delete() {
		IdWrapper<T> object = list.getSelectionModel().getSelectedItem();
		if (object != null) {
			chosen.remove(object);
			refresh();
		}
	}
	
	public static <E extends ID> void display(String labelText, ObservableList<IdWrapper<E>> chosen, ObservableList<E> available, Creator<E> creator) {
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
				BetonQuestEditor.refresh();
			});
			@SuppressWarnings("unchecked")
			SortedChoiceController<E> controller = (SortedChoiceController<E>) fxmlLoader.getController();
			controller.setData(labelText, chosen, available, creator);
			window.show();
		} catch (IOException e) {
			BetonQuestEditor.showStackTrace(e);
		}
	}
	
	public static interface Creator<E> {
		public E create(String name);
	}

}
