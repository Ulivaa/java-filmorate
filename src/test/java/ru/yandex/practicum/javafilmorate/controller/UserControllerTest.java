// Старые тесты. Не актуальны. Все кейсы покрывает постман

//package ru.yandex.practicum.javafilmorate.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//
//
//public class UserControllerTest {
//    private HttpClient client;
//    private HttpRequest.Builder requestBuilder;
//    final int PORT = 8080;
//
//
//    @BeforeEach
//    void beforeEach() {
//        client = HttpClient.newHttpClient();
//        requestBuilder = HttpRequest.newBuilder();
//    }
//
//    @Test
//    void validation200() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"login\": \"login\",\n" +
//                "        \"email\": \"email@mail.ru\",\n" +
//                "        \"name\": \"name\",\n" +
//                "        \"birthday\": \"1996-04-16\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/users"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationDate() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"login\": \"login\",\n" +
//                "        \"email\": \"email@mail.ru\",\n" +
//                "        \"name\": \"name\",\n" +
//                "        \"birthday\": \"2023-04-16\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/users"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationNullNonNullFieldLogin() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"email\": \"email@mail.ru\",\n" +
//                "        \"name\": \"name\",\n" +
//                "        \"birthday\": \"1996-04-16\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/users"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationNullNonNullFieldEmail() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"login\": \"login\",\n" +
//                "        \"email\": \"emailail.ru\",\n" +
//                "        \"name\": \"name\",\n" +
//                "        \"birthday\": \"1996-04-16\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/users"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationEmailNotRight() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"login\": \"login\",\n" +
//                "        \"name\": \"name\",\n" +
//                "        \"birthday\": \"1996-04-16\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/users"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    HttpRequest returnPostRequest(URI uri, String json) {
//        return requestBuilder
//                .POST(HttpRequest.BodyPublishers.ofString(json))
//                .uri(uri)
//                .version(HttpClient.Version.HTTP_1_1)
//                .header("Accept", "text/html").header("Content-Type", "application/json")
//                .build();
//    }
//}
//
