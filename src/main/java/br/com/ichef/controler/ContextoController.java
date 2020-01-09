package br.com.ichef.controler;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

@SessionScoped
@Named
public class ContextoController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -36222423182436073L;

	private String crudMessage;
	
	private Object object;
	
	private Object objectFilter;	
	
	private String idComponente;
	
	private String telaOrigem;
	
	private Boolean perfilConsulta;
	
	@PostConstruct
	public void init() {
		System.out.println("Sessão iniciada!");
	}
	
	@PreDestroy
	public void detroy() {
		System.out.println("Sessão expirada!");
	}
	
    public String getCrudMessage() {
        return crudMessage;
    }

	public void setCrudMessage(String crudMessage) {
		this.crudMessage = crudMessage;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	/**
	 * Restringe o tamanho de um atributo
	 * 
	 * @param value
	 * @param maximo
	 * @return
	 */
	public String truncaValor(String value, int maximo) {
		if (value.trim().length() > maximo) {
			return value.substring(0, maximo);
		}

		return value;
	}
	
	/**
	 * Método usado para guardar o id do componente que chamou o evento.
	 * Esse método será usado normalmente em botões que chamam dialogs
	 */
	public void guardarIdComponente(){
		FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    idComponente = (String) map.get("idComponente");
	}
	
	/**
	 * Este método trabalha em conjunto com o método guardarIdComponente. Também será usado normalmente junto com os dialogs.
	 * Quando o dialog for fechado este método dará foco no botão que chamou o dialog.
	 * @throws InterruptedException 
	 */
	public void giveFocus() throws InterruptedException {
		Thread.sleep(250);
		RequestContext.getCurrentInstance().execute("giveFocus('"+idComponente+"')");
	}

	public String getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(String idComponente) {
		this.idComponente = idComponente;
	}
	
	/**
	 * Método responsável por armazenar o nome da tela, que será utilizada para voltar a tela chamadadora
	 * ao clicar em voltar
	 * @return
	 */
	public String getTelaOrigem() {
		return telaOrigem;
	}

	public void setTelaOrigem(String telaOrigem) {
		this.telaOrigem = telaOrigem;
	}
	/**
	 * Propriedade utilizada para manter o filtro utilizada em uma tela de consulta
	 * @return
	 */
	public Object getObjectFilter() {
		return objectFilter;
	}

	public void setObjectFilter(Object objectFilter) {
		this.objectFilter = objectFilter;
	}
	
	public void limpar(){
		this.crudMessage=null;
		this.idComponente =null;
		this.object = null;
		this.objectFilter = null;
		this.telaOrigem = null;		
	}

	public Boolean getPerfilConsulta() {
		return perfilConsulta;
	}

	public void setPerfilConsulta(Boolean perfilConsulta) {
		this.perfilConsulta = perfilConsulta;
	}
	
}
