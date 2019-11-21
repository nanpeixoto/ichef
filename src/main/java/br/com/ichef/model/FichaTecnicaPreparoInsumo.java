package br.com.ichef.model;

import java.math.BigDecimal;
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

/**
 * The persistent class for the ficha_tecnica_preparo_insumo database table.
 * 
 */
@Entity
@Table(name = "ficha_tecnica_preparo_insumo")
public class FichaTecnicaPreparoInsumo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FICHA_TEC_INSUMO")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_INSUMO")
	private Insumo insumo;

	@Column(name = "NR_APROVEITAMENTO")
	private Long aproveitamento;

	@Column(name = "NR_CUSTO_BRUTO")
	private BigDecimal custoBruto;

	@Column(name = "NR_CUSTO_TOTAL")
	private BigDecimal custoTotal;

	@Column(name = "NR_QTD_BRUTA")
	private BigDecimal quantidadeBruta;

	@Column(name = "NR_QTD_LIQUIDA")
	private BigDecimal quantidadeLiquida;
	
	@Column(name = "NR_QTD_LIQUIDA_INFORMADA")
	private BigDecimal quantidadeLiquidaInformada;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TECNICA_PREPATO")
	private FichaTecnicaPreparo fichaTecnicaPreparo;

	@ManyToOne
	@JoinColumn(name = "CD_FICHA_TEC_PERPARO_REFERENCIA")
	private FichaTecnicaPreparo fichaTecnicaPreparoReferencia;

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
		return "insumo.descricao";
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		// TODO Auto-generated method stub
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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public Insumo getInsumo() {
		return insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public FichaTecnicaPreparo getFichaTecnicaPreparo() {
		return fichaTecnicaPreparo;
	}

	public void setFichaTecnicaPreparo(FichaTecnicaPreparo fichaTecnicaPreparo) {
		this.fichaTecnicaPreparo = fichaTecnicaPreparo;
	}

	public Long getAproveitamento() {
		return aproveitamento;
	}

	public void setAproveitamento(Long aproveitamento) {
		this.aproveitamento = aproveitamento;
	}

	public BigDecimal getCustoBruto() {
		return custoBruto;
	}

	public void setCustoBruto(BigDecimal custoBruto) {
		this.custoBruto = custoBruto;
	}

	public BigDecimal getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(BigDecimal custoTotal) {
		this.custoTotal = custoTotal;
	}

	public BigDecimal getQuantidadeBruta() {
		return quantidadeBruta;
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

	public FichaTecnicaPreparo getFichaTecnicaPreparoReferencia() {
		return fichaTecnicaPreparoReferencia;
	}

	public void setFichaTecnicaPreparoReferencia(FichaTecnicaPreparo fichaTecnicaPreparoReferencia) {
		this.fichaTecnicaPreparoReferencia = fichaTecnicaPreparoReferencia;
	}

	public BigDecimal getQuantidadeLiquidaInformada() {
		return quantidadeLiquidaInformada;
	}

	public void setQuantidadeLiquidaInformada(BigDecimal quantidadeLiquidaInformada) {
		this.quantidadeLiquidaInformada = quantidadeLiquidaInformada;
	}
	
	

}