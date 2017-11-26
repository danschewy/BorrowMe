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
    String first_name;
    String last_name;
    String address;
    String email;
    ArrayList <Integer> rentedItems;
    ArrayList <Integer> itemsForRent;

    public User (){
    }
    public User (String first_name, String last_name,String address, String email){
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.email = email;
    }
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setUserId (int id){
        this.id = id;
    }
    public void setAddress (String address){
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addRentedItem (Integer itemid) {
        rentedItems.add(itemid);
    }

    public void addItemForRent (Integer itemid) {
        itemsForRent.add(itemid);
    }

    public String getAddress() {
        return address;
    }
    public String getEmail(){
        return email;
    }
    int getId () {
        return id;
    public void setId(int id) {
        this.id = id;
    }
}
