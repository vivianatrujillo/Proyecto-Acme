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
import com.acme.entities.PacienteEnfermedad;
import com.acme.entities.PacienteEnfermedadEnfermedad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class PacienteEnfermedadJpaController implements Serializable {

    public PacienteEnfermedadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacienteEnfermedad pacienteEnfermedad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pacienteEnfermedad.getPacienteEnfermedadEnfermedadList() == null) {
            pacienteEnfermedad.setPacienteEnfermedadEnfermedadList(new ArrayList<PacienteEnfermedadEnfermedad>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente idPaciente = pacienteEnfermedad.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                pacienteEnfermedad.setIdPaciente(idPaciente);
            }
            List<PacienteEnfermedadEnfermedad> attachedPacienteEnfermedadEnfermedadList = new ArrayList<PacienteEnfermedadEnfermedad>();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach : pacienteEnfermedad.getPacienteEnfermedadEnfermedadList()) {
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach = em.getReference(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach.getClass(), pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach.getPacienteEnfermedadEnfermedadPK());
                attachedPacienteEnfermedadEnfermedadList.add(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedadToAttach);
            }
            pacienteEnfermedad.setPacienteEnfermedadEnfermedadList(attachedPacienteEnfermedadEnfermedadList);
            em.persist(pacienteEnfermedad);
            if (idPaciente != null) {
                idPaciente.getPacienteEnfermedadList().add(pacienteEnfermedad);
                idPaciente = em.merge(idPaciente);
            }
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad : pacienteEnfermedad.getPacienteEnfermedadEnfermedadList()) {
                PacienteEnfermedad oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.getPacienteEnfermedad();
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.setPacienteEnfermedad(pacienteEnfermedad);
                pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = em.merge(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                if (oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad != null) {
                    oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                    oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad = em.merge(oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListPacienteEnfermedadEnfermedad);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPacienteEnfermedad(pacienteEnfermedad.getCodPacienteEnfermedadcol()) != null) {
                throw new PreexistingEntityException("PacienteEnfermedad " + pacienteEnfermedad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacienteEnfermedad pacienteEnfermedad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteEnfermedad persistentPacienteEnfermedad = em.find(PacienteEnfermedad.class, pacienteEnfermedad.getCodPacienteEnfermedadcol());
            Paciente idPacienteOld = persistentPacienteEnfermedad.getIdPaciente();
            Paciente idPacienteNew = pacienteEnfermedad.getIdPaciente();
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListOld = persistentPacienteEnfermedad.getPacienteEnfermedadEnfermedadList();
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListNew = pacienteEnfermedad.getPacienteEnfermedadEnfermedadList();
            List<String> illegalOrphanMessages = null;
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListOld) {
                if (!pacienteEnfermedadEnfermedadListNew.contains(pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteEnfermedadEnfermedad " + pacienteEnfermedadEnfermedadListOldPacienteEnfermedadEnfermedad + " since its pacienteEnfermedad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                pacienteEnfermedad.setIdPaciente(idPacienteNew);
            }
            List<PacienteEnfermedadEnfermedad> attachedPacienteEnfermedadEnfermedadListNew = new ArrayList<PacienteEnfermedadEnfermedad>();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach : pacienteEnfermedadEnfermedadListNew) {
                pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach = em.getReference(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach.getClass(), pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach.getPacienteEnfermedadEnfermedadPK());
                attachedPacienteEnfermedadEnfermedadListNew.add(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedadToAttach);
            }
            pacienteEnfermedadEnfermedadListNew = attachedPacienteEnfermedadEnfermedadListNew;
            pacienteEnfermedad.setPacienteEnfermedadEnfermedadList(pacienteEnfermedadEnfermedadListNew);
            pacienteEnfermedad = em.merge(pacienteEnfermedad);
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getPacienteEnfermedadList().remove(pacienteEnfermedad);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getPacienteEnfermedadList().add(pacienteEnfermedad);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListNew) {
                if (!pacienteEnfermedadEnfermedadListOld.contains(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad)) {
                    PacienteEnfermedad oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.getPacienteEnfermedad();
                    pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.setPacienteEnfermedad(pacienteEnfermedad);
                    pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = em.merge(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
                    if (oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad != null && !oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.equals(pacienteEnfermedad)) {
                        oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
                        oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad = em.merge(oldPacienteEnfermedadOfPacienteEnfermedadEnfermedadListNewPacienteEnfermedadEnfermedad);
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
                Integer id = pacienteEnfermedad.getCodPacienteEnfermedadcol();
                if (findPacienteEnfermedad(id) == null) {
                    throw new NonexistentEntityException("The pacienteEnfermedad with id " + id + " no longer exists.");
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
            PacienteEnfermedad pacienteEnfermedad;
            try {
                pacienteEnfermedad = em.getReference(PacienteEnfermedad.class, id);
                pacienteEnfermedad.getCodPacienteEnfermedadcol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacienteEnfermedad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteEnfermedadEnfermedad> pacienteEnfermedadEnfermedadListOrphanCheck = pacienteEnfermedad.getPacienteEnfermedadEnfermedadList();
            for (PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedadListOrphanCheckPacienteEnfermedadEnfermedad : pacienteEnfermedadEnfermedadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PacienteEnfermedad (" + pacienteEnfermedad + ") cannot be destroyed since the PacienteEnfermedadEnfermedad " + pacienteEnfermedadEnfermedadListOrphanCheckPacienteEnfermedadEnfermedad + " in its pacienteEnfermedadEnfermedadList field has a non-nullable pacienteEnfermedad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente idPaciente = pacienteEnfermedad.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getPacienteEnfermedadList().remove(pacienteEnfermedad);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(pacienteEnfermedad);
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

    public List<PacienteEnfermedad> findPacienteEnfermedadEntities() {
        return findPacienteEnfermedadEntities(true, -1, -1);
    }

    public List<PacienteEnfermedad> findPacienteEnfermedadEntities(int maxResults, int firstResult) {
        return findPacienteEnfermedadEntities(false, maxResults, firstResult);
    }

    private List<PacienteEnfermedad> findPacienteEnfermedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacienteEnfermedad.class));
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

    public PacienteEnfermedad findPacienteEnfermedad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacienteEnfermedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteEnfermedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacienteEnfermedad> rt = cq.from(PacienteEnfermedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
