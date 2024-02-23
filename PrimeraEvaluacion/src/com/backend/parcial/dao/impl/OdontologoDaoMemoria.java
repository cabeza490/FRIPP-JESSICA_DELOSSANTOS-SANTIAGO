package com.backend.parcial.dao.impl;

import com.backend.parcial.dao.IDao;
import com.backend.parcial.entity.Odontologo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoMemoria implements IDao<Odontologo> {

    private final Logger LOGGER = Logger.getLogger(OdontologoDaoMemoria.class);
    private List<Odontologo> odontologoRepository = new ArrayList<>();

    public OdontologoDaoMemoria() {
        this.odontologoRepository = odontologoRepository;
    }


    @Override
    public Odontologo registrar(Odontologo odontologo) {

        Odontologo odontologoGuardado = new Odontologo(odontologo.getId(), odontologo.getNroMatricula(), odontologo.getNombre(), odontologo.getApellido());

        odontologoRepository.add(odontologoGuardado);

        LOGGER.info("Odontólogo registrado: " + odontologoGuardado);

        return odontologo;
    }

    @Override
    public List<Odontologo> listarTodos() {

        LOGGER.info("Listado de odontólogos: " + odontologoRepository);
        return odontologoRepository;
    }
}
