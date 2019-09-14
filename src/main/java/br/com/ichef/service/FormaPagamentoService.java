package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.FormaPagamento;

public class FormaPagamentoService extends GenericDAO<FormaPagamento> {
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
	
	

}
