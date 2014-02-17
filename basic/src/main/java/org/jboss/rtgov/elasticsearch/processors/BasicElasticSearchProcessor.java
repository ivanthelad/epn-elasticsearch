package org.jboss.rtgov.elasticsearch.processors;

import org.jboss.rtgov.elasticsearch.repo.ElasticSearchException;
import org.jboss.rtgov.elasticsearch.repo.elastic.ElasticRepo;
import org.jboss.rtgov.elasticsearch.repo.elastic.impl.api.BasicElasticAPIRepo;
import org.mvel2.MVEL;
import org.overlord.rtgov.ep.EventProcessor;
import org.overlord.rtgov.internal.ep.DefaultEPContext;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 03/02/14
 * Time: 10:35
 */
public  class BasicElasticSearchProcessor extends EventProcessor {
    private static final Logger LOG=Logger.getLogger(BasicElasticSearchProcessor.class.getName());

    private static final String BASIC_ELASTIC_REPO = "BasicElasticRepo";
    protected DefaultEPContext _context = null;
    private ElasticRepo basicElasticRestRepo = null;
    private String script=null;
    private Object scriptExpression=null;
    private String correleationScript = null ;

    public Object getScriptExpression() {
        return scriptExpression;
    }

    public void setScriptExpression(Object scriptExpression) {
        this.scriptExpression = scriptExpression;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(script != null && script.equals("NONE"))
            this.script="genrandom.mvel";

        // Load the script
        java.io.InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(script);

        if (is == null) {
            throw new Exception("Unable to locate MVEL script '"+script+"'");
        } else {
            byte[] b=new byte[is.available()];
            is.read(b);
            is.close();

            // Compile expression
            scriptExpression = MVEL.compileExpression(new String(b));

            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Initialized script="+script
                        +" compiled="+scriptExpression);
            }
        }

        _context = new DefaultEPContext(getServices());
        /**
         * expect type SimpleDocumentRepo;
         */
        basicElasticRestRepo = (BasicElasticAPIRepo) _context.getService(BASIC_ELASTIC_REPO);
    }

    @Override
    public  Serializable process(String source, Serializable event, int retriesLeft) throws Exception {
         //   if (!getScript().equals("NONE")) {
                return process(source,  event, retriesLeft, processMvel(source,event,retriesLeft));
         //   } else {
           //     return process(source,  event, retriesLeft, getRandom());
         //   }


    }
    private String processMvel(String source, Serializable event, int retriesLeft){
        java.io.Serializable ret=null;

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Process event '"+event+" from source '"+source
                    +"' on MVEL Event Processor '"+getScript()
                    +"'");
        }

        if (scriptExpression != null) {
            java.util.Map<String,Object> vars=
                    new java.util.HashMap<String, Object>();

            vars.put("source", source);
            vars.put("event", event);
            vars.put("retriesLeft", retriesLeft);
            vars.put("epc", _context);

            synchronized (this) {
                _context.handle(null);

               return (String) MVEL.executeExpression(scriptExpression, vars);

            }

        }

      return null;
    }

    /**
     * @return
     */
    public  Serializable process(String source, Serializable event, int retriesLeft, String id) throws Exception{
        try {
            basicElasticRestRepo.persist(event,id);
        } catch (ElasticSearchException e) {
            e.printStackTrace();
            throw  e;
        }
        return event;
    }

    protected String getRandom() {
        return (UUID.randomUUID().toString());
    }
}
