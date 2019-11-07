package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "email")
public class Email extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_EMAIL")
	private Long id;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "DS_ASSUNTO")
	private String assunto;

	@Column(name = "DS_LOG_EMAIL")
	private String log;

	@Column(name = "DS_MENSAGEM")
	private String mensagem;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ENVIO")
	private Date dataEnvio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INCLUSAO")
	private Date dataInclusao;

	@Column(name = "DS_IDENTIFICADOR")
	private String identificador;

	@Column(name = "DS_ORIGEM")
	private String origem;

	@Column(name = "SN_EMAIL_ENVIADO")
	private String emailEnviao;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getEmailEnviao() {
		return emailEnviao;
	}

	public void setEmailEnviao(String emailEnviao) {
		this.emailEnviao = emailEnviao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

}