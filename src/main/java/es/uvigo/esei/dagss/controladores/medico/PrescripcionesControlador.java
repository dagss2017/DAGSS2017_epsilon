/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "prescripcionesControlador")
@SessionScoped
public class PrescripcionesControlador implements Serializable{
    
    @Inject
    PrescripcionDAO prescripcionDAO;
    
    List<Prescripcion> prescripciones;
    Prescripcion prescripcionActual;
    
    
    public PrescripcionesControlador(){
    }
    
    public List<Prescripcion> loadPrescripciones(Paciente paciente){
        this.prescripciones = this.prescripcionDAO.getRecetasPaciente(paciente);
        return this.prescripciones;
    }

    public PrescripcionDAO getPrescripcionDAO() {
        return prescripcionDAO;
    }

    public void setPrescripcionDAO(PrescripcionDAO prescripcionDAO) {
        this.prescripcionDAO = prescripcionDAO;
    }

    public List<Prescripcion> getPrescripciones() {
        return prescripciones;
    }

    public void setPrescripciones(List<Prescripcion> prescripciones) {
        this.prescripciones = prescripciones;
    }

    public Prescripcion getPrescripcionActual() {
        return prescripcionActual;
    }

    public void setPrescripcionActual(Prescripcion prescripcionActual) {
        this.prescripcionActual = prescripcionActual;
    }
    
    
    
}
