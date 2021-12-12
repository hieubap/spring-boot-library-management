//package library.jpa.controller;
//
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//@WebMvcTest(UserController.class)
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class UserRoleControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
////    @MockBean
//////    @Autowired
////    private HeadBookService headBookService;
////
////    @MockBean
////    private UserDetailServiceAlter userDetailServiceAlter;
////
////    @MockBean
////    private CardService cardService;
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void testGetAllBook() throws Exception {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/manager/book/head/list"))
//                .andReturn();
//        Assert.assertEquals(200, result.getResponse().getStatus());
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//}