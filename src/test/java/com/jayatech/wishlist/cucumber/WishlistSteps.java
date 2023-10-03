package com.jayatech.wishlist.cucumber;

import com.jayatech.wishlist.api.controller.WishlistController;
import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishlistFoundException;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import com.jayatech.wishlist.domain.repository.WishlistRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishlistSteps {

    @Autowired
    private WishlistController wishlistController;

    @Autowired
    private WishlistRepository wishlistRepository;
    private UserDTO user;
    private ResponseEntity<Wishlist> response;
    private ResponseEntity<ProductCheckResponse> checkResponse;

    private String wishlistId;

    private String productId;

    private String wishlistItemId;

    private Exception exception;

    @When("send a product id with id {string}")
    public void sendProductId(String id) {
        productId = id;
    }

    @Given("a user with userId {string}")
    public void userWithUserId(String userId) {
        user = new UserDTO();
        user.setUserId(userId);
    }


    @When("the user creates a wishlist")
    public void theUserCreatesAWishlist() {
        response = wishlistController.createWishlist(user);
    }

    @When("the user doesn't create a wishlist")
    public void theUserDoesntCreatesAWishlist() {
        try {
            response = wishlistController.createWishlist(user);
        } catch (WishlistFoundException e) {
            exception = e;
        }
    }

    @Then("a wishlist is created successfully")
    public void wishlistIsCreatedSuccessfully() {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Wishlist wishlist = response.getBody();
        wishlistId = response.getBody().getId();
        assertNotNull(wishlist);
    }

    @Then("should not create a wishlist a {string}")
    public void verifyResponseContainsMessage(String status) {
        assertNotNull(exception);
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientException = (HttpClientErrorException) exception;
            assertEquals(HttpStatus.NOT_FOUND, httpClientException.getStatusCode());
            assertTrue(httpClientException.getResponseBodyAsString().contains(status));
        }
    }

    @Given("there is an existing wishlist by userId {string}")
    public void thereIsAnExistingWishlistByUserId(String userId) {
        wishlistId = wishlistRepository.findByUserId(userId).get().getId();
    }

    @Given("there is an existing wishlist with ID {string}")
    public void thereIsAnExistingWishlistWithID(String id) {
        wishlistId = id;
    }

    @When("I request the wishlist with ID")
    public void requestTheWishlistWithID() {
        try {
            response = wishlistController.getWishList(wishlistId);
        } catch (ResourceNotFoundException e) {
            exception = e;
        }
    }

    @Then("the response with a wishlist by id")
    public void responseWishWishlistById() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Wishlist wishlist = response.getBody();
        assertNotNull(wishlist);
    }

    @Then("the response with a wishlist by id not found")
    public void responseWishWishlistByIdNotFound() {
        assertNotNull(exception);
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientException = (HttpClientErrorException) exception;
            assertEquals(HttpStatus.NOT_FOUND, httpClientException.getStatusCode());
        }
    }

    @Then("increment wishlist with a product")
    public void incrementWishlistWithAProduct() {
        try {
            response = wishlistController.incrementWishlist(wishlistId, productId);
        } catch (RegisteredProductException e) {
            exception = e;
        }
    }

    @Then("the response the wishlist with the product")
    public void responseWishlistWithProduct() {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Wishlist wishlist = response.getBody();
        assertNotNull(wishlist);
    }

    @Then("the response the wishlist exception product registered")
    public void responseWishlistProductRegistered() {
        assertNotNull(exception);
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientException = (HttpClientErrorException) exception;
            assertEquals(HttpStatus.BAD_REQUEST, httpClientException.getStatusCode());
        }
    }

    @Then("check if the product is on the wishlist")
    public void checkIfTheProductIsOnTheWishlist() {
        checkResponse = wishlistController.checkWishList(response.getBody().getId(), productId);
    }

    @Then("the response should be checkProduct")
    public void theResponseShouldBeCheckProduct() {
        assertEquals(HttpStatus.OK, checkResponse.getStatusCode());
        ProductCheckResponse productCheckResponse = checkResponse.getBody();
        assertNotNull(productCheckResponse);
    }

    @Then("the response should be checkProduct not exists")
    public void theResponseShouldBeCheckProductNotExists() {
        assertEquals(HttpStatus.OK, checkResponse.getStatusCode());
        ProductCheckResponse productCheckResponse = checkResponse.getBody();
        assertNull(productCheckResponse.getProduct());
    }

    @When("get wishlist item")
    public void getWishlistItem() {
        wishlistItemId = response.getBody().getWishListItems().get(0).getId();
    }

    @Then("delete a wishlistItem of a wishlist")
    public void deleteAWishlistItemOfAWishlist() {
        try {
            wishlistController.deleteProductWishList(wishlistId, wishlistItemId);
        } catch (ResourceNotFoundException e) {
            exception = e;
        }
    }

    @Given("get wishlist item with itemId {string}")
    public void getWishlistItemWithItemId(String itemId) {
        wishlistItemId = itemId;
    }

    @Then("the exception should be wishlist item not found")
    public void theExceptionShouldBeWishlistItemNotFound() {
        assertNotNull(exception);
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientException = (HttpClientErrorException) exception;
            assertEquals(HttpStatus.NOT_FOUND, httpClientException.getStatusCode());
        }
    }

    @Then("cleanUp by userId {string}")
    public void cleanupByUserId(String userId) {
        String id = wishlistRepository.findByUserId(userId).get().getId();
        wishlistRepository.deleteById(id);
    }
}