package com.silvana.homecare.web.rest.errors;

public class PacienteYaTieneCuidador extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PacienteYaTieneCuidador() {
        super(ErrorConstants.PACIENTE_ALREADY_ASIGNED, "Email de paciente ya tiene famliiar asignado", "userManagement", "familiarexists");
    }
}
