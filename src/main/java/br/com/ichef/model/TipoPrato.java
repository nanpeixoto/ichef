package br.com.ichef.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

/**
 * The persistent class for the tip_prato database table.
 * 
 */
@Entity
@Table(name = "tip_prato")
public class TipoPrato extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public static final long TIPO_PRATO_CONGELADO = 5l;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_TIP_PRATO")
	private Long id;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Column(name = "DS_TIP_PRATO")
	private String descricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "SN_PLUS")
	private String snPlus;

	@Column(name = "SN_CONTAGEM")
	private String snContagem;

	@Column(name = "SN_CONGELADO")
	private String snCongelado;

	@Transient
	private boolean isAtivo;

	@Transient
	private boolean congelado;

	@Transient
	private boolean plus;

	@Transient
	private boolean contagem;

	// bi-directional many-to-one association to TipPratoPreco
	@OneToMany(mappedBy = "tipoPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<TipoPratoPreco> precos;

	@OneToMany(mappedBy = "tipoPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<TipoPratoInsumo> insumos;

	// @OneToMany(mappedBy = "tipoPrato")
	// private List<VwTipoPratoPreco> ultimoPreco;

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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public String getDescricao() {
		return descricao == null ? descricao : descricao.toUpperCase();
	}

	public String getDescricaoSemQuentinha() {
		String retornoSemQuentinha = "";
		if (getDescricao().contains("QUENTINHA")) {
			retornoSemQuentinha = getDescricao().replace("QUENTINHA", "").replace(" - ", "");
		} else {
			retornoSemQuentinha = getDescricao();
		}

		if (!retornoSemQuentinha.equalsIgnoreCase(""))

			return retornoSemQuentinha + "-";
		else
			return retornoSemQuentinha;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
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

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
		if (isAtivo == Boolean.TRUE) {
			setAtivo("S");
		} else
			setAtivo("N");
	}

	public boolean isCongelado() {
		if (snCongelado != null) {
			if (snCongelado.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return congelado;
	}

	public void setCongelado(boolean congelado) {
		this.congelado = congelado;
		if (congelado == Boolean.TRUE) {
			setSnCongelado("S");
		} else
			setSnCongelado("N");
	}

	public List<TipoPratoPreco> getPrecos() {
		return precos;
	}

	public void setPrecos(List<TipoPratoPreco> precos) {
		this.precos = precos;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public List<TipoPratoInsumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(List<TipoPratoInsumo> insumos) {
		this.insumos = insumos;
	}

	public Long getQuantidadeTotal() {
		Long quantidadeTotal = 0l;
		if (insumos != null)
			for (TipoPratoInsumo tipoPratoInsumo : insumos) {
				quantidadeTotal += tipoPratoInsumo.getQuantidade();
			}
		return quantidadeTotal;
	}

	public BigDecimal getCustoTotal() {
		BigDecimal custoTotal = new BigDecimal("0");
		if (insumos != null)
			for (TipoPratoInsumo tipoPratoInsumo : insumos) {
				custoTotal = custoTotal.add(tipoPratoInsumo.getCustoTotal());
			}
		return custoTotal.setScale(2, RoundingMode.CEILING);
	}

	public String getDescricaoComValorInsumo() {
		return getDescricao() + " " + formataValor(getCustoTotal());
	}

	public Object formataValor(Object valor) {
		try {

			if (valor != null) {
				return "R$ " + valor.toString().replaceAll(",", ".").replace(".", ",");
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

	public TipoPratoPreco getPrecoAtual() {
		TipoPratoPreco preco = null;
		if (getPrecos() != null && getPrecos().size() > 0) {
			preco = new TipoPratoPreco();

			Collections.sort(getPrecos());
			preco = getPrecos().get(0);

		}
		return preco;
	}

	// public List<VwTipoPratoPreco> getUltimoPreco() {
	// return ultimoPreco;
	// }

	// public void setUltimoPreco(List<VwTipoPratoPreco> ultimoPreco) {
	// this.ultimoPreco = ultimoPreco;
	// }

	public String getSnPlus() {
		return snPlus;
	}

	public void setSnPlus(String snPlus) {
		this.snPlus = snPlus;
	}

	public boolean isPlus() {
		if (snPlus != null) {
			if (snPlus.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return plus;
	}

	public void setPlus(boolean isAtivo) {
		this.plus = isAtivo;
		if (isAtivo == Boolean.TRUE) {
			setSnPlus("S");
		} else
			setSnPlus("N");
	}

	public String getSnContagem() {
		return snContagem;
	}

	public void setSnContagem(String snContagem) {
		this.snContagem = snContagem;
	}

	public boolean isContagem() {
		if (snContagem != null) {
			if (snContagem.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return contagem;
	}

	public void setContagem(boolean contagem) {
		this.contagem = isAtivo;
		if (contagem == Boolean.TRUE) {
			setSnContagem("S");
		} else
			setSnContagem("N");

	}

	public String getSnCongelado() {
		return snCongelado;
	}

	public void setSnCongelado(String snCongelado) {
		this.snCongelado = snCongelado;
	}

}