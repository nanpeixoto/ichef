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
import javax.persistence.Transient;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
public class Entregador extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_ENTREGADOR")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
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

	@Transient
	private boolean isAtivo;

	@OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EntregadorArea> areas;

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
		Entregador other = (Entregador) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
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
		return "descricao";
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

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<EntregadorArea> getAreas() {
		return areas;
	}

	public void setAreas(List<EntregadorArea> areas) {
		this.areas = areas;
	}

}