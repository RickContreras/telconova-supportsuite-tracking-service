package com.telconova.tracking.repository;

import com.telconova.tracking.TestcontainersConfiguration;
import com.telconova.tracking.entity.Evidencia;
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
class EvidenciaRepositoryTest {

    @Autowired
    private EvidenciaRepository evidenciaRepository;

    @Test
    void shouldFindEvidenciasByAvanceId() {
        // Arrange
        UUID avanceId = UUID.randomUUID();

        Evidencia evidencia1 = new Evidencia();
        evidencia1.setAvanceId(avanceId);
        evidencia1.setNombreArchivo("test1.jpg");
        evidencia1.setTipoContenido("image/jpeg");
        evidencia1.setRutaArchivo("/storage/test1.jpg");
        evidencia1.setTamanoBytes(1024L);

        Evidencia evidencia2 = new Evidencia();
        evidencia2.setAvanceId(avanceId);
        evidencia2.setNombreArchivo("test2.pdf");
        evidencia2.setTipoContenido("application/pdf");
        evidencia2.setRutaArchivo("/storage/test2.pdf");
        evidencia2.setTamanoBytes(2048L);

        evidenciaRepository.saveAll(List.of(evidencia1, evidencia2));

        // Act
        List<Evidencia> result = evidenciaRepository.findByAvanceId(avanceId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("nombreArchivo").contains("test1.jpg", "test2.pdf");
    }
}
