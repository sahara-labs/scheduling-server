/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Key manager for node authentication.
 */
public class NodeSSLFactory
{
    /** Key store that stores the service certificate. */
    private KeyStore keyStore;

    /** Key access password. */
    private String keyPassword;
    
    /** Trust store to authenticate trust stores. */
    private KeyStore trustStore;
    
    /** Secure socket factory. */
    private SSLSocketFactory sslFactory;
    
    /** Certificate factory to load PEM encoded certificates. */
    private CertificateFactory certFactory;
   
    /** Logger. */
    private final Logger logger;
    
    public NodeSSLFactory()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Initialise key stores.
     * 
     * @param config application configuration
     */
    public void init(Config config) throws Exception
    {
        String path = config.getProperty("Keystore_Path");
        String password = config.getProperty("Keystore_Password");
        if (path == null || password == null)
        {
            this.logger.error("Cannot load node key stores because the keystore path or access password " +
                    "has not been configured.");
            throw new Exception("Keystore details not configured.");
        }
        this.keyStore = this.load(path, password);
        
        if ((path = config.getProperty("Truststore_Path")) == null ||
            (password = config.getProperty("Truststore_Password")) == null)
        {
            this.logger.error("Cannot load or create trust store because the truststore path or access " + 
                    "password has not been configured.");
            throw new Exception("Truststore details not configured.");
        }
        this.trustStore = this.loadOrCreate(path, password);
            
        if ((this.keyPassword = config.getProperty("Key_Password")) == null)
        {
            this.logger.error("Cannot setup key access as the key access password has not been configured.");
            throw new Exception("Key access password not configured.");
        }
        
        this.sslFactory = this.loadSSLSocketFactory();
        this.certFactory = CertificateFactory.getInstance("X.509");
    }
    
    /**
     * Gets a SSL socket factory which will authenticate a node with the stored
     * node certificate.
     * 
     * @param name node name
     * @param cert stored node certificate in PEM format
     * @return socket factory
     * @throws Exception error loading SSL factory
     */
    public SSLSocketFactory getFactoryFor(String name, String cert) throws Exception
    {
        try
        {
            if (!this.trustStore.isCertificateEntry(name))
            {
                synchronized (this)
                {
                    if (!this.trustStore.isCertificateEntry(name))
                    {
                        Certificate x509 = this.certFactory.generateCertificate(new ByteArrayInputStream(cert.getBytes()));
                        this.trustStore.setCertificateEntry(name, x509);
                        this.sslFactory = this.loadSSLSocketFactory();
                    }
                }
            }
        }
        catch (KeyStoreException e)
        {
            this.logger.error("Failed to add certificate to trust store for node " + name + ", exception " +
                    e.getClass() + ": " + e.getMessage());
            throw e;
        }
        catch (CertificateException e)
        {
            this.logger.error("Failed to convert PEM encoded certificate to X509 certificate object for node " + name + 
                    ", exception " + e.getClass() + ": " + e.getMessage());
            throw e;
        }
        
        return this.sslFactory;
    }
    
    /**
     * Add certificate for the specified node replacing any that exist.
     * 
     * @param name node name
     * @param cert stored certificate in PEM format
     */
    public void addTrustCert(String name, String cert)
    {
        synchronized (this)
        {
            try
            {
                if (this.trustStore.isCertificateEntry(name))
                {
                    this.trustStore.deleteEntry(name);
                }
                
                Certificate x509 = this.certFactory.generateCertificate(new ByteArrayInputStream(cert.getBytes()));
                this.trustStore.setCertificateEntry(name, x509);
            }
            catch (KeyStoreException e)
            {
                this.logger.error("Failed to add certificate to trust store for node " + name + ", exception " +
                        e.getClass() + ": " + e.getMessage());
            }
            catch (CertificateException e)
            {
                this.logger.error("Failed to convert PEM encoded certificate to X509 certificate object for node " + name + 
                        ", exception " + e.getClass() + ": " + e.getMessage());
            }

            
        }
    }
    
