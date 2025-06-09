package com.telconova.tracking.repository;

import com.telconova.tracking.TestcontainersConfiguration;
import com.telconova.tracking.entity.Avance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AvanceRepositoryTest {

    @Autowired
    private AvanceRepository avanceRepository;

    @Test
    void shouldFindAvancesByOrdenId() {
        // Arrange
        UUID ordenId = UUID.randomUUID();
        Avance avance1 = new Avance();
        avance1.setOrdenId(ordenId);
        avance1.setTecnicoId(UUID.randomUUID());
        avance1.setComentario("Test comentario 1");
        avance1.setTiempoInvertido(30);

        Avance avance2 = new Avance();
        avance2.setOrdenId(ordenId);
        avance2.setTecnicoId(UUID.randomUUID());
        avance2.setComentario("Test comentario 2");
        avance2.setTiempoInvertido(45);

        avanceRepository.saveAll(List.of(avance1, avance2));

        // Act
        List<Avance> result = avanceRepository.findByOrdenIdOrderByCreadoEnDesc(ordenId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("comentario").contains("Test comentario 1",
                "Test comentario 2");
    }
}
