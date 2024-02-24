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

        // preparar el servicio
        odontologoService = new OdontologoService(new OdontologoDaoH2());

        // obtener el listado de los odontólogos
        odontologos = odontologoService.listarTodos();

        // Test
        Assertions.assertTrue(odontologos.size() > 0);
    }

    @Test
    void deberiaListarTodosLosOdontologosEnMemoria() {

        // preparar el servicio
        odontologoService = new OdontologoService(new OdontologoDaoMemoria());

        // registrar un par de odontólogos y obtener el listado
        Odontologo odontologo1 = new Odontologo(1,12345, "Juan", "Perez");
        odontologoService.registrarOdontologo(odontologo1);
        Odontologo odontologo2 = new Odontologo(2,54321, "Maria", "Gomez");
        odontologoService.registrarOdontologo(odontologo2);
        odontologos = odontologoService.listarTodos();

        // Test
        Assertions.assertTrue(odontologos.size() > 0);
    }
}
