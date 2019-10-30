package com.epam.tmpl.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Andrei_Rohau
 *
 * @param <ID>
 *            object's identifier, same as primary key in database
 * @param <T>
 *            Domain class
 */
public abstract class AbstractDao<ID extends Serializable, T> {

	private final Class<T> persistentClass;
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public AbstractDao(SessionFactory sessionFactory) {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return session
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	protected T getByKey(ID id) {
		return (T) getSession().get(persistentClass, id);
	}

	protected void persist(T entity) {
		getSession().persist(entity);
	}

	protected int delete(ID id) {
		String hql = "delete " + persistentClass.getName() + " where id = :id";
		Query q = getSession().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}

	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(persistentClass);
	}

}
