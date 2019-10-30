package com.epam.tmpl.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.exception.ImageStoreDAOException;

/**
 * @author Andrei_Rohau
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageRepositoryImplTest {

	@Spy
	private List<Image> images = new LinkedList<Image>();
	@Mock
	private SessionFactory sessionFactory;
	@Mock
	private Session session;
	@Mock
	private Query query;
	@Mock
	private Criteria criteria;
	@InjectMocks
	private ImageRepositoryImpl imageRepositoryImpl;

	private Image image;

	private static final String HQL = "delete " + Image.class.getName() + " where id = :id";
	private static final String ID = "id";
	private static final Image IMAGE_TO_THROW_EXCEPTION = new Image("exception", "exception");
	private static final long LONG_TO_THROW_EXCEPTION = 3L;
	private static final int MAX_RESULTS = 50;
	private static final int FIRST_RESULT = 0;
	private static final long LONG_ID = 1L;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		image = new Image("imagename", "base64encodedContent");
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		doNothing().when(session).persist(image);
		doThrow(new HibernateException("")).when(session).persist(IMAGE_TO_THROW_EXCEPTION);
		
		when(session.get(Image.class, LONG_ID)).thenReturn(image);
		when(session.get(Image.class, LONG_TO_THROW_EXCEPTION)).thenThrow(new HibernateException(""));
		
		when(session.createQuery(HQL)).thenReturn(query);
		when(query.setParameter(ID, LONG_ID)).thenReturn(query);
		when(query.setParameter(ID, LONG_TO_THROW_EXCEPTION)).thenThrow(new HibernateException(""));
		when(query.executeUpdate()).thenReturn(1);
		
		when(session.createCriteria(Image.class)).thenReturn(criteria);
		when(criteria.add(Restrictions.like("description", "DATA%"))).thenReturn(criteria);
		when(criteria.setMaxResults(MAX_RESULTS)).thenReturn(criteria);
		when(criteria.setFirstResult(FIRST_RESULT)).thenReturn(criteria);
		when(criteria.list()).thenReturn(images);
		
	}

	@Test
	public void testSave() throws Exception {
		images.add(image);
		imageRepositoryImpl.save(images);
		
		verify(sessionFactory).getCurrentSession();
		verify(session).persist(image);
	}

	@Test(expected = ImageStoreDAOException.class)
	public void testSaveThrowsException() throws Exception {
		images.add(IMAGE_TO_THROW_EXCEPTION);
		imageRepositoryImpl.save(images);
	}

	@Test
	public void testFindById() throws Exception {
		Image actual = imageRepositoryImpl.findById(LONG_ID);
		
		verify(sessionFactory).getCurrentSession();
		verify(session).get(Image.class, LONG_ID);
		
		assertEquals(actual, image);
	}

	@Test(expected = ImageStoreDAOException.class)
	public void testFindByIdThrowsException() throws Exception {
		imageRepositoryImpl.findById(LONG_TO_THROW_EXCEPTION);
	}
	
	@Test
	public void testDeleteById() throws Exception {
		int actual = imageRepositoryImpl.deleteById(LONG_ID);
		
		verify(sessionFactory).getCurrentSession();
		verify(session).createQuery(HQL);
		verify(query).setParameter(ID, LONG_ID);
		verify(query).executeUpdate();
		
		assertEquals(1, actual);
	}
	
	@Test(expected = ImageStoreDAOException.class)
	public void testDeleteByIdThrowsException() throws Exception {
		imageRepositoryImpl.deleteById(LONG_TO_THROW_EXCEPTION);
	}

	@Test
	public void testFindAll() throws Exception {
		List<Image> actualResult = imageRepositoryImpl.findAll(image);
		
		verify(sessionFactory).getCurrentSession();
		verify(session).createCriteria(Image.class);
		verify(criteria).setMaxResults(MAX_RESULTS);
		verify(criteria).setFirstResult(FIRST_RESULT);
		verify(criteria).list();
		
		assertEquals(images, actualResult);
	}

	@Test(expected = ImageStoreDAOException.class)
	public void testFindAllThrowsException() throws Exception {
		when(criteria.list()).thenThrow(new HibernateException(""));
		
		imageRepositoryImpl.findAll(image);
	}

}
