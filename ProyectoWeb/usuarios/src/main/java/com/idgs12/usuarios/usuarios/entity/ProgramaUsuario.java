package com.idgs12.usuarios.usuarios.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "programa_usuario")

public class ProgramaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    private Long programaId;
}
