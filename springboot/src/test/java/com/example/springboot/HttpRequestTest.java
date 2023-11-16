package com.example.springboot;

import com.example.springboot.dao.PlayerDAOImpl;
import com.example.springboot.model.Player;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlayerDAOImpl playerDAO;

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        playerDAO.save(new Player());
       String response = (this.restTemplate.getForObject("http://localhost:" + port + "/api/players",
                String.class));

        int playerIdToCheck = 1;

        assertThat(response).contains("\"id\":" + playerIdToCheck);
    }
}
