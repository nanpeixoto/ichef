package br.com.ichef.listener;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 * Classe que impede acesso direto as paginas sem esta logado
 * 
 * @author jsouzaa
 *
 */
public class AuthorizationListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Metodo que verifica se o usuario logado e o mesmo do login
	 * 
	 * @param arg0
	 */
	@Override
	public void afterPhase(PhaseEvent arg0) {
		FacesContext facesContext = arg0.getFacesContext();
		String currentPage = facesContext.getViewRoot().getViewId();

		boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
		boolean isLoginEntregadorPage = (currentPage.lastIndexOf("loginEntregador.xhtml") > -1);
		boolean isPainel = (currentPage.contains("painel_empresa"));

		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

		if (session == null)

		{
			NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
			nh.handleNavigation(facesContext, null, "loginPage");
		} else {
			Object currentUser = session.getAttribute("loggedUser");

			if (isPainel) {
				return;
			}

			if ( /* !isLoginEntregadorPage ||*/ !isLoginPage && (currentUser == null || currentUser == "")) {
				NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
				if (isLoginPage)
					nh.handleNavigation(facesContext, null, "loginPage");
				/*if (isLoginEntregadorPage)
					nh.handleNavigation(facesContext, null, "loginPageEntregador");*/
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
