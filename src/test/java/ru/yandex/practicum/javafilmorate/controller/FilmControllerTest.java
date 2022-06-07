// Старые тесты. Не актуальны. Все кейсы покрывает постман

package ru.yandex.practicum.javafilmorate.controller;
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
public class FilmControllerTest {
//    private HttpClient client;
//    private HttpRequest.Builder requestBuilder;
//    final int PORT = 8080;
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
//                "        \"name\": \"name3\",\n" +
//                "        \"description\": \"description\",\n" +
//                "        \"releaseDate\": \"1997-04-15\",\n" +
//                "        \"duration\": \"40\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/films"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationNegativeDuration() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"name\": \"name3\",\n" +
//                "        \"description\": \"description\",\n" +
//                "        \"releaseDate\": \"1997-04-15\",\n" +
//                "        \"duration\": \"-40\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/films"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationDate() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"name\": \"name3\",\n" +
//                "        \"description\": \"description\",\n" +
//                "        \"releaseDate\": \"1895-12-27\",\n" +
//                "        \"duration\": \"40\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/films"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationNullNonNullField() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"description\": \"description\",\n" +
//                "        \"releaseDate\": \"1997-04-15\",\n" +
//                "        \"duration\": \"120\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/films"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertNotEquals(200, response.statusCode());
//    }
//
//    @Test
//    void validationBigDescription() throws IOException, InterruptedException {
//        String json = "{\n" +
//                "        \"name\": \"name3\",\n" +
//                "        \"description\": \"descriptions3descriptions3descriptions3descriptions3descriptions3descriptions" +
//                "3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3descriptions3\",\n" +
//                "        \"releaseDate\": \"1997-04-15\",\n" +
//                "        \"duration\": \"120\"\n" +
//                "    }";
//        HttpRequest request = returnPostRequest(URI.create("http://localhost:"+PORT+"/films"), json);
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(500, response.statusCode());
//
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
}
