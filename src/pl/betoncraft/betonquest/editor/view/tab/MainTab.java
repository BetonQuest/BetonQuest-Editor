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

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pl.betoncraft.betonquest.editor.model.GlobalLocation;
import pl.betoncraft.betonquest.editor.model.NpcBinding;
import pl.betoncraft.betonquest.editor.model.StaticEvent;
import pl.betoncraft.betonquest.editor.model.Variable;
import pl.betoncraft.betonquest.editor.view.Tabs;

/**
 * The "main" tab of the package.
 *
 * @author Jakub Sapalski
 */
public class MainTab extends Tab {
	
	public final TableView<NpcBinding> npcTable;
	public final Button npcAdd;
	public final Button npcEdit;
	public final Button npcRemove;
	
	public final ListView<GlobalLocation> globLocList;
	public final Button globLocAdd;
	public final Button globLocEdit;
	public final Button globLocRemove;
	
	public final TableView<StaticEvent> staticTable;
	public final Button staticAdd;
	public final Button staticEdit;
	public final Button staticRemove;
	
	public final TableView<Variable> variableTable;
	public final Button variableAdd;
	public final Button variableEdit;
	public final Button variableRemove;

	/**
	 * Creates main tab.
	 * 
	 * @param tabs
	 *            instance of Tabs pane
	 */
	public MainTab(Tabs tabs) {
		setText("Main");
		
		// create grid
		GridPane grid = new GridPane();
		StackPane npcs = new StackPane();
		StackPane globLocs = new StackPane();
		StackPane statics = new StackPane();
		StackPane variables = new StackPane();
		grid.add(npcs, 0, 0);
		grid.add(globLocs, 0, 1);
		grid.add(statics, 1, 0);
		grid.add(variables, 1, 1);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		grid.getColumnConstraints().addAll(col1, col2);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(50);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(50);
		grid.getRowConstraints().addAll(row1, row2);
		grid.setHgap(5);
		GridPane.setMargin(npcs, new Insets(5));
		GridPane.setMargin(globLocs, new Insets(5));
		GridPane.setMargin(statics, new Insets(5));
		GridPane.setMargin(variables, new Insets(5));
		
		// npcs list
		VBox npcBox = new VBox();
		Label npcText = new Label("NPC Bindings");
		npcText.setId("option-label");
		npcTable = new TableView<>();
		ButtonBar npcButtons = new ButtonBar();
		npcButtons.setPadding(new Insets(5, 0, 0, 0));
		npcAdd = new Button();
		npcAdd.setText("Add");
		npcEdit = new Button();
		npcEdit.setText("Edit");
		npcRemove = new Button();
		npcRemove.setText("Delete");
		npcButtons.getButtons().addAll(npcAdd, npcEdit, npcRemove);
		npcBox.getChildren().addAll(npcText, npcTable, npcButtons);
		npcs.getChildren().add(npcBox);
		
		// global locations
		VBox globLocBox = new VBox();
		Label globLocText = new Label("Global Locations");
		globLocText.setId("option-label");
		globLocList = new ListView<>();
		ButtonBar globLocButtons = new ButtonBar();
		globLocButtons.setPadding(new Insets(5, 0, 0, 0));
		globLocAdd = new Button();
		globLocAdd.setText("Add");
		globLocEdit = new Button();
		globLocEdit.setText("Edit");
		globLocRemove = new Button();
		globLocRemove.setText("Delete");
		globLocButtons.getButtons().addAll(globLocAdd, globLocEdit, globLocRemove);
		globLocBox.getChildren().addAll(globLocText, globLocList, globLocButtons);
		globLocs.getChildren().add(globLocBox);
		
		// static events
		VBox staticBox = new VBox();
		Label staticText = new Label("Static Events");
		staticText.setId("option-label");
		staticTable = new TableView<>();
		ButtonBar staticButtons = new ButtonBar();
		staticButtons.setPadding(new Insets(5, 0, 0, 0));
		staticAdd = new Button();
		staticAdd.setText("Add");
		staticEdit = new Button();
		staticEdit.setText("Edit");
		staticRemove = new Button();
		staticRemove.setText("Delete");
		staticButtons.getButtons().addAll(staticAdd, staticEdit, staticRemove);
		staticBox.getChildren().addAll(staticText, staticTable, staticButtons);
		statics.getChildren().add(staticBox);
		
		// variables
		VBox variableBox = new VBox();
		Label variableText = new Label("Variables");
		variableText.setId("option-label");
		variableTable = new TableView<>();
		ButtonBar variableButtons = new ButtonBar();
		variableButtons.setPadding(new Insets(5, 0, 0, 0));
		variableAdd = new Button();
		variableAdd.setText("Add");
		variableEdit = new Button();
		variableEdit.setText("Edit");
		variableRemove = new Button();
		variableRemove.setText("Delete");
		variableButtons.getButtons().addAll(variableAdd, variableEdit, variableRemove);
		variableBox.getChildren().addAll(variableText, variableTable, variableButtons);
		variables.getChildren().add(variableBox);
		
		setContent(grid);
	}
}
