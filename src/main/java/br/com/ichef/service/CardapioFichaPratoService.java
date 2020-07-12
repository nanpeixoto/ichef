package br.com.ichef.service;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.ichef.arquitetura.service.EntityManagerProducer;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.CardapioFichaPrato;

public class CardapioFichaPratoService extends GenericDAO<CardapioFichaPrato> {
	private static final long serialVersionUID = 1L;

	public String atualizarOrdem(CardapioFichaPrato entity) {

		 
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE CardapioFichaPrato SET ordem = " + entity.getOrdem() + ", usuarioAlteracao.id = "
					+ entity.getUsuarioAlteracao().getId() + ", dataAlteracao = now() where id = " + entity.getId());

			if (hql != null) {
			 

				 
				Query query = getEntityManager().createQuery(hql.toString());
				result = query.executeUpdate();
			 
			}
			if (result == 0)
				return "Operação Não Realizada. Contact o ADM do sistema";
			return null;
		} catch (Exception e) {
		 
			return e.getMessage();
		}

	}
}
