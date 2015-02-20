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
import com.acme.entities.Tratamiento;
import com.acme.entities.Valoracion;
import com.acme.entities.ValoracionTratamiento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class ValoracionTratamientoJpaController implements Serializable {

    public ValoracionTratamientoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValoracionTratamiento valoracionTratamiento) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tratamiento idTratamiento = valoracionTratamiento.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento = em.getReference(idTratamiento.getClass(), idTratamiento.getIdTratamiento());
                valoracionTratamiento.setIdTratamiento(idTratamiento);
            }
            Valoracion idValoracion = valoracionTratamiento.getIdValoracion();
            if (idValoracion != null) {
                idValoracion = em.getReference(idValoracion.getClass(), idValoracion.getIdValoracion());
                valoracionTratamiento.setIdValoracion(idValoracion);
            }
            em.persist(valoracionTratamiento);
            if (idTratamiento != null) {
                idTratamiento.getValoracionTratamientoList().add(valoracionTratamiento);
                idTratamiento = em.merge(idTratamiento);
            }
            if (idValoracion != null) {
                idValoracion.getValoracionTratamientoList().add(valoracionTratamiento);
                idValoracion = em.merge(idValoracion);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findValoracionTratamiento(valoracionTratamiento.getCodValoracionTratamientocol()) != null) {
                throw new PreexistingEntityException("ValoracionTratamiento " + valoracionTratamiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValoracionTratamiento valoracionTratamiento) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ValoracionTratamiento persistentValoracionTratamiento = em.find(ValoracionTratamiento.class, valoracionTratamiento.getCodValoracionTratamientocol());
            Tratamiento idTratamientoOld = persistentValoracionTratamiento.getIdTratamiento();
            Tratamiento idTratamientoNew = valoracionTratamiento.getIdTratamiento();
            Valoracion idValoracionOld = persistentValoracionTratamiento.getIdValoracion();
            Valoracion idValoracionNew = valoracionTratamiento.getIdValoracion();
            if (idTratamientoNew != null) {
                idTratamientoNew = em.getReference(idTratamientoNew.getClass(), idTratamientoNew.getIdTratamiento());
                valoracionTratamiento.setIdTratamiento(idTratamientoNew);
            }
            if (idValoracionNew != null) {
                idValoracionNew = em.getReference(idValoracionNew.getClass(), idValoracionNew.getIdValoracion());
                valoracionTratamiento.setIdValoracion(idValoracionNew);
            }
            valoracionTratamiento = em.merge(valoracionTratamiento);
            if (idTratamientoOld != null && !idTratamientoOld.equals(idTratamientoNew)) {
                idTratamientoOld.getValoracionTratamientoList().remove(valoracionTratamiento);
                idTratamientoOld = em.merge(idTratamientoOld);
            }
            if (idTratamientoNew != null && !idTratamientoNew.equals(idTratamientoOld)) {
                idTratamientoNew.getValoracionTratamientoList().add(valoracionTratamiento);
                idTratamientoNew = em.merge(idTratamientoNew);
            }
            if (idValoracionOld != null && !idValoracionOld.equals(idValoracionNew)) {
                idValoracionOld.getValoracionTratamientoList().remove(valoracionTratamiento);
                idValoracionOld = em.merge(idValoracionOld);
            }
            if (idValoracionNew != null && !idValoracionNew.equals(idValoracionOld)) {
                idValoracionNew.getValoracionTratamientoList().add(valoracionTratamiento);
                idValoracionNew = em.merge(idValoracionNew);
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
                Integer id = valoracionTratamiento.getCodValoracionTratamientocol();
                if (findValoracionTratamiento(id) == null) {
                    throw new NonexistentEntityException("The valoracionTratamiento with id " + id + " no longer exists.");
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
            ValoracionTratamiento valoracionTratamiento;
            try {
                valoracionTratamiento = em.getReference(ValoracionTratamiento.class, id);
                valoracionTratamiento.getCodValoracionTratamientocol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoracionTratamiento with id " + id + " no longer exists.", enfe);
            }
            Tratamiento idTratamiento = valoracionTratamiento.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento.getValoracionTratamientoList().remove(valoracionTratamiento);
                idTratamiento = em.merge(idTratamiento);
            }
            Valoracion idValoracion = valoracionTratamiento.getIdValoracion();
            if (idValoracion != null) {
                idValoracion.getValoracionTratamientoList().remove(valoracionTratamiento);
                idValoracion = em.merge(idValoracion);
            }
            em.remove(valoracionTratamiento);
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

    public List<ValoracionTratamiento> findValoracionTratamientoEntities() {
        return findValoracionTratamientoEntities(true, -1, -1);
    }

    public List<ValoracionTratamiento> findValoracionTratamientoEntities(int maxResults, int firstResult) {
        return findValoracionTratamientoEntities(false, maxResults, firstResult);
    }

    private List<ValoracionTratamiento> findValoracionTratamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ValoracionTratamiento.class));
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

    public ValoracionTratamiento findValoracionTratamiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoracionTratamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoracionTratamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ValoracionTratamiento> rt = cq.from(ValoracionTratamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
