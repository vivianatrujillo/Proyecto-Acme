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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Referencia.findAll", query = "SELECT r FROM Referencia r"),
    @NamedQuery(name = "Referencia.findByIdReferencia", query = "SELECT r FROM Referencia r WHERE r.idReferencia = :idReferencia"),
    @NamedQuery(name = "Referencia.findByReferenciaFamiliar", query = "SELECT r FROM Referencia r WHERE r.referenciaFamiliar = :referenciaFamiliar")})
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idReferencia")
    private Integer idReferencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "referencia_familiar")
    private String referenciaFamiliar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReferencia")
    private List<PacienteReferencia> pacienteReferenciaList;

    public Referencia() {
    }

    public Referencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Referencia(Integer idReferencia, String referenciaFamiliar) {
        this.idReferencia = idReferencia;
        this.referenciaFamiliar = referenciaFamiliar;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getReferenciaFamiliar() {
        return referenciaFamiliar;
    }

    public void setReferenciaFamiliar(String referenciaFamiliar) {
        this.referenciaFamiliar = referenciaFamiliar;
    }

    @XmlTransient
    public List<PacienteReferencia> getPacienteReferenciaList() {
        return pacienteReferenciaList;
    }

    public void setPacienteReferenciaList(List<PacienteReferencia> pacienteReferenciaList) {
        this.pacienteReferenciaList = pacienteReferenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referencia)) {
            return false;
        }
        Referencia other = (Referencia) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getReferenciaFamiliar() ;
    }
    
}
