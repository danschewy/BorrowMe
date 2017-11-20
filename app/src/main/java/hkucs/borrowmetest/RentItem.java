package hkucs.borrowmetest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dan on 11/8/17.
 */

public class RentItem implements Serializable{

    //counter to ensure unique id for every object
    private static int counter = 0;

    //object's unique id
    int objectId;
    int ownerId;

    boolean isAvailable = true;
    String description;
    String title;
    double pricePerHour;
    int[] images;
    ArrayList <String> tags;

    //Constructor
    public RentItem(int owner){
        objectId = counter++;
        ownerId = owner;
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

    void setAvailable(){
        isAvailable = !isAvailable;
    }

    boolean isAvailable(){
        return isAvailable;
    }


    void addTag(String tag){
        tags.add(tag);
    }
}
