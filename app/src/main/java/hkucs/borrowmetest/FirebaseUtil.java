package hkucs.borrowmetest;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by hasan on 04/12/2017.
 */

public class FirebaseUtil {

    DatabaseReference mDatabaseReference;

    public FirebaseUtil(DatabaseReference databaseReference){
        this.mDatabaseReference = databaseReference;
    }

    public void writeNewUser(String userId, String name, String email, String address){
        User user = new User(name,email,address);
        mDatabaseReference.child("users").child(userId).setValue(user);
    }

    public void writeNewItem(String itemId, String description, String title, String category, double price, String photoUrl, String ownerId){
        Item item = new Item(description,title,category,price,photoUrl, ownerId);
        mDatabaseReference.child("items").child(itemId).setValue(item);
    }

}
