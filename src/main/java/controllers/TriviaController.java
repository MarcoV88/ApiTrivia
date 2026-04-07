package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import httpRequest.PeticionHttp;
import service.ServiceTrivia;

import java.io.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TriviaController {

    public void handle(HttpExchange exchange) throws IOException, InterruptedException{
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        ServiceTrivia serviceTrivia = new ServiceTrivia();

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {

            //  GET
            if (method.equalsIgnoreCase("GET")) {

                if (path.startsWith("/trivia/dificultad/easy")) {
                    PeticionHttp peticionHttp = new PeticionHttp();
                    HttpResponse<String> response = peticionHttp.peticion("https://opentdb.com/api.php?amount=10&difficulty=easy");
                    JsonArray resultado = serviceTrivia.formato(response);
                    sendResponse(exchange, 200, resultado.toString());
                    return;
                }

                if (path.startsWith("/trivia/dificultad/medium")) {
                    PeticionHttp peticionHttp = new PeticionHttp();
                    HttpResponse<String> response = peticionHttp.peticion("https://opentdb.com/api.php?amount=10&difficulty=medium");
                    JsonArray resultado = serviceTrivia.formato(response);
                    sendResponse(exchange, 200, resultado.toString());
                    return;
                }

                if (path.startsWith("/trivia/dificultad/hard")) {
                    PeticionHttp peticionHttp = new PeticionHttp();
                    HttpResponse<String> response = peticionHttp.peticion("https://opentdb.com/api.php?amount=10&difficulty=hard");
                    JsonArray resultado = serviceTrivia.formato(response);
                    sendResponse(exchange, 200, resultado.toString());
                    return;
                }
            }

            //  POST (NUEVO)
            else if (method.equalsIgnoreCase("POST")) {

                if (path.equals("/trivia/verificar")) {
                    String body = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );
                    System.out.println("Body recibido: " + body);
                    sendResponse(exchange, 200, body);
                }
                else {
                    sendResponse(exchange, 404, "Endpoint POST no válido");
                }
            }

            else {
                sendResponse(exchange, 405, "Método no permitido");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error en DogsController");
        }




        sendResponse(exchange, 404, "No encontrada la ruta");
    }

        private void sendResponse (HttpExchange exchange,int status, String body) throws IOException {

            exchange.getResponseHeaders().add("Content-Type", "application/json");

            byte[] bytes = body.getBytes();

            exchange.sendResponseHeaders(status, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

    public static class ApiTrivia {
        int response_code;
        Results results;

        public int getResponse_code() {
            return response_code;
        }

        public void setResponse_code(int response_code) {
            this.response_code = response_code;
        }

        public Results getResults() {
            return results;
        }

        public void setResults(Results results) {
            this.results = results;
        }
    }
    public static class Results {
        String type;
        String difficulty;
        String category;
        String question;
        String correct_answer;
        List<String> incorrect_answers;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getCorrect_answer() {
            return correct_answer;
        }

        public void setCorrect_answer(String correct_answer) {
            this.correct_answer = correct_answer;
        }

        public List<String> getIncorrect_answers() {
            return incorrect_answers;
        }

        public void setIncorrect_answers(List<String> incorrect_answers) {
            this.incorrect_answers = incorrect_answers;
        }
    }
}
