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
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.PackageSet;
import pl.betoncraft.betonquest.editor.model.PackageSet.SaveType;
import pl.betoncraft.betonquest.editor.model.QuestPackage;

/**
 * Controls main menu.
 *
 * @author Jakub Sapalski
 */
public class MainMenuController {
	
	private static MainMenuController instance;
	
	@FXML private MenuItem save;
	@FXML private Menu saveAs;
	@FXML private MenuItem export;
	@FXML private MenuItem pack;
	
	public MainMenuController() {
		instance = this;
	}
	
	public static void setSaveEnabled(boolean enabled) {
		instance.save.setDisable(!enabled);
	}
	
	public static void setSaveAsEnabled(boolean enabled) {
		instance.saveAs.setDisable(!enabled);
	}
	
	public static void setExportEnabled(boolean enabled) {
		instance.export.setDisable(!enabled);
	}
	
	public static void setPackEnabled(boolean enabled) {
		instance.pack.setDisable(!enabled);
	}
	
	public static MainMenuController getInstance() {
		return instance;
	}

	@FXML private void newSet() {
		try {
			StringProperty name = new SimpleStringProperty();
			if (NameEditController.display(name, true)) {
				PackageSet set = new PackageSet(null, SaveType.NONE, name.get());
				QuestPackage pack = new QuestPackage(set, name.get());
				set.getPackages().add(pack);
				BetonQuestEditor.getInstance().getSets().add(set);
				RootController.setPackageSets(BetonQuestEditor.getInstance().getSets());
				BetonQuestEditor.getInstance().display(set);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void newPackage() {
		try {
			StringProperty name = new SimpleStringProperty();
			if (NameEditController.display(name, true)) {
				PackageSet set = BetonQuestEditor.getInstance().getDisplayedPackage().getSet();
				QuestPackage pack = new QuestPackage(set, name.get());
				set.getPackages().add(pack);
				RootController.setPackageSets(BetonQuestEditor.getInstance().getSets());
				BetonQuestEditor.getInstance().display(pack);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void loadZip() {
		try {
			BetonQuestEditor instance = BetonQuestEditor.getInstance();
			FileChooser fc = new FileChooser();
			fc.setTitle(instance.getLanguage().getString("select-file"));
			ExtensionFilter filter = new ExtensionFilter("ZIP Files", "*.zip");
			fc.getExtensionFilters().add(filter);
			fc.setSelectedExtensionFilter(filter);
			File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
			if (desktop != null) fc.setInitialDirectory(desktop);
			File selectedFile = fc.showOpenDialog(instance.getPrimaryStage());
			if (selectedFile != null) {
				try {
					PackageSet set = PackageSet.loadFromZip(selectedFile);
					instance.display(set);
				} catch (Exception e) {
					ExceptionController.display(e);
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void loadDirectory() {
		try {
			BetonQuestEditor instance = BetonQuestEditor.getInstance();
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle(instance.getLanguage().getString("select-folder"));
			File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
			if (desktop != null) dc.setInitialDirectory(desktop);
			File selectedFile = dc.showDialog(instance.getPrimaryStage());
			if (selectedFile != null) {
				try {
					PackageSet set = PackageSet.loadFromDirectory(selectedFile);
					if (set != null) {
						instance.display(set);
					}
				} catch (Exception e) {
					ExceptionController.display(e);
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML public void save() {
		try {
			PackageSet set = BetonQuestEditor.getInstance().getDisplayedPackage().getSet();
			if (set.getFile() != null) {
				switch (set.getSaveType()) {
				case ZIP:
					set.saveToZip(set.getFile());
					break;
				case DIR:
					set.saveToDirectory(set.getFile());
					break;
				default:
					// do nothing
				}
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void saveZip() {
		try {
			BetonQuestEditor instance = BetonQuestEditor.getInstance();
			FileChooser fc = new FileChooser();
			fc.setTitle(instance.getLanguage().getString("select-file"));
			ExtensionFilter filter = new ExtensionFilter("ZIP Files", "*.zip");
			fc.getExtensionFilters().add(filter);
			fc.setSelectedExtensionFilter(filter);
			File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
			if (desktop != null) fc.setInitialDirectory(desktop);
			File selectedFile = fc.showSaveDialog(instance.getPrimaryStage());
			if (selectedFile != null) {
				PackageSet set = BetonQuestEditor.getInstance().getDisplayedPackage().getSet();
				set.saveToZip(selectedFile);
				set.setSaveType(SaveType.ZIP);
				set.setFile(selectedFile);
				MainMenuController.setSaveEnabled(true);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}

	@FXML private void saveDirectory() {
		try {
			BetonQuestEditor instance = BetonQuestEditor.getInstance();
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle(instance.getLanguage().getString("select-directory"));
			File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
			if (desktop != null) dc.setInitialDirectory(desktop);
			File selectedFile = dc.showDialog(instance.getPrimaryStage());
			if (selectedFile != null) {
				PackageSet set = BetonQuestEditor.getInstance().getDisplayedPackage().getSet();
				set.saveToDirectory(selectedFile);
				set.setSaveType(SaveType.DIR);
				set.setFile(selectedFile);
				MainMenuController.setSaveEnabled(true);
			}
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML public void export() {
		try {
			File script = new File("export.sh");
			if (!script.exists()) {
				script = new File("export.bat");
				if (!script.exists()) {
					BetonQuestEditor.showError("no-export-script");
					return;
				}
			}
			PackageSet set = BetonQuestEditor.getInstance().getDisplayedPackage().getSet();
			File file = new File(script.getParentFile(), "export_" + set.getName().get() + ".zip");
			set.saveToZip(file);
			ProcessBuilder pb = new ProcessBuilder(script.getName(), file.getAbsolutePath());
			pb.directory(script.getParentFile());
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML private void quit() {
		try {
			System.exit(0);
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void settings() {
		try {
			SettingsController.display();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void reload() {
		try {
			BetonQuestEditor.reload();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void about() {
		try {
			AboutController.display();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void changelog() {
		try {
			ChangelogController.display();
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
	
	@FXML private void docs() {
		try {
			Desktop.getDesktop().browse(new URI("http://betonquest.betoncraft.pl/BetonQuestDocumentation.pdf"));
		} catch (Exception e) {
			ExceptionController.display(e);
		}
	}
}
