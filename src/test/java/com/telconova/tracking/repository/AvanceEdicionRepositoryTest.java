package com.telconova.tracking.repository;

import com.telconova.tracking.TestcontainersConfiguration;
import com.telconova.tracking.TrackingServiceApplication;
import com.telconova.tracking.entity.AvanceEdicion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@ContextConfiguration(classes = TrackingServiceApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AvanceEdicionRepositoryTest {

    @Autowired
    private AvanceEdicionRepository avanceEdicionRepository;

    @Test
    void shouldFindEdicionesByAvanceId() {
        // Arrange
        UUID avanceId = UUID.randomUUID();

        AvanceEdicion edicion1 = new AvanceEdicion();
        edicion1.setAvanceId(avanceId);
        edicion1.setTiempoAnterior(30);
        edicion1.setTiempoNuevo(45);
        edicion1.setJustificacion("Ajuste por tiempo adicional");

        AvanceEdicion edicion2 = new AvanceEdicion();
        edicion2.setAvanceId(avanceId);
        edicion2.setTiempoAnterior(45);
        edicion2.setTiempoNuevo(60);
        edicion2.setJustificacion("Corrección de registro");

        avanceEdicionRepository.saveAll(List.of(edicion1, edicion2));

        // Act
        List<AvanceEdicion> result =
                avanceEdicionRepository.findByAvanceIdOrderByModificadoEnDesc(avanceId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("justificacion").contains("Ajuste por tiempo adicional",
                "Corrección de registro");
    }
}
