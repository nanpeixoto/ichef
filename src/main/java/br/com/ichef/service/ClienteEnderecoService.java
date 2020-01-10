package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.ClienteEndereco;

@Stateless
public class ClienteEnderecoService extends AbstractService<ClienteEndereco> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(ClienteEndereco entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(ClienteEndereco entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(ClienteEndereco entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
