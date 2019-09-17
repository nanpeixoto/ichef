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


/**
 * The persistent class for the ficha_tecnica_preparo_insumo database table.
 * 
 */
@Entity
@Table(name="ficha_tecnica_preparo_insumo")
public class FichaTecnicaPreparoInsumo extends BaseEntity   {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_FICHA_TEC_INSUMO")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_INSUMO")
	private Insumo insumo;

	@Column(name="NR_APROVEITAMENTO")
	private int nrAproveitamento;

	@Column(name="NR_CUSTO_BRUNO")
	private int nrCustoBruno;

	@Column(name="NR_CUSTO_TOTAL")
	private int nrCustoTotal;

	@Column(name="NR_QTD_BRUTA")
	private int nrQtdBruta;

	@Column(name="NR_QTD_LIQUIDA")
	private int nrQtdLiquida;

	@Column(name="SN_ATIVO")
	private String snAtivo;

	@ManyToOne
	@JoinColumn(name="CD_FICHA_TECNICA_PREPATO")
	private FichaTecnicaPreparo fichaTecnicaPreparo;

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

	public int getNrAproveitamento() {
		return nrAproveitamento;
	}

	public void setNrAproveitamento(int nrAproveitamento) {
		this.nrAproveitamento = nrAproveitamento;
	}

	public int getNrCustoBruno() {
		return nrCustoBruno;
	}

	public void setNrCustoBruno(int nrCustoBruno) {
		this.nrCustoBruno = nrCustoBruno;
	}

	public int getNrCustoTotal() {
		return nrCustoTotal;
	}

	public void setNrCustoTotal(int nrCustoTotal) {
		this.nrCustoTotal = nrCustoTotal;
	}

	public int getNrQtdBruta() {
		return nrQtdBruta;
	}

	public void setNrQtdBruta(int nrQtdBruta) {
		this.nrQtdBruta = nrQtdBruta;
	}

	public int getNrQtdLiquida() {
		return nrQtdLiquida;
	}

	public void setNrQtdLiquida(int nrQtdLiquida) {
		this.nrQtdLiquida = nrQtdLiquida;
	}

	public String getSnAtivo() {
		return snAtivo;
	}

	public void setSnAtivo(String snAtivo) {
		this.snAtivo = snAtivo;
	}

	public FichaTecnicaPreparo getFichaTecnicaPreparo() {
		return fichaTecnicaPreparo;
	}

	public void setFichaTecnicaPreparo(FichaTecnicaPreparo fichaTecnicaPreparo) {
		this.fichaTecnicaPreparo = fichaTecnicaPreparo;
	}
	
	

	

}