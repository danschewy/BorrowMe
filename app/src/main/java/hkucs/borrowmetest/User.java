package hkucs.borrowmetest;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Hasan on 11/20/2017.
 */

public class User implements Serializable {

    private static int counter = 0;
    private static User current;


    int id;
    String name;
    String address;
    String email;
    ArrayList <Integer> rentedItems;
    ArrayList <Integer> itemsForRent;

    public User (){
        id = counter++;
    }
    public User (String name,String address, String email){
        this.name = name;
        this.address = address;
        this.email = email;
        id = counter++;
    }


    void setId (int userId){
        this.id = userId;
    }
    void setName(String name){
        this.name = name;
    }
    void setAddress (String address){
        this.address = address;
    }
    public static void setCurrentUser(User user){current = user;}

    void setemail(String email) {
        this.email = email;
    }

    void addRentedItem (Integer itemid) {
        rentedItems.add(itemid);
    }

    void addItemForRent (Integer itemid) {
        itemsForRent.add(itemid);
    }

    public static User getCurrentUser() {
        return current;
    }
    String getName(){
        return name;
    }
    String getAddress() {
        return address;
    }
    String getEmail(){
        return email;
    }
    int getId () {
        return id;
    }

}
