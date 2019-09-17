package br.com.ichef.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.enumerator.Classificacao;

/**
 * The persistent class for the ficha_tecnica_preparo database table.
 * 
 */
@Entity
@Table(name = "ficha_tecnica_preparo")
public class FichaTecnicaPreparo extends BaseEntity {
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

	@Column(name = "NR_PERCO_CUSTO_RECEITA")
	private int nrPercoCustoReceita;

	@Column(name = "NR_PRECO_CUSTO_PORCAO")
	private int nrPrecoCustoPorcao;

	@Column(name = "NR_PRECO_VENDA_PORCAO")
	private int nrPrecoVendaPorcao;

	@Column(name = "NR_PRECO_VENDA_RECEITA")
	private int nrPrecoVendaReceita;

	@Column(name = "NR_TAMANHO")
	private int nrTamanho;

	@Column(name = "NR_TAMANHO_PORCAO_GRAMAS")
	private int nrTamanhoPorcaoGramas;

	@Column(name = "TP_CLASSIFICACAO")
	@Enumerated(EnumType.STRING)
	private Classificacao classificacao;

	@OneToMany(mappedBy = "fichaTecnicaPreparo")
	private List<FichaTecnicaPreparoInsumo> insumos;

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

	public int getNrPercoCustoReceita() {
		return nrPercoCustoReceita;
	}

	public void setNrPercoCustoReceita(int nrPercoCustoReceita) {
		this.nrPercoCustoReceita = nrPercoCustoReceita;
	}

	public int getNrPrecoCustoPorcao() {
		return nrPrecoCustoPorcao;
	}

	public void setNrPrecoCustoPorcao(int nrPrecoCustoPorcao) {
		this.nrPrecoCustoPorcao = nrPrecoCustoPorcao;
	}

	public int getNrPrecoVendaPorcao() {
		return nrPrecoVendaPorcao;
	}

	public void setNrPrecoVendaPorcao(int nrPrecoVendaPorcao) {
		this.nrPrecoVendaPorcao = nrPrecoVendaPorcao;
	}

	public int getNrPrecoVendaReceita() {
		return nrPrecoVendaReceita;
	}

	public void setNrPrecoVendaReceita(int nrPrecoVendaReceita) {
		this.nrPrecoVendaReceita = nrPrecoVendaReceita;
	}

	public int getNrTamanho() {
		return nrTamanho;
	}

	public void setNrTamanho(int nrTamanho) {
		this.nrTamanho = nrTamanho;
	}

	public int getNrTamanhoPorcaoGramas() {
		return nrTamanhoPorcaoGramas;
	}

	public void setNrTamanhoPorcaoGramas(int nrTamanhoPorcaoGramas) {
		this.nrTamanhoPorcaoGramas = nrTamanhoPorcaoGramas;
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
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

}