package com.quiz;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;



public class EmailSender {
     public static void sendConfirmationEmail(String to, String pseudo, String password) {
        final String fromEmail = "rayann07226@gmail.com"; 
        final String passwordEmail = "ylqv sqyz gctt tiyp ";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true"); 

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, passwordEmail);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Bienvenue sur Matrix");

            String htmlMsg = "<!DOCTYPE html>"
        + "<html lang='fr'>"
        + "<head>"
        + "    <meta charset='UTF-8'>"
        + "    <style>"
        + "        body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; padding: 20px; }"
        + "        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }"
        + "        h2 { color: #0041e8; }"
        + "        p { color: #333; }"
        + "    </style>"
        + "</head>"
        + "<body>"
        + "    <div class='container'>"
        + "        <h2>Bienvenue " + pseudo + " !</h2>"
        + "        <p>Merci de vous être inscrit sur <strong>Matrix</strong>.</p>"
        + "        <p>Votre compte a été créé avec succès.</p>"
        + "        <p>Nous vous invitons à garder vos informations de connexion en lieu sûr.</p>"
        + "        <p>Si vous avez des questions ou besoin d'assistance, n'hésitez pas à nous contacter.</p>"
        + "        <p>Cordialement,<br>L'équipe Matrix</p>"
        + "    </div>"
        + "</body>"
        + "</html>";


            message.setContent(htmlMsg, "text/html");

            Transport.send(message);

        } catch (MessagingException e) {
           System.out.println("Error"+e);
        }
    }
}
