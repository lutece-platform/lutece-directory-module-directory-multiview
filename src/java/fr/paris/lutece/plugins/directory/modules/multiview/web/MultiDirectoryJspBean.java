/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItemComparator;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItemComparatorConfig;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.RecordPanelFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.IDirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.IRecordColumnDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.RecordColumnDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.IRecordFilterDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.RecordFilterDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory.RecordPanelDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListPositionComparator;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListTemplateBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListUtil;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove)
 */
@Controller( controllerJsp = "ManageMultiDirectoryRecords.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_MULTIVIEW" )
public class MultiDirectoryJspBean extends AbstractJspBean
{
    // Public properties
    private static final String CONTROLLER_JSP_NAME = "ManageMultiDirectoryRecords.jsp";

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -8417121042985481292L;

    // Templates
    private static final String TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/manage_multi_directory_record.html";

    // Properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "module.directory.multiview.manage_directory_multirecord.pageTitle.label";

    // Views
    private static final String VIEW_MULTIVIEW = "view_multiview";

    // Markers
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_SEARCH_TEXT = "search_text";
    private static final String MARK_RECORD_PANEL_LIST = "record_panel_list";
    private static final String MARK_CURRENT_SELECTED_PANEL = "current_selected_panel";
    private static final String MARK_RECORD_FILTER_LIST = "record_filter_list";
    private static final String MARK_TABLE_TEMPLATE = "table_template";

    // JSP URL
    private static final String JSP_MANAGE_MULTIVIEW = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Constants
    private static final String BASE_SORT_URL_PATTERN = JSP_MANAGE_MULTIVIEW + "?current_selected_panel=%s";

    // Session fields
    private String _strSearchedText = StringUtils.EMPTY;
    private String _strSelectedPanelTechnicalCode = StringUtils.EMPTY;
    private transient List<IRecordColumn> _listRecordColumn;
    private transient List<IRecordFilterDisplay> _listRecordFilterDisplay;
    private transient List<IRecordColumnDisplay> _listRecordColumnDisplay;
    private transient List<IRecordPanelDisplay> _listRecordPanelDisplay;
    private transient IRecordPanelDisplay _recordPanelDisplayActive;
    private transient DirectoryRecordItemComparatorConfig _directoryRecordItemComparatorConfig;
    private final transient IDirectoryMultiviewService _directoryMultiviewService = SpringContextService.getBean( IDirectoryMultiviewService.BEAN_NAME );
    private final transient IDirectoryMultiviewSearchService _directoryMultiviewSearchService = SpringContextService
            .getBean( IDirectoryMultiviewSearchService.BEAN_NAME );

    /**
     * Return management of directory record ( list of directory record ).
     * 
     * @param request
     *            The Http request
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return IPluginActionResult
     */
    @View( value = VIEW_MULTIVIEW, defaultView = true )
    public String getManageDirectoryRecord( HttpServletRequest request ) throws AccessDeniedException
    {
        // Retrieve the list of all filters, columns and panels if the pagination and the sort are not used
        boolean bIsSessionLost = isSessionLost( );
        if ( isPaginationAndSortNotUsed( request ) || bIsSessionLost )
        {
            initRecordRelatedLists( request );
            _strSearchedText = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SEARCHED_TEXT );
            manageSelectedPanel( );
        }

        // Build the Column for the Panel and save their values for the active panel
        buildRecordPanelDisplayWithData( );

        // Sort the list of DirectoryRecordItem of the RecordPanel with the request information
        sortDirectoryRecordItemList( request, _recordPanelDisplayActive.getDirectoryRecordItemList( ) );

        // Build the template of each record filter display
        if ( isPaginationAndSortNotUsed( request ) || bIsSessionLost )
        {
            _listRecordFilterDisplay.stream( ).forEach( recordFilterDisplay -> recordFilterDisplay.buildTemplate( request ) );
            Collections.sort( _listRecordFilterDisplay, new RecordListPositionComparator( ) );
        }

