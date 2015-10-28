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
package pl.betoncraft.betonquest.editor.view;

import pl.betoncraft.betonquest.editor.controller.MainMenuController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Main menu bar on top of the application.
 *
 * @author Jakub Sapalski
 */
public class MainMenu extends MenuBar {
	
	public final MenuItem newPackage;
	public final MenuItem load;
	public final MenuItem save;
	public final MenuItem export;
	public final MenuItem quit;
	public final MenuItem about;
	public final MenuItem docs;
	
	public MainMenu(Root root) {
		// create menus
		Menu file = new Menu("File");
		Menu edit = new Menu("Edit");
		Menu help = new Menu("Help");
		getMenus().addAll(file, edit, help);
		
		// file menu
		newPackage = new MenuItem("New");
		load = new MenuItem("Load");
		save = new MenuItem("Save");
		export = new MenuItem("Export");
		quit = new MenuItem("Quit");
		file.getItems().addAll(newPackage, load, new SeparatorMenuItem(), save, export, new SeparatorMenuItem(), quit);

		// about menu
		about = new MenuItem("About");
		docs = new MenuItem("Docs");
		help.getItems().addAll(about, docs);
		
		// assign the controller
		new MainMenuController(this);
	}
}
