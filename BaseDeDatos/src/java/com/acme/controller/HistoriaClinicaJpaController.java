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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Trabajador;
import com.acme.entities.FormatoDeEvolucion;
import java.util.ArrayList;
import java.util.List;
import com.acme.entities.Tratamiento;
import com.acme.entities.Diagnostico;
import com.acme.entities.HistoriaClinica;
import com.acme.entities.Paciente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class HistoriaClinicaJpaController implements Serializable {

    public HistoriaClinicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoriaClinica historiaClinica) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (historiaClinica.getFormatoDeEvolucionList() == null) {
            historiaClinica.setFormatoDeEvolucionList(new ArrayList<FormatoDeEvolucion>());
        }
        if (historiaClinica.getTratamientoList() == null) {
            historiaClinica.setTratamientoList(new ArrayList<Tratamiento>());
        }
        if (historiaClinica.getDiagnosticoList() == null) {
            historiaClinica.setDiagnosticoList(new ArrayList<Diagnostico>());
        }
        if (historiaClinica.getPacienteList() == null) {
            historiaClinica.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trabajador idTrabajador = historiaClinica.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador = em.getReference(idTrabajador.getClass(), idTrabajador.getIdTrabajador());
                historiaClinica.setIdTrabajador(idTrabajador);
            }
            List<FormatoDeEvolucion> attachedFormatoDeEvolucionList = new ArrayList<FormatoDeEvolucion>();
            for (FormatoDeEvolucion formatoDeEvolucionListFormatoDeEvolucionToAttach : historiaClinica.getFormatoDeEvolucionList()) {
                formatoDeEvolucionListFormatoDeEvolucionToAttach = em.getReference(formatoDeEvolucionListFormatoDeEvolucionToAttach.getClass(), formatoDeEvolucionListFormatoDeEvolucionToAttach.getIdFormatodeevolucion());
                attachedFormatoDeEvolucionList.add(formatoDeEvolucionListFormatoDeEvolucionToAttach);
            }
            historiaClinica.setFormatoDeEvolucionList(attachedFormatoDeEvolucionList);
            List<Tratamiento> attachedTratamientoList = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListTratamientoToAttach : historiaClinica.getTratamientoList()) {
                tratamientoListTratamientoToAttach = em.getReference(tratamientoListTratamientoToAttach.getClass(), tratamientoListTratamientoToAttach.getIdTratamiento());
                attachedTratamientoList.add(tratamientoListTratamientoToAttach);
            }
            historiaClinica.setTratamientoList(attachedTratamientoList);
            List<Diagnostico> attachedDiagnosticoList = new ArrayList<Diagnostico>();
            for (Diagnostico diagnosticoListDiagnosticoToAttach : historiaClinica.getDiagnosticoList()) {
                diagnosticoListDiagnosticoToAttach = em.getReference(diagnosticoListDiagnosticoToAttach.getClass(), diagnosticoListDiagnosticoToAttach.getIdDiagnostico());
                attachedDiagnosticoList.add(diagnosticoListDiagnosticoToAttach);
            }
            historiaClinica.setDiagnosticoList(attachedDiagnosticoList);
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : historiaClinica.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            historiaClinica.setPacienteList(attachedPacienteList);
            em.persist(historiaClinica);
            if (idTrabajador != null) {
                idTrabajador.getHistoriaClinicaList().add(historiaClinica);
                idTrabajador = em.merge(idTrabajador);
            }
            for (FormatoDeEvolucion formatoDeEvolucionListFormatoDeEvolucion : historiaClinica.getFormatoDeEvolucionList()) {
                HistoriaClinica oldIdHistoriaClinicaOfFormatoDeEvolucionListFormatoDeEvolucion = formatoDeEvolucionListFormatoDeEvolucion.getIdHistoriaClinica();
                formatoDeEvolucionListFormatoDeEvolucion.setIdHistoriaClinica(historiaClinica);
                formatoDeEvolucionListFormatoDeEvolucion = em.merge(formatoDeEvolucionListFormatoDeEvolucion);
                if (oldIdHistoriaClinicaOfFormatoDeEvolucionListFormatoDeEvolucion != null) {
                    oldIdHistoriaClinicaOfFormatoDeEvolucionListFormatoDeEvolucion.getFormatoDeEvolucionList().remove(formatoDeEvolucionListFormatoDeEvolucion);
                    oldIdHistoriaClinicaOfFormatoDeEvolucionListFormatoDeEvolucion = em.merge(oldIdHistoriaClinicaOfFormatoDeEvolucionListFormatoDeEvolucion);
                }
            }
            for (Tratamiento tratamientoListTratamiento : historiaClinica.getTratamientoList()) {
                HistoriaClinica oldIdHistoriaClinicaOfTratamientoListTratamiento = tratamientoListTratamiento.getIdHistoriaClinica();
                tratamientoListTratamiento.setIdHistoriaClinica(historiaClinica);
                tratamientoListTratamiento = em.merge(tratamientoListTratamiento);
                if (oldIdHistoriaClinicaOfTratamientoListTratamiento != null) {
                    oldIdHistoriaClinicaOfTratamientoListTratamiento.getTratamientoList().remove(tratamientoListTratamiento);
                    oldIdHistoriaClinicaOfTratamientoListTratamiento = em.merge(oldIdHistoriaClinicaOfTratamientoListTratamiento);
                }
            }
            for (Diagnostico diagnosticoListDiagnostico : historiaClinica.getDiagnosticoList()) {
                HistoriaClinica oldIdHistoriaClinicaOfDiagnosticoListDiagnostico = diagnosticoListDiagnostico.getIdHistoriaClinica();
                diagnosticoListDiagnostico.setIdHistoriaClinica(historiaClinica);
                diagnosticoListDiagnostico = em.merge(diagnosticoListDiagnostico);
                if (oldIdHistoriaClinicaOfDiagnosticoListDiagnostico != null) {
                    oldIdHistoriaClinicaOfDiagnosticoListDiagnostico.getDiagnosticoList().remove(diagnosticoListDiagnostico);
                    oldIdHistoriaClinicaOfDiagnosticoListDiagnostico = em.merge(oldIdHistoriaClinicaOfDiagnosticoListDiagnostico);
                }
            }
            for (Paciente pacienteListPaciente : historiaClinica.getPacienteList()) {
                HistoriaClinica oldIdHistoriaClinicaOfPacienteListPaciente = pacienteListPaciente.getIdHistoriaClinica();
                pacienteListPaciente.setIdHistoriaClinica(historiaClinica);
                pacienteListPaciente = em.merge(pacienteListPaciente);
                if (oldIdHistoriaClinicaOfPacienteListPaciente != null) {
                    oldIdHistoriaClinicaOfPacienteListPaciente.getPacienteList().remove(pacienteListPaciente);
                    oldIdHistoriaClinicaOfPacienteListPaciente = em.merge(oldIdHistoriaClinicaOfPacienteListPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHistoriaClinica(historiaClinica.getIdHistoriaClinica()) != null) {
                throw new PreexistingEntityException("HistoriaClinica " + historiaClinica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoriaClinica historiaClinica) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            HistoriaClinica persistentHistoriaClinica = em.find(HistoriaClinica.class, historiaClinica.getIdHistoriaClinica());
            Trabajador idTrabajadorOld = persistentHistoriaClinica.getIdTrabajador();
            Trabajador idTrabajadorNew = historiaClinica.getIdTrabajador();
            List<FormatoDeEvolucion> formatoDeEvolucionListOld = persistentHistoriaClinica.getFormatoDeEvolucionList();
            List<FormatoDeEvolucion> formatoDeEvolucionListNew = historiaClinica.getFormatoDeEvolucionList();
            List<Tratamiento> tratamientoListOld = persistentHistoriaClinica.getTratamientoList();
            List<Tratamiento> tratamientoListNew = historiaClinica.getTratamientoList();
            List<Diagnostico> diagnosticoListOld = persistentHistoriaClinica.getDiagnosticoList();
            List<Diagnostico> diagnosticoListNew = historiaClinica.getDiagnosticoList();
            List<Paciente> pacienteListOld = persistentHistoriaClinica.getPacienteList();
            List<Paciente> pacienteListNew = historiaClinica.getPacienteList();
            List<String> illegalOrphanMessages = null;
            for (FormatoDeEvolucion formatoDeEvolucionListOldFormatoDeEvolucion : formatoDeEvolucionListOld) {
                if (!formatoDeEvolucionListNew.contains(formatoDeEvolucionListOldFormatoDeEvolucion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FormatoDeEvolucion " + formatoDeEvolucionListOldFormatoDeEvolucion + " since its idHistoriaClinica field is not nullable.");
                }
            }
            for (Tratamiento tratamientoListOldTratamiento : tratamientoListOld) {
                if (!tratamientoListNew.contains(tratamientoListOldTratamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tratamiento " + tratamientoListOldTratamiento + " since its idHistoriaClinica field is not nullable.");
                }
            }
            for (Diagnostico diagnosticoListOldDiagnostico : diagnosticoListOld) {
                if (!diagnosticoListNew.contains(diagnosticoListOldDiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Diagnostico " + diagnosticoListOldDiagnostico + " since its idHistoriaClinica field is not nullable.");
                }
            }
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteListOldPaciente + " since its idHistoriaClinica field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTrabajadorNew != null) {
                idTrabajadorNew = em.getReference(idTrabajadorNew.getClass(), idTrabajadorNew.getIdTrabajador());
                historiaClinica.setIdTrabajador(idTrabajadorNew);
            }
            List<FormatoDeEvolucion> attachedFormatoDeEvolucionListNew = new ArrayList<FormatoDeEvolucion>();
            for (FormatoDeEvolucion formatoDeEvolucionListNewFormatoDeEvolucionToAttach : formatoDeEvolucionListNew) {
                formatoDeEvolucionListNewFormatoDeEvolucionToAttach = em.getReference(formatoDeEvolucionListNewFormatoDeEvolucionToAttach.getClass(), formatoDeEvolucionListNewFormatoDeEvolucionToAttach.getIdFormatodeevolucion());
                attachedFormatoDeEvolucionListNew.add(formatoDeEvolucionListNewFormatoDeEvolucionToAttach);
            }
            formatoDeEvolucionListNew = attachedFormatoDeEvolucionListNew;
            historiaClinica.setFormatoDeEvolucionList(formatoDeEvolucionListNew);
            List<Tratamiento> attachedTratamientoListNew = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListNewTratamientoToAttach : tratamientoListNew) {
                tratamientoListNewTratamientoToAttach = em.getReference(tratamientoListNewTratamientoToAttach.getClass(), tratamientoListNewTratamientoToAttach.getIdTratamiento());
                attachedTratamientoListNew.add(tratamientoListNewTratamientoToAttach);
            }
            tratamientoListNew = attachedTratamientoListNew;
            historiaClinica.setTratamientoList(tratamientoListNew);
            List<Diagnostico> attachedDiagnosticoListNew = new ArrayList<Diagnostico>();
            for (Diagnostico diagnosticoListNewDiagnosticoToAttach : diagnosticoListNew) {
                diagnosticoListNewDiagnosticoToAttach = em.getReference(diagnosticoListNewDiagnosticoToAttach.getClass(), diagnosticoListNewDiagnosticoToAttach.getIdDiagnostico());
                attachedDiagnosticoListNew.add(diagnosticoListNewDiagnosticoToAttach);
            }
            diagnosticoListNew = attachedDiagnosticoListNew;
            historiaClinica.setDiagnosticoList(diagnosticoListNew);
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            historiaClinica.setPacienteList(pacienteListNew);
            historiaClinica = em.merge(historiaClinica);
            if (idTrabajadorOld != null && !idTrabajadorOld.equals(idTrabajadorNew)) {
                idTrabajadorOld.getHistoriaClinicaList().remove(historiaClinica);
                idTrabajadorOld = em.merge(idTrabajadorOld);
            }
            if (idTrabajadorNew != null && !idTrabajadorNew.equals(idTrabajadorOld)) {
                idTrabajadorNew.getHistoriaClinicaList().add(historiaClinica);
                idTrabajadorNew = em.merge(idTrabajadorNew);
            }
            for (FormatoDeEvolucion formatoDeEvolucionListNewFormatoDeEvolucion : formatoDeEvolucionListNew) {
                if (!formatoDeEvolucionListOld.contains(formatoDeEvolucionListNewFormatoDeEvolucion)) {
                    HistoriaClinica oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion = formatoDeEvolucionListNewFormatoDeEvolucion.getIdHistoriaClinica();
                    formatoDeEvolucionListNewFormatoDeEvolucion.setIdHistoriaClinica(historiaClinica);
                    formatoDeEvolucionListNewFormatoDeEvolucion = em.merge(formatoDeEvolucionListNewFormatoDeEvolucion);
                    if (oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion != null && !oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion.equals(historiaClinica)) {
                        oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion.getFormatoDeEvolucionList().remove(formatoDeEvolucionListNewFormatoDeEvolucion);
                        oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion = em.merge(oldIdHistoriaClinicaOfFormatoDeEvolucionListNewFormatoDeEvolucion);
                    }
                }
            }
            for (Tratamiento tratamientoListNewTratamiento : tratamientoListNew) {
                if (!tratamientoListOld.contains(tratamientoListNewTratamiento)) {
                    HistoriaClinica oldIdHistoriaClinicaOfTratamientoListNewTratamiento = tratamientoListNewTratamiento.getIdHistoriaClinica();
                    tratamientoListNewTratamiento.setIdHistoriaClinica(historiaClinica);
                    tratamientoListNewTratamiento = em.merge(tratamientoListNewTratamiento);
                    if (oldIdHistoriaClinicaOfTratamientoListNewTratamiento != null && !oldIdHistoriaClinicaOfTratamientoListNewTratamiento.equals(historiaClinica)) {
                        oldIdHistoriaClinicaOfTratamientoListNewTratamiento.getTratamientoList().remove(tratamientoListNewTratamiento);
                        oldIdHistoriaClinicaOfTratamientoListNewTratamiento = em.merge(oldIdHistoriaClinicaOfTratamientoListNewTratamiento);
                    }
                }
            }
            for (Diagnostico diagnosticoListNewDiagnostico : diagnosticoListNew) {
                if (!diagnosticoListOld.contains(diagnosticoListNewDiagnostico)) {
                    HistoriaClinica oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico = diagnosticoListNewDiagnostico.getIdHistoriaClinica();
                    diagnosticoListNewDiagnostico.setIdHistoriaClinica(historiaClinica);
                    diagnosticoListNewDiagnostico = em.merge(diagnosticoListNewDiagnostico);
                    if (oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico != null && !oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico.equals(historiaClinica)) {
                        oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico.getDiagnosticoList().remove(diagnosticoListNewDiagnostico);
                        oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico = em.merge(oldIdHistoriaClinicaOfDiagnosticoListNewDiagnostico);
                    }
                }
            }
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    HistoriaClinica oldIdHistoriaClinicaOfPacienteListNewPaciente = pacienteListNewPaciente.getIdHistoriaClinica();
                    pacienteListNewPaciente.setIdHistoriaClinica(historiaClinica);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                    if (oldIdHistoriaClinicaOfPacienteListNewPaciente != null && !oldIdHistoriaClinicaOfPacienteListNewPaciente.equals(historiaClinica)) {
                        oldIdHistoriaClinicaOfPacienteListNewPaciente.getPacienteList().remove(pacienteListNewPaciente);
                        oldIdHistoriaClinicaOfPacienteListNewPaciente = em.merge(oldIdHistoriaClinicaOfPacienteListNewPaciente);
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
                Integer id = historiaClinica.getIdHistoriaClinica();
                if (findHistoriaClinica(id) == null) {
                    throw new NonexistentEntityException("The historiaClinica with id " + id + " no longer exists.");
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
            HistoriaClinica historiaClinica;
            try {
                historiaClinica = em.getReference(HistoriaClinica.class, id);
                historiaClinica.getIdHistoriaClinica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historiaClinica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FormatoDeEvolucion> formatoDeEvolucionListOrphanCheck = historiaClinica.getFormatoDeEvolucionList();
            for (FormatoDeEvolucion formatoDeEvolucionListOrphanCheckFormatoDeEvolucion : formatoDeEvolucionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HistoriaClinica (" + historiaClinica + ") cannot be destroyed since the FormatoDeEvolucion " + formatoDeEvolucionListOrphanCheckFormatoDeEvolucion + " in its formatoDeEvolucionList field has a non-nullable idHistoriaClinica field.");
            }
            List<Tratamiento> tratamientoListOrphanCheck = historiaClinica.getTratamientoList();
            for (Tratamiento tratamientoListOrphanCheckTratamiento : tratamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HistoriaClinica (" + historiaClinica + ") cannot be destroyed since the Tratamiento " + tratamientoListOrphanCheckTratamiento + " in its tratamientoList field has a non-nullable idHistoriaClinica field.");
            }
            List<Diagnostico> diagnosticoListOrphanCheck = historiaClinica.getDiagnosticoList();
            for (Diagnostico diagnosticoListOrphanCheckDiagnostico : diagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HistoriaClinica (" + historiaClinica + ") cannot be destroyed since the Diagnostico " + diagnosticoListOrphanCheckDiagnostico + " in its diagnosticoList field has a non-nullable idHistoriaClinica field.");
            }
            List<Paciente> pacienteListOrphanCheck = historiaClinica.getPacienteList();
            for (Paciente pacienteListOrphanCheckPaciente : pacienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HistoriaClinica (" + historiaClinica + ") cannot be destroyed since the Paciente " + pacienteListOrphanCheckPaciente + " in its pacienteList field has a non-nullable idHistoriaClinica field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Trabajador idTrabajador = historiaClinica.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador.getHistoriaClinicaList().remove(historiaClinica);
                idTrabajador = em.merge(idTrabajador);
            }
            em.remove(historiaClinica);
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

    public List<HistoriaClinica> findHistoriaClinicaEntities() {
        return findHistoriaClinicaEntities(true, -1, -1);
    }

    public List<HistoriaClinica> findHistoriaClinicaEntities(int maxResults, int firstResult) {
        return findHistoriaClinicaEntities(false, maxResults, firstResult);
    }

    private List<HistoriaClinica> findHistoriaClinicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoriaClinica.class));
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

    public HistoriaClinica findHistoriaClinica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoriaClinica.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriaClinicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoriaClinica> rt = cq.from(HistoriaClinica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
