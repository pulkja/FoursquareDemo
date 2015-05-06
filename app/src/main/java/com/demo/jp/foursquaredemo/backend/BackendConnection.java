package com.demo.jp.foursquaredemo.backend;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jari Pulkkinen on 5.5.2015.
 *
 * Backend connection interface for connection and reading the response.
 */
public interface BackendConnection {

    /**
     * Connect to the backend.
     * @return response code
     */
    public int connect(final String pRequestParams) throws IOException;

    /**
     * Disconnect the connection.
     */
    public void disconnect();

    /**
     * Get the input stream for reading data from the backend response.
     * @return
     */
    public InputStream getInputStream() throws IOException;

}
