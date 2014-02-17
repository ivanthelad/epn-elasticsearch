package org.jboss.rtgov.elasticsearch.repo.elastic;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.rtgov.elasticsearch.repo.ElasticSearchException;
import org.overlord.rtgov.activity.util.ActivityUtil;
import org.overlord.rtgov.common.service.Service;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 08/02/14
 * Time: 22:10
 */
public abstract class AbstractElasticRepo<K,E> extends Service implements ElasticRepo<K,E>  {

    protected ObjectMapper MAPPER = null;
    //todo . Make configurable Elastic Search url.
    protected String index = null;
    protected String type = null;
    //todo change host and port to be a list of connections as opposed to a single connection.
    protected  String host = null;
    protected  int port = 0;
    Logger logger = Logger.getLogger(AbstractElasticRepo.class.getName());

    protected AbstractElasticRepo() {

    }

    @Override
    public void init() throws Exception {
        super.init();
        if(host ==null || index == null || type == null || port == 0 ){
            logger.severe("Properties not fully set ["+this.toString()+"]");
            throw new IllegalStateException("Properties not fully set ["+this.toString()+"]");
        }
    }

    protected  String convertTypeToJson(E obj) throws ElasticSearchException {
        try {
            logger.info("[/"+index+"/"+type+"] Converting to json document from Type [" + obj.getClass().getName() + "] ");

            return ActivityUtil.objectToJSONString(obj);

        } catch (Exception e) {
            throw new  ElasticSearchException("Failed to convert from object to json String [class:"+obj.getClass().getName()+"]", e);
        }
    }

    @Override
    public String toString() {
        return "AbstractElasticRepo{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
