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

import java.util.Arrays;
import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.standalone.IRecordFilterStandaloneQueryPart;

/**
 * Interface used for the record filter associated to a record panel
 */
public interface IRecordPanelQueryPart extends IRecordFilterStandaloneQueryPart
{
    String STANDALONE_SELECT_QUERY = "directory.id_directory, record.id_record";
    String STANDALONE_FROM_QUERY = "directory_directory AS directory";
    String DIRECTORY_RECORDS_JOIN_QUERY = "INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory";

    /**
     * Return the select query part of the record filter standalone query part
     * 
     * @return the select query part of the record filter standalone query part
     */
    default String getRecordFilterStandaloneSelectQuery( )
    {
        return STANDALONE_SELECT_QUERY;
    }

    /**
     * Return the from query part of the record filter standalone query part
     * 
     * @return the from query part of the record filter standalone query part
     */
    default String getRecordFilterStandaloneFromQuery( )
    {
        return STANDALONE_FROM_QUERY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default List<String> getRecordFilterStandaloneJoinQueries( )
    {
        return Arrays.asList( DIRECTORY_RECORDS_JOIN_QUERY );
    }
}
