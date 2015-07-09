package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.turtlehunter.ElCircoServer.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Prueba extends Recordatorio {

    public Prueba(Map<String, String> te) {
        this.date = te.get("date");
        this.description = te.get("description");
        this.grupos = new Gson().fromJson(te.get("grupos"), new TypeToken<ArrayList<Grupo>>() {
        }.getType());
        this.recordatorioID = UUID.fromString(te.get("recordatorioID"));
        this.titulo = te.get("materia");
    }

    public Map toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("date", date);
        map.put("description", description);
        map.put("grupos", new Gson().toJson(grupos));
        map.put("recordatorioID", recordatorioID.toString());
        map.put("materia", titulo);
        return map;
    }

    public Prueba(Prueba prueba) {
        this.setDate(prueba.getDate());
        this.setDescription(prueba.getDescription());
        this.setGrupos(prueba.getGrupos());
        this.setRecordatorioID(prueba.getRecordatorioID());
        this.setTitulo(prueba.getTitulo());
    }

    public Prueba(String recordatorioID) {
        new Prueba(Main.database.pruebaDB.get(UUID.fromString(recordatorioID)));
    }

}
