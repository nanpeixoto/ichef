package br.com.ichef.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FILTRO")
	private Date datafiltro;

	@Column(name = "NR_QTD")
	private Integer quantidade;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	// @Column(name = "DS_VALIDADE")
	// private String validade;

	@Column(name = "ORDEM_DERIVACAO")
	private Integer ordemDerivacao;

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
	@JoinColumn(name = "CD_FICHA_TEC_PRATO_TIPO")
	private FichaTecnicaPratoTipo fichaTecnicaPratoTipo;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CLIENTE_ENDERECO")
	private ClienteEndereco clienteEndereco;

	@Transient
	private List<PedidoDerivacaoContagem> pedidoDerivacaoContagem;

	@Column(name = "VL_SALDO")
	private BigDecimal saldo;

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
		return "dataEntrega,entregador.nome,ordemDerivacao,ordemEntrega,cliente.nome";
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		return null;
	}

	@Override
	public Date getDataCadastro() {
		return null;
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

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
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

	public Integer getOrdemDerivacao() {
		return ordemDerivacao;
	}

	public void setOrdemDerivacao(Integer ordemDerivacao) {
		this.ordemDerivacao = ordemDerivacao;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getValidade() {
		Calendar calend = Calendar.getInstance();
		calend.setTime(datafiltro);
		if (tipoPrato.isCongelado())
			calend.add(Calendar.DAY_OF_MONTH, 90);

		DateFormat df = new SimpleDateFormat("dd/MM"); // para formatar a data

		return "Consumir até " + df.format(calend.getTime());
	}

}
