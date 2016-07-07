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
package pl.betoncraft.betonquest.editor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Main class for the application.
 * 
 * @author Jakub Sapalski
 */
public class BetonQuestEditor extends Application {
	
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(Locale.US);
		stage = primaryStage;
		try {
			URL location = getClass().getResource("view/Root.fxml");
			ResourceBundle resources = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			BorderPane root = (BorderPane) fxmlLoader.load();
			Scene scene = new Scene(root, 1280, 720);
			scene.getStylesheets().add(getClass().getResource("resource/style.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle(resources.getString("betonquest-editor"));
			stage.getIcons().add(new Image(getClass().getResourceAsStream("resource/icon.png")));
			stage.setMinHeight(600);
			stage.setMinWidth(800);
			stage.setMaximized(true);
			stage.show();
		} catch (Exception e) {
			showStackTrace(e);
		}
	}
	
	/**
	 * @return the primary stage of this application
	 */
	public static Stage getPrimaryStage() {
		return stage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Shows an exception to the user.
	 * 
	 * @param e
	 *            exception to show
	 */
	public static void showStackTrace(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception");
		alert.setHeaderText("There was some unexpected exception!");
		alert.setContentText("Please send this stack trace to the author: coosheck@gmail.com");

		// create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(false);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
