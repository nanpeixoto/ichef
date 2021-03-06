package br.com.ichef.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ichef.arquitetura.service.EntityManagerProducer;
import br.com.ichef.arquitetura.service.Transacional;
import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.Usuario;
import br.com.ichef.util.Util;

public class PedidoService extends GenericDAO<Pedido> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<Pedido> findByParameters(Pedido object, FilterVisitor visitor) throws Exception {

		List<Pedido> pedidos = mount(super.findByParameters(object, visitor));
		order(pedidos);
		return pedidos;

	}

	public Double findValorDebito(Long codigoCliente, Long codigoEmpresa) {
		StringBuilder sb = new StringBuilder();
		sb.append("select saldo from vw_cliente_saldo_empresa where cd_cliente =  " + codigoCliente
				+ " and cd_empresa = " + codigoEmpresa);

		Query query = getManager().createNativeQuery(sb.toString());
		Double saldo = Double.parseDouble(query.getSingleResult().toString());
		return saldo;

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
			}
		});
	}

	@Override
	public List<Pedido> findByParameters(Pedido object) throws Exception {
		List<Pedido> pedidos = mount(super.findByParameters(object));
		order(pedidos);
		return pedidos;
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

	@Transacional
	public Integer findTotalPedidoPrato(Pedido pedido) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" SELECT   sum(case when COALESCE(tp.SN_PLUS,'N') = 'S'  THEN  NR_QTD*2  ELSE  NR_QTD END)   nr_total_pedido "
						+ " fROM pedido p, tip_prato tp " + " WHERE   CD_CARDAPIO_PRATO = "
						+ pedido.getCardapioFichaPrato().getId() + " and cd_empresa = " + pedido.getEmpresa().getId()
						+ " and p.CD_TIP_PRATO = tp.CD_TIP_PRATO ");
		try {
			if (!getManager().isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				setManager(producer.createEntityManager());
			} else {
				getManager().clear();
			}

			Query query = getManager().createNativeQuery(sb.toString());
			int count = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}

	}

	@Transacional
	public Integer findQtdPedidoPratoEntregador(Pedido pedido) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT   sum( NR_QTD )   nr_total_pedido "
				+ " fROM pedido p , tip_prato t   WHERE    p.cd_tip_prato = t.CD_TIP_PRATO  AND   cd_entregador = "
				+ pedido.getEntregador().getId()
				+ " and   coalesce(t.SN_CONTAGEM, 'N')  = 'S' and date_format( dt_entrega, '%d/%m/%Y' ) ='"
				+ Util.dateToString(pedido.getDataEntrega()) + "'");
		try {
			if (!getManager().isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				setManager(producer.createEntityManager());
			} else {
				getManager().clear();
			}

			Query query = getManager().createNativeQuery(sb.toString());
			int count = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}

	}

	public String findPedidosFinalizacao(Pedido pedido) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select " + "	GROUP_CONCAT(cd_pedido)  " + " from pedido p  " + " WHERE CD_EMPRESA = "
				+ pedido.getEmpresa().getId() + " 	and sn_confirmado = 'N' "
				+ " 	and DATE_FORMAT(p.DT_ENTREGA,'%d/%m/%Y') ='" + pedido.getDataEntregaFormatada() + "' ");
		if (pedido.getEntregador() != null) {
			sb.append(" and cd_entregador = " + pedido.getEntregador().getId());
		}

		try {
			if (!getManager().isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				setManager(producer.createEntityManager());
			} else {
				getManager().clear();
			}

			Query query = getManager().createNativeQuery(sb.toString());
			String count = query.getSingleResult() != null ? query.getSingleResult().toString() : null;
			return count;
		} catch (NoResultException e) {
			return null;
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

	public String excluirPedido(Pedido entity) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("delete from  Pedido  where id = " + entity.getId());

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

	public String finalizarPedido(String listaPedidos, Usuario usuarioLogado) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE Pedido SET snConfirmado = 'S'");

			hql.append(", usuarioFinalizacao.id = " + usuarioLogado.getId() + ", dataFinalizacao = now() where id  in( "
					+ listaPedidos + ")");

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

	public String atualizarFormaPagamentoValorPago(Pedido pedido) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE Pedido SET formaPagamento.id = " + pedido.getFormaPagamento().getId());
			hql.append(" , valorPago = " + pedido.getValorPago());
			hql.append(" , dataAlteracao = now()");
			hql.append(" , usuarioAlteracao.id = " + pedido.getUsuarioAlteracao().getId());
			hql.append(" where  id = " + pedido.getId());

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
	protected Pedido updateImpl(Pedido entity) throws Exception {

		if (getManager().isOpen()) {
			getManager().clear();
		}
		return super.updateImpl(entity);
	}

	public String atualizarData(Pedido entity, Usuario usuario) {
		 
			EntityTransaction tx = null;
			try {

				StringBuilder hql = null;
				int result = -1;

				hql = new StringBuilder();
				// `DT_ENTREGA`='2020-01-24 00:00:00'
				
				int qtdPedidoPratoEntregador = findQtdPedidoPratoEntregador(entity);
				if (entity.getEntregador().getQuantiadadeQuentinha() != null
						&& (entity.getTipoPrato().isContagem()
								&& ((qtdPedidoPratoEntregador + entity.getQuantidade()) > entity.getEntregador().getQuantiadadeQuentinha()))) {
					String erro = "Quantidade m�xima do Entregador j� atingida, qtd dispon�vel: " + (entity.getEntregador().getQuantiadadeQuentinha() - qtdPedidoPratoEntregador);
					return erro;
				}
				
				SimpleDateFormat formatarData  = new SimpleDateFormat("yyyy-MM-dd");
				hql.append("UPDATE Pedido SET dataEntrega = '" + formatarData.format(entity.getDataEntrega()) + " 00:00:00', usuarioAlteracao.id = "
						+ usuario.getId() + ", dataAlteracao = now() where id = " + entity.getId());

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
					return "Opera��o N�o Realizada. Contact o ADM do sistema";
				return null;
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return e.getMessage();
			} finally {
				getManager().close();
			}

		
		
	}

}
