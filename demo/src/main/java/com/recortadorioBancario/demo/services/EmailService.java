package com.recortadorioBancario.demo.services;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String cuerpoPlano) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            // true = multipart (text + html)
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

            // 1) FROM con display name
            // IMPORTANTE: el address debe ser la cuenta autenticada en SMTP (o el relay puede rechazarlo)
            InternetAddress from = new InternetAddress("recordatoriobancario@gmail.com", "RecordatorioBancario");
            helper.setFrom(from);

            // 2) To y subject
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            // 3) Reply-To (opcional)
            helper.setReplyTo(new InternetAddress("recordatoriobancario@gmail.com", "RecordatorioBancario"));

            // 4) Texto plano + HTML simple (evitar HTML inseguro)
            String html = """
                <div style="font-family:Arial,Helvetica,sans-serif;font-size:14px;line-height:1.5;color:#222">
                  <p>Hola,</p>
                  <p>%s</p>
                  <hr/>
                  <p style="font-size:12px;color:#666">
                    Si no deseas recibir estos recordatorios, responde a este correo con "STOP" o contacta a RecordatorioBancario.
                  </p>
                </div>
            """.formatted(escapeHtml(cuerpoPlano));

            helper.setText(cuerpoPlano, html);

            // 5) Encabezados recomendados
            mime.addHeader("X-Priority", "3");
            mime.addHeader("X-Mailer", "RecordatorioBancario-App");
            mime.addHeader("List-Unsubscribe", "<mailto:recordatoriobancario@gmail.com>");

            mailSender.send(mime);
            System.out.println("✅ Correo enviado a " + destinatario);
        } catch (Exception e) {
            // Registrar con detalle
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeHtml(String s) {
        return s == null ? "" : s
                .replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;");
    }
}