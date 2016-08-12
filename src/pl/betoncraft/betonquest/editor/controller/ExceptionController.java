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

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Controls an exception window.
 *
 * @author Jakub Sapalski
 */
public class ExceptionController {

	private Stage stage;
	@FXML private TextArea stackTrace;
	
	private void setException(Stage stage, Exception e) {
		this.stage = stage;
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		String string = stringWriter.toString();
		stackTrace.setText(string);
	}
	
	@FXML private void link() {
		try {
			Desktop.getDesktop().browse(new URI("https://github.com/Co0sh/BetonQuest-Editor/issues"));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@FXML private void copy() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        String string = stackTrace.getText();
		// add 4 spaces before each line so when pasted on GitHub it's formatted as code
        content.putString("    " + string.replace("\n", "\n    ").trim());
        clipboard.setContent(content);
	}
	
	@FXML private void close() {
		stage.close();
	}
	
	public static void display(Exception exception) {
		try {
			Stage window = new Stage();
			URL location = BetonQuestEditor.class.getResource("view/window/ExceptionWindow.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			VBox root = (VBox) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(BetonQuestEditor.class.getResource("resource/style.css").toExternalForm());
			window.setScene(scene);
			window.setTitle(resources.getString("exception"));
			window.getIcons().add(new Image(BetonQuestEditor.class.getResourceAsStream("resource/icon.png")));
			window.setHeight(500);
			window.setWidth(700);
			window.setResizable(false);
			ExceptionController controller = (ExceptionController) fxmlLoader.getController();
			controller.setException(window, exception);
			window.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
