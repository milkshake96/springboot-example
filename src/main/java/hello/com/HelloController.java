package hello.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

//For Images
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;

@RestController
public class HelloController {

    //List<User> listOfUsers = new ArrayList<User>(User.addingTestingValues());

    @Autowired
    private UserRepository userRepository;

    @RequestMapping (value = "/")
    public String index(){
        return "Greetings from spring boot!";
    }

    //Find all image using path saved in DB and convert all image to 64Base
    //Get All users
    @RequestMapping (value = "/users", method = RequestMethod.GET)
    public Object listUsers(){
        ArrayList<User> usersInDB = (ArrayList<User>) userRepository.findAll();

        for (int i = 0; i < usersInDB.size(); i++) {
            //Delete
            System.out.println(usersInDB.get(i).getUserImage());
            loadingUserImage(usersInDB.get(i));
        }

        return JSONconverter(usersInDB);
    }

    //Take in base64 convert to file and store
    //Add new User
    @RequestMapping (value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addUser(@RequestBody User newUser){
        ArrayList<User> usersInDB = (ArrayList<User>) userRepository.findAll();
        boolean existingUser = false;

        //Store image as file
        storingUserImage(newUser);

        if(newUser.getUserID()!=null) {
            for (int i = 0; i < usersInDB.size(); i++) {
                if (usersInDB.get(i).equals(newUser.getUserID())) {
                    existingUser = true;
                    userRepository.findByUserID(usersInDB.get(i).getUserID()).setUserName(newUser.getUserName());
                    userRepository.findByUserID(usersInDB.get(i).getUserID()).setUserImage(newUser.getUserImage());
                }
            }

            if(!existingUser)
                userRepository.save(newUser);

        } else {
            userRepository.save(newUser);
        }

        return userRepository.findAll();
    }

    //Find image and return as base64
    //Read a specific User
    @RequestMapping (value = "/user/{userID}", method = RequestMethod.GET)
    public Object getUser(@PathVariable("userID") Long userID){
        User selectedUser = searchforUser(userID);
        if(selectedUser == null)
            return "User does not exist in the database!";

        loadingUserImage(selectedUser);
        return selectedUser;
    }

    //Delete the image file first before deleting the data in database
    //Remove a new User
    @RequestMapping (value = "/user/{userID}", method = RequestMethod.POST)
    public Object removeUser(@PathVariable("userID") Long userID){
        User selectedUser = searchforUser(userID);
        if(selectedUser == null)
            return "User does not exist in the database!";

        userRepository.delete(selectedUser);

        ArrayList<User> usersInDB = (ArrayList<User>)userRepository.findAll();

        for(int i = 0 ; i < usersInDB.size(); i++){
            loadingUserImage(usersInDB.get(i));
        }

        String json = JSONconverter(usersInDB);

        return json;
    }

    //Display Specific user image
    @RequestMapping (value = "/userImg/{userID}", method = RequestMethod.GET)
    public Object displayUserImage(@PathVariable("userID") Long userID){
        User selectedUser = searchforUser(userID);
        if(selectedUser == null)
            return "User does not exist in the database!";


        String[] initialImgPath = selectedUser.getUserImage().split("\\.");

        loadingUserImage(selectedUser);

        String redirectURL = "<img src=\"data:image/"+ initialImgPath[initialImgPath.length-1] + ";charset=utf-8;base64,";
        redirectURL = redirectURL.trim();
        redirectURL = redirectURL + selectedUser.getUserImage();
        redirectURL = redirectURL + "\" />";

        System.out.println(redirectURL);
        return redirectURL;
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

    //Parse Iterator into List
    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    //Additional function to search for existing user in DB using ID
    public User searchforUser(Long userID){
        User selectedUser = null;

        for (User existingUser : userRepository.findAll()){
            if(existingUser.getUserID().equals(userID)){
                selectedUser = existingUser;
            }
        }

        return selectedUser;
    }

    //Store User image as file and store location in userImage attribute
    public User storingUserImage(User user){
        if(user.getUserImage()!=null){

            String base64String = user.getUserImage();
            String[] strings = base64String.split(",");
            String extension;
            switch (strings[0]) {//check image's extension
                case "data:image/jpeg;base64":
                    extension = "jpeg";
                    break;
                case "data:image/png;base64":
                    extension = "png";
                    break;
                default://should write cases for more images types
                    extension = "jpg";
                    break;
            }
            //convert base64 string to binary data
            byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
            String path = "C:\\Users\\User-Pc\\Desktop\\userTestingImage\\" + user.getUserID() + "." + extension;
            File file = new File(path);
            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                outputStream.write(data);
                user.setUserImage(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    //Load User image to base64 and store image in userImage attribute
    public User loadingUserImage(User user){
        if(user.getUserImage()!=null){
            File file =  new File(user.getUserImage());
            String encodstring = "";

            try {
                FileInputStream fileInputStreamReader = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fileInputStreamReader.read(bytes);
                encodstring = Base64.encodeBase64String(bytes);
                user.setUserImage(encodstring);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return user;
    }
}
