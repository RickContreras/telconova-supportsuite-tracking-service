package com.telconova.tracking.config;

import com.telconova.tracking.TestcontainersConfiguration;
import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.repository.AvanceRepository;
import com.telconova.tracking.service.AvanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class RedisCacheIntegrationTest {

    @MockitoSpyBean
    private AvanceRepository avanceRepository;

    @Autowired
    private AvanceService avanceService;

    @Test
    void shouldCacheAvanceById() {
        // Arrange
        Avance avance = new Avance();
        avance.setOrdenId(UUID.randomUUID());
        avance.setTecnicoId(UUID.randomUUID());
        avance.setComentario("Test comentario");
        avance.setTiempoInvertido(30);

        Avance savedAvance = avanceRepository.save(avance);
        UUID id = savedAvance.getId();

        // Reiniciar el contador del spy para mediciones precisas
        reset(avanceRepository);

        // Act - First call should hit the database
        avanceService.findById(id);
        // Second call should hit the cache
        avanceService.findById(id);
        // Third call should also hit the cache
        avanceService.findById(id);

        // Assert - Repository should only be called once
        verify(avanceRepository, times(1)).findById(id);
    }
}
