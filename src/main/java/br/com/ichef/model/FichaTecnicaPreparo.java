package br.com.ichef.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.util.JSFUtil;

/**
 * The persistent class for the ficha_tecnica_preparo database table.
 * 
 */
@Entity
@Table(name = "ficha_tecnica_preparo")
public class FichaTecnicaPreparo extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FICHA_TECNICA_PERPARO")
	private Long id;;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Column(name = "DS_DESCRICAO")
	private String descricao;

	@Column(name = "SN_COPIA")
	private String copia;

	@Column(name = "DS_MODO_PREPARO")
	private String modoPreparo;

	@Column(name = "DS_OBERVACAO")
	private String obervacao;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	// @Column(name = "NR_PRECO_CUSTO_RECEITA")
	// private BigDecimal precoCustoReceita;

	// @Column(name = "NR_PRECO_CUSTO_PORCAO")
	// private BigDecimal precoCustoPorcao;

	// @Column(name = "NR_PRECO_VENDA_PORCAO")
	// private BigDecimal precoVendaPorcao;

	// @Column(name = "NR_PRECO_VENDA_RECEITA")
	// private BigDecimal precoVendaReceita;

	@Column(name = "NR_TAMANHO")
	private Long tamanho;

	@Column(name = "NR_TAMANHO_PORCAO_GRAMAS")
	private Long tamanhoPorcaoGramas;

	@Column(name = "TP_CLASSIFICACAO")
	private String classificacao;

	@OneToMany( mappedBy = "fichaTecnicaPreparo", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<FichaTecnicaPreparoInsumo> insumos;

	@Transient
	private boolean isAtivo;

	@Transient
	private boolean mostraPreparo;

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

	public boolean isEdicao() {
		return !isInclusao();
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public String getDescricao() {
		if (descricao == null)
			return descricao;
		return descricao.toUpperCase();
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public String getModoPreparo() {
		return modoPreparo;
	}

	public void setModoPreparo(String modoPreparo) {
		this.modoPreparo = modoPreparo;
	}

	public String getObervacao() {
		return obervacao;
	}

	public void setObervacao(String obervacao) {
		this.obervacao = obervacao;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public BigDecimal getPrecoCustoReceita() {
		return getCustoTotal();
	}

	// public void setPrecoCustoReceita(BigDecimal precoCustoReceita) {
	// this.precoCustoReceita = precoCustoReceita;
	// }

	public BigDecimal getPrecoCustoPorcao() {
		return (getCustoTotal().doubleValue() > 0d
				? (getCustoTotal().divide(new BigDecimal(getTamanho()), BigDecimal.ROUND_UP)).setScale(2,
						RoundingMode.CEILING)
				: getCustoTotal());
	}

	// public void setPrecoCustoPorcao(BigDecimal precoCustoPorcao) {
	// this.precoCustoPorcao = precoCustoPorcao;
	// }

	public BigDecimal getPrecoVendaPorcao() {
		return (getPrecoVendaReceita().doubleValue() > 0d
				? (getPrecoVendaReceita().divide(new BigDecimal(getTamanho()), BigDecimal.ROUND_UP)).setScale(2,
						RoundingMode.CEILING)
				: getPrecoVendaReceita());
	}

	// public void setPrecoVendaPorcao(BigDecimal precoVendaPorcao) {
	// this.precoVendaPorcao = precoVendaPorcao;
	// }

	public BigDecimal getCustoTotal() {
		try {
			BigDecimal custoTotal = new BigDecimal(0);
			for (FichaTecnicaPreparoInsumo insumo : getInsumos()) {
				if (insumo.getAtivo().equalsIgnoreCase("S"))
					custoTotal = custoTotal
							.add(insumo.getCustoTotal() == null ? new BigDecimal(0) : insumo.getCustoTotal());

			}
			return custoTotal;
		} catch (Exception e) {
			
		}

		return new BigDecimal(0);
	}

	public BigDecimal getPrecoVendaReceita() {
		Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
		return (getCustoTotal().doubleValue() > 0d
				? getCustoTotal().divide(new BigDecimal(config.getCustoMercadoriaVendida()).divide(new BigDecimal(100)))
						.setScale(2, RoundingMode.CEILING)
				: getCustoTotal());
	}

	// public void setPrecoVendaReceita(BigDecimal precoVendaReceita) {
	// this.precoVendaReceita = precoVendaReceita;
	// }

	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	public Long getTamanhoPorcaoGramas() {
		return tamanhoPorcaoGramas;
	}

	public void setTamanhoPorcaoGramas(Long tamanhoPorcaoGramas) {
		this.tamanhoPorcaoGramas = tamanhoPorcaoGramas;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public List<FichaTecnicaPreparoInsumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(List<FichaTecnicaPreparoInsumo> insumos) {
		this.insumos = insumos;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@Override
	public FichaTecnicaPreparo clone() throws CloneNotSupportedException {
		return (FichaTecnicaPreparo) super.clone();
	}

	public String getCopia() {
		return copia;
	}

	public void setCopia(String copia) {
		this.copia = copia;
	}

	public String getDescricaoComInsumos() {
		return getDescricao() + "<br/>(" + getDescricaoInsumo() + ")";
	}

	public String getDescricaoInsumo() {
		String insumos = "";
		for (FichaTecnicaPreparoInsumo insumo : getInsumos()) {
			if (insumos != "")
				insumos += "/ ";
			insumos += insumo.getInsumo().getDescricao();
		}
		return insumos;
	}

	public boolean isMostraPreparo() {
		return mostraPreparo;
	}

	public void setMostraPreparo(boolean mostraPreparo) {
		this.mostraPreparo = mostraPreparo;
	}

}