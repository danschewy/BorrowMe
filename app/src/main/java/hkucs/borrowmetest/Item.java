package hkucs.borrowmetest;

/**
 * Created by hasan on 04/12/2017.
 */

public class Item {

    String description;
    String title;
    String category;
    double price;
    String photoUrl;
    String ownerEmail;
    boolean isAvailable;

    public Item(){
    }

    public Item(String description,String title,String category,double price, String photoUrl,String ownerEmail){
        this.description = description;
        this.title = title;
        this.category = category;
        this.price = price;
        this.photoUrl = photoUrl;
        this.ownerEmail = ownerEmail;
        this.isAvailable = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

