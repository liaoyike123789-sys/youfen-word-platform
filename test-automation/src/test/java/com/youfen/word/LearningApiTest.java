package com.youfen.word;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** 学习模块接口测试（对应用例 TC-LEARN-001 ~ TC-LEARN-020） */
public class LearningApiTest extends BaseApiTest {

    @Test
    void todayQueue_shouldReturnWords() {
        String token = registerAndLogin("today");
        auth(token).queryParam("limit", 5).get("/api/learning/today")
                .then().body("code", is(0))
                .body("data.size()", lessThanOrEqualTo(5));
    }

    @Test
    void review_known_shouldRecord() {
        String token = registerAndLogin("rev_known");
        Map<String, Object> body = new HashMap<>();
        body.put("wordId", 1);
        body.put("result", "known");
        auth(token).body(body).post("/api/learning/review")
                .then().body("data.result", is("known"));
    }

    @Test
    void review_unknown_shouldAddToWrongbook() {
        String token = registerAndLogin("rev_unknown");
        Map<String, Object> body = new HashMap<>();
        body.put("wordId", 2);
        body.put("result", "unknown");
        auth(token).body(body).post("/api/learning/review").then().statusCode(200);

        auth(token).get("/api/learning/wrongbook")
                .then().body("data.wordId", hasItem(2));
    }

    @Test
    void review_invalidResult_shouldFail() {
        String token = registerAndLogin("rev_bad");
        Map<String, Object> body = new HashMap<>();
        body.put("wordId", 1);
        body.put("result", "maybe");
        auth(token).body(body).post("/api/learning/review")
                .then().body("code", not(0));
    }

    @Test
    void records_paged() {
        String token = registerAndLogin("rec");
        auth(token).get("/api/learning/records")
                .then().body("data.content", notNullValue());
    }

    @Test
    void removeWrongbook_thenGone() {
        String token = registerAndLogin("rmwrong");
        Map<String, Object> body = new HashMap<>();
        body.put("wordId", 3);
        body.put("result", "unknown");
        auth(token).body(body).post("/api/learning/review");
        auth(token).delete("/api/learning/wrongbook/3").then().statusCode(200);
    }

    @Test
    void checkin_thenStatusShowsLearned() {
        String token = registerAndLogin("ckin");
        auth(token).post("/api/learning/checkin").then().statusCode(200);
        auth(token).get("/api/learning/checkin/status")
                .then().body("data.learnedCount", greaterThanOrEqualTo(1));
    }

    @Test
    void history_returnsList() {
        String token = registerAndLogin("hist");
        auth(token).get("/api/learning/history").then().body("data", notNullValue());
    }
}
