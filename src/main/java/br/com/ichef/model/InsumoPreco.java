package br.com.ichef.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "insumo_preco")
public class InsumoPreco extends BaseEntity implements Comparable<InsumoPreco> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_INSUMO_PRECO")
	private Long id;;

	@Column(name = "VL_PRECO")
	private Double preco;

	@Column(name = "DT_VIGENCIA")
	private Date dataVigencia;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@ManyToOne
	@BatchSize(size = 100)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@BatchSize(size = 100)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@BatchSize(size = 100)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_INSUMO")
	private Insumo insumo;

	@ManyToOne
	@BatchSize(size = 100)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

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
		return "dataVigencia";
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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	@Override
	public boolean isEdicao() {
		return !isInclusao();
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Date getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public Insumo getInsumo() {
		return insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public String getPrecoFormatado() {
		if (getPreco() != null) {
			return "R$ " + getPreco().toString().replaceAll(",", ".").replace(".", ",");
		}
		return getPreco().toString();
	}

	public String getDataVigenciaFormatada() {
		try {
			SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
			return sdate.format(getDataVigencia());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int compareTo(InsumoPreco o) {
		return this.getDataVigencia().compareTo(o.getDataVigencia());
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}