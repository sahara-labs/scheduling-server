/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Client for node client API.
 */
public class NodeClient
{
    /** Operations list. */
    public static final String ALLOCATE = "/session/allocate";
    public static final String RELEASE  = "/session/release";
    public static final String NOTIFY   = "/session/notify";
    public static final String ACTIVITY = "/session/activity";
    
    /** Node to contact. */
    private final String nodeUrl;
    
    /** Name of the node to connect to. */
    private final String nodeName;
    
    /** Certificate for node in PEM format. */
    private final String nodeCert;
    
    /** Logger. */
    private final Logger logger;
    
    public NodeClient(Rig rig)
    {
        this.logger = LoggerActivator.getLogger();
        this.nodeUrl = rig.getContactUrl();
        this.nodeName = rig.getName();
        this.nodeCert = rig.getCert();
    }
    
    /**
     * Call node operation returning response code.
     * 
     * @param operation operation name
     * @return response code
     */
    public int requestCode(String operation)
    {
        try
        {
            /* Make request. */
            URL url = new URL(this.nodeUrl + operation);
            
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            
            /* Configure the connection to use the stored cert to authenticate
             * the connection. */
            conn.setSSLSocketFactory(NodeProviderActivator.getSocketFactory().getFactoryFor(this.nodeName, this.nodeCert));
            conn.setHostnameVerifier(hostnameVerifier);
            
            /* Request method is always POST. */ 
            conn.setRequestMethod("GET");
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            
            return conn.getResponseCode();
        }
        catch (MalformedURLException ex)
        {
            this.logger.error("Error generating node URL, this is probably caused by incorrect configuration of node address, exception " +
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        catch (IOException ex)
        {
            this.logger.error("Communication error calling node request '" + operation + "', exception " + ex.getClass().getSimpleName() +  
                    ": " + ex.getMessage());
        }
        catch (Exception e)
        {
            this.logger.error("Failed client set up, exception " + e.getClass() + ": " + e.getMessage());
        }
        
        return 500;
    }
    
    /**
     * Calls the node operation.
     * 
     * @param operation operation name
     * @param request request parameter
     * @return the node response
     */
    public <T> OperationResponse request(String operation, T request)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request);

            /* Make request. */
            URL url = new URL(this.nodeUrl + operation);
            
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            
            /* Configure the connection to use the stored cert to authenticate
             * the connection. */
            conn.setSSLSocketFactory(NodeProviderActivator.getSocketFactory().getFactoryFor(this.nodeName, this.nodeCert));
            conn.setHostnameVerifier(hostnameVerifier);

            /* We are sending and receiving content. */
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            /* Request method is always POST. */ 
            conn.setRequestMethod("POST");

            /* Content types for request and response are JSON. */
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("Accept", "application/json");

            /* Open connection. */
            conn.connect();
            
            /* Send data. */
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(json);
            out.flush();
            
            /* Check response code, if it is not 200, something about the request 
             * is invalid. */
            int code = conn.getResponseCode();
            if (code == 200)
            {
                /* Load response type. */
                return mapper.readValue(conn.getInputStream(), OperationResponse.class);
            }
            else
            {
                this.logger.debug("Failed calling node operation '" + operation + "', received response code " + code);
                return new OperationResponse(code);
            }            
        }
        catch (JsonProcessingException ex)
        {
            this.logger.error("JSON parsing error, exception " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        catch (MalformedURLException ex)
        {
            this.logger.error("Error generating node URL, this is probably caused by incorrect configuration of HUB address, exception " +
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        catch (IOException ex)
        {
            this.logger.error("Communication error calling node request '" + operation + "', exception " + ex.getClass().getSimpleName() +  
                    ": " + ex.getMessage());
        }
        catch (Exception e)
        {
            this.logger.error("Failed client set up, exception " + e.getClass() + ": " + e.getMessage());
        }
        
        /* Blank response for the case where an exception has occurred. */
        return new OperationResponse(500);
    }    
    
    /** Dummy hostname verifier, that accepts all hostnames. */
    private static HostnameVerifier hostnameVerifier = new HostnameVerifier()
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            /* Accept all hosts. */
            return true; 
        }
    };
}
