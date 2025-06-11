package com.telconova.tracking.service;

import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.repository.AvanceRepository;
import com.telconova.tracking.service.impl.AvanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvanceServiceImplTest {

    @Mock
    private AvanceRepository avanceRepository;

    @InjectMocks
    private AvanceServiceImpl avanceService;

    private Avance avance;
    private UUID avanceId;
    private Long ordenId;

    @BeforeEach
    void setUp() {
        avanceId = UUID.randomUUID();
        ordenId = 456L;

        avance = new Avance();
        avance.setId(avanceId);
        avance.setOrdenId(ordenId);
        avance.setTecnicoId(123L);
        avance.setComentario("Prueba de servicio");
        avance.setTiempoInvertido(30);
    }

    @Test
    void shouldSaveAvance() {
        when(avanceRepository.save(any(Avance.class))).thenReturn(avance);

        Avance result = avanceService.save(avance);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(avanceId);
        verify(avanceRepository, times(1)).save(avance);
    }

    @Test
    void shouldFindByIdWhenAvanceExists() {
        when(avanceRepository.findById(avanceId)).thenReturn(Optional.of(avance));

        Optional<Avance> result = avanceService.findById(avanceId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(avanceId);
        verify(avanceRepository, times(1)).findById(avanceId);
    }

    @Test
    void shouldFindByOrdenId() {
        List<Avance> avances = Arrays.asList(avance);
        when(avanceRepository.findByOrdenIdOrderByCreadoEnDesc(ordenId)).thenReturn(avances);

        List<Avance> result = avanceService.findByOrdenId(ordenId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrdenId()).isEqualTo(ordenId);
        verify(avanceRepository, times(1)).findByOrdenIdOrderByCreadoEnDesc(ordenId);
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(avanceRepository).deleteById(avanceId);

        avanceService.deleteById(avanceId);

        verify(avanceRepository, times(1)).deleteById(avanceId);
    }
}
