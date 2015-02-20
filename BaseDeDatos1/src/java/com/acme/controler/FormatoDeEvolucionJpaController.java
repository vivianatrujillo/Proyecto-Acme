/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controler;

import com.acme.controler.exceptions.NonexistentEntityException;
import com.acme.controler.exceptions.PreexistingEntityException;
import com.acme.controler.exceptions.RollbackFailureException;
import com.acme.entities.FormatoDeEvolucion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Tratamiento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class FormatoDeEvolucionJpaController implements Serializable {

    public FormatoDeEvolucionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FormatoDeEvolucion formatoDeEvolucion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tratamiento idTratamiento = formatoDeEvolucion.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento = em.getReference(idTratamiento.getClass(), idTratamiento.getIdTratamiento());
                formatoDeEvolucion.setIdTratamiento(idTratamiento);
            }
            em.persist(formatoDeEvolucion);
            if (idTratamiento != null) {
                idTratamiento.getFormatoDeEvolucionList().add(formatoDeEvolucion);
                idTratamiento = em.merge(idTratamiento);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFormatoDeEvolucion(formatoDeEvolucion.getIdFormatodeevolucion()) != null) {
                throw new PreexistingEntityException("FormatoDeEvolucion " + formatoDeEvolucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FormatoDeEvolucion formatoDeEvolucion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FormatoDeEvolucion persistentFormatoDeEvolucion = em.find(FormatoDeEvolucion.class, formatoDeEvolucion.getIdFormatodeevolucion());
            Tratamiento idTratamientoOld = persistentFormatoDeEvolucion.getIdTratamiento();
            Tratamiento idTratamientoNew = formatoDeEvolucion.getIdTratamiento();
            if (idTratamientoNew != null) {
                idTratamientoNew = em.getReference(idTratamientoNew.getClass(), idTratamientoNew.getIdTratamiento());
                formatoDeEvolucion.setIdTratamiento(idTratamientoNew);
            }
            formatoDeEvolucion = em.merge(formatoDeEvolucion);
            if (idTratamientoOld != null && !idTratamientoOld.equals(idTratamientoNew)) {
                idTratamientoOld.getFormatoDeEvolucionList().remove(formatoDeEvolucion);
                idTratamientoOld = em.merge(idTratamientoOld);
            }
            if (idTratamientoNew != null && !idTratamientoNew.equals(idTratamientoOld)) {
                idTratamientoNew.getFormatoDeEvolucionList().add(formatoDeEvolucion);
                idTratamientoNew = em.merge(idTratamientoNew);
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
                Integer id = formatoDeEvolucion.getIdFormatodeevolucion();
                if (findFormatoDeEvolucion(id) == null) {
                    throw new NonexistentEntityException("The formatoDeEvolucion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FormatoDeEvolucion formatoDeEvolucion;
            try {
                formatoDeEvolucion = em.getReference(FormatoDeEvolucion.class, id);
                formatoDeEvolucion.getIdFormatodeevolucion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formatoDeEvolucion with id " + id + " no longer exists.", enfe);
            }
            Tratamiento idTratamiento = formatoDeEvolucion.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento.getFormatoDeEvolucionList().remove(formatoDeEvolucion);
                idTratamiento = em.merge(idTratamiento);
            }
            em.remove(formatoDeEvolucion);
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

    public List<FormatoDeEvolucion> findFormatoDeEvolucionEntities() {
        return findFormatoDeEvolucionEntities(true, -1, -1);
    }

    public List<FormatoDeEvolucion> findFormatoDeEvolucionEntities(int maxResults, int firstResult) {
        return findFormatoDeEvolucionEntities(false, maxResults, firstResult);
    }

    private List<FormatoDeEvolucion> findFormatoDeEvolucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FormatoDeEvolucion.class));
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

    public FormatoDeEvolucion findFormatoDeEvolucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FormatoDeEvolucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormatoDeEvolucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FormatoDeEvolucion> rt = cq.from(FormatoDeEvolucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
