package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepositorio extends JpaRepository<Evento, Integer> {
}
