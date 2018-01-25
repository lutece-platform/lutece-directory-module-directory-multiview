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
package fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.portal.service.admin.AdminUserService;

/**
 * Helper Class for the Implementation of the IColumnFilter based on RecordField value
 */
public class RecordFieldHelperColumnFilter
{
    /**
     * Create the map which associate for each id record its RecordAssignement
     * 
     * @param request
     *          The HttpServletRequest to retrieve the user data from
     * @return the map which associate for each id record its RecordAssignement
     */
    public Map<Integer, RecordAssignment> createRecordAssignmentFilerMap( HttpServletRequest request )
    {
        // Retrieve the list of all RecordAssignment
        RecordAssignmentFilter recordAssignmentFilter = new RecordAssignmentFilter( );
        recordAssignmentFilter.setUserUnitIdList( AssignmentService.findAllSubUnitsIds( AdminUserService.getAdminUser( request ) ) );
        recordAssignmentFilter.setActiveDirectory( true );
        recordAssignmentFilter.setActiveAssignmentRecordsOnly( true );
        recordAssignmentFilter.setLastActiveAssignmentRecordsOnly( true );

        List<RecordAssignment> recordAssignmentList = AssignmentService.getRecordAssignmentFiltredList( recordAssignmentFilter );

        // Populate the map with the last AssignmentRecord for each Record
        Map<Integer, RecordAssignment> recordAssignmentMap = new LinkedHashMap<>( );
        for ( RecordAssignment assignedRecord : recordAssignmentList )
        {
            if ( !recordAssignmentMap.containsKey( assignedRecord.getIdRecord( ) )
                    || recordAssignmentMap.get( assignedRecord.getIdRecord( ) ).getAssignmentDate( ).before( assignedRecord.getAssignmentDate( ) ) )
            {
                // Keep only the last one
                recordAssignmentMap.put( assignedRecord.getIdRecord( ), assignedRecord );

            }
        }
        
        return recordAssignmentMap;
    }
}
