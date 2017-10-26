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

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.web.constants.Parameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


public class DirectoryMultiviewService
{
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_STATE_WORKFLOW = "search_state_workflow";
    private static final String PARAMETER_ID_FILTER_PERIOD = "search_open_since";
    
    private static final String MARK_DEFAULT_WORKFLOW_STATE_ID = "search_state_workflow_id_default";
    private static final String MARK_DEFAULT_ID_DIRECTORY = "search_directory_id_default";
    private static final String MARK_DEFAULT_PERIOD = "search_period_id_default";
    private static final String MARK_RECORD = "record";
    private static final String MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD = "map_id_entry_list_record_field";
    private static final String MARK_PRECISIONS = "precisions";
    
    /**
     * Set the filter. Return true if filter changed; false otherwise
     * @param filter
     * @param request
     * @return true if filter changed, false otherwise
     */
    public static boolean getRecordAssignmentFilter ( RecordAssignmentFilter filter, HttpServletRequest request )
    {
        //Parameters for filtering
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdWorkflowState = request.getParameter( PARAMETER_ID_STATE_WORKFLOW );
        String strIdPeriodParameter = request.getParameter( PARAMETER_ID_FILTER_PERIOD );
        String strSortedAttributeName =  request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = request.getParameter( Parameters.SORTED_ASC );
        
        int nIdWorkflowState = DirectoryMultiviewUtils.convertStringToInt( strIdWorkflowState );
        int nIdDirectory = DirectoryMultiviewUtils.convertStringToInt( strIdDirectory );
        int nIdPeriodParameter = DirectoryMultiviewUtils.convertStringToInt( strIdPeriodParameter );

        if ( filter.getDirectoryId( ) != nIdDirectory 
                || filter.getStateId( )!= nIdWorkflowState 
                || filter.getNumberOfDays( ) != nIdPeriodParameter
                || filter.isAsc() != Boolean.getBoolean( strAscSort )
                || !filter.getOrderBy().equals( strSortedAttributeName ) )
        {
             //Construct the filter based on record assignments.
            if ( nIdDirectory > 0 )
            {
                filter.setDirectoryId( nIdDirectory );
            }
            if ( nIdWorkflowState > 0 )
            {
                filter.setStateId( nIdWorkflowState );
            }
            if ( nIdPeriodParameter > 0 )
            {
                filter.setNumberOfDays( nIdPeriodParameter );
            }
            if ( strSortedAttributeName != null )
            {
                filter.setOrderBy( strSortedAttributeName );
            }
            if ( strAscSort != null )
            {
                filter.setAsc( Boolean.valueOf( strAscSort) );
            }
            return true;
        }
        return false;
    }
    
    public static void populateDefaultFilterMarkers( RecordAssignmentFilter filter, Map<String, Object> model )
    {
        if ( filter.getDirectoryId( ) > 0 )
        {
            model.put( MARK_DEFAULT_ID_DIRECTORY, filter.getDirectoryId( ) );
        }
        if ( filter.getStateId( ) > 0 )
        {
            model.put( MARK_DEFAULT_WORKFLOW_STATE_ID, filter.getStateId( ));
        }
        if ( filter.getNumberOfDays( ) > 0 )
        {
            model.put( MARK_DEFAULT_PERIOD, filter.getNumberOfDays( ) );
        }
    }
    
    /**
     * 
     * @param resourceActions
     * @param listPrecisions
     * @param locale 
     */
    public static void populateRecordPrecisions( List<Map<String,Object>> resourceActions, List<IEntry> listPrecisions, Locale locale )
    {
        for ( Map<String,Object> mapResourceActions : resourceActions )
        {
            Record record = (Record)mapResourceActions.get( MARK_RECORD );
            Map<String, List<RecordField>> mapRecordFields = (Map<String, List<RecordField>>) mapResourceActions.get( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD );
            List<String> precisions = new ArrayList<>();
            for ( IEntry entry : listPrecisions )
            {
                
                if ( entry.getDirectory( ).getIdDirectory( ) == record.getDirectory( ).getIdDirectory( ) )
                {
                    List<RecordField>  listRecordField = mapRecordFields.get( Integer.toString( entry.getIdEntry( ) ) );
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
