package au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf;

import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatusResponse;

/**
 * LocalRigProviderSkeletonInterface, interface for the local rig provider 
 * SOAP operations.
 */
public interface LocalRigProviderSkeletonInterface
{
    /**
     * Registers a rig with the scheduling server.
     * 
     * @param registerRig request parameters
     * @return response parameters
     */
    public RegisterRigResponse registerRig(RegisterRig registerRig);

    /**
     * Updates the status of a rig.
     * 
     * @param updateRigStatus request parameters
     * @return response parameters
     */
    public UpdateRigStatusResponse updateRigStatus(UpdateRigStatus updateRigStatus);

    /**
     * Removes a rig registration.
     * 
     * @param removeRig request parameters
     * @return response parameters
     */
    public RemoveRigResponse removeRig(RemoveRig removeRig);
}
