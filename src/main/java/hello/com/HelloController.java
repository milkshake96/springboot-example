package hello.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        userRepository.save(newUser);

        return userRepository.findAll();
    }

//    //Read a specific User
//    @RequestMapping (value = "/user/{userID}", method = RequestMethod.GET)
//    public Object getUser(@PathVariable("userID") Long userID){
//        User selectedUser = searchforUser(userID);
//        String jsonString = "";
//
//        if(selectedUser != null){
//            jsonString = JSONconverter(selectedUser);
//        } else {
//            jsonString = "cannot find selected user";
//        }
//
//        System.out.println(jsonString);
//
//        return jsonString;
//    }

//    //Remove a new User
//    @RequestMapping (value = "/user/{userID}", method = RequestMethod.POST)
//    public Object removeUser(@PathVariable("userID") Long userID){
//        User selectedUser = searchforUser(userID);
//        listOfUsers.remove(selectedUser);
//        String jsonString = JSONconverter(listOfUsers);
//
//        return jsonString;
//    }

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

//    public User searchforUser(Long userID){
//        User selectedUser = null;
//
//        for (User existingUser : listOfUsers){
//            if(existingUser.getUserID().equals(userID)){
//                selectedUser = existingUser;
//            }
//        }
//
//        return selectedUser;
//    }


}
