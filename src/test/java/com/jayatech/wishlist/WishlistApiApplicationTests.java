package com.jayatech.wishlist;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
        glue = "com.jayatech.wishlist",
        plugin = {"pretty", "html:target/cucumber-reports"}
)
class WishlistApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
