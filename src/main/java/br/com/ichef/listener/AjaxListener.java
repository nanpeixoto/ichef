package br.com.ichef.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.context.OmniPartialViewContext;

import br.com.ichef.arquitetura.util.Constantes;


public class AjaxListener implements PhaseListener {

	/**
	 * Classe Listener para interceptar todas as requisições Ajax e recarregar o
	 * javaScript da tela atualizada.
	 */
	private static final long serialVersionUID = 6L;

	public void afterPhase(PhaseEvent pEvento) {

	}

	public void beforePhase(PhaseEvent pEvento) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		boolean ajaxRequest = Constantes.PARTIAL_AJAX.equals(request.getHeader(Constantes.FACES_REQUEST));
		if (ajaxRequest) {
			OmniPartialViewContext requestContext = (OmniPartialViewContext) FacesContext.getCurrentInstance().getAttributes().get(Constantes.OMNI_PARTIAL_VIEW_CONTEXT);
			 requestContext.addCallbackScript("onRenderAjax();");
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}
