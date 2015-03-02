/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "paciente_cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacienteCita.findAll", query = "SELECT p FROM PacienteCita p"),
    @NamedQuery(name = "PacienteCita.findByFecha", query = "SELECT p FROM PacienteCita p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "PacienteCita.findByHora", query = "SELECT p FROM PacienteCita p WHERE p.hora = :hora"),
    @NamedQuery(name = "PacienteCita.findByIdPacienteCita", query = "SELECT p FROM PacienteCita p WHERE p.idPacienteCita = :idPacienteCita")})
public class PacienteCita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIME)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hora")
    @Temporal(TemporalType.DATE)
    private Date hora;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPaciente_Cita")
    private Integer idPacienteCita;
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;
    @JoinColumn(name = "idCita", referencedColumnName = "idCita")
    @ManyToOne(optional = false)
    private Cita idCita;

    public PacienteCita() {
    }

    public PacienteCita(Integer idPacienteCita) {
        this.idPacienteCita = idPacienteCita;
    }

    public PacienteCita(Integer idPacienteCita, Date fecha, Date hora) {
        this.idPacienteCita = idPacienteCita;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Integer getIdPacienteCita() {
        return idPacienteCita;
    }

    public void setIdPacienteCita(Integer idPacienteCita) {
        this.idPacienteCita = idPacienteCita;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Cita getIdCita() {
        return idCita;
    }

    public void setIdCita(Cita idCita) {
        this.idCita = idCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPacienteCita != null ? idPacienteCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteCita)) {
            return false;
        }
        PacienteCita other = (PacienteCita) object;
        if ((this.idPacienteCita == null && other.idPacienteCita != null) || (this.idPacienteCita != null && !this.idPacienteCita.equals(other.idPacienteCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.PacienteCita[ idPacienteCita=" + idPacienteCita + " ]";
    }
    
}
