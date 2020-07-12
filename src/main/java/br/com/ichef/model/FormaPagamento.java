package br.com.ichef.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FORMA_PAGAMENTO")
	private Long id;

	@Column(name = "DS_FORMA_PAGAMENTO")
	private String descricao;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "SN_CARTEIRA")
	private String carteira;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "TAXA")
	private Double taxa;

	@Column(name = "SN_PERCENTUAL")
	private String percentual;

	@Column(name = "SN_CORTESIA")
	private String statusCortesia;

	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Transient
	private boolean isAtivo;

	@Transient
	private boolean cortesia;

	@Transient
	private boolean isCarteira;

	@Transient
	private boolean isPercentual;

	public boolean isPercentual() {
		if (percentual != null) {
			if (percentual.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return isPercentual;
	}

	public void setPercentual(boolean isAtivo) {
		this.isPercentual = isAtivo;
		if (isPercentual == Boolean.TRUE) {
			setPercentual("S");
		} else
			setPercentual("N");
	}

	public String getPercentual() {
		return percentual;
	}

	public void setPercentual(String percentual) {
		this.percentual = percentual;
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

	public boolean isCarteira() {
		if (carteira != null) {
			if (carteira.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return isCarteira;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
		if (isAtivo == Boolean.TRUE) {
			setAtivo("S");
		} else
			setAtivo("N");
	}

	public void setCarteira(boolean isCarteira) {
		this.isCarteira = isCarteira;
		if (isCarteira == Boolean.TRUE) {
			setCarteira("S");
		} else
			setCarteira("N");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormaPagamento other = (FormaPagamento) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "descricao";
	}

	@Override
	public String getAuditoria() {
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

	public String getDescricao() {
		if (descricao == null)
			return descricao;
		return descricao.toUpperCase();
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
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

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public String getDescricaoTipoTaxa() {
		if (getTaxa() != null) {
			if (getPercentual().equals("N"))
				return "Taxa de ".toUpperCase() + formataValor(getTaxa());
			return "Percentual de ".toUpperCase() + formataDecimal(getTaxa()) + "%";
		}
		return "";
	}

	public String geteCarteira() {
		if (getCarteira().equals("S"))
			return "Sim".toUpperCase();
		return "Não".toUpperCase();
	}

	public String getDescricaoCortesia() {
		if (getStatusCortesia().equals("S"))
			return "Sim".toUpperCase();
		return "Não".toUpperCase();
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public Double getTaxa() {
		return taxa;
	}

	public void setTaxa(Double taxa) {
		this.taxa = taxa;
	}

	public Object formataDecimal(Object valor) {
		try {
			NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

			if (valor != null) {
				return nf.format(valor);
				// return "R$ " + valor.toString().replaceAll(",", ".").replace(".", ",");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			return valor.toString();
		} catch (Exception e) {
			return valor;
		}

	}

	public Object formataValor(Object valor) {
		try {
			Locale meuLocal = new Locale("pt", "BR");
			NumberFormat real = NumberFormat.getCurrencyInstance(meuLocal);

			if (valor != null) {
				return real.format(valor);
				// return "R$ " + valor.toString().replaceAll(",", ".").replace(".", ",");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			return valor.toString();
		} catch (Exception e) {
			return valor;
		}

	}

	public String getStatusCortesia() {
		return statusCortesia;
	}

	public void setStatusCortesia(String statusCortesia) {
		this.statusCortesia = statusCortesia;
	}

	public boolean isCortesia() {
		if (getStatusCortesia() != null) {
			if (getStatusCortesia().equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return cortesia;
	}

	public void setCortesia(boolean isAtivo) {
		this.cortesia = isAtivo;
		if (cortesia == Boolean.TRUE) {
			setStatusCortesia("S");
		} else
			setStatusCortesia("N");
	}

	@Override
	public Object getId() {
		return (Long) id;
	}

}
