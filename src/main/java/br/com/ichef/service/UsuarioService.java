package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Usuario;

@SuppressWarnings("unchecked")
public class UsuarioService extends AbstractService<Usuario> {
	private static final long serialVersionUID = 1L;

	public List<Usuario> findByLogin(String login) {
		List<Usuario> usuariosre = new ArrayList<Usuario>();
		try {
			usuariosre = new ArrayList<Usuario>();
			List<Usuario> usuarios = entityManager.createQuery("FROM Usuario u WHERE login = :LoginUsuario")
					.setParameter("LoginUsuario", login).getResultList();

			usuariosre.add(usuarios.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		}

		return usuariosre;
	}

	public List<Usuario> findByLogin(String login, String senha) {
		return entityManager.createQuery("FROM Usuario WHERE login = :LoginUsuario and senha = :SenhaUsuario")
				.setParameter("LoginUsuario", login).setParameter("SenhaUsuario", senha).setMaxResults(1)
				.getResultList();
	}

	@Override
	protected void validaCampos(Usuario entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(Usuario entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(Usuario entity) throws AppException {
		// TODO Auto-generated method stub

	}

}
