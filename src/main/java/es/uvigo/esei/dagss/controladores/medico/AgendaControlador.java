/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author osgonzalez
 */

@Named(value = "agendaControlador")
@SessionScoped
public class AgendaControlador implements Serializable {

    @Inject
    CitaDAO citaDAO;
    
    List<Cita> citas;
    Cita citaActual;


    /**
     * Creates a new instance of AdministradorControlador
     */
    public AgendaControlador() {
    }

    
    public List<Cita> loadCitas(Medico medico) {
        this.citas = citaDAO.buscarCitasDelMedico(medico);
        return this.citas;
    }
    
    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Cita getCitaActual() {
        return citaActual;
    }

    public void setCitaActual(Cita citaActual) {
        this.citaActual = citaActual;
    }

    
    public String finalizarCitaPaciente(){
         this.citaActual.setEstado(EstadoCita.COMPLETADA);
         this.citaDAO.actualizar(this.citaActual);
         
         return "/medico/privado/agenda/agenda.xhtml?faces-redirect = true";
    }
    
    
        public String marcarAusente(){
         this.citaActual.setEstado(EstadoCita.AUSENTE);
         this.citaDAO.actualizar(this.citaActual);
         
         return "/medico/privado/agenda/agenda.xhtml?faces-redirect = true";
    }
    
    
    public boolean esPlanificada(Cita cita){
        return cita.getEstado().equals(EstadoCita.PLANIFICADA);
    }

    
    
}
