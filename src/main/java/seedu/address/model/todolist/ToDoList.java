package seedu.address.model.todolist;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of actions that the user may wish to take related to their transactions.
 */
public class ToDoList {
    private List<String> list;
    private int index;

    public ToDoList() {
        this.list = new ArrayList<>(list);
        int index = this.list.size() - 1;
    }

    /**
     * Appends a user input String to the end of the list.
     */
    public void add(String element) {
        list.add(element);
    }

    /**
     * Removes a specific String that user initially input.
     * @param index
     */
    public void delete(int index) {
        list.remove(index);
    }
}
