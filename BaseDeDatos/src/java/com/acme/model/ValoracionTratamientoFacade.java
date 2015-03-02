/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.model;

import com.acme.entities.ValoracionTratamiento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author usuario
 */
@Stateless
public class ValoracionTratamientoFacade extends AbstractFacade<ValoracionTratamiento> {
    @PersistenceContext(unitName = "BaseDeDatosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ValoracionTratamientoFacade() {
        super(ValoracionTratamiento.class);
    }
    
}
