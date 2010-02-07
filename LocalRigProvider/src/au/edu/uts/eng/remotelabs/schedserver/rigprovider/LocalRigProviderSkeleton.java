package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse;


/**
 * LocalRigProviderSkeleton java skeleton for the axisService
 */
public class LocalRigProviderSkeleton implements LocalRigProviderSkeletonInterface
{
    /** Logger. */
    private Logger logger;

    public LocalRigProviderSkeleton()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public RemoveRigResponse removeRig(final RemoveRig removeRig)
    {
        RemoveRigType rem = removeRig.getRemoveRig();
        this.logger.info("Called LocalRigProvider#removeRig with parameters: name=" + rem.getName() + ", removal " +
        		"reason=" + rem.getRemovalReason() + '.');
        throw new UnsupportedOperationException("Called LocalRigProviderSkeleton#removeRig.");
    }

    @Override
    public UpdateRigStatusResponse updateRigStatus(final UpdateRigStatus updateRigStatus)
    {
        
        throw new UnsupportedOperationException("Called LocalRigProvider#updateRigStatus.");
    }

    @Override
    public RegisterRigResponse registerRig(final RegisterRig registerRig4)
    {
        //TODO : fill this with the necessary business logic
        throw new UnsupportedOperationException("Please implement "
                + this.getClass().getName() + "#registerRig");
    }

}
