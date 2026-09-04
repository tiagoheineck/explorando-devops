package br.ifc.edu.sample.project.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping({"/", "/health"})
    public Map<String, String> hello() {
        log.info("Requisição recebida no endpoint raiz");
        return Map.of("status", "UP", "message", "Aplicação Spring Boot funcionando corretamente.");
    }

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        log.info("Endpoint /hello acessado com sucesso.");
        return Map.of("message", "Hello from Spring Boot + Observability stack!");
    }

    @GetMapping("/simulate")
    public Map<String, String> simulateTraffic() {
        log.info("Simulando carga normal no serviço.");
        log.warn("Alerta simulado: latência acima do limite esperado.");

        try {
            int result = 10 / 0;
            log.info("Resultado da operação: {}", result);
        } catch (ArithmeticException ex) {
            log.error("Erro simulado durante processamento da requisição.", ex);
        }

        return Map.of(
            "status", "ok",
            "message", "Eventos de logs INFO, WARN e ERROR gerados para observabilidade."
        );
    }
}