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
package pl.betoncraft.betonquest.editor.view.tab;

import javafx.scene.control.Tab;
import pl.betoncraft.betonquest.editor.view.Tabs;

/**
 * The "translations" tab of the package.
 *
 * @author Jakub Sapalski
 */
public class TranslationTab extends Tab {

	/**
	 * Creates translations tab.
	 * 
	 * @param tabs
	 *            instance of Tabs pane
	 */
	public TranslationTab(Tabs tabs) {
		setText("Translations");
	}

}
