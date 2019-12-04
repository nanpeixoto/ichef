package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "pedido")
public class Pedido extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_PEDIDO")
	private Long id;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_PEDIDO")
	private Date dataPedido;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO")
	private Cardapio cadapio;

	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO_PRATO")
	private CardapioFichaPrato cardapioFichaPrato;

	@ManyToOne
	@JoinColumn(name = "CD_ENTREGADOR")
	private Entregador entregador;

	@ManyToOne
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

	@Column(name = "NR_PRECO_CUSTO_RECEITA")
	private BigDecimal precoCustoReceita;

	@Column(name = "NR_PRECO_CUSTO_PORCAO")
	private BigDecimal precoCustoPorcao;

	@Column(name = "NR_PRECO_VENDA_PORCAO")
	private BigDecimal precoVendaPorcao;

	@Column(name = "NR_PRECO_VENDA_RECEITA")
	private BigDecimal precoVendaReceita;

	@Column(name = "NR_PRECO_VENDA")
	private BigDecimal precoVenda;

	@ManyToOne
	@JoinColumn(name = "CD_TIP_PRATO")
	private TipoPrato tipoPrato;

	@ManyToOne
	@JoinColumn(name = "CD_FORMA_PAGAMENTO")
	private FormaPagamento formaPagamento;

	@ManyToOne
	@JoinColumn(name = "CD_DERIVACAO")
	private Derivacao derivacao;

	@ManyToOne
	@JoinColumn(name = "CD_CLIENTE")
	private Pedido cliente;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@JoinColumn(name = "CD_CLIENTE_ENDERECO")
	private ClienteEndereco clienteEndereco;

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
		return null;
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
	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
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

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Cardapio getCadapio() {
		return cadapio;
	}

	public void setCadapio(Cardapio cadapio) {
		this.cadapio = cadapio;
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

	public BigDecimal getPrecoCustoReceita() {
		return precoCustoReceita;
	}

	public void setPrecoCustoReceita(BigDecimal precoCustoReceita) {
		this.precoCustoReceita = precoCustoReceita;
	}

	public BigDecimal getPrecoCustoPorcao() {
		return precoCustoPorcao;
	}

	public void setPrecoCustoPorcao(BigDecimal precoCustoPorcao) {
		this.precoCustoPorcao = precoCustoPorcao;
	}

	public BigDecimal getPrecoVendaPorcao() {
		return precoVendaPorcao;
	}

	public void setPrecoVendaPorcao(BigDecimal precoVendaPorcao) {
		this.precoVendaPorcao = precoVendaPorcao;
	}

	public BigDecimal getPrecoVendaReceita() {
		return precoVendaReceita;
	}

	public void setPrecoVendaReceita(BigDecimal precoVendaReceita) {
		this.precoVendaReceita = precoVendaReceita;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public TipoPrato getTipoPrato() {
		return tipoPrato;
	}

	public void setTipoPrato(TipoPrato tipoPrato) {
		this.tipoPrato = tipoPrato;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Derivacao getDerivacao() {
		return derivacao;
	}

	public void setDerivacao(Derivacao derivacao) {
		this.derivacao = derivacao;
	}

	public Pedido getCliente() {
		return cliente;
	}

	public void setCliente(Pedido cliente) {
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

}
