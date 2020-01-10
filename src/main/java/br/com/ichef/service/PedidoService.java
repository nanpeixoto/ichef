package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Pedido;

@Stateless
public class PedidoService extends AbstractService<Pedido> {
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public List<Pedido> findByParameters(Pedido object, FilterVisitor visitor) throws Exception {

		return mount(super.findByParameters(object, visitor));
	}
	
	@Override
	public List<Pedido> findByParameters(Pedido object) throws Exception {

		return mount(super.findByParameters(object));
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
		sb.append(" SELECT  sum(NR_QTD)   nr_total_pedido " + " fROM pedido p, cardapio c  "
				+ " where p.CD_CARDAPIO = c.CD_CARDAPIO " + " and CD_CARDAPIO_PRATO = "
				+ pedido.getCardapioFichaPrato().getId() + " and cd_empresa = " + pedido.getEmpresa().getId()
				+ " and date_format( data, '%d/%m/%Y' ) =   '" + pedido.getCardapio().getDataFormatada() + "'  "
				+ " group by c.DATA,  p.CD_EMPRESA, p.CD_CARDAPIO_PRATO ");
		try {
			Query query = entityManager.createNativeQuery(sb.toString());
			int count = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}

	}

	public String finalizarPedido(Pedido entity) {
	
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

				 

				 
				Query query = entityManager.createQuery(hql.toString());
				result = query.executeUpdate();
				

			}
			if (result == 0) {

				return "Opera��o N�o Realizada. Contact o ADM do sistema";
			}

			return null;
		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		} /*
			 * finally { if (getManager().isOpen()) getManager().close(); }
			 */

	}

	@Override
	protected void validaCampos(Pedido entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Pedido entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Pedido entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
