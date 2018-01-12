/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
    // Completar aqui
    
    public List<Receta> listarRecetas(String NSS) 
    {
        TypedQuery<Receta> q = em.createQuery(  "SELECT r " +
                                                " FROM Receta AS r"+
                                                " INNER JOIN Prescripcion AS p"+
                                                " ON r.prescripcion.id = p.id"+
                                                " INNER JOIN Paciente AS d"+
                                                " ON p.paciente.id = d.id"+
                                                " WHERE d.numeroSeguridadSocial= :NSS",Receta.class);
    
    q.setParameter("NSS", NSS);

        return q.getResultList();
    }
    
    /*
    TypedQuery<Receta> q = em.createQuery(  "SELECT r " +
                                                " FROM Receta AS r"+
                                                " INNER JOIN Prescripcion AS p"+
                                                " ON r.prescripcion.id = p.id"+
                                                " INNER JOIN Paciente AS d"+
                                                " ON p.paciente.id = d.id"+
                                                " WHERE d.numeroSeguridadSocial= :NSS",Receta.class);
    
    q.setParameter("NSS", NSS);
    */
}
