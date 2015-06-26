package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import io.github.turtlehunter.ElCircoServer.Main;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Grupo {
    public UUID grupoID;
    public String name;
    public ArrayList<Usuario> usuarios;

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

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario) {
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
