/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "paciente_enfermedad_enfermedad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacienteEnfermedadEnfermedad.findAll", query = "SELECT p FROM PacienteEnfermedadEnfermedad p"),
    @NamedQuery(name = "PacienteEnfermedadEnfermedad.findByCodPacienteEnfermedadEnfermedad", query = "SELECT p FROM PacienteEnfermedadEnfermedad p WHERE p.pacienteEnfermedadEnfermedadPK.codPacienteEnfermedadEnfermedad = :codPacienteEnfermedadEnfermedad"),
    @NamedQuery(name = "PacienteEnfermedadEnfermedad.findByCodPacienteEnfermedadcol", query = "SELECT p FROM PacienteEnfermedadEnfermedad p WHERE p.pacienteEnfermedadEnfermedadPK.codPacienteEnfermedadcol = :codPacienteEnfermedadcol"),
    @NamedQuery(name = "PacienteEnfermedadEnfermedad.findByIdEnfermedad", query = "SELECT p FROM PacienteEnfermedadEnfermedad p WHERE p.pacienteEnfermedadEnfermedadPK.idEnfermedad = :idEnfermedad")})
public class PacienteEnfermedadEnfermedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PacienteEnfermedadEnfermedadPK pacienteEnfermedadEnfermedadPK;
    @JoinColumn(name = "idEnfermedad", referencedColumnName = "idEnfermedad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Enfermedad enfermedad;
    @JoinColumn(name = "cod_Paciente_Enfermedadcol", referencedColumnName = "cod_Paciente_Enfermedadcol", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PacienteEnfermedad pacienteEnfermedad;

    public PacienteEnfermedadEnfermedad() {
    }

    public PacienteEnfermedadEnfermedad(PacienteEnfermedadEnfermedadPK pacienteEnfermedadEnfermedadPK) {
        this.pacienteEnfermedadEnfermedadPK = pacienteEnfermedadEnfermedadPK;
    }

    public PacienteEnfermedadEnfermedad(int codPacienteEnfermedadEnfermedad, int codPacienteEnfermedadcol, int idEnfermedad) {
        this.pacienteEnfermedadEnfermedadPK = new PacienteEnfermedadEnfermedadPK(codPacienteEnfermedadEnfermedad, codPacienteEnfermedadcol, idEnfermedad);
    }

    public PacienteEnfermedadEnfermedadPK getPacienteEnfermedadEnfermedadPK() {
        return pacienteEnfermedadEnfermedadPK;
    }

    public void setPacienteEnfermedadEnfermedadPK(PacienteEnfermedadEnfermedadPK pacienteEnfermedadEnfermedadPK) {
        this.pacienteEnfermedadEnfermedadPK = pacienteEnfermedadEnfermedadPK;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public PacienteEnfermedad getPacienteEnfermedad() {
        return pacienteEnfermedad;
    }

    public void setPacienteEnfermedad(PacienteEnfermedad pacienteEnfermedad) {
        this.pacienteEnfermedad = pacienteEnfermedad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacienteEnfermedadEnfermedadPK != null ? pacienteEnfermedadEnfermedadPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteEnfermedadEnfermedad)) {
            return false;
        }
        PacienteEnfermedadEnfermedad other = (PacienteEnfermedadEnfermedad) object;
        if ((this.pacienteEnfermedadEnfermedadPK == null && other.pacienteEnfermedadEnfermedadPK != null) || (this.pacienteEnfermedadEnfermedadPK != null && !this.pacienteEnfermedadEnfermedadPK.equals(other.pacienteEnfermedadEnfermedadPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.PacienteEnfermedadEnfermedad[ pacienteEnfermedadEnfermedadPK=" + pacienteEnfermedadEnfermedadPK + " ]";
    }
    
}
