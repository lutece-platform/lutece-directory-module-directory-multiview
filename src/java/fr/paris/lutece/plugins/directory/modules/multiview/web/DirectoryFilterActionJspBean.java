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
package fr.paris.lutece.plugins.directory.modules.multiview.web;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterAction;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterActionHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage DirectoryViewFilterAction features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectoryFilterActions.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_FILTER_MANAGEMENT" )
public class DirectoryFilterActionJspBean extends AbstractManageDirectoryFilterActionJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORYFILTERACTIONS = "/admin/plugins/directory/modules/multiview/manage_directoryfilteractions.html";
    private static final String TEMPLATE_CREATE_DIRECTORYFILTERACTION = "/admin/plugins/directory/modules/multiview/create_directoryfilteraction.html";
    private static final String TEMPLATE_MODIFY_DIRECTORYFILTERACTION = "/admin/plugins/directory/modules/multiview/modify_directoryfilteraction.html";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORYFILTERACTION = "id";
    private static final String PARAMETER_ID_DIRECTORYFILTER = "directory_filter_id";
    private static final String PARAMETER_ID_DIRECTORYWORKFLOW = "directory_workflow_id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERACTIONS = "module.directory.multiview.manage_directoryfilteractions.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTERACTION = "module.directory.multiview.modify_directoryfilteraction.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTERACTION = "module.directory.multiview.create_directoryfilteraction.pageTitle";

    // Markers
    private static final String MARK_DIRECTORYFILTERACTION_LIST = "directoryfilteraction_list";
    private static final String MARK_DIRECTORYFILTERACTION = "directoryfilteraction";
    private static final String MARK_DIRECTORYFILTERID = "directoryfilterid";
    private static final String MARK_DIRECTORYWORKFLOWID = "directoryworkflowid";
    private static final String MARK_ACTION_LIST = "action_list";
    private static final String JSP_MANAGE_DIRECTORYFILTERACTIONS = "jsp/admin/plugins/directory/modules/multiview/ManageDirectoryFilterActions.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTERACTION = "module.directory.multiview.message.confirmRemoveDirectoryFilterAction";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.directory.multiview.model.entity.directoryfilteraction.attribute.";

    // Views
    private static final String VIEW_MANAGE_DIRECTORYFILTERACTIONS = "manageDirectoryFilterActions";
    private static final String VIEW_CREATE_DIRECTORYFILTERACTION = "createDirectoryFilterAction";
    private static final String VIEW_MODIFY_DIRECTORYFILTERACTION = "modifyDirectoryFilterAction";

    // Actions
    private static final String ACTION_CREATE_DIRECTORYFILTERACTION = "createDirectoryFilterAction";
    private static final String ACTION_MODIFY_DIRECTORYFILTERACTION = "modifyDirectoryFilterAction";
    private static final String ACTION_REMOVE_DIRECTORYFILTERACTION = "removeDirectoryFilterAction";
    private static final String ACTION_CONFIRM_REMOVE_DIRECTORYFILTERACTION = "confirmRemoveDirectoryFilterAction";

    // Infos
    private static final String INFO_DIRECTORYFILTERACTION_CREATED = "module.directory.multiview.info.directoryfilteraction.created";
    private static final String INFO_DIRECTORYFILTERACTION_UPDATED = "module.directory.multiview.info.directoryfilteraction.updated";
    private static final String INFO_DIRECTORYFILTERACTION_REMOVED = "module.directory.multiview.info.directoryfilteraction.removed";

    // Session variable to store working values
    private DirectoryViewFilterAction _directoryfilteraction;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DIRECTORYFILTERACTIONS, defaultView = true )
    public String getManageDirectoryFilterActions( HttpServletRequest request )
    {
        _directoryfilteraction = null;
        int nIdDirectoryWorkflow = -1;

        int nIdDirectoryFilter = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        String strIdDirectoryWorkflow = request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW );

        if ( strIdDirectoryWorkflow == null )
        {
            DirectoryViewFilter dvf = DirectoryViewFilterHome.findByPrimaryKey( nIdDirectoryFilter );
            if ( dvf != null )
                nIdDirectoryWorkflow = dvf.getIdDirectory( );
            Directory dir = DirectoryHome.findByPrimaryKey( dvf.getIdDirectory( ), getPlugin( ) );
            if ( dir != null )
                nIdDirectoryWorkflow = dir.getIdWorkflow( );

        }
        else
        {
            nIdDirectoryWorkflow = Integer.parseInt( strIdDirectoryWorkflow );
        }

        List<DirectoryViewFilterAction> listDirectoryFilterActions = DirectoryViewFilterActionHome
                .getDirectoryFilterActionsListByDirectoryFilter( nIdDirectoryFilter );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DIRECTORYFILTERACTION_LIST, listDirectoryFilterActions,
                JSP_MANAGE_DIRECTORYFILTERACTIONS );
        model.put( MARK_DIRECTORYFILTERID, nIdDirectoryFilter );
        model.put( MARK_DIRECTORYWORKFLOWID, nIdDirectoryWorkflow );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERACTIONS, TEMPLATE_MANAGE_DIRECTORYFILTERACTIONS, model );
    }

    /**
     * Returns the form to create a directoryfilteraction
     *
     * @param request
     *            The Http request
     * @return the html code of the directoryfilteraction form
     */
    @View( VIEW_CREATE_DIRECTORYFILTERACTION )
    public String getCreateDirectoryFilterAction( HttpServletRequest request )
    {
        _directoryfilteraction = ( _directoryfilteraction != null ) ? _directoryfilteraction : new DirectoryViewFilterAction( );

        int nIdDirectoryFilter = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        int nIdDirectoryWorkflow = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW ) );

        _directoryfilteraction.setIdDirectoryFilter( nIdDirectoryFilter );

        Collection<State> stateList = WorkflowService.getInstance( ).getAllStateByWorkflow( nIdDirectoryWorkflow, getUser( ) );

        ReferenceList actionList = new ReferenceList( );
        for ( State state : stateList )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setCode( state.getResourceId( ) );
            item.setName( state.getName( ) );
            actionList.add( item );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORYFILTERACTION, _directoryfilteraction );
        model.put( MARK_ACTION_LIST, actionList );

        model.put( MARK_DIRECTORYFILTERID, nIdDirectoryFilter );
        model.put( MARK_DIRECTORYWORKFLOWID, nIdDirectoryWorkflow );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTERACTION, TEMPLATE_CREATE_DIRECTORYFILTERACTION, model );
    }

    /**
     * Process the data capture form of a new directoryfilteraction
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DIRECTORYFILTERACTION )
    public String doCreateDirectoryFilterAction( HttpServletRequest request )
    {

        populate( _directoryfilteraction, request );

        Map<String, String> additionalParameters = new HashMap<>( );
        String strIdDirectoryFilter = request.getParameter( PARAMETER_ID_DIRECTORYFILTER );
        String strIdDirectoryWorkflow = request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW );
        additionalParameters.put( PARAMETER_ID_DIRECTORYFILTER, strIdDirectoryFilter );
        additionalParameters.put( PARAMETER_ID_DIRECTORYWORKFLOW, strIdDirectoryWorkflow );

        // Check constraints
        if ( !validateBean( _directoryfilteraction, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_CREATE_DIRECTORYFILTERACTION, additionalParameters );
        }

        DirectoryViewFilterActionHome.create( _directoryfilteraction );
        addInfo( INFO_DIRECTORYFILTERACTION_CREATED, getLocale( ) );

        return redirect( request, VIEW_MANAGE_DIRECTORYFILTERACTIONS, additionalParameters );
    }

    /**
     * Manages the removal form of a directoryfilteraction whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DIRECTORYFILTERACTION )
    public String getConfirmRemoveDirectoryFilterAction( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERACTION ) );
        int nIdDirectoryFilter = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        int nIdDirectoryWorkflow = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW ) );

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DIRECTORYFILTERACTION ) );
        url.addParameter( PARAMETER_ID_DIRECTORYFILTERACTION, nId );
        url.addParameter( PARAMETER_ID_DIRECTORYFILTER, nIdDirectoryFilter );
        url.addParameter( PARAMETER_ID_DIRECTORYWORKFLOW, nIdDirectoryWorkflow );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTERACTION, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a directoryfilteraction
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage directoryfilteractions
     */
    @Action( ACTION_REMOVE_DIRECTORYFILTERACTION )
    public String doRemoveDirectoryFilterAction( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERACTION ) );
        DirectoryViewFilterActionHome.remove( nId );
        addInfo( INFO_DIRECTORYFILTERACTION_REMOVED, getLocale( ) );

        Map<String, String> additionalParameters = new HashMap<>( );

        String strIdDirectoryFilter = request.getParameter( PARAMETER_ID_DIRECTORYFILTER );
        String strIdDirectoryWorkflow = request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW );
        additionalParameters.put( PARAMETER_ID_DIRECTORYFILTER, strIdDirectoryFilter );
        additionalParameters.put( PARAMETER_ID_DIRECTORYWORKFLOW, strIdDirectoryWorkflow );

        return redirect( request, VIEW_MANAGE_DIRECTORYFILTERACTIONS, additionalParameters );
    }

    /**
     * Returns the form to update info about a directoryfilteraction
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DIRECTORYFILTERACTION )
    public String getModifyDirectoryFilterAction( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERACTION ) );
        int nIdDirectoryFilter = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        int nIdDirectoryWorkflow = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW ) );

        if ( _directoryfilteraction == null || ( _directoryfilteraction.getId( ) != nId ) )
        {
            _directoryfilteraction = DirectoryViewFilterActionHome.findByPrimaryKey( nId );
        }

        Collection<State> stateList = WorkflowService.getInstance( ).getAllStateByWorkflow( nIdDirectoryWorkflow, getUser( ) );

        ReferenceList actionList = new ReferenceList( );
        for ( State state : stateList )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setCode( state.getResourceId( ) );
            item.setName( state.getName( ) );
            actionList.add( item );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORYFILTERACTION, _directoryfilteraction );
        model.put( MARK_ACTION_LIST, actionList );
        model.put( MARK_DIRECTORYFILTERID, nIdDirectoryFilter );
        model.put( MARK_DIRECTORYWORKFLOWID, nIdDirectoryWorkflow );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTERACTION, TEMPLATE_MODIFY_DIRECTORYFILTERACTION, model );
    }

    /**
     * Process the change form of a directoryfilteraction
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DIRECTORYFILTERACTION )
    public String doModifyDirectoryFilterAction( HttpServletRequest request )
    {
        populate( _directoryfilteraction, request );

        Map<String, String> additionalParameters = new HashMap<>( );
        String strIdDirectoryFilter = request.getParameter( PARAMETER_ID_DIRECTORYFILTER );
        String strIdDirectoryWorkflow = request.getParameter( PARAMETER_ID_DIRECTORYWORKFLOW );
        additionalParameters.put( PARAMETER_ID_DIRECTORYFILTER, strIdDirectoryFilter );
        additionalParameters.put( PARAMETER_ID_DIRECTORYWORKFLOW, strIdDirectoryWorkflow );

        // Check constraints
        if ( !validateBean( _directoryfilteraction, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            additionalParameters.put( PARAMETER_ID_DIRECTORYFILTERACTION, String.valueOf( _directoryfilteraction.getId( ) ) );
            return redirect( request, VIEW_MODIFY_DIRECTORYFILTERACTION, additionalParameters );
        }

        DirectoryViewFilterActionHome.update( _directoryfilteraction );
        addInfo( INFO_DIRECTORYFILTERACTION_UPDATED, getLocale( ) );

        return redirect( request, VIEW_MANAGE_DIRECTORYFILTERACTIONS, additionalParameters );
    }
}
