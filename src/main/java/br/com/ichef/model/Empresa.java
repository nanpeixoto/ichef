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

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "empresa")
public class Empresa extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_EMPRESA")
	private Long id;

	@Column(name = "DS_RAZAO_SOCIAL")
	private String razaoSocal;

	@Column(name = "NR_CNPJ")
	private String CNPJ;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "NR_TELEFONE")
	private String telefone;

	@Column(name = "VL_CUSTO_MAXIMO")
	private BigDecimal valorCustoMaximo;

	@Column(name = "SN_LIMITA_FICHA_TEC_CUSTO")
	private String limitaFichaTecnicaPeloCusto;

	@Column(name = "DT_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@OneToMany(mappedBy = "empresa",  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UsuarioEmpresa> usuarioEmpresas;

	@ManyToOne
	@JoinColumn(name = "CD_CIDADE")
	private Cidade cidade;

	@Column(name = "DS_ENDERECO")
	private String endereco;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@ManyToOne
	@JoinColumn(name = "CD_LOCALIDADE")
	private Localidade localidade;

	@javax.persistence.Transient
	private boolean limitaCustoFicha;

	@Transient
	private boolean isAtivo;

	@Column(name = "SN_ATIVO")
	private String ativo;

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

	public boolean isLimitaCustoFicha() {
		if (limitaFichaTecnicaPeloCusto != null) {
			if (limitaFichaTecnicaPeloCusto.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return limitaCustoFicha;
	}

	public void setLimitaCustoFicha(boolean limitaCustoFicha) {
		this.limitaCustoFicha = limitaCustoFicha;
		if (limitaCustoFicha == Boolean.TRUE) {
			setLimitaFichaTecnicaPeloCusto("S");
		} else
			setLimitaFichaTecnicaPeloCusto("N");
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "nomeFantasia";
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

	public String getRazaoSocal() {
		return razaoSocal;
	}

	public void setRazaoSocal(String razaoSocal) {
		this.razaoSocal = razaoSocal.toUpperCase();
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}

	public BigDecimal getValorCustoMaximo() {
		return valorCustoMaximo;
	}

	public void setValorCustoMaximo(BigDecimal valorCustoMaximo) {
		this.valorCustoMaximo = valorCustoMaximo;
	}

	public String getLimitaFichaTecnicaPeloCusto() {
		return limitaFichaTecnicaPeloCusto;
	}

	public void setLimitaFichaTecnicaPeloCusto(String limitaFichaTecnicaPeloCusto) {
		this.limitaFichaTecnicaPeloCusto = limitaFichaTecnicaPeloCusto;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
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

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco.toUpperCase();
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo.toUpperCase();
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia.toUpperCase();
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getNomeFantasiaAbreviado() {
		try {
			return getNomeFantasia().replace("COZINHA DE CHEF -", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getNomeFantasia();
	}
	

}
