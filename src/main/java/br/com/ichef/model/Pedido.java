package br.com.ichef.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

	@Column(name = "NR_QTD")
	private Integer quantidade;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO")
	private Cardapio cardapio;

	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO_PRATO")
	private CardapioFichaPrato cardapioFichaPrato;

	@ManyToOne
	@JoinColumn(name = "CD_ENTREGADOR")
	private Entregador entregador;

	@ManyToOne
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "CD_LOCALIDADE")
	private Localidade localidade;

	@Column(name = "VL_DIARIA_ENTREGADOR")
	private BigDecimal valorDiariaEntregador;

	@Column(name = "VL_PEDIDO")
	private BigDecimal valorPedido;

	@Column(name = "NR_ORDEM_ENTREGA")
	private Integer ordemEntrega;

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
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TEC_PRATO_TIPO")
	private FichaTecnicaPratoTipo fichaTecnicaPratoTipo;

	@ManyToOne
	@JoinColumn(name = "CD_CLIENTE_ENDERECO")
	private ClienteEndereco clienteEndereco;

	@Column(name = "NR_PRECO_VENDA_RECEITA")
	private BigDecimal precoVendaReceita;

	@Column(name = "NR_PRECO_CUSTO_PORCAO")
	private BigDecimal precoCustoPorcao;

	@Column(name = "NR_PRECO_CUSTO_TIPO_PRATO")
	private BigDecimal precoVendaTipoPrato;

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
		try {

			if (observacao != null)
				this.observacao = observacao.toUpperCase();
			else
				this.observacao = observacao;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
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

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Integer getOrdemEntrega() {
		return ordemEntrega;
	}

	public void setOrdemEntrega(Integer ordemEntrega) {
		this.ordemEntrega = ordemEntrega;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public BigDecimal getValorDiariaEntregador() {
		return valorDiariaEntregador;
	}

	public void setValorDiariaEntregador(BigDecimal valorDiariaEntregador) {
		this.valorDiariaEntregador = valorDiariaEntregador;
	}

	public BigDecimal getValorPedido() {
		return valorPedido;
	}

	public void setValorPedido(BigDecimal valorPedido) {
		this.valorPedido = valorPedido;
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
			return sdate.format(getDataPedido());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
