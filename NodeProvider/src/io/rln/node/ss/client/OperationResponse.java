/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.client;

/**
 * Response from calling operations.
 */
public class OperationResponse
{
    /** Message possibly indicating a reason for failure. */
    private String message;
    
    /** HTTP response code. */
    private int code;
    
    public OperationResponse() 
    {
        this.code = 200;
    }
    
    public OperationResponse(int code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public int getCode()
    {
        return this.code;
    }
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    /**
     * Whether the operation was successful.
     * 
     * @return true if successful
     */
    public boolean ok()
    {
        return this.code == 200;
    }
    
    public String error()
    {
        return this.code + ": " + this.message;
    }
}
