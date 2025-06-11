package com.telconova.tracking.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.telconova.tracking.mapper.EvidenciaMapper;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public EvidenciaMapper evidenciaMapper() {
        return Mockito.mock(EvidenciaMapper.class);
    }
}
