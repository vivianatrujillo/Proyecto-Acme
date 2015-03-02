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
@Table(name = "permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p"),
    @NamedQuery(name = "Permisos.findByCodPermiso", query = "SELECT p FROM Permisos p WHERE p.codPermiso = :codPermiso"),
    @NamedQuery(name = "Permisos.findByNombre", query = "SELECT p FROM Permisos p WHERE p.nombre = :nombre")})
public class Permisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_permiso")
    private Integer codPermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permisoscodpermiso")
    private List<RolHasPermisos> rolHasPermisosList;

    public Permisos() {
    }

    public Permisos(Integer codPermiso) {
        this.codPermiso = codPermiso;
    }

    public Permisos(Integer codPermiso, String nombre) {
        this.codPermiso = codPermiso;
        this.nombre = nombre;
    }

    public Integer getCodPermiso() {
        return codPermiso;
    }

    public void setCodPermiso(Integer codPermiso) {
        this.codPermiso = codPermiso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<RolHasPermisos> getRolHasPermisosList() {
        return rolHasPermisosList;
    }

    public void setRolHasPermisosList(List<RolHasPermisos> rolHasPermisosList) {
        this.rolHasPermisosList = rolHasPermisosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPermiso != null ? codPermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.codPermiso == null && other.codPermiso != null) || (this.codPermiso != null && !this.codPermiso.equals(other.codPermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
}
