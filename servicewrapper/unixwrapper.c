/**
* Sahara Scheduling Server - Service Wrapper
*
* @license See LICENSE in the top level directory for complete license terms.
*
* Copyright (c) 2009, University of Technology, Sydney
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
* @date 8th April 2010
*/

#include <errno.h>
#include <pthread.h>
#include <pwd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "jvmrunner.h"

/** System user environment variable. */
#define USER_ENV "SCHED_SERVER_USER"

/* Event handlers. */
void *runSchedulingServer();
void shutDownSchedulingServer();

/* Setup functions. */
void setupLogFiles(uid_t uid, gid_t gid);
int setupLogFile(const char *path, uid_t uid, gid_t gid);


/**
 * Starts up the rig client by creating a JVM in a seperate thread.
 * Installs a signal handler so a SIGINT or SIGTERM causes the rig
 * client to shut down. 
 */
int main(int argc, char *argv[])
{
    pthread_t thread;
    int code;
    
    const char *user;
    struct passwd *userRecord;

    struct sigaction shutAction;

    if (!loadConfig())
    {
        logMessage("ERROR: Failed to load configuration, exiting...\n\n");
        printf("Failed to load configuration, exiting...\n\n");
        return 1;
    }
    
    /* We may have been asked to run as another user, if so, drop
     * priviledges to that user. */
    if ((user = getenv(USER_ENV)) != NULL)
    {
        if ((userRecord = getpwnam(user)) == NULL)
        {
            logMessage("ERROR: Failed to find Scheduling Server system user: %s\n", user);
            printf("Failed to find Scheduling Server system user: %s\n", user);
            return 3;
        }
        
        /* Make sure we can write to the log file(s). */
        setupLogFiles(userRecord->pw_uid, userRecord->pw_gid);

        /* Drop root privileges. */
        if (setuid(userRecord->pw_uid) != 0)
        {
            logMessage("ERROR: Failed changed process ownership to user %s.\n", user);
            perror("Failed to changed process ownership");
            return 3;
        }
    }
    else
    {
        if ((userRecord = getpwuid(getuid())) != NULL) user = strdup(userRecord->pw_name);
    }
    
    logMessage("NORMAL: Going to the run the Scheduling Server as user: %s\n", user);
    
    if (!generateClassPath())
    {
        logMessage("ERROR: Failed to detected current directory...\n\n");
        printf("Failed to detected current directory...\n\n");
        return 2;
    }
    
    /* Install signal handler for SIGINT & SIGTERM. */
    shutAction.sa_handler = shutDownSchedulingServer;
    sigemptyset(&shutAction.sa_mask);
    shutAction.sa_flags = SA_RESTART;
    if (sigaction(SIGINT, &shutAction, NULL) == -1)
    {
        logMessage("ERROR: Unable to install interrupt signal (SIGINT) handler.");
        perror("Failed installing SIGINT handler");
    }
    if (sigaction(SIGTERM, &shutAction, 0) == -1)
    {
        logMessage("ERROR: Unable to install terminate signal (SIGTERM) handler.");
        perror("Failed installing SIGTERM handler");
    }

    /* Start thread. */
    if (code = pthread_create(&thread, NULL, runSchedulingServer, NULL))
    {
        logMessage("ERROR: Failed to create JVM thread with code %i.\n", code);
        printf("Failed to create JVM thread with code %i.\n", code);
        return 3;
    }

    pthread_join(thread, NULL);
    return 0;
}

/**
 * Thread handler which runs the Scheduling Server.
 */
void *runSchedulingServer()
{
    logMessage("NORMAL: Starting up Schduling Server...\n");
    if (!startJVM())
    {
        printf("ERROR: Failed to start Scheduling Server.\n\n");
    }
}

/**
 * Event handler that stops the  Scheduling Server.
 */
void shutDownSchedulingServer()
{
    logMessage("Shutting down Scheduling Server...\n");
    shutDownJVM();
}

#define CONF_FILE "conf/schedulingserver.properties"
#define PATH_LEN 1024

/**
 * Sets up the log file so it can be written with a user that does not 
 * have elevated privileges. This is to occur before we have dropped
 * privileges to the Scheduling Server system user.
 * 
 * @param uid the users uid 
 * @param gid the users gid
 */
