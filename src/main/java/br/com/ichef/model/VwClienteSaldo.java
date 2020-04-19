package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.util.Util;

@Entity
@Table(name = "vw_cliente_saldo")
public class VwClienteSaldo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private VwClienteSaldoID id;

	@Column(name = "SALDO")
	private BigDecimal valorSaldo;

	@Column(name = "CD_LOCALIDADE")
	private Long codigoLocalidade;

	@Column(name = "CD_EMPRESA", insertable = false, updatable = false)
	private Long codigoEmpresa;

	//@Column(name = "CD_TIP_LOCALIDADE")
	//private Long codigoTipoLocalidade;

	@Column(name = "DS_LOCALIDADE")
	private String descricaoLocalidade;

	@Column(name = "NM_CLIENTE")
	private String nome;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "SN_BLOQUEADO")
	private String bloqueado;

	//@Column(name = "DS_TIP_LOCALIDADE")
	//private String descricaoTipoLocalidade;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Column(name = "DATA_CARTEIRA")
	private Date dataCarteira;

	// bi-directional many-to-one association to AreaLocalidade
	@OneToMany(mappedBy = "vwClienteSaldo")
	@Fetch(FetchMode.SELECT)
	private List<VwClienteCarteiraSaldo> saldos;

	@Column(name = "SALDO_OUTRA_EMPRESA")
	private BigDecimal valorSaldoOutraEmpresa;
	
	@Transient
	private boolean estaBloqueado;
	
	@Transient
	private boolean isAtivo;


	public long diasDevedor() {
		try {
			return Util.diferencaEmDias(new Date(), getDataCarteira());
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}

	}

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public void setId(Object id) {
		this.id = (VwClienteSaldoID) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return "nome";
		// return null;
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
	public Usuario getUsuarioCadastro() {
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
		return null;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<VwClienteCarteiraSaldo> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<VwClienteCarteiraSaldo> saldos) {
		this.saldos = saldos;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public Long getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Long codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}



	public String getDescricaoLocalidade() {
		return descricaoLocalidade;
	}

	public void setDescricaoLocalidade(String descricaoLocalidade) {
		this.descricaoLocalidade = descricaoLocalidade;
	}
	
	/*public Long getCodigoTipoLocalidade() {
		return codigoTipoLocalidade;
	}

	public void setCodigoTipoLocalidade(Long codigoTipoLocalidade) {
		this.codigoTipoLocalidade = codigoTipoLocalidade;
	}

	public String getDescricaoTipoLocalidade() {
		return descricaoTipoLocalidade;
	}

	public void setDescricaoTipoLocalidade(String descricaoTipoLocalidade) {
		this.descricaoTipoLocalidade = descricaoTipoLocalidade;
	}*/

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Date getDataCarteira() {
		return dataCarteira;
	}

	public void setDataCarteira(Date dataCarteira) {
		this.dataCarteira = dataCarteira;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValorSaldoOutraEmpresa() {
		return valorSaldoOutraEmpresa;
	}

	public void setValorSaldoOutraEmpresa(BigDecimal valorSaldoOutraEmpresa) {
		this.valorSaldoOutraEmpresa = valorSaldoOutraEmpresa;
	}

	public void setId(VwClienteSaldoID id) {
		this.id = id;
	}

	public String getLink() {
		return "../cliente/cadastro-cliente.xhtml?id=" + ((VwClienteSaldoID) getId()).getCodigoCliente()
				+ "#tabs:tabCarteira";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getListaSaldosEmail() {
		String mensagemFinal = "";
		BigDecimal saldoTotal = (getValorSaldoOutraEmpresa() != null ? getValorSaldo().add(getValorSaldoOutraEmpresa())
				: getValorSaldo());
		String mensagemSaldoTotal = "<br><br>Seu saldo atual é de <span     style=\"color: "
				+ (saldoTotal.compareTo(new BigDecimal("0")) >= 0 ? "red" : "green") + "; font-weight: bold;\">"
				+ Util.formataValor(saldoTotal) + "</span>";
		String outrasEmpresas = (getValorSaldoOutraEmpresa() != null && saldoTotal.compareTo(new BigDecimal("0")) >= 0
				? " <br> Empresa atual:" + Util.formataValor(getValorSaldo())
						+ (getValorSaldo().compareTo(new BigDecimal("0")) >= 0 ? "" : " (crédito) ")
						+ " - Outras empresas: " + Util.formataValor(getValorSaldoOutraEmpresa())
						+ (getValorSaldoOutraEmpresa().compareTo(new BigDecimal("0")) >= 0 ? "" : " (crédito) ") + "."
				: " ");
		String creditoDebito = (saldoTotal.compareTo(new BigDecimal("0")) >= 0 ? "" : " (CRÉDITO) ");

		mensagemSaldoTotal = mensagemSaldoTotal + creditoDebito + outrasEmpresas
				+ "<br><bR> Segue abaixo os valores listados para a empresa " + getNomeFantasia() + ": <br>";

		String listaDebitos = "";
		String thead = "<table role=\"grid\" border=\"1\">" + "<thead id=\"tabListaPedidos:0:tabListaSaldos_head\">"
				+ "<tr role=\"row\">"
				+ "<th  class=\"ui-state-default ui-sortable-column ui-filter-column\"   scope=\"col\" style=\"width:10%\" >"
				+ " Item " + "</th>"
				+ "<th  class=\"ui-state-default ui-sortable-column ui-filter-column\"   scope=\"col\" style=\"width:10%\" >"
				+ " Data " + "</th>"
				+ "<th  class=\"ui-state-default ui-sortable-column ui-filter-column\"   scope=\"col\" style=\"width:10%\" >"
				+ " Valor Devido " + "</th>"
				+ "<th  class=\"ui-state-default ui-sortable-column ui-filter-column\"   scope=\"col\" style=\"width:10%\" >"
				+ " Valor Pago " + "</th>"
				+ "<th  class=\"ui-state-default ui-sortable-column ui-filter-column\"   scope=\"col\" style=\"width:10%\" >"
				+ " Forma Pagamento " + "</th>" + "</tr>" + "</thead>"
				+ "<tbody class=\"ui-datatable-data ui-widget-content\">";
		String trInicio = "<tr  class=\"ui-widget-content ui-datatable-even\" role=\"row\">";
		String trFim = "</tr>";
		for (VwClienteCarteiraSaldo vwClienteCarteiraSaldo : getSaldos()) {
			listaDebitos = listaDebitos + trInicio + "<td style=\"text-align: center; \">"
					+ vwClienteCarteiraSaldo.getDescricaoOuPrato() + "</td>" + "<td style=\"text-align: center; \">"
					+ Util.formataData(vwClienteCarteiraSaldo.getData()) + "</td>"
					+ "<td style=\"text-align: center; \">"
					+ Util.formataValor(
							(vwClienteCarteiraSaldo.getValorDevido() != null ? vwClienteCarteiraSaldo.getValorDevido()
									: 0))
					+ "</td>" + "<td style=\"text-align: center; \">"
					+ Util.formataValor(
							(vwClienteCarteiraSaldo.getValorPago() != null ? vwClienteCarteiraSaldo.getValorPago() : 0))
					+ "</td>" + "<td style=\"text-align: center; \">"
					+ (vwClienteCarteiraSaldo.getFormaPagamento() != null
							? vwClienteCarteiraSaldo.getFormaPagamento().getDescricao()
							: "")
					+ "</td>" + trFim;
		}

		String tableFim = "</tbody></table>";

		mensagemFinal = mensagemSaldoTotal + thead + listaDebitos + tableFim;

		return mensagemFinal;
	}

	public String getSituacaoAtual() {
		String situacao = "";
		if (isEstaBloqueado()) {
			situacao = "BLOQUEADO";
		}

		if (!isAtivo()) {
			if (!situacao.equalsIgnoreCase(""))
				situacao += "/";
			situacao += "INATIVO";
		} else {
			if (!situacao.equalsIgnoreCase(""))
				situacao += "/";
			situacao += "ATIVO";
		}
		
		 

		return situacao;
	}

	public boolean isEstaBloqueado() {
		if (bloqueado != null) {
			if (bloqueado.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return estaBloqueado;
	}

	public boolean isAtivo() {
		if (ativo != null) {
			if (ativo.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return isAtivo;
	}

	public String getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	

}
