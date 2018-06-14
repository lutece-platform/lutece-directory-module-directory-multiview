/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.RecordParameters;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart;

/**
 * Abstract class for the implementation of the IRecordPanelQueryPart
 */
public abstract class AbstractRecordPanelInitializerQueryPart implements IRecordPanelInitializerQueryPart
{
    // Variables
    private String _strRecordPanelInitializerSelectQuery = StringUtils.EMPTY;
    private String _strRecordPanelInitializerFromQuery = StringUtils.EMPTY;
    private List<String> _listRecordPanelInitializerJoinQueries = new ArrayList<>( );

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void buildRecordPanelInitializerQuery( RecordParameters recordParameters );

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordPanelInitializerSelectQuery( )
    {
        return _strRecordPanelInitializerSelectQuery;
    }

    /**
     * Set the select query part of the RecordPanelInitializer
     * 
     * @param strRecordPanelInitializerSelectQuery
     *            The select query part of the RecordPanelInitializer
     */
    protected void setRecordPanelInitializerSelectQuery( String strRecordPanelInitializerSelectQuery )
    {
        _strRecordPanelInitializerSelectQuery = strRecordPanelInitializerSelectQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordPanelInitializerFromQuery( )
    {
        return _strRecordPanelInitializerFromQuery;
    }

    /**
     * Set the From query part of the RecordPanelInitializer
     * 
     * @param strRecordPanelInitializerFromQuery
     *            The from query part of the RecordPanelInitializer to set
     */
    protected void setRecordPanelInitializerFromQuery( String strRecordPanelInitializerFromQuery )
    {
        _strRecordPanelInitializerFromQuery = strRecordPanelInitializerFromQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRecordPanelInitializerJoinQueries( )
    {
        return _listRecordPanelInitializerJoinQueries;
    }

    /**
     * Set the list of all join query parts of the RecordPanelInitializer
     * 
     * @param listRecordPanelInitializerJoinQueries
     *            The list of all join query parts of RecordPanelInitializer
     */
    protected void setRecordPanelInitializerJoinQueriesList( List<String> listRecordPanelInitializerJoinQueries )
    {
        _listRecordPanelInitializerJoinQueries = listRecordPanelInitializerJoinQueries;
    }
}
