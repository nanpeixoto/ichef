package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_ultimos_clientes")
public class VwUltimosClientes extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "CD_CIDADE")
	private int cdCidade;

	@Id
	@Column(name = "CD_CLIENTE")
	private Long id;

	@Column(name = "DS_APELIDO")
	private String dsApelido;

	@Column(name = "DS_EMAIL")
	private String dsEmail;

	@Column(name = "DS_TELEFONE")
	private String dsTelefone;

	@Column(name = "NM_CLIENTE")
	private String nmCliente;

	@Column(name = "SN_ATIVO")
	private String snAtivo;

	@Column(name = "SN_BLOQUEADO")
	private String snBloqueado;

	@Column(name = "SN_CARTEIRA")
	private String snCarteira;

	@Column(name = "SN_EXIBIR_SALDO")
	private String snExibirSaldo;

	@Column(name = "SN_MALA_DIRETA")
	private String snMalaDireta;

	@Column(name = "SN_SMS")
	private String snSms;

	public int getCdCidade() {
		return cdCidade;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public String getDsApelido() {
		return dsApelido;
	}

	public void setDsApelido(String dsApelido) {
		this.dsApelido = dsApelido;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public String getDsTelefone() {
		return dsTelefone;
	}

	public void setDsTelefone(String dsTelefone) {
		this.dsTelefone = dsTelefone;
	}

	

	public String getNmCliente() {
		return nmCliente;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public String getSnAtivo() {
		return snAtivo;
	}

	public void setSnAtivo(String snAtivo) {
		this.snAtivo = snAtivo;
	}

	public String getSnBloqueado() {
		return snBloqueado;
	}

	public void setSnBloqueado(String snBloqueado) {
		this.snBloqueado = snBloqueado;
	}

	public String getSnCarteira() {
		return snCarteira;
	}

	public void setSnCarteira(String snCarteira) {
		this.snCarteira = snCarteira;
	}

	public String getSnExibirSaldo() {
		return snExibirSaldo;
	}

	public void setSnExibirSaldo(String snExibirSaldo) {
		this.snExibirSaldo = snExibirSaldo;
	}

	public String getSnMalaDireta() {
		return snMalaDireta;
	}

	public void setSnMalaDireta(String snMalaDireta) {
		this.snMalaDireta = snMalaDireta;
	}

	public String getSnSms() {
		return snSms;
	}

	public void setSnSms(String snSms) {
		this.snSms = snSms;
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
		return null;
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		return null;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		return null;
	}

	@Override
	public Date getDataCadastro() {
		return null;
	}

	@Override
	public boolean isEdicao() {
		return false;
	}

}