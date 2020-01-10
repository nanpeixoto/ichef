package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.ClienteTelefone;

@Stateless
public class ClienteTelefoneService extends AbstractService<ClienteTelefone> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(ClienteTelefone entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(ClienteTelefone entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(ClienteTelefone entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
