package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;

/**
 * The persistent class for the cliente_email_auditoria database table.
 * 
 */
@Entity
@Table(name = "cliente_email_auditoria")
public class ClienteEmailAuditoria extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_AUDITORIA")
	private Long id;

	@Column(name = "CD_CLIENTE")
	private Long codigoCliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO")
	private Usuario usuario;

	@Column(name = "DATA_ENVIO")
	private Date dataEnvio;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "SALDO_TOTAL")
	private BigDecimal saldoTotal;

	@Column(name = "SALDO_EMPRESA")
	private BigDecimal saldoEmpresa;

	@Column(name = "CD_EMPRESA_ATUAL")
	private Long codigoEmpresaAtual;

	@Column(name = "DS_EMPRESA_ATUAL")
	private String descricaoEmpresa;

	@Column(name = "DS_ERRO")
	private String erro;

	@Lob
	@Column(name = "DS_MENSAGEM")
	private String mensagem;

	@Column(name = "SN_ENVIADO")
	private String enviado;

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
		return "dataEnvio desc";
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

	@Override
	public boolean isEdicao() {
		// TODO Auto-generated method stub
		return false;
	}

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getEnviado() {
		return enviado;
	}

	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public BigDecimal getSaldoEmpresa() {
		return saldoEmpresa;
	}

	public void setSaldoEmpresa(BigDecimal saldoEmpresa) {
		this.saldoEmpresa = saldoEmpresa;
	}

	public Long getCodigoEmpresaAtual() {
		return codigoEmpresaAtual;
	}

	public void setCodigoEmpresaAtual(Long codigoEmpresaAtual) {
		this.codigoEmpresaAtual = codigoEmpresaAtual;
	}

	public String getDescricaoEmpresa() {
		return descricaoEmpresa;
	}

	public void setDescricaoEmpresa(String descricaoEmpresa) {
		this.descricaoEmpresa = descricaoEmpresa;
	}
	
	public String cor() {
		if(getEnviado().equals("S"))
			return "green";
		else 
			return "red";
	}

}