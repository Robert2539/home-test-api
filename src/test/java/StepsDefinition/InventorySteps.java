package StepsDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import static org.hamcrest.Matchers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class InventorySteps {
	Response res;
	public static Map<String, String> completeBody;
	@Given("the API at api-inventory is up and running")
	public void the_api_at_api_inventory_is_up_and_running() {
		RestAssured.baseURI = "http://localhost:3100/";
	}

	@When("I send a GET request to inventory api")
	public void i_send_a_get_request_to_inventory_api() {
		res = RestAssured.given().when().get("api/inventory");
	}

	@Then("the response status should be {int}")
	public void the_response_status_should_be(int i) {
		//int s = Integer.parseInt(string);
		Assert.assertEquals(i, res.getStatusCode());
	}

	@And("the response should contain at least {int} items")
	public void the_response_should_contain_at_least_items(int items) {
		int size = res.jsonPath().getList("data").size();
		Assert.assertEquals(items, size);
		
	}

	@And("each item in the response should have id name price image")
	public void each_item_in_the_response_should_have_id_name_price_image() {
		 List<Map<String, ?>> items = res.jsonPath().getList("data");

	        for (Map<String, ?> item : items) {
	        	 Assert.assertTrue(item.containsKey("id"), "Missing 'id' field");
	             Assert.assertTrue(item.containsKey("name"), "Missing 'name' field");
	             Assert.assertTrue(item.containsKey("price"), "Missing 'price' field");
	             Assert.assertTrue(item.containsKey("image"), "Missing 'image' field");
	        }
	}
	

@When("I send a GET request to filter API with id={int} in query parameter")
public void i_send_a_get_request_to_filter_api_with_id_in_query_parameter(Integer id) {
	res = RestAssured.given().queryParam("id", id).when().get("api/inventory/filter");
}


@Then("the response should contain the item with id {string}, name {string}, price {string}, and image {string}")
public void the_response_should_contain_the_item_with_id_name_price_and_image(String expectedId, String expectedName, String expectedPrice, String expectedImage) {
	String actualId = res.jsonPath().getString("id");
    String actualName = res.jsonPath().getString("name");
    String actualPrice = res.jsonPath().getString("price");
    String actualImage = res.jsonPath().getString("image");

    Assert.assertEquals(actualId, expectedId, "ID mismatch");
    Assert.assertEquals(actualName, expectedName, "Name mismatch");
    Assert.assertEquals(actualPrice, expectedPrice, "Price mismatch");
    Assert.assertEquals(actualImage, expectedImage, "Image mismatch");
}

@When("a POST request is sent to add API with body id {string}, name {string}, image {string}, and price {string}")
public void a_post_request_is_sent_to_add_api_with_body_id_name_image_and_price(String id, String name, String image, String price) {
	 completeBody = new HashMap<String, String>();
	 completeBody.put("id", id);
	 completeBody.put("name", name);
	 completeBody.put("image", image);
	 completeBody.put("price", price);
     
     res = RestAssured.given().contentType("application/json").body(completeBody).when().post("api/inventory/add");
}


@When("a POST request is sent to add API with body name {string}, image {string}, and price {string}")
public void a_post_request_is_sent_to_add_api_with_body_name_image_and_price(String name, String image, String price) {
	 Map<String, String> body = new HashMap<String, String>();
     body.put("name", name);
     body.put("image", image);
     body.put("price", price);
     res = RestAssured.given().contentType("application/json").body(body).when().post("api/inventory/add");
}

@Then("the response body should contain the message {string}")
public void the_response_body_should_contain_the_message(String string) {
    String resBody = res.getBody().asString();
    Assert.assertTrue(resBody.contains(string));
}


@Then("the response should contain items which we added in add API")
public void the_response_should_contain_items_which_we_added_in_add_api() {
	
	RestAssured.given().when().get("api/inventory").then().body("data", hasItem(allOf(
            hasEntry("id", completeBody.get("id")),
            hasEntry("name", completeBody.get("name")),
            hasEntry("image", completeBody.get("image")),
            hasEntry("price", completeBody.get("price"))
        )));
	
}
}
