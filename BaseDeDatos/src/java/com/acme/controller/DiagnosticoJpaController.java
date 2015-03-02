/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import com.acme.entities.Diagnostico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Valoracion;
import com.acme.entities.HistoriaClinica;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class DiagnosticoJpaController implements Serializable {

    public DiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diagnostico diagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Valoracion idValoracion = diagnostico.getIdValoracion();
            if (idValoracion != null) {
                idValoracion = em.getReference(idValoracion.getClass(), idValoracion.getIdValoracion());
                diagnostico.setIdValoracion(idValoracion);
            }
            HistoriaClinica idHistoriaClinica = diagnostico.getIdHistoriaClinica();
            if (idHistoriaClinica != null) {
                idHistoriaClinica = em.getReference(idHistoriaClinica.getClass(), idHistoriaClinica.getIdHistoriaClinica());
                diagnostico.setIdHistoriaClinica(idHistoriaClinica);
            }
            em.persist(diagnostico);
            if (idValoracion != null) {
                idValoracion.getDiagnosticoList().add(diagnostico);
                idValoracion = em.merge(idValoracion);
            }
            if (idHistoriaClinica != null) {
                idHistoriaClinica.getDiagnosticoList().add(diagnostico);
                idHistoriaClinica = em.merge(idHistoriaClinica);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDiagnostico(diagnostico.getIdDiagnostico()) != null) {
                throw new PreexistingEntityException("Diagnostico " + diagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diagnostico diagnostico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diagnostico persistentDiagnostico = em.find(Diagnostico.class, diagnostico.getIdDiagnostico());
            Valoracion idValoracionOld = persistentDiagnostico.getIdValoracion();
            Valoracion idValoracionNew = diagnostico.getIdValoracion();
            HistoriaClinica idHistoriaClinicaOld = persistentDiagnostico.getIdHistoriaClinica();
            HistoriaClinica idHistoriaClinicaNew = diagnostico.getIdHistoriaClinica();
            if (idValoracionNew != null) {
                idValoracionNew = em.getReference(idValoracionNew.getClass(), idValoracionNew.getIdValoracion());
                diagnostico.setIdValoracion(idValoracionNew);
            }
            if (idHistoriaClinicaNew != null) {
                idHistoriaClinicaNew = em.getReference(idHistoriaClinicaNew.getClass(), idHistoriaClinicaNew.getIdHistoriaClinica());
                diagnostico.setIdHistoriaClinica(idHistoriaClinicaNew);
            }
            diagnostico = em.merge(diagnostico);
            if (idValoracionOld != null && !idValoracionOld.equals(idValoracionNew)) {
                idValoracionOld.getDiagnosticoList().remove(diagnostico);
                idValoracionOld = em.merge(idValoracionOld);
            }
            if (idValoracionNew != null && !idValoracionNew.equals(idValoracionOld)) {
                idValoracionNew.getDiagnosticoList().add(diagnostico);
                idValoracionNew = em.merge(idValoracionNew);
            }
            if (idHistoriaClinicaOld != null && !idHistoriaClinicaOld.equals(idHistoriaClinicaNew)) {
                idHistoriaClinicaOld.getDiagnosticoList().remove(diagnostico);
                idHistoriaClinicaOld = em.merge(idHistoriaClinicaOld);
            }
            if (idHistoriaClinicaNew != null && !idHistoriaClinicaNew.equals(idHistoriaClinicaOld)) {
                idHistoriaClinicaNew.getDiagnosticoList().add(diagnostico);
                idHistoriaClinicaNew = em.merge(idHistoriaClinicaNew);
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
                Integer id = diagnostico.getIdDiagnostico();
                if (findDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.");
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
            Diagnostico diagnostico;
            try {
                diagnostico = em.getReference(Diagnostico.class, id);
                diagnostico.getIdDiagnostico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.", enfe);
            }
            Valoracion idValoracion = diagnostico.getIdValoracion();
            if (idValoracion != null) {
                idValoracion.getDiagnosticoList().remove(diagnostico);
                idValoracion = em.merge(idValoracion);
            }
            HistoriaClinica idHistoriaClinica = diagnostico.getIdHistoriaClinica();
            if (idHistoriaClinica != null) {
                idHistoriaClinica.getDiagnosticoList().remove(diagnostico);
                idHistoriaClinica = em.merge(idHistoriaClinica);
            }
            em.remove(diagnostico);
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

    public List<Diagnostico> findDiagnosticoEntities() {
        return findDiagnosticoEntities(true, -1, -1);
    }

    public List<Diagnostico> findDiagnosticoEntities(int maxResults, int firstResult) {
        return findDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Diagnostico> findDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diagnostico.class));
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

    public Diagnostico findDiagnostico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diagnostico> rt = cq.from(Diagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
