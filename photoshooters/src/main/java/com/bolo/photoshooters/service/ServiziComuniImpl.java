package com.bolo.photoshooters.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import com.bolo.photoshooters.web.EMF;

public class ServiziComuniImpl implements ServiziComuni {

	@Override
	public <T extends Serializable> void deleteAll(Class<T> c) throws Exception{
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		deleteAll(c, em);
		em.getTransaction().commit();
		em.close();
	}
	
	@Override
	public <T extends Serializable> void deleteAll(Class<T> c, EntityManager em) throws Exception{
		em.createQuery("delete from "+c.getSimpleName()).executeUpdate();
	}
			
	@Override
	public <T extends Serializable> void persist(T t, EntityManager em) throws Exception{
		em.persist(t);
	}
	
	@Override
	public <T extends Serializable> void persist(T t) throws Exception{
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		persist(t, em);
		em.getTransaction().commit();
		em.close();
	}
		
	public <T extends Serializable> T getReference(Class<T> c, int id, EntityManager em) throws Exception{
		return (T)em.getReference(c, id);
	}
	
	@Override
	public <T extends Serializable> T getReference(Class<T> c, int id) throws Exception{
		EntityManager em = EMF.createEntityManager();
		T t = getReference(c, id, em);
		em.close();
		return t;
	}
	
	@Override
    public <T extends Serializable> void merge(T t, EntityManager em) throws Exception{
            em.merge(t);
    }

	@Override
    public <T extends Serializable> void merge(T t) throws Exception{
            EntityManager em = EMF.createEntityManager();
            em.getTransaction().begin();
            merge(t, em);
            em.getTransaction().commit();
            em.close();
    }
	
	@Override
	public <T extends Serializable> void delete(T c) throws Exception{
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		delete(c, em);
		em.getTransaction().commit();
		em.close();
	}
	
	@Override
	public <T extends Serializable> void delete(T c, EntityManager em) throws Exception{
		em.remove(em.merge(c));
	}
	
	public <T extends Serializable> List<T> getAll(Class<T> c) throws Exception{
		EntityManager em = EMF.createEntityManager();
		List<T> lista = getAll(c, em);
		em.close();
		return lista;
	}
	
	public <T extends Serializable> List<T> getAll(Class<T> c, EntityManager em) throws Exception{
		return em.createQuery(" FROM "+c.getSimpleName()+" c").getResultList();
	}
		
	public <T extends Serializable> T getById(Class<T> c, Object id) throws Exception{
		EntityManager em = EMF.createEntityManager();
		T t = getById(c, id, em);
		em.close();
		return t;
	}
	
	public <T extends Serializable> T getById(Class<T> c, Object id, EntityManager em) throws Exception{
		
		
		return em.find(c, id);
	}
	
	public void refresh(Object id) throws Exception{
		EntityManager em = EMF.createEntityManager();
		refresh(id, em);
		em.close();
	}
	
	public void refresh(Object id, EntityManager em) throws Exception{
		em.refresh(id);
	}
	
}
