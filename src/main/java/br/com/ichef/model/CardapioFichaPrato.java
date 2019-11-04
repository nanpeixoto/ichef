package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "cardapio_ficha_prato")
public class CardapioFichaPrato extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CARDAPIO_PRATO")
	private Long id;

	@Column(name = "NR_QTD")
	private Integer quantidade;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

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
		return null;
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

}