package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> { }
