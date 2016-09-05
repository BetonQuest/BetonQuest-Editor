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
package pl.betoncraft.betonquest.editor.model;

import java.util.HashMap;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.betoncraft.betonquest.editor.data.Translatable;

/**
 * Stores Strings in different languages.
 *
 * @author Jakub Sapalski
 */
public class TranslatableText {

	private Translatable owner;
	private TranslationManager manager;
	private boolean multiLang;
	private HashMap<String, StringProperty> text = new HashMap<>();;
	private StringProperty def = new SimpleStringProperty();

	/**
	 * Creates new empty translatable text.
	 * 
	 * @param owner
	 *            object which contains the translated text
	 */
	public TranslatableText(Translatable owner) {
		this.owner = owner;
		manager = owner.getPack().getTranslationManager();
		multiLang = manager.getDefault() != null;
		manager.getTranslations().add(this);
	}

	/**
	 * Automatically sets the language based on owner's package's default
	 * language. If it's null, it will set default language.
	 * 
	 * @param text
	 *            string to set
	 * @return StringProperty containing the text
	 */
	public StringProperty set(String text) {
		String lang = manager.getDefault();
		if (lang == null) {
			return setDef(text);
		} else {
			return setLang(lang, text);
		}
	}

	/**
	 * Sets text in default language.
	 * 
	 * @param text
	 *            string to set
	 * @return StringProperty containing the text
	 */
	public StringProperty setDef(String text) {
		this.def.set(text);
		multiLang = false;
		return this.def;
	}

	/**
	 * Sets text in specified language.
	 * 
	 * @param lang
	 *            language to use
	 * @param text
	 *            string to set
	 * @return StringProperty containing the text
	 */
	public StringProperty setLang(String lang, String text) {
		if (text == null) {
			this.text.remove(lang);
			return null;
		}
		StringProperty value = new SimpleStringProperty(text);
		this.text.put(lang, value);
		multiLang = true;
		return value;
	}

	/**
	 * Automatically gets the text in owner's package's default language.
	 * 
	 * @return StringProperty containing the text
	 */
	public StringProperty get() {
		String lang = manager.getDefault();
		if (lang == null || !multiLang) {
			return getDef();
		} else {
			StringProperty value = getLang(lang);
			if (value == null) {
				value = setLang(lang, "");
			}
			return value;
		}
	}

	/**
	 * Returns text in default language.
	 * 
	 * @return text in default language
	 */
	public StringProperty getDef() {
		return def;
	}

	/**
	 * Returns text in specified language.
	 * 
	 * @param lang
	 *            language in which the text will be returned
	 * @return text in specified language
	 */
	public StringProperty getLang(String lang) {
		return text.get(lang);
	}

	/**
	 * Checks if the text is translated to multiple languages.
	 * 
	 * @return true if the TranslatableText uses secific languages, false if it
	 *         uses default language
	 */
	public boolean hasMultipleLanguages() {
		return multiLang;
	}

	/**
	 * Returns the set of languages in which this text is translated. If it uses
	 * default language, the set will be empty.
	 * 
	 * @return the set of languages
	 */
	public Set<String> getLanguages() {
		return text.keySet();
	}

	/**
	 * Returns the object containing this TranslatableText.
	 * 
	 * @return owner of this object
	 */
	public Translatable getOwner() {
		return owner;
	}

}
