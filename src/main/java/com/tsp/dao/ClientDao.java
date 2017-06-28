package com.tsp.dao;

import com.tsp.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOT USED
 * 
 * @author Linar Abzaltdinov
 */

@Repository
@Transactional
public class ClientDao
{
    @PersistenceContext
    EntityManager em;
    
    public void save(Client client)
    {
        em.persist(client);
    }
    
    public List<Client> getAllClients()
    {
        List<Client> resultList = em.createQuery("select d from Client d order by id", Client.class).getResultList();
        return resultList;
    }
    
    public Client findById(Long id)
    {
        return em.find(Client.class, id);
    }
    
    public Client findByUsername(String username)
    {
        try {
            TypedQuery<Client> query = em.createQuery("select d from Client d where d.username=:name", Client.class).setParameter("name", username);
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null; 
        } 
    }

    @Transactional
    public void removeById(Long id)
    {
        Client found = this.findById(id);
        if (found != null)
            em.remove(found);
    }
}
