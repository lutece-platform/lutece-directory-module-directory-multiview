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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordlistpanel;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;

/**
 * Implementation of the IRecordListPanel interface for the MyProcedures panel
 */
public class RecordListMyRecordsPanel extends AbstractRecordListPanel
{
    // Constants
    private static final String MY_RECORDS_PANEL_NAME = "my_records";
    private static final String PROPERTY_MY_RECORDS_PANEL_TITLE = "module.directory.multiview.records_list.label_my_records_panel";
    private static final int DEFAULT_USER_IDENTIFIER_VALUE = NumberUtils.INTEGER_MINUS_ONE;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureRecordListPanel( HttpServletRequest request, RecordAssignmentFilter recordAssignmentFilter, Collection<Directory> collectionDirectory )
    {
        // Retrieve the id of the user from the request
        int nUserIdentifier = getAdminUserIdFromRequest( request );

        // Create the filter used for the search of the records
        RecordAssignmentFilter recordAssignmentFilterClone = new RecordAssignmentFilter( recordAssignmentFilter );
        recordAssignmentFilterClone.getListAssignedUserId( ).add( nUserIdentifier );

        // Initialization of the panel
        initPanel( request, collectionDirectory, MY_RECORDS_PANEL_NAME, PROPERTY_MY_RECORDS_PANEL_TITLE, recordAssignmentFilterClone );
    }

    /**
     * Return the identifier of the AdminUser from the request
     * 
     * @param request
     *            The request to retrieve the user from
     * @return the identifier of the AdminUser from the request
     */
    private int getAdminUserIdFromRequest( HttpServletRequest request )
    {
        int nUserIdentifier = DEFAULT_USER_IDENTIFIER_VALUE;

        AdminUser adminUser = AdminUserService.getAdminUser( request );
        if ( adminUser != null )
        {
            nUserIdentifier = adminUser.getUserId( );
        }

        return nUserIdentifier;
    }
}
