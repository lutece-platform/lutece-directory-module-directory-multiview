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
package fr.paris.lutece.plugins.directory.modules.filter.web;

import fr.paris.lutece.plugins.directory.modules.filter.business.DirectoryFilterCondition;
import fr.paris.lutece.plugins.directory.modules.filter.business.DirectoryFilterConditionHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage DirectoryFilterCondition features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectoryFilterConditions.jsp", controllerPath = "jsp/admin/plugins/directory/modules/filter/", right = "DIRECTORY_FILTER_MANAGEMENT" )
public class DirectoryFilterConditionJspBean extends AbstractManageDirectoryFilterConditionJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORYFILTERCONDITIONS = "/admin/plugins/directory/modules/filter/manage_directoryfilterconditions.html";
    private static final String TEMPLATE_CREATE_DIRECTORYFILTERCONDITION = "/admin/plugins/directory/modules/filter/create_directoryfiltercondition.html";
    private static final String TEMPLATE_MODIFY_DIRECTORYFILTERCONDITION = "/admin/plugins/directory/modules/filter/modify_directoryfiltercondition.html";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORYFILTERCONDITION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERCONDITIONS = "module.directory.filter.manage_directoryfilterconditions.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTERCONDITION = "module.directory.filter.modify_directoryfiltercondition.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTERCONDITION = "module.directory.filter.create_directoryfiltercondition.pageTitle";

    // Markers
    private static final String MARK_DIRECTORYFILTERCONDITION_LIST = "directoryfiltercondition_list";
    private static final String MARK_DIRECTORYFILTERCONDITION = "directoryfiltercondition";

    private static final String JSP_MANAGE_DIRECTORYFILTERCONDITIONS = "jsp/admin/plugins/directory/modules/filter/ManageDirectoryFilterConditions.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTERCONDITION = "module.directory.filter.message.confirmRemoveDirectoryFilterCondition";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.directory.filter.model.entity.directoryfiltercondition.attribute.";

    // Views
    private static final String VIEW_MANAGE_DIRECTORYFILTERCONDITIONS = "manageDirectoryFilterConditions";
    private static final String VIEW_CREATE_DIRECTORYFILTERCONDITION = "createDirectoryFilterCondition";
    private static final String VIEW_MODIFY_DIRECTORYFILTERCONDITION = "modifyDirectoryFilterCondition";

    // Actions
    private static final String ACTION_CREATE_DIRECTORYFILTERCONDITION = "createDirectoryFilterCondition";
    private static final String ACTION_MODIFY_DIRECTORYFILTERCONDITION = "modifyDirectoryFilterCondition";
    private static final String ACTION_REMOVE_DIRECTORYFILTERCONDITION = "removeDirectoryFilterCondition";
    private static final String ACTION_CONFIRM_REMOVE_DIRECTORYFILTERCONDITION = "confirmRemoveDirectoryFilterCondition";

    // Infos
    private static final String INFO_DIRECTORYFILTERCONDITION_CREATED = "module.directory.filter.info.directoryfiltercondition.created";
    private static final String INFO_DIRECTORYFILTERCONDITION_UPDATED = "module.directory.filter.info.directoryfiltercondition.updated";
    private static final String INFO_DIRECTORYFILTERCONDITION_REMOVED = "module.directory.filter.info.directoryfiltercondition.removed";
    
    // Session variable to store working values
    private DirectoryFilterCondition _directoryfiltercondition;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DIRECTORYFILTERCONDITIONS, defaultView = true )
    public String getManageDirectoryFilterConditions( HttpServletRequest request )
    {
        _directoryfiltercondition = null;
        List<DirectoryFilterCondition> listDirectoryFilterConditions = DirectoryFilterConditionHome.getDirectoryFilterConditionsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DIRECTORYFILTERCONDITION_LIST, listDirectoryFilterConditions, JSP_MANAGE_DIRECTORYFILTERCONDITIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERCONDITIONS, TEMPLATE_MANAGE_DIRECTORYFILTERCONDITIONS, model );
    }

    /**
     * Returns the form to create a directoryfiltercondition
     *
     * @param request The Http request
     * @return the html code of the directoryfiltercondition form
     */
    @View( VIEW_CREATE_DIRECTORYFILTERCONDITION )
    public String getCreateDirectoryFilterCondition( HttpServletRequest request )
    {
        _directoryfiltercondition = ( _directoryfiltercondition != null ) ? _directoryfiltercondition : new DirectoryFilterCondition(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DIRECTORYFILTERCONDITION, _directoryfiltercondition );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTERCONDITION, TEMPLATE_CREATE_DIRECTORYFILTERCONDITION, model );
    }

    /**
     * Process the data capture form of a new directoryfiltercondition
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DIRECTORYFILTERCONDITION )
    public String doCreateDirectoryFilterCondition( HttpServletRequest request )
    {
        populate( _directoryfiltercondition, request );

        // Check constraints
        if ( !validateBean( _directoryfiltercondition, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DIRECTORYFILTERCONDITION );
        }

        DirectoryFilterConditionHome.create( _directoryfiltercondition );
        addInfo( INFO_DIRECTORYFILTERCONDITION_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERCONDITIONS );
    }

    /**
     * Manages the removal form of a directoryfiltercondition whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DIRECTORYFILTERCONDITION )
    public String getConfirmRemoveDirectoryFilterCondition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERCONDITION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DIRECTORYFILTERCONDITION ) );
        url.addParameter( PARAMETER_ID_DIRECTORYFILTERCONDITION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTERCONDITION, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a directoryfiltercondition
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage directoryfilterconditions
     */
    @Action( ACTION_REMOVE_DIRECTORYFILTERCONDITION )
    public String doRemoveDirectoryFilterCondition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERCONDITION ) );
        DirectoryFilterConditionHome.remove( nId );
        addInfo( INFO_DIRECTORYFILTERCONDITION_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERCONDITIONS );
    }

    /**
     * Returns the form to update info about a directoryfiltercondition
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DIRECTORYFILTERCONDITION )
    public String getModifyDirectoryFilterCondition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTERCONDITION ) );

        if ( _directoryfiltercondition == null || ( _directoryfiltercondition.getId(  ) != nId ))
        {
            _directoryfiltercondition = DirectoryFilterConditionHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_DIRECTORYFILTERCONDITION, _directoryfiltercondition );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTERCONDITION, TEMPLATE_MODIFY_DIRECTORYFILTERCONDITION, model );
    }

    /**
     * Process the change form of a directoryfiltercondition
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DIRECTORYFILTERCONDITION )
    public String doModifyDirectoryFilterCondition( HttpServletRequest request )
    {
        populate( _directoryfiltercondition, request );

        // Check constraints
        if ( !validateBean( _directoryfiltercondition, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DIRECTORYFILTERCONDITION, PARAMETER_ID_DIRECTORYFILTERCONDITION, _directoryfiltercondition.getId( ) );
        }

        DirectoryFilterConditionHome.update( _directoryfiltercondition );
        addInfo( INFO_DIRECTORYFILTERCONDITION_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERCONDITIONS );
    }
}