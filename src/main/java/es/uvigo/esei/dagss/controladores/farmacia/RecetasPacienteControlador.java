/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Receta;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
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
    
    @Inject
    PacienteDAO pacienteDAO;
    
    @Inject
    PrescripcionDAO prescripcionDAO;
    
    @Inject
    RecetaDAO recetaDAO;
    
    List<Receta> recetas;

    public RecetasPacienteControlador()
    {}
    
    @PostConstruct
    public void inicializar() {
       // recetas = recetaDAO.listarRecetas(NSS);
    }

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
        
        recetas = recetaDAO.listarRecetas(NSS);
        destino = "/farmacia/privado/recetas/listadoRecetas.xhtml";
        
        return destino;
    }
}
