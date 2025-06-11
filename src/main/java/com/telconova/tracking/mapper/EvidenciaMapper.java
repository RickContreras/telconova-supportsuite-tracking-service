package com.telconova.tracking.mapper;

import com.telconova.tracking.dto.EvidenciaDto;
import com.telconova.tracking.entity.Evidencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface EvidenciaMapper {

    @Mapping(target = "url", ignore = true)
    EvidenciaDto toDto(Evidencia entity);

    List<EvidenciaDto> toDto(List<Evidencia> entities);

    @Mapping(target = "rutaArchivo", ignore = true)
    Evidencia toEntity(EvidenciaDto dto);
}
