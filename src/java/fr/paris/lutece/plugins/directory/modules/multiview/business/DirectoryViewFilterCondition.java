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
import java.util.HashMap;

/**
 * This is the business class for the object DirectoryViewFilterCondition
 */
public class DirectoryViewFilterCondition implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;
    private int _nIdDirectoryFilter;
    private int _nIdEntry;
    private String _strEntryTitle;
    private int _nOperator;
    private int _nFilterType;
    private String _strOperatorName;
    private String _strFilterTypeName;

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
     * Returns the IdDirectoryFilter
     * 
     * @return The IdDirectoryFilter
     */
    public int getIdDirectoryFilter( )
    {
        return _nIdDirectoryFilter;
    }

    /**
     * Sets the IdDirectoryFilter
     * 
     * @param nIdDirectoryFilter
     *            The IdDirectoryFilter
     */
    public void setIdDirectoryFilter( int nIdDirectoryFilter )
    {
        _nIdDirectoryFilter = nIdDirectoryFilter;
    }

    /**
     * Returns the IdEntry
     * 
     * @return The IdEntry
     */
    public int getIdEntry( )
    {
        return _nIdEntry;
    }

    /**
     * Sets the IdEntry
     * 
     * @param nIdEntry
     *            The IdEntry
     */
    public void setIdEntry( int nIdEntry )
    {
        _nIdEntry = nIdEntry;
    }

    /**
     * Returns the Operator
     * 
     * @return The Operator
     */
    public int getOperator( )
    {
        return _nOperator;
    }

    /**
     * Sets the Operator
     * 
     * @param nOperator
     *            The Operator
     */
    public void setOperator( int nOperator )
    {
        _nOperator = nOperator;
    }

    /**
     * Returns the FilterType
     * 
     * @return The FilterType
     */
    public int getFilterType( )
    {
        return _nFilterType;
    }

    /**
     * Sets the FilterType
     * 
     * @param nFilterType
     *            The FilterType
     */
    public void setFilterType( int nFilterType )
    {
        _nFilterType = nFilterType;
    }

    /**
     * Returns the Entry title
     * 
     * @return The Entry title
     */
    public String getEntryTitle( )
    {
        return _strEntryTitle;
    }

    /**
     * Sets the Entry title
     * 
     * @param Entry
     *            title The Entry title
     */
    public void setEntryTitle( String _strEntryTitle )
    {
        this._strEntryTitle = _strEntryTitle;
    }

    /**
     * Returns the operator name
     * 
     * @return The operator name
     */
    public String getOperatorName( )
    {
        return _strOperatorName;
    }

    /**
     * Sets the operator name
     * 
     * @param name
     */
    public void setOperatorName( String _strOperatorName )
    {
        this._strOperatorName = _strOperatorName;
    }

    /**
     * Returns the filter type name
     * 
     * @return The filter type name
     */
    public String getFilterTypeName( )
    {
        return _strFilterTypeName;
    }

    /**
     * Sets the filter type name
     * 
     * @param name
     */
    public void setFilterTypeName( String _strFilterTypeName )
    {
        this._strFilterTypeName = _strFilterTypeName;
    }

}
