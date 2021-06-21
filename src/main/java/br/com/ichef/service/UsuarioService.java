package br.com.ichef.service;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.ichef.arquitetura.service.EntityManagerProducer;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.Usuario;

@SuppressWarnings("unchecked")
public class UsuarioService extends GenericDAO<Usuario> {
	private static final long serialVersionUID = 1L;

	public List<Usuario> findByLogin(String login) {

		return getManager().createQuery("FROM Usuario WHERE login = :LoginUsuario").setParameter("LoginUsuario", login)
				.setMaxResults(1).getResultList();

	}

	public List<Usuario> findByLogin(String login, String senha) {
		return getManager().createQuery("FROM Usuario WHERE login = :LoginUsuario and senha = :SenhaUsuario")
				.setParameter("LoginUsuario", login).setParameter("SenhaUsuario", senha).setMaxResults(1)
				.getResultList();
	}
	
	
	public String alterarSenha(Usuario entity) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE Usuario SET senha = '"+entity.getSenha()+"'");
			 hql.append(" where  id = "+entity.getId());
			

			if (hql != null) {

				if (!getManager().isOpen()) {
					EntityManagerProducer producer = new EntityManagerProducer();
					setManager(producer.createEntityManager());
				} else {
					getManager().clear();
				}

				tx = getManager().getTransaction();
				tx.begin();

				Query query = getManager().createQuery(hql.toString());
				result = query.executeUpdate();
				tx.commit();

			}
			if (result == 0) {
				return "Operação Não Realizada. Contact o ADM do sistema";
			}

			return null;
		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		} /*
			 * finally { if (getManager().isOpen()) getManager().close(); }
			 */

	}

}
