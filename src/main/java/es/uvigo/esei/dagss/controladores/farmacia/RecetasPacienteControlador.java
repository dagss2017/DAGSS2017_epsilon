/*
 Proyecto Java EE, DAGSS-2017
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Receta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author LuisF
 */
@Named(value = "recetasPacienteControlador")
@SessionScoped
public class RecetasPacienteControlador implements Serializable
{
    private String NSS;
    
    @EJB
    PacienteDAO pacienteDAO;
    
    @EJB
    PrescripcionDAO prescripcionDAO;
    
    @EJB
    RecetaDAO recetaDAO;
    
    List<Receta> recetas;

    public RecetasPacienteControlador()
    {}

    public void setNSS(String NSS) {
        this.NSS = NSS;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }
    
     public String getNSS() {
        return NSS;
    }
     
    public String listarRecetas()
    {
        String destino = null;
        if (NSS==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un paciente", ""));
        } else 
        {
            Paciente paciente=pacienteDAO.buscarPorTarjetaSanitaria(NSS);
            if(paciente==null)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca un paciente correcto", ""));
            }
            else
            {
                recetas = recetaDAO.listarRecetas(NSS);
                destino = "/farmacia/privado/recetas/listadoRecetas.xhtml";
            }
        }
        
        return destino;
    }
    
    
    //Refactored
    public String recetaValida(Receta receta)
    {
        LocalDateTime now = LocalDateTime.now();
        Date now2= Date.from(now.toInstant(ZoneOffset.UTC));
        
        if(receta.getFinValidez().before(now2))
        {
            return "No Disponible";
        }
        else
        {
            if(receta.getEstado().equals(EstadoReceta.GENERADA))
                return "Disponible para Suministro";
            else
                return "No Disponible";
        }  
    }
    
    public boolean estaDisponible(Receta receta)
    {
        LocalDateTime now = LocalDateTime.now();
        Date now2= Date.from(now.toInstant(ZoneOffset.UTC));
        
        return receta.getEstadoReceta().equals(EstadoReceta.GENERADA)&&(receta.getFinValidez().after(now2));
    }
        
    public String retorno(Receta receta, Farmacia farmacia)
    {
    /*   receta.setEstado(EstadoReceta.SERVIDA);
       receta.setFarmaciaDispensadora(farmacia);
       recetaDAO.actualizar(receta);
       recetas = recetaDAO.listarRecetas(NSS);   
      */ 
       return "/farmacia/privado/recetas/listadoRecetas.xhtml";
    }
    
    public String servirReceta(Receta receta,Farmacia farmacia){
        receta.setEstado(EstadoReceta.SERVIDA);
        receta.setFarmaciaDispensadora(farmacia);
        recetaDAO.actualizar(receta);
        
        return "/farmacia/privado/recetas/recetaServida.xhtml?faces-redirect = true";
    }

}
