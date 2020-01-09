
package br.com.ichef.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.exception.AppException;


public class AppService<T extends BaseEntity> implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 5746962210345825045L;

	@PersistenceContext(unitName = "sacPU")
	protected EntityManager entityManager;

	

	protected Criteria createCriteria(T object, FilterVisitor visitor) throws AppException {
		return createCriteria(object, visitor, null, null);
	}

	protected Criteria createCriteria(T object) throws AppException {
		return createCriteria(object, null, null, null);
	}

	protected Criteria createCriteria(T object, Integer maxResul) throws AppException {
		return createCriteria(object, null, null, null);
	}

	protected Criteria createCriteria(T object, FilterVisitor visitor, EntityManager entityManager) throws AppException {
		return createCriteria(object, visitor, entityManager, null);
	}
	
	protected Criteria createCriteria(T object, FilterVisitor visitor, Integer max) throws AppException {
		return createCriteria(object, visitor, null, max);
	}

	protected Criteria createCriteria(T object, FilterVisitor visitor, EntityManager entityManager, Integer maxResul) throws AppException {
		Criteria criteria = null;
		if (entityManager != null)
			criteria = getSession(entityManager).createCriteria(object.getClass());
		else
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
	private void createSubCriteria(Criteria criteria, BaseEntity object, FilterVisitor visitor) throws AppException {
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
				new AppException();
			}

			for (Field field : fields) {

				if (field != null && (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class))) {
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
			throw new AppException(e);
		}
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

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected Session getSession(EntityManager entityManager) {
		return entityManager.unwrap(Session.class);
	}

	protected Class<?> getTypeClass() {
		Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return clazz;
	}
	
	protected Class<?> getTypeClass(BaseEntity object) {
		Class<?> clazz =object.getClass() ; //(Class<?>) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return clazz;
	}

}

