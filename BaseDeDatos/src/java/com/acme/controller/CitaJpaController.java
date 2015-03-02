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
import com.acme.entities.Cita;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.PacienteCita;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cita.getPacienteCitaList() == null) {
            cita.setPacienteCitaList(new ArrayList<PacienteCita>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PacienteCita> attachedPacienteCitaList = new ArrayList<PacienteCita>();
            for (PacienteCita pacienteCitaListPacienteCitaToAttach : cita.getPacienteCitaList()) {
                pacienteCitaListPacienteCitaToAttach = em.getReference(pacienteCitaListPacienteCitaToAttach.getClass(), pacienteCitaListPacienteCitaToAttach.getIdPacienteCita());
                attachedPacienteCitaList.add(pacienteCitaListPacienteCitaToAttach);
            }
            cita.setPacienteCitaList(attachedPacienteCitaList);
            em.persist(cita);
            for (PacienteCita pacienteCitaListPacienteCita : cita.getPacienteCitaList()) {
                Cita oldIdCitaOfPacienteCitaListPacienteCita = pacienteCitaListPacienteCita.getIdCita();
                pacienteCitaListPacienteCita.setIdCita(cita);
                pacienteCitaListPacienteCita = em.merge(pacienteCitaListPacienteCita);
                if (oldIdCitaOfPacienteCitaListPacienteCita != null) {
                    oldIdCitaOfPacienteCitaListPacienteCita.getPacienteCitaList().remove(pacienteCitaListPacienteCita);
                    oldIdCitaOfPacienteCitaListPacienteCita = em.merge(oldIdCitaOfPacienteCitaListPacienteCita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCita(cita.getIdCita()) != null) {
                throw new PreexistingEntityException("Cita " + cita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita persistentCita = em.find(Cita.class, cita.getIdCita());
            List<PacienteCita> pacienteCitaListOld = persistentCita.getPacienteCitaList();
            List<PacienteCita> pacienteCitaListNew = cita.getPacienteCitaList();
            List<String> illegalOrphanMessages = null;
            for (PacienteCita pacienteCitaListOldPacienteCita : pacienteCitaListOld) {
                if (!pacienteCitaListNew.contains(pacienteCitaListOldPacienteCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteCita " + pacienteCitaListOldPacienteCita + " since its idCita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PacienteCita> attachedPacienteCitaListNew = new ArrayList<PacienteCita>();
            for (PacienteCita pacienteCitaListNewPacienteCitaToAttach : pacienteCitaListNew) {
                pacienteCitaListNewPacienteCitaToAttach = em.getReference(pacienteCitaListNewPacienteCitaToAttach.getClass(), pacienteCitaListNewPacienteCitaToAttach.getIdPacienteCita());
                attachedPacienteCitaListNew.add(pacienteCitaListNewPacienteCitaToAttach);
            }
            pacienteCitaListNew = attachedPacienteCitaListNew;
            cita.setPacienteCitaList(pacienteCitaListNew);
            cita = em.merge(cita);
            for (PacienteCita pacienteCitaListNewPacienteCita : pacienteCitaListNew) {
                if (!pacienteCitaListOld.contains(pacienteCitaListNewPacienteCita)) {
                    Cita oldIdCitaOfPacienteCitaListNewPacienteCita = pacienteCitaListNewPacienteCita.getIdCita();
                    pacienteCitaListNewPacienteCita.setIdCita(cita);
                    pacienteCitaListNewPacienteCita = em.merge(pacienteCitaListNewPacienteCita);
                    if (oldIdCitaOfPacienteCitaListNewPacienteCita != null && !oldIdCitaOfPacienteCitaListNewPacienteCita.equals(cita)) {
                        oldIdCitaOfPacienteCitaListNewPacienteCita.getPacienteCitaList().remove(pacienteCitaListNewPacienteCita);
                        oldIdCitaOfPacienteCitaListNewPacienteCita = em.merge(oldIdCitaOfPacienteCitaListNewPacienteCita);
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
                Integer id = cita.getIdCita();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteCita> pacienteCitaListOrphanCheck = cita.getPacienteCitaList();
            for (PacienteCita pacienteCitaListOrphanCheckPacienteCita : pacienteCitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cita (" + cita + ") cannot be destroyed since the PacienteCita " + pacienteCitaListOrphanCheckPacienteCita + " in its pacienteCitaList field has a non-nullable idCita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cita);
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

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
