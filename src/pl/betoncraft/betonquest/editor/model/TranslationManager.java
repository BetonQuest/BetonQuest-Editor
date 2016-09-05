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

public class TranslationManager {
	
	private QuestPackage pack;
	private String defLang;
	private final HashMap<String, Integer> languages = new HashMap<>();
	private final ObservableList<TranslatableText> translations = FXCollections.observableArrayList();
	
	public TranslationManager(QuestPackage pack) {
		this.pack = pack;
	}
	
	public QuestPackage getPack() {
		return pack;
	}
	
	public String getDefault() {
		return defLang;
	}
	
	public void setDefault(String lang) {
		defLang = lang;
	}
	
	public ObservableList<TranslatableText> getTranslations() {
		return translations;
	}
	
	public HashMap<String, Integer> getLanguages() {
		countLanguages();
		return languages;
	}
	
	public boolean hasDefaultStrings() {
		for (TranslatableText text : translations) {
			if (!text.hasMultipleLanguages()) {
				return true;
			}
		}
		return false;
	}
	
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
	 * Counts languages used in the package and returns the one which is most widely used.
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
