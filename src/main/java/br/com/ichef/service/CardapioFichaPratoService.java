package br.com.ichef.service;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.util.EntityManagerProducer;

public class CardapioFichaPratoService extends GenericDAO<CardapioFichaPrato> {
	private static final long serialVersionUID = 1L;

	public String atualizarOrdem(CardapioFichaPrato entity) {

		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE CardapioFichaPrato SET ordem = " + entity.getOrdem() + ", usuarioAlteracao.id = "
					+ entity.getUsuarioAlteracao().getId() + ", dataAlteracao = now() where id = " + entity.getId());

			if (hql != null) {
				if (!getManager().isOpen()) {
					EntityManagerProducer producer = new EntityManagerProducer();
					setManager(producer.createEntityManager());
				}

				tx = getManager().getTransaction();
				tx.begin();
				Query query = getManager().createQuery(hql.toString());
				result = query.executeUpdate();
				tx.commit();
			}
			if (result == 0)
				return "Operação Não Realizada. Contact o ADM do sistema";
			return null;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return e.getMessage();
		} finally {
			getManager().close();
		}

	}
}
