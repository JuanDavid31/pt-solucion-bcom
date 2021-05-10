package com.bcom.pt.util;

import com.bcom.pt.entidad.Evento;

public class MapperUtils {

    public static void mapearEvento(Evento evento, Evento eventoNuevo) {
        evento.setNombre(eventoNuevo.getNombre());
        evento.setDescripcion(eventoNuevo.getDescripcion());
        evento.setFecha(eventoNuevo.getFecha());
    }
}
