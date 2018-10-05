package hello.com;


import javax.persistence.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid")
    private Long userID;

    @Column(name = "username")
    private String userName;

    @Column(name = "userimage")
    private String userImage;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static List<User> addingTestingValues (){
        List<User> testingUsers = new ArrayList<User>();

        for(int i = 0 ; i < 5; i++){
            User newUser = new User();
            Long userID = Long.parseLong(String.valueOf(i));
            String userName = "User-Name No." + (i+1);

            newUser.setUserID(userID);
            newUser.setUserName(userName);

            testingUsers.add(newUser);
        }

        return testingUsers;
    }

    @Override
    public String toString(){
        return userID + ", " + userName;
    }
}
