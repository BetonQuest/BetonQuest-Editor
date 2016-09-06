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

import java.awt.Desktop;
import java.net.URI;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Controls "About" window.
 * 
 * @author Jakub Sapalski
 */
public class AboutController {
	
	private Stage stage;
	
	@FXML private GridPane root;
	@FXML ImageView image;
	@FXML TextFlow text;
	
	@FXML public void close() {
		stage.close();
	}
	
	public static void display() {
		try {
			AboutController controller = (AboutController) BetonQuestEditor.createWindow("view/window/AboutWindow.fxml",
					"about", 600, 200);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			controller.image.setImage(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			Text title = new Text(BetonQuestEditor.getInstance().getLanguage().getString("betonquest-editor")
					+ System.lineSeparator());
			title.setFont(Font.font("System", FontWeight.BOLD, 20));
			Text version = new Text(BetonQuestEditor.getInstance().getLanguage().getString("version") + " "
					+ BetonQuestEditor.getInstance().getVesion() + System.lineSeparator());
			version.setFont(Font.font(12));
			Text copyright = new Text("Copyright © 2016 Jakub \"Co0sh\" Sapalski" + System.lineSeparator());
			copyright.setFont(Font.font(12));
			Hyperlink license = new Hyperlink(BetonQuestEditor.getInstance().getLanguage().getString("license"));
			license.setOnAction(event -> {
				try {
					Desktop.getDesktop().browse(new URI("https://www.gnu.org/licenses/gpl.txt"));
				} catch (Exception e) {
					ExceptionController.display(e);
				}
			});
			Hyperlink source = new Hyperlink(BetonQuestEditor.getInstance().getLanguage().getString("source"));
			source.setOnAction(event -> {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Co0sh/BetonQuest-Editor"));
				} catch (Exception e) {
					ExceptionController.display(e);
				}
			});
			controller.text.getChildren().addAll(title, version, copyright, license, source);
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
					controller.close();
				}
			});
			controller.stage.showAndWait();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

}
