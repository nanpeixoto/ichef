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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "cliente_endereco")
public class ClienteEndereco extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CLIENTE_ENDERECO")
	private Long id;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_LOCALIDADE")
	private Localidade localidade;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DS_ENDERECO")
	private String endereco;

	@Column(name = "SN_PRINCIPAL")
	private String principal;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CLIENTE")
	private Cliente cliente;

	@Transient
	private boolean endPrincipal;
	
	public String getEnderecoCompleto() {
		String endereco = getEndereco()==null?"":getEndereco();
		
		return endereco + " - "+getLocalidade().getDescricao();
	}

	public boolean isEndPrincipal() {
		if (principal != null) {
			if (principal.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return endPrincipal;
	}

	public void setEndPrincipal(boolean isAtivo) {
		this.endPrincipal = isAtivo;
		if (isAtivo == Boolean.TRUE) {
			setPrincipal("S");
			;
		} else
			setPrincipal("N");
		;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public String getEndereco() {
		try {
			return endereco.toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		try {

			if (endereco != null)
				this.endereco = endereco.toUpperCase();
			else
				this.endereco = endereco;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
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
		return "id";
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

}