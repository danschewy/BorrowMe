package hkucs.borrowmetest;

/**
 * Created by H on 11/26/2017.
 */

public class Category {
    private int id = 0;
    String title;

    public Category(String title){
        id = id++;
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public int getId(){
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
