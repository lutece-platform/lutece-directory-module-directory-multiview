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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Factory for the RecordListPanel
 */
public final class RecordListPanelFactory
{
    // Variables
    private static final List<IRecordListPanel> _listRecordListPanel = SpringContextService.getBeansOfType( IRecordListPanel.class );
    
    /**
     * Constructor
     */
    private RecordListPanelFactory( )
    {
        
    }
    
    /**
     * Create the list of RecordListPanel of the factory
     * 
     * @param request
     *          The HttpServletRequest to retrieve the parameter values from
     * @param recordAssignmentfilter
     *          The filter to use for filtering the records
     * @param collectionDirectory
     *          The collection of Directory to retrieve the records from
     * @return the list of RecordListPanel
     */
    public static List<IRecordListPanel> createRecordListPanelList( HttpServletRequest request, RecordAssignmentFilter recordAssignmentfilter, Collection<Directory> collectionDirectory )
    {
        for ( IRecordListPanel recordListPanel : _listRecordListPanel )
        {
            recordListPanel.configureRecordListPanel( request, recordAssignmentfilter, collectionDirectory );
        }
        
        // Sort the list of panel by their position
        Collections.sort( _listRecordListPanel, new RecordListPanelComparator( ) );
        
        return _listRecordListPanel;
    }
}
