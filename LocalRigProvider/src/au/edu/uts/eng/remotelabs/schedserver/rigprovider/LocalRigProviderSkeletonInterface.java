package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse;

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
