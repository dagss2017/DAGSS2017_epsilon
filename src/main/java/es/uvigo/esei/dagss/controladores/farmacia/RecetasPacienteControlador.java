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
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author LuisF
 */
@Named(value = "RecetasPacienteControlador")
@ConversationScoped
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
    
    public String recetaValida(Date fecha,Receta receta)
    {
        LocalDateTime now = LocalDateTime.now();
        Date now2= Date.from(now.toInstant(ZoneOffset.UTC));
        
        if(fecha.before(now2))
        {
            return "No Disponible";
        }
        else
        {
            if("GENERADA".equals(receta.getEstado().toString()))
                return "Disponible para Suministro";
            else
                return "No Disponible";
        }  
    }
    
    public boolean servida(Receta receta, Date fecha)
    {
        LocalDateTime now = LocalDateTime.now();
        Date now2= Date.from(now.toInstant(ZoneOffset.UTC));
        
        return "GENERADA".equals(receta.getEstadoReceta().toString())&&(fecha.after(now2));
    }
        
    public void retorno(Receta receta, Farmacia farmacia)
    {
       receta.setEstado(EstadoReceta.SERVIDA);
       receta.setFarmaciaDispensadora(farmacia);
       recetaDAO.actualizar(receta);
       recetas = recetaDAO.listarRecetas(NSS);   
    }

}
