package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepositorio extends CrudRepository<Usuario, Integer> { }
