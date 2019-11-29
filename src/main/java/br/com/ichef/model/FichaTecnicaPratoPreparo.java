package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

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

/**
 * The persistent class for the ficha_tecnica_prato_preparo database table.
 * 
 */
@Entity
@Table(name = "ficha_tecnica_prato_preparo")
public class FichaTecnicaPratoPreparo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FICHA_TEC_PRATO_PREPARO")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TECNICA_PREPARO")
	private FichaTecnicaPreparo fichaTecnicaPreparo;

	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TECNICA_PRATO")
	private FichaTecnicaPrato fichaTecnicaPrato;

	@Column(name = "NR_APROVEITAMENTO")
	private Long aproveitamento;

	//@Column(name = "NR_CUSTO_BRUTO")
	//private BigDecimal custoBruto;

	//@Column(name = "NR_CUSTO_TOTAL")
	//private BigDecimal custoTotal;

	@Column(name = "NR_QTD_BRUTA")
	private BigDecimal quantidadeBruta;

	@Column(name = "NR_QTD_LIQUIDA")
	private BigDecimal quantidadeLiquida;

	@Column(name = "SN_ATIVO")
	private String ativo;

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
		return "fichaTecnicaPrepato.descricao";
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDataCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public FichaTecnicaPreparo getFichaTecnicaPreparo() {
		return fichaTecnicaPreparo;
	}

	public void setFichaTecnicaPreparo(FichaTecnicaPreparo fichaTecnicaPreparo) {
		this.fichaTecnicaPreparo = fichaTecnicaPreparo;
	}

	public FichaTecnicaPrato getFichaTecnicaPrato() {
		return fichaTecnicaPrato;
	}

	public void setFichaTecnicaPrato(FichaTecnicaPrato fichaTecnicaPrato) {
		this.fichaTecnicaPrato = fichaTecnicaPrato;
	}

	public Long getAproveitamento() {
		return aproveitamento;
	}

	public void setAproveitamento(Long aproveitamento) {
		this.aproveitamento = aproveitamento;
	}

	

	public BigDecimal getCustoTotal() {
		//return custoTotal;
		return getFichaTecnicaPreparo().getPrecoCustoPorcao();
	}

	//public void setCustoTotal(BigDecimal custoTotal) {
	//	this.custoTotal = custoTotal;
	//}

	public BigDecimal getQuantidadeBruta() {
		return quantidadeBruta;
	}
	
	public String getQuantidadeBrutaFormatada() {
		Locale meuLocal = new Locale("pt", "BR");
		return  String.format(meuLocal, "%.2f", getQuantidadeBruta() );
	}

	public void setQuantidadeBruta(BigDecimal quantidadeBruta) {
		this.quantidadeBruta = quantidadeBruta;
	}

	public BigDecimal getQuantidadeLiquida() {
		return quantidadeLiquida;
	}

	public void setQuantidadeLiquida(BigDecimal quantidadeLiquida) {
		this.quantidadeLiquida = quantidadeLiquida;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

}