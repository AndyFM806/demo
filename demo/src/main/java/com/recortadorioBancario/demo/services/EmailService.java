package com.recortadorioBancario.demo.services;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Enviar correo con HTML + texto plano + headers para mejorar entregabilidad
    public void enviarCorreo(String destinatario, String asunto, String cuerpoPlano) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

            // IMPORTANTÍSIMO: el From debe ser la cuenta autenticada en SMTP (tu Gmail)
            helper.setFrom("recordatoriobancario@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            // HTML + texto plano
            String html = """
                <div style="font-family:Arial,Helvetica,sans-serif;font-size:14px;line-height:1.5">
                  <p>Hola,</p>
                  <p>%s</p>
                  <hr/>
                  <p style="font-size:12px;color:#555">
                    Si no deseas recibir estos recordatorios, puedes desuscribirte respondiendo a este correo con "STOP".
                  </p>
                </div>
            """.formatted(escapeHtml(cuerpoPlano));

            helper.setText(cuerpoPlano, html);

            // Headers “amistosos”
            mime.addHeader("X-Priority", "3"); // normal (1 alta, 3 normal)
            mime.addHeader("X-Mailer", "RecordatorioBancario-App");
            // Gmail respeta List-Unsubscribe (ayuda a reputación)
            mime.addHeader("List-Unsubscribe", "<mailto:recordatoriobancario@gmail.com>");

            mailSender.send(mime);
            System.out.println("✅ Correo enviado a " + destinatario);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }

    private String escapeHtml(String s) {
        return s == null ? "" : s
                .replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;");
    }
}
