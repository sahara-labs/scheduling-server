
package au.edu.uts.eng.remotelabs.schedserver.reports.impl.tests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.reports.impl.SessionStatistics;

/**
 * Tests the {@link SessionStatistics} class.
 */
public class SessionStatisticsTester extends TestCase
{
    /** Object under test. */
    private SessionStatistics record;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        this.record = new SessionStatistics();
    }

    public void testAddRecord()
    {
        Date request = new Date(System.currentTimeMillis() - 30000);
        Date assign = new Date(System.currentTimeMillis() + 20000);
        Date remove = new Date(System.currentTimeMillis() + 70000);
        Date after = new Date(System.currentTimeMillis() + 100000);
        Date request2 = new Date(System.currentTimeMillis() - 40000);
        Date assign2 = new Date(System.currentTimeMillis() + 30000);
        Date remove2 = new Date(System.currentTimeMillis() + 80000);
        Date after2 = new Date(System.currentTimeMillis() + 110000);

        short sh = 1;
        
        Session ses1 = new Session();
        ses1.setUserName("test1");
        ses1.setUserNamespace("REPS");
        ses1.setRequestTime(request);
        ses1.setResourceType("RIG");
        ses1.setRequestedResourceName("Rig1");
        ses1.setPriority(sh);
        ses1.setActivityLastUpdated(after);
        ses1.setAssignedRigName("Rig1");
        ses1.setAssignmentTime(assign);
        ses1.setActive(true);
        ses1.setRemovalTime(remove);
        ses1.setRemovalReason("ended");
        ses1.setCodeReference("none");
        ses1.setExtensions(sh);

        Session ses2 = new Session();
        ses2.setUserName("test1");
        ses2.setUserNamespace("REPS");
        ses2.setRequestTime(request2);
        ses2.setResourceType("RIG");
        ses2.setRequestedResourceName("Rig1");
        ses2.setPriority(sh);
        ses2.setActivityLastUpdated(after2);
        ses2.setAssignedRigName("Rig1");
        ses2.setAssignmentTime(assign2);
        ses2.setActive(true);
        ses2.setRemovalTime(remove2);
        ses2.setRemovalReason("ended");
        ses2.setCodeReference("none");
        ses2.setExtensions(sh);
        
        this.record.addRecord(ses1);
        
        assertEquals(this.record.queueDuration.size(),1);
        assertEquals(this.record.sessionDuration.size(),1);

        this.record.addRecord(ses2);
        assertEquals(this.record.queueDuration.size(),2);
        assertEquals(this.record.sessionDuration.size(),2);
    }

    public void testGetMethods()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        // Queue time 60 min
        // Session time 120 min
        Date request = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date assign = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date remove = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date after = cal.getTime();
        
        // Queue time 30min
        // Session time 60 min
        cal.add(Calendar.MINUTE, 30);
        Date request2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date assign2 = cal.getTime();
        cal.add(Calendar.MINUTE, 60);
        Date remove2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after2 = cal.getTime();

        // Queue time 0 hr
        // Session time 40 min
        cal.add(Calendar.MINUTE, 30);
        Date request3 = cal.getTime();
        Date assign3 = cal.getTime();
        cal.add(Calendar.MINUTE, 40);
        Date remove3 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after3 = cal.getTime();

        // Queue time 20 min
        // Session time 0 min
        cal.add(Calendar.MINUTE, 30);
        Date request4 = cal.getTime();
        Date assign4 = null;
        cal.add(Calendar.MINUTE, 20);
        Date remove4 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after4 = cal.getTime();

        short sh = 1;
        
        Session ses1 = new Session();
        ses1.setUserName("test1");
        ses1.setUserNamespace("REPS");
        ses1.setRequestTime(request);
        ses1.setResourceType("RIG");
        ses1.setRequestedResourceName("Rig1");
        ses1.setPriority(sh);
        ses1.setActivityLastUpdated(after);
        ses1.setAssignedRigName("Rig1");
        ses1.setAssignmentTime(assign);
        ses1.setActive(true);
        ses1.setRemovalTime(remove);
        ses1.setRemovalReason("ended");
        ses1.setCodeReference("none");
        ses1.setExtensions(sh);
          
        Session ses2 = new Session();
        ses2.setUserName("test1");
        ses2.setUserNamespace("REPS");
        ses2.setRequestTime(request2);
        ses2.setResourceType("RIG");
        ses2.setRequestedResourceName("Rig1");
        ses2.setPriority(sh);
        ses2.setActivityLastUpdated(after2);
        ses2.setAssignedRigName("Rig1");
        ses2.setAssignmentTime(assign2);
        ses2.setActive(true);
        ses2.setRemovalTime(remove2);
        ses2.setRemovalReason("ended");
        ses2.setCodeReference("none");
        ses2.setExtensions(sh);
          
        Session ses3 = new Session();
        ses3.setUserName("test1");
        ses3.setUserNamespace("REPS");
        ses3.setRequestTime(request3);
        ses3.setResourceType("RIG");
        ses3.setRequestedResourceName("Rig1");
        ses3.setPriority(sh);
        ses3.setActivityLastUpdated(after3);
        ses3.setAssignedRigName("Rig1");
        ses3.setAssignmentTime(assign3);
        ses3.setActive(true);
        ses3.setRemovalTime(remove3);
        ses3.setRemovalReason("ended");
        ses3.setCodeReference("none");
        ses3.setExtensions(sh);
          
        Session ses4 = new Session();
        ses4.setUserName("test1");
        ses4.setUserNamespace("REPS");
        ses4.setRequestTime(request4);
        ses4.setResourceType("RIG");
        ses4.setRequestedResourceName("Rig1");
        ses4.setPriority(sh);
        ses4.setActivityLastUpdated(after4);
        ses4.setAssignedRigName("Rig1");
        ses4.setAssignmentTime(assign4);
        ses4.setActive(true);
        ses4.setRemovalTime(remove4);
        ses4.setRemovalReason("ended");
        ses4.setCodeReference("none");
        ses4.setExtensions(sh);
        
        this.record.addRecord(ses1);
        this.record.addRecord(ses2);
        this.record.addRecord(ses3);
        this.record.addRecord(ses4);
        
        int aveQ = this.record.getAverageQueueDuration();
        int aveS = this.record.getAverageSessionDuration();
        int minQ = this.record .getMinimumQueueDuration();
        int minS = this.record.getMinimumSessionDuration();
        int maxQ = this.record .getMaximumQueueDuration();
        int maxS = this.record.getMaximumSessionDuration();
        int medQ = this.record .getMedianQueueDuration();
        int medS = this.record.getMedianSessionDuration();
        assertEquals(aveQ,275*6);
        assertEquals(aveS,55*60);
        assertEquals(minQ,0);
        assertEquals(minS,0);
        assertEquals(maxQ,60*60);
        assertEquals(maxS,120*60);
        assertEquals(medQ,25*60);
        assertEquals(medS,50*60);
    }
}
