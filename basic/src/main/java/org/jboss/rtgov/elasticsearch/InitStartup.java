package org.jboss.rtgov.elasticsearch;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 09/01/14
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
@Startup
@Singleton
public class InitStartup {
    @PostConstruct
    void init() {
        System.out.println( "Starting up ");


    }
}
