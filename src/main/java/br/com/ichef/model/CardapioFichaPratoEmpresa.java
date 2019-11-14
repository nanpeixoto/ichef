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
import javax.persistence.Transient;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "cardapio_ficha_prato_empresa")
public class CardapioFichaPratoEmpresa extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CARDAPIO_PRATO_EMPRESA")
	private Long id;

	@Column(name = "NR_QUANTIDADE")
	private Integer quantidade;

	@ManyToOne
	@JoinColumn(name = "CD_CARDAPIO_PRATO")
	private CardapioFichaPrato cardapioFichaPrato;

	@ManyToOne
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

	@Column(name = "SN_VENDER_ACIMA_LIMITE")
	private String venderAcimaDoLimite;

	@Transient
	private boolean podeVenderAcimaDoLimite;

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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public CardapioFichaPrato getCardapioFichaPrato() {
		return cardapioFichaPrato;
	}

	public void setCardapioFichaPrato(CardapioFichaPrato cardapioFichaPrato) {
		this.cardapioFichaPrato = cardapioFichaPrato;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public String getSituacao() {
		if (getVenderAcimaDoLimite().equals("S"))
			return "Não Limitado".toUpperCase();
		return "Limitado".toUpperCase();
	}

	

}