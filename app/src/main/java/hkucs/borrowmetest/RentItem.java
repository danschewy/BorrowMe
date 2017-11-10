package hkucs.borrowmetest;

import java.util.ArrayList;

/**
 * Created by chrx on 11/8/17.
 */

public class RentItem {
    int id;
    String description;
    String title;
    ArrayList images;

    void RentItem(){

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

}
