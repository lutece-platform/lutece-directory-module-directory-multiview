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

import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage DirectoryViewFilter features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDirectoryFilters.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_FILTER_MANAGEMENT" )
public class DirectoryFilterJspBean extends AbstractJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORYFILTERS = "/admin/plugins/directory/modules/multiview/manage_directoryfilters.html";
    private static final String TEMPLATE_CREATE_DIRECTORYFILTER = "/admin/plugins/directory/modules/multiview/create_directoryfilter.html";
    private static final String TEMPLATE_MODIFY_DIRECTORYFILTER = "/admin/plugins/directory/modules/multiview/modify_directoryfilter.html";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORYFILTER = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERS = "module.directory.multiview.manage_directoryfilters.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTER = "module.directory.multiview.modify_directoryfilter.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTER = "module.directory.multiview.create_directoryfilter.pageTitle";

    // Markers
    private static final String MARK_DIRECTORYFILTER_LIST = "directoryfilter_list";
    private static final String MARK_DIRECTORYFILTER = "directoryfilter";
    private static final String MARK_DIRECTORYREFERENCELIST = "directoryreference_list";
    private static final String MARK_ENTRY_LIST = "entry_list";

    private static final String JSP_MANAGE_DIRECTORYFILTERS = "jsp/admin/plugins/directory/modules/multiview/ManageDirectoryFilters.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTER = "module.directory.multiview.message.confirmRemoveDirectoryFilter";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.directory.multiview.model.entity.directoryfilter.attribute.";

    // Views
    private static final String VIEW_MANAGE_DIRECTORYFILTERS = "manageDirectoryFilters";
    private static final String VIEW_CREATE_DIRECTORYFILTER = "createDirectoryFilter";
    private static final String VIEW_MODIFY_DIRECTORYFILTER = "modifyDirectoryFilter";

    // Actions
    private static final String ACTION_CREATE_DIRECTORYFILTER = "createDirectoryFilter";
    private static final String ACTION_MODIFY_DIRECTORYFILTER = "modifyDirectoryFilter";
    private static final String ACTION_REMOVE_DIRECTORYFILTER = "removeDirectoryFilter";
    private static final String ACTION_CONFIRM_REMOVE_DIRECTORYFILTER = "confirmRemoveDirectoryFilter";

    // Infos
    private static final String INFO_DIRECTORYFILTER_CREATED = "module.directory.multiview.info.directoryfilter.created";
    private static final String INFO_DIRECTORYFILTER_UPDATED = "module.directory.multiview.info.directoryfilter.updated";
    private static final String INFO_DIRECTORYFILTER_REMOVED = "module.directory.multiview.info.directoryfilter.removed";

    // Session variable to store working values
    private DirectoryViewFilter _directoryfilter;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DIRECTORYFILTERS, defaultView = true )
    public String getManageDirectoryFilters( HttpServletRequest request )
    {
        _directoryfilter = null;
        List<DirectoryViewFilter> listDirectoryFilters = DirectoryViewFilterHome.getDirectoryFiltersList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DIRECTORYFILTER_LIST, listDirectoryFilters, JSP_MANAGE_DIRECTORYFILTERS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DIRECTORYFILTERS, TEMPLATE_MANAGE_DIRECTORYFILTERS, model );
    }

    /**
     * Returns the form to create a directoryfilter
     *
     * @param request
     *            The Http request
     * @return the html code of the directoryfilter form
     */
    @View( VIEW_CREATE_DIRECTORYFILTER )
    public String getCreateDirectoryFilter( HttpServletRequest request )
    {
        _directoryfilter = ( _directoryfilter != null ) ? _directoryfilter : new DirectoryViewFilter( );

        ReferenceList directoryReferenceList = DirectoryHome.getDirectoryList( getPlugin( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORYFILTER, _directoryfilter );
        model.put( MARK_DIRECTORYREFERENCELIST, directoryReferenceList );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DIRECTORYFILTER, TEMPLATE_CREATE_DIRECTORYFILTER, model );
    }

    /**
     * Process the data capture form of a new directoryfilter
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DIRECTORYFILTER )
    public String doCreateDirectoryFilter( HttpServletRequest request )
    {
        populate( _directoryfilter, request );

        // Check constraints
        if ( !validateBean( _directoryfilter, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DIRECTORYFILTER );
        }

        DirectoryViewFilterHome.create( _directoryfilter );
        addInfo( INFO_DIRECTORYFILTER_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERS );
    }

    /**
     * Manages the removal form of a directoryfilter whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DIRECTORYFILTER )
    public String getConfirmRemoveDirectoryFilter( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DIRECTORYFILTER ) );
        url.addParameter( PARAMETER_ID_DIRECTORYFILTER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DIRECTORYFILTER, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a directoryfilter
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage directoryfilters
     */
    @Action( ACTION_REMOVE_DIRECTORYFILTER )
    public String doRemoveDirectoryFilter( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );
        DirectoryViewFilterHome.remove( nId );
        addInfo( INFO_DIRECTORYFILTER_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERS );
    }

    /**
     * Returns the form to update info about a directoryfilter
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DIRECTORYFILTER )
    public String getModifyDirectoryFilter( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORYFILTER ) );

        if ( _directoryfilter == null || ( _directoryfilter.getId( ) != nId ) )
        {
            _directoryfilter = DirectoryViewFilterHome.findByPrimaryKey( nId );
        }

        ReferenceList entryReferenceList = getEntryReferenceListByDirectoryId( _directoryfilter.getIdDirectory( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_DIRECTORYFILTER, _directoryfilter );
        model.put( MARK_ENTRY_LIST, entryReferenceList );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DIRECTORYFILTER, TEMPLATE_MODIFY_DIRECTORYFILTER, model );
    }

    /**
     * Process the change form of a directoryfilter
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DIRECTORYFILTER )
    public String doModifyDirectoryFilter( HttpServletRequest request )
    {
        populate( _directoryfilter, request );

        // Check constraints
        if ( !validateBean( _directoryfilter, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DIRECTORYFILTER, PARAMETER_ID_DIRECTORYFILTER, _directoryfilter.getId( ) );
        }

        DirectoryViewFilterHome.update( _directoryfilter );
        addInfo( INFO_DIRECTORYFILTER_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DIRECTORYFILTERS );
    }

    /**
     * Load the entry reference list of thze directory
     *
     * @param nIdDirectory
     *            the directory id
     * 
     * @return The reference list of entries
     */
    private ReferenceList getEntryReferenceListByDirectoryId( int nIdDirectory )
    {

        ReferenceList entryReferenceList = new ReferenceList( );

        EntryFilter filter = new EntryFilter( );
        filter.setIdDirectory( nIdDirectory );
        List<IEntry> entryList = EntryHome.getEntryList( filter, getPlugin( ) );

        for ( IEntry entry : entryList )
        {
            if ( entry.getTitle( ) != null )
            {
                ReferenceItem item = new ReferenceItem( );
                item.setCode( String.valueOf( entry.getIdEntry( ) ) );
                item.setName( entry.getTitle( ) );
                entryReferenceList.add( item );
            }
        }

        return entryReferenceList;
    }
}
