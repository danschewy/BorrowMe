package hkucs.borrowmetest;

public class Category {
    private int id;
    String title;

    public Category(){

    }

    public Category(String title){
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
    public void setId (int id) {
        this.id = id;
    }


}
