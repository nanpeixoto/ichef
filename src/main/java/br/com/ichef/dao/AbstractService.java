
package br.com.ichef.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile.FetchOverride;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.FacesMensager;
import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.exception.AppException;
import br.com.ichef.exception.BusinessException;
import br.com.ichef.exception.RequiredException;
import br.com.ichef.util.MensagemUtil;
import br.com.ichef.util.Util;

/**
 * Classe utilizada por todo serviço EJB que faz operação de CRUD e/ou precisa*
 * implementar regras de campos obrigatórios e regras negócio. Contém métodos
 * comuns que devem ser implementados, assim como métodos utilitários.
 * 
 * @param <T>
 */
@Stateless
public abstract class AbstractService<T extends BaseEntity> extends AppService<T> {

	private static final long serialVersionUID = 297699908122496343L;

	// @Inject
	// protected Logger logger;

	@Inject
	protected FacesMensager facesMessager;

	protected List<String> mensagens;

	private boolean editando;

	// =======================================================================================================
	// Template methods
	// =======================================================================================================

	protected abstract void validaCampos(T entity);

	protected abstract void validaRegras(T entity);

	protected abstract void validaRegrasExcluir(T entity) throws AppException;

	// =======================================================================================================
	// Implements methods
	// =======================================================================================================

 

	/**
	 * Lista todas as entidades cadastradas na tabela que reprensenta a entidade
	 * anotada nesta interface.
	 * 
	 * @return Coleção contendo todas as entidades cadastradas na4 tabela que
	 *         reprensenta a entidade anotada nesta interface.
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> listAll() {
		String campoOrderBy = null;
		try {
			Method m = getTypeClass().getDeclaredMethod("getColumnOrderBy");
			Class c = Class.forName(getTypeClass().getName());
			Object t = c.newInstance();
			campoOrderBy = (String) m.invoke(t);
		} catch (Exception e) {
			// String men = "Erro de Ordenação do Campo";
			// logger.info(men);
			new AppException();
		}

		try {
			if (campoOrderBy == null)
				return entityManager.createQuery(("FROM " + getTypeClass().getName())).getResultList();
			else
				return entityManager.createQuery(("FROM " + getTypeClass().getName()) + " ORDER BY " + campoOrderBy)
						.getResultList();
		} catch (Exception e) {
			// logger.error(LogUtil.getMensagemPadraoLog(e.getMessage(),
			// LogUtil.getNomeFuncionalidade(getTypeClass().getName()),
			// "findAll"));
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));

		}
		return null;

	}

	/**
	 * Lista todas as entidades cadastradas na tabela que reprensenta a entidade
	 * anotada nesta interface.
	 * 
	 * @return Coleção contendo todas as entidades cadastradas na4 tabela que
	 *         reprensenta a entidade anotada nesta interface.
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll(EntityManager entityManagerPrd) {
		String campoOrderBy = null;
		try {
			Method m = getTypeClass().getDeclaredMethod("getColumnOrderBy");
			Class c = Class.forName(getTypeClass().getName());
			Object t = c.newInstance();
			campoOrderBy = (String) m.invoke(t);
		} catch (Exception e) {
			// String men = "Erro de Ordenação do Campo";
			// logger.info(men);
			new AppException();
		}

		try {
			if (campoOrderBy == null)
				return entityManagerPrd.createQuery(("FROM " + getTypeClass().getName())).getResultList();
			else
				return entityManagerPrd.createQuery(("FROM " + getTypeClass().getName()) + " ORDER BY " + campoOrderBy)
						.getResultList();
		} catch (Exception e) {
			// logger.error(LogUtil.getMensagemPadraoLog(e.getMessage(),
			// LogUtil.getNomeFuncionalidade(getTypeClass().getName()),
			// "findAll"));
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));

		}
		return null;

	}

	/**
	 * Recupera a entidade a partir de seu identificador
	 * 
	 * @param id
	 *            O identificador da entidade
	 * @return A entidade com seus dados preenchidos a partir do banco de dados
	 */
	@SuppressWarnings("unchecked")
	public T getById(Object id) {
		try {
			return (T) entityManager.find(getTypeClass(), id);
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
			return null;

		}

	}

	/**
	 * Recupera a entidade a partir de seu identificador
	 * 
	 * @param id
	 *            O identificador da entidade
	 * @return A entidade com seus dados preenchidos a partir do banco de dados
	 */
	@SuppressWarnings("unchecked")
	public T getById(Object id, EntityManager entityManager) {
		try {
			return (T) entityManager.find(getTypeClass(), id);
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
			return null;

		}

	}

