/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.model;

import com.acme.entities.HistoriaClinica;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author usuario
 */
@Stateless
public class HistoriaClinicaFacade extends AbstractFacade<HistoriaClinica> {
    @PersistenceContext(unitName = "BaseDeDatosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoriaClinicaFacade() {
        super(HistoriaClinica.class);
    }
    
}
