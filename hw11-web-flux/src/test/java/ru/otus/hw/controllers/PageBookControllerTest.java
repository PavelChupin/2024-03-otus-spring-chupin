//package ru.otus.hw.controllers;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//@WebMvcTest(PageBookController.class)
//class PageBookControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @DisplayName("Должен вернуть страницу списка книг")
//    @Test
//    void listBooksPageTest() throws Exception {
//        mockMvc.perform(get("/list"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("list"));
//    }
//
//    @DisplayName("Должен вернуть страницу добавления книги")
//    @Test
//    void createBookPageTest() throws Exception {
//        mockMvc.perform(get("/create/book"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("create"));
//    }
//
//    @DisplayName("Должен вернуть страницу редактирования книги")
//    @Test
//    void editPageTest() throws Exception {
//        mockMvc.perform(get(String.format("/edit/book/%s", "1L")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("edit"));
//    }
//
//    @DisplayName("Должен вернуть страницу удаления книги")
//    @Test
//    void deletePageTest() throws Exception {
//        mockMvc.perform(get(String.format("/delete/book/%s", "1L")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("delete"));
//    }
//}