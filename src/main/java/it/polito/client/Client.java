package it.polito.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;


/***
 *  Classe per il client con il quale effettuare le richieste al server e testarlo.
 */
public class Client {

    public int login(String username, String password) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = mapper.createArrayNode();

            /**
             * Create three JSON Objects objectNode1
             * Add all these three objects in the array
             */

            ObjectNode objectNode1 = mapper.createObjectNode();
            objectNode1.put("username", username);
            objectNode1.put("password", password);
            /**
             * Array contains JSON Objects
             */
            arrayNode.add(objectNode1);

            HttpClient client = HttpClient.
                    newBuilder().
                    build();

            HttpRequest req = HttpRequest.
                    newBuilder().
                    header("Content-Type", "application/json").
                    uri(new URI("http://localhost:8080/login")).
                    POST(HttpRequest.BodyProcessor.fromString(arrayNode.toString())).
                    build();

            HttpResponse res = client.send(req, HttpResponse.BodyHandler.asString());
            int statusCode = res.statusCode();
            return statusCode;
        } catch (Exception e) {
            throw new RuntimeException(e); // modificare e gestire meglio le eccezioni
        }
    }

    public int logout(String username, String password) {
        try {
            HttpClient client = HttpClient.
                    newBuilder().
                    build();


            HttpRequest req = HttpRequest.
                    newBuilder().
                    uri(new URI("http://localhost:8080/logout")).
                    GET().
                    build();

            HttpResponse res = client.send(req, HttpResponse.BodyHandler.asString());
            int statusCode = res.statusCode();
            return statusCode;
        } catch (Exception e) {
            throw new RuntimeException(e); // modificare e gestire meglio le eccezioni
        }
    }

    public int sendPosition(double lat, double lon, long time) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = mapper.createArrayNode();

            ObjectNode objectNode1 = mapper.createObjectNode();
            objectNode1.put("lat", lat);
            objectNode1.put("lon", lon);
            objectNode1.put("time", time);
            /**
             * Array contains JSON Objects
             */
            arrayNode.add(objectNode1);

            HttpClient client = HttpClient.
                    newBuilder().
                    build();

            HttpRequest req = HttpRequest.
                    newBuilder().
                    header("Content-Type", "application/json").
                    uri(new URI("http://localhost:8080/")).
                    POST(HttpRequest.BodyProcessor.fromString(arrayNode.toString())).
                    build();

            HttpResponse res = client.send(req, HttpResponse.BodyHandler.asString());
            int statusCode = res.statusCode();
            return statusCode;
        } catch (Exception e) {
            throw new RuntimeException(e); // modificare e gestire meglio le eccezioni
        }
    }

    public int getPosition(long before, long after) {
        try {
            HttpClient client = HttpClient.
                    newBuilder().
                    build();

            HttpRequest req = HttpRequest.
                    newBuilder().
                    header("Content-Type", "application/json").
                    uri(new URI("http://localhost:8080/position")).
                    GET().
                    build();

            HttpResponse res = client.send(req, HttpResponse.BodyHandler.asString());
            int statusCode = res.statusCode();
            return statusCode;
        } catch (Exception e) {
            throw new RuntimeException(e); // modificare e gestire meglio le eccezioni
        }
    }
}