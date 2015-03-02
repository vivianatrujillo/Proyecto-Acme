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
import com.acme.entities.Rol;
import java.util.ArrayList;
import java.util.List;
import com.acme.entities.HistoriaClinica;
import com.acme.entities.Paciente;
import com.acme.entities.Trabajador;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class TrabajadorJpaController implements Serializable {

    public TrabajadorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trabajador trabajador) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (trabajador.getRolList() == null) {
            trabajador.setRolList(new ArrayList<Rol>());
        }
        if (trabajador.getHistoriaClinicaList() == null) {
            trabajador.setHistoriaClinicaList(new ArrayList<HistoriaClinica>());
        }
        if (trabajador.getPacienteList() == null) {
            trabajador.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : trabajador.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getCodRol());
                attachedRolList.add(rolListRolToAttach);
            }
            trabajador.setRolList(attachedRolList);
            List<HistoriaClinica> attachedHistoriaClinicaList = new ArrayList<HistoriaClinica>();
            for (HistoriaClinica historiaClinicaListHistoriaClinicaToAttach : trabajador.getHistoriaClinicaList()) {
                historiaClinicaListHistoriaClinicaToAttach = em.getReference(historiaClinicaListHistoriaClinicaToAttach.getClass(), historiaClinicaListHistoriaClinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaClinicaList.add(historiaClinicaListHistoriaClinicaToAttach);
            }
            trabajador.setHistoriaClinicaList(attachedHistoriaClinicaList);
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : trabajador.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            trabajador.setPacienteList(attachedPacienteList);
            em.persist(trabajador);
            for (Rol rolListRol : trabajador.getRolList()) {
                Trabajador oldIdTrabajadorOfRolListRol = rolListRol.getIdTrabajador();
                rolListRol.setIdTrabajador(trabajador);
                rolListRol = em.merge(rolListRol);
                if (oldIdTrabajadorOfRolListRol != null) {
                    oldIdTrabajadorOfRolListRol.getRolList().remove(rolListRol);
                    oldIdTrabajadorOfRolListRol = em.merge(oldIdTrabajadorOfRolListRol);
                }
            }
            for (HistoriaClinica historiaClinicaListHistoriaClinica : trabajador.getHistoriaClinicaList()) {
                Trabajador oldIdTrabajadorOfHistoriaClinicaListHistoriaClinica = historiaClinicaListHistoriaClinica.getIdTrabajador();
                historiaClinicaListHistoriaClinica.setIdTrabajador(trabajador);
                historiaClinicaListHistoriaClinica = em.merge(historiaClinicaListHistoriaClinica);
                if (oldIdTrabajadorOfHistoriaClinicaListHistoriaClinica != null) {
                    oldIdTrabajadorOfHistoriaClinicaListHistoriaClinica.getHistoriaClinicaList().remove(historiaClinicaListHistoriaClinica);
                    oldIdTrabajadorOfHistoriaClinicaListHistoriaClinica = em.merge(oldIdTrabajadorOfHistoriaClinicaListHistoriaClinica);
                }
            }
            for (Paciente pacienteListPaciente : trabajador.getPacienteList()) {
                Trabajador oldIdTrabajadorOfPacienteListPaciente = pacienteListPaciente.getIdTrabajador();
                pacienteListPaciente.setIdTrabajador(trabajador);
                pacienteListPaciente = em.merge(pacienteListPaciente);
                if (oldIdTrabajadorOfPacienteListPaciente != null) {
                    oldIdTrabajadorOfPacienteListPaciente.getPacienteList().remove(pacienteListPaciente);
                    oldIdTrabajadorOfPacienteListPaciente = em.merge(oldIdTrabajadorOfPacienteListPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTrabajador(trabajador.getIdTrabajador()) != null) {
                throw new PreexistingEntityException("Trabajador " + trabajador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trabajador trabajador) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trabajador persistentTrabajador = em.find(Trabajador.class, trabajador.getIdTrabajador());
            List<Rol> rolListOld = persistentTrabajador.getRolList();
            List<Rol> rolListNew = trabajador.getRolList();
            List<HistoriaClinica> historiaClinicaListOld = persistentTrabajador.getHistoriaClinicaList();
            List<HistoriaClinica> historiaClinicaListNew = trabajador.getHistoriaClinicaList();
            List<Paciente> pacienteListOld = persistentTrabajador.getPacienteList();
            List<Paciente> pacienteListNew = trabajador.getPacienteList();
            List<String> illegalOrphanMessages = null;
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rol " + rolListOldRol + " since its idTrabajador field is not nullable.");
                }
            }
            for (HistoriaClinica historiaClinicaListOldHistoriaClinica : historiaClinicaListOld) {
                if (!historiaClinicaListNew.contains(historiaClinicaListOldHistoriaClinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoriaClinica " + historiaClinicaListOldHistoriaClinica + " since its idTrabajador field is not nullable.");
                }
            }
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteListOldPaciente + " since its idTrabajador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getCodRol());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            trabajador.setRolList(rolListNew);
            List<HistoriaClinica> attachedHistoriaClinicaListNew = new ArrayList<HistoriaClinica>();
            for (HistoriaClinica historiaClinicaListNewHistoriaClinicaToAttach : historiaClinicaListNew) {
                historiaClinicaListNewHistoriaClinicaToAttach = em.getReference(historiaClinicaListNewHistoriaClinicaToAttach.getClass(), historiaClinicaListNewHistoriaClinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaClinicaListNew.add(historiaClinicaListNewHistoriaClinicaToAttach);
            }
            historiaClinicaListNew = attachedHistoriaClinicaListNew;
            trabajador.setHistoriaClinicaList(historiaClinicaListNew);
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            trabajador.setPacienteList(pacienteListNew);
            trabajador = em.merge(trabajador);
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    Trabajador oldIdTrabajadorOfRolListNewRol = rolListNewRol.getIdTrabajador();
                    rolListNewRol.setIdTrabajador(trabajador);
                    rolListNewRol = em.merge(rolListNewRol);
                    if (oldIdTrabajadorOfRolListNewRol != null && !oldIdTrabajadorOfRolListNewRol.equals(trabajador)) {
                        oldIdTrabajadorOfRolListNewRol.getRolList().remove(rolListNewRol);
                        oldIdTrabajadorOfRolListNewRol = em.merge(oldIdTrabajadorOfRolListNewRol);
                    }
                }
            }
            for (HistoriaClinica historiaClinicaListNewHistoriaClinica : historiaClinicaListNew) {
                if (!historiaClinicaListOld.contains(historiaClinicaListNewHistoriaClinica)) {
                    Trabajador oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica = historiaClinicaListNewHistoriaClinica.getIdTrabajador();
                    historiaClinicaListNewHistoriaClinica.setIdTrabajador(trabajador);
                    historiaClinicaListNewHistoriaClinica = em.merge(historiaClinicaListNewHistoriaClinica);
                    if (oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica != null && !oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica.equals(trabajador)) {
                        oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica.getHistoriaClinicaList().remove(historiaClinicaListNewHistoriaClinica);
                        oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica = em.merge(oldIdTrabajadorOfHistoriaClinicaListNewHistoriaClinica);
                    }
                }
            }
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    Trabajador oldIdTrabajadorOfPacienteListNewPaciente = pacienteListNewPaciente.getIdTrabajador();
                    pacienteListNewPaciente.setIdTrabajador(trabajador);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                    if (oldIdTrabajadorOfPacienteListNewPaciente != null && !oldIdTrabajadorOfPacienteListNewPaciente.equals(trabajador)) {
                        oldIdTrabajadorOfPacienteListNewPaciente.getPacienteList().remove(pacienteListNewPaciente);
                        oldIdTrabajadorOfPacienteListNewPaciente = em.merge(oldIdTrabajadorOfPacienteListNewPaciente);
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
                Integer id = trabajador.getIdTrabajador();
                if (findTrabajador(id) == null) {
                    throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.");
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
            Trabajador trabajador;
            try {
                trabajador = em.getReference(Trabajador.class, id);
                trabajador.getIdTrabajador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rol> rolListOrphanCheck = trabajador.getRolList();
            for (Rol rolListOrphanCheckRol : rolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Rol " + rolListOrphanCheckRol + " in its rolList field has a non-nullable idTrabajador field.");
            }
            List<HistoriaClinica> historiaClinicaListOrphanCheck = trabajador.getHistoriaClinicaList();
            for (HistoriaClinica historiaClinicaListOrphanCheckHistoriaClinica : historiaClinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the HistoriaClinica " + historiaClinicaListOrphanCheckHistoriaClinica + " in its historiaClinicaList field has a non-nullable idTrabajador field.");
            }
            List<Paciente> pacienteListOrphanCheck = trabajador.getPacienteList();
            for (Paciente pacienteListOrphanCheckPaciente : pacienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Paciente " + pacienteListOrphanCheckPaciente + " in its pacienteList field has a non-nullable idTrabajador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(trabajador);
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

    public List<Trabajador> findTrabajadorEntities() {
        return findTrabajadorEntities(true, -1, -1);
    }

    public List<Trabajador> findTrabajadorEntities(int maxResults, int firstResult) {
        return findTrabajadorEntities(false, maxResults, firstResult);
    }

    private List<Trabajador> findTrabajadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trabajador.class));
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

    public Trabajador findTrabajador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trabajador.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrabajadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trabajador> rt = cq.from(Trabajador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
