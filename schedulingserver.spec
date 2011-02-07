#
# Sahara Scheduling Server
#
# Schedules and assigns remote laboratory rigs.
#
# @license See LICENSE in the top level directory for complete license terms.
#
# Copyright (c) 2009, University of Technology, Sydney
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
#  * Redistributions of source code must retain the above copyright notice,
#    this list of conditions and the following disclaimer.
#  * Redistributions in binary form must reproduce the above copyright
#    notice, this list of conditions and the following disclaimer in the
#    documentation and/or other materials provided with the distribution.
#  * Neither the name of the University of Technology, Sydney nor the names
#    of its contributors may be used to endorse or promote products derived from
#    this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
# DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
# FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
# SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
# CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
# OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
# @author Michael Diponio (mdiponio), Tania Machet (tmachet)
# @date 27rd April 2010
#

# Spec file to generate the Scheduling Server RPM

Name: SchedulingServer
Summary: Schedules and assigns remote laboratory rigs.
Version: 3.0
Release: 0
License: BSD
Group: Sahara

URL: http://sourceforge.net/projects/labshare-sahara/
Distribution: Redhat, SuSE
Vendor: University of Technology, Sydney
Packager: Tania Machet <tmachet@eng.uts.edu.au>, Michael Diponio <mdiponio@eng.uts.edu.au>
Requires: jre >= 1.6.0

%define installdir /usr/lib/schedulingserver

%description
Schedules and assigns remote laboratory rigs.

%install

mkdir -p $RPM_BUILD_ROOT/%{installdir}

# Framework
mkdir -p $RPM_BUILD_ROOT/%{installdir}/bin
cp $RPM_BUILD_DIR/../../bin/SchedulingServer.jar $RPM_BUILD_ROOT/%{installdir}/bin/SchedulingServer.jar

# Bundles
mkdir -p $RPM_BUILD_ROOT/%{installdir}/bundles
for BUNDLE in $RPM_BUILD_DIR/../../bundles/*.jar 
do
	cp $BUNDLE $RPM_BUILD_ROOT/%{installdir}/bundles/
done

# Configuration files
mkdir -p $RPM_BUILD_ROOT/%{installdir}/conf
install -m 700 $RPM_BUILD_DIR/../../conf/schedulingserver.properties $RPM_BUILD_ROOT/%{installdir}/conf/schedulingserver.properties
cp $RPM_BUILD_DIR/../../conf/scheduling_service.ini $RPM_BUILD_ROOT/%{installdir}/conf/scheduling_service.ini

# Service wrapper & init file
install -m 700 $RPM_BUILD_DIR/../../servicewrapper/schedulingservice $RPM_BUILD_ROOT/%{installdir}/schedulingservice
mkdir -p $RPM_BUILD_ROOT/etc/init.d
install -m 755 $RPM_BUILD_DIR/../../servicewrapper/schedulingserver_init  $RPM_BUILD_ROOT/etc/init.d/schedulingserver

# Schema files
mkdir -p $RPM_BUILD_ROOT/%{installdir}/schemas/
for SCHEMA in $RPM_BUILD_DIR/../../doc/db/schema/*.sql
do
	cp $SCHEMA $RPM_BUILD_ROOT/%{installdir}/schemas
done
cp  $RPM_BUILD_ROOT/InstallerFiles/migrationScriptV2ToV3.sql $RPM_BUILD_ROOT/%{installdir}/schemas

# Miscellaneous stuff
mkdir -p $RPM_BUILD_ROOT/etc/Sahara
cp $RPM_BUILD_DIR/../../LICENSE $RPM_BUILD_ROOT/%{installdir}/LICENSE

%post

# Let the operating system know the service has been added
if [ -x /usr/lib/lsb/install_initd ]; then
  /usr/lib/lsb/install_initd /etc/init.d/schedulingserver
elif [ -x /sbin/chkconfig ]; then
  /sbin/chkconfig --add schedulingserver
else
   for i in 3 4 5; do
        ln -sf /etc/init.d/schedulingserver /etc/rc.d/rc${i}.d/S90schedulingserver
   done
   for i in 1 6; do
        ln -sf /etc/init.d/schedulingserver /etc/rc.d/rc${i}.d/K10schedulingserver
   done
fi

# Add a symlink to the Rig Client configuration into /etc
ln -sf %{installdir}/conf /etc/Sahara/SchedulingServer

%preun

# Let the operating system know the service has been removed
if [ $1 = 0 ]; then
  /etc/init.d/schedulingserver stop  > /dev/null 2>&1
  if [ -x /usr/lib/lsb/remove_initd ]; then
    /usr/lib/lsb/remove_initd /etc/init.d/schedulingserver
  elif [ -x /sbin/chkconfig ]; then
    /sbin/chkconfig --del schedulingserver
  else
    rm -f /etc/rc.d/rc?.d/???schedulingserver
  fi
fi

# Remove configuration symlink
rm -f  /etc/Sahara/SchedulingServer

%files
/etc/init.d/schedulingserver
/etc/Sahara
%{installdir}
%{installdir}/bin
%{installdir}/bin/SchedulingServer.jar
%{installdir}/bundles
%{installdir}/bundles/*.jar
%{installdir}/conf
%{installdir}/conf/schedulingserver.properties
%{installdir}/conf/scheduling_service.ini
%{installdir}/schedulingservice
%{installdir}/LICENSE
%{installdir}/schemas
