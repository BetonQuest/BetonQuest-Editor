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

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Controls an exception window.
 *
 * @author Jakub Sapalski
 */
public class ExceptionController {

	private Stage stage;

	@FXML private Pane root;
	@FXML private TextArea stackTrace;
	
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
			ExceptionController controller = (ExceptionController) BetonQuestEditor
					.createWindow("view/window/ExceptionWindow.fxml", "exception", 700, 500);
			controller.stage = (Stage) controller.root.getScene().getWindow();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			exception.printStackTrace(printWriter);
			exception.printStackTrace(); // print to stderr as well
			String string = stringWriter.toString();
			controller.stackTrace.setText(string);
			controller.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
					controller.close();
				}
			});
			controller.stage.showAndWait();
		} catch (Exception e) {
			// there was an exception when trying to display another exception
			// it's sad because now the only thing which can be done is to print both exceptions to stderr
			exception.printStackTrace();
			e.printStackTrace();
		}
	}
	
}
