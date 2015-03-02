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
 * @author usuario
 */
@Entity
@Table(name = "rol_has_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolHasPermisos.findAll", query = "SELECT r FROM RolHasPermisos r"),
    @NamedQuery(name = "RolHasPermisos.findByIdRolPermisos", query = "SELECT r FROM RolHasPermisos r WHERE r.idRolPermisos = :idRolPermisos")})
public class RolHasPermisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "idRol_Permisos")
    private String idRolPermisos;
    @JoinColumn(name = "Rol_cod_rol", referencedColumnName = "cod_rol")
    @ManyToOne(optional = false)
    private Rol rolcodrol;
    @JoinColumn(name = "Permisos_cod_permiso", referencedColumnName = "cod_permiso")
    @ManyToOne(optional = false)
    private Permisos permisoscodpermiso;

    public RolHasPermisos() {
    }

    public RolHasPermisos(String idRolPermisos) {
        this.idRolPermisos = idRolPermisos;
    }

    public String getIdRolPermisos() {
        return idRolPermisos;
    }

    public void setIdRolPermisos(String idRolPermisos) {
        this.idRolPermisos = idRolPermisos;
    }

    public Rol getRolcodrol() {
        return rolcodrol;
    }

    public void setRolcodrol(Rol rolcodrol) {
        this.rolcodrol = rolcodrol;
    }

    public Permisos getPermisoscodpermiso() {
        return permisoscodpermiso;
    }

    public void setPermisoscodpermiso(Permisos permisoscodpermiso) {
        this.permisoscodpermiso = permisoscodpermiso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRolPermisos != null ? idRolPermisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolHasPermisos)) {
            return false;
        }
        RolHasPermisos other = (RolHasPermisos) object;
        if ((this.idRolPermisos == null && other.idRolPermisos != null) || (this.idRolPermisos != null && !this.idRolPermisos.equals(other.idRolPermisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getIdRolPermisos();
    }
    
}
