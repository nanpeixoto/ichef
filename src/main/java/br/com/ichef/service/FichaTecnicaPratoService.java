package br.com.ichef.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.Query;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoPreparo;
import br.com.ichef.model.FichaTecnicaPratoTipo;

public class FichaTecnicaPratoService extends GenericDAO<FichaTecnicaPrato> {
	private static final long serialVersionUID = 1L;

	@Override
	public FichaTecnicaPrato saveOrUpdade(FichaTecnicaPrato entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}
	
	

	public void calcularPercos(FichaTecnicaPrato entity, Configuracao configuracao) {
		try {

			if (entity.getFichaTecnicaPratoPreparos() != null) {
				BigDecimal precoVendaReceita = new BigDecimal(0), custoTotal = new BigDecimal(0),
						tamanho = new BigDecimal(1);

				for (FichaTecnicaPratoPreparo item : entity.getFichaTecnicaPratoPreparos()) {
					if (item.getAtivo().equalsIgnoreCase("S"))
						custoTotal = custoTotal
								.add(item.getCustoTotal() == null ? new BigDecimal(0) : item.getCustoTotal());

				}
				//precoVendaReceita = (custoTotal.doubleValue() > 0d ? custoTotal
				//		.divide(new BigDecimal(configuracao.getCustoMercadoriaVendida()).divide(new BigDecimal(100)))
				//		.setScale(2, RoundingMode.CEILING) : custoTotal);

				//entity.setPrecoVendaReceita(precoVendaReceita);

				//entity.setPrecoVendaPorcao((precoVendaReceita.doubleValue() > 0d
				//		? (precoVendaReceita.divide(tamanho, BigDecimal.ROUND_UP)).setScale(2, RoundingMode.CEILING)
				//		: precoVendaReceita));

				//entity.setPrecoCustoPorcao((custoTotal.doubleValue() > 0d
				//		? (custoTotal.divide(tamanho, BigDecimal.ROUND_UP)).setScale(2, RoundingMode.CEILING)
				//		: custoTotal));

				//entity.setPrecoCustoReceita(custoTotal);

				/*if (entity.getFichaTecnicaPratoTipos() != null) {
					for (FichaTecnicaPratoTipo fichaTipo : entity.getFichaTecnicaPratoTipos()) {
						fichaTipo.setCustoTotal(custoTotal.add(fichaTipo.getTipoPrato().getCustoTotal()));
					}
				}*/

			} else {
				/*entity.setPrecoVendaReceita(new BigDecimal(0));
				entity.setPrecoVendaPorcao(new BigDecimal(0));

				entity.setPrecoCustoPorcao(new BigDecimal(0));

				entity.setPrecoCustoReceita(new BigDecimal(0));*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean validaRegras(FichaTecnicaPrato entity) {
		return true;
	}

	public List<FichaTecnicaPrato> listAll(Boolean ativo) {
		FichaTecnicaPrato filter = new FichaTecnicaPrato();
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

	public Integer findQuantidadeFichaByPreparacao(String listaPreparacoes) {
		StringBuilder sb =  new StringBuilder();
		sb.append("select sum(qtd) from ( SELECT count(distinct this_.CD_FICHA_TECNICA_PRATO) qtd " +
				"    FROM ficha_tecnica_prato this_ INNER JOIN ficha_tecnica_prato_preparo pe1_ " + 
				"         ON this_.cd_ficha_tecnica_prato = pe1_.cd_ficha_tecnica_prato " + 
				"   group by this_.CD_FICHA_TECNICA_PRATO " + 
				"   having GROUP_CONCAT(pe1_.cd_ficha_tecnica_preparo)   = '"+ listaPreparacoes+"')tab"
		);
		
		Query query = getEntityManager().createNativeQuery(sb.toString());
		int count =  query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
		return count;
	}

}
