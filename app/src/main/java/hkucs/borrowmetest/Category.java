package hkucs.borrowmetest;

public class Category {
    private int id = 0;
    String title;

    public Category(){
        id = id++;
    }

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
