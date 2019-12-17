package br.com.ichef.service;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Pedido;

public class PedidoService extends GenericDAO<Pedido> {
	private static final long serialVersionUID = 1L;
	

 
	/*public List<Pedido> findByParametersEmpresa(Pedido object, FilterVisitor visitor) throws Exception {
		StringBuilder hql = new StringBuilder();
		hql.append(" select * ");
		hql.append(" from Pedido p ");
		hql.append(" join fetch p.cardapio c ");
		hql.append(" join fetch p.cardapioFichaPrato cfp ");
		hql.append(" join fetch p.entregador e ");
		hql.append(" join fetch p.empresa em ");
		//hql.append(" where i.atendimento.id = :atendimento ");
		//hql.append(" and gf.id = 6 ");
		TypedQuery<Pedido> query =getManager().createQuery(hql.toString(),Pedido.class);
		//query.setParameter("atendimento", object.getAtendimento().getId());
		return query.getResultList();
	}
	*/
	 
}
