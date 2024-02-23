package com.backend.parcial.test;

import com.backend.parcial.dao.impl.OdontologoDaoH2;
import com.backend.parcial.dao.impl.OdontologoDaoMemoria;
import com.backend.parcial.entity.Odontologo;
import com.backend.parcial.service.OdontologoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class OdontologoServiceTest {
    private OdontologoService odontologoService;
    private List<Odontologo> odontologos = new ArrayList<>();

    @Test
    void deberiaListarTodosLosOdontologosEnH2() {
        // arrange
        odontologoService = new OdontologoService(new OdontologoDaoH2());

        // act
        odontologos = odontologoService.listarTodos();

        // Test
        Assertions.assertTrue(odontologos.size() > 0);
    }

    @Test
    void deberiaListarTodosLosOdontologosEnMemoria() {
        // arrange
        odontologoService = new OdontologoService(new OdontologoDaoMemoria());

        //act
        Odontologo odontologo = new Odontologo(12345, "Juan", "Perez");
        odontologoService.registrarOdontologo(odontologo);
        odontologos = odontologoService.listarTodos();

        // Test
        Assertions.assertTrue(odontologos.size() > 0);
    }
}
