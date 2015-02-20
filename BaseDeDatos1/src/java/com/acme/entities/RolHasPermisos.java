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
@Table(name = "rol_has_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolHasPermisos.findAll", query = "SELECT r FROM RolHasPermisos r"),
    @NamedQuery(name = "RolHasPermisos.findByRolPermisos", query = "SELECT r FROM RolHasPermisos r WHERE r.rolPermisos = :rolPermisos")})
public class RolHasPermisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Rol_Permisos")
    private String rolPermisos;
    @JoinColumn(name = "cod_permiso", referencedColumnName = "cod_permiso")
    @ManyToOne(optional = false)
    private Permisos codPermiso;
    @JoinColumn(name = "cod_rol", referencedColumnName = "cod_rol")
    @ManyToOne(optional = false)
    private Rol codRol;

    public RolHasPermisos() {
    }

    public RolHasPermisos(String rolPermisos) {
        this.rolPermisos = rolPermisos;
    }

    public String getRolPermisos() {
        return rolPermisos;
    }

    public void setRolPermisos(String rolPermisos) {
        this.rolPermisos = rolPermisos;
    }

    public Permisos getCodPermiso() {
        return codPermiso;
    }

    public void setCodPermiso(Permisos codPermiso) {
        this.codPermiso = codPermiso;
    }

    public Rol getCodRol() {
        return codRol;
    }

    public void setCodRol(Rol codRol) {
        this.codRol = codRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolPermisos != null ? rolPermisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolHasPermisos)) {
            return false;
        }
        RolHasPermisos other = (RolHasPermisos) object;
        if ((this.rolPermisos == null && other.rolPermisos != null) || (this.rolPermisos != null && !this.rolPermisos.equals(other.rolPermisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.acme.entities.RolHasPermisos[ rolPermisos=" + rolPermisos + " ]";
    }
    
}
