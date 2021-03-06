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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "entregador")
public class Entregador extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_ENTREGADOR")
	private Long id;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DT_INICIO")
	private Date dataInicio;

	@Column(name = "NM_ENTREGADOR")
	private String nome;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "VL_DIARIA")
	private BigDecimal valorDiaria;
	
	@Column(name = "NR_QTD_QUENTINHAS")
	private Integer quantiadadeQuentinha;

	@Transient
	private boolean isAtivo;

	@OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<EntregadorLocalidade> localidades;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_EMPRESA")
	private Empresa empresa;

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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	@Override
	public String getColumnOrderBy() {
		return "nome";
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
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		// TODO Auto-generated method stub
		return usuarioAlteracao;
	}

	@Override
	public Date getDataAlteracao() {
		// TODO Auto-generated method stub
		return dataAlteracao;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		// TODO Auto-generated method stub
		return usuarioCadastro;
	}

	@Override
	public Date getDataCadastro() {
		// TODO Auto-generated method stub
		return dataCadastro;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getNome() {
		if (nome == null)
			return nome;
		return nome.toUpperCase();
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(BigDecimal valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	public List<EntregadorLocalidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<EntregadorLocalidade> localidades) {
		this.localidades = localidades;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public Integer getQuantiadadeQuentinha() {
		return quantiadadeQuentinha;
	}

	public void setQuantiadadeQuentinha(Integer quantiadadeQuentinha) {
		this.quantiadadeQuentinha = quantiadadeQuentinha;
	}
	
	

}