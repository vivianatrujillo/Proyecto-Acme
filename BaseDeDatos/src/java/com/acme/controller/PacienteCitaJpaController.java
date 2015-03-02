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
import com.acme.entities.Paciente;
import com.acme.entities.Cita;
import com.acme.entities.PacienteCita;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class PacienteCitaJpaController implements Serializable {

    public PacienteCitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacienteCita pacienteCita) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente idPaciente = pacienteCita.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                pacienteCita.setIdPaciente(idPaciente);
            }
            Cita idCita = pacienteCita.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                pacienteCita.setIdCita(idCita);
            }
            em.persist(pacienteCita);
            if (idPaciente != null) {
                idPaciente.getPacienteCitaList().add(pacienteCita);
                idPaciente = em.merge(idPaciente);
            }
            if (idCita != null) {
                idCita.getPacienteCitaList().add(pacienteCita);
                idCita = em.merge(idCita);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPacienteCita(pacienteCita.getIdPacienteCita()) != null) {
                throw new PreexistingEntityException("PacienteCita " + pacienteCita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacienteCita pacienteCita) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteCita persistentPacienteCita = em.find(PacienteCita.class, pacienteCita.getIdPacienteCita());
            Paciente idPacienteOld = persistentPacienteCita.getIdPaciente();
            Paciente idPacienteNew = pacienteCita.getIdPaciente();
            Cita idCitaOld = persistentPacienteCita.getIdCita();
            Cita idCitaNew = pacienteCita.getIdCita();
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                pacienteCita.setIdPaciente(idPacienteNew);
            }
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                pacienteCita.setIdCita(idCitaNew);
            }
            pacienteCita = em.merge(pacienteCita);
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getPacienteCitaList().remove(pacienteCita);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getPacienteCitaList().add(pacienteCita);
                idPacienteNew = em.merge(idPacienteNew);
            }
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getPacienteCitaList().remove(pacienteCita);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getPacienteCitaList().add(pacienteCita);
                idCitaNew = em.merge(idCitaNew);
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
                Integer id = pacienteCita.getIdPacienteCita();
                if (findPacienteCita(id) == null) {
                    throw new NonexistentEntityException("The pacienteCita with id " + id + " no longer exists.");
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
            PacienteCita pacienteCita;
            try {
                pacienteCita = em.getReference(PacienteCita.class, id);
                pacienteCita.getIdPacienteCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacienteCita with id " + id + " no longer exists.", enfe);
            }
            Paciente idPaciente = pacienteCita.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getPacienteCitaList().remove(pacienteCita);
                idPaciente = em.merge(idPaciente);
            }
            Cita idCita = pacienteCita.getIdCita();
            if (idCita != null) {
                idCita.getPacienteCitaList().remove(pacienteCita);
                idCita = em.merge(idCita);
            }
            em.remove(pacienteCita);
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

    public List<PacienteCita> findPacienteCitaEntities() {
        return findPacienteCitaEntities(true, -1, -1);
    }

    public List<PacienteCita> findPacienteCitaEntities(int maxResults, int firstResult) {
        return findPacienteCitaEntities(false, maxResults, firstResult);
    }

    private List<PacienteCita> findPacienteCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacienteCita.class));
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

    public PacienteCita findPacienteCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacienteCita.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacienteCita> rt = cq.from(PacienteCita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
