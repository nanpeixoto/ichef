package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cidade;

@Stateless
public class CidadeService extends AbstractService<Cidade> {
	private static final long serialVersionUID = 1L;

	public List<Cidade> listAll(Boolean ativo) {
		Cidade cidade = new Cidade();
		cidade.setAtivo("S");
		try {
			if (ativo)
				return super.findByParameters(cidade);
			else
				return super.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void validaCampos(Cidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Cidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Cidade entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	
	

}
