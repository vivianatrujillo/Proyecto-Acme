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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Paciente;
import com.acme.entities.Rh;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class RhJpaController implements Serializable {

    public RhJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rh rh) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rh.getPacienteList() == null) {
            rh.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : rh.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            rh.setPacienteList(attachedPacienteList);
            em.persist(rh);
            for (Paciente pacienteListPaciente : rh.getPacienteList()) {
                Rh oldIdRHOfPacienteListPaciente = pacienteListPaciente.getIdRH();
                pacienteListPaciente.setIdRH(rh);
                pacienteListPaciente = em.merge(pacienteListPaciente);
                if (oldIdRHOfPacienteListPaciente != null) {
                    oldIdRHOfPacienteListPaciente.getPacienteList().remove(pacienteListPaciente);
                    oldIdRHOfPacienteListPaciente = em.merge(oldIdRHOfPacienteListPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRh(rh.getIdRH()) != null) {
                throw new PreexistingEntityException("Rh " + rh + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rh rh) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rh persistentRh = em.find(Rh.class, rh.getIdRH());
            List<Paciente> pacienteListOld = persistentRh.getPacienteList();
            List<Paciente> pacienteListNew = rh.getPacienteList();
            List<String> illegalOrphanMessages = null;
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteListOldPaciente + " since its idRH field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            rh.setPacienteList(pacienteListNew);
            rh = em.merge(rh);
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    Rh oldIdRHOfPacienteListNewPaciente = pacienteListNewPaciente.getIdRH();
                    pacienteListNewPaciente.setIdRH(rh);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                    if (oldIdRHOfPacienteListNewPaciente != null && !oldIdRHOfPacienteListNewPaciente.equals(rh)) {
                        oldIdRHOfPacienteListNewPaciente.getPacienteList().remove(pacienteListNewPaciente);
                        oldIdRHOfPacienteListNewPaciente = em.merge(oldIdRHOfPacienteListNewPaciente);
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
                Integer id = rh.getIdRH();
                if (findRh(id) == null) {
                    throw new NonexistentEntityException("The rh with id " + id + " no longer exists.");
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
            Rh rh;
            try {
                rh = em.getReference(Rh.class, id);
                rh.getIdRH();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rh with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Paciente> pacienteListOrphanCheck = rh.getPacienteList();
            for (Paciente pacienteListOrphanCheckPaciente : pacienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rh (" + rh + ") cannot be destroyed since the Paciente " + pacienteListOrphanCheckPaciente + " in its pacienteList field has a non-nullable idRH field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(rh);
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

    public List<Rh> findRhEntities() {
        return findRhEntities(true, -1, -1);
    }

    public List<Rh> findRhEntities(int maxResults, int firstResult) {
        return findRhEntities(false, maxResults, firstResult);
    }

    private List<Rh> findRhEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rh.class));
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

    public Rh findRh(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rh.class, id);
        } finally {
            em.close();
        }
    }

    public int getRhCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rh> rt = cq.from(Rh.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
