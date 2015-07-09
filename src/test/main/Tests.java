import org.junit.Test;
import static org.junit.Assert.*;
import org.restlet.resource.ClientResource;

import java.io.IOException;

public class Tests {
    @Test
    public void isOnlineTest() throws IOException {
        //1 localhost:9080/api/isOnline
        //2 localhost:9080/api/isOnline/
        //3 localhost:9080/api/usuario?usuarioID=something
        //4 localhost:9080/api/
        //5 localhost:9080/api
        //6 localhost:9080/api/notacommand
        //7 localhost:9080/api/usuario?usuarioID=5231b533-ba17-4787-98a3-f2df37de2aD7 (test user)

        assertEquals(new ClientResource("http://localhost:9080/api/isOnline").get().getText(), "{\"serverVersion\":\"1.0\",\"clientVersion\":\"1.0\",\"content\":\"Online\",\"status\":\"OK\"}");
        assertEquals(new ClientResource("http://localhost:9080/api/isOnline/").get().getText(), "{\"serverVersion\":\"1.0\",\"clientVersion\":\"1.0\",\"content\":\"Online\",\"status\":\"OK\"}");
        System.out.println(new ClientResource("http://localhost:9080/api/usuario?usuarioID=\"5231b533-ba17-4787-98a3-f2df37de2aD7\"").get().getText());

    }
}
