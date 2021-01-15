package com.hlushkov.web.controller;

import com.hlushkov.service.config.RootApplicationContext;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(value = {TestConfiguration.class, RootApplicationContext.class, com.hlushkov.web.config.WebApplicationContext.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void dbSetUp() {
        flyway.migrate();
    }

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DisplayName("Returns list of all movies in json format")
    void getAllMovies() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/movie"))
                .andDo(print())
                //.andExpect(jsonPath("$.id").value("1"))
                .andExpect(status().isOk()).andReturn().getResponse();
        //then
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("application/json", response.getContentType());
        assertNotNull(response.getContentAsString());
        assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
        assertTrue(response.getContentAsString().contains("Dances with Wolves"));
        //FIXME Russian film names is not correct
    }
}