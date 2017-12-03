package hkucs.borrowmetest;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by Dan on 11/8/17.
 */

public class RentItem implements Serializable{

    private static ArrayList<RentItem> items = new ArrayList<>();
    //counter to ensure unique id for every object
    private static int counter = 0;

    //object's unique id
    int id;
    int ownerId;

    int isAvailable = 1;
    String description;
    String title;
    double pricePerHour;
    int[] images;
    byte[] image;
    int categoryId;


    //Constructor
    public RentItem(){
    }


    public static ArrayList<RentItem> getItems() {
        return items;
    }

    public static void setItems(ArrayList<RentItem> items) {
        RentItem.items = items;
    }

    void setDescription(String description){
        this.description = description;
    }

    String getDescription(){
        return description;
    }

    void setTitle(String title){
        this.title = title;
    }

    String getTitle(){
        return title;
    }

    void setImage(byte[] image){
        this.image = image;
    }

    byte[] getImage(){
        return this.image;
    }
    void setImages(int[] images){
        this.images = images;
    }

    int[] getImages(){
        return images;
    }

    void setPricePerHour(double price){
        pricePerHour = price;
    }

    double getPricePerHour(){
        return pricePerHour;
    }

    void setAvailable(int a){
        isAvailable = a;
    }

    int isAvailable(){
        return isAvailable;
    }

    int getOwnerId(){
        return ownerId;
    }

    int getId(){
        return id;
    }

    void setId(int id){
        this.id = id;
    }

    void setOwnerId(int id){
        this.ownerId = id;
    }

    void setCategoryId(int id){
        this.categoryId = id;
    }
    int getCategoryId(){
        return categoryId;
    }

}
