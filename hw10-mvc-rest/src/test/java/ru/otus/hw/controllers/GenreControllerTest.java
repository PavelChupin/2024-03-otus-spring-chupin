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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    private static final ObjectMapper jsonMapper = new JsonMapper();

    private static final List<GenreDto> genres = new ArrayList<>();

    @BeforeAll
    static void init() {
        genres.add(new GenreDto(1L, "Genre1"));
        genres.add(new GenreDto(2L, "Genre2"));
    }

    @Test
    void listTest() throws Exception {
        final String expected = jsonMapper.writeValueAsString(genres);

        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void listInternalServerErrorTest() throws Exception {

        when(genreService.findAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}