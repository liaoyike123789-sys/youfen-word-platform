package com.youfen.word;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * 测试基类：统一配置 baseURI、token 登录辅助。
 * 运行前请先启动 backend 服务（默认 http://localhost:8080）。
 */
public abstract class BaseApiTest {

    protected static String baseUrl = "http://localhost:8080";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /** 注册一个随机用户名的测试用户并返回 token */
    protected String registerAndLogin(String prefix) {
        String username = prefix + "_" + System.currentTimeMillis();
        Map<String, String> reg = new HashMap<>();
        reg.put("username", username);
        reg.put("password", "123456");
        reg.put("nickname", "tester");
        given().contentType("application/json").body(reg).post("/api/auth/register").then().statusCode(200);

        Map<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", "123456");
        Response resp = given().contentType("application/json").body(login).post("/api/auth/login");
        return resp.jsonPath().getString("data.token");
    }

    protected io.restassured.specification.RequestSpecification auth(String token) {
        return given().header("Authorization", "Bearer " + token).contentType("application/json");
    }
}
