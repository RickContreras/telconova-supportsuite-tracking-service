package com.telconova.tracking.mapper;

import com.telconova.tracking.dto.AvanceDto;
import com.telconova.tracking.entity.Avance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvanceMapper {

    public AvanceDto toDto(Avance avance) {
        AvanceDto dto = new AvanceDto();
        dto.setId(avance.getId());
        dto.setOrdenId(avance.getOrdenId());
        dto.setTecnicoId(avance.getTecnicoId());
        dto.setComentario(avance.getComentario());
        dto.setTiempoInvertido(avance.getTiempoInvertido());
        dto.setCreadoEn(avance.getCreadoEn());
        dto.setModificadoEn(avance.getModificadoEn());
        return dto;
    }

    public List<AvanceDto> toDto(List<Avance> avances) {
        return avances.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Avance toEntity(AvanceDto dto) {
        Avance avance = new Avance();
        avance.setId(dto.getId());
        avance.setOrdenId(dto.getOrdenId());
        avance.setTecnicoId(dto.getTecnicoId());
        avance.setComentario(dto.getComentario());
        avance.setTiempoInvertido(dto.getTiempoInvertido());
        return avance;
    }
}
