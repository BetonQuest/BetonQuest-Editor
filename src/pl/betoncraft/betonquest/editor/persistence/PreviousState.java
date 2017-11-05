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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Keeps track of previous session state.
 *
 * @author Jakub Sapalski
 */
public class PreviousState {

	private File file;
	private Properties props = new Properties();

	private List<String> openPackageSets = new ArrayList<>();
	private String selectedPackageSet;
	private String selectedPackage;
	private int selectedTab;

	public PreviousState(File file) throws IOException {
		this.file = file;

		// nothing to load if there's no file
		if (!file.exists()) {
			return;
		}

		// load the properties
		Reader reader = new FileReader(file);
		props.load(reader);
		reader.close();

		// get properties
		openPackageSets = Arrays.asList(props.getProperty("list").split(","));
		selectedPackageSet = props.getProperty("set");
		selectedPackage = props.getProperty("pack");
		selectedTab = Integer.valueOf(props.getProperty("tab"));

	}

	/**
	 * Saves the state to a file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {

		// remove the file if there are no open packages
		if (openPackageSets.isEmpty()) {
			file.delete();
			return;
		}

		// set properties
		props.setProperty("list", String.join(",", openPackageSets));
		props.setProperty("set", String.valueOf(selectedPackageSet));
		props.setProperty("pack", selectedPackage);
		props.setProperty("tab", String.valueOf(selectedTab));

		// save properties to file
		Writer writer = new FileWriter(file);
		props.store(writer, null);
		writer.close();

	}

	/**
	 * @return the list of previously open package sets
	 */
	public List<String> getOpenPackageSets() {
		return openPackageSets;
	}

	/**
	 * @param openPackageSets
	 *            the list of package sets to open next time
	 */
	public void setOpenPackageSets(List<String> openPackageSets) {
		this.openPackageSets = openPackageSets;
	}

	/**
	 * @return the previously selected set
	 */
	public String getSelectedPackageSet() {
		return selectedPackageSet;
	}

	/**
	 * @param selectedPackageSet
	 *            the index of package set to select next time
	 */
	public void setSelectedPackageSet(String selectedPackageSet) {
		this.selectedPackageSet = selectedPackageSet;
	}

	/**
	 * @return the name of a selected package
	 */
	public String getSelectedPackage() {
		return selectedPackage;
	}

	/**
	 * @param selectedPackage the package to select next time
	 */
	public void setSelectedPackage(String selectedPackage) {
		this.selectedPackage = selectedPackage;
	}

	/**
	 * @return the previously selected tab
	 */
	public int getSelectedTab() {
		return selectedTab;
	}

	/**
	 * @param selectedTab
	 *            tab to select next time
	 */
	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

}