void setupLogFiles(uid_t uid, gid_t gid)
{
    char buf[PATH_LEN], *line, *prop, logType[PATH_LEN], logFile[PATH_LEN];
    int logBackups, i;
    FILE *fp;
    
    memset(logType, 0, PATH_LEN);
    memset(logFile, 0, PATH_LEN);
    
    /* Open the configuration file. */
    if ((fp = fopen(CONF_FILE, "r")) == NULL)
    {
        logMessage("ERROR: Unable to open configuration file: %s", CONF_FILE);
        return;
    }
    
    while (fgets(buf, PATH_LEN, fp) != NULL)
    {
        line = trim(buf);
        
        /* Empty comment line. */
        if (strlen(line) == 0 || line[0] == '#') continue;
        
        i = 0;
        while (line[i] != '\0' && !isspace(line[i])) i++;
               
        if (strncmp("Logger_Type", line, i) == 0) strcpy(logType, trim(line + i));
        else if (strncmp("Log_File_Name", line, i) == 0) strcpy(logFile, trim(line + i));
        else if (strncmp("Log_File_Backups", line, i) == 0) logBackups = atoi(trim(line + i));
    }
    
    fclose(fp);
    
    if (strcmp("File", logType) == 0 || strcmp("RolledFile", logType) == 0)
    {
        /* The logger type has the Scheduling Server writing to the filesystem
         * so we will ensure the file exists and is owned by the system user. */
        if (logFile[0] == 0)
        {
            logMessage("ERROR: Unable to setup log file permission because the log file path has not been configured.\n");
            return;
        }
        
        if (!setupLogFile(logFile, uid, gid)) return;
    }
    
    if (strcmp("RolledFile", logType) == 0 && logBackups > 0)
    {
        char backupFile[PATH_LEN];
        
        /* Also setup log backup files. This may or may not give appropriate 
         * permission to create backups if the parent is not writable by the 
         * Scheduling Server user. It is however too invasive to chown the 
         * parent directory since it is probably a system directory like 
         * '/var/log/'. */
        for (i = 1; i <= logBackups; i++)
        {
            snprintf(backupFile, PATH_LEN, "%s.%i", logFile, i);
            if (!setupLogFile(backupFile, uid, gid))
            {
                logMessage("ERROR: Unable to setup backup log file %s\n.", backupFile);
                perror("Unable to setup backup file");
            }
        }
    }
}

/**
 * Setups the log file with ownership assigned to the specified user id. If
 * the log file does not exist, it is created with user ownership. If the log
 * file does exist but is owned by another user, it has its ownership changed
 * to the specified user.
 * 
 * @param logFile the path to the log file
 * @param uid the users uid
 * @param gid the users gid
 * @return 1 if successful, 0 if failure
 */
int setupLogFile(const char *logFile, uid_t uid, gid_t gid)
{
    struct stat logStat;
    
    if (stat(logFile, &logStat))
    {
        /* The parent directory may not exist, if not we will need to 
         * create it. */
        char path[PATH_LEN];
        int pos = 0, len = strlen(logFile);
        struct stat dirStat;
        
        memset(path, 0, PATH_LEN);
        
        do
        {
            while (pos < len && logFile[pos++] != '/');
            
            if (pos < len)
            {
                /* Path component. */
                strncpy(path, logFile, pos);
                
                if (stat(path, &dirStat))
                {
                    /* Directory doesn't exist, so create it. */
                    if (mkdir(path, 0755))
                    {
                        logMessage("ERROR: Cannot create parent directory %s of log file %s\n", path, logFile);
                        perror("Creating parent directory failed");
                        return 0;
                    }
                    
                    if (chown(path, uid, gid))
                    {
                        logMessage("ERROR: Failed to change ownership of parent directory %s.\n", path);
                        perror("Changing ownership failed");
                        return 0;
                    }
                }
            }
        }
        while (pos < len);
        
        /* Create the log file. */
        FILE *lf;
        if ((lf = fopen(logFile, "w")) == NULL)
        {
            logMessage("ERROR: Failed to create log file %s with error code %i.\n", logFile, errno);
            perror("Failed to create log file");
            return 0;
        }
        
        fputc(' ', lf); /* Some dummy data. */
        fclose(lf);
        
        if (chown(logFile, uid, gid))
        {
            logMessage("ERROR: Failed to change ownership of log file %s to uid %i with error code %i.\n", logFile, (int)uid, errno);
            perror("Failed to change ownership to log file");
            return 0;
        }
    }
    else if (logStat.st_uid != uid)
    {
        /* Log file exists but is not owned by the Scheduling Server system user. */
        if (chown(logFile, uid, gid))
        {
            logMessage("ERROR: Failed to change ownership of log file %s to uid %i with error code %i.\n", logFile, (int)uid, errno);
            perror("Failed to change ownership of log file");
            return 0;
        }
    }
    
    return 1;
}
