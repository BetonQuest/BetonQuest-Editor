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

package pl.betoncraft.betonquest.editor.data;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pl.betoncraft.betonquest.editor.BetonQuestEditor;
import pl.betoncraft.betonquest.editor.model.QuestPackage;

/**
 * Basic implementation of ID.
 *
 * @author Jakub Sapalski
 */
public abstract class SimpleID implements ID {

	protected StringProperty id;
	protected QuestPackage pack;
	protected int index = -1;

	private ChangeListener<String> changeListener;
	
	protected SimpleID() {
		changeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.contains(" ")) {
					id.set(newValue.replace(" ", "_"));
				}
			}
		};
	}

	@Override
	public StringProperty getId() {
		id.removeListener(changeListener);
		id.addListener(changeListener);
		return id;
	}
	
	@Override
	public String getRelativeName(QuestPackage pack) {
		return (pack.equals(this.pack)) ? id.get() : getAbsoluteName();
	}
	
	@Override
	public String getAbsoluteName() {
		return pack.getName().get() + "." + id.get();
	}

	@Override
	public QuestPackage getPack() {
		return pack;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean needsEditing() {
		return id == null || id.get() == null || id.get().isEmpty();
	}
	
	@Override
	public String toString() {
		return getRelativeName(BetonQuestEditor.getInstance().getDisplayedPackage());
	}

}
