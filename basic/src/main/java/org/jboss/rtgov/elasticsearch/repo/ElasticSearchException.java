package org.jboss.rtgov.elasticsearch.repo;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 13/11/13
 * Time: 23:24
 * To change this template use File | Settings | File Templates.
 */
//todo. Excpetion translation should define error codes that prograte from services
    // todo if service is fatal then there should be no retries :)

public class ElasticSearchException extends Exception {

    public ElasticSearchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ElasticSearchException(String s) {
        super(s);
    }
}

