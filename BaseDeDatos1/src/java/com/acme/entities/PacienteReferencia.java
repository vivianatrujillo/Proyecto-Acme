/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "paciente_referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacienteReferencia.findAll", query = "SELECT p FROM PacienteReferencia p"),
    @NamedQuery(name = "PacienteReferencia.findByCodPacienteReferencia", query = "SELECT p FROM PacienteReferencia p WHERE p.codPacienteReferencia = :codPacienteReferencia")})
public class PacienteReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "cod_Paciente_Referencia")
    private String codPacienteReferencia;
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;
    @JoinColumn(name = "idReferencia", referencedColumnName = "idReferencia")
    @ManyToOne(optional = false)
    private Referencia idReferencia;

    public PacienteReferencia() {
    }

    public PacienteReferencia(String codPacienteReferencia) {
        this.codPacienteReferencia = codPacienteReferencia;
    }

    public String getCodPacienteReferencia() {
        return codPacienteReferencia;
    }

    public void setCodPacienteReferencia(String codPacienteReferencia) {
        this.codPacienteReferencia = codPacienteReferencia;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Referencia getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Referencia idReferencia) {
        this.idReferencia = idReferencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPacienteReferencia != null ? codPacienteReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteReferencia)) {
            return false;
        }
        PacienteReferencia other = (PacienteReferencia) object;
        if ((this.codPacienteReferencia == null && other.codPacienteReferencia != null) || (this.codPacienteReferencia != null && !this.codPacienteReferencia.equals(other.codPacienteReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.PacienteReferencia[ codPacienteReferencia=" + codPacienteReferencia + " ]";
    }
    
}
