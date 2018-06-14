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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordDirectoryNameConstants;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Implementation of the IRecordColumnQueryPart interface for a directory column
 */
public class RecordColumnDirectoryQueryPart extends AbstractRecordColumnQueryPart
{
    // Constants
    private static final String DIRECTORY_SELECT_QUERY_PART = "directory.title";
    private static final String DIRECTORY_FROM_QUERY_PART = StringUtils.EMPTY;
    private static final String DIRECTORY_JOIN_QUERY_PART = StringUtils.EMPTY;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnSelectQuery( )
    {
        return DIRECTORY_SELECT_QUERY_PART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnFromQuery( )
    {
        return DIRECTORY_FROM_QUERY_PART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRecordColumnJoinQueries( )
    {
        return Arrays.asList( DIRECTORY_JOIN_QUERY_PART );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object> getMapRecordColumnValues( DAOUtil daoUtil )
    {
        Map<String, Object> mapRecordColumnValues = new LinkedHashMap<>( );
        String strDirectoryTitle = daoUtil.getString( RecordDirectoryNameConstants.COLUMN_DIRECTORY_TITLE );
        mapRecordColumnValues.put( RecordDirectoryNameConstants.COLUMN_DIRECTORY_TITLE, strDirectoryTitle );

        return mapRecordColumnValues;
    }
}
