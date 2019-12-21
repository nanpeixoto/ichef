package br.com.ichef.service;

import javax.persistence.NoResultException;
import javax.persistence.Query;

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
	
	
	public Integer findTotalPedidoPrato(Pedido pedido) {
		StringBuilder sb =  new StringBuilder();
		sb.append(" SELECT  sum(NR_QTD)   nr_total_pedido " + 
				" fROM pedido p, cardapio c  " + 
				" where p.CD_CARDAPIO = c.CD_CARDAPIO " + 
				" and CD_CARDAPIO_PRATO = "+pedido.getCardapioFichaPrato().getId()+" and cd_empresa = "+pedido.getEmpresa().getId()
					+" and date_format( data, '%d/%m/%Y' ) =   '"+pedido.getCardapio().getDataFormatada()+"'  " + 
				" group by c.DATA,  p.CD_EMPRESA, p.CD_CARDAPIO_PRATO "
		);
		try {
			Query query = getManager().createNativeQuery(sb.toString());
			int count =  query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) :  	0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}
		
	}
	 
}
