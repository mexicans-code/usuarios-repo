package com.idgs12.idgs12.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idgs12.idgs12.dto.DivisionToCreateDto;
import com.idgs12.idgs12.dto.DivisionToUpdateDto;
import com.idgs12.idgs12.dto.DivisionToViewListDto;
import com.idgs12.idgs12.entity.DivisionEntity;
import com.idgs12.idgs12.entity.ProgramaEducativo;
import com.idgs12.idgs12.repository.DivisionRepository;

@Service
public class DivisionService {
    @Autowired
    private DivisionRepository divisionRepository;

    // Listar todas las divisiones
    public List<DivisionToViewListDto> findAll() {
        List<DivisionEntity> divisiones = divisionRepository.findAll();
        List<DivisionToViewListDto> resultado = new ArrayList<>();
        for (DivisionEntity division : divisiones) {
            DivisionToViewListDto dto = new DivisionToViewListDto();
            dto.setDivisionId(division.getId());
            dto.setNombre(division.getNombre());
            if (division.getProgramaEducativo() != null) {
                List<String> programas = new ArrayList<>();
                for (ProgramaEducativo prog : division.getProgramaEducativo()) {
                    programas.add(prog.getPrograma());
                }
                dto.setProgramasEducativos(programas);
            } else {
                dto.setProgramasEducativos(new ArrayList<>());
            }
            resultado.add(dto);
        }
        return resultado;
    }

    // Buscar una división por ID
    public DivisionToViewListDto findById(Long id) {
        DivisionEntity division = divisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " + id));

        DivisionToViewListDto dto = new DivisionToViewListDto();
        dto.setDivisionId(division.getId());
        dto.setNombre(division.getNombre());

        if (division.getProgramaEducativo() != null) {
            List<String> programas = new ArrayList<>();
            for (ProgramaEducativo prog : division.getProgramaEducativo()) {
                programas.add(prog.getPrograma());
            }
            dto.setProgramasEducativos(programas);
        } else {
            dto.setProgramasEducativos(new ArrayList<>());
        }

        return dto;
    }

    // Crear una nueva división
    public DivisionToViewListDto create(DivisionToCreateDto dto) {
        DivisionEntity entity = new DivisionEntity();
        entity.setNombre(dto.getNombre());
        entity.setImage(dto.getImage());
        entity.setActivo(dto.getActivo());

        DivisionEntity saved = divisionRepository.save(entity);

        DivisionToViewListDto resultado = new DivisionToViewListDto();
        resultado.setDivisionId(saved.getId());
        resultado.setNombre(saved.getNombre());
        resultado.setProgramasEducativos(new ArrayList<>());

        return resultado;
    }

    // Actualizar una división existente
    public DivisionToViewListDto update(Long id, DivisionToUpdateDto dto) {
        DivisionEntity division = divisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " + id));

        division.setNombre(dto.getNombre());
        division.setImage(dto.getImage());
        division.setActivo(dto.getActivo());

        DivisionEntity updated = divisionRepository.save(division);

        DivisionToViewListDto resultado = new DivisionToViewListDto();
        resultado.setDivisionId(updated.getId());
        resultado.setNombre(updated.getNombre());

        if (updated.getProgramaEducativo() != null) {
            List<String> programas = new ArrayList<>();
            for (ProgramaEducativo prog : updated.getProgramaEducativo()) {
                programas.add(prog.getPrograma());
            }
            resultado.setProgramasEducativos(programas);
        } else {
            resultado.setProgramasEducativos(new ArrayList<>());
        }

        return resultado;
    }

    // Eliminar una división
    public void delete(Long id) {
        if (!divisionRepository.existsById(id)) {
            throw new RuntimeException("División no encontrada con ID: " + id);
        }
        divisionRepository.deleteById(id);
    }

}