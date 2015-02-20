/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controler;

import com.acme.controler.exceptions.IllegalOrphanException;
import com.acme.controler.exceptions.NonexistentEntityException;
import com.acme.controler.exceptions.PreexistingEntityException;
import com.acme.controler.exceptions.RollbackFailureException;
import com.acme.entities.Rol;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Trabajador;
import com.acme.entities.RolHasPermisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rol.getRolHasPermisosList() == null) {
            rol.setRolHasPermisosList(new ArrayList<RolHasPermisos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trabajador idTrabajador = rol.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador = em.getReference(idTrabajador.getClass(), idTrabajador.getIdTrabajador());
                rol.setIdTrabajador(idTrabajador);
            }
            List<RolHasPermisos> attachedRolHasPermisosList = new ArrayList<RolHasPermisos>();
            for (RolHasPermisos rolHasPermisosListRolHasPermisosToAttach : rol.getRolHasPermisosList()) {
                rolHasPermisosListRolHasPermisosToAttach = em.getReference(rolHasPermisosListRolHasPermisosToAttach.getClass(), rolHasPermisosListRolHasPermisosToAttach.getRolPermisos());
                attachedRolHasPermisosList.add(rolHasPermisosListRolHasPermisosToAttach);
            }
            rol.setRolHasPermisosList(attachedRolHasPermisosList);
            em.persist(rol);
            if (idTrabajador != null) {
                idTrabajador.getRolList().add(rol);
                idTrabajador = em.merge(idTrabajador);
            }
            for (RolHasPermisos rolHasPermisosListRolHasPermisos : rol.getRolHasPermisosList()) {
                Rol oldCodRolOfRolHasPermisosListRolHasPermisos = rolHasPermisosListRolHasPermisos.getCodRol();
                rolHasPermisosListRolHasPermisos.setCodRol(rol);
                rolHasPermisosListRolHasPermisos = em.merge(rolHasPermisosListRolHasPermisos);
                if (oldCodRolOfRolHasPermisosListRolHasPermisos != null) {
                    oldCodRolOfRolHasPermisosListRolHasPermisos.getRolHasPermisosList().remove(rolHasPermisosListRolHasPermisos);
                    oldCodRolOfRolHasPermisosListRolHasPermisos = em.merge(oldCodRolOfRolHasPermisosListRolHasPermisos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRol(rol.getCodRol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getCodRol());
            Trabajador idTrabajadorOld = persistentRol.getIdTrabajador();
            Trabajador idTrabajadorNew = rol.getIdTrabajador();
            List<RolHasPermisos> rolHasPermisosListOld = persistentRol.getRolHasPermisosList();
            List<RolHasPermisos> rolHasPermisosListNew = rol.getRolHasPermisosList();
            List<String> illegalOrphanMessages = null;
            for (RolHasPermisos rolHasPermisosListOldRolHasPermisos : rolHasPermisosListOld) {
                if (!rolHasPermisosListNew.contains(rolHasPermisosListOldRolHasPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RolHasPermisos " + rolHasPermisosListOldRolHasPermisos + " since its codRol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTrabajadorNew != null) {
                idTrabajadorNew = em.getReference(idTrabajadorNew.getClass(), idTrabajadorNew.getIdTrabajador());
                rol.setIdTrabajador(idTrabajadorNew);
            }
            List<RolHasPermisos> attachedRolHasPermisosListNew = new ArrayList<RolHasPermisos>();
            for (RolHasPermisos rolHasPermisosListNewRolHasPermisosToAttach : rolHasPermisosListNew) {
                rolHasPermisosListNewRolHasPermisosToAttach = em.getReference(rolHasPermisosListNewRolHasPermisosToAttach.getClass(), rolHasPermisosListNewRolHasPermisosToAttach.getRolPermisos());
                attachedRolHasPermisosListNew.add(rolHasPermisosListNewRolHasPermisosToAttach);
            }
            rolHasPermisosListNew = attachedRolHasPermisosListNew;
            rol.setRolHasPermisosList(rolHasPermisosListNew);
            rol = em.merge(rol);
            if (idTrabajadorOld != null && !idTrabajadorOld.equals(idTrabajadorNew)) {
                idTrabajadorOld.getRolList().remove(rol);
                idTrabajadorOld = em.merge(idTrabajadorOld);
            }
            if (idTrabajadorNew != null && !idTrabajadorNew.equals(idTrabajadorOld)) {
                idTrabajadorNew.getRolList().add(rol);
                idTrabajadorNew = em.merge(idTrabajadorNew);
            }
            for (RolHasPermisos rolHasPermisosListNewRolHasPermisos : rolHasPermisosListNew) {
                if (!rolHasPermisosListOld.contains(rolHasPermisosListNewRolHasPermisos)) {
                    Rol oldCodRolOfRolHasPermisosListNewRolHasPermisos = rolHasPermisosListNewRolHasPermisos.getCodRol();
                    rolHasPermisosListNewRolHasPermisos.setCodRol(rol);
                    rolHasPermisosListNewRolHasPermisos = em.merge(rolHasPermisosListNewRolHasPermisos);
                    if (oldCodRolOfRolHasPermisosListNewRolHasPermisos != null && !oldCodRolOfRolHasPermisosListNewRolHasPermisos.equals(rol)) {
                        oldCodRolOfRolHasPermisosListNewRolHasPermisos.getRolHasPermisosList().remove(rolHasPermisosListNewRolHasPermisos);
                        oldCodRolOfRolHasPermisosListNewRolHasPermisos = em.merge(oldCodRolOfRolHasPermisosListNewRolHasPermisos);
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
                Integer id = rol.getCodRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getCodRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RolHasPermisos> rolHasPermisosListOrphanCheck = rol.getRolHasPermisosList();
            for (RolHasPermisos rolHasPermisosListOrphanCheckRolHasPermisos : rolHasPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the RolHasPermisos " + rolHasPermisosListOrphanCheckRolHasPermisos + " in its rolHasPermisosList field has a non-nullable codRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Trabajador idTrabajador = rol.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador.getRolList().remove(rol);
                idTrabajador = em.merge(idTrabajador);
            }
            em.remove(rol);
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

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
