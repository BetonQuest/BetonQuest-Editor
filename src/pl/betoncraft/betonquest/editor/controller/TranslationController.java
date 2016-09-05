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
package pl.betoncraft.betonquest.editor.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.TranslationManager;
import javafx.scene.layout.GridPane;

public class TranslationController {
	
	private static TranslationController instance;
	private TranslationManager manager;
	
	@FXML ChoiceBox<String> language;
	@FXML Button convert;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML GridPane translations;
	@FXML Button translate;
	
	public TranslationController() {
		instance = this;
	}
	
	public static void setLanguages(TranslationManager manager) {
		instance.manager = manager;
		if (manager.getDefault() == null) {
			instance.language.getItems().clear();
			instance.language.setDisable(true);
			instance.convert.setDisable(false);
			instance.add.setDisable(true);
			instance.edit.setDisable(true);
			instance.delete.setDisable(true);
		} else {
			instance.language.setDisable(false);
			instance.language.getItems().setAll(manager.getLanguages().keySet());
			instance.language.getSelectionModel().select(manager.getDefault());
			instance.convert.setDisable(true);
			instance.add.setDisable(false);
			instance.edit.setDisable(false);
			instance.delete.setDisable(false);
		}
	}

	@FXML public void setDefault() {
		String lang = language.getSelectionModel().getSelectedItem();
		if (lang != null && !lang.equals(manager.getDefault())) {
			BetonQuestEditor.getInstance().getDisplayedPackage().getTranslationManager().setDefault(lang);
			BetonQuestEditor.getInstance().refresh();
		}
	}

	@FXML public void add() {
		StringProperty lang = new SimpleStringProperty();
		if (NameEditController.display(lang)) {
			manager.createLanguage(lang.get());
		}
	}

	@FXML public void convert() {
		StringProperty lang = new SimpleStringProperty();
		if (NameEditController.display(lang)) {
			manager.convert(lang.get());
		}
	}

	@FXML public void edit() {
		StringProperty lang = new SimpleStringProperty(manager.getDefault());
		if (NameEditController.display(lang)) {
			manager.renameLanguage(manager.getDefault(), lang.get());
		}
		
	}

	@FXML public void delete() {
		if (BetonQuestEditor.confirm("confirm-action")) {
			manager.deleteLanguage(manager.getDefault());
		}
	}

	@FXML public void translate() {
		// TODO translate language
	}

}
