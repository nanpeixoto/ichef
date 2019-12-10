package br.com.ichef.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.persistence.Transient;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.util.JSFUtil;

@Entity
@Table(name = "ficha_tecnica_prato")
public class FichaTecnicaPrato extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FICHA_TECNICA_PRATO")
	private Long id;

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

	// bi-directional many-to-one association to FichaTecnicaPratoPreparo
	@OneToMany(mappedBy = "fichaTecnicaPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FichaTecnicaPratoPreparo> fichaTecnicaPratoPreparos;

	// bi-directional many-to-one association to FichaTecnicaPratoTipo
	@OneToMany(mappedBy = "fichaTecnicaPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FichaTecnicaPratoTipo> fichaTecnicaPratoTipos;

	@Transient
	private boolean isAtivo;

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

	public String getCopia() {
		return copia;
	}

	public void setCopia(String copia) {
		this.copia = copia;
	}

	public BigDecimal getPrecoCustoPorcao() {
		try {
			return (getCustoTotal().doubleValue() > 0d
					? (getCustoTotal().divide(new BigDecimal(1), BigDecimal.ROUND_UP)).setScale(2, RoundingMode.CEILING)
					: getCustoTotal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}

	// public void setPrecoCustoPorcao(BigDecimal precoCustoPorcao) {
	// this.precoCustoPorcao = precoCustoPorcao;
	// }

	public BigDecimal getPrecoVendaPorcao() {
		try {
			return (getPrecoVendaReceita().doubleValue() > 0d
					? (getPrecoVendaReceita().divide(new BigDecimal(1), BigDecimal.ROUND_UP)).setScale(2,
							RoundingMode.CEILING)
					: getPrecoVendaReceita());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}

	// public void setPrecoVendaPorcao(BigDecimal precoVendaPorcao) {
	// this.precoVendaPorcao = precoVendaPorcao;
	// }

	public BigDecimal getCustoTotal() {
		BigDecimal custoTotal = new BigDecimal(0);
		try {

			for (FichaTecnicaPratoPreparo item : getFichaTecnicaPratoPreparos()) {
				if (item.getAtivo().equalsIgnoreCase("S"))
					custoTotal = custoTotal
							.add(item.getCustoTotal() == null ? new BigDecimal(0) : item.getCustoTotal());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custoTotal;
	}

	public BigDecimal getPrecoVendaReceita() {
		try {
			Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
			return getCustoTotal().doubleValue() > 0d ? getCustoTotal()
					.divide(new BigDecimal(config.getCustoMercadoriaVendida()).divide(new BigDecimal(100)))
					.setScale(2, RoundingMode.CEILING) : getCustoTotal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}

	// public void setPrecoVendaReceita(BigDecimal precoVendaReceita) {
	// this.precoVendaReceita = precoVendaReceita;
	// }

	public List<FichaTecnicaPratoPreparo> getFichaTecnicaPratoPreparos() {
		return fichaTecnicaPratoPreparos;
	}

	public void setFichaTecnicaPratoPreparos(List<FichaTecnicaPratoPreparo> fichaTecnicaPratoPreparos) {
		this.fichaTecnicaPratoPreparos = fichaTecnicaPratoPreparos;
	}

	public List<FichaTecnicaPratoTipo> getFichaTecnicaPratoTipos() {
		return fichaTecnicaPratoTipos;
	}

	public void setFichaTecnicaPratoTipos(List<FichaTecnicaPratoTipo> fichaTecnicaPratoTipos) {
		this.fichaTecnicaPratoTipos = fichaTecnicaPratoTipos;
	}

	@Override
	public FichaTecnicaPrato clone() throws CloneNotSupportedException {
		return (FichaTecnicaPrato) super.clone();
	}

	public String getDescricaoComPreparo() {
		return getDescricao() + "\r(" + getDescricaoPreparo() + ")";
	}

	public String getDescricaoPreparo() {
		String insumos = "";
		for (FichaTecnicaPratoPreparo preparo : getFichaTecnicaPratoPreparos()) {
			if (insumos != "")
				insumos += "/ ";
			insumos += preparo.getFichaTecnicaPreparo().getDescricao();
		}
		return insumos;
	}

	public String getPercoPorTipoPrato() {
		String valoresPorTipo = "";
		if (fichaTecnicaPratoTipos != null) {
			for (FichaTecnicaPratoTipo fichaTipo : fichaTecnicaPratoTipos) {
				if (valoresPorTipo != "")
					valoresPorTipo += "<br>";
				valoresPorTipo += fichaTipo.getTipoPrato().getDescricao() + " "
						+ formataValor(fichaTipo.getCustoTotal().add(getPrecoCustoPorcao()));
			}
		}
		return valoresPorTipo;
	}

	public BigDecimal getPercoPorTipoPrato(TipoPrato tipoPrato) {

		for (FichaTecnicaPratoTipo fichaTipo : fichaTecnicaPratoTipos) {
			if (tipoPrato.getId().equals(fichaTipo.getTipoPrato().getId()))
				return fichaTipo.getCustoTotal().add(getPrecoCustoPorcao());
		}

		return null;
	}

	public String getPercoPorTipoPratoPrincipal() {
		String precoPorTipo = "0";
		boolean precoEncontrado = false;

		try {
			Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
			for (FichaTecnicaPratoTipo fichaTipo : fichaTecnicaPratoTipos) {
				if (config.getTipoPrato().getId().equals(fichaTipo.getTipoPrato().getId())) {
					precoPorTipo = (String) formataValor(
							getPrecoCustoPorcao().add(config.getTipoPrato().getCustoTotal()));
					precoEncontrado = true;
				}
			}
			if (!precoEncontrado) {
				precoPorTipo = (String) formataValor(
						getPrecoCustoPorcao().add(fichaTecnicaPratoTipos.get(0).getCustoTotal()));
			}
		} catch (Exception e) {
			return "0";
		}
		return precoPorTipo;
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
}