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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

/**
 * The persistent class for the cliente_carteira database table.
 * 
 */
@Entity
@Table(name = "cliente_carteira")
public class ClienteCarteira extends BaseEntity  implements Comparable<ClienteCarteira>{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CARTEIRA")
	private Long id;

	@Column(name = "DATA")
	private Date data;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastrado;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_DERIVACAO")
	private Derivacao derivacao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_TIP_PRATO")
	private TipoPrato tipoPrato;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_EMPRESA_LOGADA")
	private Empresa empresaLogada;

	@Column(name = "DS_CARTEIRA")
	private String descricao;

	/* (P)edido (C)redito (D)ebito */
	@Column(name = "SN_TIPO_CARTEIRA")
	private String tipoCarteira;

	@Column(name = "VALOR_DEVIDO")
	private BigDecimal valorDevido;

	@Column(name = "VALOR_PAGO")
	private BigDecimal valorPago;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CLIENTE")
	private Cliente cliente;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CARDAPIO")
	private Cardapio cardapio;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_FICHA_TEC_PRATO")
	private FichaTecnicaPrato fichaTecnicaPrato;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_FORMA_PAGAMENTO")
	private FormaPagamento formaPagamento;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_PEDIDO")
	private Pedido pedido;

	@Column(name = "VL_SALDO")
	private BigDecimal saldo;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}

	@Override
	public String getColumnOrderBy() {
		return "data";
	}

	@Override
	public String getAuditoria() {
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
	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public Date getDataCadastrado() {
		return dataCadastrado;
	}

	public void setDataCadastrado(Date dataCadastrado) {
		this.dataCadastrado = dataCadastrado;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	@Override
	public Date getDataCadastro() {
		return dataCadastrado;
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		if (descricao == null)
			return descricao;
		return descricao.toUpperCase();
	}

	public void setDescricao(String descricao) {
		if (descricao != null)
			this.descricao = descricao.toUpperCase();

	}

	public String getTipoCarteira() {
		return tipoCarteira;
	}

	public void setTipoCarteira(String tipoCarteira) {
		this.tipoCarteira = tipoCarteira;
	}

	public BigDecimal getValorDevido() {
		return valorDevido;
	}

	public void setValorDevido(BigDecimal valorDevido) {
		this.valorDevido = valorDevido;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public FichaTecnicaPrato getFichaTecnicaPrato() {
		return fichaTecnicaPrato;
	}

	public void setFichaTecnicaPrato(FichaTecnicaPrato fichaTecnicaPrato) {
		this.fichaTecnicaPrato = fichaTecnicaPrato;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getDescricaoOuPrato() {
		if (getDescricao() != null && getFichaTecnicaPrato() == null)
			return getDescricao();
		if (getFichaTecnicaPrato() != null && getDescricao() != null)
			return getDescricao() + " <br>" + getFichaTecnicaPrato().getDescricao() + "<br>"
					+ getTipoPrato().getDescricao() + "-"
					+ (getDerivacao() != null ? getDerivacao().getDescricao() : "");
		if (getFichaTecnicaPrato() != null && getDescricao() == null)
			return getFichaTecnicaPrato().getDescricao() + "<br>" + getTipoPrato().getDescricao() + "-"
					+ (getDerivacao() != null ? getDerivacao().getDescricao() : "");
		return getDescricao();
	}

	public Derivacao getDerivacao() {
		return derivacao;
	}

	public void setDerivacao(Derivacao derivacao) {
		this.derivacao = derivacao;
	}

	public TipoPrato getTipoPrato() {
		return tipoPrato;
	}

	public void setTipoPrato(TipoPrato tipoPrato) {
		this.tipoPrato = tipoPrato;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Empresa getEmpresaLogada() {
		return empresaLogada;
	}

	public void setEmpresaLogada(Empresa empresaLogada) {
		this.empresaLogada = empresaLogada;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	@Override
	public int compareTo(ClienteCarteira o) {
		return o.getData().compareTo(this.getData());
	}


}