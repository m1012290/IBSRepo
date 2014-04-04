package com.shrinfo.ibs.cmn.mvc;

import javax.servlet.ServletConfig;

/**
 * An interface for initialisation activities in the web application. Each initialisation task must implement this interface.
 * It is recommended that every implementation of this interface is named ending with "WITask".
 */
public interface IWebInitTask{
    public void initialize( ServletConfig servletConfig );
}