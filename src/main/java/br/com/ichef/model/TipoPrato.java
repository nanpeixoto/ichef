package br.com.ichef.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
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

	@Transient
	private boolean isAtivo;

	// bi-directional many-to-one association to TipPratoPreco
	@OneToMany(mappedBy = "tipoPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)

	private List<TipoPratoPreco> precos;

	@OneToMany(mappedBy = "tipoPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<TipoPratoInsumo> insumos;

	@OneToMany(mappedBy = "tipoPrato")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<VwTipoPratoPreco> ultimoPreco;

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

	public Set<TipoPratoInsumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(Set<TipoPratoInsumo> insumos) {
		this.insumos = insumos;
	}

	public Set<VwTipoPratoPreco> getUltimoPreco() {
		return ultimoPreco;
	}

	public void setUltimoPreco(Set<VwTipoPratoPreco> ultimoPreco) {
		this.ultimoPreco = ultimoPreco;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static long getTipoPratoCongelado() {
		return TIPO_PRATO_CONGELADO;
	}

	public void setId(Long id) {
		this.id = id;
	}

}