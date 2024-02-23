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
        odontologoService = new OdontologoService(new OdontologoDaoH2());
        odontologos = odontologoService.listarTodos();

        Assertions.assertTrue(odontologos.size() > 0);
    }

    @Test
    void deberiaListarTodosLosOdontologosEnMemoria() {
        odontologoService = new OdontologoService(new OdontologoDaoMemoria());
        odontologos = odontologoService.listarTodos();

        Assertions.assertTrue(odontologos.size() > 0);
    }
}
