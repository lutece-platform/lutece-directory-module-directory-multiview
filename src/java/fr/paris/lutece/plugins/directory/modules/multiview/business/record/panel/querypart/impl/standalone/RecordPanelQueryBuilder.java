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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.querypart.impl.standalone;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;

/**
 * Query builder utility class for RecordFilterPanel
 */
public final class RecordPanelQueryBuilder
{
    /**
     * Constructor
     */
    private RecordPanelQueryBuilder( )
    {

    }

    /**
     * Build the select query part of the record panel
     * 
     * @param recordPanelQueryPart
     *            The record panel to use
     * @return the select query part of the record panel
     */
    public static String buildPanelSelectQueryPart( IRecordPanelQueryPart recordPanelQueryPart )
    {
        String strPanelSelectQueryPart = StringUtils.EMPTY;

        if ( recordPanelQueryPart != null )
        {
            String strRecordPanelSelectQueryPart = recordPanelQueryPart.getRecordFilterStandaloneSelectQuery( );
            if ( StringUtils.isNotBlank( strRecordPanelSelectQueryPart ) )
            {
                strPanelSelectQueryPart = strRecordPanelSelectQueryPart;
            }
        }

        return strPanelSelectQueryPart;
    }

    /**
     * Build the from query part of record panel
     * 
     * @param recordPanelQueryPart
     *            The query part of the record panel to use
     * @return the from query part of the record panel
     */
    public static String buildPanelFromQueryPart( IRecordPanelQueryPart recordPanelQueryPart )
    {
        String strRecordFilterStandaloneFromQueryPart = StringUtils.EMPTY;

        if ( recordPanelQueryPart != null )
        {
            String strRecordPanelFromQueryPart = recordPanelQueryPart.getRecordFilterStandaloneFromQuery( );
            if ( StringUtils.isNotBlank( strRecordPanelFromQueryPart ) )
            {
                strRecordFilterStandaloneFromQueryPart = strRecordPanelFromQueryPart;
            }
        }

        return strRecordFilterStandaloneFromQueryPart;
    }

    /**
     * Populate the given string builder with all the join queries from the given record panel
     * 
     * @param stringBuilderJoinQueryPart
     *            The string builder to populate with all join queries parts of the record panel
     * @param recordPanelQueryPart
     *            The record panel to retrieve the list of join query parts from
     */
    public static void buildPanelJoinQueryParts( StringBuilder stringBuilderJoinQueryPart, IRecordPanelQueryPart recordPanelQueryPart )
    {
        List<String> listRecordFilterPanelJoinQueries = recordPanelQueryPart.getRecordFilterStandaloneJoinQueries( );
        if ( listRecordFilterPanelJoinQueries != null && !listRecordFilterPanelJoinQueries.isEmpty( ) )
        {
            for ( String strRecordPanelJoinQuery : listRecordFilterPanelJoinQueries )
            {
                if ( StringUtils.isNotBlank( strRecordPanelJoinQuery ) )
                {
                    stringBuilderJoinQueryPart.append( strRecordPanelJoinQuery );
                    stringBuilderJoinQueryPart.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
                }
            }
        }
    }
}