        // Retrieve the list of all id of record of the active RecordListPanelDisplay
        List<Integer> listIdRecord = RecordListUtil.getListIdRecordOfRecordPanel( _recordPanelDisplayActive );

        // Build the model
        Map<String, Object> model = getPaginatedListModel( request, Paginator.PARAMETER_PAGE_INDEX, listIdRecord, JSP_MANAGE_MULTIVIEW,
                _strSelectedPanelTechnicalCode );
        model.put( MARK_PAGINATOR, getPaginator( ) );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_SEARCH_TEXT, _strSearchedText );
        model.put( MARK_RECORD_FILTER_LIST, _listRecordFilterDisplay );

        // Add the template of column to the model
        String strSortUrl = String.format( BASE_SORT_URL_PATTERN, _strSelectedPanelTechnicalCode );
        String strRedirectionDetailsBaseUrl = buildRedirectionDetailsBaseUrl( );
        List<DirectoryRecordItem> listDirectoryRecordItemToDisplay = buildDirectoryRecordItemListToDisplay( );
        String strTableTemplate = RecordListTemplateBuilder.buildTableTemplate( _listRecordColumnDisplay, listDirectoryRecordItemToDisplay, getLocale( ),
                strRedirectionDetailsBaseUrl, strSortUrl );
        model.put( MARK_TABLE_TEMPLATE, strTableTemplate );

        // Add the list of all record panel
        model.put( MARK_RECORD_PANEL_LIST, _listRecordPanelDisplay );
        model.put( MARK_CURRENT_SELECTED_PANEL, _strSelectedPanelTechnicalCode );

        return getPage( PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE, TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD, model );
    }

    /**
     * Return the boolean which tell if the pagination and the sort are not used
     * 
     * @param request
     *            The request to retrieve the information from
     * @return the boolean which tell if the pagination and the sort are not used
     */
    private boolean isPaginationAndSortNotUsed( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_PAGE_INDEX ) == null
                && request.getParameter( DirectoryMultiviewConstants.PARAMETER_SORT_COLUMN_POSITION ) == null;
    }

    /**
     * Return the boolean which tell if the session is lost
     * 
     * @return the boolean which tell if the session is lost
     */
    private boolean isSessionLost( )
    {
        return _listRecordColumn == null || _listRecordFilterDisplay == null || _listRecordColumnDisplay == null || _listRecordPanelDisplay == null
                || _recordPanelDisplayActive == null;
    }

    /**
     * Build the list of all columns, filters and record panels and all of their display equivalents
     * 
     * @param request
     *            The request used to build the list of the records elements
     */
    private void initRecordRelatedLists( HttpServletRequest request )
    {
        List<IRecordFilter> listRecordFilter = new RecordFilterFactory( ).buildRecordFilterList( );
        List<IRecordPanel> listRecordPanel = new RecordPanelFactory( ).buildRecordPanelList( );

        RecordColumnFactory recordColumnFactory = SpringContextService.getBean( RecordColumnFactory.BEAN_NAME );
        _listRecordColumn = recordColumnFactory.buildRecordColumnList( );

        _listRecordFilterDisplay = new RecordFilterDisplayFactory( ).createRecordFilterDisplayList( request, listRecordFilter );
        _listRecordColumnDisplay = new RecordColumnDisplayFactory( ).createRecordColumnDisplayList( _listRecordColumn );
        _listRecordPanelDisplay = new RecordPanelDisplayFactory( ).createRecordPanelDisplayList( request, listRecordPanel );
    }

    /**
     * Retrieve the technical code of the selected panel and change the value of the previous selected one if it wasn't the same and reset the pagination in
     * this case
     */
    private void manageSelectedPanel( )
    {
        IRecordPanelDisplay recordPanelDisplay = _directoryMultiviewService.findActiveRecordPanel( _listRecordPanelDisplay );
        if ( recordPanelDisplay != null )
        {
            String strSelectedPanelTechnicalCode = recordPanelDisplay.getTechnicalCode( );
            if ( StringUtils.isNotBlank( strSelectedPanelTechnicalCode ) && !_strSelectedPanelTechnicalCode.equals( strSelectedPanelTechnicalCode ) )
            {
                _strSelectedPanelTechnicalCode = strSelectedPanelTechnicalCode;
                resetCurrentPaginatorPageIndex( );
            }
        }
    }

    /**
     * Build all the record panels by building their template and retrieve the data of their columns for the given list of filter and the specified text to
     * search
     */
    private void buildRecordPanelDisplayWithData( )
    {
        // Retrieve the list of all RecordFilter
        List<IRecordFilter> listRecordFilter = _listRecordFilterDisplay.stream( ).map( IRecordFilterDisplay::getRecordFilter ).collect( Collectors.toList( ) );

        for ( IRecordPanelDisplay recordPanelDisplay : _listRecordPanelDisplay )
        {
            // Retrieve the RecordPanel from the RecordPanelDisplay
            IRecordPanel recordPanel = recordPanelDisplay.getRecordPanel( );

            // Populate the RecordColumns from the information of the list of RecordFilterItem of the given RecordPanel
            _directoryMultiviewService.populateRecordColumns( recordPanel, _listRecordColumn, listRecordFilter );

            if ( StringUtils.isNotBlank( _strSearchedText ) )
            {
                _directoryMultiviewSearchService.filterBySearchedText( recordPanel, _strSearchedText );
            }

            // Associate for each RecordColumnDisplay its RecordColumnValues if the panel is active
            if ( recordPanelDisplay.isActive( ) )
            {
                _recordPanelDisplayActive = recordPanelDisplay;
            }

            // Build the template of the record list panel
            recordPanelDisplay.buildTemplate( getLocale( ) );
        }
    }

    /**
     * Build the list of DirectoryRecordItem to display for the given IRecordFilterPanelDisplay based on the number of items of the current paginator
     * 
     * @return list of DirectoryRecordItem to display for the given IRecordFilterPanelDisplay
     */
    private List<DirectoryRecordItem> buildDirectoryRecordItemListToDisplay( )
    {
        List<DirectoryRecordItem> listDirectoryRecordItemToDisplay = new ArrayList<>( );

        List<Integer> listIdRecordPaginated = getPaginator( ).getPageItems( );
        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanelDisplayActive.getDirectoryRecordItemList( );
        if ( listIdRecordPaginated != null && !listIdRecordPaginated.isEmpty( ) && listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
        {
            for ( DirectoryRecordItem directoryRecordItem : listDirectoryRecordItem )
            {
                Integer nIdRecord = directoryRecordItem.getIdRecord( );
                if ( listIdRecordPaginated.contains( nIdRecord ) )
                {
                    listDirectoryRecordItemToDisplay.add( directoryRecordItem );
                }
            }
        }

        return listDirectoryRecordItemToDisplay;
    }

    /**
     * Sort the given list of DirectoryRecordItem from the values contains in the request
     * 
     * @param request
     *            The request to retrieve the values used for the sort
     * @param listDirectoryRecordItem
     *            The list of DirectoryRecordItem to sort
     */
    private void sortDirectoryRecordItemList( HttpServletRequest request, List<DirectoryRecordItem> listDirectoryRecordItem )
    {
        if ( request.getParameter( DirectoryMultiviewConstants.PARAMETER_SORT_COLUMN_POSITION ) != null )
        {
            buildDirectoryRecordItemComparatorConfiguration( request );
        }

        if ( listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
        {
            DirectoryRecordItemComparator directoryRecordItemComparator = new DirectoryRecordItemComparator( _directoryRecordItemComparatorConfig );
            Collections.sort( listDirectoryRecordItem, directoryRecordItemComparator );
        }
    }

    /**
     * Build the configuration to use for sort the DirectoryRecordItem with the information from the request
     * 
     * @param request
     *            The request to retrieve the values for the sort from
     */
    private void buildDirectoryRecordItemComparatorConfiguration( HttpServletRequest request )
    {
        String strColumnToSortPosition = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SORT_COLUMN_POSITION );
        int nColumnToSortPosition = NumberUtils.toInt( strColumnToSortPosition, NumberUtils.INTEGER_MINUS_ONE );

        String strSortKey = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SORT_ATTRIBUTE_NAME );

        String strAscSort = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SORT_ASC_VALUE );
        boolean bAscSort = Boolean.parseBoolean( strAscSort );

        _directoryRecordItemComparatorConfig = new DirectoryRecordItemComparatorConfig( nColumnToSortPosition, strSortKey, bAscSort );
    }

    /**
     * Build the base url to use for redirect to the page of the details of a record
     * 
     * @return the base url to use for redirect to the details page of a record
     */
    private String buildRedirectionDetailsBaseUrl( )
    {
        UrlItem urlRedirectionDetails = new UrlItem( MultiviewRecordDetailsJspBean.getMultiviewRecordDetailsBaseUrl( ) );

        if ( !CollectionUtils.isEmpty( _listRecordFilterDisplay ) )
        {
            for ( IRecordFilterDisplay recordFilterDisplay : _listRecordFilterDisplay )
            {
                // Add all the filters values
                String strFilterValue = recordFilterDisplay.getValue( );
                if ( !StringUtils.isEmpty( strFilterValue ) )
                {
                    String strFilterFullName = DirectoryMultiviewConstants.PARAMETER_URL_FILTER_PREFIX + recordFilterDisplay.getParameterName( );
                    urlRedirectionDetails.addParameter( strFilterFullName, strFilterValue );
                }
            }
        }

        // Add the search text
        if ( !StringUtils.isEmpty( _strSearchedText ) )
        {
            urlRedirectionDetails.addParameter( DirectoryMultiviewConstants.PARAMETER_SEARCHED_TEXT, _strSearchedText );
        }

        // Add the selected panel technical code
        urlRedirectionDetails.addParameter( DirectoryMultiviewConstants.PARAMETER_SELECTED_PANEL, _strSelectedPanelTechnicalCode );

        // Add sort filter data to the url
        addFilterSortConfigToUrl( urlRedirectionDetails );

        return urlRedirectionDetails.getUrl( );
    }

    /**
     * Add the information for rebuild the used sort
     * 
     * @param urlRedirectionDetails
     *            The UrlItem which represent the url to use for redirect to the records details page
     */
    private void addFilterSortConfigToUrl( UrlItem urlRedirectionDetails )
    {
        if ( _directoryRecordItemComparatorConfig != null )
        {
            String strSortPosition = Integer.toString( _directoryRecordItemComparatorConfig.getColumnToSortPosition( ) );
            String strAttributeName = _directoryRecordItemComparatorConfig.getSortAttributeName( );
            String strAscSort = String.valueOf( _directoryRecordItemComparatorConfig.isAscSort( ) );

            urlRedirectionDetails.addParameter( DirectoryMultiviewConstants.PARAMETER_SORT_COLUMN_POSITION, strSortPosition );
            urlRedirectionDetails.addParameter( DirectoryMultiviewConstants.PARAMETER_SORT_ATTRIBUTE_NAME, strAttributeName );
            urlRedirectionDetails.addParameter( DirectoryMultiviewConstants.PARAMETER_SORT_ASC_VALUE, strAscSort );
        }
    }

    /**
     * Return the base url of the controller for the view which display the list of records
     * 
     * @return the base url of the controller for the view which display the list of records
     */
    protected static String getMultiviewBaseViewUrl( )
    {
        return CONTROLLER_JSP_NAME + "?view=" + VIEW_MULTIVIEW;
    }
}
