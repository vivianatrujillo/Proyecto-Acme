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
import com.acme.entities.Enfermedad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.PacienteEnfermedadEnfermedad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class EnfermedadJpaController implements Serializable {

    public EnfermedadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Enfermedad enfermedad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (enfermedad.getPacienteEnfermedadEnfermedadList() == null) {
            enfermedad.setPacienteEnfermedadEnfermedadList(new ArrayList<PacienteEnfermedadEnfermedad>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PacienteEnfermedadEnfermedad> attachedPacienteEnfermedadEnfermedadList = new ArrayList<PacienteEnfermedadEnfermedad>();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach : enfermedad.getPacienteEnfermedadEnfermedadList()) {
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach = em.getReference(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach.getClass(), pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach.getPacienteEnfermedadEnfermedadPK());
                attachedPacienteEnfermedadEnfermedadList.add(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach);
            }
            enfermedad.setPacienteEnfermedadEnfermedadList(attachedPacienteEnfermedadEnfermedadList);
            em.persist(enfermedad);
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad : enfermedad.getPacienteEnfermedadEnfermedadList()) {
                Enfermedad oldEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.getEnfermedad();
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.setEnfermedad(enfermedad);
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = em.merge(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                if (oldEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad != null) {
                    oldEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                    oldEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = em.merge(oldEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEnfermedad(enfermedad.getIdEnfermedad()) != null) {
                throw new PreexistingEntityException("Enfermedad " + enfermedad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Enfermedad enfermedad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Enfermedad persistentEnfermedad = em.find(Enfermedad.class, enfermedad.getIdEnfermedad());
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListOld = persistentEnfermedad.getPacienteEnfermedadEnfermedadList();
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListNew = enfermedad.getPacienteEnfermedadEnfermedadList();
            List<String> illegalOrphanMessages = null;
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListOld) {
                if (!pacienteEnfermedadEnfermedadListNew.contains(pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteEnfermedadEnfermedad " + pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad + " since its enfermedad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PacienteEnfermedadEnfermedad> attachedPacienteEnfermedadEnfermedadListNew = new ArrayList<PacienteEnfermedadEnfermedad>();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach : pacienteEnfermedadEnfermedadListNew) {
                pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach = em.getReference(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach.getClass(), pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach.getPacienteEnfermedadEnfermedadPK());
                attachedPacienteEnfermedadEnfermedadListNew.add(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach);
            }
            pacienteEnfermedadEnfermedadListNew = attachedPacienteEnfermedadEnfermedadListNew;
            enfermedad.setPacienteEnfermedadEnfermedadList(pacienteEnfermedadEnfermedadListNew);
            enfermedad = em.merge(enfermedad);
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListNew) {
                if (!pacienteEnfermedadEnfermedadListOld.contains(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad)) {
                    Enfermedad oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.getEnfermedad();
                    pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.setEnfermedad(enfermedad);
                    pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = em.merge(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
                    if (oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad != null && !oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.equals(enfermedad)) {
                        oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
                        oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = em.merge(oldEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
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
                Integer id = enfermedad.getIdEnfermedad();
                if (findEnfermedad(id) == null) {
                    throw new NonexistentEntityException("The enfermedad with id " + id + " no longer exists.");
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
            Enfermedad enfermedad;
            try {
                enfermedad = em.getReference(Enfermedad.class, id);
                enfermedad.getIdEnfermedad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enfermedad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListOrphanCheck = enfermedad.getPacienteEnfermedadEnfermedadList();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListOrphanCheckPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Enfermedad (" + enfermedad + ") cannot be destroyed since the PacienteEnfermedadEnfermedad " + pacienteEnfermedadEnfermedadListOrphanCheckPacienteEnfermedadEnfermedad + " in its pacienteEnfermedadEnfermedadList field has a non-nullable enfermedad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(enfermedad);
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

    public List<Enfermedad> findEnfermedadEntities() {
        return findEnfermedadEntities(true, -1, -1);
    }

    public List<Enfermedad> findEnfermedadEntities(int maxResults, int firstResult) {
        return findEnfermedadEntities(false, maxResults, firstResult);
    }

    private List<Enfermedad> findEnfermedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Enfermedad.class));
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

    public Enfermedad findEnfermedad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Enfermedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnfermedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Enfermedad> rt = cq.from(Enfermedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
