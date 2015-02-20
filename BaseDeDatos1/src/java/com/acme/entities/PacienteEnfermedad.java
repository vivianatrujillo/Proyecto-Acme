/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "paciente_enfermedad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacienteEnfermedad.findAll", query = "SELECT p FROM PacienteEnfermedad p"),
    @NamedQuery(name = "PacienteEnfermedad.findByCodPacienteEnfermedadcol", query = "SELECT p FROM PacienteEnfermedad p WHERE p.codPacienteEnfermedadcol = :codPacienteEnfermedadcol")})
public class PacienteEnfermedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_Paciente_Enfermedadcol")
    private Integer codPacienteEnfermedadcol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteEnfermedad")
    private List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadList;
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;

    public PacienteEnfermedad() {
    }

    public PacienteEnfermedad(Integer codPacienteEnfermedadcol) {
        this.codPacienteEnfermedadcol = codPacienteEnfermedadcol;
    }

    public Integer getCodPacienteEnfermedadcol() {
        return codPacienteEnfermedadcol;
    }

    public void setCodPacienteEnfermedadcol(Integer codPacienteEnfermedadcol) {
        this.codPacienteEnfermedadcol = codPacienteEnfermedadcol;
    }

    @XmlTransient
    public List<PacienteEnfermedadEnfermedad> getPacienteEnfermedadEnfermedadList() {
        return pacienteEnfermedadEnfermedadList;
    }

    public void setPacienteEnfermedadEnfermedadList(List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadList) {
        this.pacienteEnfermedadEnfermedadList = pacienteEnfermedadEnfermedadList;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPacienteEnfermedadcol != null ? codPacienteEnfermedadcol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteEnfermedad)) {
            return false;
        }
        PacienteEnfermedad other = (PacienteEnfermedad) object;
        if ((this.codPacienteEnfermedadcol == null && other.codPacienteEnfermedadcol != null) || (this.codPacienteEnfermedadcol != null && !this.codPacienteEnfermedadcol.equals(other.codPacienteEnfermedadcol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.PacienteEnfermedad[ codPacienteEnfermedadcol=" + codPacienteEnfermedadcol + " ]";
    }
    
}
