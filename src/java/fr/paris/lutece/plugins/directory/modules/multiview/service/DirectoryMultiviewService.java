/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.IRecordFilterParameter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;

/**
 * Service for the module-directory-multiview
 */
public class DirectoryMultiviewService implements IDirectoryMultiviewService
{
    // Marks
    private static final String MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD = "map_id_entry_list_record_field";
    private static final String MARK_PRECISIONS = "precisions";

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordAssignmentFilter getRecordAssignmentFilter( HttpServletRequest request, List<IRecordFilterParameter> listRecordFilterParameter )
    {
        RecordAssignmentFilter filter = new RecordAssignmentFilter( );

        for ( IRecordFilterParameter recordFilterParameter : listRecordFilterParameter )
        {
            IRecordFilterItem recordFilterItem = recordFilterParameter.getRecordFilterItem( );
            recordFilterItem.setItemValue( filter, recordFilterParameter.getValueFromRequest( request ) );
        }

        return filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateDefaultFilterMarkers( RecordAssignmentFilter filter, List<IRecordFilterParameter> listRecordFilterParameter, Map<String, Object> model )
    {
        for ( IRecordFilterParameter recordFilterParameter : listRecordFilterParameter )
        {
            IRecordFilterItem recordFilterItem = recordFilterParameter.getRecordFilterItem( );

            Object objectFilterValue = recordFilterItem.getItemValue( filter );
            model.put( recordFilterParameter.getRecordFilterModelMark( ), objectFilterValue );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateRecordPrecisions( List<Map<String, Object>> resourceActions, List<IEntry> listPrecisions, Locale locale )
    {
        for ( Map<String, Object> mapResourceActions : resourceActions )
        {
            Record record = (Record) mapResourceActions.get( DirectoryMultiviewConstants.MARK_RECORD );
            Map<String, List<RecordField>> mapRecordFields = (Map<String, List<RecordField>>) mapResourceActions.get( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD );
            List<String> precisions = new ArrayList<>( );
            for ( IEntry entry : listPrecisions )
            {
                if ( entry.getDirectory( ).getIdDirectory( ) == record.getDirectory( ).getIdDirectory( ) )
                {
                    List<RecordField> listRecordField = mapRecordFields.get( Integer.toString( entry.getIdEntry( ) ) );
                    for ( RecordField field : listRecordField )
                    {
                        precisions.add( field.getValue( ) );
                    }
                }
            }
            mapResourceActions.put( MARK_PRECISIONS, precisions );
        }
    }
}