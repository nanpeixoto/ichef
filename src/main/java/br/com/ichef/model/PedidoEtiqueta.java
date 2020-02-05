package br.com.ichef.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_pedido_etiqueta")
public class PedidoEtiqueta extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_pedido_id")
	private Long id;

	@Column(name = "CD_PEDIDO")
	private Long codigoPedido;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FILTRO")
	private Date datafiltro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "NR_QTD")
	private Integer quantidade;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CARDAPIO_PRATO")
	private CardapioFichaPrato cardapioFichaPrato;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_ENTREGADOR")
	private Entregador entregador;

	@Column(name = "NR_ORDEM_ENTREGA")
	private Integer ordemEntrega;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_LOCALIDADE")
	private Localidade localidade;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_TIP_PRATO")
	private TipoPrato tipoPrato;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_DERIVACAO")
	private Derivacao derivacao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CLIENTE")
	private Cliente cliente;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_FICHA_TEC_PRATO_TIPO")
	private FichaTecnicaPratoTipo fichaTecnicaPratoTipo;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CLIENTE_ENDERECO")
	private ClienteEndereco clienteEndereco;

	@Column(name = "NR_PRECO_VENDA_RECEITA")
	private BigDecimal precoVendaReceita;

	@Column(name = "NR_PRECO_CUSTO_PORCAO")
	private BigDecimal precoCustoPorcao;

	@Column(name = "NR_PRECO_CUSTO_TIPO_PRATO")
	private BigDecimal precoVendaTipoPrato;

	@Column(name = "SN_CONFIRMADO")
	private String snConfirmado;

	@Column(name = "LOG_LANCAMENTO_CARTEIRA")
	private String logLancamentoCarteira;

	@Transient
	private boolean confirmado;

	@Transient
	private List<PedidoDerivacaoContagem> pedidoDerivacaoContagem;

	public boolean isConfirmado() {
		if (snConfirmado != null) {
			if (snConfirmado.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
		if (confirmado == Boolean.TRUE) {
			setSnConfirmado("S");
		} else
			setSnConfirmado("N");
	}

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return "dataEntrega,entregador.nome,ordemEntrega,cliente.nome";
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	@Override
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	@Override
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getObservacao() {
		if (observacao == null || observacao.equalsIgnoreCase(""))
			return "-";
		return observacao;

	}

	public void setObservacao(String observacao) {
		try {

			if (observacao != null)
				this.observacao = observacao.toUpperCase();
			else
				this.observacao = observacao;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CardapioFichaPrato getCardapioFichaPrato() {
		return cardapioFichaPrato;
	}

	public void setCardapioFichaPrato(CardapioFichaPrato cardapioFichaPrato) {
		this.cardapioFichaPrato = cardapioFichaPrato;
	}

	public Entregador getEntregador() {
		return entregador;
	}

	public void setEntregador(Entregador entregador) {
		this.entregador = entregador;
	}

	public TipoPrato getTipoPrato() {
		return tipoPrato;
	}

	public void setTipoPrato(TipoPrato tipoPrato) {
		this.tipoPrato = tipoPrato;
	}

	public Derivacao getDerivacao() {
		return derivacao;
	}

	public void setDerivacao(Derivacao derivacao) {
		this.derivacao = derivacao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ClienteEndereco getClienteEndereco() {
		return clienteEndereco;
	}

	public void setClienteEndereco(ClienteEndereco clienteEndereco) {
		this.clienteEndereco = clienteEndereco;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public BigDecimal getPrecoVendaReceita() {
		return precoVendaReceita;
	}

	public void setPrecoVendaReceita(BigDecimal precoVendaReceita) {
		this.precoVendaReceita = precoVendaReceita;
	}

	public BigDecimal getPrecoCustoPorcao() {
		return precoCustoPorcao;
	}

	public void setPrecoCustoPorcao(BigDecimal precoCustoPorcao) {
		this.precoCustoPorcao = precoCustoPorcao;
	}

	public BigDecimal getPrecoVendaTipoPrato() {
		return precoVendaTipoPrato;
	}

	public void setPrecoVendaTipoPrato(BigDecimal precoVendaTipoPrato) {
		this.precoVendaTipoPrato = precoVendaTipoPrato;
	}

	public FichaTecnicaPratoTipo getFichaTecnicaPratoTipo() {
		return fichaTecnicaPratoTipo;
	}

	public void setFichaTecnicaPratoTipo(FichaTecnicaPratoTipo fichaTecnicaPratoTipo) {
		this.fichaTecnicaPratoTipo = fichaTecnicaPratoTipo;
	}

	public String getDataFormatadaPedido() {
		try {
			SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
			return sdate.format(getDataEntrega());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDataEntregaFormatada() {
		try {
			SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
			return sdate.format(getDataEntrega());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSnConfirmado() {
		return snConfirmado;
	}

	public void setSnConfirmado(String snConfirmado) {
		this.snConfirmado = snConfirmado;
	}

	public String getLogLancamentoCarteira() {
		return logLancamentoCarteira;
	}

	public void setLogLancamentoCarteira(String logLancamentoCarteira) {
		this.logLancamentoCarteira = logLancamentoCarteira;
	}

	public List<PedidoDerivacaoContagem> getPedidoDerivacaoContagem() {
		return pedidoDerivacaoContagem;
	}

	public void setPedidoDerivacaoContagem(List<PedidoDerivacaoContagem> pedidoDerivacaoContagem) {
		this.pedidoDerivacaoContagem = pedidoDerivacaoContagem;
	}

	public Long getCodigoPedido() {
		return codigoPedido;
	}

	public void setCodigoPedido(Long codigoPedido) {
		this.codigoPedido = codigoPedido;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getOrdemEntrega() {
		return ordemEntrega;
	}

	public void setOrdemEntrega(Integer ordemEntrega) {
		this.ordemEntrega = ordemEntrega;
	}

	public Date getDatafiltro() {
		return datafiltro;
	}

	public void setDatafiltro(Date datafiltro) {
		this.datafiltro = datafiltro;
	}

}
