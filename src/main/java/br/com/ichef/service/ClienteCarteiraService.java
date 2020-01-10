package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.ClienteCarteira;

@Stateless
public class ClienteCarteiraService extends AbstractService<ClienteCarteira> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(ClienteCarteira entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(ClienteCarteira entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(ClienteCarteira entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
