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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.data.Translatable;
import pl.betoncraft.betonquest.editor.model.TranslationManager;
import pl.betoncraft.betonquest.editor.model.Translator;

public class TranslationController {
	
	private static TranslationController instance;
	private TranslationManager manager;
	private Translator translator;
	
	@FXML ChoiceBox<String> language;
	@FXML Button convert;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML GridPane translations;
	@FXML Button translate;
	@FXML Label objectLabel;
	@FXML TextArea original;
	@FXML Button previous;
	@FXML TextArea translation;
	@FXML Button next;
	@FXML HBox choose;
	
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
			instance.translate.setDisable(true);
		} else {
			instance.language.setDisable(false);
			instance.language.getItems().setAll(manager.getLanguages().keySet());
			instance.language.getSelectionModel().select(manager.getDefault());
			instance.convert.setDisable(true);
			instance.add.setDisable(false);
			instance.edit.setDisable(false);
			instance.delete.setDisable(false);
			instance.translate.setDisable(false);
		}
	}

	@FXML public void setDefault() {
		try {
			String lang = language.getSelectionModel().getSelectedItem();
			if (lang != null && !lang.equals(manager.getDefault())) {
				BetonQuestEditor.getInstance().getDisplayedPackage().getTranslationManager().setDefault(lang);
				BetonQuestEditor.getInstance().refresh();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void add() {
		try {
			StringProperty lang = new SimpleStringProperty();
			if (NameEditController.display(lang)) {
				manager.createLanguage(lang.get());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void convert() {
		try {
			StringProperty lang = new SimpleStringProperty();
			if (NameEditController.display(lang)) {
				manager.convert(lang.get());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void edit() {
		try {
			StringProperty lang = new SimpleStringProperty(manager.getDefault());
			if (NameEditController.display(lang)) {
				manager.renameLanguage(manager.getDefault(), lang.get());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
		
	}

	@FXML public void delete() {
		try {
			if (BetonQuestEditor.confirm("confirm-action")) {
				manager.deleteLanguage(manager.getDefault());
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void translate() {
		try {
			StringProperty lang = new SimpleStringProperty();
			if (NameEditController.display(lang)) {
				if (!manager.getLanguages().containsKey(lang.get())) {
					manager.createLanguage(lang.get());
				}
				translator = new Translator(manager, manager.getDefault(), lang.get());
				choose.setDisable(true);
				translate.setVisible(false);
				translations.setVisible(true);
				next();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void previous() {
		try {
			translator.translate(translation.getText());
			if (translator.hasPrevious()) {
				translator.previous();
				display();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void next() {
		try {
			translator.translate(translation.getText());
			if (translator.hasNext()) {
				translator.next();
				display();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	private void display() {
		next.setDisable(!translator.hasNext());
		previous.setDisable(!translator.hasPrevious());
		String label = BetonQuestEditor.getInstance().getLanguage().getString("translate-from-to");
		Translatable object = translator.getObject();
		label = label.replace("{type}", object.getType());
		label = label.replace("{id}", object.toString());
		label = label.replace("{from}", translator.getFromLanguage());
		label = label.replace("{to}", translator.getToLanguage());
		objectLabel.setText(label);
		original.setText(translator.getText());
		translation.setText(translator.getTranslation());
	}

	@FXML public void translationKey(KeyEvent event) {
		try {
			KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
			KeyCombination backspace = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);
			if (enter.match(event)) {
				next();
			} else if (backspace.match(event)) {
				previous();
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML public void close() {
		try {
			translator = null;
			choose.setDisable(false);
			translate.setVisible(true);
			translations.setVisible(false);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	public static void clear() {
		instance.language.setItems(FXCollections.observableArrayList());
		instance.convert.setDisable(true);
		instance.add.setDisable(true);
		instance.edit.setDisable(true);
		instance.delete.setDisable(true);
		instance.close();
	}

}
