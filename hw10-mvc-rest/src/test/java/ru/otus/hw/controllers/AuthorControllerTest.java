package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    private static final ObjectMapper jsonMapper = new JsonMapper();

    private static final List<AuthorDto> authors = new ArrayList<>();

    @BeforeAll
    static void init() {
        authors.add(new AuthorDto(1L, "Author1"));
        authors.add(new AuthorDto(2L, "Author2"));
    }

    @Test
    void listTest() throws Exception {
        final String expected = jsonMapper.writeValueAsString(authors);

        when(authorService.findAll()).thenReturn(authors);

        mockMvc.perform(get("/api/v1/author"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void listInternalServerErrorTest() throws Exception {

        when(authorService.findAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/v1/author"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}