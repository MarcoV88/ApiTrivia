package router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TriviaController;

import java.io.IOException;
import java.io.OutputStream;

public class RouterHandler implements HttpHandler {

    private final TriviaController triviaController = new TriviaController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();


        if (path.startsWith("/trivia")) {
            try {
                triviaController.dificultadPreguntasFacil(exchange);
                triviaController.dificultadPreguntasMedia(exchange);
                triviaController.dificultadPreguntasDificil(exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String response = "404 - Ruta no encontrada";
        exchange.sendResponseHeaders(404, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
