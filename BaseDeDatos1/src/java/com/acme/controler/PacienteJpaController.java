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
import com.acme.entities.Eps;
import com.acme.entities.Genero;
import com.acme.entities.HistoriaClinica;
import com.acme.entities.Rh;
import com.acme.entities.Trabajador;
import com.acme.entities.Valoracion;
import java.util.ArrayList;
import java.util.List;
import com.acme.entities.PacienteReferencia;
import com.acme.entities.Cita;
import com.acme.entities.Paciente;
import com.acme.entities.PacienteEnfermedad;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (paciente.getValoracionList() == null) {
            paciente.setValoracionList(new ArrayList<Valoracion>());
        }
        if (paciente.getPacienteReferenciaList() == null) {
            paciente.setPacienteReferenciaList(new ArrayList<PacienteReferencia>());
        }
        if (paciente.getCitaList() == null) {
            paciente.setCitaList(new ArrayList<Cita>());
        }
        if (paciente.getPacienteEnfermedadList() == null) {
            paciente.setPacienteEnfermedadList(new ArrayList<PacienteEnfermedad>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Eps idEps = paciente.getIdEps();
            if (idEps != null) {
                idEps = em.getReference(idEps.getClass(), idEps.getIdEps());
                paciente.setIdEps(idEps);
            }
            Genero idGenero = paciente.getIdGenero();
            if (idGenero != null) {
                idGenero = em.getReference(idGenero.getClass(), idGenero.getIdGenero());
                paciente.setIdGenero(idGenero);
            }
            HistoriaClinica idHistoriaClinica = paciente.getIdHistoriaClinica();
            if (idHistoriaClinica != null) {
                idHistoriaClinica = em.getReference(idHistoriaClinica.getClass(), idHistoriaClinica.getIdHistoriaClinica());
                paciente.setIdHistoriaClinica(idHistoriaClinica);
            }
            Rh idRH = paciente.getIdRH();
            if (idRH != null) {
                idRH = em.getReference(idRH.getClass(), idRH.getIdRH());
                paciente.setIdRH(idRH);
            }
            Trabajador idTrabajador = paciente.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador = em.getReference(idTrabajador.getClass(), idTrabajador.getIdTrabajador());
                paciente.setIdTrabajador(idTrabajador);
            }
            List<Valoracion> attachedValoracionList = new ArrayList<Valoracion>();
            for (Valoracion valoracionListValoracionToAttach : paciente.getValoracionList()) {
                valoracionListValoracionToAttach = em.getReference(valoracionListValoracionToAttach.getClass(), valoracionListValoracionToAttach.getIdValoracion());
                attachedValoracionList.add(valoracionListValoracionToAttach);
            }
            paciente.setValoracionList(attachedValoracionList);
            List<PacienteReferencia> attachedPacienteReferenciaList = new ArrayList<PacienteReferencia>();
            for (PacienteReferencia pacienteReferenciaListPacienteReferenciaToAttach : paciente.getPacienteReferenciaList()) {
                pacienteReferenciaListPacienteReferenciaToAttach = em.getReference(pacienteReferenciaListPacienteReferenciaToAttach.getClass(), pacienteReferenciaListPacienteReferenciaToAttach.getCodPacienteReferencia());
                attachedPacienteReferenciaList.add(pacienteReferenciaListPacienteReferenciaToAttach);
            }
            paciente.setPacienteReferenciaList(attachedPacienteReferenciaList);
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : paciente.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getIdCita());
                attachedCitaList.add(citaListCitaToAttach);
            }
            paciente.setCitaList(attachedCitaList);
            List<PacienteEnfermedad> attachedPacienteEnfermedadList = new ArrayList<PacienteEnfermedad>();
            for (PacienteEnfermedad pacienteEnfermedadListPacienteEnfermedadToAttach : paciente.getPacienteEnfermedadList()) {
                pacienteEnfermedadListPacienteEnfermedadToAttach = em.getReference(pacienteEnfermedadListPacienteEnfermedadToAttach.getClass(), pacienteEnfermedadListPacienteEnfermedadToAttach.getCodPacienteEnfermedadcol());
                attachedPacienteEnfermedadList.add(pacienteEnfermedadListPacienteEnfermedadToAttach);
            }
            paciente.setPacienteEnfermedadList(attachedPacienteEnfermedadList);
            em.persist(paciente);
            if (idEps != null) {
                idEps.getPacienteList().add(paciente);
                idEps = em.merge(idEps);
            }
            if (idGenero != null) {
                idGenero.getPacienteList().add(paciente);
                idGenero = em.merge(idGenero);
            }
            if (idHistoriaClinica != null) {
                idHistoriaClinica.getPacienteList().add(paciente);
                idHistoriaClinica = em.merge(idHistoriaClinica);
            }
            if (idRH != null) {
                idRH.getPacienteList().add(paciente);
                idRH = em.merge(idRH);
            }
            if (idTrabajador != null) {
                idTrabajador.getPacienteList().add(paciente);
                idTrabajador = em.merge(idTrabajador);
            }
            for (Valoracion valoracionListValoracion : paciente.getValoracionList()) {
                Paciente oldIdPacienteOfValoracionListValoracion = valoracionListValoracion.getIdPaciente();
                valoracionListValoracion.setIdPaciente(paciente);
                valoracionListValoracion = em.merge(valoracionListValoracion);
                if (oldIdPacienteOfValoracionListValoracion != null) {
                    oldIdPacienteOfValoracionListValoracion.getValoracionList().remove(valoracionListValoracion);
                    oldIdPacienteOfValoracionListValoracion = em.merge(oldIdPacienteOfValoracionListValoracion);
                }
            }
            for (PacienteReferencia pacienteReferenciaListPacienteReferencia : paciente.getPacienteReferenciaList()) {
                Paciente oldIdPacienteOfPacienteReferenciaListPacienteReferencia = pacienteReferenciaListPacienteReferencia.getIdPaciente();
                pacienteReferenciaListPacienteReferencia.setIdPaciente(paciente);
                pacienteReferenciaListPacienteReferencia = em.merge(pacienteReferenciaListPacienteReferencia);
                if (oldIdPacienteOfPacienteReferenciaListPacienteReferencia != null) {
                    oldIdPacienteOfPacienteReferenciaListPacienteReferencia.getPacienteReferenciaList().remove(pacienteReferenciaListPacienteReferencia);
                    oldIdPacienteOfPacienteReferenciaListPacienteReferencia = em.merge(oldIdPacienteOfPacienteReferenciaListPacienteReferencia);
                }
            }
            for (Cita citaListCita : paciente.getCitaList()) {
                Paciente oldIdPacienteOfCitaListCita = citaListCita.getIdPaciente();
                citaListCita.setIdPaciente(paciente);
                citaListCita = em.merge(citaListCita);
                if (oldIdPacienteOfCitaListCita != null) {
                    oldIdPacienteOfCitaListCita.getCitaList().remove(citaListCita);
                    oldIdPacienteOfCitaListCita = em.merge(oldIdPacienteOfCitaListCita);
                }
            }
            for (PacienteEnfermedad pacienteEnfermedadListPacienteEnfermedad : paciente.getPacienteEnfermedadList()) {
                Paciente oldIdPacienteOfPacienteEnfermedadListPacienteEnfermedad = pacienteEnfermedadListPacienteEnfermedad.getIdPaciente();
                pacienteEnfermedadListPacienteEnfermedad.setIdPaciente(paciente);
                pacienteEnfermedadListPacienteEnfermedad = em.merge(pacienteEnfermedadListPacienteEnfermedad);
                if (oldIdPacienteOfPacienteEnfermedadListPacienteEnfermedad != null) {
                    oldIdPacienteOfPacienteEnfermedadListPacienteEnfermedad.getPacienteEnfermedadList().remove(pacienteEnfermedadListPacienteEnfermedad);
                    oldIdPacienteOfPacienteEnfermedadListPacienteEnfermedad = em.merge(oldIdPacienteOfPacienteEnfermedadListPacienteEnfermedad);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPaciente(paciente.getIdPaciente()) != null) {
                throw new PreexistingEntityException("Paciente " + paciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getIdPaciente());
            Eps idEpsOld = persistentPaciente.getIdEps();
            Eps idEpsNew = paciente.getIdEps();
            Genero idGeneroOld = persistentPaciente.getIdGenero();
            Genero idGeneroNew = paciente.getIdGenero();
            HistoriaClinica idHistoriaClinicaOld = persistentPaciente.getIdHistoriaClinica();
            HistoriaClinica idHistoriaClinicaNew = paciente.getIdHistoriaClinica();
            Rh idRHOld = persistentPaciente.getIdRH();
            Rh idRHNew = paciente.getIdRH();
            Trabajador idTrabajadorOld = persistentPaciente.getIdTrabajador();
            Trabajador idTrabajadorNew = paciente.getIdTrabajador();
            List<Valoracion> valoracionListOld = persistentPaciente.getValoracionList();
            List<Valoracion> valoracionListNew = paciente.getValoracionList();
            List<PacienteReferencia> pacienteReferenciaListOld = persistentPaciente.getPacienteReferenciaList();
            List<PacienteReferencia> pacienteReferenciaListNew = paciente.getPacienteReferenciaList();
            List<Cita> citaListOld = persistentPaciente.getCitaList();
            List<Cita> citaListNew = paciente.getCitaList();
            List<PacienteEnfermedad> pacienteEnfermedadListOld = persistentPaciente.getPacienteEnfermedadList();
            List<PacienteEnfermedad> pacienteEnfermedadListNew = paciente.getPacienteEnfermedadList();
            List<String> illegalOrphanMessages = null;
            for (Valoracion valoracionListOldValoracion : valoracionListOld) {
                if (!valoracionListNew.contains(valoracionListOldValoracion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Valoracion " + valoracionListOldValoracion + " since its idPaciente field is not nullable.");
                }
            }
            for (PacienteReferencia pacienteReferenciaListOldPacienteReferencia : pacienteReferenciaListOld) {
                if (!pacienteReferenciaListNew.contains(pacienteReferenciaListOldPacienteReferencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteReferencia " + pacienteReferenciaListOldPacienteReferencia + " since its idPaciente field is not nullable.");
                }
            }
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its idPaciente field is not nullable.");
                }
            }
            for (PacienteEnfermedad pacienteEnfermedadListOldPacienteEnfermedad : pacienteEnfermedadListOld) {
                if (!pacienteEnfermedadListNew.contains(pacienteEnfermedadListOldPacienteEnfermedad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteEnfermedad " + pacienteEnfermedadListOldPacienteEnfermedad + " since its idPaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEpsNew != null) {
                idEpsNew = em.getReference(idEpsNew.getClass(), idEpsNew.getIdEps());
                paciente.setIdEps(idEpsNew);
            }
            if (idGeneroNew != null) {
                idGeneroNew = em.getReference(idGeneroNew.getClass(), idGeneroNew.getIdGenero());
                paciente.setIdGenero(idGeneroNew);
            }
            if (idHistoriaClinicaNew != null) {
                idHistoriaClinicaNew = em.getReference(idHistoriaClinicaNew.getClass(), idHistoriaClinicaNew.getIdHistoriaClinica());
                paciente.setIdHistoriaClinica(idHistoriaClinicaNew);
            }
            if (idRHNew != null) {
                idRHNew = em.getReference(idRHNew.getClass(), idRHNew.getIdRH());
                paciente.setIdRH(idRHNew);
            }
            if (idTrabajadorNew != null) {
                idTrabajadorNew = em.getReference(idTrabajadorNew.getClass(), idTrabajadorNew.getIdTrabajador());
                paciente.setIdTrabajador(idTrabajadorNew);
            }
            List<Valoracion> attachedValoracionListNew = new ArrayList<Valoracion>();
            for (Valoracion valoracionListNewValoracionToAttach : valoracionListNew) {
                valoracionListNewValoracionToAttach = em.getReference(valoracionListNewValoracionToAttach.getClass(), valoracionListNewValoracionToAttach.getIdValoracion());
                attachedValoracionListNew.add(valoracionListNewValoracionToAttach);
            }
            valoracionListNew = attachedValoracionListNew;
            paciente.setValoracionList(valoracionListNew);
            List<PacienteReferencia> attachedPacienteReferenciaListNew = new ArrayList<PacienteReferencia>();
            for (PacienteReferencia pacienteReferenciaListNewPacienteReferenciaToAttach : pacienteReferenciaListNew) {
                pacienteReferenciaListNewPacienteReferenciaToAttach = em.getReference(pacienteReferenciaListNewPacienteReferenciaToAttach.getClass(), pacienteReferenciaListNewPacienteReferenciaToAttach.getCodPacienteReferencia());
                attachedPacienteReferenciaListNew.add(pacienteReferenciaListNewPacienteReferenciaToAttach);
            }
            pacienteReferenciaListNew = attachedPacienteReferenciaListNew;
            paciente.setPacienteReferenciaList(pacienteReferenciaListNew);
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getIdCita());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            paciente.setCitaList(citaListNew);
            List<PacienteEnfermedad> attachedPacienteEnfermedadListNew = new ArrayList<PacienteEnfermedad>();
            for (PacienteEnfermedad pacienteEnfermedadListNewPacienteEnfermedadToAttach : pacienteEnfermedadListNew) {
                pacienteEnfermedadListNewPacienteEnfermedadToAttach = em.getReference(pacienteEnfermedadListNewPacienteEnfermedadToAttach.getClass(), pacienteEnfermedadListNewPacienteEnfermedadToAttach.getCodPacienteEnfermedadcol());
                attachedPacienteEnfermedadListNew.add(pacienteEnfermedadListNewPacienteEnfermedadToAttach);
            }
            pacienteEnfermedadListNew = attachedPacienteEnfermedadListNew;
            paciente.setPacienteEnfermedadList(pacienteEnfermedadListNew);
            paciente = em.merge(paciente);
            if (idEpsOld != null && !idEpsOld.equals(idEpsNew)) {
                idEpsOld.getPacienteList().remove(paciente);
                idEpsOld = em.merge(idEpsOld);
            }
            if (idEpsNew != null && !idEpsNew.equals(idEpsOld)) {
                idEpsNew.getPacienteList().add(paciente);
                idEpsNew = em.merge(idEpsNew);
            }
            if (idGeneroOld != null && !idGeneroOld.equals(idGeneroNew)) {
                idGeneroOld.getPacienteList().remove(paciente);
                idGeneroOld = em.merge(idGeneroOld);
            }
            if (idGeneroNew != null && !idGeneroNew.equals(idGeneroOld)) {
                idGeneroNew.getPacienteList().add(paciente);
                idGeneroNew = em.merge(idGeneroNew);
            }
            if (idHistoriaClinicaOld != null && !idHistoriaClinicaOld.equals(idHistoriaClinicaNew)) {
                idHistoriaClinicaOld.getPacienteList().remove(paciente);
                idHistoriaClinicaOld = em.merge(idHistoriaClinicaOld);
            }
            if (idHistoriaClinicaNew != null && !idHistoriaClinicaNew.equals(idHistoriaClinicaOld)) {
                idHistoriaClinicaNew.getPacienteList().add(paciente);
                idHistoriaClinicaNew = em.merge(idHistoriaClinicaNew);
            }
            if (idRHOld != null && !idRHOld.equals(idRHNew)) {
                idRHOld.getPacienteList().remove(paciente);
                idRHOld = em.merge(idRHOld);
            }
            if (idRHNew != null && !idRHNew.equals(idRHOld)) {
                idRHNew.getPacienteList().add(paciente);
                idRHNew = em.merge(idRHNew);
            }
            if (idTrabajadorOld != null && !idTrabajadorOld.equals(idTrabajadorNew)) {
                idTrabajadorOld.getPacienteList().remove(paciente);
                idTrabajadorOld = em.merge(idTrabajadorOld);
            }
            if (idTrabajadorNew != null && !idTrabajadorNew.equals(idTrabajadorOld)) {
                idTrabajadorNew.getPacienteList().add(paciente);
                idTrabajadorNew = em.merge(idTrabajadorNew);
            }
            for (Valoracion valoracionListNewValoracion : valoracionListNew) {
                if (!valoracionListOld.contains(valoracionListNewValoracion)) {
                    Paciente oldIdPacienteOfValoracionListNewValoracion = valoracionListNewValoracion.getIdPaciente();
                    valoracionListNewValoracion.setIdPaciente(paciente);
                    valoracionListNewValoracion = em.merge(valoracionListNewValoracion);
                    if (oldIdPacienteOfValoracionListNewValoracion != null && !oldIdPacienteOfValoracionListNewValoracion.equals(paciente)) {
                        oldIdPacienteOfValoracionListNewValoracion.getValoracionList().remove(valoracionListNewValoracion);
                        oldIdPacienteOfValoracionListNewValoracion = em.merge(oldIdPacienteOfValoracionListNewValoracion);
                    }
                }
            }
            for (PacienteReferencia pacienteReferenciaListNewPacienteReferencia : pacienteReferenciaListNew) {
                if (!pacienteReferenciaListOld.contains(pacienteReferenciaListNewPacienteReferencia)) {
                    Paciente oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia = pacienteReferenciaListNewPacienteReferencia.getIdPaciente();
                    pacienteReferenciaListNewPacienteReferencia.setIdPaciente(paciente);
                    pacienteReferenciaListNewPacienteReferencia = em.merge(pacienteReferenciaListNewPacienteReferencia);
                    if (oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia != null && !oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia.equals(paciente)) {
                        oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia.getPacienteReferenciaList().remove(pacienteReferenciaListNewPacienteReferencia);
                        oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia = em.merge(oldIdPacienteOfPacienteReferenciaListNewPacienteReferencia);
                    }
                }
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Paciente oldIdPacienteOfCitaListNewCita = citaListNewCita.getIdPaciente();
                    citaListNewCita.setIdPaciente(paciente);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldIdPacienteOfCitaListNewCita != null && !oldIdPacienteOfCitaListNewCita.equals(paciente)) {
                        oldIdPacienteOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldIdPacienteOfCitaListNewCita = em.merge(oldIdPacienteOfCitaListNewCita);
                    }
                }
            }
            for (PacienteEnfermedad pacienteEnfermedadListNewPacienteEnfermedad : pacienteEnfermedadListNew) {
                if (!pacienteEnfermedadListOld.contains(pacienteEnfermedadListNewPacienteEnfermedad)) {
                    Paciente oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad = pacienteEnfermedadListNewPacienteEnfermedad.getIdPaciente();
                    pacienteEnfermedadListNewPacienteEnfermedad.setIdPaciente(paciente);
                    pacienteEnfermedadListNewPacienteEnfermedad = em.merge(pacienteEnfermedadListNewPacienteEnfermedad);
                    if (oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad != null && !oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad.equals(paciente)) {
                        oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad.getPacienteEnfermedadList().remove(pacienteEnfermedadListNewPacienteEnfermedad);
                        oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad = em.merge(oldIdPacienteOfPacienteEnfermedadListNewPacienteEnfermedad);
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
                Integer id = paciente.getIdPaciente();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
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
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getIdPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Valoracion> valoracionListOrphanCheck = paciente.getValoracionList();
            for (Valoracion valoracionListOrphanCheckValoracion : valoracionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Valoracion " + valoracionListOrphanCheckValoracion + " in its valoracionList field has a non-nullable idPaciente field.");
            }
            List<PacienteReferencia> pacienteReferenciaListOrphanCheck = paciente.getPacienteReferenciaList();
            for (PacienteReferencia pacienteReferenciaListOrphanCheckPacienteReferencia : pacienteReferenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the PacienteReferencia " + pacienteReferenciaListOrphanCheckPacienteReferencia + " in its pacienteReferenciaList field has a non-nullable idPaciente field.");
            }
            List<Cita> citaListOrphanCheck = paciente.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable idPaciente field.");
            }
            List<PacienteEnfermedad> pacienteEnfermedadListOrphanCheck = paciente.getPacienteEnfermedadList();
            for (PacienteEnfermedad pacienteEnfermedadListOrphanCheckPacienteEnfermedad : pacienteEnfermedadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the PacienteEnfermedad " + pacienteEnfermedadListOrphanCheckPacienteEnfermedad + " in its pacienteEnfermedadList field has a non-nullable idPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Eps idEps = paciente.getIdEps();
            if (idEps != null) {
                idEps.getPacienteList().remove(paciente);
                idEps = em.merge(idEps);
            }
            Genero idGenero = paciente.getIdGenero();
            if (idGenero != null) {
                idGenero.getPacienteList().remove(paciente);
                idGenero = em.merge(idGenero);
            }
            HistoriaClinica idHistoriaClinica = paciente.getIdHistoriaClinica();
            if (idHistoriaClinica != null) {
                idHistoriaClinica.getPacienteList().remove(paciente);
                idHistoriaClinica = em.merge(idHistoriaClinica);
            }
            Rh idRH = paciente.getIdRH();
            if (idRH != null) {
                idRH.getPacienteList().remove(paciente);
                idRH = em.merge(idRH);
            }
            Trabajador idTrabajador = paciente.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador.getPacienteList().remove(paciente);
                idTrabajador = em.merge(idTrabajador);
            }
            em.remove(paciente);
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

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
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

    public Paciente findPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
