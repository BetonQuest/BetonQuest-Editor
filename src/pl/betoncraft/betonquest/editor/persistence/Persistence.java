/**
 * BetonQuest Editor - advanced quest creating tool for BetonQuest
 * Copyright (C) 2017  Jakub "Co0sh" Sapalski
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

package pl.betoncraft.betonquest.editor.persistence;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for saving the editor state, settings etc. between
 * sessions.
 *
 * @author Jakub Sapalski
 */
public class Persistence {

	private static final String BETONQUEST_DIR_NAME = ".betonquest-editor";
	private static final String CONFIG_NAME = "config.properties";
	private static final String PREVIOUS_STATE_NAME = "previous-state.properties";

	private static Persistence instance;
	private boolean enabled = true;
	private File betonQuestDir;

	private Settings settings;
	private PreviousState prevState;

	public Persistence() throws IOException {
		instance = this;

		// prepare the directory
		loadBetonQuestDir();
		
		// deploy or load the settings
		settings = new Settings(new File(betonQuestDir, CONFIG_NAME));
		
		// load previous state
		prevState = new PreviousState(new File(betonQuestDir, PREVIOUS_STATE_NAME));

	}
	
	/**
	 * @return whenever saving persistent data is enabled
	 */
	public static boolean isEnabled() {
		return instance.enabled;
	}
	
	/**
	 * @return the instance of Settings object
	 */
	public static Settings getSettings() {
		return instance.settings;
	}
	
	/**
	 * @return the instance of PreviousState object
	 */
	public static PreviousState getPreviousState() {
		return instance.prevState;
	}

	private void loadBetonQuestDir() {

		// local directory allows for portability
		File local = new File(BETONQUEST_DIR_NAME);
		if (local.exists() && local.isDirectory()) {

			// local directory found
			betonQuestDir = local;

		} else {

			// get the global directory located at user's home
			String homePath = System.getProperty("user.home");
			if (homePath != null) {

				File home = new File(homePath);
				if (home.exists() && home.isDirectory()) {

					// found the path to the home directory
					betonQuestDir = new File(home, BETONQUEST_DIR_NAME);

				}
			} 

			// if can't get home directory, use the local one
			if (betonQuestDir == null) {
				betonQuestDir = local;
			}
			
			// create directory if it doesn't exist, fail if can't create
			if (!betonQuestDir.exists() && !betonQuestDir.mkdir()) {
				enabled = false;
			}
			
			// check if it's a directory
			if (!betonQuestDir.isDirectory()) {
				enabled = false;
			}
		}
	}

}
