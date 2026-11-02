package com.youfen.word;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** 冒烟测试：部署后第一件事跑这个，验证核心链路通不通 */
public class SmokeTest extends BaseApiTest {

    @Test
    void health_up() {
        given().get("/api/health").then().statusCode(200).body("data.status", is("UP"));
    }

    @Test
    void happyPath_register_login_review_stats() {
        String token = registerAndLogin("smoke");
        auth(token).get("/api/auth/me").then().body("code", is(0));
        given().get("/api/words").then().body("data.totalElements", greaterThan(0));
    }
}
