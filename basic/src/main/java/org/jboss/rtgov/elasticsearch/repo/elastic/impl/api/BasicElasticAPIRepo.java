package org.jboss.rtgov.elasticsearch.repo.elastic.impl.api;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.jboss.rtgov.elasticsearch.repo.ElasticSearchException;
import org.jboss.rtgov.elasticsearch.repo.elastic.AbstractElasticRepo;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 07/02/14
 * Time: 23:47
 * //  its possible to start a node in my application ..   http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/client.html
 * TODO, Define a pool of clients. The current design means each serive instance results in an additional connection
 */
public class BasicElasticAPIRepo extends AbstractElasticRepo<String, Serializable> {

    Logger logger = Logger.getLogger(AbstractElasticRepo.class.getName());

    private Client client;

    @Override
    public void init() throws Exception {
        super.init();

        // When you start a Node, the most important decision is whether it should hold data or not. In other words, should indices and shards be allocated to it. Many times we would like to have the clients just be clients, without shards being allocated to them. This is simple to configure by setting either node.data setting to false or node.client to true (the NodeBuilder respective helper methods on it)
        //    Node node = NodeBuilder.nodeBuilder().client(true).node();
        //   Client client = node.client();

// on shutdown

        //    node.close();

        // TODO definie a form initalization concept for the  elastic Search AP I
         // TODO, Define a time to live on the client.

        //  Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "localtestsearch").build();
        client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    @Override
    public void persist(Serializable document, String id) throws ElasticSearchException {

        try {


            IndexResponse indexResponse = client.prepareIndex(index, type, id).setSource(convertTypeToJson(document)).execute().actionGet();

        }catch (Exception e){
            logger.log( Level.SEVERE, "[/"+index+"/"+type+"] Could not store  json document from Type [" + document.getClass().getName() + "] ");
             throw  new ElasticSearchException("[/"+index+"/"+type+"] Could not store  json document from Type [" + document.getClass().getName() + "] ", e);
        } finally{
            logger.log(Level.FINER," Possibly need to close the connection here. ");
         // client.close();
        }
    }

    @Override
    public void remove(Serializable document, String id) {
        logger.warning(" REMOVE METHOD NOT IMPLEMENTED  ");
        throw new UnsupportedOperationException(" REMOVE METHOD NOT IMPLEMENTED  ");
    }
}
