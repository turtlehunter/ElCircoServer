package io.github.turtlehunter.ElCircoServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.turtlehunter.ElCircoServer.objects.*;
import org.jivesoftware.smack.SmackException;
import org.json.simple.JSONObject;
import org.restlet.resource.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            case "sync":
                try {
                    Map<String, String> parsed = splitQuery(new URL(resourceURI));
                    String usuarioID = parsed.get("usuarioID");
                    Usuario usuario = new Usuario(usuarioID);
                    ArrayList<Mensaje> mensajes = new ArrayList<>();
                    ArrayList<Tarea> tareas = new ArrayList<>();
                    ArrayList<Prueba> pruebas = new ArrayList<>();
                    ArrayList<Juntada> juntadas = new ArrayList<>();
                    for(UUID uuid: Main.database.mensajeDB.keySet()) {
                        Mensaje mensaje = Main.database.mensajeDB.get(uuid);
                        for(UUID uuid1: mensaje.getGrupos()) {
                            Grupo grupo = Main.database.grupoDB.get(uuid1);
                            if(grupo.getUsuarios().contains(usuario.getUsuarioID())) {
                                mensajes.add(mensaje);
                                break;
                            }
                        }
                    }
                    for(UUID uuid: Main.database.tareasDB.keySet()) {
                        Tarea tarea = Main.database.tareasDB.get(uuid);
                        for(UUID uuid1: tarea.getGrupos()) {
                            Grupo grupo = Main.database.grupoDB.get(uuid1);
                            if(grupo.getUsuarios().contains(usuario.getUsuarioID())) {
                                tareas.add(tarea);
                                break;
                            }
                        }
                    }
                    for(UUID uuid: Main.database.pruebaDB.keySet()) {
                        Prueba prueba = Main.database.pruebaDB.get(uuid);
                        for(UUID uuid1: prueba.getGrupos()) {
                            Grupo grupo = Main.database.grupoDB.get(uuid1);
                            if(grupo.getUsuarios().contains(usuario.getUsuarioID())) {
                                pruebas.add(prueba);
                                break;
                            }
                        }
                    }
                    for(UUID uuid: Main.database.juntadaDB.keySet()) {
                        Juntada juntada = Main.database.juntadaDB.get(uuid);
                        for(UUID uuid1: juntada.getGrupos()) {
                            Grupo grupo = Main.database.grupoDB.get(uuid1);
                            if (grupo.getUsuarios().contains(usuario.getUsuarioID())) {
                                juntadas.add(juntada);
                                break;
                            }
                        }
                    }
                    return createJson("status", "OK",
                                      "mensajes", new Gson().toJson(mensajes, new TypeToken<ArrayList<Mensaje>>(){}.getType()),
                                      "tareas", new Gson().toJson(tareas, new TypeToken<ArrayList<Tarea>>(){}.getType()),
                                      "pruebas", new Gson().toJson(pruebas, new TypeToken<ArrayList<Prueba>>(){}.getType()),
                                      "juntadas", new Gson().toJson(juntadas, new TypeToken<ArrayList<Juntada>>(){}.getType())
                    );
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    Main.logger.warn("Exeption parsing URL", e);
                }
                return createJson("status", "FAIL",
                        "content", "User not valid");
            default:
                return error404();
        }
    }

    @Post("json")
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
            case "login":
                try {
                    String username = splitQuery(new URL(resourceURI)).get("username");
                    String password = hash(splitQuery(new URL(resourceURI)).get("password"));
                    for(UUID uuid: Main.database.usuarioDB.keySet()) {
                        Usuario usuario = Main.database.usuarioDB.get(uuid);
                        if(usuario.getUsuario().equals(username)) {
                            if(usuario.getPassword().equals(password)) {
                                return createJson("status", "OK", "content", usuario.getUsuarioID().toString());
                            }
                        }
                    }
                    return createJson("status", "FAIL", "content", "Invalid username or password");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "tarea":
                try {
                    Main.database.tareasDB.put(Recordatorio.nextUUID(), new Tarea(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "prueba":
                try {
                    Main.database.pruebaDB.put(Recordatorio.nextUUID(), new Prueba(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "juntada":
                try {
                    Main.database.juntadaDB.put(Recordatorio.nextUUID(), new Juntada(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "usuario":
                try {
                    Main.database.usuarioDB.put(Recordatorio.nextUUID(), new Usuario(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "mensaje":
                try {
                    Main.database.mensajeDB.put(Recordatorio.nextUUID(), new Mensaje(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "grupo":
                try {
                    Main.database.grupoDB.put(Recordatorio.nextUUID(), new Grupo(splitQuery(new URL(resourceURI))));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
        }
        return null;
    }

    private String hash(String password) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    private String convertToHex(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte aRaw : raw) {
            sb.append(Integer.toString((aRaw & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Delete("txt")
    public String restDelete() {
        String resourceURI = getReference().toString();
        String rootURI = getRootRef().toString();
        String routedPart = getReference().getBaseRef().toString();
        String remainingPart = getReference().getRemainingPart();

        String[] str = remainingPart.split("/");
        System.out.println(Arrays.toString(str));
        if (str.length < 2) return error404();
        String command = str[1];
        String retStr = null;
        switch (command) {
            case "tarea":
                try {
                    sync(Main.database.tareasDB.get(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID"))).grupos);
                    Main.database.tareasDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID")));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "prueba":
                try {
                    sync(Main.database.pruebaDB.get(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID"))).grupos);
                    Main.database.pruebaDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID")));
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "juntada":
                try {
                    sync(Main.database.juntadaDB.get(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID"))).grupos);
                    Main.database.juntadaDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID")));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "usuario":
                try {
                    Main.database.usuarioDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("usuarioID")));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "mensaje":
                try {
                    sync(Main.database.mensajeDB.get(UUID.fromString(splitQuery(new URL(resourceURI)).get("recordatorioID"))).grupos);
                    Main.database.mensajeDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("mensajeID")));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
            case "grupo":
                try {
                    Main.database.grupoDB.remove(UUID.fromString(splitQuery(new URL(resourceURI)).get("grupoID")));
                    return createJson("status", "OK", "content", "Done");
                } catch (UnsupportedEncodingException e) {
                    Main.logger.trace("UnsupportedEncoding", e);
                    return createJson("status", "FAIL", "content", "UnsupportedEncoding");
                } catch (MalformedURLException e) {
                    Main.logger.trace("MalformedURL", e);
                    return createJson("status", "FAIL", "content", "MalformedURL");
                }
        }
        return createJson("status", "FAIL", "content", "Internal error, probably your fault");

    }

    private void sync(ArrayList<UUID> grupos) {
        for(UUID uuid: grupos) {
            Grupo grupo = Main.database.grupoDB.get(uuid);
            for(UUID user: grupo.usuarios) {
                Usuario usuario = Main.database.usuarioDB.get(user);
                send(usuario.GCM, "Sync", "syncme");
            }
        }
    }

    private void send(String gcm, String content, String collapseKey) {
        String messageId = Main.activeConnection.nextMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("Message", content);
        payload.put("CCS", "El circo");
        payload.put("EmbeddedMessageId", messageId);
        String message = SmackCcsClient.createJsonMessage(gcm, messageId, payload,
                collapseKey, true);

        try {
            Main.activeConnection.sendDownstreamMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
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
