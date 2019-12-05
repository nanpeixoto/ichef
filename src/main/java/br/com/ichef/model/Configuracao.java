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

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "configuracao")
public class Configuracao extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CONFIGURACAO")
	private Integer id;

	@Column(name = "HOST_SMTP")
	private String hostSMTP;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Column(name = "NR_CMV")
	private Integer custoMercadoriaVendida;

	@ManyToOne
	@JoinColumn(name = "CD_TIPO_PRATO_CARRO_CHEF")
	private TipoPrato tipoPrato;

	@ManyToOne
	@JoinColumn(name = "CD_FORMA_PAG_CARTEIRA")
	private FormaPagamento formaPagamento;

	@ManyToOne
	@JoinColumn(name = "CD_DERIVACAO_PADRAO")
	private Derivacao derivacao;

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Integer) id;

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

	public String getHostSMTP() {
		return hostSMTP;
	}

	public void setHostSMTP(String hostSMTP) {
		this.hostSMTP = hostSMTP;
	}

	public Integer getCustoMercadoriaVendida() {
		return custoMercadoriaVendida;
	}

	public void setCustoMercadoriaVendida(Integer custoMercadoriaVendida) {
		this.custoMercadoriaVendida = custoMercadoriaVendida;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public TipoPrato getTipoPrato() {
		return tipoPrato;
	}

	public void setTipoPrato(TipoPrato tipoPrato) {
		this.tipoPrato = tipoPrato;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Derivacao getDerivacao() {
		return derivacao;
	}

	public void setDerivacao(Derivacao derivacao) {
		this.derivacao = derivacao;
	}

}
