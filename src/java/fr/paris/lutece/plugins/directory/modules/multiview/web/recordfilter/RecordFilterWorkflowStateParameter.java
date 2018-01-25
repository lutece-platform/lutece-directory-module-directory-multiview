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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterWorkflowStateItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.IColumnFilter;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Implementation of IRecordFilterParameter for the workflow state filter
 */
public class RecordFilterWorkflowStateParameter implements IRecordFilterParameter
{
    // Constants
    private static final String PARAMETER_WORKFLOW_STATE_FILTER = "search_state_workflow";
    private static final String MARK_WORKFLOW_STATE_FILTER = "id_workflow_state";

    // Variables
    private final RecordFilterWorkflowStateItem _recordFilterWorkflowStateItem;
    private final WorkflowStateColumnFilter _workflowStateColumnFilter;

    /**
     * Constructor
     * 
     * @param request
     *          The HttpServletRequest
     */
    public RecordFilterWorkflowStateParameter( HttpServletRequest request )
    {
        _recordFilterWorkflowStateItem = new RecordFilterWorkflowStateItem( );
        _workflowStateColumnFilter = new WorkflowStateColumnFilter( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_WORKFLOW_STATE_FILTER );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterWorkflowStateItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return MARK_WORKFLOW_STATE_FILTER;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IColumnFilter getColumnFilter( )
    {
        return _workflowStateColumnFilter;
    }
    
    /**
     * Implementation of the IColumnFilter interface for the AssignedUnit
     */
    private final class WorkflowStateColumnFilter implements IColumnFilter
    {
        // Constants
        private static final String STATE_CODE_ATTRIBUTE = "id";
        private static final String STATE_NAME_ATTRIBUTE = "name";
        private static final int DEFAULT_DIRECTORY_VALUE = -1;
        
        // Variables
        private final List<State> _listWorkflowState;
        private int _nIdDirectory = NumberUtils.INTEGER_MINUS_ONE;
        private HttpServletRequest _request;
        private String _strFilterTemplate;
        
        /**
         * Constructor
         * 
         * @param request
         *          The HttpServletRequest
         */
        WorkflowStateColumnFilter( HttpServletRequest request )
        {
            _request = request;
            _listWorkflowState = new ArrayList<>( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void populateListValue( )
        {
            Directory directory = DirectoryHome.findByPrimaryKey( _nIdDirectory, DirectoryUtils.getPlugin( ) );
            
            if ( directory != null )
            {
                _listWorkflowState.addAll( WorkflowService.getInstance( ).getAllStateByWorkflow( directory.getIdWorkflow( ),
                        AdminUserService.getAdminUser( _request ) ) );
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceList createReferenceList( )
        {
            ReferenceListFactory referenceListFactory = new ReferenceListFactory( _listWorkflowState, STATE_CODE_ATTRIBUTE, STATE_NAME_ATTRIBUTE );
            
            return referenceListFactory.createReferenceList( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void buildTemplate( RecordAssignmentFilter filter, HttpServletRequest request )
        {
            String strTemplateResult = StringUtils.EMPTY;
            
            // For the list of workflow state we will based it on the directory of the filter
            _nIdDirectory = filter.getDirectoryId( );
            
            // We will overwrite the request of the object for retrieve the RBAC right of the user
            _request = request;
            populateListValue( );
            
            // If no directory has been selected we will return an empty list
            ReferenceList referenceList = new ReferenceList( );
            if ( _nIdDirectory != DEFAULT_DIRECTORY_VALUE )
            {
                referenceList = createReferenceList( );
            }

            Map<String, Object> model = new LinkedHashMap<>( );
            model.put( MARK_FILTER_LIST, referenceList );
            model.put( MARK_FILTER_LIST_VALUE, getRecordFilterItem( ).getItemValue( filter ) );
            model.put( MARK_FILTER_NAME, PARAMETER_WORKFLOW_STATE_FILTER );

            HtmlTemplate htmlTemplate = AppTemplateService.getTemplate( FILTER_TEMPLATE_NAME, request.getLocale( ), model );
            if ( htmlTemplate != null )
            {
                strTemplateResult = htmlTemplate.getHtml( );
            }

            _strFilterTemplate = strTemplateResult;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTemplate( )
        {
            return _strFilterTemplate;
        }
    }
}
