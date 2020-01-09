package br.com.ichef.service;

import javax.persistence.Query;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.CardapioFichaPrato;

public class CardapioFichaPratoService extends AbstractService<CardapioFichaPrato> {
	private static final long serialVersionUID = 1L;

	public String atualizarOrdem(CardapioFichaPrato entity) {

		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE CardapioFichaPrato SET ordem = " + entity.getOrdem() + ", usuarioAlteracao.id = "
					+ entity.getUsuarioAlteracao().getId() + ", dataAlteracao = now() where id = " + entity.getId());

			if (hql != null) {

				Query query = entityManager.createQuery(hql.toString());
				result = query.executeUpdate();

			}
			if (result == 0)
				return "Operação Não Realizada. Contact o ADM do sistema";
			return null;
		} catch (Exception e) {

			return e.getMessage();
		}

	}

	@Override
	protected void validaCampos(CardapioFichaPrato entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(CardapioFichaPrato entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(CardapioFichaPrato entity) throws AppException {
		// TODO Auto-generated method stub

	}
}
