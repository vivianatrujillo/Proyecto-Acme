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
import com.acme.entities.Enfermedad;
import com.acme.entities.PacienteEnfermedad;
import com.acme.entities.PacienteEnfermedadEnfermedad;
import com.acme.entities.PacienteEnfermedadEnfermedadPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class PacienteEnfermedadEnfermedadJpaController implements Serializable {

    public PacienteEnfermedadEnfermedadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK() == null) {
            pacienteEnfermedadEnfermedad.setPacienteEnfermedadEnfermedadPK(new PacienteEnfermedadEnfermedadPK());
        }
        pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK().setIdEnfermedad(pacienteEnfermedadEnfermedad.getEnfermedad().getIdEnfermedad());
        pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK().setCodPacienteEnfermedadcol(pacienteEnfermedadEnfermedad.getPacienteEnfermedad().getCodPacienteEnfermedadcol());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Enfermedad enfermedad = pacienteEnfermedadEnfermedad.getEnfermedad();
            if (enfermedad != null) {
                enfermedad = em.getReference(enfermedad.getClass(), enfermedad.getIdEnfermedad());
                pacienteEnfermedadEnfermedad.setEnfermedad(enfermedad);
            }
            PacienteEnfermedad pacienteEnfermedad = pacienteEnfermedadEnfermedad.getPacienteEnfermedad();
            if (pacienteEnfermedad != null) {
                pacienteEnfermedad = em.getReference(pacienteEnfermedad.getClass(), pacienteEnfermedad.getCodPacienteEnfermedadcol());
                pacienteEnfermedadEnfermedad.setPacienteEnfermedad(pacienteEnfermedad);
            }
            em.persist(pacienteEnfermedadEnfermedad);
            if (enfermedad != null) {
                enfermedad.getPacienteEnfermedadEnfermedadList().add(pacienteEnfermedadEnfermedad);
                enfermedad = em.merge(enfermedad);
            }
            if (pacienteEnfermedad != null) {
                pacienteEnfermedad.getPacienteEnfermedadEnfermedadList().add(pacienteEnfermedadEnfermedad);
                pacienteEnfermedad = em.merge(pacienteEnfermedad);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPacienteEnfermedadEnfermedad(pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK()) != null) {
                throw new PreexistingEntityException("PacienteEnfermedadEnfermedad " + pacienteEnfermedadEnfermedad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedad) throws NonexistentEntityException, RollbackFailureException, Exception {
        pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK().setIdEnfermedad(pacienteEnfermedadEnfermedad.getEnfermedad().getIdEnfermedad());
        pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK().setCodPacienteEnfermedadcol(pacienteEnfermedadEnfermedad.getPacienteEnfermedad().getCodPacienteEnfermedadcol());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteEnfermedadEnfermedad persistentPacienteEnfermedadEnfermedad = em.find(PacienteEnfermedadEnfermedad.class, pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK());
            Enfermedad enfermedadOld = persistentPacienteEnfermedadEnfermedad.getEnfermedad();
            Enfermedad enfermedadNew = pacienteEnfermedadEnfermedad.getEnfermedad();
            PacienteEnfermedad pacienteEnfermedadOld = persistentPacienteEnfermedadEnfermedad.getPacienteEnfermedad();
            PacienteEnfermedad pacienteEnfermedadNew = pacienteEnfermedadEnfermedad.getPacienteEnfermedad();
            if (enfermedadNew != null) {
                enfermedadNew = em.getReference(enfermedadNew.getClass(), enfermedadNew.getIdEnfermedad());
                pacienteEnfermedadEnfermedad.setEnfermedad(enfermedadNew);
            }
            if (pacienteEnfermedadNew != null) {
                pacienteEnfermedadNew = em.getReference(pacienteEnfermedadNew.getClass(), pacienteEnfermedadNew.getCodPacienteEnfermedadcol());
                pacienteEnfermedadEnfermedad.setPacienteEnfermedad(pacienteEnfermedadNew);
            }
            pacienteEnfermedadEnfermedad = em.merge(pacienteEnfermedadEnfermedad);
            if (enfermedadOld != null && !enfermedadOld.equals(enfermedadNew)) {
                enfermedadOld.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedad);
                enfermedadOld = em.merge(enfermedadOld);
            }
            if (enfermedadNew != null && !enfermedadNew.equals(enfermedadOld)) {
                enfermedadNew.getPacienteEnfermedadEnfermedadList().add(pacienteEnfermedadEnfermedad);
                enfermedadNew = em.merge(enfermedadNew);
            }
            if (pacienteEnfermedadOld != null && !pacienteEnfermedadOld.equals(pacienteEnfermedadNew)) {
                pacienteEnfermedadOld.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedad);
                pacienteEnfermedadOld = em.merge(pacienteEnfermedadOld);
            }
            if (pacienteEnfermedadNew != null && !pacienteEnfermedadNew.equals(pacienteEnfermedadOld)) {
                pacienteEnfermedadNew.getPacienteEnfermedadEnfermedadList().add(pacienteEnfermedadEnfermedad);
                pacienteEnfermedadNew = em.merge(pacienteEnfermedadNew);
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
                PacienteEnfermedadEnfermedadPK id = pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK();
                if (findPacienteEnfermedadEnfermedad(id) == null) {
                    throw new NonexistentEntityException("The pacienteEnfermedadEnfermedad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PacienteEnfermedadEnfermedadPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteEnfermedadEnfermedad pacienteEnfermedadEnfermedad;
            try {
                pacienteEnfermedadEnfermedad = em.getReference(PacienteEnfermedadEnfermedad.class, id);
                pacienteEnfermedadEnfermedad.getPacienteEnfermedadEnfermedadPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacienteEnfermedadEnfermedad with id " + id + " no longer exists.", enfe);
            }
            Enfermedad enfermedad = pacienteEnfermedadEnfermedad.getEnfermedad();
            if (enfermedad != null) {
                enfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedad);
                enfermedad = em.merge(enfermedad);
            }
            PacienteEnfermedad pacienteEnfermedad = pacienteEnfermedadEnfermedad.getPacienteEnfermedad();
            if (pacienteEnfermedad != null) {
                pacienteEnfermedad.getPacienteEnfermedadEnfermedadList().remove(pacienteEnfermedadEnfermedad);
                pacienteEnfermedad = em.merge(pacienteEnfermedad);
            }
            em.remove(pacienteEnfermedadEnfermedad);
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

    public List<PacienteEnfermedadEnfermedad> findPacienteEnfermedadEnfermedadEntities() {
        return findPacienteEnfermedadEnfermedadEntities(true, -1, -1);
    }

    public List<PacienteEnfermedadEnfermedad> findPacienteEnfermedadEnfermedadEntities(int maxResults, int firstResult) {
        return findPacienteEnfermedadEnfermedadEntities(false, maxResults, firstResult);
    }

    private List<PacienteEnfermedadEnfermedad> findPacienteEnfermedadEnfermedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacienteEnfermedadEnfermedad.class));
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

    public PacienteEnfermedadEnfermedad findPacienteEnfermedadEnfermedad(PacienteEnfermedadEnfermedadPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacienteEnfermedadEnfermedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteEnfermedadEnfermedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacienteEnfermedadEnfermedad> rt = cq.from(PacienteEnfermedadEnfermedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
