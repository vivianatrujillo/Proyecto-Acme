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
import com.acme.entities.PacienteReferencia;
import com.acme.entities.Referencia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class ReferenciaJpaController implements Serializable {

    public ReferenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referencia referencia) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (referencia.getPacienteReferenciaList() == null) {
            referencia.setPacienteReferenciaList(new ArrayList<PacienteReferencia>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PacienteReferencia> attachedPacienteReferenciaList = new ArrayList<PacienteReferencia>();
            for (PacienteReferencia pacienteReferenciaListPacienteReferenciaToAttach : referencia.getPacienteReferenciaList()) {
                pacienteReferenciaListPacienteReferenciaToAttach = em.getReference(pacienteReferenciaListPacienteReferenciaToAttach.getClass(), pacienteReferenciaListPacienteReferenciaToAttach.getCodPacienteReferencia());
                attachedPacienteReferenciaList.add(pacienteReferenciaListPacienteReferenciaToAttach);
            }
            referencia.setPacienteReferenciaList(attachedPacienteReferenciaList);
            em.persist(referencia);
            for (PacienteReferencia pacienteReferenciaListPacienteReferencia : referencia.getPacienteReferenciaList()) {
                Referencia oldIdReferenciaOfPacienteReferenciaListPacienteReferencia = pacienteReferenciaListPacienteReferencia.getIdReferencia();
                pacienteReferenciaListPacienteReferencia.setIdReferencia(referencia);
                pacienteReferenciaListPacienteReferencia = em.merge(pacienteReferenciaListPacienteReferencia);
                if (oldIdReferenciaOfPacienteReferenciaListPacienteReferencia != null) {
                    oldIdReferenciaOfPacienteReferenciaListPacienteReferencia.getPacienteReferenciaList().remove(pacienteReferenciaListPacienteReferencia);
                    oldIdReferenciaOfPacienteReferenciaListPacienteReferencia = em.merge(oldIdReferenciaOfPacienteReferenciaListPacienteReferencia);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReferencia(referencia.getIdReferencia()) != null) {
                throw new PreexistingEntityException("Referencia " + referencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referencia referencia) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Referencia persistentReferencia = em.find(Referencia.class, referencia.getIdReferencia());
            List<PacienteReferencia> pacienteReferenciaListOld = persistentReferencia.getPacienteReferenciaList();
            List<PacienteReferencia> pacienteReferenciaListNew = referencia.getPacienteReferenciaList();
            List<String> illegalOrphanMessages = null;
            for (PacienteReferencia pacienteReferenciaListOldPacienteReferencia : pacienteReferenciaListOld) {
                if (!pacienteReferenciaListNew.contains(pacienteReferenciaListOldPacienteReferencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteReferencia " + pacienteReferenciaListOldPacienteReferencia + " since its idReferencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PacienteReferencia> attachedPacienteReferenciaListNew = new ArrayList<PacienteReferencia>();
            for (PacienteReferencia pacienteReferenciaListNewPacienteReferenciaToAttach : pacienteReferenciaListNew) {
                pacienteReferenciaListNewPacienteReferenciaToAttach = em.getReference(pacienteReferenciaListNewPacienteReferenciaToAttach.getClass(), pacienteReferenciaListNewPacienteReferenciaToAttach.getCodPacienteReferencia());
                attachedPacienteReferenciaListNew.add(pacienteReferenciaListNewPacienteReferenciaToAttach);
            }
            pacienteReferenciaListNew = attachedPacienteReferenciaListNew;
            referencia.setPacienteReferenciaList(pacienteReferenciaListNew);
            referencia = em.merge(referencia);
            for (PacienteReferencia pacienteReferenciaListNewPacienteReferencia : pacienteReferenciaListNew) {
                if (!pacienteReferenciaListOld.contains(pacienteReferenciaListNewPacienteReferencia)) {
                    Referencia oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia = pacienteReferenciaListNewPacienteReferencia.getIdReferencia();
                    pacienteReferenciaListNewPacienteReferencia.setIdReferencia(referencia);
                    pacienteReferenciaListNewPacienteReferencia = em.merge(pacienteReferenciaListNewPacienteReferencia);
                    if (oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia != null && !oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia.equals(referencia)) {
                        oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia.getPacienteReferenciaList().remove(pacienteReferenciaListNewPacienteReferencia);
                        oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia = em.merge(oldIdReferenciaOfPacienteReferenciaListNewPacienteReferencia);
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
                Integer id = referencia.getIdReferencia();
                if (findReferencia(id) == null) {
                    throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.");
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
            Referencia referencia;
            try {
                referencia = em.getReference(Referencia.class, id);
                referencia.getIdReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteReferencia> pacienteReferenciaListOrphanCheck = referencia.getPacienteReferenciaList();
            for (PacienteReferencia pacienteReferenciaListOrphanCheckPacienteReferencia : pacienteReferenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Referencia (" + referencia + ") cannot be destroyed since the PacienteReferencia " + pacienteReferenciaListOrphanCheckPacienteReferencia + " in its pacienteReferenciaList field has a non-nullable idReferencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(referencia);
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

    public List<Referencia> findReferenciaEntities() {
        return findReferenciaEntities(true, -1, -1);
    }

    public List<Referencia> findReferenciaEntities(int maxResults, int firstResult) {
        return findReferenciaEntities(false, maxResults, firstResult);
    }

    private List<Referencia> findReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referencia.class));
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

    public Referencia findReferencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencia> rt = cq.from(Referencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
