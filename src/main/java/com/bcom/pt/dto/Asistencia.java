package com.bcom.pt.dto;

import javax.validation.constraints.Positive;

public class Asistencia {
    @Positive(message = "debe ser un id valido")
    public
    int idEvento;
    @Positive(message = "debe ser un id valido")
    public
    int idUsuario;

    public Asistencia() {}

    public Asistencia(int idEvento, int idUsuario) {
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
    }
}
