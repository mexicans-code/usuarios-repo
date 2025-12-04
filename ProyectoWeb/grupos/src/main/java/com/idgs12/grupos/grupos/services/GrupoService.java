package com.idgs12.grupos.grupos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idgs12.grupos.grupos.dto.AsignarProfesorDTO;
import com.idgs12.grupos.grupos.dto.GrupoDTO;
import com.idgs12.grupos.grupos.dto.GrupoResponseDTO;
import com.idgs12.grupos.grupos.dto.ProfesorDTO;
import com.idgs12.grupos.grupos.dto.UsuarioDTO;
import com.idgs12.grupos.grupos.entity.GrupoProfesor;
import com.idgs12.grupos.grupos.entity.GrupoUsuario;
import com.idgs12.grupos.grupos.entity.GruposEntity;
import com.idgs12.grupos.grupos.FeignClient.ProfesorFeignClient;
import com.idgs12.grupos.grupos.FeignClient.UsuarioFeignClient;
import com.idgs12.grupos.grupos.repository.GrupoProfesorRepository;
import com.idgs12.grupos.grupos.repository.GrupoRepository;
import com.idgs12.grupos.grupos.repository.GrupoUsuarioRepository;

@Service
public class GrupoService {

    @Autowired
    private GrupoUsuarioRepository grupoUsuarioRepository;

    @Autowired
    private GrupoProfesorRepository grupoProfesorRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private ProfesorFeignClient profesorFeignClient;

    // ================================
    // MÉTODOS EXISTENTES
    // ================================

    // ✅ Ver grupo con sus alumnos
    public GrupoResponseDTO findByIdWithAlumnos(int grupoId) {
        GruposEntity grupo = grupoRepository.findById(grupoId).orElse(null);

        if (grupo == null) {
            return null;
        }

        GrupoResponseDTO response = new GrupoResponseDTO();
        response.setId(grupo.getId());
        response.setNombre(grupo.getNombre());
        response.setCuatrimestre(grupo.getCuatrimestre());
        response.setEstado(grupo.getEstado());

        // Obtener alumnos
        List<GrupoUsuario> grupoUsuarios = grupoUsuarioRepository.findByGrupo_Id(grupoId);

        List<UsuarioDTO> alumnos = grupoUsuarios.stream()
                .map(gu -> {
                    try {
                        return usuarioFeignClient.getUsuarioById(gu.getUsuarioId());
                    } catch (Exception e) {
                        System.err.println("❌ Error al obtener usuario: " + e.getMessage());
                        return null;
                    }
                })
                .filter(u -> u != null)
                .collect(Collectors.toList());

        response.setAlumnos(alumnos);

        // ✅ NUEVO: Obtener profesores del grupo
        List<ProfesorDTO> profesores = obtenerProfesoresDelGrupo(grupoId);
        response.setProfesores(profesores);

        return response;
    }

    // Crear grupo
    @Transactional
    public GruposEntity crearGrupo(GrupoDTO grupoDTO) {
        GruposEntity grupo = new GruposEntity();
        grupo.setNombre(grupoDTO.getNombre());
        grupo.setCuatrimestre(grupoDTO.getCuatrimestre());
        grupo.setEstado(grupoDTO.getEstado());
        return grupoRepository.save(grupo);
    }

    // Actualizar grupo
    @Transactional
    public GruposEntity actualizarGrupo(GrupoDTO grupoDTO) {
        GruposEntity grupo = grupoRepository.findById(grupoDTO.getId()).orElse(new GruposEntity());
        grupo.setNombre(grupoDTO.getNombre());
        grupo.setCuatrimestre(grupoDTO.getCuatrimestre());
        grupo.setEstado(grupoDTO.getEstado());
        return grupoRepository.save(grupo);
    }

    // Asignar usuarios a un grupo
    @Transactional
    public void asignarUsuariosAGrupo(int grupoId, List<Long> usuarioIds) {
        grupoUsuarioRepository.deleteByGrupo_Id(grupoId);

        if (usuarioIds != null && !usuarioIds.isEmpty()) {
            GruposEntity grupo = grupoRepository.findById(grupoId).orElse(null);

            if (grupo == null) {
                throw new RuntimeException("Grupo no encontrado");
            }

            List<GrupoUsuario> relaciones = usuarioIds.stream()
                    .map(usuarioId -> {
                        GrupoUsuario gu = new GrupoUsuario();
                        gu.setGrupo(grupo);
                        gu.setUsuarioId(usuarioId);
                        return gu;
                    })
                    .collect(Collectors.toList());

            grupoUsuarioRepository.saveAll(relaciones);

            System.out.println("✅ Asignados " + relaciones.size() + " alumnos al grupo " + grupoId);
        }
    }

