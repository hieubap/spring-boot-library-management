//package library.jpa.controller;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.InvalidMediaTypeException;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Objects;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TestWhenServerRunning {
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void exampleTest() {
//        String json = "";
//
//        try {
//            ClientResponse clientResponse = Objects.requireNonNull(WebClient.create()
//                    .get()
//                    .uri("http://localhost:8080/manager/book/head/list")
//                    .header("Authorization", "Token eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbeyJpZCI6MSwibmFtZSI6IkJPT0tfUkVBRCIsImF1dGhvcml0eSI6IkJPT0tfUkVBRCJ9LHsiaWQiOjIsIm5hbWUiOiJCT09LX1dSSVRFIiwiYXV0aG9yaXR5IjoiQk9PS19XUklURSJ9LHsiaWQiOm51bGwsIm5hbWUiOiJST0xFX0FETUlOIiwiYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjA3NzU1MzA2LCJleHAiOjE2MDg5MTU2MDB9.K1PqJqNG9RgjhfKp95DZ1WNPiJ4Sz-t4zyZhqCc6uqIZKOVcMoNDIcsVb7ICRCl7")
//                    .exchange()
//                    .block());
//            json = clientResponse
//                    .bodyToMono(String.class)
//                    .block();
//        } catch (InvalidMediaTypeException e) {
//            System.out.println("error");
//        }
////        WebClient webClient  = (WebClient)WebTestClient.bindToServer().baseUrl("http://localhost:8080")
////                .defaultHeader("Authorization","Token eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbeyJpZCI6MSwibmFtZSI6IkJPT0tfUkVBRCIsImF1dGhvcml0eSI6IkJPT0tfUkVBRCJ9LHsiaWQiOjIsIm5hbWUiOiJCT09LX1dSSVRFIiwiYXV0aG9yaXR5IjoiQk9PS19XUklURSJ9LHsiaWQiOm51bGwsIm5hbWUiOiJST0xFX0FETUlOIiwiYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjA3NzU1MzA2LCJleHAiOjE2MDg5MTU2MDB9.K1PqJqNG9RgjhfKp95DZ1WNPiJ4Sz-t4zyZhqCc6uqIZKOVcMoNDIcsVb7ICRCl7").build();
////
//////        EntityResponse s =
////                webClient.get().uri("/manager/book/head/list")
////                .exchange()
////                        .block();
////                        .expectHeader().contentType(String.valueOf(MediaType.APPLICATION_JSON))
////                .expectBody(String.class).returnResult().getResponseBody();
//
////        EntityResponse<Object> e = new ObjectMapper().convertValue(s,EntityResponse.class);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(json);
//        String prettyJsonString = gson.toJson(je);
//
//        System.out.println("Body Response To Client : \n" + prettyJsonString);
//
//
//    }
//}
