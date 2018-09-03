package kobdratenkov.ncedu.ru.lifetracker;

import java.io.Serializable;


public class User implements Serializable {

    private Long userID;

    private String login;

    private String hashPassword;

    private String firstName;

    private String lastName;

    public User() {
    }

    public User(String login, String hashPassword) {
        this.login = login;
        this.hashPassword = hashPassword;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return String.format(" User: id = %d, login = '%s', password = '%s'", userID, login, hashPassword);
    }
}

