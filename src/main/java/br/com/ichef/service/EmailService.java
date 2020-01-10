package br.com.ichef.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.dto.EmailDTO;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Email;

@Stateless
public class EmailService extends AbstractService<Email> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String EMAIL_REMETENTE = "cozinhadechef.notificacoes@gmail.com";
	private static String SENHA_REMETENTE = "nan@3440265";

	private Properties init() {
		Properties props = new Properties();

		/** Par�metros de conex�o com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		return props;

	}

	public EmailDTO enviarEmail(EmailDTO email) {

		Session session = Session.getDefaultInstance(init(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_REMETENTE, SENHA_REMETENTE);
			}
		});

		/** Ativa Debug para sess�o */
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

			/** M�todo para enviar a mensagem criada */
			Transport.send(message);

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

	@Override
	protected void validaCampos(Email entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Email entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Email entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
