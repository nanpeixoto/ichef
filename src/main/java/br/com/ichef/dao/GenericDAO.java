package br.com.ichef.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.FacesMensager;
import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.excepticon.NegocioExcepticon;
import br.com.ichef.util.EntityManagerProducer;

@SuppressWarnings("unchecked")
public class GenericDAO<T extends BaseEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected FacesMensager facesMessager = new FacesMensager();;

	@Inject
	private EntityManager manager;

	@SuppressWarnings("unused")
	private T save(T entity) throws Exception {
		try {
			return saveImpl(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public T saveOrUpdade(T entity) throws Exception {
		try {
			if (entity.getId() == null)
				return saveImpl(entity);
			else
				return updateImpl(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	protected T updateImpl(T entity) throws Exception {
		EntityTransaction tx = null;
		try {

			if (!manager.isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				manager = producer.createEntityManager();

			}
			tx = manager.getTransaction();
			tx.begin();
			manager.merge(entity);
			manager.flush();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		} finally {
			if(manager.isOpen())
			manager.close();
		}

		return entity;
	}

	protected T saveImpl(T entity) throws Exception {
		EntityTransaction tx = null;
		try {

			if (!manager.isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				manager = producer.createEntityManager();
			}
			tx = manager.getTransaction();
			tx.begin();
			manager.persist(entity);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		} finally {
			if (manager.isOpen())
				manager.close();
		}

		return entity;
	}

	/*
	 * public GenericDAO() { manager = getEntityManager(); }
	 * 
	 * private EntityManager getEntityManager() { EntityManagerFactory factory =
	 * Persistence.createEntityManagerFactory("PU"); if (manager == null) { manager
	 * = factory.createEntityManager(); }
	 * 
	 * return manager; }
	 */

	public void excluir(BaseEntity entity) {
		EntityTransaction tx = null;
		try {
			if (!manager.isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				manager = producer.createEntityManager();
			}
			tx = manager.getTransaction();
			tx.begin();
			entity = getById(entity.getId());
			manager.remove(entity);
			manager.flush();
			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw new NegocioExcepticon("Item não pode ser excluído.");
		} finally {
			if (manager.isOpen())
				manager.close();
		}
	}

	public T getById(Object id) {
		try {
			return (T) manager.find(getTypeClass(), id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	public List<T> listAll() {
		String campoOrderBy = null;
		try {
			Method m = getTypeClass().getDeclaredMethod("getColumnOrderBy");
			Class c = Class.forName(getTypeClass().getName());
			Object t = c.newInstance();
			campoOrderBy = (String) m.invoke(t);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (campoOrderBy == null)
				return manager.createQuery(("FROM " + getTypeClass().getName())).getResultList();
			else
				return manager.createQuery(("FROM " + getTypeClass().getName()) + " ORDER BY " + campoOrderBy)
						.getResultList();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
		// return manager.createNativeQuery("SELECT * FROM Tarefa",
		// BaseEntity.class).getResultList();
	}

	public List<T> findByParameters(T object) throws Exception {
		if (!manager.isOpen()) {
			// System.out.println("conecao fechada");
			EntityManagerProducer producer = new EntityManagerProducer();
			manager = producer.createEntityManager();
		}

		// System.out.println("conecao");

		Criteria criteria = createCriteria(object);

		return criteria.list();
	}

	public List<T> findByParameters(T object, FilterVisitor visitor) throws Exception {
		if (!manager.isOpen()) {
			// System.out.println("conecao fechada");
			EntityManagerProducer producer = new EntityManagerProducer();
			manager = producer.createEntityManager();
		}
		Criteria criteria = null;
		if (visitor != null)
			criteria = createCriteria(object, visitor);
		else
			criteria = createCriteria(object);

		if (criteria != null)
			return criteria.list();
		else
			return null;
	}

	protected Criteria createCriteria(T object, FilterVisitor visitor) throws Exception {
		return createCriteria(object, visitor, null);
	}

	protected Criteria createCriteria(T object) throws Exception {
		return createCriteria(object, null, null);
	}

	@SuppressWarnings("deprecation")
	protected Criteria createCriteria(T object, FilterVisitor visitor, Integer maxResul) throws Exception {
		Criteria criteria = null;
		criteria = getSession().createCriteria(object.getClass());
		if (object.getId() != null) {
			criteria.add(Restrictions.idEq(object.getId()));
		}
		Example example = createExample(object);
		criteria.add(example);
		createSubCriteria(criteria, object, visitor);
		if (visitor != null) {
			visitor.visitCriteria(criteria);
		}
		if (maxResul != null && maxResul != 0)
			return criteria.setMaxResults(maxResul);
		else
			return criteria;
	}

	@SuppressWarnings("rawtypes")
	private void createSubCriteria(Criteria criteria, BaseEntity object, FilterVisitor visitor) throws Exception {
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			String campoOrderBy = null;
			String[] campo = null;
			Order ordenacao = null;
			String aliasCriados = "";

			try {
				Method m = getTypeClass(object).getDeclaredMethod("getColumnOrderBy");
				Class c = Class.forName(getTypeClass(object).getName());
				Object t = c.newInstance();
				campoOrderBy = (String) m.invoke(t);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Field field : fields) {

				if (field != null && (field.isAnnotationPresent(ManyToOne.class)
						|| field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(OneToMany.class)
						|| field.isAnnotationPresent(ManyToMany.class))) {
					String propertyName = field.getName();
					Object sub = PropertyUtils.getProperty(object, propertyName);

					if (sub != null && sub instanceof ArrayList && (((ArrayList) sub).size()) > 0) {
						sub = ((ArrayList) sub).get(0);
					}

					if (sub != null && sub instanceof BaseEntity) {
						Criteria subCriteria = null;
						subCriteria = criteria.createCriteria(propertyName, propertyName);
						aliasCriados = aliasCriados.concat(propertyName);

						Object id = ((BaseEntity) sub).getId();

						if (id != null) {
							subCriteria.add(Restrictions.idEq(id));

						} else {
							Example ex = createExample(sub);
							subCriteria.add(ex);
							createSubCriteria(subCriteria, (BaseEntity) sub, visitor);
							if (visitor != null) {
								visitor.visitSubCriteria(subCriteria);
							}
						}
					}
				}
			}

			if (campoOrderBy != null) {

				if (campoOrderBy.contains(",")) {
					campo = campoOrderBy.split(",");
					// campo = null;
				} else {
					campo = new String[1];
					campo[0] = campoOrderBy;
				}

				if (campo != null) {
					for (String string : campo) {
						if (string.contains(" desc")) {
							if (string.contains(".")) {
								String[] classe = null;
								classe = (string.replace(".", ",")).split(",");
								if (!aliasCriados.contains(classe[0])) {
									criteria.createAlias(classe[0], classe[0]);
									aliasCriados = aliasCriados.concat(classe[0]);
								}
								ordenacao = Order.desc(classe[0] + "." + classe[1].replace(" desc", ""));
							} else {
								ordenacao = Order.desc(string.replace(" desc", ""));
							}
						} else {
							if (string.contains(".")) {
								String[] classe = null;
								classe = (string.replace(".", ",")).split(",");
								if (!aliasCriados.contains(classe[0])) {
									criteria.createAlias(classe[0], classe[0]);
									aliasCriados = aliasCriados.concat(classe[0]);
								}
								ordenacao = Order.asc(classe[0] + "." + classe[1].replace(" asc", ""));
							} else {
								ordenacao = Order.asc(string.replace(" asc", ""));
							}
						}
						if (ordenacao != null) {
							criteria.addOrder(ordenacao);
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Session getSession() {
		return manager.unwrap(Session.class);
	}

	private Example createExample(Object object) {

		Example example = Example.create(object).excludeZeroes().enableLike(MatchMode.ANYWHERE).ignoreCase();

		PropertySelector notNullOrEmptySelector = new PropertySelector() {

			private static final long serialVersionUID = 3872103103165444592L;

			public boolean include(Object object, String propertyName, org.hibernate.type.Type type) {
				return object != null && (!(object instanceof String) || !((String) object).equals(""));
			}
		};
		example.setPropertySelector(notNullOrEmptySelector);
		return example;
	}

	protected Class<?> getTypeClass() {
		Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		return clazz;
	}

	protected Class<?> getTypeClass(BaseEntity object) {
		Class<?> clazz = object.getClass(); // (Class<?>) ((ParameterizedType)
											// object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return clazz;
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

}
