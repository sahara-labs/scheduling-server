package au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.StatusType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigType;


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
    public RegisterRigResponse registerRig(final RegisterRig registerRig)
    {
        RegisterRigType type = registerRig.getRegisterRig();
        StatusType status = type.getStatus();
        this.logger.info("Called " + this.getClass().getName() + "#registerRig with parameters: name=" + type.getName()
                + ", type=" + type.getType() + ", capabilities=" + type.getCapabilities() + ", isOnline=" + 
                status.getIsOnline() + ", offlineReason=" + status.getOfflineReason() + ".");
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#registerRig implementation.");
    }

    @Override
    public RemoveRigResponse removeRig(final RemoveRig removeRig)
    {
        RemoveRigType rem = removeRig.getRemoveRig();
        this.logger.info("Called " + this.getClass().getName() + "#removeRig with parameters: name=" + rem.getName() 
                + ", removal " + "reason=" + rem.getRemovalReason() + '.');
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#removeRig implementation.");
    }

    @Override
    public UpdateRigStatusResponse updateRigStatus(final UpdateRigStatus updateRigStatus)
    {
        UpdateRigType type = updateRigStatus.getUpdateRigStatus();
        StatusType status = type.getStatus();
        this.logger.info("Called " + this.getClass().getName() + "#updateRigStatus with parameters: name=" + type.getName()
                + ", isOnline=" + status.getIsOnline() + ", offlineReason=" + status.getOfflineReason() + '.');
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#updateRigStatus implementation.");
    }
}
