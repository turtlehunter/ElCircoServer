package io.github.turtlehunter.ElCircoServer.objects;

import com.google.gson.Gson;
import io.github.turtlehunter.ElCircoServer.Main;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Usuario {
    public UUID usuarioID;
    public String GCM;
    public String usuario;
    public String password;
    public String realName;
    public ArrayList<Grupo> grupos;

    public UUID getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(UUID usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getGCM() {
        return GCM;
    }

    public void setGCM(String GCM) {
        this.GCM = GCM;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void addGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    public void removeGrupo(Grupo grupo) {
        this.grupos.remove(grupo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario1 = (Usuario) o;

        return new EqualsBuilder()
                .append(usuarioID, usuario1.usuarioID)
                .append(GCM, usuario1.GCM)
                .append(usuario, usuario1.usuario)
                .append(password, usuario1.password)
                .append(realName, usuario1.realName)
                .append(grupos, usuario1.grupos)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(usuarioID)
                .append(GCM)
                .append(usuario)
                .append(password)
                .append(realName)
                .append(grupos)
                .toHashCode();
    }

    public Map toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("usuarioID", usuarioID.toString());
        map.put("GCM", GCM);
        map.put("grupos", new Gson().toJson(grupos));
        map.put("usuario", usuario);
        map.put("realName", realName);
        return map;
    }

    public Usuario(Usuario usuario) {
        this.setGCM(usuario.getGCM());
        this.setUsuario(usuario.getUsuario());
        this.setGrupos(usuario.getGrupos());
        this.setUsuarioID(usuario.getUsuarioID());
        this.setPassword(usuario.getPassword());
        this.setRealName(usuario.getRealName());
    }

    public Usuario(String usuarioID) {
        new Usuario(Main.database.usuarioDB.get(UUID.fromString(usuarioID)));
    }

}
