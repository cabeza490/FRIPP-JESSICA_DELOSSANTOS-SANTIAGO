package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.DomicilioEntradaDto;
import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaodontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private  OdontologoService odontologoService;

    @Test
    @Order(1)
    void deberiaRegistrarseUnTurnoConPacienteJuan_yOdontologoPepe_yRetornarSuId() throws BadRequestException, ResourceNotFoundException {

        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Juan", "Perez", 123456,
                LocalDate.of(2024, 3, 22),
                new DomicilioEntradaDto("Calle", 1234, "Localidad", "Provincia"));
        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("12345", "Pepe", "Doctor");

        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(pacienteSalidaDto.getId(), odontologoSalidaDto.getId(), LocalDateTime.now());
        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        assertNotNull(turnoSalidaDto);
        assertNotNull(turnoSalidaDto.getId());

    }

    @Test
    @Order(2)
    void deberiaEliminarseElTurnoConId1() {

        assertDoesNotThrow(() -> turnoService.eliminarTurno(1L));

    }

    @Test
    @Order(3)
    void deberiaDevolverUnaListaVaciaDeTurnos() {

        List<TurnoSalidaDto> turnos = turnoService.listarTurnos();

        assertTrue(turnos.isEmpty());

    }

}