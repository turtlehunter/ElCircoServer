package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.turtlehunter.ElCircoServer.Main;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Juntada extends Recordatorio {
    public UUID recordatorioID;
    public String titulo;
    public String description;
    public String direccion;
    public String date;
    public ArrayList<UUID> grupos;

    public Juntada(Map<String, String> te) {
        this.date = te.get("date");
        this.description = te.get("description");
        this.grupos = new Gson().fromJson(te.get("grupos"), new TypeToken<ArrayList<Grupo>>() {
        }.getType());
        this.recordatorioID = UUID.fromString(te.get("recordatorioID"));
        this.titulo = te.get("titulo");
        this.direccion = te.get("dirrecion");
    }

    public UUID getRecordatorioID() {
        return recordatorioID;
    }

    public void setRecordatorioID(UUID recordatorioID) {
        this.recordatorioID = recordatorioID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<UUID> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<UUID> grupos) {
        this.grupos = grupos;
    }

    public void addGrupo(UUID grupo) {
        this.grupos.add(grupo);
    }

    public void removeGrupo(UUID grupo) {
        this.grupos.remove(grupo);
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Juntada juntada = (Juntada) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(recordatorioID, juntada.recordatorioID)
                .append(titulo, juntada.titulo)
                .append(description, juntada.description)
                .append(direccion, juntada.direccion)
                .append(date, juntada.date)
                .append(grupos, juntada.grupos)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(recordatorioID)
                .append(titulo)
                .append(description)
                .append(direccion)
                .append(date)
                .append(grupos)
                .toHashCode();
    }
    public Map toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("date", date);
        map.put("description", description);
        map.put("grupos", new Gson().toJson(grupos));
        map.put("recordatorioID", recordatorioID.toString());
        map.put("titulo", titulo);
        map.put("direccion", direccion);
        return map;
    }

    public Juntada(String recordatorioID) {
        new Juntada(Main.database.juntadaDB.get(UUID.fromString(recordatorioID)));
    }

    public Juntada(Juntada juntada) {
        this.setDate(juntada.getDate());
        this.setDescription(juntada.getDescription());
        this.setGrupos(juntada.getGrupos());
        this.setRecordatorioID(juntada.getRecordatorioID());
        this.setTitulo(juntada.getTitulo());
        this.setDireccion(juntada.getDireccion());
    }

}
