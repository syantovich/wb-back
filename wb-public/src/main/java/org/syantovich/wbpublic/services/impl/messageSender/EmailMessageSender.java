package org.syantovich.wbpublic.services.impl.messageSender;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.services.MessageSender;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@Service
@NoArgsConstructor
public class EmailMessageSender implements MessageSender {

    @Value("${mail.smtp.username}")
    private String username;
    @Value("${mail.smtp.password}")
    private String password;
    @Value("${mail.smtp.auth}")
    private boolean isAuth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttlsEnabled;
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private Integer port;

    @Override
    public boolean send(String to, String subject, String text) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", isAuth);
        properties.put("mail.smtp.starttls.enable", starttlsEnabled);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
