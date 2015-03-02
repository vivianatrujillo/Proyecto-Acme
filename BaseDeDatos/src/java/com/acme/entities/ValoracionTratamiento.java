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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "valoracion_tratamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoracionTratamiento.findAll", query = "SELECT v FROM ValoracionTratamiento v"),
    @NamedQuery(name = "ValoracionTratamiento.findByCodValoracionTratamientocol", query = "SELECT v FROM ValoracionTratamiento v WHERE v.codValoracionTratamientocol = :codValoracionTratamientocol")})
public class ValoracionTratamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_Valoracion_Tratamientocol")
    private Integer codValoracionTratamientocol;
    @JoinColumn(name = "idValoracion", referencedColumnName = "idValoracion")
    @ManyToOne(optional = false)
    private Valoracion idValoracion;
    @JoinColumn(name = "idTratamiento", referencedColumnName = "idTratamiento")
    @ManyToOne(optional = false)
    private Tratamiento idTratamiento;

    public ValoracionTratamiento() {
    }

    public ValoracionTratamiento(Integer codValoracionTratamientocol) {
        this.codValoracionTratamientocol = codValoracionTratamientocol;
    }

    public Integer getCodValoracionTratamientocol() {
        return codValoracionTratamientocol;
    }

    public void setCodValoracionTratamientocol(Integer codValoracionTratamientocol) {
        this.codValoracionTratamientocol = codValoracionTratamientocol;
    }

    public Valoracion getIdValoracion() {
        return idValoracion;
    }

    public void setIdValoracion(Valoracion idValoracion) {
        this.idValoracion = idValoracion;
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
        hash += (codValoracionTratamientocol != null ? codValoracionTratamientocol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoracionTratamiento)) {
            return false;
        }
        ValoracionTratamiento other = (ValoracionTratamiento) object;
        if ((this.codValoracionTratamientocol == null && other.codValoracionTratamientocol != null) || (this.codValoracionTratamientocol != null && !this.codValoracionTratamientocol.equals(other.codValoracionTratamientocol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.ValoracionTratamiento[ codValoracionTratamientocol=" + codValoracionTratamientocol + " ]";
    }
    
}
