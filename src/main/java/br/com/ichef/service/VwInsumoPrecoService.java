package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.VwInsumoPreco;

@Stateless
public class VwInsumoPrecoService extends AbstractService<VwInsumoPreco> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(VwInsumoPreco entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(VwInsumoPreco entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(VwInsumoPreco entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	 

}
