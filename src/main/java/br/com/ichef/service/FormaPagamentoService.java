package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.FormaPagamento;

public class FormaPagamentoService extends AbstractService<FormaPagamento> {
	private static final long serialVersionUID = 1L;

	public List<FormaPagamento> listAll(Boolean ativo) {
		FormaPagamento filtro = new FormaPagamento();
		filtro.setAtivo("S");
		try {
			if (ativo)
				return super.findByParameters(filtro);
			else
				return super.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void validaCampos(FormaPagamento entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(FormaPagamento entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(FormaPagamento entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	
	

}
