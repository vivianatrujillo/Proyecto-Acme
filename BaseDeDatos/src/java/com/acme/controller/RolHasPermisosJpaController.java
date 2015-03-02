/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Rol;
import com.acme.entities.Permisos;
import com.acme.entities.RolHasPermisos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class RolHasPermisosJpaController implements Serializable {

    public RolHasPermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RolHasPermisos rolHasPermisos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol rolcodrol = rolHasPermisos.getRolcodrol();
            if (rolcodrol != null) {
                rolcodrol = em.getReference(rolcodrol.getClass(), rolcodrol.getCodRol());
                rolHasPermisos.setRolcodrol(rolcodrol);
            }
            Permisos permisoscodpermiso = rolHasPermisos.getPermisoscodpermiso();
            if (permisoscodpermiso != null) {
                permisoscodpermiso = em.getReference(permisoscodpermiso.getClass(), permisoscodpermiso.getCodPermiso());
                rolHasPermisos.setPermisoscodpermiso(permisoscodpermiso);
            }
            em.persist(rolHasPermisos);
            if (rolcodrol != null) {
                rolcodrol.getRolHasPermisosList().add(rolHasPermisos);
                rolcodrol = em.merge(rolcodrol);
            }
            if (permisoscodpermiso != null) {
                permisoscodpermiso.getRolHasPermisosList().add(rolHasPermisos);
                permisoscodpermiso = em.merge(permisoscodpermiso);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRolHasPermisos(rolHasPermisos.getIdRolPermisos()) != null) {
                throw new PreexistingEntityException("RolHasPermisos " + rolHasPermisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RolHasPermisos rolHasPermisos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RolHasPermisos persistentRolHasPermisos = em.find(RolHasPermisos.class, rolHasPermisos.getIdRolPermisos());
            Rol rolcodrolOld = persistentRolHasPermisos.getRolcodrol();
            Rol rolcodrolNew = rolHasPermisos.getRolcodrol();
            Permisos permisoscodpermisoOld = persistentRolHasPermisos.getPermisoscodpermiso();
            Permisos permisoscodpermisoNew = rolHasPermisos.getPermisoscodpermiso();
            if (rolcodrolNew != null) {
                rolcodrolNew = em.getReference(rolcodrolNew.getClass(), rolcodrolNew.getCodRol());
                rolHasPermisos.setRolcodrol(rolcodrolNew);
            }
            if (permisoscodpermisoNew != null) {
                permisoscodpermisoNew = em.getReference(permisoscodpermisoNew.getClass(), permisoscodpermisoNew.getCodPermiso());
                rolHasPermisos.setPermisoscodpermiso(permisoscodpermisoNew);
            }
            rolHasPermisos = em.merge(rolHasPermisos);
            if (rolcodrolOld != null && !rolcodrolOld.equals(rolcodrolNew)) {
                rolcodrolOld.getRolHasPermisosList().remove(rolHasPermisos);
                rolcodrolOld = em.merge(rolcodrolOld);
            }
            if (rolcodrolNew != null && !rolcodrolNew.equals(rolcodrolOld)) {
                rolcodrolNew.getRolHasPermisosList().add(rolHasPermisos);
                rolcodrolNew = em.merge(rolcodrolNew);
            }
            if (permisoscodpermisoOld != null && !permisoscodpermisoOld.equals(permisoscodpermisoNew)) {
                permisoscodpermisoOld.getRolHasPermisosList().remove(rolHasPermisos);
                permisoscodpermisoOld = em.merge(permisoscodpermisoOld);
            }
            if (permisoscodpermisoNew != null && !permisoscodpermisoNew.equals(permisoscodpermisoOld)) {
                permisoscodpermisoNew.getRolHasPermisosList().add(rolHasPermisos);
                permisoscodpermisoNew = em.merge(permisoscodpermisoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = rolHasPermisos.getIdRolPermisos();
                if (findRolHasPermisos(id) == null) {
                    throw new NonexistentEntityException("The rolHasPermisos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RolHasPermisos rolHasPermisos;
            try {
                rolHasPermisos = em.getReference(RolHasPermisos.class, id);
                rolHasPermisos.getIdRolPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolHasPermisos with id " + id + " no longer exists.", enfe);
            }
            Rol rolcodrol = rolHasPermisos.getRolcodrol();
            if (rolcodrol != null) {
                rolcodrol.getRolHasPermisosList().remove(rolHasPermisos);
                rolcodrol = em.merge(rolcodrol);
            }
            Permisos permisoscodpermiso = rolHasPermisos.getPermisoscodpermiso();
            if (permisoscodpermiso != null) {
                permisoscodpermiso.getRolHasPermisosList().remove(rolHasPermisos);
                permisoscodpermiso = em.merge(permisoscodpermiso);
            }
            em.remove(rolHasPermisos);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RolHasPermisos> findRolHasPermisosEntities() {
        return findRolHasPermisosEntities(true, -1, -1);
    }

    public List<RolHasPermisos> findRolHasPermisosEntities(int maxResults, int firstResult) {
        return findRolHasPermisosEntities(false, maxResults, firstResult);
    }

    private List<RolHasPermisos> findRolHasPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RolHasPermisos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RolHasPermisos findRolHasPermisos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RolHasPermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolHasPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RolHasPermisos> rt = cq.from(RolHasPermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
