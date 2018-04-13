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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.standalone;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;

/**
 * Query builder utility class for RecordFilterStandalone class
 */
public final class RecordFilterStandaloneQueryBuilder
{
    /**
     * Constructor
     */
    private RecordFilterStandaloneQueryBuilder( )
    {

    }

    /**
     * Populate the given string builder with all the join query parts from the given list of record filter standalone
     * 
     * @param stringBuilderJoinQueryPart
     *            The string builder to populate with the join query parts from the list of all record filter standalone
     * @param listRecordFilterQueryPartStandalone
     *            The list of all record filter standalone to retrieve the list of join query parts from
     */
    public static void buildRecordFilterStandaloneQueryParts( StringBuilder stringBuilderJoinQueryPart,
            List<IRecordFilterStandaloneQueryPart> listRecordFilterQueryPartStandalone )
    {
        for ( IRecordFilterStandaloneQueryPart recordFilterQueryPartStandalone : listRecordFilterQueryPartStandalone )
        {
            List<String> listRecordFilterStandaloneJoinQueries = recordFilterQueryPartStandalone.getRecordFilterStandaloneJoinQueries( );
            if ( listRecordFilterStandaloneJoinQueries != null && !listRecordFilterStandaloneJoinQueries.isEmpty( ) )
            {
                for ( String strJoinQueryPart : listRecordFilterStandaloneJoinQueries )
                {
                    if ( StringUtils.isNotBlank( strJoinQueryPart ) )
                    {
                        stringBuilderJoinQueryPart.append( strJoinQueryPart );
                        stringBuilderJoinQueryPart.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
                    }
                }
            }
        }
    }
}
