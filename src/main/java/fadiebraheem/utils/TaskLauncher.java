package fadiebraheem.utils;

import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskLauncher {

    private final DAO mongoDao;

    public TaskLauncher() {
        Logger logger = Logger.getLogger("org.mongodb.driver");
        logger.setLevel(Level.SEVERE);

        Scanner in = new Scanner(System.in);
        mongoDao = new DAO();
        int option,day,month,year;
        String id;

        // fetching data from the database and mapping each object with task id
        mongoDao.getData()
                .forEach(document -> {
                    Task task = new Task( document.get("content").toString(), document.get("date").toString()
                            ,document.get("status").toString().equals(TaskStatus.IN_PROGRESS.toString()) ? TaskStatus.IN_PROGRESS : TaskStatus.DONE);
                            Task.tasks.put(document.get("_id").toString(),task);
                });

        while(true) {
            System.out.println("1.View Your Tasks");
            System.out.println("2.Add a New Task");
            System.out.println("3.Delete a Task");
            System.out.println("4.Edit a Task");
            System.out.println("5.Exit");
            System.out.print("Pick An Option: ");

            option = in.nextInt();

            System.out.println("\n\n");
            switch (option) {
                case 1:
                    //region View task
                    Task.tasks
                            .forEach((_id, task) -> {
                                System.out.println(_id + "\n=============================");
                                System.out.println(task);
                            });
                    break;
                    //endregion
                case 2:
                    //region Add task
                    String content, date;
                    String[] splittedDate;
                    in.nextLine();
                    System.out.println("Enter a task");
                    content = in.nextLine();

                    System.out.println("Enter The Date (DD/MM/YYYY)");
                    date = in.nextLine();

                    while (!date.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")) {
                        System.out.println("Enter a Valid Date Format(DD/MM/YYYY)");
                        date = in.nextLine();

                    }

                    splittedDate = date.split("/");

                     day = Integer.parseInt(splittedDate[0]);
                     month = Integer.parseInt(splittedDate[1]);
                     year = Integer.parseInt(splittedDate[2]);

                     Task task = new  Task(content, DateFormat.getInstance().format(new Date(year, month, day)));
                    id = mongoDao.addTask(task);
                    Task.tasks.put(id,task);
                    System.out.println("Added successfully");
                    break;
                    //endregion
                case 3:
                    //region Delete task
                    System.out.println("Enter The ID  You Wish To Delete");
                     id = in.next();

                    if(Task.tasks.containsKey(id)) {
                        mongoDao.deleteTask(id);
                        Task.tasks.remove(id);
                        System.out.println("Deleted successfully");
                    }
                    else
                        System.out.println("ID NOT FOUND");

                    break;
                    //endregion
                case 4:
                    //region Edit task
                    System.out.println("Enter The ID you Wish To Edit");
                    id = in.next();

                    if(Task.tasks.containsKey(id)) {
                        String updatedContent = Task.tasks.get(id).getContent();
                        String updatedDate  = Task.tasks.get(id).getDate();
                        Bson updatedDocument;

                        System.out.println("do you wish to change the content Y/N?");
                        in.nextLine();
                        if(in.nextLine().equalsIgnoreCase("Y")) {
                            System.out.println("Enter new content");
                            updatedContent = in.nextLine();
                        }

                        System.out.println("do you wish to change the date Y/N?");
                        if(in.nextLine().equalsIgnoreCase("Y")){
                             System.out.println("enter new date");
                             updatedDate = in.nextLine();

                            while (!updatedDate.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")) {
                                System.out.println("Enter a Valid Date Format(DD/MM/YYYY)");
                                updatedDate = in.nextLine();

                            }

                            splittedDate = updatedDate.split("/");

                             day = Integer.parseInt(splittedDate[0]);
                             month = Integer.parseInt(splittedDate[1]);
                             year = Integer.parseInt(splittedDate[2]);
                            updatedDate = DateFormat.getInstance().format(new Date(year, month, day));

                        }

                        System.out.println("Do You Wish To Change Your Task Status? Y/N");
                        TaskStatus updateStatus = Task.tasks.get(id).getStatus();

                        if(in.nextLine().equalsIgnoreCase("Y")){
                                if(updateStatus == TaskStatus.IN_PROGRESS)
                                    updateStatus = TaskStatus.DONE;
                                else
                                    updateStatus = TaskStatus.IN_PROGRESS;
                        }

                        updatedDocument = Updates.combine(
                            Updates.set("content",updatedContent),
                            Updates.set("date", updatedDate),
                            Updates.set("status", updateStatus.toString())
                        );
                        mongoDao.editTask(id, updatedDocument);

                        Task.tasks.put(id,new Task(updatedContent, updatedDate, updateStatus));
                        System.out.println("Edited successfully");
                    }
                    else
                        System.out.println("ID not found");
                    break;
                    //endregion
                default:
                    return;
            }
            System.out.println("\n\n");
        }
    }
}
