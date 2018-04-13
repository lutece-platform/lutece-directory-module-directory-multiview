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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

/**
 * ManageDirectoryFilterCondition JSP Bean abstract class for JSP Bean
 */
public abstract class AbstractJspBean extends MVCAdminJspBean
{
    // Rights
    public static final String RIGHT_MANAGEDIRECTORYFILTERCONDITION = "DIRECTORY_FILTER_MANAGEMENT";

    // Generated serial UID
    private static final long serialVersionUID = 5595439308265255189L;

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "directory-multiview.listItems.itemsPerPage";

    // Parameters
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Markers
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // Variables
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private LocalizedPaginator<Integer> _paginator;

    /**
     * Return a model that contains the list and paginator infos
     * 
     * @param request
     *            The HTTP request
     * @param strBookmark
     *            The bookmark
     * @param list
     *            The list of item
     * @param strManageJsp
     *            The JSP
     * @param strActivePanelName
     *            The name of the active panel
     * @return The model
     */
    protected Map<String, Object> getPaginatedListModel( HttpServletRequest request, String strBookmark, List<Integer> list, String strManageJsp,
            String strActivePanelName )
    {
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

        UrlItem url = new UrlItem( strManageJsp );
        url.addParameter( DirectoryMultiviewConstants.PARAMETER_CURRENT_SELECTED_PANEL, strActivePanelName );
        String strSearchText = StringUtils.defaultString( request.getParameter( DirectoryMultiviewConstants.PARAMETER_SEARCHED_TEXT ), StringUtils.EMPTY );
        url.addParameter( DirectoryMultiviewConstants.PARAMETER_SEARCHED_TEXT, strSearchText );
        String strUrl = url.getUrl( );

        // PAGINATOR
        _paginator = new LocalizedPaginator<Integer>( list, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        Map<String, Object> model = getModel( );

        model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        model.put( MARK_PAGINATOR, _paginator );
        model.put( strBookmark, _paginator.getPageItems( ) );

        return model;
    }

    /**
     * reset current paginator page index to 1
     */
    public void resetCurrentPaginatorPageIndex( )
    {
        _strCurrentPageIndex = "1";
    }

    /**
     * Return the paginator
     * 
     * @return the paginator
     */
    protected LocalizedPaginator<Integer> getPaginator( )
    {
        return _paginator;
    }
}
