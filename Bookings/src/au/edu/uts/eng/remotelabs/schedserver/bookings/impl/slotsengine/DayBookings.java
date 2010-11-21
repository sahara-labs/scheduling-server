/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Michael Diponio (mdiponio)
 * @date 15th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * The bookings for a day.
 * <br />
 * This class is not thread safe and must be externally synchronized before 
 * invoking any of it's methods.
 */
public class DayBookings
{
    /** Loaded rig bookings. */
    private Map<Rig, RigBookings> rigBookings;
    
    /** The day key of this day. */
    private final String day;
    
    /** The beginning time of this day. */
    private final Date dayBegin;
    
    /** The end time of this day. */
    private final Date dayEnd;
    
    /** Loaded request capabilities. */
    private Set<RequestCapabilities> loadedCapabilites;
    
    /** Logger. */
    private Logger logger;
    
    public DayBookings(String day)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.rigBookings = new HashMap<Rig, RigBookings>();
        
        this.day = day;
        this.dayBegin= TimeUtil.getDayBegin(this.day).getTime();
        this.dayEnd = TimeUtil.getDayEnd(this.day).getTime();
        
        this.loadedCapabilites = new HashSet<RequestCapabilities>();
    }
    
    /**
     * Gets the free slots for the rig. 
     * 
     * @param rig the rig to find free slots of
     * @param start start slot
     * @param end end slot
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(Rig rig, int start, int end, Session ses)
    {
        RigBookings rb = this.getRigBookings(rig, ses);
        
        return null;
    }
    
    /**
     * Gets the rig bookings for the rig. If the rig bookings hasn't been 
     * loaded for the rig, it is loaded by:
     * <ul>
     *  <li>Loading the rig and its booking for the day.</li>
     *  <li>Loading all the rigs in the rig type and their bookings.</li>
     *  <li>Linking the rig type resource loop.</li>
     *  <li>Loading the rig type bookings and assigning them to a rig.</li>
     *  <li>Loading the request capabilities resource loops for those that
     *  have at least one booking.</li>
     *  <li>Loading the request capabilities bookings.</li>
     * </ul>
     * 
     * @param rig rig to find bookings of
     * @return rig bookings
     */
    private RigBookings getRigBookings(Rig rig, Session ses)
    {
        if (!this.rigBookings.containsKey(rig))
        {
            this.logger.debug("Loaded day bookings for rig '" + rig.getName() + "' on day " + this.day + ".");
            
            List<RequestCapabilities> capsToLoad = new ArrayList<RequestCapabilities>();

            RigBookings rb = new RigBookings(rig, this.day);
            this.loadRig(rb, rig, ses);
            this.rigBookings.put(rig, rb);
            
            /* Add the capabilities that need to be loaded. */
            for (MatchingCapabilities match : rig.getRigCapabilities().getMatchingCapabilitieses())
            {
                capsToLoad.add(match.getRequestCapabilities());
            }

            this.loadRigType(rig, ses, capsToLoad);
            
            /* Load the request capabilities resource loops for those that have 
             * bookings. */
            this.loadRequestCapabilities(capsToLoad, ses, true);
        }
        
        return this.rigBookings.get(rig);
    }

    /**
     * Loads the request capabilities.
     * 
     * @param capsList capabilities list
     * @param ses database session
     */
    private void loadRequestCapabilities(List<RequestCapabilities> capsList, Session ses, boolean ignoreNoBookings)
    {
        while (capsList.size() > 0)
        {
            RequestCapabilities reqCaps = capsList.get(0);
            Criteria qu = ses.createCriteria(Bookings.class)
                .add(Restrictions.eq("resourceType", ResourcePermission.CAPS_PERMISSION))
                .add(Restrictions.eq("requestCapabilities", reqCaps))
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.lt("startTime", this.dayEnd))
                .add(Restrictions.gt("endTime", this.dayBegin));
            @SuppressWarnings("unchecked")
            List<Bookings> bookings = qu.list();
            
            /* There are no bookings for this class so no need to load it
             * yet. */
            if (ignoreNoBookings && bookings.size() == 0)
            {
                capsList.remove(0);
                continue;
            }
            
            /* Find all the matching rigs. */
            List<Rig> matchingRigs = new ArrayList<Rig>();
            for (MatchingCapabilities match: reqCaps.getMatchingCapabilitieses())
            {
                matchingRigs.addAll(match.getRigCapabilities().getRigs());
            }
            
            /* Make sure all the rigs are loaded. */
            for (Rig r : matchingRigs)
            {
                if (!this.rigBookings.containsKey(r))
                {
                    RigBookings b = new RigBookings(r, this.day);
                    this.loadRig(b, r, ses);
                    this.rigBookings.put(r, b);
                    /* By definition, since a rig wasn't loaded, it's type wasn't 
                     * loaded either. */
                    this.loadRigType(r, ses, capsList);
                }
            }
            
            /* Complete the request capabilities resource loop. */
            RigBookings first = this.rigBookings.get(matchingRigs.get(0));
            RigBookings prev = first;
            for (int i = 1; i < matchingRigs.size(); i++)
            {
                RigBookings next = this.rigBookings.get(matchingRigs.get(1));
                prev.setCapsLoopNext(reqCaps, next);
                prev = next;
            }
            prev.setCapsLoopNext(reqCaps, first);
            
            /* Load the request capabilities bookings. */
            for (Bookings booking : bookings)
            {
                MBooking membooking = new MBooking(booking, this.day);
                RigBookings next = first;

                do
                {
                    if (next.areSlotsFree(membooking))
                    {
                        if (next.commitBooking(membooking))
                        {
                            /* If there is a next booking, try load it to the next rig. */
                            first = next.getCapsLoopNext(reqCaps);
                            break;

                        }
                        else
                        {
                            this.logger.error("Failed to commit a booking to a slots that should of been empty. This " +
                                    "is a probable race condition. Ominous, but the loading search will continue regardless.");
                        }
                    }
                    next = next.getCapsLoopNext(reqCaps);
                }
                while (next != first);
                
                if (!next.hasBooking(membooking))
                {
                    /* The loop was completed and no position was found to put 
                     * the booking, so the request capabilities was over-booked. 
                     * The booking  will need to be canceled. */
                    this.logger.error("Request capabilities '" + reqCaps.getCapabilities() + "' is over commited " +
                    		"and has over lapping bookings. The booking for '" + booking.getUserNamespace() + ':' + 
                    		booking.getUserName() + "' starting at " + booking.getStartTime() + " is being cancelled.");
                    booking.setActive(false);
                    booking.setCancelReason("Request capabilities was overbooked.");
                    ses.beginTransaction();
                    ses.flush();
                    ses.getTransaction().commit();
         
                    // TODO Cancel notification
                }
            }
            
            this.loadedCapabilites.add(capsList.remove(0));
        }
    }

    /**
     * Loads a rig type by creating the rig bookings type loop, then loading 
     * the rig type bookings to rigs.
     * 
     * @param rig rig in type
     * @param ses database session
     * @param capsToLoad list of request capabilities that may need to be loaded from
     *      the rigs in type
     */
    @SuppressWarnings("unchecked")
    private void loadRigType(Rig rig, Session ses, List<RequestCapabilities> capsToLoad)
    {
        RigBookings first = this.rigBookings.get(rig);
        RigBookings prev = first;
        
        /* Set up the rig type navigation loop. */
        RigType rigType = rig.getRigType();
        Set<Rig> rigs = rigType.getRigs();
        for (Rig r : rigs)
        {
            /* Don't duplicate the initial rig. */
            if (r.equals(rig)) continue;
            
            for (MatchingCapabilities match : r.getRigCapabilities().getMatchingCapabilitieses())
            {
                RequestCapabilities reqCaps = match.getRequestCapabilities();
                if (!capsToLoad.contains(reqCaps))
                {
                    capsToLoad.add(reqCaps);
                }
            }

            RigBookings next = new RigBookings(r, this.day);
            this.loadRig(next, r, ses);
            this.rigBookings.put(r, next);
            prev.setTypeLoopNext(next);
            prev = next;
        }
        
        /* Complete the type loop. */
        prev.setTypeLoopNext(first);
        
        /* Load up the type bookings. */
        Criteria qu = ses.createCriteria(Bookings.class)
            .add(Restrictions.eq("resourceType", ResourcePermission.TYPE_PERMISSION))
            .add(Restrictions.eq("rigType", rigType))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("startTime", this.dayEnd))
            .add(Restrictions.gt("endTime", this.dayBegin));
        
        for (Bookings booking : (List<Bookings>)qu.list())
        {
            MBooking membooking = new MBooking(booking, this.day);
            RigBookings next = first;

            do
            {
                if (next.areSlotsFree(membooking))
                {
                    if (next.commitBooking(membooking))
                    {
                        /* If there is a next booking, try load it to the next rig. */
                        first = next.getTypeLoopNext();
                        break;

                    }
                    else
                    {
                        this.logger.error("Failed to commit a booking to a slots that should of been empty. This " +
                                "is a probable race condition. Ominous, but the loading search will continue regardless.");
                    }
                }
                next = next.getTypeLoopNext();
            }
            while (next != first);
            
            if (!next.hasBooking(membooking))
            {
                /* The loop was completed and no position was found to put 
                 * the booking, so the type was over-booked. The booking 
                 * will need to be canceled. */
                this.logger.error("Rig type '" + rigType.getName() + "' is over commited and has over lapping bookings. " +
                    "The booking for '" + booking.getUserNamespace() + ':' + booking.getUserName() + "' starting at " +
                    booking.getStartTime() + " is being cancelled.");
                booking.setActive(false);
                booking.setCancelReason("Rig type was overbooked.");
                ses.beginTransaction();
                ses.flush();
                ses.getTransaction().commit();
   
                // TODO Cancel notification
            }
        }
    }

    /**
     * Loads the rig booking times for the rig. If the rig is over committed
     * (overlapping rig bookings), one of the bookings is canceled.
     * 
     * @param bookings rig bookings container
     * @param rig rig to load bookings from
     * @param ses database session
     */
    @SuppressWarnings("unchecked")
    private void loadRig(RigBookings bookings, Rig rig, Session ses)
    {
        Criteria qu = ses.createCriteria(Bookings.class)
            .add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION))
            .add(Restrictions.eq("rig", rig))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("startTime", this.dayEnd))
            .add(Restrictions.gt("endTime", this.dayBegin));

        for (Bookings booking : (List<Bookings>)qu.list())
        {
            if (!bookings.commitBooking(new MBooking(booking, this.day)))
            {
                /* The rig has been over booked so this booking will 
                 * need to be canceled. */
                this.logger.error("Rig '" + rig.getName() + "' is over commited and has over lapping bookings. " +
                        "The booking for '" + booking.getUserNamespace() + ':' + booking.getUserName() + "' starting at " +
                        booking.getStartTime() + " is being cancelled.");
                booking.setActive(false);
                booking.setCancelReason("Rig was overbooked.");
                ses.beginTransaction();
                ses.flush();
                ses.getTransaction().commit();

                // TODO Cancel notification
            }
        }
    }
    
    public String getDay()
    {
        return this.day;
    }
}
