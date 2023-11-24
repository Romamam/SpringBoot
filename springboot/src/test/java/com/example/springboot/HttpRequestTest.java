//package com.example.springboot;
//
//import com.example.springboot.dao.PlayerDAOImpl;
//import com.example.springboot.model.Player;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//public class HttpRequestTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private PlayerDAOImpl playerDAO;
//
//    @Test
//    void greetingShouldReturnDefaultMessage() throws Exception {
//        Player player = new Player();
//        player.setId(1);
//        playerDAO.save(player);
//       String response = (this.restTemplate.getForObject("http://localhost:" + port + "/api/players",
//                String.class));
//
//        int playerIdToCheck = 1;
//
//        assertThat(response).contains("\"id\":" + playerIdToCheck);
//    }
//
//    @Test
//    void testDelete(){
//        Player player = new Player();
//        player.setId(1);
//        playerDAO.save(player);
//        restTemplate.delete("http://localhost:" + port + "/api/player/" + player.getId());
//
//        String response = restTemplate.getForObject("http://localhost:" + port + "/api/player", String.class);
//        assertThat(response).doesNotContain("\"id\":" + player.getId());
//    }
//
//    @Test
//    void testUpdate(){
//        Player player = new Player();
//        player.setName("John");
//        playerDAO.save(player);
//
//        player.setName("UpdatedName");
//
//        restTemplate.put("http://localhost:" + port + "/api/player", player);
//
//        String response = restTemplate.getForObject("http://localhost:" + port + "/api/players", String.class);
//        assertThat(response).contains("\"name\":\"UpdatedName\"");
//    }
//
//}
