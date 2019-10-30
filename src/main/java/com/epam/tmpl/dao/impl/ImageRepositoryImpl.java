package com.epam.tmpl.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.tmpl.dao.AbstractDao;
import com.epam.tmpl.dao.ImageRepository;
import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.exception.ImageStoreDAOException;

@Repository
public class ImageRepositoryImpl extends AbstractDao<Long, Image> implements ImageRepository {

	@Autowired
	public ImageRepositoryImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void save(List<Image> images) throws ImageStoreDAOException {
		try {
			for (Image image : images) {
				persist(image);
			}
		} catch (HibernateException e) {
			throw new ImageStoreDAOException("Error while uploading images to database. ", e);
		}
	}

	@Override
	public Image findById(Long id) throws ImageStoreDAOException {
		try {
			return getByKey(id);
		} catch (HibernateException e) {
			throw new ImageStoreDAOException("Error while finding image by id from database. ", e);
		}
	}

	@Override
	public int deleteById(Long id) throws ImageStoreDAOException {
		try {
			return delete(id);
		} catch (HibernateException e) {
			throw new ImageStoreDAOException("Error while deleting image by id from database. ", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> findAll(Image criteria) throws ImageStoreDAOException {
		try {
			Criteria cr = createEntityCriteria();
			cr.add(Restrictions.like("description", "DATA%"));
			cr.setMaxResults(50);
			cr.setFirstResult(0);
			return cr.list();
		} catch (HibernateException e) {
			throw new ImageStoreDAOException("Error while finding all images from database. ", e);
		}
	}

}
