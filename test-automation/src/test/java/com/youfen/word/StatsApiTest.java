package com.youfen.word;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** 统计模块接口测试（对应用例 TC-STATS-001 ~ TC-STATS-012） */
public class StatsApiTest extends BaseApiTest {

    @Test
    void overview_shouldHaveThreeKeys() {
        String token = registerAndLogin("ov");
        auth(token).get("/api/stats/overview")
                .then().body("data.totalWords", notNullValue())
                .body("data.totalReviews", notNullValue())
                .body("data.wrongBookSize", notNullValue());
    }

    @Test
    void today_accuracyIsPercent() {
        String token = registerAndLogin("td");
        auth(token).get("/api/stats/today")
                .then().body("data.learned", notNullValue())
                .body("data.accuracy", notNullValue());
    }

    @Test
    void trend_returnsArray() {
        String token = registerAndLogin("tr");
        auth(token).get("/api/stats/trend").then().body("data", notNullValue());
    }

    @Test
    void mastery_hasMasteredAndLearning() {
        String token = registerAndLogin("ms");
        auth(token).get("/api/stats/mastery")
                .then().body("data.mastered", notNullValue())
                .body("data.learning", notNullValue());
    }

    @Test
    void streak_isNonNegative() {
        String token = registerAndLogin("st");
        auth(token).get("/api/stats/streak")
                .then().body("data", greaterThanOrEqualTo(0));
    }

    @Test
    void accuracy_between0and100() {
        String token = registerAndLogin("ac");
        Double acc = auth(token).get("/api/stats/accuracy").then().extract().path("data");
        org.junit.jupiter.api.Assertions.assertTrue(acc >= 0 && acc <= 100);
    }
}
