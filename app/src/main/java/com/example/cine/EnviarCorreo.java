package com.example.cine;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EnviarCorreo {
    public static void enviarCorreo(String usuario, String password,String destinatario, String asunto, String contenido){

      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  // Configuración del servidor SMTP
                  Properties propiedades = new Properties();
                  propiedades.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
                  propiedades.put("mail.smtp.socketFactory.port", "465");
                  propiedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                  propiedades.put("mail.smtp.auth", "true");
                  propiedades.put("mail.smtp.port", "465");
                  // Crear una sesión
                  Session sesion = Session.getInstance(propiedades,
                          new javax.mail.Authenticator() {
                              protected PasswordAuthentication getPasswordAuthentication() {
                                  return new PasswordAuthentication(usuario, password);
                              }
                          });
                  try {
                      // Crear el mensaje
                      Message mensaje = new MimeMessage(sesion);
                      mensaje.setFrom(new InternetAddress(usuario));
                      mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                      mensaje.setSubject(asunto);
                      mensaje.setText(contenido);

                      // Enviar el mensaje
                      Transport.send(mensaje);
                      System.out.println("Correo enviado correctamente");

                  } catch (MessagingException e) {
                      System.out.println(e.getMessage());
                  }catch (Exception e){
                      System.out.println(e.getMessage());
                  }
              }catch (Exception e){
                  System.out.println(e.getMessage());
              }

          }
      }).start();

    }
}
