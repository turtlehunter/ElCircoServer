package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.turtlehunter.ElCircoServer.Main;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Grupo implements Serializable{
    public UUID grupoID;
    public String name;
    public ArrayList<UUID> usuarios;

    public Grupo(String test, UUID uuid, ArrayList<UUID> usuarios) {
        this.grupoID = uuid;
        this.name = test;
        this.usuarios = usuarios;
    }

    public Grupo(Map<String, String> te) {
        this.name = te.get("name");
        this.usuarios = new Gson().fromJson(te.get("usuarios"), new TypeToken<ArrayList<Grupo>>() {
        }.getType());
        this.grupoID = UUID.fromString(te.get("grupoID"));
    }

    public UUID getGrupoID() {
        return grupoID;
    }

    public void setGrupoID(UUID grupoID) {
        this.grupoID = grupoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UUID> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<UUID> usuarios) {
        this.usuarios = usuarios;
    }
    public void addUsuario(UUID usuario) {
        this.usuarios.add(usuario);
    }

    public void removeUsuario(UUID usuario) {
        this.usuarios.remove(usuario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Grupo grupo = (Grupo) o;

        return new EqualsBuilder()
                .append(grupoID, grupo.grupoID)
                .append(name, grupo.name)
                .append(usuarios, grupo.usuarios)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(grupoID)
                .append(name)
                .append(usuarios)
                .toHashCode();
    }
    public Map toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("usuarios", new Gson().toJson(usuarios));
        map.put("grupoID", grupoID.toString());
        return map;
    }

    public Grupo(String grupoID) {
        new Grupo(Main.database.grupoDB.get(UUID.fromString(grupoID)));
    }

    public Grupo(Grupo grupo) {
        this.setName(grupo.getName());
        this.setUsuarios(grupo.getUsuarios());
        this.setGrupoID(grupo.getGrupoID());
    }
}
