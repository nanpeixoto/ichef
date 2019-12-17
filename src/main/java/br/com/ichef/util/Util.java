package br.com.ichef.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Classse utilizadas para colocar o objeto na sessÃƒÂ£o e ser utilizado.
 * 
 * @author esouzaa
 * @author jteixeira
 * @author jsouzasa
 * @author rmotal
 * 
 */

public class Util extends ReportUtils {

	public static final String STRING_VAZIA = "";

	private static final String WILDCARD = "%";

	public static String LOGO_HSI = "logo_hsi.jpg";

	public static String NF_EMITIDA = "nf_emitida.png";

	public static final String DIR_DEFAULT = "resources/";

	public static final String RELATORIO = "resources.properties";

	public static final String LETRAS_COM_ACENTUACAO = "Ãƒï¿½Ãƒâ‚¬ÃƒÆ’Ãƒâ€šÃƒâ€žÃƒâ€°ÃƒË†ÃƒÅ Ãƒâ€¹Ãƒï¿½ÃƒÅ’Ãƒï¿½ÃƒÅ½Ãƒâ€œÃƒâ€™Ãƒâ€¢Ãƒâ€�Ãƒâ€“ÃƒÅ¡Ãƒâ„¢Ãƒâ€ºÃƒÅ“Ãƒâ€¡Ãƒâ€˜Ãƒï¿½Ã…Â¸ÃƒÂ¡ÃƒÂ ÃƒÂ£ÃƒÂ¢ÃƒÂ¤ÃƒÂ©ÃƒÂ¨ÃƒÂªÃƒÂ«ÃƒÂ­ÃƒÂ¬ÃƒÂ¯ÃƒÂ®ÃƒÂ³ÃƒÂ²ÃƒÂµÃƒÂ´ÃƒÂ¶ÃƒÂºÃƒÂ¹ÃƒÂ»ÃƒÂ¼ÃƒÂ§ÃƒÂ±ÃƒÂ½ÃƒÂ¿";
	public static final String LETRAS_SEM_ACENTUACAO = "AAAAAEEEEIIIIOOOOOUUUUCNYYaaaaaeeeeiiiiooooouuuucnyy";

	
	
	 public static Date zerarHoras(Date data){
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(data);
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();
	    }

	 
	public static String calculaIdadeCompletaPaciente(Date dataNasc) {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(dataNasc);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		int emAnos = age;
		int emMeses = calculaIdadeMeses(dataNasc);

		return emAnos + " a " + emMeses + " m";
	}

	// Calcula a Idade baseado em java.util.Date
	public static int calculaIdadeMeses(Date dataNasc) {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(dataNasc);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.MONTH) - dateOfBirth.get(Calendar.MONTH);
		dateOfBirth.add(Calendar.MONTH, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		int emMeses = (age < 0 ? age * -1 : age) - 12;
		return (emMeses < 0 ? emMeses * -1 : emMeses);
	}

	public static Calendar toOnlyDate(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date;
	}

	public static Date getUltimoDiaDoMes(Date date) {
		Calendar calendar = toCalendar(date);
		// muda a data da variÃƒÂ¡vel para o ÃƒÂºltimo dia do mÃƒÂªs
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}

