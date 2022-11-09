package api;

import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RandomString;
import java.util.HashMap;
import java.util.Map;


public class AddTwentyTask implements constants {

    Map<String, String> header;
    RegisterUser registerUser = null;
    LoginUser loginUser = null;
    AddTask addTaskUtil = null;
    String name = "Assignment";
    String age = "21";
    String email = "assignment" + RandomString.getAlphaNumericString(4) + "@gmail.com";
    String token = "";


    @Test
    public void AddTask() {
        header = new HashMap<String, String>();
        JSONObject register = new JSONObject();
        JSONObject login = new JSONObject();
        JSONObject addTask = new JSONObject();
        registerUser = new RegisterUser();
        loginUser = new LoginUser();
        addTaskUtil = new AddTask();
        header.put("Content-Type", "application/json");
        register.put("name", name);
        register.put("email", email);
        register.put("password", "Assignment");
        register.put("age", age);
        Response registerResponse = registerUser.registerGeneric(register, header);
        System.out.println("Register Response body: " + registerResponse.getBody().prettyPrint());
        String Id = registerResponse.jsonPath().getString("user._id");
        Assert.assertEquals(registerResponse.jsonPath().getString("user.email"), email, "email does not match");
        Assert.assertEquals(registerResponse.jsonPath().getString("user.name"), name, "name does not match");
        Assert.assertEquals(registerResponse.jsonPath().getInt("user.age"), Integer.parseInt(age), "age does not match");
        login.put("email", registerResponse.jsonPath().getString("user.email"));
        login.put("password", "Assignment");
        Response loginResponse = loginUser.loginGeneric(login, header);
        System.out.println("Login Response body: " + loginResponse.getBody().prettyPrint());
        token = loginResponse.jsonPath().getString("token");
        Assert.assertEquals(loginResponse.jsonPath().getString("user.email"), email, "email does not match");
        Assert.assertEquals(loginResponse.jsonPath().getString("user.name"), name, "name does not match");
        Assert.assertEquals(loginResponse.jsonPath().getInt("user.age"), Integer.parseInt(age), "age does not match");
        Assert.assertEquals(loginResponse.jsonPath().getString("user._id"), Id, "Id does not match");
        header.put("Authorization", "Bearer" + token);
        for (int i = 1; i <= 20; i++) {
            addTask.put("description", "reading book-" + i);
            Response taskResponse = addTaskUtil.addTaskGeneric(addTask, header);
            System.out.println("Task Response body: " + taskResponse.getBody().prettyPrint());
        }
    }
}


