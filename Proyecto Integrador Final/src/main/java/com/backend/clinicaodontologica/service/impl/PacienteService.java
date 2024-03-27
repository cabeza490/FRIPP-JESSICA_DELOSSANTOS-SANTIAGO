package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.PacienteRepository;
import com.backend.clinicaodontologica.service.IPacienteService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements IPacienteService {

    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

    private PacienteRepository pacienteRepository;

    private ModelMapper modelMapper;

    public PacienteService(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {

        LOGGER.info("PacienteEntradaDto: {}", JsonPrinter.toString(paciente));

        Paciente pacienteEntidad = modelMapper.map(paciente, Paciente.class);
        Paciente pacienteEntidadConId = pacienteRepository.save(pacienteEntidad);

        PacienteSalidaDto pacienteSalidaDto = modelMapper.map(pacienteEntidadConId, PacienteSalidaDto.class);

        LOGGER.warn("Paciente registrado con éxito: {}", JsonPrinter.toString((pacienteSalidaDto)));

        return pacienteSalidaDto;
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {

        List<PacienteSalidaDto> pacientesSalidaDto = pacienteRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PacienteSalidaDto.class))
                .toList();

        LOGGER.info("Listado de todos los pacientes: {}", JsonPrinter.toString(pacientesSalidaDto));

        return pacientesSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(Long id) throws ResourceNotFoundException {

        Paciente paciente = pacienteRepository.findById(id).orElse(null);

        PacienteSalidaDto pacienteSalidaDto = null;

        if (paciente != null) {
            pacienteSalidaDto = modelMapper.map(paciente, PacienteSalidaDto.class);
            LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(pacienteSalidaDto));
        } else {
            LOGGER.error("El id no para el paciente se encuentra registrado en la base de datos: {}", id);
            throw new ResourceNotFoundException("El id no para el paciente se encuentra registrado en la base de datos: {}" + id);
        }

        return pacienteSalidaDto;
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException {

        if (buscarPacientePorId(id) != null) {
            pacienteRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el paciente con el id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el paciente con el id: {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el paciente con el id: {}" + id);
        }

    }

    @Override
    public PacienteSalidaDto modificarPaciente(PacienteEntradaDto pacienteEntradaDto, Long id) throws ResourceNotFoundException {

        Paciente pacienteRecibido = modelMapper.map(pacienteEntradaDto, Paciente.class);
        Paciente pacienteAActualizar = pacienteRepository.findById(id).orElse(null);
        PacienteSalidaDto pacienteSalidaDto = null;

        if (pacienteAActualizar != null) {

            pacienteAActualizar.setNombre(pacienteRecibido.getNombre());
            pacienteAActualizar.setApellido(pacienteRecibido.getApellido());
            pacienteAActualizar.setDni(pacienteRecibido.getDni());
            pacienteAActualizar.setFechaIngreso(pacienteRecibido.getFechaIngreso());
            pacienteAActualizar.setDomicilio(pacienteRecibido.getDomicilio());
            pacienteRepository.save(pacienteAActualizar);

            pacienteSalidaDto = modelMapper.map(pacienteAActualizar, PacienteSalidaDto.class);

            LOGGER.warn("Paciente actualizado: {}", JsonPrinter.toString(pacienteSalidaDto));
        } else {
            LOGGER.error("No se pudieron actualizar los datos, el id del paciente no se encuentra registrado");
            throw new ResourceNotFoundException("No se pudieron actualizar los datos, el id del paciente no se encuentra registrado");
        }
        return pacienteSalidaDto;
    }

    private void configureMapping() {

        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilioEntradaDto, Paciente::setDomicilio));

        modelMapper.typeMap(Paciente.class, PacienteSalidaDto.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDto::setDomicilioSalidaDto));
    }
}
