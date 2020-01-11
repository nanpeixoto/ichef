package br.com.ichef.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Pedido;
import br.com.ichef.util.EntityManagerProducer;

public class PedidoService extends GenericDAO<Pedido> {
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public List<Pedido> findByParameters(Pedido object, FilterVisitor visitor) throws Exception {

		List<Pedido>  pedidos = mount(super.findByParameters(object, visitor));
		order(pedidos);
		return  pedidos;
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void order(List<Pedido> persons) {

	    Collections.sort(persons, new Comparator() {

	        public int compare(Object o1, Object o2) {

	            String x1 = ((Pedido) o1).getEntregador().getNome();
	            String x2 = ((Pedido) o2).getEntregador().getNome();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            Integer ordem1 = ((Pedido) o1).getOrdemEntrega();
	            Integer ordem2 = ((Pedido) o2).getOrdemEntrega();
	            return ordem1.compareTo(ordem2);
	    }});
	}
	
	@Override
	public List<Pedido> findByParameters(Pedido object) throws Exception {
		List<Pedido>  pedidos = mount(super.findByParameters(object));
		order(pedidos);
		return  pedidos;
	}

	/*
	 * public List<Pedido> findByParametersEmpresa(Pedido object, FilterVisitor
	 * visitor) throws Exception { StringBuilder hql = new StringBuilder();
	 * hql.append(" select * "); hql.append(" from Pedido p ");
	 * hql.append(" join fetch p.cardapio c ");
	 * hql.append(" join fetch p.cardapioFichaPrato cfp ");
	 * hql.append(" join fetch p.entregador e ");
	 * hql.append(" join fetch p.empresa em ");
	 * //hql.append(" where i.atendimento.id = :atendimento ");
	 * //hql.append(" and gf.id = 6 "); TypedQuery<Pedido> query
	 * =getManager().createQuery(hql.toString(),Pedido.class);
	 * //query.setParameter("atendimento", object.getAtendimento().getId()); return
	 * query.getResultList(); }
	 */

	public Integer findTotalPedidoPrato(Pedido pedido) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT  sum(NR_QTD)   nr_total_pedido " + " fROM pedido p "
				+ "WHERE   CD_CARDAPIO_PRATO = "+pedido.getCardapioFichaPrato().getId() );
		try {
			Query query = getManager().createNativeQuery(sb.toString());
			int count = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}

	}

	public String finalizarPedido(Pedido entity) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE Pedido SET snConfirmado = '" + entity.getSnConfirmado() + "'");

			if (entity.getLogLancamentoCarteira() != null)
				hql.append(", logLancamentoCarteira='" + entity.getLogLancamentoCarteira() + "'");

			hql.append(", usuarioFinalizacao.id = " + entity.getUsuarioFinalizacao().getId()
					+ ", dataFinalizacao = now() where id = " + entity.getId());

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
