package br.com.ichef.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.VwResumoDiarioPedidos;
import br.com.ichef.util.Util;

public class VwResumoDiarioPedidosService extends GenericDAO<VwResumoDiarioPedidos> {
	private static final long serialVersionUID = 1L;
	
 
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<VwResumoDiarioPedidos> findByParameters(Date dataInicio, Date dataFim, Long codigoEmpresa) throws Exception {
		
		Criteria criteria =  getSession().createCriteria( VwResumoDiarioPedidos.class );
		
		criteria.add(Restrictions.sqlRestriction("data between STR_TO_DATE( '"
				+ Util.dateToString( dataInicio) + "', '%d/%m/%Y' ) and  STR_TO_DATE('"
				+ Util.dateToString(dataFim) + "', '%d/%m/%Y' ) "));
		if(codigoEmpresa!=null )
			criteria.add(Restrictions.eq("id.codigoEmpresa", codigoEmpresa));
		
		 criteria.addOrder( Order.asc("id.codigoEmpresa")  );
		criteria.addOrder( Order.asc("id.data")  );
		criteria.addOrder( Order.asc("ordem")  ); 
		criteria.addOrder( Order.asc("descricaoCardapioFicha")  ); 
		criteria.addOrder( Order.asc("descricaoFormaPagamento")  ); 
		
		return criteria.list();
	}

}
