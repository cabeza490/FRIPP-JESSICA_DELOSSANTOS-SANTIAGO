package com.backend.clinicaodontologica.service.impl;


import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.OdontologoRepository;
import com.backend.clinicaodontologica.service.IOdontologoService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);

    //    @Autowired
    private final OdontologoRepository odontologoRepository;

    //    @Autowired
    private final ModelMapper modelMapper;

    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {

        LOGGER.info("OdontologoEntradaDto: {}", JsonPrinter.toString(odontologo));

        Odontologo odontologoEntidad = modelMapper.map(odontologo, Odontologo.class);
        odontologoRepository.save(odontologoEntidad);

        OdontologoSalidaDto odontologoSalidaDto = modelMapper.map(odontologoEntidad, OdontologoSalidaDto.class);

        LOGGER.warn("Odontologo registrado con éxito: {}", JsonPrinter.toString(odontologoSalidaDto));

        return odontologoSalidaDto;
    }

    @Override
    public List<OdontologoSalidaDto> listarOdontologos() {

        List<OdontologoSalidaDto> odontologosSalidaDto = odontologoRepository.findAll()
                .stream()
                .map(o -> modelMapper.map(o, OdontologoSalidaDto.class))
                .toList();

        LOGGER.info("Listado de todos los odontologos: {}", JsonPrinter.toString(odontologosSalidaDto));

        return odontologosSalidaDto;
    }

    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(Long id) throws ResourceNotFoundException {

        Odontologo odontologo = odontologoRepository.findById(id).orElse(null);
        OdontologoSalidaDto odontologoSalidaDto = null;

        if (odontologo != null) {
            odontologoSalidaDto = modelMapper.map(odontologo, OdontologoSalidaDto.class);
            LOGGER.info("Odontologo encontrado: {}", JsonPrinter.toString(odontologoSalidaDto));
        } else {
            LOGGER.error("El id para el odontologo no se encuentra registrado en la base de datos: {}", id);
            throw new ResourceNotFoundException("El id para el odontologo no se encuentra registrado en la base de datos: " + id);
        }

        return odontologoSalidaDto;
    }

    @Override
    public void eliminarOdontologo(Long id) throws ResourceNotFoundException {

        if (buscarOdontologoPorId(id) != null) {
            odontologoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el odontologo con el id: {}", id);
        } else  {
            LOGGER.error("No se ha encontrado el odontologo con el id: {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el odontologo con el id: " + id);
        }

    }

    @Override
    public OdontologoSalidaDto modificarOdontologo(OdontologoEntradaDto odontologoEntradaDto, Long id) throws ResourceNotFoundException {

        Odontologo odontologoRecibido = modelMapper.map(odontologoEntradaDto, Odontologo.class);
        Odontologo odontologoAActualizar = odontologoRepository.findById(id).orElse(null);
        OdontologoSalidaDto odontologoSalidaDto = null;

        if (odontologoAActualizar != null) {

            odontologoAActualizar.setNombre(odontologoRecibido.getNombre());
            odontologoAActualizar.setApellido(odontologoRecibido.getApellido());
            odontologoAActualizar.setMatricula(odontologoRecibido.getMatricula());
            odontologoRepository.save(odontologoAActualizar);

            odontologoSalidaDto = modelMapper.map(odontologoAActualizar, OdontologoSalidaDto.class);

            LOGGER.warn("Odontologo actualizado: {}", JsonPrinter.toString(odontologoSalidaDto));
        } else {
            LOGGER.error("No se pudieron actualizar los datos, el id del odontólogo no se encuentra registrado");
            throw new ResourceNotFoundException("No se pudieron actualizar los datos, el id del odontólogo no se encuentra registrado");
        }

        return odontologoSalidaDto;
    }

}
