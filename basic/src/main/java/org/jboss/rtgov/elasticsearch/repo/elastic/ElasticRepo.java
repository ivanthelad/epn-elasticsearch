package org.jboss.rtgov.elasticsearch.repo.elastic;


import org.jboss.rtgov.elasticsearch.repo.ElasticSearchException;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 19/11/13
 * Time: 09:55
 * To change this template use File | Settings | File Templates.
 */
public interface ElasticRepo<K, E> {
    void persist(E document, K id) throws ElasticSearchException;
    void remove (E document,K id);
}
