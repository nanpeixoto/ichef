package br.com.ichef.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.SessionScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.dto.EmailDTO;
import br.com.ichef.model.Email;

@SessionScoped
public class EmailService extends GenericDAO<Email> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String EMAIL_REMETENTE = "cozinhadechef.notificacoes@gmail.com";
	private static String SENHA_REMETENTE = "nan@3440265";

	private Properties init() {
		Properties props = new Properties();

		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		return props;

	}

	public EmailDTO enviarEmail(EmailDTO email) {
		
		String emailRemetente = ( email.getEmailRemetente()!=null ? email.getEmailRemetente():EMAIL_REMETENTE);
		String senhaRemetente = ( email.getSenhaRemetente()!=null ? email.getSenhaRemetente():SENHA_REMETENTE);

		Session session = Session.getDefaultInstance(init(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailRemetente, senhaRemetente);
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);

			// Remetente
			message.setFrom(new InternetAddress(EMAIL_REMETENTE));

			// detino
			Address[] toUser = InternetAddress.parse(email.getDestinatario());

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(email.getAssunto());// Assunto
			message.setText(email.getTexto());

			/** Método para enviar a mensagem criada */
			Transport.send(message);

			email.setSituacao("S");

		} catch (MessagingException e) {
			email.setLog(e.getMessage());
			email.setSituacao("E");
		}

		email.setDataEnvio(new Date());

		return email;
	}
	
	public EmailDTO enviarEmailHtml(EmailDTO email) {
		
		String emailRemetente = ( email.getEmailRemetente()!=null ? email.getEmailRemetente():EMAIL_REMETENTE);
		String senhaRemetente = ( email.getSenhaRemetente()!=null ? email.getSenhaRemetente():SENHA_REMETENTE);
		

		Session session = Session.getDefaultInstance(init(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailRemetente, senhaRemetente);
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(false);

		try {

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress( EMAIL_REMETENTE ));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getDestinatario()));
			 message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse( EMAIL_REMETENTE));

			// Set Subject: header field
			message.setSubject(email.getAssunto());
			

			// Send the actual HTML message, as big as you like
			message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
			message.setContent(email.getTexto().replace("\n", "<br>"), "text/html; charset=iso-8859-1");
			message.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// Send message
			Transport.send(message);

			//System.out.println("Sent message successfully....");
			
			email.setSituacao("S");

		} catch (MessagingException e) {
			email.setLog(e.getMessage());
			email.setSituacao("E");
		}

		email.setDataEnvio(new Date());

		return email;
	}

	public void enviarEmails() {
		Email email = new Email();
		email.setEmailEnviao("N");
		try {
			List<Email> emails = new ArrayList<Email>();
			emails = findByParameters(email);
			System.out.println("Emails Encontratos:" +emails.size());
			for (Email emailParaEnviar : emails) {
				try {
					EmailDTO dto = new EmailDTO();
					dto.setAssunto(emailParaEnviar.getAssunto());
					dto.setDestinatario(emailParaEnviar.getEmail());
					dto.setTexto(emailParaEnviar.getMensagem());

					dto = enviarEmail(dto);

					emailParaEnviar.setDataEnvio(dto.getDataEnvio());
					emailParaEnviar.setLog(dto.getLog());
					emailParaEnviar.setEmailEnviao(dto.getSituacao());

				} catch (Exception e) {
					emailParaEnviar.setLog(e.getMessage());
					emailParaEnviar.setEmailEnviao("E");
				}

				saveOrUpdade(emailParaEnviar);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
