package org.jboss.rtgov.elasticsearch.repo;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 12/11/13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class TestClient {
    protected static ObjectMapper MAPPER=null;

    private static void doSend(String text){
        //ClientRequest clientRequest = new ClientRequest();

        // this initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());


        //ActivityRest client = ProxyFactory.create(ActivityRest.class, "http://localhost:9200");

        System.out.println( "Sending ,,.,,... ");

        try {
          // ClientResponse<String> t =  client.putBasic(text);
         //   System.out.println(t.getResponseStatus());
           // System.out.println(t.toString());
           // System.out.println(t.getEntity());
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
 //       String temp ="{\"type\":\"ResponseSent\",\"replyToId\":\"ID-contractSave-4686-1\",\"operation\":\"contractImport\",\"interface\":\"com.allianz.gep.integration.vb.contractHandler/ebws/vb/contractImport\",\"serviceType\":\"ESB\",\"properties\":{\"total\":\"240.0\",\"customer\":\"Fred\"},\"context\":[{\"value\":\"4686-1\",\"type\":\"Link\",\"timeframe\":10000},{\"value\":\"ID-contractSave-4686-7\",\"type\":\"Message\"},{\"value\":\"4686\",\"type\":\"Conversation\"}],\"timestamp\":1384272433,\"unitId\":\"53ad5ac0-f02d-4aee-b0cc-7e0c5eac8a2c\"}";
   //     doSend(temp);
        MAPPER=new ObjectMapper();
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());


      //  ActivityRest client = ProxyFactory.create(ActivityRest.class, "http://localhost:9200");
        try {
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}