    /**
     * Remove certificate for the specified node.
     * 
     * @param name node name
     */
    public void removeTrustCert(String name)
    {
        synchronized (this)
        {
            try
            {
                if (this.trustStore.isCertificateEntry(name))
                {
                    this.trustStore.deleteEntry(name);
                }
            }
            catch (KeyStoreException e)
            {
                this.logger.error("Failed to remove certificate for node " + name + " from trust store, exception " +
                        e.getClass() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Loads a SSL socket factory that uses the Node trust store to 
     * authenticate connections with the stored Hub certificate.
     * 
     * @param keyStores the server key store and trust store container
     */
    public SSLSocketFactory loadSSLSocketFactory() throws Exception 
    {
        try
        {   
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(this.keyStore, this.keyPassword.toCharArray());
        
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(this.trustStore);
            
            SSLContext ssl = SSLContext.getInstance("TLS");
            ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            
            return ssl.getSocketFactory();
        }
        catch (NoSuchAlgorithmException ex)
        {
            this.logger.error("Error loading SSL factory for node client requests. This is a JVM installation problem. " + 
                    "Exception " + ex.getClass().getSimpleName() + ", " + ex.getMessage());
            throw ex;
        }
        catch (UnrecoverableKeyException | KeyStoreException | KeyManagementException ex)
        {
            this.logger.error("Error loading SSL factory for node client requests. This is a problem with the configured " +
                    "truststore or the configured service certificate. Exception " + ex.getClass().getSimpleName() + 
                    ", " + ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * Load the specified key store.
     * @param path path to key store
     * @param password access password
     * @return keystore loaded
     * @throws Exception error loading key store
     */
    private KeyStore load(String path, String password) throws Exception
    {
        try
        {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            
            File file = new File(path);
            if (!file.exists())
            {
                this.logger.error("Cannot load node key store because the key store does not exist at the " +
                        "configured path: " + path);
                throw new Exception("Keystore does not exist");
            }
            
            try (FileInputStream fin = new FileInputStream(file))
            {
                ks.load(fin, password.toCharArray());
            }
            
            return ks;
        }
        catch (KeyStoreException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + ", from a generic keystore exception.");
            throw e;
        }
        catch (IOException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + ", there is a problem reading or writing " +
                    "the keystore or the configured password is incorrect, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (NoSuchAlgorithmException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path +  
                    ", this might be a Java installation problem, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (CertificateException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + 
                    ", there is a problem with a certificate, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Load or create the specified keystore.
     * 
     * @param path path to key store
     * @param password key store password
     * @return keystore ready to be accessed
     * @throws Exception error loading or creating keystore
     */
    private KeyStore loadOrCreate(String path, String password) throws Exception
    {
        try
        {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

            if (new File(path).exists())
            {
                /* Keystore exists, so it only needs to be loaded. */
                this.logger.debug("Keystore at " + path + " already exists, it will be loaded");

                try (FileInputStream fis = new FileInputStream(path))
                {
                    ks.load(fis, password.toCharArray());
                }

                this.logger.debug("Keystore at " +  path + " successfully loaded.");
            }
            else
            {
                /* Keystore doesn't exist, will need to create it. */
                this.logger.debug("Keystore at " + path + " does not exist, creating it");

                ks.load(null, password.toCharArray());

                try (FileOutputStream fos = new FileOutputStream(path))
                { 
                    ks.store(fos, password.toCharArray());
                }

                this.logger.debug("Successfully created keystore at " + path);
            }

            return ks;
        }
        catch (FileNotFoundException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + 
                    ", this is most likely a permissions problem, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (NoSuchAlgorithmException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path +  
                    ", this might be a Java installation problem, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (CertificateException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + 
                    ", there is a problem with a certificate, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (IOException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + ", there is a problem reading or writing " +
                    "the keystore or the configured password is incorrect, exception " + e.getClass().getSimpleName() +
                    ": " + e.getMessage());
            throw e;
        }
        catch (KeyStoreException e)
        {
            this.logger.error("Failed creating or loading keystore at " + path + ", from a generic keystore exception.");
            throw e;
        }
    }
    
    public void shutdown()
    {
        
    }
}
