package br.com.ichef.arquitetura;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2426297005110989046L;

	public abstract Object getId();

	public abstract void setId(Object id);

	public abstract String getColumnOrderBy();

	public abstract String getAuditoria();

	public abstract Date getDataAlteracao();

	public abstract Date getDataCadastro();

	public abstract boolean isEdicao();

}
