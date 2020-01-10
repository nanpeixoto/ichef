package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.VwUltimosClientes;

@Stateless
public class VwUltimosClientesService extends AbstractService<VwUltimosClientes> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(VwUltimosClientes entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(VwUltimosClientes entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(VwUltimosClientes entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
