package hello.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class HelloController {

    //List<User> listOfUsers = new ArrayList<User>(User.addingTestingValues());

    @Autowired
    private UserRepository userRepository;

    @RequestMapping (value = "/")
    public String index(){
        return "Greetings from spring boot!";
    }

    //Get All users
    @RequestMapping (value = "/users", method = RequestMethod.GET)
    public Object listUsers(){
        return userRepository.findAll();
    }

    //Add new User
    @RequestMapping (value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addUser(@RequestBody User newUser){
        ArrayList<User> usersInDB = (ArrayList<User>) userRepository.findAll();
        boolean existingUser = false;

        if(newUser.getUserID()!=null) {
            for (int i = 0; i < usersInDB.size(); i++) {
                if (usersInDB.get(i).equals(newUser.getUserID())) {
                    existingUser = true;
                    userRepository.findByUserID(usersInDB.get(i).getUserID()).setUserName(newUser.getUserName());
                }
            }

            if(!existingUser)
                userRepository.save(newUser);

        } else {
            userRepository.save(newUser);
        }

        return userRepository.findAll();
    }

    //Read a specific User
    @RequestMapping (value = "/user/{userID}", method = RequestMethod.GET)
    public Object getUser(@PathVariable("userID") Long userID){
        User selectedUser = searchforUser(userID);

        System.out.println(selectedUser);

        if(selectedUser != null){
            return userRepository.findByUserID(selectedUser.getUserID());
        } else {
            return "cannot find selected user";
        }
    }

    //Remove a new User
    @RequestMapping (value = "/user/{userID}", method = RequestMethod.POST)
    public Object removeUser(@PathVariable("userID") Long userID){
        User selectedUser = searchforUser(userID);
        userRepository.delete(selectedUser);

        return userRepository.findAll();
    }

    //Converting Object to JSON String
    public String JSONconverter(Object anyObject){
        String jsonString = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(anyObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    public User searchforUser(Long userID){
        User selectedUser = null;

        for (User existingUser : userRepository.findAll()){
            if(existingUser.getUserID().equals(userID)){
                selectedUser = existingUser;
            }
        }

        return selectedUser;
    }


}
