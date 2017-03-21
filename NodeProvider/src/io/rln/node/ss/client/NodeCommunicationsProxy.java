/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType.Context;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy;

/**
 * Communications proxy to send messages to a Lab Node.
 */
public class NodeCommunicationsProxy implements RigCommunicationProxy
{
    /** Thread pool to execute requests. */
    private final ExecutorService pool;
    
    public NodeCommunicationsProxy()
    {
        this.pool = Executors.newCachedThreadPool();
    }
    
    @Override
    public void allocate(Session ses, org.hibernate.Session db)
    {
        if (ses.getRig().getContext() == Context.VAS)
        {
           this.pool.execute(NodeAllocator.allocate(ses, db));
        }
    }

    @Override
    public void release(Session ses, org.hibernate.Session db)
    {
        if (ses.getRig().getContext() == Context.VAS)
        {
            this.pool.execute(NodeReleaser.release(ses, db));
        }
    }

    @Override
    public void notify(String message, Session ses, org.hibernate.Session db)
    {
        if (ses.getRig().getContext() == Context.VAS)
        {
            this.pool.execute(NodeNotifier.notify(message, ses, db));
        }
    }

    @Override
    public void putMaintenance(Rig rig, boolean runTests, org.hibernate.Session db)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearMaintenance(Rig rig, org.hibernate.Session db)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasActivity(Session ses, org.hibernate.Session db)
    {
        if (ses.getRig().getContext() == Context.VAS)
        {
            return new NodeClient(ses.getRig()).requestCode(NodeClient.ACTIVITY) == 200;
        }
        else return false;
    }

    @Override
    public void hasActivity(Session ses, org.hibernate.Session db, ActivityAsyncCallback callback)
    {
        if (ses.getRig().getContext() == Context.VAS) 
        {
            this.pool.execute(() -> {
                callback.response(new NodeClient(ses.getRig()).requestCode(NodeClient.ACTIVITY) == 200);
            });
        }
    }

    /**
     * Shuts down the pool that executes async calls. 
     */
    public void shutdown()
    {
        this.pool.shutdown();
    }
}
