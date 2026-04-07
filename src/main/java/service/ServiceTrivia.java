package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;

public class ServiceTrivia {

    public JsonArray formato( HttpResponse<String>  response) throws IOException, InterruptedException {
        Random rand = new Random();
        Gson gson = new Gson();
        JsonObject jsonRaiz = gson.fromJson(response.body(), JsonObject.class);
        JsonArray results = jsonRaiz.getAsJsonArray("results");
        JsonArray data = new JsonArray();
        for (JsonElement element : results) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject dataObj = new JsonObject();
            dataObj.addProperty("Question", obj.get("question").getAsString());
            JsonArray respuestasIncorrectas = obj.getAsJsonArray("incorrect_answers");
            ArrayList<String> respuestasOrdenRandom = new ArrayList<>();
            for (JsonElement respuesta : respuestasIncorrectas) {
                respuestasOrdenRandom.add(respuesta.getAsString());
            }

            String respuestasCorrecta = obj.get("correct_answer").getAsString();
            int posicionAleatoria = rand.nextInt(respuestasOrdenRandom.size() + 1);
            respuestasOrdenRandom.add(posicionAleatoria, respuestasCorrecta);
            JsonArray respuestas = new JsonArray();
            for (String respuesta : respuestasOrdenRandom) {
                respuestas.add(respuesta);
            }

            dataObj.addProperty("Correct_answer", respuestasCorrecta);
            dataObj.add("Answers", respuestas);
            data.add(dataObj);
        }

        return data;
    }
}
