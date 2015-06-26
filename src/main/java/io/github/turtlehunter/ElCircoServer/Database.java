package io.github.turtlehunter.ElCircoServer;

import io.github.turtlehunter.ElCircoServer.objects.*;
import org.mapdb.*;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

public class Database {

    public DB db;
    public ConcurrentNavigableMap<UUID, Tarea> tareasDB;
    public ConcurrentNavigableMap<UUID, Prueba> pruebaDB;
    public ConcurrentNavigableMap<UUID, Juntada> juntadaDB;
    public ConcurrentNavigableMap<UUID, Grupo> grupoDB;
    public ConcurrentNavigableMap<UUID, Usuario> usuarioDB;
    public ConcurrentNavigableMap<UUID, Mensaje> mensajeDB;
    public ConcurrentNavigableMap<UUID, UUID> tokenDB;

    public Database() {
        db = DBMaker.newFileDB(new File("elcirco.db"))
                .closeOnJvmShutdown()
                .encryptionEnable("fillme")
                .make();
        tareasDB = db.createTreeMap("tareasdb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        pruebaDB = db.createTreeMap("pruebadb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        juntadaDB = db.createTreeMap("juntadadb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        grupoDB = db.createTreeMap("grupodb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        usuarioDB = db.createTreeMap("usuariodb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        mensajeDB = db.createTreeMap("mensajedb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.JAVA)
                .makeOrGet();
        tokenDB = db.createTreeMap("tokendb")
                .keySerializerWrap(Serializer.UUID)
                .valueSerializer(Serializer.UUID)
                .makeOrGet();
    }

    public void compact() {
        db.compact();
    }

    public void close() {
        db.close();
    }

    public void commit() {
        db.commit();
    }
}
