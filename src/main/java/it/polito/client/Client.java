package it.polito.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


/***
 *  Classe per il client con il quale effettuare le richieste al server e testarlo.
 */
public class Client {

    public int login(String username, String password)
            throws URISyntaxException, InterruptedException, IOException {
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
    }

    public int logout()
            throws URISyntaxException, InterruptedException, IOException {
        HttpClient client = HttpClient.
                newBuilder().
                build();


        HttpRequest req = HttpRequest.
                newBuilder().
                uri(new URI("http://localhost:8080?logout=true")).
                GET().
                build();

        HttpResponse res = client.send(req, HttpResponse.BodyHandler.asString());
        int statusCode = res.statusCode();
        return statusCode;
    }

    public int sendPosition(double lat, double lon, long time)
            throws URISyntaxException, InterruptedException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("latitude", lat);
        objectNode1.put("longitude", lon);
        objectNode1.put("timestamp", time);
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
    }

    public int getPosition(long before, long after)
            throws URISyntaxException, InterruptedException, IOException {
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
    }
}