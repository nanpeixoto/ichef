package br.com.ichef.model;

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
@Table(name = "cardapio_ficha_prato")
public class CardapioFichaPrato extends BaseEntity implements Comparable<CardapioFichaPrato>  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CARDAPIO_PRATO")
	private Long id;

	@Column(name = "NR_QTD")
	private Integer quantidade;

	@Column(name = "DS_CARDAPIO_FICHA")
	private String descricao;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@Column(name = "NR_ORDEM")
	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	// bi-directional many-to-one association to Cardapio
	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO")
	private Cardapio cardapio;

	// bi-directional many-to-one association to FichaTecnicaPrato
	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TECNICA_PRATO")
	private FichaTecnicaPrato fichaTecnicaPrato;

	@Column(name = "SN_VENDER_ACIMA_LIMITE")
	private String venderAcimaDoLimite;

	@Transient
	private boolean podeVenderAcimaDoLimite;

	// bi-directional many-to-one association to AreaLocalidade
	@OneToMany(mappedBy = "cardapioFichaPrato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CardapioFichaPratoEmpresa> fichaPratoEmpresa;

	public boolean isPodeVenderAcimaDoLimite() {
		if (getVenderAcimaDoLimite() != null) {
			if (getVenderAcimaDoLimite().equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return podeVenderAcimaDoLimite;
	}

	public void setPodeVenderAcimaDoLimite(boolean podeVenderAcimaDoLimite) {
		this.podeVenderAcimaDoLimite = podeVenderAcimaDoLimite;
		if (podeVenderAcimaDoLimite == Boolean.TRUE) {
			setVenderAcimaDoLimite("S");
		} else
			setVenderAcimaDoLimite("N");
	}

	public String getVenderAcimaDoLimite() {
		return venderAcimaDoLimite;
	}

	public void setVenderAcimaDoLimite(String venderAcimaDoLimite) {
		this.venderAcimaDoLimite = venderAcimaDoLimite;
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
		// TODO Auto-generated method stub
		return "ordem";
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

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}

	public FichaTecnicaPrato getFichaTecnicaPrato() {
		return fichaTecnicaPrato;
	}

	public void setFichaTecnicaPrato(FichaTecnicaPrato fichaTecnicaPrato) {
		this.fichaTecnicaPrato = fichaTecnicaPrato;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricao() {
		try {
			return descricao.toUpperCase();
		} catch (Exception e) {
			return descricao;
		}
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public List<CardapioFichaPratoEmpresa> getFichaPratoEmpresa() {
		return fichaPratoEmpresa;
	}

	public void setFichaPratoEmpresa(List<CardapioFichaPratoEmpresa> fichaPratoEmpresa) {
		this.fichaPratoEmpresa = fichaPratoEmpresa;
	}

	public String getObservacao() {
		try {
			return observacao.toUpperCase();
		} catch (Exception e) {
			return observacao;
		}

	}

	public void setObservacao(String observacao) {
		this.observacao = observacao.toUpperCase();
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	@Override
	public int compareTo(CardapioFichaPrato o) {
		return this.getOrdem().compareTo(o.getOrdem());
	}

}