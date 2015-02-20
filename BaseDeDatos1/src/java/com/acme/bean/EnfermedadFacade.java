/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.bean;

import com.acme.entities.Enfermedad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cpe
 */
@Stateless
public class EnfermedadFacade extends AbstractFacade<Enfermedad> {
    @PersistenceContext(unitName = "BaseDeDatos1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnfermedadFacade() {
        super(Enfermedad.class);
    }
    
}
