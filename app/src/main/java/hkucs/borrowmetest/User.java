package hkucs.borrowmetest;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Hasan on 11/20/2017.
 */

public class User implements Serializable {

    String name;
    String address;
    String email;

    public User(){

    }
    public User(String name, String address, String email){
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
