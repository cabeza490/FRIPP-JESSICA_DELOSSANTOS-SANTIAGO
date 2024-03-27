package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.TurnoRepository;
import com.backend.clinicaodontologica.service.ITurnoService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);

    private final TurnoRepository turnoRepository;

    private final ModelMapper modelMapper;

    private final PacienteService pacienteService;

    private final OdontologoService odontologoService;

    public TurnoService(TurnoRepository turnoRepository,
                        ModelMapper modelMapper,
                        PacienteService pacienteService,
                        OdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        configureMapping();
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws ResourceNotFoundException, BadRequestException {

        TurnoSalidaDto turnoSalidaDto = null;
        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto
                .getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto
                .getOdontologoId());

        String faltaPacienteEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String faltaOdontologoEnBdd = "El odontologo no se encuentra en nuestra base de datos";
        String faltanAmbos = "El paciente y el odontologo no se encuentran en nuestra base de datos";

        if(paciente == null || odontologo == null) {
            if(paciente == null && odontologo == null) {
                LOGGER.error(faltanAmbos);
                throw new BadRequestException(faltanAmbos);
            } else if (paciente == null) {
                LOGGER.error(faltaPacienteEnBdd);
                throw new BadRequestException(faltaPacienteEnBdd);
            } else if (odontologo == null) {
                LOGGER.error(faltaOdontologoEnBdd);
                throw new BadRequestException(faltaOdontologoEnBdd);
            }

        } else {
            Turno turno = turnoRepository.save(modelMapper
                    .map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = entidadASalidaDto(turno, paciente, odontologo);

            LOGGER.warn("Nuevo turno registrado con exito: {}", JsonPrinter.toString(turnoSalidaDto));
        }

        return turnoSalidaDto;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {

        List<TurnoSalidaDto> turnosSalidaDto = turnoRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TurnoSalidaDto.class))
                .toList();

        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnosSalidaDto));

        return turnosSalidaDto;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) throws ResourceNotFoundException {

        Turno turno = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if(turno != null) {
            turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoSalidaDto));
        } else {
            LOGGER.error("El id para el turno no se encuentra registrado en la base de datos: {}", id);
            throw new ResourceNotFoundException("El id para el turno no se encuentra registrado en la base de datos: {}" + id);
        }

        return turnoSalidaDto;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {

        if (buscarTurnoPorId(id) != null) {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con el id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el turno con el id: {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el turno con el id: {}" + id);
        }

    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws ResourceNotFoundException {

        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if (turnoAActualizar != null) {

            turnoAActualizar.setOdontologo(turnoRecibido.getOdontologo());
            turnoAActualizar.setPaciente(turnoRecibido.getPaciente());
            turnoAActualizar.setFechaYHora(turnoRecibido.getFechaYHora());
            turnoRepository.save(turnoAActualizar);

            turnoSalidaDto = modelMapper.map(turnoAActualizar, TurnoSalidaDto.class);

            LOGGER.warn("Turno actualizado: {}", JsonPrinter.toString(turnoSalidaDto));
        } else  {
            LOGGER.error("No se pudieron actualizar los datos, el id del turno no se encuentra registrado");
            throw new ResourceNotFoundException("No se pudieron actualizar los datos, el id del turno no se encuentra registrado");
        }

        return turnoSalidaDto;
    }

    private TurnoSalidaDto entidadASalidaDto(Turno turno,
                                             PacienteSalidaDto pacienteSalidaDto,
                                             OdontologoSalidaDto odontologoSalidaDto) {
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteSalidaDto(pacienteSalidaDto);
        turnoSalidaDto.setOdontologoSalidaDto(odontologoSalidaDto);

        return turnoSalidaDto;
    }


    private void configureMapping() {

//        modelMapper.typeMap(TurnoEntradaDto.class, Turno.class)
//                .addMappings(mapper -> mapper.map(TurnoEntradaDto::getPacienteId, Turno::setPaciente))
//                .addMappings(mapper -> mapper.map(TurnoEntradaDto::getOdontologoId, Turno::setOdontologo));

        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoSalidaDto));
    }
}
