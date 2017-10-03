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
package fr.paris.lutece.plugins.directory.modules.multiview.business;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.io.Serializable;

/**
 * This is the business class for the object DirectoryViewFilter
 */
public class DirectoryViewFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;
    private int _nIdDirectory;
    private String _strDirectoryTitle;

    @NotEmpty( message = "#i18n{module.directory.multiview.validation.directoryfilter.Name.notEmpty}" )
    @Size( max = 255, message = "#i18n{module.directory.multiview.validation.directoryfilter.Name.size}" )
    private String _strName;

    @Size( max = 50, message = "#i18n{module.directory.multiview.validation.directoryfilter.Style.size}" )
    private String _strStyle;

    private int _nPosition;
    private int _nIdEntryTitle;
    private String _strEntryTitle;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Name
     * 
     * @return The Name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * 
     * @param strName
     *            The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Style
     * 
     * @return The Style
     */
    public String getStyle( )
    {
        return _strStyle;
    }

    /**
     * Sets the Style
     * 
     * @param strStyle
     *            The Style
     */
    public void setStyle( String strStyle )
    {
        _strStyle = strStyle;
    }

    /**
     * Returns the Position
     * 
     * @return The Position
     */
    public int getPosition( )
    {
        return _nPosition;
    }

    /**
     * Sets the Position
     * 
     * @param nPosition
     *            The Position
     */
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * Returns the directory id
     * 
     * @return _nIdDirectory
     */
    public int getIdDirectory( )
    {
        return _nIdDirectory;
    }

    /**
     * Sets the _nIdDirectory
     * 
     * @param nIdDirectory
     *            The Directory id
     */
    public void setIdDirectory( int nIdDirectory )
    {
        this._nIdDirectory = nIdDirectory;
    }

    /**
     * Returns the directory title
     * 
     * @return _strDirectoryTitle
     */
    public String getDirectoryTitle( )
    {
        return _strDirectoryTitle;
    }

    /**
     * Sets the _strDirectoryTitle
     * 
     * @param _strDirectoryTitle
     *            The Directory title
     */
    public void setDirectoryTitle( String _strDirectoryTitle )
    {
        this._strDirectoryTitle = _strDirectoryTitle;
    }

    /**
     * get the _nIdEntryTitle
     * 
     * @return _nIdEntryTitle The Id of the entry to use for the title of the record
     */
    public int getIdEntryTitle( )
    {
        return _nIdEntryTitle;
    }

    /**
     * Sets the _nIdEntryTitle
     * 
     * @param _nIdEntryTitle
     *            The Id of the entry to use for the title of the record
     */
    public void setIdEntryTitle( int _nIdEntryTitle )
    {
        this._nIdEntryTitle = _nIdEntryTitle;
    }

    /**
     * get the _strEntryTitle
     * 
     * @return _strEntryTitle The title the entry to use for the title of the record
     */
    public String getEntryTitle( )
    {
        return _strEntryTitle;
    }

    /**
     * Sets the _strEntryTitle
     * 
     * @param _strEntryTitle
     *            The entry title
     */
    public void setEntryTitle( String _strEntryTitle )
    {
        this._strEntryTitle = _strEntryTitle;
    }

}
