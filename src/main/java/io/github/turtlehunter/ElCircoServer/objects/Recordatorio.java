package io.github.turtlehunter.ElCircoServer.objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Recordatorio {
    public UUID recordatorioID;
    public String titulo;
    public String description;
    public String date;
    public ArrayList<UUID> grupos;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Recordatorio)) return false;

        Recordatorio that = (Recordatorio) o;

        return new EqualsBuilder()
                .append(recordatorioID, that.recordatorioID)
                .append(titulo, that.titulo)
                .append(description, that.description)
                .append(date, that.date)
                .append(grupos, that.grupos)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(recordatorioID)
                .append(titulo)
                .append(description)
                .append(date)
                .append(grupos)
                .toHashCode();
    }

    public static UUID nextUUID() {
        return UUID.randomUUID();
    }
}
