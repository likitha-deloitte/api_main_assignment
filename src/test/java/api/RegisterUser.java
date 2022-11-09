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

public class RegisterUser implements constants {

    Map<String, String> header;
    JSONObject jsonObject;

    public Response registerGeneric(Object request, Map<String, String> header) {
        return given().log().all().contentType(ContentType.JSON).baseUri(BASE_URL).headers(header)
                .body(request).when().post(REGISTER).then().log().all().extract().response();
    }

    @Test
    public void valid_Data() {
        System.out.println("Test description : Verify by passing valid values");
        header = new HashMap<String, String>();
        jsonObject = new JSONObject();
        String name = "Assignment";
        String age = "22";
        String email = "assignment" + RandomString.getAlphaNumericString(4) + "@gmail.com";
        header.put("Content-Type", "application/json");
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("password", "Assignment");
        jsonObject.put("age", age);
        Response res = registerGeneric(jsonObject, header);
        System.out.println(" Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "201", "Status code doesnot match");
        Assert.assertEquals(res.jsonPath().getString("user.email"), email, "email does not match");
        Assert.assertEquals(res.jsonPath().getString("user.name"), name, "name does not match");
        Assert.assertEquals(res.jsonPath().getInt("user.age"), Integer.parseInt(age), "age does not match");
    }

    @Test
    public void invalid_Email() {
        System.out.println("Test description : Verify by passing Invalid email");
        header = new HashMap<String, String>();
        jsonObject  = new JSONObject();
        String name="Assignment";
        String age = "22";
        header.put("Content-Type", "Application/json");
        jsonObject.put("name",name);
        jsonObject.put("email", "Assignment");
        jsonObject.put("password", "Assignment");
        jsonObject.put("age",age);
        Response res = registerGeneric(jsonObject,header);
        System.out.println(" Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "400", "Status code doesnot match");
        Assert.assertEquals(res.asString().replace("\"", ""), "User validation failed: email: Email is invalid", "Response message does not match");
    }

    @Test
    public void registerWithSameUser() {
        System.out.println("Test description : Register with same user details and verify error");
        header = new HashMap<String, String>();
        JSONObject register = new JSONObject();
        String name="Assignment";
        String age="22";
        String email="assignment"+RandomString.getAlphaNumericString(4)+"@gmail.com";
        header.put("Content-Type", "Application/json");
        register.put("name",name);
        register.put("email", email);
        register.put("password", "Assignment");
        register.put("age",age);
        Response res = registerGeneric(register, header);
        System.out.println(" Response body: " + res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()), "201", "Status code doesnot match");
        res=registerGeneric(register,header);
        System.out.println("Registering with same user Response body:" +res.getBody().prettyPrint());
        Assert.assertEquals(String.valueOf(res.getStatusCode()),"400","Status code does not match");
    }

}





