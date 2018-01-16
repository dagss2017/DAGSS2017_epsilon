/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Named(value = "prescripcionesControlador")
@SessionScoped
public class PrescripcionesControlador implements Serializable{
    
    @Inject
    PrescripcionDAO prescripcionDAO;
    
    List<Prescripcion> prescripciones;
    Prescripcion prescripcionActual;
    

    private boolean medicIsSelected;
    
    
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
    
        
    public void doNuevo(Paciente paciente,Medico medico) {
        this.prescripcionActual = new Prescripcion();
        this.prescripcionActual.setPaciente(paciente);
        this.prescripcionActual.setMedico(medico);
        this.prescripcionActual.setFechaInicio(Calendar.getInstance().getTime()); 
        this.prescripcionActual.setDosis(0);
        this.medicIsSelected=false;

    }
    
    
    public String doGuardarNuevo() {

        //this.prescripcionActual = this.prescripcionDAO.crear(prescripcionActual);
        Prescripcion p = new Prescripcion();
        p.setFechaInicio(Calendar.getInstance().getTime());
        p.setDosis(325);
        p.setIndicaciones(this.prescripcionActual.getMedicamento().toString());
        this.prescripcionActual = this.prescripcionDAO.crear(p);
        System.out.println(".>"+this.prescripcionActual);
        return "/medico/privado/Prescripciones/formularioPrescripciones.xhtml";

    }
    
    
    public boolean medicamentoIsSelected(){
        return this.medicIsSelected;
    }
    
    
    public void guardarMedicamentoEnPrescripcion(Medicamento medicamento){
        this.prescripcionActual.setMedicamento(medicamento);
        this.medicIsSelected = true;
    }
    
    
    @AroundInvoke
    private Object logMethod(InvocationContext invocationContext) throws Exception{

        System.out.println(invocationContext.getMethod().getName()+"->" );
        for(Object object : invocationContext.getParameters()){
            System.out.print(object.toString()+" || ");
        }
        System.out.println("");
        return invocationContext.proceed();

    }
    
    
}
