package br.com.ichef.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.FichaTecnicaPreparo;
import br.com.ichef.model.FichaTecnicaPreparoInsumo;

public class FichaTecnicaPreparoService extends AbstractService<FichaTecnicaPreparo> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<FichaTecnicaPreparo> findByParameters(FichaTecnicaPreparo object, FilterVisitor visitor)
			throws Exception {

		return mount(super.findByParameters(object, visitor));
	}

	@Override
	public List<FichaTecnicaPreparo> findByParameters(FichaTecnicaPreparo object) throws Exception {

		return mount(super.findByParameters(object));
	}

	public void calcularPercos(FichaTecnicaPreparo entity, Configuracao configuracao) {
		try {

			if (entity.getInsumos() != null && entity.getTamanho() != null) {
				BigDecimal precoVendaReceita = new BigDecimal(0), custoTotal = new BigDecimal(0),
						tamanho = new BigDecimal(0);
				tamanho = new BigDecimal(entity.getTamanho());
				for (FichaTecnicaPreparoInsumo insumo : entity.getInsumos()) {
					if (insumo.getAtivo().equalsIgnoreCase("S"))
						custoTotal = custoTotal
								.add(insumo.getCustoTotal() == null ? new BigDecimal(0) : insumo.getCustoTotal());

				}
				precoVendaReceita = (custoTotal.doubleValue() > 0d ? custoTotal
						.divide(new BigDecimal(configuracao.getCustoMercadoriaVendida()).divide(new BigDecimal(100)))
						.setScale(2, RoundingMode.CEILING) : custoTotal);
				// entity.setPrecoVendaReceita(precoVendaReceita);
				// entity.setPrecoVendaPorcao((precoVendaReceita.doubleValue() > 0d
				// ? (precoVendaReceita.divide(tamanho, BigDecimal.ROUND_UP)).setScale(2,
				// RoundingMode.CEILING)
				// : precoVendaReceita));

				// entity.setPrecoCustoPorcao((custoTotal.doubleValue() > 0d
				// ? (custoTotal.divide(tamanho, BigDecimal.ROUND_UP)).setScale(2,
				// RoundingMode.CEILING)
				// : custoTotal));

				// entity.setPrecoCustoReceita(custoTotal);

			} else {
				/*
				 * entity.setPrecoVendaReceita(new BigDecimal(0));
				 * entity.setPrecoVendaPorcao(new BigDecimal(0));
				 * 
				 * entity.setPrecoCustoPorcao(new BigDecimal(0));
				 * 
				 * entity.setPrecoCustoReceita(new BigDecimal(0));
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<FichaTecnicaPreparo> listAll(Boolean ativo) {
		FichaTecnicaPreparo filter = new FichaTecnicaPreparo();
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
	protected void validaCampos(FichaTecnicaPreparo entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(FichaTecnicaPreparo entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(FichaTecnicaPreparo entity) throws AppException {
		// TODO Auto-generated method stub

	}

}
