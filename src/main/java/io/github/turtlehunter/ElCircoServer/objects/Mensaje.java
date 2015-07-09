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

public class Mensaje {
    public UUID mensajeID;
    public String titulo;
    public String description;
    public String from;
    public ArrayList<UUID> grupos;

    public Mensaje(Map<String, String> te) {
        this.from = te.get("from");
        this.description = te.get("description");
        this.grupos = new Gson().fromJson(te.get("grupos"), new TypeToken<ArrayList<Grupo>>() {
        }.getType());
        this.mensajeID = UUID.fromString(te.get("mensajeID"));
        this.titulo = te.get("titulo");
    }

    public UUID getMensajeID() {
        return mensajeID;
    }

    public void setMensajeID(UUID mensajeID) {
        this.mensajeID = mensajeID;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Mensaje mensaje = (Mensaje) o;

        return new EqualsBuilder()
                .append(mensajeID, mensaje.mensajeID)
                .append(titulo, mensaje.titulo)
                .append(description, mensaje.description)
                .append(from, mensaje.from)
                .append(grupos, mensaje.grupos)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mensajeID)
                .append(titulo)
                .append(description)
                .append(from)
                .append(grupos)
                .toHashCode();
    }
    public Map toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("from", from);
        map.put("description", description);
        map.put("grupos", new Gson().toJson(grupos));
        map.put("mensajeID", mensajeID.toString());
        map.put("titulo", titulo);
        return map;
    }

    public Mensaje(String mensajeID) {
        new Mensaje(Main.database.mensajeDB.get(UUID.fromString(mensajeID)));
    }

    public Mensaje(Mensaje mensaje) {
        this.setFrom(mensaje.getFrom());
        this.setDescription(mensaje.getDescription());
        this.setGrupos(mensaje.getGrupos());
        this.setMensajeID(mensaje.getMensajeID());
        this.setTitulo(mensaje.getTitulo());
    }
}
