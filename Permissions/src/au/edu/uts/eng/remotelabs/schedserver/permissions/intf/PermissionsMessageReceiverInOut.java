
/**
 * PermissionsMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */
        package au.edu.uts.eng.remotelabs.schedserver.permissions.intf;

        /**
        *  PermissionsMessageReceiverInOut message receiver
        */

        public class PermissionsMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        PermissionsSkeletonInterface skel = (PermissionsSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("addUserLock".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse addUserLockResponse59 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addUserLockResponse59 =
                                                   
                                                   
                                                         skel.addUserLock(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addUserLockResponse59, false);
                                    } else 

            if("addPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse addPermissionResponse61 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addPermissionResponse61 =
                                                   
                                                   
                                                         skel.addPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addPermissionResponse61, false);
                                    } else 

            if("addUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse addUserResponse63 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addUserResponse63 =
                                                   
                                                   
                                                         skel.addUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addUserResponse63, false);
                                    } else 

            if("deleteUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse deleteUserResponse65 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteUserResponse65 =
                                                   
                                                   
                                                         skel.deleteUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteUserResponse65, false);
                                    } else 

            if("getUserClassesForUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse getUserClassesForUserResponse67 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserClassesForUserResponse67 =
                                                   
                                                   
                                                         skel.getUserClassesForUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserClassesForUserResponse67, false);
                                    } else 

            if("deleteAcademicPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse deleteAcademicPermissionResponse69 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteAcademicPermissionResponse69 =
                                                   
                                                   
                                                         skel.deleteAcademicPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteAcademicPermissionResponse69, false);
                                    } else 

            if("getAcademicPermissionsForAcademic".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademicResponse71 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getAcademicPermissionsForAcademicResponse71 =
                                                   
                                                   
                                                         skel.getAcademicPermissionsForAcademic(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getAcademicPermissionsForAcademicResponse71, false);
                                    } else 

            if("deleteUserAssociation".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse deleteUserAssociationResponse73 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteUserAssociationResponse73 =
                                                   
                                                   
                                                         skel.deleteUserAssociation(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteUserAssociationResponse73, false);
                                    } else 

            if("getPermissionsForUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse getPermissionsForUserClassResponse75 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPermissionsForUserClassResponse75 =
                                                   
                                                   
                                                         skel.getPermissionsForUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPermissionsForUserClassResponse75, false);
                                    } else 

            if("editPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse editPermissionResponse77 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editPermissionResponse77 =
                                                   
                                                   
                                                         skel.editPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editPermissionResponse77, false);
                                    } else 

            if("editUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse editUserClassResponse79 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editUserClassResponse79 =
                                                   
                                                   
                                                         skel.editUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editUserClassResponse79, false);
                                    } else 

            if("deleteUserLock".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse deleteUserLockResponse81 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteUserLockResponse81 =
                                                   
                                                   
                                                         skel.deleteUserLock(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteUserLockResponse81, false);
                                    } else 

            if("editAcademicPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse editAcademicPermissionResponse83 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editAcademicPermissionResponse83 =
                                                   
                                                   
                                                         skel.editAcademicPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editAcademicPermissionResponse83, false);
                                    } else 

            if("deleteUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse deleteUserClassResponse85 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteUserClassResponse85 =
                                                   
                                                   
                                                         skel.deleteUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteUserClassResponse85, false);
                                    } else 

            if("addUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse addUserClassResponse87 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addUserClassResponse87 =
                                                   
                                                   
                                                         skel.addUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addUserClassResponse87, false);
                                    } else 

            if("deletePermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse deletePermissionResponse89 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deletePermissionResponse89 =
                                                   
                                                   
                                                         skel.deletePermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deletePermissionResponse89, false);
                                    } else 

            if("getPermissionsForUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse getPermissionsForUserResponse91 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPermissionsForUserResponse91 =
                                                   
                                                   
                                                         skel.getPermissionsForUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPermissionsForUserResponse91, false);
                                    } else 

            if("getUsersInUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse getUsersInUserClassResponse93 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUsersInUserClassResponse93 =
                                                   
                                                   
                                                         skel.getUsersInUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUsersInUserClassResponse93, false);
                                    } else 

            if("addUserAssociation".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse addUserAssociationResponse95 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addUserAssociationResponse95 =
                                                   
                                                   
                                                         skel.addUserAssociation(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addUserAssociationResponse95, false);
                                    } else 

            if("getUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse getUserResponse97 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserResponse97 =
                                                   
                                                   
                                                         skel.getUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserResponse97, false);
                                    } else 

            if("getAcademicPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse getAcademicPermissionResponse99 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getAcademicPermissionResponse99 =
                                                   
                                                   
                                                         skel.getAcademicPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getAcademicPermissionResponse99, false);
                                    } else 

            if("addAcademicPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse addAcademicPermissionResponse101 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addAcademicPermissionResponse101 =
                                                   
                                                   
                                                         skel.addAcademicPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addAcademicPermissionResponse101, false);
                                    } else 

            if("unlockUserLock".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse unlockUserLockResponse103 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               unlockUserLockResponse103 =
                                                   
                                                   
                                                         skel.unlockUserLock(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), unlockUserLockResponse103, false);
                                    } else 

            if("getUserClasses".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse getUserClassesResponse105 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserClassesResponse105 =
                                                   
                                                   
                                                         skel.getUserClasses(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserClassesResponse105, false);
                                    } else 

            if("bulkAddUserClassUsers".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse bulkAddUserClassUsersResponse107 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               bulkAddUserClassUsersResponse107 =
                                                   
                                                   
                                                         skel.bulkAddUserClassUsers(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), bulkAddUserClassUsersResponse107, false);
                                    } else 

            if("getAcademicPermissionsForUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse getAcademicPermissionsForUserClassResponse109 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getAcademicPermissionsForUserClassResponse109 =
                                                   
                                                   
                                                         skel.getAcademicPermissionsForUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getAcademicPermissionsForUserClassResponse109, false);
                                    } else 

            if("getPermission".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse getPermissionResponse111 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getPermissionResponse111 =
                                                   
                                                   
                                                         skel.getPermission(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getPermissionResponse111, false);
                                    } else 

            if("getUserClass".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse getUserClassResponse113 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserClassResponse113 =
                                                   
                                                   
                                                         skel.getUserClass(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserClassResponse113, false);
                                    } else 

            if("editUser".equals(methodName)){
                
                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse editUserResponse115 = null;
	                        au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser wrappedParam =
                                                             (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editUserResponse115 =
                                                   
                                                   
                                                         skel.editUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editUserResponse115, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse wrapaddUserLock(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse wrapaddPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse wrapaddUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse wrapdeleteUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse wrapgetUserClassesForUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse wrapdeleteAcademicPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse wrapgetAcademicPermissionsForAcademic(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse wrapdeleteUserAssociation(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse wrapgetPermissionsForUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse wrapeditPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse wrapeditUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse wrapdeleteUserLock(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse wrapeditAcademicPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse wrapdeleteUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse wrapaddUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse wrapdeletePermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse wrapgetPermissionsForUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse wrapgetUsersInUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse wrapaddUserAssociation(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse wrapgetUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse wrapgetAcademicPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse wrapaddAcademicPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse wrapunlockUserLock(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse wrapgetUserClasses(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse wrapbulkAddUserClassUsers(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse wrapgetAcademicPermissionsForUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse wrapgetPermission(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse wrapgetUserClass(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse wrapeditUser(){
                                au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse wrappedElement = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse.class.equals(type)){
                
                           return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    