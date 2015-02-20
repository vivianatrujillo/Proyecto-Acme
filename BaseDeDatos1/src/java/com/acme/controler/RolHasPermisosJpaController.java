/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controler;

import com.acme.controler.exceptions.NonexistentEntityException;
import com.acme.controler.exceptions.PreexistingEntityException;
import com.acme.controler.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Permisos;
import com.acme.entities.Rol;
import com.acme.entities.RolHasPermisos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
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
            Permisos codPermiso = rolHasPermisos.getCodPermiso();
            if (codPermiso != null) {
                codPermiso = em.getReference(codPermiso.getClass(), codPermiso.getCodPermiso());
                rolHasPermisos.setCodPermiso(codPermiso);
            }
            Rol codRol = rolHasPermisos.getCodRol();
            if (codRol != null) {
                codRol = em.getReference(codRol.getClass(), codRol.getCodRol());
                rolHasPermisos.setCodRol(codRol);
            }
            em.persist(rolHasPermisos);
            if (codPermiso != null) {
                codPermiso.getRolHasPermisosList().add(rolHasPermisos);
                codPermiso = em.merge(codPermiso);
            }
            if (codRol != null) {
                codRol.getRolHasPermisosList().add(rolHasPermisos);
                codRol = em.merge(codRol);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRolHasPermisos(rolHasPermisos.getRolPermisos()) != null) {
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
            RolHasPermisos persistentRolHasPermisos = em.find(RolHasPermisos.class, rolHasPermisos.getRolPermisos());
            Permisos codPermisoOld = persistentRolHasPermisos.getCodPermiso();
            Permisos codPermisoNew = rolHasPermisos.getCodPermiso();
            Rol codRolOld = persistentRolHasPermisos.getCodRol();
            Rol codRolNew = rolHasPermisos.getCodRol();
            if (codPermisoNew != null) {
                codPermisoNew = em.getReference(codPermisoNew.getClass(), codPermisoNew.getCodPermiso());
                rolHasPermisos.setCodPermiso(codPermisoNew);
            }
            if (codRolNew != null) {
                codRolNew = em.getReference(codRolNew.getClass(), codRolNew.getCodRol());
                rolHasPermisos.setCodRol(codRolNew);
            }
            rolHasPermisos = em.merge(rolHasPermisos);
            if (codPermisoOld != null && !codPermisoOld.equals(codPermisoNew)) {
                codPermisoOld.getRolHasPermisosList().remove(rolHasPermisos);
                codPermisoOld = em.merge(codPermisoOld);
            }
            if (codPermisoNew != null && !codPermisoNew.equals(codPermisoOld)) {
                codPermisoNew.getRolHasPermisosList().add(rolHasPermisos);
                codPermisoNew = em.merge(codPermisoNew);
            }
            if (codRolOld != null && !codRolOld.equals(codRolNew)) {
                codRolOld.getRolHasPermisosList().remove(rolHasPermisos);
                codRolOld = em.merge(codRolOld);
            }
            if (codRolNew != null && !codRolNew.equals(codRolOld)) {
                codRolNew.getRolHasPermisosList().add(rolHasPermisos);
                codRolNew = em.merge(codRolNew);
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
                String id = rolHasPermisos.getRolPermisos();
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
                rolHasPermisos.getRolPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolHasPermisos with id " + id + " no longer exists.", enfe);
            }
            Permisos codPermiso = rolHasPermisos.getCodPermiso();
            if (codPermiso != null) {
                codPermiso.getRolHasPermisosList().remove(rolHasPermisos);
                codPermiso = em.merge(codPermiso);
            }
            Rol codRol = rolHasPermisos.getCodRol();
            if (codRol != null) {
                codRol.getRolHasPermisosList().remove(rolHasPermisos);
                codRol = em.merge(codRol);
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
