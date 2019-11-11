package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.service.CardapioFichaPratoService;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.FichaTecnicaPratoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class CardapioController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private CardapioService service;

	@Inject
	private CardapioFichaPratoService cardapioFichaPratoService;

	@Inject
	private FichaTecnicaPratoService fichaTecnicaPratoService;

	private Cardapio entity;

	private Long id;

	private List<Cardapio> lista = new ArrayList<Cardapio>();
	private List<Cardapio> listaFiltro = new ArrayList<Cardapio>();

	private List<Cardapio> listaSelecionadas = new ArrayList<Cardapio>();

	private List<FichaTecnicaPrato> pratos = new ArrayList<FichaTecnicaPrato>();

	private FichaTecnicaPrato prato;
	private Integer quantidade;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		}

		obterListas();
	}

	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Cardapio());
			getEntity().setAtivo("S");
			getEntity().setData(new Date());
			setQuantidade(1);
		}
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
		try {

			setPratos(fichaTecnicaPratoService.listAll(true));// , visitor));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirItensSelecionadas(CardapioFichaPrato insumo) {
		List<CardapioFichaPrato> temp = new ArrayList<CardapioFichaPrato>();
		temp.addAll(entity.getPratos());
		for (CardapioFichaPrato item : entity.getPratos()) {
			if (insumo.getFichaTecnicaPrato().getId().equals(item.getFichaTecnicaPrato().getId()))
				temp.remove(item);
		}
		entity.getPratos().clear();
		entity.getPratos().addAll(temp);

		updateComponentes("Stable");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void adicionarPrato() {
		boolean existe = false;

		if (getPrato() == null) {
			facesMessager.error(getRequiredMessage("Prato"));
			return;
		}

		if (getQuantidade() == null) {
			facesMessager.error(getRequiredMessage("Quantidade"));
			return;
		}

		if (getEntity().getPratos() != null) {
			for (CardapioFichaPrato ficha : getEntity().getPratos()) {
				if (getPrato().getId().equals(ficha.getFichaTecnicaPrato().getId()))
					existe = true;
			}
		}

		if (!existe) {
			CardapioFichaPrato ficha = new CardapioFichaPrato();
			ficha.setCardapio(entity);
			ficha.setQuantidade(getQuantidade());
			ficha.setDataCadastro(new Date());
			ficha.setFichaTecnicaPrato(getPrato());
			ficha.setUsuarioCadastro(userLogado);

			if (getEntity().getPratos() == null) {
				getEntity().setPratos(new ArrayList<CardapioFichaPrato>());
			}
			getEntity().getPratos().add(ficha);

			if (getEntity().getId() != null) {
				try {
					cardapioFichaPratoService.saveOrUpdade(ficha);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			setPrato(null);
			quantidade = 1;

		} else {
			facesMessager.error("Prato já cadastrado");
		}

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}
	
	public void atualizarListaPreparo() {
		obterListas();
	}

	/*
	 * public void excluirLocalidadesSelecionadas(CardapioLocalidade local) {
	 * List<CardapioLocalidade> temp = new ArrayList<>();
	 * temp.addAll(entity.getLocalidades()); for (CardapioLocalidade arealoc :
	 * entity.getLocalidades()) { if
	 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
	 * temp.remove(arealoc); } entity.getLocalidades().clear();
	 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
	 * FacesUtil.addInfoMessage("Itens excluídos com sucesso"); }
	 */

	public String Salvar() throws Exception {

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());

		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());

		}

		service.saveOrUpdade(entity);

		return "lista-cardapio.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-cardapio.xhtml?faces-redirect=true";
	}

	public CardapioService getService() {
		return service;
	}

	public void setService(CardapioService service) {
		this.service = service;
	}

	public Cardapio getEntity() {
		return entity;
	}

	public void setEntity(Cardapio entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cardapio> getLista() {
		return lista;
	}

	public void setLista(List<Cardapio> lista) {
		this.lista = lista;
	}

	public List<Cardapio> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Cardapio> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<Cardapio> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Cardapio> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<FichaTecnicaPrato> getPratos() {
		return pratos;
	}

	public void setPratos(List<FichaTecnicaPrato> pratos) {
		this.pratos = pratos;
	}

	public FichaTecnicaPrato getPrato() {
		return prato;
	}

	public void setPrato(FichaTecnicaPrato prato) {
		this.prato = prato;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
