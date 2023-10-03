package com.jayatech.wishlist.cucumber;

import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.service.ProductService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductSteps {

    @LocalServerPort
    private int port;

    private ResponseEntity<Product> response;
    private Product product;
    private Exception exception;

    @Autowired
    private ProductService productService;

    @Given("there is an existing product with ID {string}")
    public void createExistingProduct(String productId) {
        product = productService.findById(productId);
    }

    @Given("there is no product with ID {string}")
    public void setNonExistingProductId(String productId) {
        try {
            product = productService.findById(productId);
        } catch (ResourceNotFoundException e) {
            exception = e;
        }
    }

    @When("I request the product with ID {string}")
    public void requestProductWithId(String productId) {
        try {
            String baseUrl = "http://localhost:" + port + "/products/" + productId;
            response = new RestTemplate().getForEntity(baseUrl, Product.class);
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCodeValue());
    }

    @Then("the response should contain the following product details:")
    public void verifyResponseContainsProductDetails(Map<String, String> productDetails) {
        Product productResponse = response.getBody();
        assertNotNull(productResponse);
        assertEquals(product, productResponse);
        assertEquals(productDetails.get("Name"), productResponse.getName());
    }

    @Then("I should receive a {string}")
    public void verifyResponseContainsMessage(String status) {
        assertNotNull(exception);
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientException = (HttpClientErrorException) exception;
            assertEquals(HttpStatus.NOT_FOUND, httpClientException.getStatusCode());
            assertTrue(httpClientException.getResponseBodyAsString().contains(status));
        }
    }
}