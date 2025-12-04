package com.idgs12.grupos.grupos.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.idgs12.grupos.grupos.dto.AsignarProfesorDTO;
import com.idgs12.grupos.grupos.dto.GrupoDTO;
import com.idgs12.grupos.grupos.dto.GrupoListDTO;
import com.idgs12.grupos.grupos.dto.GrupoResponseDTO;
import com.idgs12.grupos.grupos.dto.ProfesorDTO;
import com.idgs12.grupos.grupos.entity.GruposEntity;
import com.idgs12.grupos.grupos.repository.GrupoProfesorRepository;
import com.idgs12.grupos.grupos.repository.GrupoRepository;
import com.idgs12.grupos.grupos.repository.GrupoUsuarioRepository;
import com.idgs12.grupos.grupos.services.GrupoService;

@RestController
@RequestMapping("/grupos")
@CrossOrigin(origins = "*")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private GrupoUsuarioRepository grupoUsuarioRepository;

    @Autowired
    private GrupoProfesorRepository grupoProfesorRepository;

    // ================================
    // ENDPOINTS DE GRUPOS
    // ================================

    // Obtener todos los grupos
    @GetMapping("/all")
    public List<GrupoListDTO> getAllGrupos() {
        return grupoRepository.findAll()
                .stream()
                .map(grupo -> {
                    GrupoListDTO dto = new GrupoListDTO();
                    dto.setId(grupo.getId());
                    dto.setNombre(grupo.getNombre());
                    dto.setCuatrimestre(grupo.getCuatrimestre());
                    dto.setEstado(grupo.getEstado());

                    // Contar alumnos del grupo
                    int cantidadAlumnos = grupoUsuarioRepository.findByGrupo_Id(grupo.getId()).size();
                    dto.setCantidadAlumnos(cantidadAlumnos);

                    // Contar profesores del grupo
                    int cantidadProfesores = grupoProfesorRepository.findByGrupo_Id(grupo.getId()).size();
                    dto.setCantidadProfesores(cantidadProfesores);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Ver grupo con sus alumnos y profesores
    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponseDTO> getGrupoConAlumnos(@PathVariable int id) {
        GrupoResponseDTO grupo = grupoService.findByIdWithAlumnos(id);
        if (grupo != null) {
            return ResponseEntity.ok(grupo);
        }
        return ResponseEntity.notFound().build();
    }

    // Crear un nuevo grupo
    @PostMapping
    public ResponseEntity<GruposEntity> crearGrupo(@RequestBody GrupoDTO grupoDTO) {
        GruposEntity nuevoGrupo = grupoService.crearGrupo(grupoDTO);
        return ResponseEntity.ok(nuevoGrupo);
    }

    // Actualizar un grupo existente
    @PutMapping("/{id}")
    public ResponseEntity<GruposEntity> actualizarGrupo(@PathVariable int id, @RequestBody GrupoDTO grupoDTO) {
        grupoDTO.setId(id);
        GruposEntity grupoActualizado = grupoService.actualizarGrupo(grupoDTO);
        return ResponseEntity.ok(grupoActualizado);
    }

    // ================================
    // ENDPOINTS DE ALUMNOS
    // ================================

    // Asignar usuarios (alumnos) a un grupo
    @PostMapping("/{id}/asignar-usuarios")
    public ResponseEntity<String> asignarUsuarios(@PathVariable int id,
            @RequestBody List<Long> usuarioIds) {
        try {
            grupoService.asignarUsuariosAGrupo(id, usuarioIds);
            return ResponseEntity.ok("Usuarios asignados correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Obtener grupo de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<GrupoResponseDTO> getGrupoPorUsuario(@PathVariable Long usuarioId) {
        GrupoResponseDTO grupo = grupoService.findGrupoByUsuarioId(usuarioId);
        if (grupo != null) {
            return ResponseEntity.ok(grupo);
        }
        return ResponseEntity.notFound().build();
    }

    // ================================
    // ENDPOINTS DE PROFESORES
    // ================================

    // Asignar profesor a un grupo
    @PostMapping("/{id}/profesores")
    public ResponseEntity<?> asignarProfesor(
            @PathVariable Integer id,
            @RequestBody AsignarProfesorDTO dto) {
        try {
            grupoService.asignarProfesor(id, dto);
            return ResponseEntity.ok(Map.of("mensaje", "Profesor asignado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener profesores de un grupo
    @GetMapping("/{id}/profesores")
    public ResponseEntity<List<ProfesorDTO>> getProfesoresDelGrupo(@PathVariable Integer id) {
        List<ProfesorDTO> profesores = grupoService.obtenerProfesoresDelGrupo(id);
        return ResponseEntity.ok(profesores);
    }

    // Remover profesor de un grupo
    @DeleteMapping("/{grupoId}/profesores/{profesorId}")
    public ResponseEntity<?> removerProfesor(
            @PathVariable Integer grupoId,
            @PathVariable Long profesorId) {
        try {
            grupoService.removerProfesor(grupoId, profesorId);
            return ResponseEntity.ok(Map.of("mensaje", "Profesor removido correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener profesores disponibles (activos) - Para dropdown en el frontend
    @GetMapping("/profesores-disponibles")
    public ResponseEntity<List<ProfesorDTO>> getProfesoresDisponibles() {
        try {
            List<ProfesorDTO> profesores = grupoService.obtenerProfesoresActivos();
            return ResponseEntity.ok(profesores);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Obtener grupos de un profesor específico
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<GruposEntity>> getGruposDelProfesor(@PathVariable Long profesorId) {
        List<GruposEntity> grupos = grupoService.obtenerGruposDelProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }
}