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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_USUARIO")
	private Long id;

	@NotEmpty
	@Column(name = "NM_USUARIO")
	private String nome;

	@Column(name = "DS_EMAIL")
	private String email;

	@NotEmpty
	@Column(name = "SN_ATIVO")
	private String ativo;

	@NotEmpty
	@Column(name = "DT_CADASTRO")
	private Date dataCadastrado;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@NotEmpty
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlterado;

	@NotEmpty
	@Column(name = "DS_LOGIN")
	private String login;

	@NotEmpty
	@Column(name = "NM_USUARIO_ABREVIADO")
	private String nomeAbreviado;

	@NotEmpty
	@Column(name = "DS_SENHA")
	private String senha;

	@OneToMany(mappedBy = "usuario",  cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UsuarioEmpresa> usuarioEmpresas;
	
	@Transient
	private Empresa empresaLogada;

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		// TODO Auto-generated method stub
		return getUsuarioAlteracao();
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
		return dataCadastrado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Date getDataCadastrado() {
		return dataCadastrado;
	}

	public void setDataCadastrado(Date dataCadastrado) {
		this.dataCadastrado = dataCadastrado;
	}

	public Usuario getUsuarioAlterado() {
		return usuarioAlterado;
	}

	public void setUsuarioAlterado(Usuario usuarioAlterado) {
		this.usuarioAlterado = usuarioAlterado;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNomeAbreviado() {
		return nomeAbreviado;
	}

	public void setNomeAbreviado(String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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

	public Empresa getEmpresaLogada() {
		return empresaLogada;
	}

	public void setEmpresaLogada(Empresa empresaLogada) {
		this.empresaLogada = empresaLogada;
	}
	
	

}
