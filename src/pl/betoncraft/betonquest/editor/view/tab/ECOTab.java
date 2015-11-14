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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import pl.betoncraft.betonquest.editor.model.Condition;
import pl.betoncraft.betonquest.editor.model.Event;
import pl.betoncraft.betonquest.editor.model.Objective;
import pl.betoncraft.betonquest.editor.view.Tabs;

/**
 * The "Events, Conditions, Objectives" tab. 
 *
 * @author Jakub Sapalski
 */
public class ECOTab extends Tab {
	
	public final ListView<Event> eventList;
	public final Button addEvent;
	public final Button editEvent;
	public final Button delEvent;

	public final ListView<Condition> conditionList;
	public final Button addCondition;
	public final Button editCondition;
	public final Button delCondition;

	public final ListView<Objective> objectiveList;
	public final Button addObjective;
	public final Button editObjective;
	public final Button delObjective;

	/**
	 * Creates a tab with events, conditions and objectives.
	 * 
	 * @param tabs
	 *            instance of Tabs pane
	 */
	public ECOTab(Tabs tabs) {
		setText("Events, Conditions, Objectives");
		
		HBox main = new HBox();
		main.setSpacing(15);
		main.setPadding(new Insets(15));
		
		// events
		VBox eventBox = new VBox();
		eventBox.setSpacing(10);
		Label eventLabel = new Label("Events");
		eventList = new ListView<>();
		ButtonBar eventButtonBar = new ButtonBar();
		addEvent = new Button("Add");
		editEvent = new Button("Edit");
		delEvent = new Button("Delete");
		eventButtonBar.getButtons().addAll(addEvent, editEvent, delEvent);
		eventBox.getChildren().addAll(eventLabel, eventList, eventButtonBar);
		
		// conditions
		VBox conditionBox = new VBox();
		conditionBox.setSpacing(10);
		Label conditionLabel = new Label("Conditions");
		conditionList = new ListView<>();
		ButtonBar conditionButtonBar = new ButtonBar();
		addCondition = new Button("Add");
		editCondition = new Button("Edit");
		delCondition = new Button("Delete");
		conditionButtonBar.getButtons().addAll(addCondition, editCondition, delCondition);
		conditionBox.getChildren().addAll(conditionLabel, conditionList, conditionButtonBar);
		
		// objectives
		VBox objectiveBox = new VBox();
		objectiveBox.setSpacing(10);
		Label objectiveLabel = new Label("Objectives");
		objectiveList = new ListView<>();
		ButtonBar objectiveButtonBar = new ButtonBar();
		addObjective = new Button("Add");
		editObjective = new Button("Edit");
		delObjective = new Button("Delete");
		objectiveButtonBar.getButtons().addAll(addObjective, editObjective, delObjective);
		objectiveBox.getChildren().addAll(objectiveLabel, objectiveList, objectiveButtonBar);
		
		// add to main
		main.getChildren().addAll(eventBox, conditionBox, objectiveBox);
		for (Node box : main.getChildren()) {
			HBox.setHgrow(box, Priority.ALWAYS);
		}
		for (ListView<?> list : new ListView[]{eventList, conditionList, objectiveList}) {
			VBox.setVgrow(list, Priority.ALWAYS);
		}
		for (Label label : new Label[]{eventLabel, conditionLabel, objectiveLabel}) {
			label.setId("option-label");
		}
		setContent(main);
	}

}
