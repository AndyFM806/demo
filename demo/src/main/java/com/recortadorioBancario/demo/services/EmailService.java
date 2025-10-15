package com.recortadorioBancario.demo.services;


import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    // Tu API Key de Resend
    private static final String RESEND_API_KEY = "RESEND_API_KEY";

    public void enviarCorreo(String destinatario, String asunto, String cuerpoPlano) {
        try {
            // ============================
            // Plantilla HTML profesional
            // ============================
            String html = """
                <div style="font-family:Arial,Helvetica,sans-serif;
                            background:#f4f6f8;
                            color:#333;
                            padding:20px;
                            border-radius:10px;
                            max-width:600px;
                            margin:auto;">
                    <h2 style="color:#2a7ae4;text-align:center;">📢 Recordatorio de Pago</h2>
                    <p>Hola,</p>
                    <p>%s</p>
                    <p>Por favor, realiza el pago antes de la fecha indicada para evitar cargos adicionales.</p>

                    <div style="border-top:1px solid #ddd;margin-top:20px;padding-top:10px;font-size:12px;color:#777;text-align:center;">
                        © 2025 Recordatorio Bancario — Este es un mensaje automático.<br/>
                        Si no deseas recibir recordatorios, responde con “STOP”.
                    </div>
                </div>
            """.formatted(escapeHtml(cuerpoPlano));

            // ============================
            // Construir JSON del correo
            // ============================
            String json = """
                {
                  "from": "Recordatorio Bancario <notificaciones@resend.dev>",
                  "to": ["%s"],
                  "subject": "%s",
                  "html": "%s"
                }
            """.formatted(destinatario, escapeJson(asunto), escapeJson(html));

            // ============================
            // Enviar a la API de Resend
            // ============================
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer " + RESEND_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("✅ Resend API status: " + response.statusCode());
            System.out.println("📨 Response body: " + response.body());
        } catch (Exception e) {
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

    private String escapeJson(String s) {
        return s == null ? "" : s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
