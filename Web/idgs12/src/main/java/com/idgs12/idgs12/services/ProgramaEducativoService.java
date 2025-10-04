// package com.idgs12.idgs12.services;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.idgs12.idgs12.dto.ProgramaEducativoToCreateDto;
// import com.idgs12.idgs12.dto.ProgramaEducativoToUpdateDto;
// import com.idgs12.idgs12.dto.ProgramaEducativoToViewListDto;
// import com.idgs12.idgs12.entity.ProgramaEducativo;
// import com.idgs12.idgs12.entity.DivisionEntity;
// import com.idgs12.idgs12.repository.ProgramaEducativoRepository;
// import com.idgs12.idgs12.repository.DivisionRepository;

// @Service
// public class ProgramaEducativoService {

// @Autowired
// private ProgramaEducativoRepository programaEducativoRepository;

// @Autowired
// private DivisionRepository divisionRepository;

// // Listar todos los programas educativos
// public List<ProgramaEducativoToViewListDto> findAll() {
// List<ProgramaEducativo> programas = programaEducativoRepository.findAll();
// List<ProgramaEducativoToViewListDto> resultado = new ArrayList<>();

// for (ProgramaEducativo programa : programas) {
// ProgramaEducativoToViewListDto dto = new ProgramaEducativoToViewListDto();
// dto.setProgramaId(programa.getId());
// dto.setPrograma(programa.getPrograma());
// dto.setActivo(programa.isActivo());

// if (programa.getDivision() != null) {
// dto.setDivisionNombre(programa.getDivision().getNombre());
// dto.setDivisionId(programa.getDivision().getId());
// }

// resultado.add(dto);
// }

// return resultado;
// }

// // Buscar un programa educativo por ID
// public ProgramaEducativoToViewListDto findById(Long id) {
// ProgramaEducativo programa = programaEducativoRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Programa Educativo no encontrado con
// ID: " + id));

// ProgramaEducativoToViewListDto dto = new ProgramaEducativoToViewListDto();
// dto.setProgramaId(programa.getId());
// dto.setPrograma(programa.getPrograma());
// dto.setActivo(programa.isActivo());

// if (programa.getDivision() != null) {
// dto.setDivisionNombre(programa.getDivision().getNombre());
// dto.setDivisionId(programa.getDivision().getId());
// }

// return dto;
// }

// // Crear un nuevo programa educativo
// public ProgramaEducativoToViewListDto create(ProgramaEducativoToCreateDto
// dto) {
// ProgramaEducativo entity = new ProgramaEducativo();
// entity.setPrograma(dto.getPrograma());
// entity.setActivo(dto.isActivo());

// if (dto.getDivisionId() != null) {
// DivisionEntity division = divisionRepository.findById(dto.getDivisionId())
// .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " +
// dto.getDivisionId()));
// entity.setDivision(division);
// }

// ProgramaEducativo saved = programaEducativoRepository.save(entity);

// ProgramaEducativoToViewListDto resultado = new
// ProgramaEducativoToViewListDto();
// resultado.setProgramaId(saved.getId());
// resultado.setPrograma(saved.getPrograma());
// resultado.setActivo(saved.isActivo());

// if (saved.getDivision() != null) {
// resultado.setDivisionNombre(saved.getDivision().getNombre());
// resultado.setDivisionId(saved.getDivision().getId());
// }

// return resultado;
// }

// // Actualizar un programa educativo existente
// public ProgramaEducativoToViewListDto update(Long id,
// ProgramaEducativoToUpdateDto dto) {
// ProgramaEducativo programa = programaEducativoRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Programa Educativo no encontrado con
// ID: " + id));

// programa.setPrograma(dto.getPrograma());
// programa.setActivo(dto.isActivo());

// if (dto.getDivisionId() != null) {
// DivisionEntity division = divisionRepository.findById(dto.getDivisionId())
// .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " +
// dto.getDivisionId()));
// programa.setDivision(division);
// }

// ProgramaEducativo updated = programaEducativoRepository.save(programa);

// ProgramaEducativoToViewListDto resultado = new
// ProgramaEducativoToViewListDto();
// resultado.setProgramaId(updated.getId());
// resultado.setPrograma(updated.getPrograma());
// resultado.setActivo(updated.isActivo());

// if (updated.getDivision() != null) {
// resultado.setDivisionNombre(updated.getDivision().getNombre());
// resultado.setDivisionId(updated.getDivision().getId());
// }

// return resultado;
// }

// // Eliminar un programa educativo
// public void delete(Long id) {
// if (!programaEducativoRepository.existsById(id)) {
// throw new RuntimeException("Programa Educativo no encontrado con ID: " + id);
// }
// programaEducativoRepository.deleteById(id);
// }

// // Habilitar un programa educativo
// public ProgramaEducativoToViewListDto habilitar(Long id) {
// ProgramaEducativo programa = programaEducativoRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Programa Educativo no encontrado con
// ID: " + id));

// programa.setActivo(true);
// ProgramaEducativo updated = programaEducativoRepository.save(programa);

// return convertirADto(updated);
// }

// // Deshabilitar un programa educativo
// public ProgramaEducativoToViewListDto deshabilitar(Long id) {
// ProgramaEducativo programa = programaEducativoRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Programa Educativo no encontrado con
// ID: " + id));

// programa.setActivo(false);
// ProgramaEducativo updated = programaEducativoRepository.save(programa);

// return convertirADto(updated);
// }

// // Quitar programa educativo de su división (desasociar)
// public ProgramaEducativoToViewListDto quitarDeDivision(Long id) {
// ProgramaEducativo programa = programaEducativoRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Programa Educativo no encontrado con
// ID: " + id));

// programa.setDivision(null);
// ProgramaEducativo updated = programaEducativoRepository.save(programa);

// return convertirADto(updated);
// }

// private ProgramaEducativoToViewListDto convertirADto(ProgramaEducativo
// programa) {
// ProgramaEducativoToViewListDto dto = new ProgramaEducativoToViewListDto();
// dto.setProgramaId(programa.getId());
// dto.setPrograma(programa.getPrograma());
// dto.setActivo(programa.isActivo());

// if (programa.getDivision() != null) {
// dto.setDivisionNombre(programa.getDivision().getNombre());
// dto.setDivisionId(programa.getDivision().getId());
// }

// return dto;
// }
// }