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
import com.acme.entities.PacienteReferencia;
import com.acme.entities.Referencia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class PacienteReferenciaJpaController implements Serializable {

    public PacienteReferenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacienteReferencia pacienteReferencia) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente idPaciente = pacienteReferencia.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                pacienteReferencia.setIdPaciente(idPaciente);
            }
            Referencia idReferencia = pacienteReferencia.getIdReferencia();
            if (idReferencia != null) {
                idReferencia = em.getReference(idReferencia.getClass(), idReferencia.getIdReferencia());
                pacienteReferencia.setIdReferencia(idReferencia);
            }
            em.persist(pacienteReferencia);
            if (idPaciente != null) {
                idPaciente.getPacienteReferenciaList().add(pacienteReferencia);
                idPaciente = em.merge(idPaciente);
            }
            if (idReferencia != null) {
                idReferencia.getPacienteReferenciaList().add(pacienteReferencia);
                idReferencia = em.merge(idReferencia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPacienteReferencia(pacienteReferencia.getCodPacienteReferencia()) != null) {
                throw new PreexistingEntityException("PacienteReferencia " + pacienteReferencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacienteReferencia pacienteReferencia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteReferencia persistentPacienteReferencia = em.find(PacienteReferencia.class, pacienteReferencia.getCodPacienteReferencia());
            Paciente idPacienteOld = persistentPacienteReferencia.getIdPaciente();
            Paciente idPacienteNew = pacienteReferencia.getIdPaciente();
            Referencia idReferenciaOld = persistentPacienteReferencia.getIdReferencia();
            Referencia idReferenciaNew = pacienteReferencia.getIdReferencia();
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                pacienteReferencia.setIdPaciente(idPacienteNew);
            }
            if (idReferenciaNew != null) {
                idReferenciaNew = em.getReference(idReferenciaNew.getClass(), idReferenciaNew.getIdReferencia());
                pacienteReferencia.setIdReferencia(idReferenciaNew);
            }
            pacienteReferencia = em.merge(pacienteReferencia);
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getPacienteReferenciaList().remove(pacienteReferencia);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getPacienteReferenciaList().add(pacienteReferencia);
                idPacienteNew = em.merge(idPacienteNew);
            }
            if (idReferenciaOld != null && !idReferenciaOld.equals(idReferenciaNew)) {
                idReferenciaOld.getPacienteReferenciaList().remove(pacienteReferencia);
                idReferenciaOld = em.merge(idReferenciaOld);
            }
            if (idReferenciaNew != null && !idReferenciaNew.equals(idReferenciaOld)) {
                idReferenciaNew.getPacienteReferenciaList().add(pacienteReferencia);
                idReferenciaNew = em.merge(idReferenciaNew);
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
                String id = pacienteReferencia.getCodPacienteReferencia();
                if (findPacienteReferencia(id) == null) {
                    throw new NonexistentEntityException("The pacienteReferencia with id " + id + " no longer exists.");
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
            PacienteReferencia pacienteReferencia;
            try {
                pacienteReferencia = em.getReference(PacienteReferencia.class, id);
                pacienteReferencia.getCodPacienteReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacienteReferencia with id " + id + " no longer exists.", enfe);
            }
            Paciente idPaciente = pacienteReferencia.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getPacienteReferenciaList().remove(pacienteReferencia);
                idPaciente = em.merge(idPaciente);
            }
            Referencia idReferencia = pacienteReferencia.getIdReferencia();
            if (idReferencia != null) {
                idReferencia.getPacienteReferenciaList().remove(pacienteReferencia);
                idReferencia = em.merge(idReferencia);
            }
            em.remove(pacienteReferencia);
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

    public List<PacienteReferencia> findPacienteReferenciaEntities() {
        return findPacienteReferenciaEntities(true, -1, -1);
    }

    public List<PacienteReferencia> findPacienteReferenciaEntities(int maxResults, int firstResult) {
        return findPacienteReferenciaEntities(false, maxResults, firstResult);
    }

    private List<PacienteReferencia> findPacienteReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacienteReferencia.class));
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

    public PacienteReferencia findPacienteReferencia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacienteReferencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacienteReferencia> rt = cq.from(PacienteReferencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
