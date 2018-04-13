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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Class used to build the query of the filter
 */
public final class RecordFilterQueryBuilder
{
    // Constants
    private static final String DEFAULT_QUERY_VALUE = StringUtils.EMPTY;
    private static final String KEY_NAME_SEPARATOR = "$";
    private static final String DEFAULT_ITEM_VALUE = "-1";

    /**
     * Constructor
     */
    private RecordFilterQueryBuilder( )
    {

    }

    /**
     * Build the query of a RecordFilter from the specified pattern and the RecordFilterItem to retrieve the values from
     * 
     * @param strRecordFilterQueryPattern
     *            The pattern to use for building the query of the RecordFilter
     * @param recordFilterItem
     *            The RecordFilterItem to retrieve the values from for format the pattern
     * @return the query formatted with all parameter replace by their values or an empty String if a parameter is missing in the RecordFilterItem
     */
    public static String buildRecordFilterQuery( String strRecordFilterQueryPattern, RecordFilterItem recordFilterItem )
    {
        String strRecordFilterQuery = DEFAULT_QUERY_VALUE;

        if ( StringUtils.isNotBlank( strRecordFilterQueryPattern ) && recordFilterItem != null )
        {
            Map<String, Object> mapFilterItemNameValue = recordFilterItem.getMapFilterNameValues( );

            if ( mapFilterItemNameValue != null && !mapFilterItemNameValue.isEmpty( ) )
            {
                strRecordFilterQuery = strRecordFilterQueryPattern;

                for ( Entry<String, Object> entryFilterItemNameValue : mapFilterItemNameValue.entrySet( ) )
                {
                    String strFilterItemName = entryFilterItemNameValue.getKey( );
                    Object objFilterItemValue = entryFilterItemNameValue.getValue( );

                    // If a value is missing we will interrupt the processing for the current filter and
                    // reset the current query to avoid SQL error
                    if ( objFilterItemValue == null || String.valueOf( objFilterItemValue ).equals( DEFAULT_ITEM_VALUE ) )
                    {
                        strRecordFilterQuery = DEFAULT_QUERY_VALUE;
                        break;
                    }
                    else
                    {
                        String strFilterItemValue = String.valueOf( objFilterItemValue );
                        String strFilterItemNameBuilt = buildFilterNameToReplace( strFilterItemName );
                        strRecordFilterQuery = strRecordFilterQuery.replaceAll( Pattern.quote( strFilterItemNameBuilt ), strFilterItemValue );
                    }
                }
            }
        }

        return strRecordFilterQuery;
    }

    /**
     * Format the filter name to replace in the query pattern of the filter
     * 
     * @param strFilterItemName
     *            The name of the filter item to format
     * @return the formatted name of the filter item
     */
    private static String buildFilterNameToReplace( String strFilterItemName )
    {
        StringBuilder stringBuilderFilterName = new StringBuilder( );
        stringBuilderFilterName.append( KEY_NAME_SEPARATOR ).append( strFilterItemName ).append( KEY_NAME_SEPARATOR );

        return stringBuilderFilterName.toString( );
    }
}