	/**
	 * Insere a entidade no banco de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser inserida.
	 * @return A entidade que está persistida no banco.
	 * @throws AppException
	 */

	public T save(T entity) throws AppException {
		processValidations(entity, false);
		try {
			return saveImpl(entity);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public T save(T entity, EntityManager entityManager) throws AppException {
		processValidations(entity, false);
		try {
			return saveImpl(entity, entityManager);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	/**
	 * Insere a entidade no banco de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser inserida.
	 * @return A entidade que está persistida no banco.
	 * @throws AppException
	 * @throws Exception
	 */

	public T saveOrUpdade(T entity) throws AppException, Exception {
		processValidations(entity, false);
		try {
			if (entity.getId() == null)
				return saveImpl(entity);
			else
				return updateImpl(entity);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public T saveOrUpdade(T entity, EntityManager entityManager) throws AppException {
		processValidations(entity, false);
		try {
			if (entity.getId() == null)
				return saveImpl(entity, entityManager);
			else
				return updateImpl(entity, entityManager);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	/**
	 * Atualiza os valores da entidade no banco de dados.
	 * 
	 * @param entidade
	 *            Entidade que terá os valores atualizados.
	 * @return A entidade que está persistida no banco.
	 * @throws AppException
	 */
	public T update(T entity) throws AppException {
		processValidations(entity, true);
		try {

			return updateImpl(entity);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

	/**
	 * Atualiza os valores de uma lista de objetos de determinada entidade.
	 * 
	 * @param listEntity
	 * @return Retorna a lista das entidades atualizadas.
	 * @throws RequiredException
	 * @throws BusinessException
	 * @throws Exception
	 */
	public List<T> update(Collection<T> listEntity) throws AppException {
		processValidations(listEntity, true);
		try {

			return updateLote(listEntity);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

	/**
	 * Exclui a entidade do banco de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser excluída.
	 * @throws BusinessException
	 * @throws AppException
	 */
	public void excluir(T entity) throws AppException {
		try {

			// tx.begin();
			processDeleteValidations(entity);
			deleteImpl(entity);
			// tx.commit();

		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object) throws AppException, Exception {
		Criteria criteria = createCriteria(object);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object, int maxResul) throws AppException {
		Criteria criteria = createCriteria(object, maxResul);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object, FilterVisitor visitor, int maxResul) throws AppException {
		Criteria criteria = createCriteria(object, visitor, maxResul);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object, EntityManager entityManager) throws AppException {
		Criteria criteria = createCriteria(object, null, entityManager);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object, FilterVisitor visitor, EntityManager entityManager) throws AppException {
		Criteria criteria = createCriteria(object, visitor, entityManager);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(T object, FilterVisitor visitor) throws AppException, Exception {
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

	// =======================================================================================================
	// IMPLEMENTAÇÕES DEFAULT
	// =======================================================================================================
	/**
	 * Método que salva uma entidade. Se necessário, ele será sobrescrito para
	 * realizar algo antes e/ou depois da operação de save.
	 * 
	 * @param entity
	 * @return
	 * @throws AppException
	 */
	protected T saveImpl(T entity) throws AppException {
		try {
			// tx.begin();
			entityManager.persist(entity);
			// tx.commit();
		} catch (Exception e) {
			mensagens.add(e.getMessage());
			if (!Util.isNullOuVazio(mensagens))
				throw new BusinessException(e.getMessage());
		}

		return entity;
	}

	// =======================================================================================================
	// IMPLEMENTAÇÕES DEFAULT
	// =======================================================================================================
	/**
	 * Método que salva uma entidade. Se necessário, ele será sobrescrito para
	 * realizar algo antes e/ou depois da operação de save.
	 * 
	 * @param entity
	 * @return
	 * @throws AppException
	 */
	protected T saveImpl(T entity, EntityManager entityManager) throws AppException {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			mensagens.add(e.getMessage());
			if (!Util.isNullOuVazio(mensagens))
				throw new BusinessException(e.getMessage());
		}

		return entity;
	}

	/**
	 * Método que atualiza uma entidade. Se necessário, ele será sobrescrito para
	 * realizar algo antes e/ou depois da operação de update.
	 * 
	 * @param entity
	 * @return
	 * @throws AppException
	 */

	protected T updateImpl(T entity) throws AppException {

		try {
			// tx.begin();
			entityManager.merge(entity);
			entityManager.flush();
			// tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}

		return entity;
	}

	/**
	 * Método que atualiza uma entidade. Se necessário, ele será sobrescrito para
	 * realizar algo antes e/ou depois da operação de update.
	 * 
	 * @param entity
	 * @return
	 * @throws AppException
	 */
	protected T updateImpl(T entity, EntityManager entityManager) throws AppException {

		try {

			entityManager.merge(entity);
			entityManager.flush();

		} catch (Exception e) {
			throw new AppException(e);
		}

		return entity;
	}

	/**
	 * Implementação default do do método para fazer update em lote
	 * 
	 * @param listEntity
	 * @return Retorna uma lista das entidades salvas.
	 * @throws AppException
	 */
	protected List<T> updateLote(Collection<T> listEntity) throws AppException {
		List<T> listReturn = new ArrayList<T>();
		for (T entity : listEntity) {
			listReturn.add(updateImpl(entity));
		}
		return listReturn;
	}

	/**
	 * Método que remove uma entidade. Se necessário, ele será sobrescrito para
	 * realizar algo antes e/ou depois da operação de delete.
	 * 
	 * @param entity
	 * @throws AppException
	 * @throws BusinessException
	 * @throws Exception
	 */
	protected void deleteImpl(T entity) {

		T attachedEntity = entityManager.merge(entity);
		entityManager.remove(attachedEntity);

	}

	/**
	 * Processa todas as validações implementadas no validaCamposObrigatorios e no
	 * validaRegras durante o save e o update.
	 * 
	 * @param entity
	 *            Entidade a ser validada.
	 * @param editando
	 *            Parametro que será usado nos métodos das RNs.
	 * @throws RequiredException
	 *             Quando algum campo obrigatório não foi preenchido.
	 * @throws BusinessException
	 *             Quando alguma RN não foi atendida.
	 */
	protected void processValidations(T entity, boolean editando) throws RequiredException, BusinessException {

		setEditando(editando);
		mensagens = new ArrayList<String>();

		validaCampos(entity);
		if (!Util.isNullOuVazio(mensagens))
			throw new RequiredException(mensagens);

		validaRegras(entity);
		if (!Util.isNullOuVazio(mensagens))
			throw new BusinessException(mensagens);
	}

	/**
	 * Processa todas as validações implementadas no validaCamposObrigatorios e no
	 * validaRegras durante o save e o update.
	 * 
	 * @param listEntity
	 *            lista da Entidade a ser validada.
	 * @param editando
	 *            Parametro que será usado nos métodos das RNs.
	 * @throws RequiredException
	 *             Quando algum campo obrigatório não foi preenchido.
	 * @throws BusinessException
	 *             Quando alguma RN não foi atendida.
	 */
	protected void processValidations(Collection<T> listEntity, boolean editando)
			throws RequiredException, BusinessException {
		setEditando(editando);
		mensagens = new ArrayList<String>();

		for (T entity : listEntity) {
			validaCampos(entity);
		}
		if (!Util.isNullOuVazio(mensagens))
			throw new RequiredException("Campos Obrigatórios não preenchidos.", mensagens);

		for (T entity : listEntity) {
			validaRegras(entity);
		}
		if (!Util.isNullOuVazio(mensagens))
			throw new BusinessException("Regras de negócio não atendidas.", mensagens);
	}

	/**
	 * Processa as RNs implementadas no validaRegrasExcluir durante o remove.
	 * 
	 * @param entity
	 *            Entidade a ser validada.
	 * @throws BusinessException
	 *             Se alguma regra não foi atendida.
	 */
	protected void processDeleteValidations(T entity) throws AppException {
		mensagens = new ArrayList<String>();
		try {
			validaRegrasExcluir(entity);
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
		}

		if (!Util.isNullOuVazio(mensagens))
			throw new BusinessException("Regras de negócio não atendidas.", mensagens);
	}

	// =======================================================================================================
	// MÉT0D0S DE ACESSO
	// =======================================================================================================
	protected boolean isEditando() {
		return editando;
	}

	protected void setEditando(boolean editando) {
		this.editando = editando;
	}

	public List<T> mount(List<T> list) {
		if (list != null && list.size() > 0) {
			Map<Object, T> map = new HashMap<Object, T>();
			for (T item : list) {
				if (!map.containsKey(item.getId())) {
					map.put((Object) item.getId(), item);
				}
			}
			return (new ArrayList<T>(map.values()));
		}
		return list;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

}