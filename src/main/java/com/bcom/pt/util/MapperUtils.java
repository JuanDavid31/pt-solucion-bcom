package com.bcom.pt.util;

import com.bcom.pt.entidad.Evento;

public class MapperUtils {

    public static Evento mapearEvento(Evento evento, Evento eventoNuevo) {
        return evento.withNombre(eventoNuevo.getNombre())
            .withDescripcion(eventoNuevo.getDescripcion())
            .withFecha(eventoNuevo.getFecha());
    }
}
