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
package pl.betoncraft.betonquest.editor.custom;

import javafx.scene.control.TreeItem;

/**
 * A custom TreeItem representing tree root, PackageSet or QuestPackage.
 * 
 * @author Jakub Sapalski
 */
public class PackageTreeItem extends TreeItem<String> {
	
	public enum Type {
		ROOT, TYPE, SET, PACKAGE
	}
	
	private Type type;
	
	public PackageTreeItem(Type type) {
		super();
		this.type = type;
	}

	public PackageTreeItem(Type type, String string) {
		super(string);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

}
