package io.github.turtlehunter.ElCircoServer;

import io.github.turtlehunter.ElCircoServer.objects.*;
import org.json.simple.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class HttpsServer extends ServerResource {
    @Get("txt")
    public String restGet() {
        String resourceURI = getReference().toString();
        String rootURI = getRootRef().toString();
        String routedPart = getReference().getBaseRef().toString();
        String remainingPart = getReference().getRemainingPart();
        String command = "";
        String str = "";

        //1 localhost:9080/api/isOnline
        //2 localhost:9080/api/isOnline/
        //3 localhost:9080/api/usuario?usuarioID=something
        //4 localhost:9080/api/
        //5 localhost:9080/api
        if (!remainingPart.isEmpty()) { //solves 5
            if (remainingPart.startsWith("/")) { //solves 4
                str = remainingPart.substring(1); //removes first /
                if(str.contains("?")) {
                    int spaceIndex = str.indexOf("?");
                    if (spaceIndex != -1) {
                        command = str.substring(0, spaceIndex); //solves 4
                    }
                } else {
                    if(str.charAt(str.length()-1)=='/') {
                        str = str.substring(0, str.length() - 1); //solves 2
                    }
                    command = str; //solves 1, and sets for 2
                }
            }
        }
        if(command.isEmpty()) command = str;
        String retStr = null;
        switch (command) {
            case "isOnline":
                return createJson(
                        "status", "OK",
                        "content", "Online",
                        "serverVersion", Main.serverVersion,
                        "clientVersion", Main.clientVersion);
            case "tarea":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String recordatorioID = parsed.get("recordatorioID");
                    JSONObject json = new JSONObject(new Tarea(recordatorioID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            case "prueba":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String recordatorioID = parsed.get("recordatorioID");
                    JSONObject json = new JSONObject(new Prueba(recordatorioID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            case "juntada":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String recordatorioID = parsed.get("recordatorioID");
                    JSONObject json = new JSONObject(new Juntada(recordatorioID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            case "usuario":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String usuarioID = parsed.get("usuarioID");
                    JSONObject json = new JSONObject(new Usuario(usuarioID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            case "mensaje":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String mensajeID = parsed.get("mensajeID");
                    JSONObject json = new JSONObject(new Mensaje(mensajeID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            case "grupo":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String grupoID = parsed.get("grupoID");
                    JSONObject json = new JSONObject(new Grupo(grupoID).toMap());
                    retStr = json.toJSONString();
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return retStr == null ? createJson(
                        "status", "FAIL",
                        "content", "Object doesnt exists") : createJson(
                        "status", "OK",
                        "content", retStr);
            default:
                return error404();
        }
    }

    @Put("json")
    public String restPut() {
        String resourceURI = getReference().toString();
        String rootURI = getRootRef().toString();
        String routedPart = getReference().getBaseRef().toString();
        String remainingPart = getReference().getRemainingPart();

        String[] str = remainingPart.split("/");
        System.out.println(Arrays.toString(str));
        if(str.length < 2) return error404();
        String command = str[1];
        String retStr = null;
        switch (command) {
            case "tarea":

        }
        return null;
    }

    private String error404() {
        return createJson("status", "FAIL", "content", "404");
    }


    public String createJson(final String... args) {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("String, String pairs are required, but there seems to be an odd one out");

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        for (int i = 0; i < args.length; i += 2) {
            hashMap.put(args[i], args[i + 1]);
        }
        return new JSONObject(hashMap).toJSONString();
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

}
