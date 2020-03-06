package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.util.Util;

@Entity
@Table(name = "vw_cliente_saldo")
public class VwClienteSaldo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private VwClienteSaldoID id;

	@Column(name = "SALDO")
	private BigDecimal valorSaldo;

	@Column(name = "CD_LOCALIDADE")
	private Long codigoLocalidade;

	@Column(name = "CD_EMPRESA", insertable = false, updatable = false)
	private Long codigoEmpresa;

	@Column(name = "CD_TIP_LOCALIDADE")
	private Long codigoTipoLocalidade;

	@Column(name = "DS_LOCALIDADE")
	private String descricaoLocalidade;

	@Column(name = "NM_CLIENTE")
	private String nome;

	@Column(name = "DS_TIP_LOCALIDADE")
	private String descricaoTipoLocalidade;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Column(name = "DATA_CARTEIRA")
	private Date dataCarteira;

	// bi-directional many-to-one association to AreaLocalidade
	@OneToMany(mappedBy = "vwClienteSaldo")
	@Fetch(FetchMode.SELECT)
	private List<VwClienteCarteiraSaldo> saldos;

	@Column(name = "SALDO_OUTRA_EMPRESA")
	private BigDecimal valorSaldoOutraEmpresa;
	
	public long diasDevedor() {
		return Util.diferencaEmDias(getDataCarteira(), new Date());
	}

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public void setId(Object id) {
		this.id = (VwClienteSaldoID) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return "nome";
		// return null;
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public Date getDataEntrega() {
		return null;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<VwClienteCarteiraSaldo> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<VwClienteCarteiraSaldo> saldos) {
		this.saldos = saldos;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public Long getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Long codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public Long getCodigoTipoLocalidade() {
		return codigoTipoLocalidade;
	}

	public void setCodigoTipoLocalidade(Long codigoTipoLocalidade) {
		this.codigoTipoLocalidade = codigoTipoLocalidade;
	}

	public String getDescricaoLocalidade() {
		return descricaoLocalidade;
	}

	public void setDescricaoLocalidade(String descricaoLocalidade) {
		this.descricaoLocalidade = descricaoLocalidade;
	}

	public String getDescricaoTipoLocalidade() {
		return descricaoTipoLocalidade;
	}

	public void setDescricaoTipoLocalidade(String descricaoTipoLocalidade) {
		this.descricaoTipoLocalidade = descricaoTipoLocalidade;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Date getDataCarteira() {
		return dataCarteira;
	}

	public void setDataCarteira(Date dataCarteira) {
		this.dataCarteira = dataCarteira;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValorSaldoOutraEmpresa() {
		return valorSaldoOutraEmpresa;
	}

	public void setValorSaldoOutraEmpresa(BigDecimal valorSaldoOutraEmpresa) {
		this.valorSaldoOutraEmpresa = valorSaldoOutraEmpresa;
	}

	public void setId(VwClienteSaldoID id) {
		this.id = id;
	}

}