    public GrupoResponseDTO findGrupoByUsuarioId(Long usuarioId) {
        List<GrupoUsuario> relaciones = grupoUsuarioRepository.findByUsuarioId(usuarioId);

        if (relaciones.isEmpty()) {
            return null;
        }

        GrupoUsuario relacion = relaciones.get(0);
        GruposEntity grupo = relacion.getGrupo();

        GrupoResponseDTO dto = new GrupoResponseDTO();
        dto.setId(grupo.getId());
        dto.setNombre(grupo.getNombre());
        dto.setCuatrimestre(grupo.getCuatrimestre());
        dto.setEstado(grupo.getEstado());

        List<GrupoUsuario> alumnosDelGrupo = grupoUsuarioRepository.findByGrupo_Id(grupo.getId());
        List<UsuarioDTO> alumnos = alumnosDelGrupo.stream()
                .map(gu -> {
                    try {
                        return usuarioFeignClient.getUsuarioById(gu.getUsuarioId());
                    } catch (Exception e) {
                        System.err.println("❌ Error al obtener usuario: " + e.getMessage());
                        return null;
                    }
                })
                .filter(u -> u != null)
                .collect(Collectors.toList());

        dto.setAlumnos(alumnos);

        return dto;
    }

    // ================================
    // MÉTODOS PARA PROFESORES
    // ================================

    // ASIGNAR PROFESOR A GRUPO
    @Transactional
    public void asignarProfesor(Integer grupoId, AsignarProfesorDTO dto) {
        System.out.println("👨‍🏫 Asignando profesor " + dto.getProfesorId() + " al grupo " + grupoId);

        // 1. Verificar que el grupo existe
        GruposEntity grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("❌ Grupo no encontrado"));

        // 2. Verificar que el profesor existe y está activo
        ProfesorDTO profesor = profesorFeignClient.getProfesorById(dto.getProfesorId());
        if (profesor == null || !profesor.getActivo()) {
            throw new RuntimeException("❌ Profesor no válido o inactivo");
        }

        // 3. Verificar que no esté ya asignado
        if (grupoProfesorRepository.existsByGrupo_IdAndProfesorId(grupoId, dto.getProfesorId())) {
            throw new RuntimeException("❌ El profesor ya está asignado a este grupo");
        }

        // 4. Crear relación
        GrupoProfesor relacion = new GrupoProfesor();
        relacion.setGrupo(grupo);
        relacion.setProfesorId(dto.getProfesorId());

        grupoProfesorRepository.save(relacion);

        System.out.println("✅ Profesor asignado correctamente");
    }

    // OBTENER PROFESORES DE UN GRUPO
    public List<ProfesorDTO> obtenerProfesoresDelGrupo(Integer grupoId) {
        List<GrupoProfesor> relaciones = grupoProfesorRepository.findByGrupo_Id(grupoId);

        return relaciones.stream()
                .map(rel -> {
                    try {
                        return profesorFeignClient.getProfesorById(rel.getProfesorId());
                    } catch (Exception e) {
                        System.err.println("⚠️ Error al obtener profesor: " + e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    // REMOVER PROFESOR DE GRUPO
    @Transactional
    public void removerProfesor(Integer grupoId, Long profesorId) {
        System.out.println("🗑️ Removiendo profesor " + profesorId + " del grupo " + grupoId);

        List<GrupoProfesor> relaciones = grupoProfesorRepository.findByGrupo_Id(grupoId);

        GrupoProfesor relacion = relaciones.stream()
                .filter(r -> r.getProfesorId().equals(profesorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("❌ Relación no encontrada"));

        grupoProfesorRepository.delete(relacion);

        System.out.println("✅ Profesor removido del grupo");
    }

    // OBTENER GRUPOS DE UN PROFESOR
    public List<GruposEntity> obtenerGruposDelProfesor(Long profesorId) {
        List<GrupoProfesor> relaciones = grupoProfesorRepository.findByProfesorId(profesorId);

        return relaciones.stream()
                .map(rel -> rel.getGrupo())
                .collect(Collectors.toList());
    }

    // OBTENER PROFESORES ACTIVOS (para dropdown)
    public List<ProfesorDTO> obtenerProfesoresActivos() {
        try {
            return profesorFeignClient.getProfesoresActivos();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener profesores activos: " + e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de profesores");
        }
    }
}