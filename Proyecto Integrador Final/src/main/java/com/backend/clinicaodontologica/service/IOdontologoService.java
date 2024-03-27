package com.backend.clinicaodontologica.service;

import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService {
    OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo);

    List<OdontologoSalidaDto> listarOdontologos();

    OdontologoSalidaDto buscarOdontologoPorId(Long id) throws ResourceNotFoundException;

    void eliminarOdontologo(Long id) throws ResourceNotFoundException;

    OdontologoSalidaDto modificarOdontologo(OdontologoEntradaDto odontologoEntradaDto, Long id) throws ResourceNotFoundException;
}
