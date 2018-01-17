/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    
    @Inject
    RecetaDAO recetaDAO;
    
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

        this.prescripcionActual = this.prescripcionDAO.crear(prescripcionActual);

        //generarRecetas();
        
        return "/medico/privado/Prescripciones/formularioPrescripciones.xhtml";

    }
    
    public void generarRecetas(){
        int dosisPrescipcion = this.prescripcionActual.getDosis();
        int dosisPorMedicamento = this.prescripcionActual.getMedicamento().getNumeroDosis();
        int numeroRecetas = (int) Math.ceil(dosisPrescipcion / dosisPorMedicamento);

        long periodoPrescpcionMillisec = this.prescripcionActual.getFechaFin().getTime() - this.prescripcionActual.getFechaInicio().getTime();
        int periodoPrescpcionsemanas = (int) Math.ceil(TimeUnit.DAYS.convert(periodoPrescpcionMillisec, TimeUnit.MILLISECONDS))+1;

        int recetasPorSemana =(int) Math.floor((numeroRecetas / periodoPrescpcionsemanas));
        Calendar calendar =  Calendar.getInstance();
        
        
        //Primera semana
        
        Receta receta = new Receta();
        receta.setPrescripcion(this.prescripcionActual);
        receta.setEstado(EstadoReceta.GENERADA);
        receta.setCantidad((int) Math.ceil((numeroRecetas / periodoPrescpcionsemanas)));
        receta.setInicioValidez(calendar.getTime());
        calendar.add(Calendar.DATE, 7);
        receta.setFinValidez(calendar.getTime());
        recetaDAO.crear(receta);
        
        
        //Resto semanas 
        
        for(int i=1 ; i<periodoPrescpcionsemanas -1; i++){
            receta = new Receta();
            receta.setPrescripcion(this.prescripcionActual);
            receta.setEstado(EstadoReceta.GENERADA);
            receta.setCantidad(recetasPorSemana);
            receta.setInicioValidez(calendar.getTime());
            calendar.add(Calendar.DATE, 7);
            receta.setFinValidez(calendar.getTime());
            recetaDAO.crear(receta);
        }
        
    }
    
    
    public boolean medicamentoIsSelected(){
        return this.medicIsSelected;
    }
    
    ///medico/privado/Prescripciones/fragmentoSelecionarMedicamento.xhtml
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
