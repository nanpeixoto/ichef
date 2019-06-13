package br.com.ichef.util;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Classe utilitária para mapeamento de códigos de mensagens para a mensagem no arquivo de properties.
 * 
 */
public final class MensagemUtil {
	
	private static Properties mensagens;
	
	/**
	 * Construtor da classe MensagemUtil.
	 */
	private MensagemUtil() {
	}
	
	/**
	 * Recupera o arquivo de propriedade das mensagens do sistema.
	 * @return
	 */
	private static Properties getMensagens() {
		inicializaProperties();
		return mensagens;
	}

	/**
	 * Inicializa o arquivo de propriedades.
	 * Método sincronizado.
	 */
	private static synchronized void inicializaProperties() {
		if (mensagens == null || mensagens.isEmpty()) {
			mensagens = new Properties();
			try {
				InputStream arquivoProperties = 
					Thread.currentThread().getContextClassLoader().getResourceAsStream("bundle.properties");
				
				mensagens.load(arquivoProperties);
			} catch (Exception e) { }
		}
	}
	
	/**
	 * Obtêm a mensagem a partir de seu código no arquivo de properties.
	 * @param codigoMensagem - Código da mensagem no arquivo de properties
	 * @param parametros - Parâmetros a serem substituídos no texto da mensagem
	 * @return Mensagem do arquivo de properties que representa o código passado como parâmetro.
	 */
	public static String obterMensagem(String codigoMensagem, Object ... parametros) {
		String mensagem = getMensagens().getProperty(codigoMensagem);
		if (mensagem != null) {
			return MessageFormat.format(mensagem, parametros);
		}
		
		return codigoMensagem;
	}
	
	/**
	 * Obtêm a mensagem a partir de seu código no arquivo de properties.
	 * @param codigoMensagem - Código da mensagem no arquivo de properties
	 * @return Mensagem do arquivo de properties que representa o código passado como parâmetro.
	 */
	public static String obterMensagem(String codigoMensagem) {
		return obterMensagem(codigoMensagem, new Object[]{});
	}
	
	public static String montaMensagemRetornoProcedure(String mensagem){
		mensagem = mensagem.replace("ORA-06512: em \"DBAHSI.PRC_SAC_UPDATE_CON_PAG_REPASSE\"", "");
		mensagem = mensagem.replace(" , line ", " na linha: ");
		mensagem = mensagem.replace("\n", " - ");
		return mensagem;
	}
	
}
