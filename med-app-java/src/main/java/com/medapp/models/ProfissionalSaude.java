package com.medapp.models;

public class ProfissionalSaude extends User {
    private static final long serialVersionUID = 1L;
    private String crm;
    private String especialidade;
    private String departamento;

    public ProfissionalSaude() {
        super();
    }

    public ProfissionalSaude(String username, String password, String email, 
                           String crm, String especialidade, String departamento) {
        super(username, password, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.departamento = departamento;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String getTipoUsuario() {
        return "Profissional de Saúde";
    }
}
