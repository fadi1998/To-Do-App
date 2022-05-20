package fadiebraheem.utils;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

//Data access Object
public class DAO {
    //region Properties
    private MongoClient client;
    private ConnectionString connectionString;
    //endregion

    //region Constructors
    public DAO(){
        connectionString = new ConnectionString("mongodb+srv://Fadi:2gzPrNvWzxllx1VQ@fadicluster.axynb.mongodb.net/?retryWrites=true&w=majority");
        client = MongoClients.create(connectionString);
    }
    //endregion

    //region Methods

    //This method add a task to the database
    public String addTask(Task task){
        Document doc = new Document(task.toMap());
        this.client.getDatabase("ToDoListDB")
                .getCollection("Tasks")
                .insertOne(doc);
        return doc.get("_id").toString();
    }

    //this method delete a task from the database
    public void deleteTask(String id){
        this.client.getDatabase("ToDoListDB")
                .getCollection("Tasks")
                .deleteOne(new Document("_id", new ObjectId(id)));
    }

    //this method edits a task
    public void editTask(String id, Bson updatedDocument){
        this.client.getDatabase("ToDoListDB")
                .getCollection("Tasks")
                .updateOne(new Document("_id",new ObjectId(id)), updatedDocument);
    }

    //this method gets the data from the database
    //returns Iterable<Document>
    public Iterable<Document> getData(){
        return this.client.getDatabase("ToDoListDB")
                .getCollection("Tasks")
                .find();
    }
    //endregion
}