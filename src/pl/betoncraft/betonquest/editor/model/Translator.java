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

public class Translator {
	
	private String from;
	private String to;
	
	private TranslationManager manager;
	
	private int current = -1;
	
	public Translator(TranslationManager manager, String from, String to) {
		this.manager = manager;
		this.from = from;
		this.to = to;
	}
	
	public String getFromLanguage() {
		return from;
	}
	
	public String getToLanguage() {
		return to;
	}
	
	public void next() {
		if (hasNext()) {
			current++;
		}
	}
	
	public boolean hasNext() {
		return current < manager.getTranslations().size() - 1;
	}
	
	public void previous() {
		if (hasPrevious()) {
			current--;
		}
	}
	
	public boolean hasPrevious() {
		return current > 0;
	}
	
	public void translate(String translation) {
		if (current >= 0 && current < manager.getTranslations().size()) {
			manager.getTranslations().get(current).setLang(to, translation);
		}
	}
	
	public String getText() {
		return manager.getTranslations().get(current).getLang(from).get();
	}
	
	public String getTranslation() {
		StringProperty sp = manager.getTranslations().get(current).getLang(to);
		if (sp == null) {
			return "";
		} else {
			return sp.get();
		}
	}
	
	public Translatable getObject() {
		return manager.getTranslations().get(current).getOwner();
	}

}
