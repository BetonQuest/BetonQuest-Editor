/**
 * 
 */
package pl.betoncraft.betonquest.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import pl.betoncraft.betonquest.editor.data.ID;
import pl.betoncraft.betonquest.editor.data.SimpleInstruction;
import pl.betoncraft.betonquest.editor.model.exception.PackageNotFoundException;

/**
 * Represents an item.
 *
 * @author Jakub Sapalski
 */
public class Item extends SimpleInstruction {

	private StringProperty instruction = new SimpleStringProperty();
	
	public Item(QuestPackage pack, String id) throws PackageNotFoundException {
		if (pack == null) {
			throw new PackageNotFoundException();
		}
		this.pack = ID.parsePackage(pack, id.replace(" ", "_"));
		this.id = new SimpleStringProperty(ID.parseId(id.replace(" ", "_")));
	}
	
	public Item(QuestPackage pack, String id, String instruction) throws PackageNotFoundException {
		this(pack, id);
		this.instruction.set(instruction);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObservableList<Item> getList() {
		return pack.getItems();
	}

}
