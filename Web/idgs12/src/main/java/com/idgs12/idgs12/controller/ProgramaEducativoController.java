// // ==================== CONTROLLER ====================
// package com.idgs12.idgs12.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.*;
// import org.springframework.beans.factory.annotation.Autowired;

// import com.idgs12.idgs12.dto.ProgramaEducativoToViewListDto;
// import com.idgs12.idgs12.dto.ProgramaEducativoToCreateDto;
// import com.idgs12.idgs12.dto.ProgramaEducativoToUpdateDto;
// import com.idgs12.idgs12.services.ProgramaEducativoService;

// @RestController
// @RequestMapping("/api")
// public class ProgramaEducativoController {

// @Autowired
// private ProgramaEducativoService programaEducativoService;

// @GetMapping("/programas")
// public List<ProgramaEducativoToViewListDto> findAll() {
// return programaEducativoService.findAll();
// }

// @GetMapping("/programas/{id}")
// public ProgramaEducativoToViewListDto findById(@PathVariable Long id) {
// return programaEducativoService.findById(id);
// }

// @PostMapping("/programas")
// public ProgramaEducativoToViewListDto create(@RequestBody
// ProgramaEducativoToCreateDto dto) {
// return programaEducativoService.create(dto);
// }

// @PutMapping("/programas/{id}")
// public ProgramaEducativoToViewListDto update(@PathVariable Long id,
// @RequestBody ProgramaEducativoToUpdateDto dto) {
// return programaEducativoService.update(id, dto);
// }

// @DeleteMapping("/programas/{id}")
// public void delete(@PathVariable Long id) {
// programaEducativoService.delete(id);
// }

// // Habilitar un programa educativo
// @PatchMapping("/programas/{id}/habilitar")
// public ProgramaEducativoToViewListDto habilitar(@PathVariable Long id) {
// return programaEducativoService.habilitar(id);
// }

// // Deshabilitar un programa educativo
// @PatchMapping("/programas/{id}/deshabilitar")
// public ProgramaEducativoToViewListDto deshabilitar(@PathVariable Long id) {
// return programaEducativoService.deshabilitar(id);
// }

// // Quitar programa educativo de una división (desasociar)
// @PatchMapping("/programas/{id}/quitar-division")
// public ProgramaEducativoToViewListDto quitarDeDivision(@PathVariable Long id)
// {
// return programaEducativoService.quitarDeDivision(id);
// }
// }