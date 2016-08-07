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
package pl.betoncraft.betonquest.editor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipFile;

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
import pl.betoncraft.betonquest.editor.controller.ConversationController;
import pl.betoncraft.betonquest.editor.controller.EcoController;
import pl.betoncraft.betonquest.editor.controller.MainController;
import pl.betoncraft.betonquest.editor.controller.OtherController;
import pl.betoncraft.betonquest.editor.model.QuestPackage;

/**
 * Main class for the application.
 * 
 * @author Jakub Sapalski
 */
public class BetonQuestEditor extends Application {

	private static BetonQuestEditor instance;
	private Stage stage;
	private ResourceBundle language;
	
	private HashMap<String, QuestPackage> loadedPackages = new HashMap<>();
	private QuestPackage currentPackage;
	private static File autoLoadPackage;
	private static File autoSavePackage;
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		Locale.setDefault(Locale.US); // TODO remove this later
		stage = primaryStage;
		try {
			URL location = getClass().getResource("view/Root.fxml");
			language = ResourceBundle.getBundle("pl.betoncraft.betonquest.editor.resource.lang.lang");
			FXMLLoader fxmlLoader = new FXMLLoader(location, language);
			BorderPane root = (BorderPane) fxmlLoader.load();
			Scene scene = new Scene(root, 1280, 720);
			scene.getStylesheets().add(getClass().getResource("resource/style.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle(language.getString("betonquest-editor"));
			stage.getIcons().add(new Image(getClass().getResourceAsStream("resource/icon.png")));
			stage.setMinHeight(600);
			stage.setMinWidth(800);
			stage.setMaximized(true);
			stage.show();
			// load package for debugging
			if (autoLoadPackage != null) try {
				QuestPackage pack = QuestPackage.loadFromZip(new ZipFile(autoLoadPackage));
				loadedPackages.put(pack.getName().get(), pack);
				display(pack);
			} catch (Exception e) {
				BetonQuestEditor.showStackTrace(e);
			}
		} catch (Exception e) {
			showStackTrace(e);
		}
	}
	
	/**
	 * @return the instance of the editor
	 */
	public static BetonQuestEditor getInstance() {
		return instance;
	}
	
	/**
	 * @return the primary stage of this application
	 */
	public Stage getPrimaryStage() {
		return stage;
	}
	
	/**
	 * @return ResourceBundle containing language strings
	 */
	public ResourceBundle getLanguage() {
		return language;
	}
	
	/**
	 * @return the list of loaded packages
	 */
	public HashMap<String, QuestPackage> getPackages() {
		return loadedPackages;
	}

	/**
	 * @return the package currently displayed in the view
	 */
	public QuestPackage getDisplayedPackage() {
		return currentPackage;
	}
	
	/**
	 * Displays a package in the view.
	 */
	public void display(QuestPackage pack) {
		 currentPackage = pack;
		 pack.sort();
		 MainController.setNpcBindings(pack.getNpcBindings());
		 MainController.setGlobVariables(pack.getVariables());
		 MainController.setStaticEvents(pack.getStaticEvents());
		 MainController.setGlobalLocations(pack.getLocations());
		 MainController.setQuestCancelers(pack.getCancelers());
		 MainController.setMainPageLines(pack.getMainPage());
		 ConversationController.setConversations(pack.getConversations());
		 EcoController.setConditions(pack.getConditions());
		 EcoController.setEvents(pack.getEvents());
		 EcoController.setObjectives(pack.getObjectives());
		 OtherController.setItems(pack.getItems());
		 OtherController.setJournal(pack.getJournal());
	}
	
	/**
	 * Refreshes the currently displayed package
	 */
	public static void refresh() {
		instance.display(instance.currentPackage);
	}

	/**
	 * Parses the arguments and starts the application.
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			autoLoadPackage = new File(args[0]);
			if (!autoLoadPackage.exists() || !autoLoadPackage.getName().endsWith(".zip")) {
				autoLoadPackage = null;
			}
			autoSavePackage = new File(args[1]);
			if (!autoSavePackage.getName().endsWith(".zip")) {
				autoSavePackage = null;
			}
		}
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

	@Override
	public void stop() throws Exception {
		if (autoSavePackage != null) try {
			autoSavePackage.createNewFile();
			currentPackage.saveToZip(autoSavePackage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.stop();
	}
}
