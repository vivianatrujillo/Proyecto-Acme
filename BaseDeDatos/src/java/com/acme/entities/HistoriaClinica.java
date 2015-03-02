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
 * @author usuario
 */
@Entity
@Table(name = "historia clinica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoriaClinica.findAll", query = "SELECT h FROM HistoriaClinica h"),
    @NamedQuery(name = "HistoriaClinica.findByIdHistoriaClinica", query = "SELECT h FROM HistoriaClinica h WHERE h.idHistoriaClinica = :idHistoriaClinica"),
    @NamedQuery(name = "HistoriaClinica.findByIdDiagnostico", query = "SELECT h FROM HistoriaClinica h WHERE h.idDiagnostico = :idDiagnostico"),
    @NamedQuery(name = "HistoriaClinica.findByIdFormatoDeEvolucion", query = "SELECT h FROM HistoriaClinica h WHERE h.idFormatoDeEvolucion = :idFormatoDeEvolucion"),
    @NamedQuery(name = "HistoriaClinica.findByIdTratamiento", query = "SELECT h FROM HistoriaClinica h WHERE h.idTratamiento = :idTratamiento")})
public class HistoriaClinica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idHistoria Clinica")
    private Integer idHistoriaClinica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_diagnostico")
    private int idDiagnostico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_formato de evolucion")
    private int idFormatoDeEvolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tratamiento")
    private int idTratamiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHistoriaClinica")
    private List<FormatoDeEvolucion> formatoDeEvolucionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHistoriaClinica")
    private List<Tratamiento> tratamientoList;
    @JoinColumn(name = "idTrabajador", referencedColumnName = "idTrabajador")
    @ManyToOne(optional = false)
    private Trabajador idTrabajador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHistoriaClinica")
    private List<Diagnostico> diagnosticoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHistoriaClinica")
    private List<Paciente> pacienteList;

    public HistoriaClinica() {
    }

    public HistoriaClinica(Integer idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public HistoriaClinica(Integer idHistoriaClinica, int idDiagnostico, int idFormatoDeEvolucion, int idTratamiento) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.idDiagnostico = idDiagnostico;
        this.idFormatoDeEvolucion = idFormatoDeEvolucion;
        this.idTratamiento = idTratamiento;
    }

    public Integer getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    public void setIdHistoriaClinica(Integer idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public int getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(int idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public int getIdFormatoDeEvolucion() {
        return idFormatoDeEvolucion;
    }

    public void setIdFormatoDeEvolucion(int idFormatoDeEvolucion) {
        this.idFormatoDeEvolucion = idFormatoDeEvolucion;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    @XmlTransient
    public List<FormatoDeEvolucion> getFormatoDeEvolucionList() {
        return formatoDeEvolucionList;
    }

    public void setFormatoDeEvolucionList(List<FormatoDeEvolucion> formatoDeEvolucionList) {
        this.formatoDeEvolucionList = formatoDeEvolucionList;
    }

    @XmlTransient
    public List<Tratamiento> getTratamientoList() {
        return tratamientoList;
    }

    public void setTratamientoList(List<Tratamiento> tratamientoList) {
        this.tratamientoList = tratamientoList;
    }

    public Trabajador getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Trabajador idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    @XmlTransient
    public List<Diagnostico> getDiagnosticoList() {
        return diagnosticoList;
    }

    public void setDiagnosticoList(List<Diagnostico> diagnosticoList) {
        this.diagnosticoList = diagnosticoList;
    }

    @XmlTransient
    public List<Paciente> getPacienteList() {
        return pacienteList;
    }

    public void setPacienteList(List<Paciente> pacienteList) {
        this.pacienteList = pacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoriaClinica != null ? idHistoriaClinica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoriaClinica)) {
            return false;
        }
        HistoriaClinica other = (HistoriaClinica) object;
        if ((this.idHistoriaClinica == null && other.idHistoriaClinica != null) || (this.idHistoriaClinica != null && !this.idHistoriaClinica.equals(other.idHistoriaClinica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.HistoriaClinica[ idHistoriaClinica=" + idHistoriaClinica + " ]";
    }
    
}
