package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    public void deberiaRegistrarUnOdontologoDeNombrePepe_yRetornarSuId() {

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("12345", "Pepe", "Doctor");

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);

        assertNotNull(odontologoSalidaDto);
        assertNotNull(odontologoSalidaDto.getId());
        assertEquals("Pepe", odontologoSalidaDto.getNombre());

    }

    @Test
    @Order(2)
    public void deberiaEliminarseElOdontologoConId1() {

        assertDoesNotThrow(() -> odontologoService.eliminarOdontologo(1L));

    }

    @Test
    @Order(3)
    public void deberiaDevolverUnaListaVaciaDeOdontologos() {

        List<OdontologoSalidaDto> odontologos = odontologoService.listarOdontologos();

        assertTrue(odontologos.isEmpty());

    }
}