package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
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

}
