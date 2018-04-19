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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.RecordParameters;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordEntryNameConstants;

/**
 * Implementation of the IRecordFilterQueryPart for an Entry filter
 */
public class RecordFilterEntryQueryPart extends AbstractRecordFilterQueryPart
{
    // Constants
    private static final int DEFAULT_POSITION = NumberUtils.INTEGER_MINUS_ONE;
    private static final String ENTRY_VALUE_QUERY_PATTERN = "column_%1$s.column_%1$s_value = ";
    private static final String ENTRY_VALUE_NAME_PATTERN = "'$column_%s$'";

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildRecordFilterQuery( RecordParameters recordParameters )
    {
        int nPosition = DEFAULT_POSITION;

        if ( recordParameters != null )
        {
            Map<String, Object> mapRecordParametersNameValues = recordParameters.getRecordParametersMap( );
            if ( mapRecordParametersNameValues != null && !mapRecordParametersNameValues.isEmpty( ) )
            {
                nPosition = findFilterColumnPosition( mapRecordParametersNameValues );
            }
        }

        StringBuilder stringBuilderEntryQueryPattern = new StringBuilder( );
        if ( nPosition != DEFAULT_POSITION )
        {
            String strEntryValueQueryPattern = String.format( ENTRY_VALUE_QUERY_PATTERN, nPosition );
            String strEntryNamePattern = String.format( ENTRY_VALUE_NAME_PATTERN, nPosition );

            stringBuilderEntryQueryPattern.append( strEntryValueQueryPattern );
            stringBuilderEntryQueryPattern.append( strEntryNamePattern );
        }

        setRecordFilterQuery( RecordFilterQueryBuilder.buildRecordFilterQuery( stringBuilderEntryQueryPattern.toString( ), recordParameters ) );
    }

    /**
     * Find the position of the column for the filter from the given values
     * 
     * @param mapRecordParametersNameValues
     *            The map used to retrieve the position of the column of the filter
     * @return the position of the column for the filter from the given values or the {@link DEFAULT_POSITION} if not found
     */
    private int findFilterColumnPosition( Map<String, Object> mapRecordParametersNameValues )
    {
        int nPosition = DEFAULT_POSITION;

        Set<String> setParameterName = mapRecordParametersNameValues.keySet( );
        for ( String strParameterName : setParameterName )
        {
            if ( strParameterName.startsWith( RecordEntryNameConstants.FILTER_ENTRY_BASE_NAME_PATTERN ) )
            {
                String strColumnNumber = strParameterName.replaceFirst( RecordEntryNameConstants.FILTER_ENTRY_BASE_NAME_PATTERN, StringUtils.EMPTY );
                nPosition = NumberUtils.toInt( strColumnNumber, NumberUtils.INTEGER_MINUS_ONE );
                break;
            }
        }

        return nPosition;
    }
}
