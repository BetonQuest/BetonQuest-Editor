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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;

/**
 * Manages all TranslatableTexts in the package.
 * 
 * @author Jakub Sapalski
 */
public class TranslationManager {
	
	private QuestPackage pack;
	private String defLang;
	private final HashMap<String, Integer> languages = new HashMap<>();
	private final ObservableList<TranslatableText> translations = FXCollections.observableArrayList();
	
	/**
	 * Creates new empty manager.
	 * 
	 * @param pack
	 *            the package owning the manager
	 */
	public TranslationManager(QuestPackage pack) {
		this.pack = pack;
	}
	
	/**
	 * @return the package owning the manager
	 */
	public QuestPackage getPack() {
		return pack;
	}
	
	/**
	 * @return default language
	 */
	public String getDefault() {
		return defLang;
	}
	
	/**
	 * @param lang new defalt language
	 */
	public void setDefault(String lang) {
		defLang = lang;
	}
	
	/**
	 * @return a list of all translatable texts created in this package
	 */
	public ObservableList<TranslatableText> getTranslations() {
		return translations;
	}
	
	/**
	 * @return a list of all languages used in this package
	 */
	public HashMap<String, Integer> getLanguages() {
		countLanguages();
		return languages;
	}
	
	/**
	 * @return true if there are texts in "default" language in the package
	 */
	public boolean hasDefaultStrings() {
		for (TranslatableText text : translations) {
			if (!text.hasMultipleLanguages()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Converts all texts in "default" language to specified one.
	 * 
	 * @param lang language to translate to
	 */
	public void convert(String lang) {
		if (!hasDefaultStrings()) {
			return;
		}
		for (TranslatableText text : translations) {
			if (!text.hasMultipleLanguages()) {
				text.setLang(lang, text.getDef().get());
			}
		}
		setDefault(lang);
		countLanguages();
		BetonQuestEditor.getInstance().refresh();
	}

	/**
	 * Creates new language and sets all texts in this language to "".
	 * 
	 * @param lang
	 */
	public void createLanguage(String lang) {
		if (languages.containsKey(lang)) {
			return;
		}
		for (TranslatableText text : translations) {
			text.setLang(lang, "");
		}
		countLanguages();
		BetonQuestEditor.getInstance().refresh();
	}

	/**
	 * Changes the name of the language to a different one.
	 * 
	 * @param current language whose name needs to be changed
	 * @param lang new name for the language
	 */
	public void renameLanguage(String current, String lang) {
		for (TranslatableText text : translations) {
			StringProperty translation = text.getLang(current);
			text.setLang(current, null);
			text.setLang(lang, translation.get());
		}
		defLang = lang;
		countLanguages();
		BetonQuestEditor.getInstance().refresh();
	}
	
	/**
	 * @param lang name of the language to delete
	 */
	public void deleteLanguage(String lang) {
		countLanguages();
		for (TranslatableText text : translations) {
			if (languages.size() <= 1) {
				text.setDef(text.getLang(lang).get());
			}
			text.setLang(lang, null);
		}
		defLang = null;
		inferDefaultLanguage();
		BetonQuestEditor.getInstance().refresh();
	}
	
	/**
	 * If there is no default language, it will try to choose the one matching
	 * system language. If there is no such language, it will choose the one
	 * with most messages. If there are languages with equal amount of messages,
	 * it will choose the first one.
	 */
	public void inferDefaultLanguage() {
		// check which language is used most widely and set it as default
		if (defLang == null) {
			countLanguages();
			String mostPopularLang = null;
			if (!languages.isEmpty()) {
				Integer max = 0;
				for (Entry<String, Integer> entry : languages.entrySet()) {
					if (entry.getValue() > max) {
						max = entry.getValue();
						mostPopularLang = entry.getKey();
					}
				}
			}
			for (String lang : languages.keySet()) {
				if (Locale.getDefault().getLanguage().contains(lang)) {
					defLang = lang;
					break;
				}
			}
			if (defLang == null) {
				defLang = mostPopularLang;
			}
		}
	}

	/**
	 * Counts languages used in the package.
	 * 
	 * @return most popular language in this package
	 */
	private void countLanguages() {
		languages.clear();
		for (TranslatableText text : translations) {
			if (!text.hasMultipleLanguages()) {
				continue;
			}
			for (String lang : text.getLanguages()) {
				Integer count = languages.get(lang);
				if (count == null) {
					count = new Integer(0);
				}
				count++;
				languages.put(lang, count);
			}
		}
	}

}
