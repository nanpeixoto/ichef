
package br.com.ichef.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

public class StringUtil {

    //CARACTERES
    public static final String CARACTER_BARRA = "/";
    public static final String CARACTER_UNDERLINE = "_";
    public static final String CARACTER_HIFEN = "-";
    public static final String CARACTER_PONTO = ".";
    public static final String CARACTER_VIRGULA = ",";
    public static final String CARACTER_VAZIO = "";
    public static final String CARACTER_ESPACO = " ";
    public static final String CARACTER_UM = "1";
    public static final String CARACTER_PARENTESE_ESQ = "(";
    public static final String CARACTER_PARENTESE_DIR = ")";
    public static final String CARACTER_COLCHETE_ESQ = "[";
    public static final String CARACTER_COLCHETE_DIR = "]";
    public static final String PATTERN_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

	public static String removeAccents(String str) {  
		  
	    str = Normalizer.normalize(str, Normalizer.Form.NFD);  
	  
	    str = str.replaceAll("[^\\p{ASCII}]", "");  
	  
	    return str;  
	  
	}  
	
	  /**
	   * Preenche a string com 'caracter' a esquerda.
	   * 
	   * @param texto
	   * @param caracter
	   * @param size
	   * @return
	    * 
	    * <code>br.gov.gitecsa.util.StringUtil#leftFill(String, char, int)</code>
	    */
	  public static String leftFill(String texto, char caracter, int size) {
	    StringBuilder str = new StringBuilder();
	    for(int i = 0; i < (size - texto.length()); i++) {
	      str.append(caracter);
	    }
	    str.append(texto);
	    return str.toString();
	  }

	  
	  public static String criptografa(String senha) {

			/**
			 * Criptografando Senha
			 */
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");

				md.update(senha.getBytes());
				BigInteger hash = new BigInteger(1, md.digest());
				senha = hash.toString(16);

			}

			catch (NoSuchAlgorithmException ns) {
				ns.printStackTrace();
			}

			return senha;
		}
	  
	  
	  public static String stringToHashSHA1(String valor) {
			try {
				if (!valor.trim().equals("")) {
					MessageDigest digest = MessageDigest.getInstance("SHA-1");
					digest.update(valor.getBytes());
					BigInteger hash = new BigInteger(1, digest.digest());
					return hash.toString(16);
				} else {
					return null;
				}
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}
	  
}
