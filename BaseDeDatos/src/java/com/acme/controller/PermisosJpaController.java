/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.IllegalOrphanException;
import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import com.acme.entities.Permisos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.RolHasPermisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class PermisosJpaController implements Serializable {

    public PermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permisos permisos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permisos.getRolHasPermisosList() == null) {
            permisos.setRolHasPermisosList(new ArrayList<RolHasPermisos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RolHasPermisos> attachedRolHasPermisosList = new ArrayList<RolHasPermisos>();
            for (RolHasPermisos rolHasPermisosListRolHasPermisosToAttach : permisos.getRolHasPermisosList()) {
                rolHasPermisosListRolHasPermisosToAttach = em.getReference(rolHasPermisosListRolHasPermisosToAttach.getClass(), rolHasPermisosListRolHasPermisosToAttach.getIdRolPermisos());
                attachedRolHasPermisosList.add(rolHasPermisosListRolHasPermisosToAttach);
            }
            permisos.setRolHasPermisosList(attachedRolHasPermisosList);
            em.persist(permisos);
            for (RolHasPermisos rolHasPermisosListRolHasPermisos : permisos.getRolHasPermisosList()) {
                Permisos oldPermisoscodpermisoOfRolHasPermisosListRolHasPermisos = rolHasPermisosListRolHasPermisos.getPermisoscodpermiso();
                rolHasPermisosListRolHasPermisos.setPermisoscodpermiso(permisos);
                rolHasPermisosListRolHasPermisos = em.merge(rolHasPermisosListRolHasPermisos);
                if (oldPermisoscodpermisoOfRolHasPermisosListRolHasPermisos != null) {
                    oldPermisoscodpermisoOfRolHasPermisosListRolHasPermisos.getRolHasPermisosList().remove(rolHasPermisosListRolHasPermisos);
                    oldPermisoscodpermisoOfRolHasPermisosListRolHasPermisos = em.merge(oldPermisoscodpermisoOfRolHasPermisosListRolHasPermisos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermisos(permisos.getCodPermiso()) != null) {
                throw new PreexistingEntityException("Permisos " + permisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permisos permisos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos persistentPermisos = em.find(Permisos.class, permisos.getCodPermiso());
            List<RolHasPermisos> rolHasPermisosListOld = persistentPermisos.getRolHasPermisosList();
            List<RolHasPermisos> rolHasPermisosListNew = permisos.getRolHasPermisosList();
            List<String> illegalOrphanMessages = null;
            for (RolHasPermisos rolHasPermisosListOldRolHasPermisos : rolHasPermisosListOld) {
                if (!rolHasPermisosListNew.contains(rolHasPermisosListOldRolHasPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RolHasPermisos " + rolHasPermisosListOldRolHasPermisos + " since its permisoscodpermiso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RolHasPermisos> attachedRolHasPermisosListNew = new ArrayList<RolHasPermisos>();
            for (RolHasPermisos rolHasPermisosListNewRolHasPermisosToAttach : rolHasPermisosListNew) {
                rolHasPermisosListNewRolHasPermisosToAttach = em.getReference(rolHasPermisosListNewRolHasPermisosToAttach.getClass(), rolHasPermisosListNewRolHasPermisosToAttach.getIdRolPermisos());
                attachedRolHasPermisosListNew.add(rolHasPermisosListNewRolHasPermisosToAttach);
            }
            rolHasPermisosListNew = attachedRolHasPermisosListNew;
            permisos.setRolHasPermisosList(rolHasPermisosListNew);
            permisos = em.merge(permisos);
            for (RolHasPermisos rolHasPermisosListNewRolHasPermisos : rolHasPermisosListNew) {
                if (!rolHasPermisosListOld.contains(rolHasPermisosListNewRolHasPermisos)) {
                    Permisos oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos = rolHasPermisosListNewRolHasPermisos.getPermisoscodpermiso();
                    rolHasPermisosListNewRolHasPermisos.setPermisoscodpermiso(permisos);
                    rolHasPermisosListNewRolHasPermisos = em.merge(rolHasPermisosListNewRolHasPermisos);
                    if (oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos != null && !oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos.equals(permisos)) {
                        oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos.getRolHasPermisosList().remove(rolHasPermisosListNewRolHasPermisos);
                        oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos = em.merge(oldPermisoscodpermisoOfRolHasPermisosListNewRolHasPermisos);
                    }
                }
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
                Integer id = permisos.getCodPermiso();
                if (findPermisos(id) == null) {
                    throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos permisos;
            try {
                permisos = em.getReference(Permisos.class, id);
                permisos.getCodPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RolHasPermisos> rolHasPermisosListOrphanCheck = permisos.getRolHasPermisosList();
            for (RolHasPermisos rolHasPermisosListOrphanCheckRolHasPermisos : rolHasPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permisos (" + permisos + ") cannot be destroyed since the RolHasPermisos " + rolHasPermisosListOrphanCheckRolHasPermisos + " in its rolHasPermisosList field has a non-nullable permisoscodpermiso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permisos);
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

    public List<Permisos> findPermisosEntities() {
        return findPermisosEntities(true, -1, -1);
    }

    public List<Permisos> findPermisosEntities(int maxResults, int firstResult) {
        return findPermisosEntities(false, maxResults, firstResult);
    }

    private List<Permisos> findPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permisos.class));
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

    public Permisos findPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permisos> rt = cq.from(Permisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
