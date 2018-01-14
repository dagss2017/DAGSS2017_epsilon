/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;




@Named(value = "atencionPacienteControlador")
@SessionScoped
public class AtencionPacienteControlador implements Serializable{
    
    private Paciente pacienteActual;
    
    @EJB
    private PrescripcionDAO prescripcionDAO;
    
    private List<Prescripcion> listaPrescripciones;
 
    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public void setPacienteActual(Paciente pacienteActual) {
        this.pacienteActual = pacienteActual;
    }
    
     public List<Prescripcion> loadPrescripcion(Paciente paciente) {
         this.pacienteActual = paciente;
        this.listaPrescripciones= prescripcionDAO.getRecetasPaciente(pacienteActual);
        return this.listaPrescripciones;
    }
    
     public String atenderPaciente(Paciente paciente){
         this.pacienteActual = paciente;
         
         return "/medico/privado/atencionPaciente/formularioAtencionPaciente.xhtml" ;
     }
    
}
