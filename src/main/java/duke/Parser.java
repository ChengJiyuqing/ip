package duke;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    static Task parseFileItem (String taskString) throws DukeException{
        if (taskString.startsWith("[T]")) {
            String name = taskString.substring(8);
            if (taskString.charAt(5) == '\u2713') {
                return new TODO(name, Task.Status.DONE);
            } else {
                return new TODO(name, Task.Status.PENDING);
            }
        } else if (taskString.startsWith("[D]")) {
            String name = taskString.split(" by: ")[0].substring(8);
            String dueDate = taskString.split(" by: ")[1];
            if (taskString.charAt(5) == '\u2713') {
                return new Deadline(name, Task.Status.DONE,dueDate);
            } else {
                return new Deadline(name, Task.Status.PENDING,dueDate);
            }
        } else if (taskString.startsWith("[E]")){
            //System.out.println(taskString.split(" at: ").length);
            String name = taskString.split(" at: ")[0].substring(8);
            String dueDate = taskString.split(" at: ")[1];
            if (taskString.charAt(5) == '\u2713') {
                return new Event(name, Task.Status.DONE,dueDate);
            } else {
                return new Event(name, Task.Status.PENDING,dueDate);
            }
        } else if (taskString.isEmpty() || taskString.isBlank()) {
            return null;
        } else {
            throw new DukeException("error parsing storage file");
        }
    }

    static void parseInput (String userMessage, Storage storage) throws DukeException, IOException {
        ArrayList<Task> itemList = storage.load();

        if (!userMessage.equals("bye") && !userMessage.equals("list")
                && !userMessage.contains("done") && !userMessage.contains("delete")) {
            Task newItem;
            if (userMessage.startsWith("todo")) {
                String name = userMessage.substring(5);
                if (!name.isEmpty() && !name.isBlank()) {
                    newItem = new TODO(name, Task.Status.PENDING);
                } else {
                    throw new DukeException("Oops, tasks cannot be empty");
                }
            } else if (userMessage.startsWith("deadline")) {

                if (!userMessage.contains("/by")) {
                    throw new DukeException("Sorry, incorrect format for Deadlines. \n Please specify a Due Date " +
                            "(and task name)");
                } else {
                    String name = userMessage.split("/by")[0].substring(9);
                    if (name.isEmpty() || name.isBlank()) {
                        throw new DukeException("Oops, tasks cannot be empty");
                    }
                    String dueDate = userMessage.split("/by")[1].substring(1);
                    newItem = new Deadline(name, Task.Status.PENDING,dueDate);
                }

            } else if (userMessage.startsWith("event")) {

                if (!userMessage.contains("/at")) {
                    throw new DukeException("Sorry, incorrect format for Events. \n Please specify a time " +
                            "(and task name)");
                } else {

                    String name = userMessage.split("/at ")[0].substring(5);
                    if (name.isEmpty() || name.isBlank()) {
                        throw new DukeException("Oops, tasks cannot be empty");
                    }
                    String time = userMessage.split("/at ")[1];
                    newItem = new Event(name, Task.Status.PENDING,time);

                }
            } else {
                throw new DukeException("Sorry, I do not understand this command");
            }
            Storage.todoToFile(newItem);
            System.out.println("new task added: " + newItem.toString());
            System.out.println("You now have " + (itemList.size() + 1) + " tasks in your list!");
        }

        //list down the contents in the list
        if (userMessage.equals("list")) {
            itemList = storage.load();
            System.out.println("Here is your list: ");
            for (int i = 0; i < itemList.size(); i++) {
                Task task = itemList.get(i);
                System.out.println((i+1) + " " + task.toString());
            }
        }


        //mark something as done
        if (userMessage.contains("done")) {
            int index = Character.getNumericValue(userMessage.charAt(5)) - 1;
            Task task = itemList.get(index);
            task.markAsDone();
            storage.modifyWithList(itemList);
            System.out.println("Good job! You have finished this task!");
            System.out.println(task.toString());
        }

        //delete task
        if (userMessage.contains("delete")) {
            int index = Character.getNumericValue(userMessage.charAt(7)) - 1;
            Task task = itemList.get(index);
            itemList.remove(index);
            storage.modifyWithList(itemList);
            System.out.println("I have deleted this task for you: ");
            System.out.println(task.toString());
            System.out.println("You now have " + itemList.size() + " tasks in your list!");
        }


    }
}