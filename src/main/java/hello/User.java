package hello;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long userID;
    private String userName;

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
