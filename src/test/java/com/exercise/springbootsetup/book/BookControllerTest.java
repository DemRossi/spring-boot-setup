package com.exercise.springbootsetup.book;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    public void saveBooks() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/import-books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Books are saved in DB!!!"));
    }

    @Test
    @Order(2)
    void test_get_books() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(394))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @Order(3)
    void test_get_books_sort_by_title_desc() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book?sort=desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(394))
                .andExpect(jsonPath("$[0].id").value(207))
                .andExpect(jsonPath("$[0].title").value("wxPython in Action"));
    }

    @Test
    @Order(4)
    void test_get_books_sort_by_title_asc() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book?sort=asc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(394))
                .andExpect(jsonPath("$[0].id").value(62))
                .andExpect(jsonPath("$[0].title").value(".NET Multithreading"));
    }

    @Test
    @Order(5)
    void test_get_books_after_date() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book?publishedAfter=2014-06-03").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(348))
                .andExpect(jsonPath("$[0].title").value("Ember.js in Action"))
                .andExpect(jsonPath("$[0].publishedDate").value("2014-06-10T09:00:00.000+0200"));
    }

    @Test
    @Order(6)
    void test_get_books_after_date_sort_by_title_desc() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book?publishedAfter=2014-06-03&sort=desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(363))
                .andExpect(jsonPath("$[0].title").value("The Well-Grounded Rubyist, Second Edition"))
                .andExpect(jsonPath("$[0].publishedDate").value("2014-06-24T09:00:00.000+0200"))
                .andExpect(jsonPath("$[1].id").value(348))
                .andExpect(jsonPath("$[1].title").value("Ember.js in Action"))
                .andExpect(jsonPath("$[1].publishedDate").value("2014-06-10T09:00:00.000+0200"));
    }

    @Test
    @Order(7)
    void test_get_books_after_date_sort_by_title_asc() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book?publishedAfter=2014-06-03&sort=asc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(348))
                .andExpect(jsonPath("$[0].title").value("Ember.js in Action"))
                .andExpect(jsonPath("$[0].publishedDate").value("2014-06-10T09:00:00.000+0200"))
                .andExpect(jsonPath("$[1].id").value(363))
                .andExpect(jsonPath("$[1].title").value("The Well-Grounded Rubyist, Second Edition"))
                .andExpect(jsonPath("$[1].publishedDate").value("2014-06-24T09:00:00.000+0200"));
    }

    @Test
    @Order(8)
    void test_get_book_by_isbn() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/book/1932394613").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(51))
                .andExpect(jsonPath("$.title").value("Ajax in Action"));
    }
}