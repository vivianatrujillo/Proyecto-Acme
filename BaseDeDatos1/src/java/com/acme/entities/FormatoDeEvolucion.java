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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "formato de evolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoDeEvolucion.findAll", query = "SELECT f FROM FormatoDeEvolucion f"),
    @NamedQuery(name = "FormatoDeEvolucion.findByIdFormatodeevolucion", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.idFormatodeevolucion = :idFormatodeevolucion"),
    @NamedQuery(name = "FormatoDeEvolucion.findByFecha", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FormatoDeEvolucion.findByHora", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.hora = :hora"),
    @NamedQuery(name = "FormatoDeEvolucion.findByLocalizacion", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.localizacion = :localizacion"),
    @NamedQuery(name = "FormatoDeEvolucion.findByCodcita", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.codcita = :codcita")})
public class FormatoDeEvolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFormato_de_evolucion")
    private Integer idFormatodeevolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Localizacion")
    private String localizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cod_cita")
    private int codcita;
    @JoinColumn(name = "idTratamiento", referencedColumnName = "idTratamiento")
    @ManyToOne(optional = false)
    private Tratamiento idTratamiento;

    public FormatoDeEvolucion() {
    }

    public FormatoDeEvolucion(Integer idFormatodeevolucion) {
        this.idFormatodeevolucion = idFormatodeevolucion;
    }

    public FormatoDeEvolucion(Integer idFormatodeevolucion, Date fecha, Date hora, String localizacion, int codcita) {
        this.idFormatodeevolucion = idFormatodeevolucion;
        this.fecha = fecha;
        this.hora = hora;
        this.localizacion = localizacion;
        this.codcita = codcita;
    }

    public Integer getIdFormatodeevolucion() {
        return idFormatodeevolucion;
    }

    public void setIdFormatodeevolucion(Integer idFormatodeevolucion) {
        this.idFormatodeevolucion = idFormatodeevolucion;
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

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public int getCodcita() {
        return codcita;
    }

    public void setCodcita(int codcita) {
        this.codcita = codcita;
    }

    public Tratamiento getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Tratamiento idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormatodeevolucion != null ? idFormatodeevolucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormatoDeEvolucion)) {
            return false;
        }
        FormatoDeEvolucion other = (FormatoDeEvolucion) object;
        if ((this.idFormatodeevolucion == null && other.idFormatodeevolucion != null) || (this.idFormatodeevolucion != null && !this.idFormatodeevolucion.equals(other.idFormatodeevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.FormatoDeEvolucion[ idFormatodeevolucion=" + idFormatodeevolucion + " ]";
    }
    
}
