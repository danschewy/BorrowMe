package hkucs.borrowmetest;

import java.util.ArrayList;

/**
 * Created by Dan on 11/8/17.
 */

public class RentItem {

    //counter to ensure unique id for every object
    private static int counter = 0;

    //object's unique id
    int objectId;
    int ownerId;

    boolean isAvailable;
    String description;
    String title;
    double pricePerHour;
    ArrayList <String> images;
    ArrayList <String> tags;

    //Constructor
    void RentItem(int owner){
        objectId = counter++;
        ownerId = owner;
    }



    void addDescription(String s){
        description = s;
    }

    void addTitle(String t){
        title = t;
    }

    void addImage(String path){
        images.add(path);
    }

    void addPricePerHour(double price){
        pricePerHour = price;
    }

    void rentItem(){
        isAvailable = !isAvailable;
    }

    void addTag(String tag){
        tags.add(tag);
    }
}
