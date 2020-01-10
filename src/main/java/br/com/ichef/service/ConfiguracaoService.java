package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Configuracao;

@Stateless
public class ConfiguracaoService extends AbstractService<Configuracao> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Configuracao entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Configuracao entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Configuracao entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

	

}
