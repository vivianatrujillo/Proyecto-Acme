/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.bean;

import com.acme.entities.PacienteEnfermedadEnfermedad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cpe
 */
@Stateless
public class PacienteEnfermedadEnfermedadFacade extends AbstractFacade<PacienteEnfermedadEnfermedad> {
    @PersistenceContext(unitName = "BaseDeDatos1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PacienteEnfermedadEnfermedadFacade() {
        super(PacienteEnfermedadEnfermedad.class);
    }
    
}