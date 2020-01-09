package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.EntregadorLocalidade;

public class EntregadorService extends AbstractService<Entregador> {
	private static final long serialVersionUID = 1L;

	public List<Entregador> listAll(Boolean ativo) {
		Entregador filter = new Entregador();
		filter.setAtivo("S");
		try {
			if (ativo)
				return super.findByParameters(filter);
			else
				return super.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void validaCampos(Entregador entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(Entregador entity) throws AppException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(Entregador entity) {
		if (entity.getLocalidades() != null && entity.getLocalidades().size() > 0) {
			for (EntregadorLocalidade localidade : entity.getLocalidades()) {
				if (localidade.getOrdem() == null || localidade.getOrdem() == 0l) {
					mensagens.add("A localidade " + localidade.getLocalidade().getDescricao()
							+ " n�o possui ordem definida.");

				}
			}
		}

	}

}
