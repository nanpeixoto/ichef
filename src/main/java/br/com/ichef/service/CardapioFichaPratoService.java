package br.com.ichef.service;

import javax.persistence.Query;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.util.EntityManagerProducer;

public class CardapioFichaPratoService extends GenericDAO<CardapioFichaPrato> {
	private static final long serialVersionUID = 1L;

	public String atualizarOrdem(CardapioFichaPrato entity) {

		try {

			StringBuilder hql = null;
			int result = -1;
			
			hql = new StringBuilder();
			
			hql.append("UPDATE CardapioFichaPrato SET ordem = "+entity.getOrdem()+", usuarioAlteracao.id = "+entity.getUsuarioAlteracao().getId()+ ", dataAlteracao = now() where id = "+entity.getId()) ;

			if (hql != null) {
				if (!getManager().isOpen()) {
					EntityManagerProducer producer = new EntityManagerProducer();
					setManager( producer.createEntityManager() ) ;
				}
				
				getManager().getTransaction().begin();
				Query query = getManager().createQuery(hql.toString());
				result = query.executeUpdate();
				getManager().getTransaction().commit();
			}
			if (result == 0)
				return "Operação Não Realizada. Contact o ADM do sistema";
			return null;
		} catch (Exception e) {
			return e.getMessage();
		} finally {
			getManager().close();
		}
		
	}
}
