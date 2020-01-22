package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class PedidoVisitor extends FilterVisitor {

	private Date data;

	private Date dataCardapio;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;

	private Date dataEntrega;

	private Date dataEntregaInicial;
	private Date dataEntregaFinal;
	
	private Long codigoCliente;
	
	private Long codigoEmpresa;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getData() != null && getDataCardapio() == null)
			criteria.add(Restrictions.or(
					Restrictions.sqlRestriction(
							" date_format( dt_entrega, '%d/%m/%Y' ) ='" + Util.dateToString(getDataEntrega()) + "'"),
					Restrictions.sqlRestriction(
							" date_format( this_.dt_cadastro, '%d/%m/%Y' ) ='" + Util.dateToString(getData()) + "'")));

		if (getDataCardapio() != null && getData() != null)
			criteria.add(Restrictions.or(
					Restrictions.sqlRestriction(
							" date_format( dt_entrega, '%d/%m/%Y' ) ='" + Util.dateToString(getDataEntrega()) + "'"),
					Restrictions.sqlRestriction(" date_format(cardapio1_.data, '%d/%m/%Y' ) ='"
							+ Util.dateToString(getDataCardapio()) + "'"),
					Restrictions.sqlRestriction(
							" date_format(  this_.dt_cadastro, '%d/%m/%Y' ) ='" + Util.dateToString(getData()) + "'")));

		if (getDataCardapio() != null && getData() == null)
			criteria.add(Restrictions.sqlRestriction(
					" date_format(cardapio1_.data, '%d/%m/%Y' ) ='" + Util.dateToString(getDataCardapio()) + "'"));

		if (getDataInicial() != null && getDataFinal() != null)
			criteria.add(Restrictions.sqlRestriction(" cardapio1_.data between STR_TO_DATE( '"
					+ Util.dateToString(getDataInicial()) + "', '%d/%m/%Y' ) and  STR_TO_DATE('"
					+ Util.dateToString(getDataFinal()) + "', '%d/%m/%Y' ) "));

		if (getDataEntrega() != null)
			criteria.add(Restrictions
					.sqlRestriction(" date_format( dt_entrega, '%d/%m/%Y' ) ='" + Util.dateToString(getDataEntrega()) + "'"));

		if (getDataEntregaInicial() != null && getDataEntregaFinal() != null)
			criteria.add(Restrictions.sqlRestriction(" dt_entrega between STR_TO_DATE( '"
					+ Util.dateToString(getDataEntregaInicial()) + "', '%d/%m/%Y' ) and  STR_TO_DATE('"
					+ Util.dateToString(getDataEntregaFinal()) + "', '%d/%m/%Y' ) "));
		
		if(getCodigoCliente()!=null) {
			criteria.add(Restrictions.eq("cliente.id", getCodigoCliente()));
		}
		
		if(getCodigoEmpresa()!=null) {
			criteria.add(Restrictions.eq("id.codigoEmpresa", getCodigoEmpresa()));
		}

	}
	
	

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}



	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}



	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataCardapio() {
		return dataCardapio;
	}

	public void setDataCardapio(Date dataCardapio) {
		this.dataCardapio = dataCardapio;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Date getDataEntregaFinal() {
		return dataEntregaFinal;
	}

	public void setDataEntregaFinal(Date dataEntregaFinal) {
		this.dataEntregaFinal = dataEntregaFinal;
	}

	public Date getDataEntregaInicial() {
		return dataEntregaInicial;
	}

	public void setDataEntregaInicial(Date dataEntregaInicial) {
		this.dataEntregaInicial = dataEntregaInicial;
	}

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

}
