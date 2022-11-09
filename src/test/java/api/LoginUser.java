package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RandomString;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginUser implements constants {

    Map<String, String> header;
    RegisterUser registerUser = null;
    JSONObject jsonObject;

    public Response loginGeneric(Object request, Map<String, String> header) {
        return given().log().all().contentType(ContentType.JSON).baseUri(BASE_URL).headers(header)
                .body(request).when().post(LOGIN).then().log().all().extract().response();
    }

    @Test
    public void valid_Data() {
        header = new HashMap<String, String>();
        JSONObject register = new JSONObject();
        JSONObject login = new JSONObject();
        registerUser = new RegisterUser();
        String name = "Assignment";
        String age = "22";
        String email = "assignment" + RandomString.getAlphaNumericString(4) + "@gmail.com";
        header.put("Content-Type", "application/json");
        register.put("name", name);
        register.put("email", email);
        register.put("password", "Assignment");
        register.put("age", age);
        Response res = registerUser.registerGeneric(register, header);
        System.out.println("Register Response body: " + res.getBody().prettyPrint());
        login.put("email", res.jsonPath().getString("user.email"));
        login.put("password", "Assignment");
        res = loginGeneric(login, header);
        System.out.println("Login Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "200", "Status code doesnot match");
        Assert.assertEquals(res.jsonPath().getString("user.email"), email, "email does not match");
        Assert.assertEquals(res.jsonPath().getString("user.name"), name, "name does not match");
        Assert.assertEquals(res.jsonPath().getInt("user.age"), Integer.parseInt(age), "age does not match");
    }

    @Test
    public void invalid_Email() {
        System.out.println("Test description : Verify by passing Invalid email");
        header = new HashMap<String, String>();
        JSONObject login = new JSONObject();
        header.put("Content-Type", "Application/json");
        login.put("email", "Assignment");
        login.put("password", "Assignment");
        Response res = loginGeneric(login, header);
        System.out.println("login Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "400", "Status code doesnot match");
        Assert.assertEquals(res.asString().replace("\"", ""), "Unable to login", "Response message does not match");
    }

    @Test
    public void notRegisteredUser() {
        System.out.println("Test description : Login with user details which is not registered");
        header = new HashMap<String, String>();
        JSONObject login = new JSONObject();
        header.put("Content-Type", "Application/json");
        login.put("email", "abcd@gmail.com");
        login.put("password", "Assignment");
        Response res = loginGeneric(login, header);
        System.out.println("login Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "400", "Status code doesnot match");
        Assert.assertEquals(res.asString().replace("\"", ""), "Unable to login", "Response message does not match");
    }

}




