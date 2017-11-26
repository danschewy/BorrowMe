package hkucs.borrowmetest;

import java.io.Serializable;

/**
 * Created by Dan on 11/8/17.
 */

public class RentItem implements Serializable{

    //counter to ensure unique id for every object
    private static int counter = 0;

    //object's unique id
    int id;
    int ownerId;

    boolean isAvailable = true;
    String description;
    String title;
    double pricePerHour;
    int[] images;
    byte[] image;

    //Constructor
    public RentItem(){
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

    void setAvailable(){
        isAvailable = !isAvailable;
    }

    boolean isAvailable(){
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

}
