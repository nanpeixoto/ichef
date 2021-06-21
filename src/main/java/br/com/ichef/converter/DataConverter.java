package br.com.ichef.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

import org.primefaces.component.calendar.Calendar;

import br.com.ichef.arquitetura.util.Constantes;

/**
 * Classe para convercao de datas.
 * 
 * @author esouzaa
 * 
 */

@FacesConverter("converter.DataConverter")
public class DataConverter extends DateTimeConverter {

	public static final String MAP_DATA_INVALIDA = "MAP_DATA_INVALIDA";

	public DataConverter() {
		setPattern(Constantes.FORMATO_DATA);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Map<String, Boolean> dataInvalida = getMapDataInvalidaSessao();

		Object retorno = value;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constantes.FORMATO_DATA);
			dateFormat.setLenient(false);
			retorno = dateFormat.parse(value);
			preencherMapdataInvalida(component, dataInvalida, false);

		} catch (ParseException e) {

			if (!value.equals(Constantes.PADRAO_FORMATO_DATA)) {

				((Calendar) component).setValue(value);
				preencherMapdataInvalida(component, dataInvalida, true);
			} else {
				preencherMapdataInvalida(component, dataInvalida, false);
			}
		}

		setMapDataInvalidaSessao(dataInvalida);

		return retorno;
	}

	/**
	 * Metodo preenche o map dataInvalida com o id do componente e booleano que
	 * informa se a data informada pelo usuario e invalida
	 * 
	 * @param component
	 * @param dataInvalida
	 * @param pIsDataValida
	 */
	public void preencherMapdataInvalida(UIComponent component, Map<String, Boolean> dataInvalida, Boolean pIsDataValida) {
		dataInvalida.put(component.getClientId().substring(component.getClientId().lastIndexOf(':') + 1), pIsDataValida);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Boolean> getMapDataInvalidaSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Map<String, Boolean> dataInvalida = (Map<String, Boolean>) session.getAttribute(DataConverter.MAP_DATA_INVALIDA);

		if (dataInvalida == null) {
			dataInvalida = new HashMap<String, Boolean>();
		}
		return dataInvalida;
	}

	public static void setMapDataInvalidaSessao(Map<String, Boolean> map) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

		session.setAttribute(DataConverter.MAP_DATA_INVALIDA, map);
	}
}
