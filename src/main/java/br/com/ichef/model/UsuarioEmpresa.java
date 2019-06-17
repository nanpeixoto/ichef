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




/**
 * The persistent class for the usuario_empresa database table.
 * 
 */
@Entity
@Table(name="usuario_empresa")
public class UsuarioEmpresa extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CD_USUARIO_EMPRESA")
	private Long id;

	//bi-directional many-to-one association to Empresa
	@ManyToOne
	@JoinColumn(name="CD_EMPRESA")
	private Empresa empresa;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="CD_USUARIO")
	private Usuario usuario;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id  = (Long) id;
		
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
	public  Usuario getUsuarioAlteracao() {
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
		// TODO Auto-generated method stub
		return false;
	}

	

}