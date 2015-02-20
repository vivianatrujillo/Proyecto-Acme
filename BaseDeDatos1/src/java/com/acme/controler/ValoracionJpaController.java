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
import com.acme.entities.ValoracionTratamiento;
import java.util.ArrayList;
import java.util.List;
import com.acme.entities.Diagnostico;
import com.acme.entities.Valoracion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class ValoracionJpaController implements Serializable {

    public ValoracionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valoracion valoracion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (valoracion.getValoracionTratamientoList() == null) {
            valoracion.setValoracionTratamientoList(new ArrayList<ValoracionTratamiento>());
        }
        if (valoracion.getDiagnosticoList() == null) {
            valoracion.setDiagnosticoList(new ArrayList<Diagnostico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente idPaciente = valoracion.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                valoracion.setIdPaciente(idPaciente);
            }
            List<ValoracionTratamiento> attachedValoracionTratamientoList = new ArrayList<ValoracionTratamiento>();
            for (ValoracionTratamiento valoracionTratamientoListValoracionTratamientoToAttach : valoracion.getValoracionTratamientoList()) {
                valoracionTratamientoListValoracionTratamientoToAttach = em.getReference(valoracionTratamientoListValoracionTratamientoToAttach.getClass(), valoracionTratamientoListValoracionTratamientoToAttach.getCodValoracionTratamientocol());
                attachedValoracionTratamientoList.add(valoracionTratamientoListValoracionTratamientoToAttach);
            }
            valoracion.setValoracionTratamientoList(attachedValoracionTratamientoList);
            List<Diagnostico> attachedDiagnosticoList = new ArrayList<Diagnostico>();
            for (Diagnostico diagnosticoListDiagnosticoToAttach : valoracion.getDiagnosticoList()) {
                diagnosticoListDiagnosticoToAttach = em.getReference(diagnosticoListDiagnosticoToAttach.getClass(), diagnosticoListDiagnosticoToAttach.getIdDiagnostico());
                attachedDiagnosticoList.add(diagnosticoListDiagnosticoToAttach);
            }
            valoracion.setDiagnosticoList(attachedDiagnosticoList);
            em.persist(valoracion);
            if (idPaciente != null) {
                idPaciente.getValoracionList().add(valoracion);
                idPaciente = em.merge(idPaciente);
            }
            for (ValoracionTratamiento valoracionTratamientoListValoracionTratamiento : valoracion.getValoracionTratamientoList()) {
                Valoracion oldIdValoracionOfValoracionTratamientoListValoracionTratamiento = valoracionTratamientoListValoracionTratamiento.getIdValoracion();
                valoracionTratamientoListValoracionTratamiento.setIdValoracion(valoracion);
                valoracionTratamientoListValoracionTratamiento = em.merge(valoracionTratamientoListValoracionTratamiento);
                if (oldIdValoracionOfValoracionTratamientoListValoracionTratamiento != null) {
                    oldIdValoracionOfValoracionTratamientoListValoracionTratamiento.getValoracionTratamientoList().remove(valoracionTratamientoListValoracionTratamiento);
                    oldIdValoracionOfValoracionTratamientoListValoracionTratamiento = em.merge(oldIdValoracionOfValoracionTratamientoListValoracionTratamiento);
                }
            }
            for (Diagnostico diagnosticoListDiagnostico : valoracion.getDiagnosticoList()) {
                Valoracion oldIdValoracionOfDiagnosticoListDiagnostico = diagnosticoListDiagnostico.getIdValoracion();
                diagnosticoListDiagnostico.setIdValoracion(valoracion);
                diagnosticoListDiagnostico = em.merge(diagnosticoListDiagnostico);
                if (oldIdValoracionOfDiagnosticoListDiagnostico != null) {
                    oldIdValoracionOfDiagnosticoListDiagnostico.getDiagnosticoList().remove(diagnosticoListDiagnostico);
                    oldIdValoracionOfDiagnosticoListDiagnostico = em.merge(oldIdValoracionOfDiagnosticoListDiagnostico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findValoracion(valoracion.getIdValoracion()) != null) {
                throw new PreexistingEntityException("Valoracion " + valoracion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valoracion valoracion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Valoracion persistentValoracion = em.find(Valoracion.class, valoracion.getIdValoracion());
            Paciente idPacienteOld = persistentValoracion.getIdPaciente();
            Paciente idPacienteNew = valoracion.getIdPaciente();
            List<ValoracionTratamiento> valoracionTratamientoListOld = persistentValoracion.getValoracionTratamientoList();
            List<ValoracionTratamiento> valoracionTratamientoListNew = valoracion.getValoracionTratamientoList();
            List<Diagnostico> diagnosticoListOld = persistentValoracion.getDiagnosticoList();
            List<Diagnostico> diagnosticoListNew = valoracion.getDiagnosticoList();
            List<String> illegalOrphanMessages = null;
            for (ValoracionTratamiento valoracionTratamientoListOldValoracionTratamiento : valoracionTratamientoListOld) {
                if (!valoracionTratamientoListNew.contains(valoracionTratamientoListOldValoracionTratamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValoracionTratamiento " + valoracionTratamientoListOldValoracionTratamiento + " since its idValoracion field is not nullable.");
                }
            }
            for (Diagnostico diagnosticoListOldDiagnostico : diagnosticoListOld) {
                if (!diagnosticoListNew.contains(diagnosticoListOldDiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Diagnostico " + diagnosticoListOldDiagnostico + " since its idValoracion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                valoracion.setIdPaciente(idPacienteNew);
            }
            List<ValoracionTratamiento> attachedValoracionTratamientoListNew = new ArrayList<ValoracionTratamiento>();
            for (ValoracionTratamiento valoracionTratamientoListNewValoracionTratamientoToAttach : valoracionTratamientoListNew) {
                valoracionTratamientoListNewValoracionTratamientoToAttach = em.getReference(valoracionTratamientoListNewValoracionTratamientoToAttach.getClass(), valoracionTratamientoListNewValoracionTratamientoToAttach.getCodValoracionTratamientocol());
                attachedValoracionTratamientoListNew.add(valoracionTratamientoListNewValoracionTratamientoToAttach);
            }
            valoracionTratamientoListNew = attachedValoracionTratamientoListNew;
            valoracion.setValoracionTratamientoList(valoracionTratamientoListNew);
            List<Diagnostico> attachedDiagnosticoListNew = new ArrayList<Diagnostico>();
            for (Diagnostico diagnosticoListNewDiagnosticoToAttach : diagnosticoListNew) {
                diagnosticoListNewDiagnosticoToAttach = em.getReference(diagnosticoListNewDiagnosticoToAttach.getClass(), diagnosticoListNewDiagnosticoToAttach.getIdDiagnostico());
                attachedDiagnosticoListNew.add(diagnosticoListNewDiagnosticoToAttach);
            }
            diagnosticoListNew = attachedDiagnosticoListNew;
            valoracion.setDiagnosticoList(diagnosticoListNew);
            valoracion = em.merge(valoracion);
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getValoracionList().remove(valoracion);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getValoracionList().add(valoracion);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (ValoracionTratamiento valoracionTratamientoListNewValoracionTratamiento : valoracionTratamientoListNew) {
                if (!valoracionTratamientoListOld.contains(valoracionTratamientoListNewValoracionTratamiento)) {
                    Valoracion oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento = valoracionTratamientoListNewValoracionTratamiento.getIdValoracion();
                    valoracionTratamientoListNewValoracionTratamiento.setIdValoracion(valoracion);
                    valoracionTratamientoListNewValoracionTratamiento = em.merge(valoracionTratamientoListNewValoracionTratamiento);
                    if (oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento != null && !oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento.equals(valoracion)) {
                        oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento.getValoracionTratamientoList().remove(valoracionTratamientoListNewValoracionTratamiento);
                        oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento = em.merge(oldIdValoracionOfValoracionTratamientoListNewValoracionTratamiento);
                    }
                }
            }
            for (Diagnostico diagnosticoListNewDiagnostico : diagnosticoListNew) {
                if (!diagnosticoListOld.contains(diagnosticoListNewDiagnostico)) {
                    Valoracion oldIdValoracionOfDiagnosticoListNewDiagnostico = diagnosticoListNewDiagnostico.getIdValoracion();
                    diagnosticoListNewDiagnostico.setIdValoracion(valoracion);
                    diagnosticoListNewDiagnostico = em.merge(diagnosticoListNewDiagnostico);
                    if (oldIdValoracionOfDiagnosticoListNewDiagnostico != null && !oldIdValoracionOfDiagnosticoListNewDiagnostico.equals(valoracion)) {
                        oldIdValoracionOfDiagnosticoListNewDiagnostico.getDiagnosticoList().remove(diagnosticoListNewDiagnostico);
                        oldIdValoracionOfDiagnosticoListNewDiagnostico = em.merge(oldIdValoracionOfDiagnosticoListNewDiagnostico);
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
                Integer id = valoracion.getIdValoracion();
                if (findValoracion(id) == null) {
                    throw new NonexistentEntityException("The valoracion with id " + id + " no longer exists.");
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
            Valoracion valoracion;
            try {
                valoracion = em.getReference(Valoracion.class, id);
                valoracion.getIdValoracion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoracion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ValoracionTratamiento> valoracionTratamientoListOrphanCheck = valoracion.getValoracionTratamientoList();
            for (ValoracionTratamiento valoracionTratamientoListOrphanCheckValoracionTratamiento : valoracionTratamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Valoracion (" + valoracion + ") cannot be destroyed since the ValoracionTratamiento " + valoracionTratamientoListOrphanCheckValoracionTratamiento + " in its valoracionTratamientoList field has a non-nullable idValoracion field.");
            }
            List<Diagnostico> diagnosticoListOrphanCheck = valoracion.getDiagnosticoList();
            for (Diagnostico diagnosticoListOrphanCheckDiagnostico : diagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Valoracion (" + valoracion + ") cannot be destroyed since the Diagnostico " + diagnosticoListOrphanCheckDiagnostico + " in its diagnosticoList field has a non-nullable idValoracion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente idPaciente = valoracion.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getValoracionList().remove(valoracion);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(valoracion);
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

    public List<Valoracion> findValoracionEntities() {
        return findValoracionEntities(true, -1, -1);
    }

    public List<Valoracion> findValoracionEntities(int maxResults, int firstResult) {
        return findValoracionEntities(false, maxResults, firstResult);
    }

    private List<Valoracion> findValoracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valoracion.class));
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

    public Valoracion findValoracion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valoracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valoracion> rt = cq.from(Valoracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
