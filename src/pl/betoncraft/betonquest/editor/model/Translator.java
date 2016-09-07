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
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.data.Translatable;

/**
 * Manages the current translating progress.
 * 
 * @author Jakub Sapalski
 */
public class Translator {
	
	private String from;
	private String to;
	
	private TranslationManager manager;
	
	private int current = -1;
	
	/**
	 * Creates new translation process, from "from" language to "to".
	 * 
	 * @param manager
	 *            TranslationManager of the package
	 * @param from
	 *            original language
	 * @param to
	 *            new language
	 */
	public Translator(TranslationManager manager, String from, String to) {
		this.manager = manager;
		this.from = from;
		this.to = to;
	}
	
	/**
	 * @return language from which the translator translates
	 */
	public String getFromLanguage() {
		return from;
	}
	
	/**
	 * @return language into which the translator translates
	 */
	public String getToLanguage() {
		return to;
	}
	
	/**
	 * Moves the translator to the next string.
	 */
	public void next() {
		if (hasNext()) {
			current++;
		}
	}
	
	/**
	 * @return true if there is next string, false if it's the end
	 */
	public boolean hasNext() {
		return current < manager.getTranslations().size() - 1;
	}
	
	/**
	 * Moves the translator to the prevous string.
	 */
	public void previous() {
		if (hasPrevious()) {
			current--;
		}
	}
	
	/**
	 * @return true if there is previous string, false if it's the beginning
	 */
	public boolean hasPrevious() {
		return current > 0;
	}
	
	/**
	 * Translates the current string.
	 * 
	 * @param translation
	 *            string to use as translation
	 */
	public void translate(String translation) {
		if (current >= 0 && current < manager.getTranslations().size()) {
			manager.getTranslations().get(current).setLang(to, translation);
		}
	}
	
	/**
	 * @return the original text in "from" language
	 */
	public String getText() {
		return manager.getTranslations().get(current).getLang(from).get();
	}
	
	/**
	 * @return the new text in "to" language
	 */
	public String getTranslation() {
		StringProperty sp = manager.getTranslations().get(current).getLang(to);
		if (sp == null) {
			return "";
		} else {
			return sp.get();
		}
	}
	
	/**
	 * @return the owner of the translated text
	 */
	public Translatable getObject() {
		return manager.getTranslations().get(current).getOwner();
	}

}
