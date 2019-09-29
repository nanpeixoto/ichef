package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.EntregadorLocalidade;

public class EntregadorService extends GenericDAO<Entregador> {
	private static final long serialVersionUID = 1L;

	@Override
	public Entregador saveOrUpdade(Entregador entity) throws Exception {

		return super.saveOrUpdade(entity);

	}

	public List<String> validaRegras(Entregador entity) {
		List<String> mensagem = new ArrayList<String>();
		// verificar se todos tem ordem definida
		if (entity.getLocalidades() != null && entity.getLocalidades().size() > 0) {
			for (EntregadorLocalidade localidade : entity.getLocalidades()) {
				if (localidade.getOrdem() == null || localidade.getOrdem() == 0l) {
					mensagem.add("A localidade "+localidade.getLocalidade().getDescricao()+" não possui ordem definida.");
					return mensagem;
				}
			}
		}

		return null;
	}

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

}
