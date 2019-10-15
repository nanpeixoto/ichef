package br.com.ichef.model;

import java.math.BigDecimal;
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

	@Column(name = "NR_PRECO_CUSTO_RECEITA")
	private BigDecimal precoCustoReceita;

	@Column(name = "NR_PRECO_CUSTO_PORCAO")
	private BigDecimal precoCustoPorcao;

	@Column(name = "NR_PRECO_VENDA_PORCAO")
	private BigDecimal precoVendaPorcao;

	@Column(name = "NR_PRECO_VENDA_RECEITA")
	private BigDecimal precoVendaReceita;

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
		return precoCustoReceita;
	}

	public void setPrecoCustoReceita(BigDecimal precoCustoReceita) {
		this.precoCustoReceita = precoCustoReceita;
	}

	public String getCopia() {
		return copia;
	}

	public void setCopia(String copia) {
		this.copia = copia;
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
	

	public String getPercoPorTipoPrato () {
		String valoresPorTipo = "";
		for (FichaTecnicaPratoTipo fichaTipo : fichaTecnicaPratoTipos) {
			if(valoresPorTipo!="")
				valoresPorTipo += "<br>";
			valoresPorTipo += fichaTipo.getTipoPrato().getDescricao()+" " +formataValor(fichaTipo.getCustoTotal());
		}
		return valoresPorTipo;
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