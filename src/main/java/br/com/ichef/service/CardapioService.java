package br.com.ichef.service;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ichef.arquitetura.service.EntityManagerProducer;
import br.com.ichef.arquitetura.service.Transacional;
import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.Empresa;

public class CardapioService extends GenericDAO<Cardapio> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<Cardapio> findByParameters(Cardapio object, FilterVisitor visitor) throws Exception {

		return mount(super.findByParameters(object, visitor));
	}
	
	@Override
	public List<Cardapio> findByParameters(Cardapio object) throws Exception {

		return mount(super.findByParameters(object));
	}


	public List<Cardapio> listAll(Boolean ativo) {
		Cardapio filter = new Cardapio();
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
 
	public Integer findMediaDeVenda(CardapioFichaPrato cardapioFichaPrato, Empresa empresa) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" SELECT AVG (QTD) MEDIA FROM ( " + 
				" SELECT \r\n" + 
				"    c.`DATA`, cfp.DS_CARDAPIO_FICHA, ftp.CD_FICHA_TECNICA_PRATO, ftp.DS_DESCRICAO, sum(p.NR_QTD) QTD " + 
				"    , p.cd_empresa " + 
				"    FROM" + 
				"         pedido   p " + 
				"        JOIN `cardapio_ficha_prato` `cfp` ON ((`cfp`.`CD_CARDAPIO_PRATO` = `p`.`CD_CARDAPIO_PRATO`)) " + 
				"        JOIN ficha_tecnica_prato ftp ON ftp.CD_FICHA_TECNICA_PRATO = cfp.CD_FICHA_TECNICA_PRATO" + 
				"        JOIN cardapio c ON c.CD_CARDAPIO = p.CD_CARDAPIO " + 
				"        WHERE ftp.cd_ficha_tecnica_prato =   " + cardapioFichaPrato.getFichaTecnicaPrato().getId()+
				"       and cd_empresa =  " + empresa.getId()+
				"  and nr_ordem <= 4 "+
				"   GROUP BY   c.`DATA`, cfp.DS_CARDAPIO_FICHA, ftp.CD_FICHA_TECNICA_PRATO, ftp.DS_DESCRICAO, p.cd_empresa  LIMIT 1, 10) A "
				+ " GROUP BY    cd_empresa ,   cd_ficha_tecnica_prato, DS_DESCRICAO " );
		try {
			if (!getManager().isOpen()) {
				EntityManagerProducer producer = new EntityManagerProducer();
				setManager(producer.createEntityManager());
			} else {
				getManager().clear();
			}

			Query query = getManager().createNativeQuery(sb.toString());
			int count = query.getSingleResult() != null ? Math.round( Float.parseFloat(query.getSingleResult().toString()) ) : 0;
			return count;
		} catch (NoResultException e) {
			return 0;
		}

	}

}
