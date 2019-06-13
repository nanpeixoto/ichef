package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "empresa")
public class Empresa extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_EMPRESA")
	private Long id;

	@Column(name = "DS_RAZAO_SOCIAL")
	private String razaoSocal;

	@Column(name = "NR_CNPJ")
	private String CNPJ;

	@Column(name = "VL_CUSTO_MAXIMO")
	private BigDecimal valorCustoMaximo;

	@Column(name = "SN_LIMITA_FICHA_TEC_CUSTO")
	private String limitaFichaTécnicaPeloCusto;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@OneToMany(mappedBy = "empresa")
	private List<UsuarioEmpresa> usuarioEmpresas;

	@javax.persistence.Transient
	private boolean limitaCustoFicha;
	
	public boolean isLimitaCustoFicha() {
		if (limitaFichaTécnicaPeloCusto != null) {
			if (limitaFichaTécnicaPeloCusto.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return limitaCustoFicha;
	}

	public void setLimitaCustoFicha(boolean limitaCustoFicha) {
		this.limitaCustoFicha = limitaCustoFicha;
		if (limitaCustoFicha == Boolean.TRUE) {
			setLimitaFichaTécnicaPeloCusto("S");
		} else
			setLimitaFichaTécnicaPeloCusto("N");
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		return null;
	}

	@Override
	public String getAuditoria() {
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

	public String getRazaoSocal() {
		return razaoSocal;
	}

	public void setRazaoSocal(String razaoSocal) {
		this.razaoSocal = razaoSocal.toUpperCase();
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}

	public BigDecimal getValorCustoMaximo() {
		return valorCustoMaximo;
	}

	public void setValorCustoMaximo(BigDecimal valorCustoMaximo) {
		this.valorCustoMaximo = valorCustoMaximo;
	}

	public String getLimitaFichaTécnicaPeloCusto() {
		return limitaFichaTécnicaPeloCusto;
	}

	public void setLimitaFichaTécnicaPeloCusto(String limitaFichaTécnicaPeloCusto) {
		this.limitaFichaTécnicaPeloCusto = limitaFichaTécnicaPeloCusto;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public List<UsuarioEmpresa> getUsuarioEmpresas() {
		return usuarioEmpresas;
	}

	public void setUsuarioEmpresas(List<UsuarioEmpresa> usuarioEmpresas) {
		this.usuarioEmpresas = usuarioEmpresas;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}



	

}
