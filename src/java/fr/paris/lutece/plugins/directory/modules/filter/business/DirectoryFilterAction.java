/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */ 
package fr.paris.lutece.plugins.directory.modules.filter.business;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.io.Serializable;

/**
 * This is the business class for the object DirectoryFilterAction
 */ 
public class DirectoryFilterAction implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    private int _nIdDirectoryFilter;
    
    private int _nIdAction;
    
    private int _nPosition;
    
    @Size( max = 50 , message = "#i18n{module.directory.filter.validation.directoryfilteraction.Style.size}" ) 
    private String _strStyle;
    
    private int _nNbItem;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the IdDirectoryFilter
     * @return The IdDirectoryFilter
     */
    public int getIdDirectoryFilter( )
    {
        return _nIdDirectoryFilter;
    }

    /**
     * Sets the IdDirectoryFilter
     * @param nIdDirectoryFilter The IdDirectoryFilter
     */ 
    public void setIdDirectoryFilter( int nIdDirectoryFilter )
    {
        _nIdDirectoryFilter = nIdDirectoryFilter;
    }
    
    /**
     * Returns the IdAction
     * @return The IdAction
     */
    public int getIdAction( )
    {
        return _nIdAction;
    }

    /**
     * Sets the IdAction
     * @param nIdAction The IdAction
     */ 
    public void setIdAction( int nIdAction )
    {
        _nIdAction = nIdAction;
    }
    
    /**
     * Returns the Position
     * @return The Position
     */
    public int getPosition( )
    {
        return _nPosition;
    }

    /**
     * Sets the Position
     * @param nPosition The Position
     */ 
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }
    
    /**
     * Returns the Style
     * @return The Style
     */
    public String getStyle( )
    {
        return _strStyle;
    }

    /**
     * Sets the Style
     * @param strStyle The Style
     */ 
    public void setStyle( String strStyle )
    {
        _strStyle = strStyle;
    }
    
    /**
     * Returns the NbItem
     * @return The NbItem
     */
    public int getNbItem( )
    {
        return _nNbItem;
    }

    /**
     * Sets the NbItem
     * @param nNbItem The NbItem
     */ 
    public void setNbItem( int nNbItem )
    {
        _nNbItem = nNbItem;
    }
}
