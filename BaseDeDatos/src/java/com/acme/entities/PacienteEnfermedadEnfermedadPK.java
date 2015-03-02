/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author usuario
 */
@Embeddable
public class PacienteEnfermedadEnfermedadPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_Paciente_Enfermedad_Enfermedad")
    private int codPacienteEnfermedadEnfermedad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_Paciente_Enfermedadcol")
    private int codPacienteEnfermedadcol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEnfermedad")
    private int idEnfermedad;

    public PacienteEnfermedadEnfermedadPK() {
    }

    public PacienteEnfermedadEnfermedadPK(int codPacienteEnfermedadEnfermedad, int codPacienteEnfermedadcol, int idEnfermedad) {
        this.codPacienteEnfermedadEnfermedad = codPacienteEnfermedadEnfermedad;
        this.codPacienteEnfermedadcol = codPacienteEnfermedadcol;
        this.idEnfermedad = idEnfermedad;
    }

    public int getCodPacienteEnfermedadEnfermedad() {
        return codPacienteEnfermedadEnfermedad;
    }

    public void setCodPacienteEnfermedadEnfermedad(int codPacienteEnfermedadEnfermedad) {
        this.codPacienteEnfermedadEnfermedad = codPacienteEnfermedadEnfermedad;
    }

    public int getCodPacienteEnfermedadcol() {
        return codPacienteEnfermedadcol;
    }

    public void setCodPacienteEnfermedadcol(int codPacienteEnfermedadcol) {
        this.codPacienteEnfermedadcol = codPacienteEnfermedadcol;
    }

    public int getIdEnfermedad() {
        return idEnfermedad;
    }

    public void setIdEnfermedad(int idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codPacienteEnfermedadEnfermedad;
        hash += (int) codPacienteEnfermedadcol;
        hash += (int) idEnfermedad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteEnfermedadEnfermedadPK)) {
            return false;
        }
        PacienteEnfermedadEnfermedadPK other = (PacienteEnfermedadEnfermedadPK) object;
        if (this.codPacienteEnfermedadEnfermedad != other.codPacienteEnfermedadEnfermedad) {
            return false;
        }
        if (this.codPacienteEnfermedadcol != other.codPacienteEnfermedadcol) {
            return false;
        }
        if (this.idEnfermedad != other.idEnfermedad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.PacienteEnfermedadEnfermedadPK[ codPacienteEnfermedadEnfermedad=" + codPacienteEnfermedadEnfermedad + ", codPacienteEnfermedadcol=" + codPacienteEnfermedadcol + ", idEnfermedad=" + idEnfermedad + " ]";
    }
    
}
