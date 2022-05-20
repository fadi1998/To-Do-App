package fadiebraheem;

import fadiebraheem.utils.TaskLauncher;

public class ToDoList {

    public static void main(String[] args) {
        try {
            new TaskLauncher();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
