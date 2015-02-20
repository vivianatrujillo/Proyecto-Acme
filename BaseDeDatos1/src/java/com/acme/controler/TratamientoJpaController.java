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
import com.acme.entities.FormatoDeEvolucion;
import com.acme.entities.Tratamiento;
import java.util.ArrayList;
import java.util.List;
import com.acme.entities.ValoracionTratamiento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class TratamientoJpaController implements Serializable {

    public TratamientoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tratamiento tratamiento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tratamiento.getFormatoDeEvolucionList() == null) {
            tratamiento.setFormatoDeEvolucionList(new ArrayList<FormatoDeEvolucion>());
        }
        if (tratamiento.getValoracionTratamientoList() == null) {
            tratamiento.setValoracionTratamientoList(new ArrayList<ValoracionTratamiento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<FormatoDeEvolucion> attachedFormatoDeEvolucionList = new ArrayList<FormatoDeEvolucion>();
            for (FormatoDeEvolucion formatoDeEvolucionListFormatoDeEvolucionToAttach : tratamiento.getFormatoDeEvolucionList()) {
                formatoDeEvolucionListFormatoDeEvolucionToAttach = em.getReference(formatoDeEvolucionListFormatoDeEvolucionToAttach.getClass(), formatoDeEvolucionListFormatoDeEvolucionToAttach.getIdFormatodeevolucion());
                attachedFormatoDeEvolucionList.add(formatoDeEvolucionListFormatoDeEvolucionToAttach);
            }
            tratamiento.setFormatoDeEvolucionList(attachedFormatoDeEvolucionList);
            List<ValoracionTratamiento> attachedValoracionTratamientoList = new ArrayList<ValoracionTratamiento>();
            for (ValoracionTratamiento valoracionTratamientoListValoracionTratamientoToAttach : tratamiento.getValoracionTratamientoList()) {
                valoracionTratamientoListValoracionTratamientoToAttach = em.getReference(valoracionTratamientoListValoracionTratamientoToAttach.getClass(), valoracionTratamientoListValoracionTratamientoToAttach.getCodValoracionTratamientocol());
                attachedValoracionTratamientoList.add(valoracionTratamientoListValoracionTratamientoToAttach);
            }
            tratamiento.setValoracionTratamientoList(attachedValoracionTratamientoList);
            em.persist(tratamiento);
            for (FormatoDeEvolucion formatoDeEvolucionListFormatoDeEvolucion : tratamiento.getFormatoDeEvolucionList()) {
                Tratamiento oldIdTratamientoOfFormatoDeEvolucionListFormatoDeEvolucion = formatoDeEvolucionListFormatoDeEvolucion.getIdTratamiento();
                formatoDeEvolucionListFormatoDeEvolucion.setIdTratamiento(tratamiento);
                formatoDeEvolucionListFormatoDeEvolucion = em.merge(formatoDeEvolucionListFormatoDeEvolucion);
                if (oldIdTratamientoOfFormatoDeEvolucionListFormatoDeEvolucion != null) {
                    oldIdTratamientoOfFormatoDeEvolucionListFormatoDeEvolucion.getFormatoDeEvolucionList().remove(formatoDeEvolucionListFormatoDeEvolucion);
                    oldIdTratamientoOfFormatoDeEvolucionListFormatoDeEvolucion = em.merge(oldIdTratamientoOfFormatoDeEvolucionListFormatoDeEvolucion);
                }
            }
            for (ValoracionTratamiento valoracionTratamientoListValoracionTratamiento : tratamiento.getValoracionTratamientoList()) {
                Tratamiento oldIdTratamientoOfValoracionTratamientoListValoracionTratamiento = valoracionTratamientoListValoracionTratamiento.getIdTratamiento();
                valoracionTratamientoListValoracionTratamiento.setIdTratamiento(tratamiento);
                valoracionTratamientoListValoracionTratamiento = em.merge(valoracionTratamientoListValoracionTratamiento);
                if (oldIdTratamientoOfValoracionTratamientoListValoracionTratamiento != null) {
                    oldIdTratamientoOfValoracionTratamientoListValoracionTratamiento.getValoracionTratamientoList().remove(valoracionTratamientoListValoracionTratamiento);
                    oldIdTratamientoOfValoracionTratamientoListValoracionTratamiento = em.merge(oldIdTratamientoOfValoracionTratamientoListValoracionTratamiento);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTratamiento(tratamiento.getIdTratamiento()) != null) {
                throw new PreexistingEntityException("Tratamiento " + tratamiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tratamiento tratamiento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tratamiento persistentTratamiento = em.find(Tratamiento.class, tratamiento.getIdTratamiento());
            List<FormatoDeEvolucion> formatoDeEvolucionListOld = persistentTratamiento.getFormatoDeEvolucionList();
            List<FormatoDeEvolucion> formatoDeEvolucionListNew = tratamiento.getFormatoDeEvolucionList();
            List<ValoracionTratamiento> valoracionTratamientoListOld = persistentTratamiento.getValoracionTratamientoList();
            List<ValoracionTratamiento> valoracionTratamientoListNew = tratamiento.getValoracionTratamientoList();
            List<String> illegalOrphanMessages = null;
            for (FormatoDeEvolucion formatoDeEvolucionListOldFormatoDeEvolucion : formatoDeEvolucionListOld) {
                if (!formatoDeEvolucionListNew.contains(formatoDeEvolucionListOldFormatoDeEvolucion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FormatoDeEvolucion " + formatoDeEvolucionListOldFormatoDeEvolucion + " since its idTratamiento field is not nullable.");
                }
            }
            for (ValoracionTratamiento valoracionTratamientoListOldValoracionTratamiento : valoracionTratamientoListOld) {
                if (!valoracionTratamientoListNew.contains(valoracionTratamientoListOldValoracionTratamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValoracionTratamiento " + valoracionTratamientoListOldValoracionTratamiento + " since its idTratamiento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<FormatoDeEvolucion> attachedFormatoDeEvolucionListNew = new ArrayList<FormatoDeEvolucion>();
            for (FormatoDeEvolucion formatoDeEvolucionListNewFormatoDeEvolucionToAttach : formatoDeEvolucionListNew) {
                formatoDeEvolucionListNewFormatoDeEvolucionToAttach = em.getReference(formatoDeEvolucionListNewFormatoDeEvolucionToAttach.getClass(), formatoDeEvolucionListNewFormatoDeEvolucionToAttach.getIdFormatodeevolucion());
                attachedFormatoDeEvolucionListNew.add(formatoDeEvolucionListNewFormatoDeEvolucionToAttach);
            }
            formatoDeEvolucionListNew = attachedFormatoDeEvolucionListNew;
            tratamiento.setFormatoDeEvolucionList(formatoDeEvolucionListNew);
            List<ValoracionTratamiento> attachedValoracionTratamientoListNew = new ArrayList<ValoracionTratamiento>();
            for (ValoracionTratamiento valoracionTratamientoListNewValoracionTratamientoToAttach : valoracionTratamientoListNew) {
                valoracionTratamientoListNewValoracionTratamientoToAttach = em.getReference(valoracionTratamientoListNewValoracionTratamientoToAttach.getClass(), valoracionTratamientoListNewValoracionTratamientoToAttach.getCodValoracionTratamientocol());
                attachedValoracionTratamientoListNew.add(valoracionTratamientoListNewValoracionTratamientoToAttach);
            }
            valoracionTratamientoListNew = attachedValoracionTratamientoListNew;
            tratamiento.setValoracionTratamientoList(valoracionTratamientoListNew);
            tratamiento = em.merge(tratamiento);
            for (FormatoDeEvolucion formatoDeEvolucionListNewFormatoDeEvolucion : formatoDeEvolucionListNew) {
                if (!formatoDeEvolucionListOld.contains(formatoDeEvolucionListNewFormatoDeEvolucion)) {
                    Tratamiento oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion = formatoDeEvolucionListNewFormatoDeEvolucion.getIdTratamiento();
                    formatoDeEvolucionListNewFormatoDeEvolucion.setIdTratamiento(tratamiento);
                    formatoDeEvolucionListNewFormatoDeEvolucion = em.merge(formatoDeEvolucionListNewFormatoDeEvolucion);
                    if (oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion != null && !oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion.equals(tratamiento)) {
                        oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion.getFormatoDeEvolucionList().remove(formatoDeEvolucionListNewFormatoDeEvolucion);
                        oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion = em.merge(oldIdTratamientoOfFormatoDeEvolucionListNewFormatoDeEvolucion);
                    }
                }
            }
            for (ValoracionTratamiento valoracionTratamientoListNewValoracionTratamiento : valoracionTratamientoListNew) {
                if (!valoracionTratamientoListOld.contains(valoracionTratamientoListNewValoracionTratamiento)) {
                    Tratamiento oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento = valoracionTratamientoListNewValoracionTratamiento.getIdTratamiento();
                    valoracionTratamientoListNewValoracionTratamiento.setIdTratamiento(tratamiento);
                    valoracionTratamientoListNewValoracionTratamiento = em.merge(valoracionTratamientoListNewValoracionTratamiento);
                    if (oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento != null && !oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento.equals(tratamiento)) {
                        oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento.getValoracionTratamientoList().remove(valoracionTratamientoListNewValoracionTratamiento);
                        oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento = em.merge(oldIdTratamientoOfValoracionTratamientoListNewValoracionTratamiento);
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
                Integer id = tratamiento.getIdTratamiento();
                if (findTratamiento(id) == null) {
                    throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.");
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
            Tratamiento tratamiento;
            try {
                tratamiento = em.getReference(Tratamiento.class, id);
                tratamiento.getIdTratamiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FormatoDeEvolucion> formatoDeEvolucionListOrphanCheck = tratamiento.getFormatoDeEvolucionList();
            for (FormatoDeEvolucion formatoDeEvolucionListOrphanCheckFormatoDeEvolucion : formatoDeEvolucionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tratamiento (" + tratamiento + ") cannot be destroyed since the FormatoDeEvolucion " + formatoDeEvolucionListOrphanCheckFormatoDeEvolucion + " in its formatoDeEvolucionList field has a non-nullable idTratamiento field.");
            }
            List<ValoracionTratamiento> valoracionTratamientoListOrphanCheck = tratamiento.getValoracionTratamientoList();
            for (ValoracionTratamiento valoracionTratamientoListOrphanCheckValoracionTratamiento : valoracionTratamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tratamiento (" + tratamiento + ") cannot be destroyed since the ValoracionTratamiento " + valoracionTratamientoListOrphanCheckValoracionTratamiento + " in its valoracionTratamientoList field has a non-nullable idTratamiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tratamiento);
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

    public List<Tratamiento> findTratamientoEntities() {
        return findTratamientoEntities(true, -1, -1);
    }

    public List<Tratamiento> findTratamientoEntities(int maxResults, int firstResult) {
        return findTratamientoEntities(false, maxResults, firstResult);
    }

    private List<Tratamiento> findTratamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tratamiento.class));
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

    public Tratamiento findTratamiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tratamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tratamiento> rt = cq.from(Tratamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
