package com.youfen.word;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** 单词模块接口测试（对应用例 TC-WORD-001 ~ TC-WORD-030） */
public class WordApiTest extends BaseApiTest {

    @Test
    void list_defaultPage_shouldReturnPagedResult() {
        given().get("/api/words")
                .then().statusCode(200)
                .body("code", is(0))
                .body("data.content", notNullValue())
                .body("data.totalElements", greaterThan(0));
    }

    @Test
    void list_withCategoryId_filter() {
        given().queryParam("categoryId", 1).get("/api/words")
                .then().body("data.content.every.categoryId", everyItem(is(1)));
    }

    @Test
    void detail_existingWord_shouldReturn() {
        given().get("/api/words/1")
                .then().body("data.id", is(1))
                .body("data.spelling", notNullValue())
                .body("data.meaning", notNullValue());
    }

    @Test
    void detail_notExist_should404() {
        given().get("/api/words/999999")
                .then().body("code", is(404));
    }

    @Test
    void search_byKeyword_hit() {
        given().queryParam("keyword", "abandon").get("/api/words/search")
                .then().body("data.totalElements", greaterThanOrEqualTo(1))
                .body("data.content[0].spelling", containsString("abandon"));
    }

    @Test
    void search_emptyKeyword_shouldFail() {
        given().queryParam("keyword", "").get("/api/words/search")
                .then().body("code", not(0));
    }

    @Test
    void byLetter_onlyStartWithGivenLetter() {
        given().pathParam("letter", "a").get("/api/words/letter/{letter}")
                .then().body("data.content.spelling", everyItem(startsWithIgnoringCase("a")));
    }

    @Test
    void byLetter_multiChar_shouldFail() {
        given().pathParam("letter", "ab").get("/api/words/letter/{letter}")
                .then().body("code", not(0));
    }

    @Test
    void categories_shouldReturn4Initial() {
        given().get("/api/categories")
                .then().body("data.size()", greaterThanOrEqualTo(4));
    }

    @Test
    void create_word_then_detail_then_delete() {
        Map<String, Object> body = new HashMap<>();
        body.put("spelling", "testword_" + System.currentTimeMillis());
        body.put("phonetic", "/test/");
        body.put("meaning", "测试单词");
        body.put("categoryId", 1);
        body.put("difficulty", 1);
        Integer id = given().contentType("application/json").body(body).post("/api/words")
                .then().statusCode(200).extract().path("data.id");

        given().get("/api/words/" + id).then().body("data.id", is(id));

        given().delete("/api/words/" + id).then().statusCode(200);
        given().get("/api/words/" + id).then().body("code", is(404));
    }

    @Test
    void update_word_partialField() {
        Map<String, Object> body = new HashMap<>();
        body.put("meaning", "更新后的释义");
        given().contentType("application/json").body(body).put("/api/words/1")
                .then().body("data.meaning", is("更新后的释义"));
    }

    @Test
    void category_crud() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "测试分类");
        body.put("description", "自动化测试创建");
        Integer id = given().contentType("application/json").body(body).post("/api/categories")
                .then().extract().path("data.id");
        given().delete("/api/categories/" + id).then().statusCode(200);
    }
}
