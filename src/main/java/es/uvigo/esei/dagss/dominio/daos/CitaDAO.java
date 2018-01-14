/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
/**
 *
 * @author osognzalez
 */

@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    public List<Cita> buscarCitasDelMedico(Medico medico) {

        
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c "
                                              + "  WHERE c.medico = :medico"
                                               + " AND c.fecha = :fecha", Cita.class);
        q.setParameter("medico", medico);

        q.setParameter("fecha",(new Date()).getTime());
        
        return q.getResultList();
    }

}
