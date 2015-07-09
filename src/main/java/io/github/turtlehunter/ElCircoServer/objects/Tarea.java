package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.turtlehunter.ElCircoServer.Database;
import io.github.turtlehunter.ElCircoServer.Main;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tarea extends Recordatorio {
    public Tarea(String recordatorioID) {
        new Tarea(Main.database.tareasDB.get(UUID.fromString(recordatorioID)));
    }

    public Tarea(Tarea tarea) {
        this.setDate(tarea.getDate());
        this.setDescription(tarea.getDescription());
        this.setGrupos(tarea.getGrupos());
        this.setRecordatorioID(tarea.getRecordatorioID());
        this.setTitulo(tarea.getTitulo());
    }

    public Tarea(Map<String, String> te) {
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
}
