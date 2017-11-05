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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * This class controls all settings for the editor.
 *
 * @author Jakub Sapalski
 */
public class Settings {

	private File file;
	private Properties properties;

	private Locale locale;
	private Set<Locale> availableLocales;

	/**
	 * Creates new Settings instance by loading properties from the specified file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public Settings(File file) throws IOException {
		this.file = file;

		// load default properties
		Properties defProps = new Properties();
		Reader defReader = new BufferedReader(
				new InputStreamReader(BetonQuestEditor.class.getResourceAsStream("resource/config.properties")));
		defProps.load(defReader);
		defReader.close();

		// deploy them as general properties
		properties = new Properties(defProps);

		// only use the file if persistence is enabled
		if (Persistence.isEnabled()) {

			// deploy properties file if it doesn't exist
			if (!file.exists()) {
				file.createNewFile();
			}

			// load user properties
			Reader reader = new FileReader(file);
			properties.load(reader);
			reader.close();

		}

		// load Settings
		load();

	}

	/**
	 * Loads the properties into this Settings instance.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		loadAvailableLanguages();
		locale = Locale.forLanguageTag(properties.getProperty("language", "en"));
	}

	private void loadAvailableLanguages() throws IOException {
		availableLocales = new HashSet<>();
		availableLocales.addAll(Arrays.asList(
				new Locale("en"),
				new Locale("pl"),
				new Locale("fr"),
				new Locale("de")
		));
	}

	/**
	 * Applies all edits to the Settings object, moving them to the properties.
	 * 
	 * @throws IOException
	 */
	public void apply() throws IOException {
		properties.setProperty("language", locale.toString());
		save();
	}

	/**
	 * Restores defaults for the Settings object.
	 * 
	 * @throws IOException
	 */
	public void restoreDefaults() throws IOException {
		properties.clear();
		save();
		load();
	}

	/**
	 * Saves user's settings to the file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {

		// saving is only possible if the config file exists
		if (file.exists()) {
			Writer writer = new FileWriter(file);
			properties.store(writer, null);
			writer.close();
		}

	}

	public Locale getLanguage() {
		return locale;
	}

	public void setLanguage(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the set of available languages
	 */
	public Set<Locale> getAvailableLocales() {
		return availableLocales;
	}

}