	public static Date getPrimeiroDiaDoProximoMes(Date date) {
		Calendar calendar = toCalendar(date);
		calendar.add(Calendar.MONTH, 1);

		return calendar.getTime();
	}

	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date getUltimoDiaUtilDoMesMesmoMenosFeriadoOuFimDeSemana(Calendar calendar) {
		// muda a data da variÃƒÂ¡vel para o ÃƒÂºltimo dia do mÃƒÂªs
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		// enquanto for sÃƒÂ¡bado, domingo ou feriado
		while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || ehFeriado(calendar)) {
			// decrementa a data em um dia
			calendar.add(Calendar.DATE, -1);
		}
		return calendar.getTime();
	}

	public  static String getDiaSemana(Date diaSemana) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(diaSemana);
		Locale brasil = new Locale( "pt" , "br" ); 
		return new DateFormatSymbols(brasil).getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
	}

	public static boolean ehFeriado(Calendar calendar) {
		Calendar feriado = Calendar.getInstance();
		// considerando 30 de abril como feriado
		feriado.set(calendar.get(Calendar.YEAR), Calendar.APRIL, 30);
		if (calendar.get(Calendar.DAY_OF_YEAR) == feriado.get(Calendar.DAY_OF_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * MÃƒÂ©todo para verificar se um objeto do tipo Boolean ÃƒÂ© nulo ;
	 * 
	 * 
	 */

	public static Boolean isNull(Object objeto) {
		return (objeto == null) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * MÃƒÂ©todo para verificar se um objeto do tipo String ÃƒÂ© nulo;
	 * 
	 * 
	 */

	public static Boolean isEmptyString(String str) {
		return (!Util.isNull(str) && str.length() == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * MÃƒÂ©todo para verificar se um objeto ÃƒÂ© nulo ou vazio;
	 * 
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public static boolean isNullOuVazio(Object pObjeto) {

		if (pObjeto == null) {

			return true;

		} else if (pObjeto instanceof Collection) {

			return ((Collection) pObjeto).isEmpty();

		} else if (pObjeto instanceof String) {

			return ((String) pObjeto).trim().equals(STRING_VAZIA);

		} else if (pObjeto instanceof Integer) {

			return ((Integer) pObjeto).intValue() == 0;
		}

		return false;
	}

	public static boolean isNullOuVazio(Integer integer) {
		return integer == null;
	}

	/**
	 * Remove acentos de uma String
	 * 
	 * @param String
	 *            string
	 */

	public static String removeAcentos(String string) {
		return Normalizer.normalize(string.trim(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public static String pesquisaPorNome(String nomeParametroPesquisa, String campoBanco) {
		StringBuilder hql = new StringBuilder();
		// String nomeModificado = WILDCARD +
		// translate(nomeParametroPesquisa.trim().toUpperCase()) + WILDCARD;
		String nomeModificado = WILDCARD + removeAcentos(nomeParametroPesquisa.trim().toUpperCase()) + WILDCARD;
		hql.append(" AND TRANSLATE(UPPER(" + campoBanco + "), '" + LETRAS_COM_ACENTUACAO + "', '"
				+ LETRAS_SEM_ACENTUACAO + "') like '" + nomeModificado + "'");
		return hql.toString();
	}

	/*
	 * public static String formatData(Date data, String pFormato){ SimpleDateFormat
	 * sdate = new SimpleDateFormat(pFormato); return sdate.format(data); }
	 */

	public static String formatData(Date data, String pFormato) {
		SimpleDateFormat sdate = new SimpleDateFormat(pFormato);
		return sdate.format(data);
	}

	public static String formatDataHora(Date data) {
		SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		return sdate.format(data);
	}

	/* MÃƒâ€°TODO PARA INSERIR ZEROS A ESQUERDA */

	public static String lpad(String input, char padding, int length) {

		String output = "";

		if (input != null) {

			output = input;

		}

		if (output.length() >= length) {

			return output;

		} else {

			StringBuffer result = new StringBuffer();

			int numChars = length - output.length();

			for (int i = 0; i < numChars; i++) {

				result.append(padding);

			}

			result.append(output);

			return result.toString();

		}

	}

	/**
	 * MÃƒÂ©todo para formataÃƒÂ§ÃƒÂ£o de CPF;
	 * 
	 * 
	 */

	public static String formataCPF(Long cpf) {
		String cpfString = lpad(Long.toString(cpf), '0', 11);

		if (cpfString != null) {

			// cpfString = cpfString.substring(0, 9) + "-" +
			// cpfString.substring(9, 11);
			cpfString = cpfString.substring(0, 3) + "." + cpfString.substring(3, 6) + "." + cpfString.substring(6, 9)
					+ "-" + cpfString.substring(9, 11);

		}

		return cpfString;
	}

	/**
	 * MÃƒÂ©todo para formataÃƒÂ§ÃƒÂ£o de cnpj
	 * 
	 * @param pCnpj
	 * @return String
	 */
	public static String formataCnpj(Long pCnpj) {
		String cnpjString = lpad(Long.toString(pCnpj), '0', 14);

		if (!isNullOuVazio(cnpjString)) {
			cnpjString = cnpjString.substring(0, 2) + "." + cnpjString.substring(2, 5) + "."
					+ cnpjString.substring(5, 8) + "/" + cnpjString.substring(8, 12) + "-"
					+ cnpjString.substring(12, 14);
		}

		return cnpjString;
	}

	/**
	 * Metodo valida se determinado nome esta de acordo com o padrao da expressao
	 * regular
	 * 
	 * @param pExpressaoRegular
	 * @param pNome
	 * @return boolean
	 */
	public static boolean validarNomePorExpressaoRegular(String pExpressaoRegular, String pNome) {
		// Obtem expressao regular informada
		Pattern pattern = Pattern.compile(pExpressaoRegular);
		// Obtem o nome informado
		Matcher matcher = pattern.matcher(pNome);
		// Verifica se o nome informado esta de acordo com expressao regular
		if (matcher.find()) {
			// Nome e valido segundo a expressao regular
			return true;
		} else {
			// Nome nao e valido segundo a expressao regular
			return false;
		}
	}

	public static String formataCodigo(Integer codigo) {

		String codigoFormatado = lpad(Integer.toString(codigo), '0', 4);
		if (codigoFormatado != null) {
			return codigoFormatado;
		} else {
			return "";
		}

	}

	/**
	 * Valida CNPJ do usuÃƒÂ¡rio.
	 * 
	 * @param cnpj
	 *            String valor com 14 dÃƒÂ­gitos
	 */
	public static Boolean validaCNPJ(String cnpj) {

		if (cnpj == null || cnpj.length() != 14) {
			return false;
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ nÃƒÂ£o possui somente nÃƒÂºmeros
			return false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();

		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc)))
				.append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		soma = 0;

		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc)))
				.append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpj_calc) || cnpj.equals("00000000000000")) {
			// JsfUtil.addErrorMessage("CNPJ InvÃƒÂ¡lido!");
			return false;
		}

		return true;
	}

	public static String convertStringToMD5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(64);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}

	public static String formataLista(List<String> lista) {
		String r = "";
		if (lista.size() >= 1) {
			r += lista.get(0);
			if (lista.size() > 1) {
				for (int i = 1; i < (lista.size() - 1); i++) {
					r += ", " + lista.get(i);
				}
				r += " e " + lista.get(lista.size() - 1);
			}
		}
		return r;
	}

	/**
	 * MÃƒÂ©todo responsÃƒÂ¡vel por formatar a mensagem para ser exibida no
	 * p:message do JSF o metÃƒÂ³do vai criar um paragrafo para cada "/n" encontrado
	 * no parÃƒÂ¢metro informado.
	 * 
	 * @param textoASerExibido
	 */
	public static void formatarMensagemDeValidacao(String textoASerExibido) {
		String[] vetor = textoASerExibido.split("/n");
		for (int i = 0; i < vetor.length; i++) {
			// JsfUtil.addErrorMessage(vetor[i]);
		}
	}

	public static String calculaTempoEntreDatas(Date d1, Date d2) {

		if (!isNullOuVazio(d1) && !isNullOuVazio(d2)) {

			long segundos = (d2.getTime() / 1000) - (d1.getTime() / 1000);
			long segundo = segundos % 60;
			long minutos = segundos / 60;
			long minuto = minutos % 60;
			long hora = minutos / 60;
			String hms = String.format("%02d:%02d:%02d", hora, minuto, segundo);
			return hms;

		}

		return "";
	}

	public Properties getProperties() throws Exception {

		File file = new File(getRealPath(DIR_DEFAULT + RELATORIO));
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			// lÃƒÂª os dados que estÃƒÂ£o no arquivo
			props.load(fis);
			fis.close();
		} catch (IOException ex) {
			throw new Exception("NÃƒÂ£o foi possivel ler o Properties");
		}
		return props;
	}

	protected static String getRealPath(String pArquivo) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

		return scontext.getRealPath(pArquivo);
	}

	public InputStream getInputStream(String arquivo) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext.getResourceAsStream(arquivo);
	}

	public String getImagem(String imagem) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext.getRealPath(getProperties().getProperty("dir.imagem") + imagem);
	}

	public String getRelatorio(String relatorio) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext.getRealPath(getProperties().getProperty("dir.relatorio") + relatorio);
	}

	public static String getRequiredMessage(String label) {
		return MensagemUtil.obterMensagem("geral.required.field", MensagemUtil.obterMensagem(label));
	}

	public static String getRequiredTextMessage(String text) {
		return MensagemUtil.obterMensagem("geral.required.field", text);
	}

	public static String dateToString(Date valor) {
		if (valor != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format(valor);
		} else {
			// log.info("A Data passada para ser convertida para STRING veio nula.");
			return null;
		}
	}

}