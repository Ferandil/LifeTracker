package kobdratenkov.ncedu.ru.lifetracker;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

import kobdratenkov.ncedu.ru.lifetracker.model.Role;


public class User implements Serializable {

    private Long userID;

    private String email;

    private String password;

    private String name;

    private String surname;

    private Role role;

    private boolean facebook;

    public User() {
    }

    public User(String login, String password) {
        this.email = login;
        this.password = password;
    }



    /**
     * General getters and setters
     */
    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String hashPassword) {
        this.password = hashPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String lastName) {
        this.surname = lastName;
    }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }


    @Override
    public String toString(){
        return String.format(" User: id = %d, login = '%s', password = '%s'", userID, email, password);
    }
}

