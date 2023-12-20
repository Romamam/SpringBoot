package com.example.springboot;

import com.example.springboot.controller.PlayerController;
import com.example.springboot.dao.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class PlayerControllerTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void changeStatusPlayerActiveOrNotActive_Success() {
        UUID playerId = UUID.randomUUID();

        ResponseEntity<?> responseEntity = playerController.changeStatusPlayerActiveOrNotActive(playerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(playerRepository).toggleActiveStatus(playerId);
    }

    @Test
    void changeStatusPlayerActiveOrNotActive_PlayerNotFound() {
        UUID playerId = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Player with ID " + playerId + " not found")).when(playerRepository).toggleActiveStatus(playerId);

        ResponseEntity<?> responseEntity = playerController.changeStatusPlayerActiveOrNotActive(playerId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Player with ID " + playerId + " not found", responseEntity.getBody());
        verify(playerRepository).toggleActiveStatus(playerId);
    }

    @Test
    void changeStatusPlayerActiveOrNotActive_ExceptionThrown() {
        UUID playerId = UUID.randomUUID();
        doThrow(RuntimeException.class).when(playerRepository).toggleActiveStatus(playerId);

        assertThrows(RuntimeException.class, () -> playerController.changeStatusPlayerActiveOrNotActive(playerId));
        verify(playerRepository).toggleActiveStatus(playerId);
    }
}
