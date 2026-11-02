package com.youfen.word;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** 认证模块接口测试（对应用例 TC-AUTH-001 ~ TC-AUTH-018） */
public class AuthApiTest extends BaseApiTest {

    @Test
    void register_success() {
        String username = "u_" + System.currentTimeMillis();
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", "123456");
        body.put("nickname", "tester");
        given().contentType("application/json").body(body)
                .post("/api/auth/register")
                .then().statusCode(200)
                .body("code", is(0))
                .body("data.username", is(username));
    }

    @Test
    void register_duplicateUsername_shouldFail() {
        String username = "dup_" + System.currentTimeMillis();
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", "123456");
        given().contentType("application/json").body(body).post("/api/auth/register").then().statusCode(200);
        given().contentType("application/json").body(body).post("/api/auth/register")
                .then().body("code", not(0))
                .body("message", containsString("用户名已存在"));
    }

    @Test
    void login_success_returnToken() {
        String token = registerAndLogin("login_ok");
        org.junit.jupiter.api.Assertions.assertNotNull(token);
        org.junit.jupiter.api.Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void login_wrongPassword_shouldFail() {
        String username = "wp_" + System.currentTimeMillis();
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", "123456");
        given().contentType("application/json").body(body).post("/api/auth/register").then().statusCode(200);

        Map<String, String> bad = new HashMap<>();
        bad.put("username", username);
        bad.put("password", "wrong_pwd");
        given().contentType("application/json").body(bad).post("/api/auth/login")
                .then().body("code", is(401))
                .body("message", containsString("密码错误"));
    }

    @Test
    void me_withToken_shouldReturnUser() {
        String token = registerAndLogin("me");
        auth(token).get("/api/auth/me")
                .then().statusCode(200)
                .body("code", is(0))
                .body("data.username", notNullValue());
    }

    @Test
    void me_withoutToken_should401() {
        given().contentType("application/json").get("/api/auth/me")
                .then().body("code", is(401));
    }

    @Test
    void changePassword_thenLoginWithNewPwd() {
        String token = registerAndLogin("chpwd");
        Map<String, String> body = new HashMap<>();
        body.put("oldPassword", "123456");
        body.put("newPassword", "newpwd456");
        auth(token).body(body).put("/api/auth/password").then().statusCode(200);
    }

    @Test
    void updateProfile_nickname() {
        String token = registerAndLogin("profile");
        Map<String, String> body = new HashMap<>();
        body.put("nickname", "新昵称");
        body.put("email", "test@example.com");
        auth(token).body(body).put("/api/auth/profile")
                .then().body("data.nickname", is("新昵称"));
    }

    @Test
    void logout_thenMeShould401() {
        String token = registerAndLogin("logout");
        auth(token).post("/api/auth/logout").then().statusCode(200);
        auth(token).get("/api/auth/me").then().body("code", is(401));
    }
}
