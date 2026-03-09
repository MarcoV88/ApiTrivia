package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import httpRequest.PeticionHttp;
import service.ServiceTrivia;

import java.io.*;
import java.net.http.HttpResponse;
import java.util.List;

public class TriviaController {

    public void handle(HttpExchange exchange) throws IOException, InterruptedException{
        String path = exchange.getRequestURI().getPath();
        PeticionHttp peticionHttp = new PeticionHttp();
        ServiceTrivia serviceTrivia = new ServiceTrivia();
            if (path.startsWith("/trivia/dificultad/easy")) {
                HttpResponse<String> response = serviceTrivia.peticion("https://opentdb.com/api.php?amount=10&difficulty=easy");
                Gson gson = new Gson();
                JsonObject jsonRaiz = gson.fromJson(response.body(), JsonObject.class);
                JsonArray results = jsonRaiz.getAsJsonArray("results");

                JsonArray data = new JsonArray();
                for (JsonElement element : results) {
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject dataObj = new JsonObject();
                    dataObj.addProperty("question", obj.get("question").getAsString());
                    JsonArray respuestas = obj.get("incorrect_answers").getAsJsonArray();
                    respuestas.add(obj.get("correct_answer").getAsString());
                    dataObj.addProperty("correct_answer", obj.get("correct_answer").getAsString());
                    dataObj.add("answers", respuestas);
                    data.add(dataObj);
                }
                sendResponse(exchange, 200, gson.toJson(data));
            }

            else if (path.startsWith("/trivia/dificultad/medium")) {
                HttpResponse<String> response = peticionHttp.peticion("https://opentdb.com/api.php?amount=10&difficulty=medium");
                sendResponse(exchange, 200, response.body());
            }
            else if (path.startsWith("/trivia/dificultad/hard")) {
                HttpResponse<String> response = peticionHttp.peticion("https://opentdb.com/api.php?amount=10&difficulty=hard");
                sendResponse(exchange, 200, response.body());
            }

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
