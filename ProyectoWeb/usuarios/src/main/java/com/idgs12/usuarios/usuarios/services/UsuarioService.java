package com.idgs12.usuarios.usuarios.services;

import com.idgs12.usuarios.usuarios.FeignClient.ProgramaFeignClient;
import com.idgs12.usuarios.usuarios.dto.ProgramaDTO;
import com.idgs12.usuarios.usuarios.dto.UsuarioDTO;
import com.idgs12.usuarios.usuarios.dto.UsuarioResponseDTO;
import com.idgs12.usuarios.usuarios.dto.ProgramaUsuarioDTO;
import com.idgs12.usuarios.usuarios.dto.UsuarioAuthDTO;
import com.idgs12.usuarios.usuarios.entity.ProgramaUsuario;
import com.idgs12.usuarios.usuarios.entity.UsuarioEntity;
import com.idgs12.usuarios.usuarios.repository.ProgramaUsuarioRepository;
import com.idgs12.usuarios.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProgramaUsuarioRepository programaUsuarioRepository;

    @Autowired
    private ProgramaFeignClient programaFeignClient;

    public List<UsuarioResponseDTO> getAllUsuariosDTO() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(usuario.getId());
            dto.setNombre(usuario.getNombre());
            dto.setApellidoPaterno(usuario.getApellidoPaterno());
            dto.setApellidoMaterno(usuario.getApellidoMaterno());
            dto.setCorreo(usuario.getCorreo());
            dto.setRol(usuario.getRol());
            dto.setMatricula(usuario.getMatricula());

            if (usuario.getProgramas() != null && !usuario.getProgramas().isEmpty()) {
                List<Long> programaIds = usuario.getProgramas().stream()
                        .map(ProgramaUsuario::getProgramaId)
                        .collect(Collectors.toList());

                List<ProgramaDTO> programas = programaFeignClient.obtenerProgramasPorIds(programaIds)
                        .stream()
                        .map(p -> {
                            ProgramaDTO pdto = new ProgramaDTO();
                            pdto.setId(p.getId());
                            pdto.setNombre(p.getNombre());
                            return pdto;
                        }).collect(Collectors.toList());

                dto.setProgramas(programas);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public UsuarioEntity saveUsuarioConProgramas(UsuarioDTO usuarioDTO) {
        UsuarioEntity usuario;

        if (usuarioDTO.getId() != 0) {
            usuario = usuarioRepository.findById(usuarioDTO.getId()).orElse(new UsuarioEntity());
        } else {
            usuario = new UsuarioEntity();
        }

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidoPaterno(usuarioDTO.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioDTO.getApellidoMaterno());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setMatricula(usuarioDTO.getMatricula());

        UsuarioEntity savedUsuario = usuarioRepository.save(usuario);

        programaUsuarioRepository.deleteByUsuario_Id(savedUsuario.getId());

        if (usuarioDTO.getProgramaIds() != null && !usuarioDTO.getProgramaIds().isEmpty()) {
            List<ProgramaUsuario> relaciones = usuarioDTO.getProgramaIds().stream()
                    .map(programaId -> {
                        ProgramaUsuario pu = new ProgramaUsuario();
                        pu.setUsuario(savedUsuario);
                        pu.setProgramaId(programaId);
                        return pu;
                    })
                    .collect(Collectors.toList());

            programaUsuarioRepository.saveAll(relaciones);
        }

        return savedUsuario;
    }

    public UsuarioResponseDTO getUsuarioDTOById(int id) {
        UsuarioEntity usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null)
            return null;

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());
        dto.setMatricula(usuario.getMatricula());

        if (usuario.getProgramas() != null && !usuario.getProgramas().isEmpty()) {
            List<Long> programaIds = usuario.getProgramas().stream()
                    .map(ProgramaUsuario::getProgramaId)
                    .collect(Collectors.toList());

            List<ProgramaDTO> programas = programaFeignClient.obtenerProgramasPorIds(programaIds)
                    .stream()
                    .map(p -> {
                        ProgramaDTO pdto = new ProgramaDTO();
                        pdto.setId(p.getId());
                        pdto.setNombre(p.getNombre());
                        return pdto;
                    }).collect(Collectors.toList());

            dto.setProgramas(programas);
        }

        return dto;
    }

    public UsuarioResponseDTO getUsuarioDTOByMatricula(String matricula) {
        System.out.println("🔍 Buscando usuario con matrícula: " + matricula);

        try {
            UsuarioEntity usuario = usuarioRepository.findByMatricula(matricula).orElse(null);

            if (usuario == null) {
                System.err.println("❌ Usuario no encontrado con matrícula: " + matricula);
                return null;
            }

            System.out.println("✅ Usuario encontrado: " + usuario.getNombre());

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(usuario.getId());
            dto.setNombre(usuario.getNombre());
            dto.setApellidoPaterno(usuario.getApellidoPaterno());
            dto.setApellidoMaterno(usuario.getApellidoMaterno());
            dto.setCorreo(usuario.getCorreo());
            dto.setRol(usuario.getRol());
            dto.setMatricula(usuario.getMatricula());

            if (usuario.getProgramas() != null && !usuario.getProgramas().isEmpty()) {
                List<Long> programaIds = usuario.getProgramas().stream()
                        .map(ProgramaUsuario::getProgramaId)
                        .collect(Collectors.toList());

                List<ProgramaDTO> programas = programaFeignClient.obtenerProgramasPorIds(programaIds)
                        .stream()
                        .map(p -> {
                            ProgramaDTO pdto = new ProgramaDTO();
                            pdto.setId(p.getId());
                            pdto.setNombre(p.getNombre());
                            return pdto;
                        }).collect(Collectors.toList());

                dto.setProgramas(programas);
            }

            return dto;

        } catch (Exception e) {
            System.err.println("❌ Error en getUsuarioDTOByMatricula: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public UsuarioAuthDTO findByCorreo(String correo) {
        UsuarioEntity entity = usuarioRepository.findByCorreo(correo);
        if (entity == null)
            return null;

        UsuarioAuthDTO dto = new UsuarioAuthDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellidoPaterno(entity.getApellidoPaterno());
        dto.setApellidoMaterno(entity.getApellidoMaterno());
        dto.setCorreo(entity.getCorreo());
        dto.setMatricula(entity.getMatricula());
        dto.setRol(entity.getRol());
        dto.setContrasena(entity.getContrasena()); // Solo para Auth

        return dto;
    }

    public UsuarioDTO createWithPassword(UsuarioAuthDTO dto) {
        System.out.println("Inicio creación usuario con correo: " + dto.getCorreo());

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellidoPaterno(dto.getApellidoPaterno());
        entity.setApellidoMaterno(dto.getApellidoMaterno());
        entity.setCorreo(dto.getCorreo());
        entity.setMatricula(dto.getMatricula());
        entity.setRol(dto.getRol());
        entity.setContrasena(dto.getContrasena()); // Ya viene encriptado desde Auth

        UsuarioEntity savedEntity = usuarioRepository.save(entity);

        System.out.println("Usuario creado con ID: " + savedEntity.getId());

        // Devolver DTO sin contraseña
        UsuarioDTO response = new UsuarioDTO();
        response.setId(savedEntity.getId());
        response.setNombre(savedEntity.getNombre());
        response.setApellidoPaterno(savedEntity.getApellidoPaterno());
        response.setApellidoMaterno(savedEntity.getApellidoMaterno());
        response.setCorreo(savedEntity.getCorreo());
        response.setMatricula(savedEntity.getMatricula());
        response.setRol(savedEntity.getRol());
        // NO incluir contrasena en la respuesta

        return response;
    }

}
