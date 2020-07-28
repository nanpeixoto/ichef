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
@Table(name = "cliente")
public class Cliente extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CLIENTE")
	private Long id;

	@Column(name = "NM_CLIENTE")
	private String nome;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "SN_MALA_DIRETA")
	private String malaDireta;

	@Column(name = "SN_CARTEIRA")
	private String carteira;

	@Column(name = "SN_BLOQUEADO")
	private String bloqueado;

	@Column(name = "SN_SMS")
	private String sms;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "SN_EXIBIR_SALDO")
	private String exibirSaldo;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_CIDADE")
	private Cidade cidade;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ClienteTelefone> telefones;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<ClienteEndereco> enderecos;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT) 
	//@Transient
	private List<ClienteCarteira> carteiras;

	@Column(name = "DS_APELIDO")
	private String apelido;

	@Column(name = "DS_TELEFONE")
	private String descricaoTelefone;

	@Transient
	private boolean isAtivo;

	@Transient
	private boolean recebeSMS;

	@Transient
	private boolean exibeSaldo;

	@Transient
	private boolean estaBloqueado;

	@Transient
	private boolean recebeMaladireta;

	@Transient
	private boolean pagaEmCarteira;

	public boolean isDesabilitado() {
		if (isEstaBloqueado() || !isAtivo())
			return true;
		else
			return false;
	}

	public String getStyleDesabilitado() {
		if (isDesabilitado()) {
			return "opacity: .35; cursor: default!important;";
		}
		return "";
	}

	public boolean isExibeSaldo() {
		if (exibirSaldo != null) {
			if (exibirSaldo.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return isAtivo;
	}

	public void setExibeSaldo(boolean exibeSaldo) {
		this.exibeSaldo = exibeSaldo;
		if (exibeSaldo == Boolean.TRUE) {
			setExibirSaldo("S");
		} else
			setExibirSaldo("N");
	}

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

	public boolean isRecebeSMS() {
		if (sms != null) {
			if (sms.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return recebeSMS;
	}

	public void setRecebeSMS(boolean recebeSMS) {
		this.recebeSMS = recebeSMS;
		if (recebeSMS == Boolean.TRUE) {
			setSms("S");
		} else
			setSms("N");
	}

	public boolean isEstaBloqueado() {
		if (bloqueado != null) {
			if (bloqueado.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return estaBloqueado;
	}

	public void setEstaBloqueado(boolean estaBloqueado) {
		this.estaBloqueado = estaBloqueado;
		if (estaBloqueado == Boolean.TRUE) {
			setBloqueado("S");
		} else
			setBloqueado("N");
	}

	public boolean isRecebeMaladireta() {
		if (malaDireta != null) {
			if (malaDireta.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return recebeMaladireta;
	}

	public void setRecebeMaladireta(boolean recebeMaladireta) {
		this.recebeMaladireta = recebeMaladireta;
		if (recebeMaladireta == Boolean.TRUE) {
			setMalaDireta("S");
		} else
			setMalaDireta("N");
	}

	public boolean isPagaEmCarteira() {
		if (carteira != null) {
			if (carteira.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return pagaEmCarteira;
	}

	public void setPagaEmCarteira(boolean pagaEmCarteira) {
		this.pagaEmCarteira = pagaEmCarteira;
		if (pagaEmCarteira == Boolean.TRUE) {
			setCarteira("S");
		} else
			setCarteira("N");
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
		Cliente other = (Cliente) obj;
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

	public String getNome() {
		try {
			return nome;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setNome(String nome) {
		try {

			if (nome != null)
				this.nome = nome.toUpperCase();
			else
				this.nome = nome;
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toUpperCase();
	}

	public String getMalaDireta() {
		return malaDireta;
	}

	public void setMalaDireta(String malaDireta) {
		this.malaDireta = malaDireta;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public String getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<ClienteTelefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<ClienteTelefone> telefones) {
		this.telefones = telefones;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido.toUpperCase();
	}

	public List<ClienteEndereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<ClienteEndereco> enderecos) {
		this.enderecos = enderecos;
	}

	public String getAllTelefones() {
		String listaTelefones = "";
		if (telefones != null) {
			for (ClienteTelefone tel : telefones) {
				if (listaTelefones != null && !listaTelefones.equalsIgnoreCase(""))
					listaTelefones = listaTelefones + " / ";
				listaTelefones = listaTelefones + tel.getTelefone().replace("-", "").replace("(", "").replace(")", "")
						.replace("+", "").replace(" ", "").trim();
			}
		}

		return listaTelefones;
	}

	public String getDescricaoTelefone() {
		return descricaoTelefone;
	}

	public void setDescricaoTelefone(String descricaoTelefone) {
		this.descricaoTelefone = descricaoTelefone;
	}

	public List<ClienteCarteira> getCarteiras() {
		return carteiras;
	}

	public void setCarteiras(List<ClienteCarteira> carteiras) {
		this.carteiras = carteiras;
	}

	public String getNomeCompleto() {
		String telefone = getDescricaoTelefone() == null ? "" : " - " + getDescricaoTelefone();
		return "C" + getId() + " - " + getNome() + telefone;
	}

	public String getNomeCompletoSituacao() {
		return getNomeCompleto() + getSituacaoAtual();
	}

	public String getSituacaoAtual() {
		String situacao = "";
		if (isEstaBloqueado()) {
			situacao = "BLOQUEADO";
		}

		if (!isAtivo()) {
			if (!situacao.equalsIgnoreCase(""))
				situacao += "/";
			situacao += "INATIVO";
		}

		return situacao;
	}

	public BigDecimal getTotalDevido() {
		BigDecimal valorDevido = new BigDecimal(0);
		try {
			if (carteiras != null) {
				for (ClienteCarteira clienteCarteira : carteiras) {
					if (clienteCarteira.getValorDevido() != null)
						valorDevido = valorDevido.add(clienteCarteira.getValorDevido());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valorDevido;
	}

	public BigDecimal getTotalPago() {
		BigDecimal valor = new BigDecimal(0);
		try {
			if (carteiras != null) {
				for (ClienteCarteira clienteCarteira : carteiras) {
					if (clienteCarteira.getValorPago() != null)
						valor = valor.add(clienteCarteira.getValorPago());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valor;
	}

	public BigDecimal getTotal() {
		BigDecimal valor = new BigDecimal(0);
		try {
			valor = getTotalDevido().subtract(getTotalPago());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valor;
	}

	public String getExibirSaldo() {
		return exibirSaldo;
	}

	public void setExibirSaldo(String exibirSaldo) {
		this.exibirSaldo = exibirSaldo;
	}

	public String getNomeComTelefonePrincipal() {
		String telPrincipal = getTelefonePrincipal();
		if (telPrincipal != null) {
			return getNome() + "-" + getTelefonePrincipal();
		}
		return getNome();
	}

	public String getTelefonePrincipal() {
		for (ClienteTelefone tel : telefones) {
			if (tel.isTelefonePrincipal())
				return tel.getTelefone();
		}

		return null;

	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao.toUpperCase();
	}

}
