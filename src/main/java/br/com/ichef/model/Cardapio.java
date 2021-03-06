package br.com.ichef.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import br.com.ichef.util.Util;

@Entity
@Table(name = "cardapio")
public class Cardapio extends BaseEntity implements Comparable<Cardapio> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_CARDAPIO")
	private Long id;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "CD_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@Column(name = "DATA")
	private Date data;

	@Column(name = "SN_ATIVO")
	private String ativo;

	@Column(name = "DS_OBSERVACAO")
	private String observacao;

	@Column(name = "DATA_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Transient
	private boolean isAtivo;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<CardapioFichaPrato> pratos;

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public List<CardapioFichaPrato> getPratos() {
		return pratos;
	}

	public void setPratos(List<CardapioFichaPrato> pratos) {
		this.pratos = pratos;
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
		// TODO Auto-generated method stub
		return "data";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
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

	public String getObservacao() {
		try {
			return observacao.toUpperCase();
		} catch (Exception e) {
			return observacao;
		}

	}

	public void setObservacao(String observacao) {
		this.observacao = observacao.toUpperCase();
	}

	public String getSituacao() {
		if (getAtivo().equals("S"))
			return "Ativo".toUpperCase();
		return "Inativo".toUpperCase();
	}

	public String getDescricaoComDiaSemana() {
		return getDataFormatada() + " - " + getDiaSemana();
	}

	public String getDataFormatada() {
		try {
			Locale meuLocal = new Locale("pt", "BR");
			SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy", meuLocal);
			return sdate.format(getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDiaSemana() {
		try {

			return Util.getDiaSemana(getData()).toUpperCase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";
	}
	
	@Override
	public int compareTo(Cardapio o) {
		return this.getData().compareTo(o.getData());
	}


}