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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItemComparator;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItemComparatorConfig;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.RecordPanelFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewAuthorizationService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.UserIdentityService;
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
import fr.paris.lutece.plugins.directory.modules.multiview.web.user.UserFactory;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.Paginator;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove)
 */
@Controller( controllerJsp = "ManageMultiDirectoryRecords.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_MULTIVIEW" )
public class MultiDirectoryJspBean extends AbstractJspBean
{
    // Public properties
    public static final String PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY = "DIRECTORY_MULTIVIEW";

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -8417121042985481292L;

    // Templates
    private static final String TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/manage_multi_directory_record.html";
    private static final String TEMPLATE_VIEW_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/view_directory_record.html";
    private static final String TEMPLATE_RECORD_HISTORY = "admin/plugins/directory/modules/multiview/record_history.html";
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/directory/modules/multiview/task_form_workflow.html";

    // Messages (I18n keys)
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";
    private static final String MESSAGE_MULTIVIEW_TITLE = "module.directory.multiview.pageTitle";

    // Properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "module.directory.multiview.manage_directory_multirecord.pageTitle.label";
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";

    // Markers
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_DIRECTORY = "directory";
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD = "map_id_entry_list_record_field";
    private static final String MARK_ID_ENTRY_TYPE_IMAGE = "id_entry_type_image";
    private static final String MARK_ID_ENTRY_TYPE_MYLUTECE_USER = "id_entry_type_mylutece_user";
    private static final String MARK_ID_ENTRY_TYPE_GEOLOCATION = "id_entry_type_geolocation";
    private static final String MARK_SHOW_DATE_CREATION_RECORD = "show_date_creation_record";
    private static final String MARK_SHOW_DATE_MODIFICATION_RECORD = "show_date_modification_record";
    private static final String MARK_RECORD = "record";
    private static final String MARK_RESOURCE_ACTIONS = "resource_actions";
    private static final String MARK_RESOURCE_HISTORY = "resource_history";
    private static final String MARK_HISTORY_WORKFLOW_ENABLED = "history_workflow";
    private static final String MARK_PERMISSION_VISUALISATION_MYLUTECE_USER = "permission_visualisation_mylutece_user";
    private static final String MARK_USER_FACTORY = "user_factory";
    private static final String MARK_USER_ATTRIBUTES = "user_attributes";
    private static final String MARK_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String MARK_ID_ACTION = "id_action";
    private static final String MARK_ID_DIRECTORY = "id_directory";
    private static final String MARK_TASK_FORM = "tasks_form";
    private static final String MARK_SEARCH_TEXT = "search_text";
    private static final String MARK_RECORD_PANEL_LIST = "record_panel_list";
    private static final String MARK_CURRENT_SELECTED_PANEL = "current_selected_panel";
    private static final String MARK_RECORD_FILTER_LIST = "record_filter_list";
    private static final String MARK_TABLE_TEMPLATE = "table_template";

    // JSP URL
    private static final String JSP_MANAGE_MULTIVIEW = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_ACTION = "id_action";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_SORT_COLUMN_POSITION = "column_position";
    private static final String PARAMETER_SORT_ATTRIBUTE_NAME = "sorted_attribute_name";
    private static final String PARAMETER_SORT_ASC_VALUE = "asc_sort";

    // Views
    private static final String VIEW_MULTIVIEW = "view_multiview";
    private static final String VIEW_RECORD_VISUALISATION = "view_record_visualisation";
    private static final String VIEW_TASKS_FORM = "view_tasksForm";

    // Actions
    private static final String ACTION_PROCESS_ACTION = "doProcessAction";
    private static final String ACTION_SAVE_TASK_FORM = "doSaveTaskForm";
    private static final String ACTION_CANCEL_TASK_FORM = "doCancelTaskForm";

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
    private final transient IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );
    private final transient IDirectoryMultiviewService _directoryMultiviewService = SpringContextService.getBean( IDirectoryMultiviewService.BEAN_NAME );
    private final transient IDirectoryMultiviewSearchService _directoryMultiviewSearchService = SpringContextService
            .getBean( IDirectoryMultiviewSearchService.BEAN_NAME );
    private final transient IDirectoryMultiviewAuthorizationService _directoryMultiviewAuthorizationService = SpringContextService
            .getBean( IDirectoryMultiviewAuthorizationService.BEAN_NAME );

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
        List<DirectoryRecordItem> listDirectoryRecordItemToDisplay = buildDirectoryRecordItemListToDisplay( );
        String strTableTemplate = RecordListTemplateBuilder.buildTableTemplate( _listRecordColumnDisplay, listDirectoryRecordItemToDisplay, getLocale( ),
                strSortUrl );
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
        return request.getParameter( PARAMETER_PAGE_INDEX ) == null && request.getParameter( PARAMETER_SORT_COLUMN_POSITION ) == null;
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
                _directoryMultiviewSearchService.filterBySearchedText( recordPanel, getUser( ), _strSearchedText, getLocale( ) );
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
        if ( request.getParameter( PARAMETER_SORT_COLUMN_POSITION ) != null )
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
        String strColumnToSortPosition = request.getParameter( PARAMETER_SORT_COLUMN_POSITION );
        int nColumnToSortPosition = NumberUtils.toInt( strColumnToSortPosition, NumberUtils.INTEGER_MINUS_ONE );

        String strSortKey = request.getParameter( PARAMETER_SORT_ATTRIBUTE_NAME );

        String strAscSort = request.getParameter( PARAMETER_SORT_ASC_VALUE );
        boolean bAscSort = Boolean.parseBoolean( strAscSort );

        _directoryRecordItemComparatorConfig = new DirectoryRecordItemComparatorConfig( nColumnToSortPosition, strSortKey, bAscSort );
    }

    /**
     * return the record visualisation
     * 
     * @param request
     *            the Http request
     * @return the record visualisation
     * @throws AccessDeniedException
     *             AccessDeniedException
     */
    @View( value = VIEW_RECORD_VISUALISATION )
    public String getRecordVisualisation( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD );
        int nIdRecord = NumberUtils.toInt( strIdRecord, NumberUtils.INTEGER_MINUS_ONE );
        Record record = _recordService.findByPrimaryKey( nIdRecord, getPlugin( ) );

        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        int nIdDirectory = NumberUtils.toInt( strIdDirectory, NumberUtils.INTEGER_MINUS_ONE );
        Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );

        boolean bRBACAuthorization = RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdRecord ),
                DirectoryResourceIdService.PERMISSION_VISUALISATION_RECORD, getUser( ) );
        boolean bAuthorizedRecord = _directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( nIdRecord );

        if ( record == null || directory == null || !bRBACAuthorization || !bAuthorizedRecord )
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( record.getDirectory( ).getIdDirectory( ), getPlugin( ), getUser( ) );

        // List directory actions
        List<DirectoryAction> listActionsForDirectoryEnable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_ENABLE, getPlugin( ),
                getLocale( ) );
        List<DirectoryAction> listActionsForDirectoryDisable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_DISABLE, getPlugin( ),
                getLocale( ) );

        listActionsForDirectoryEnable = (List<DirectoryAction>) RBACService
                .getAuthorizedActionsCollection( listActionsForDirectoryEnable, directory, getUser( ) );
        listActionsForDirectoryDisable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryDisable, directory,
                getUser( ) );

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( ) && ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL );

        // Get asynchronous file names
        boolean bGetFileName = true;

        // Get the guid
        String strGuid = UserIdentityService.getUserGuid( listEntry, nIdRecord, getPlugin( ) );

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_RECORD, record );
        model.put( MARK_ENTRY_LIST, listEntry );
        model.put( MARK_DIRECTORY, directory );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_ID_ENTRY_TYPE_GEOLOCATION, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) );
        model.put( MARK_ID_ENTRY_TYPE_IMAGE, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_IMAGE, 10 ) );
        model.put( MARK_ID_ENTRY_TYPE_MYLUTECE_USER, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_MYLUTECE_USER, 19 ) );
        model.put( MARK_PERMISSION_VISUALISATION_MYLUTECE_USER, RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdDirectory ),
                DirectoryResourceIdService.PERMISSION_VISUALISATION_MYLUTECE_USER, getUser( ) ) );
        model.put( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD, DirectoryUtils.getMapIdEntryListRecordField( listEntry, nIdRecord, getPlugin( ) ) );

        model.put( MARK_SHOW_DATE_CREATION_RECORD, directory.isDateShownInResultRecord( ) );
        model.put( MARK_SHOW_DATE_MODIFICATION_RECORD, directory.isDateModificationShownInResultRecord( ) );
        model.put(
                MARK_RESOURCE_ACTIONS,
                DirectoryService.getInstance( ).getResourceAction( record, directory, listEntry, getUser( ), listActionsForDirectoryEnable,
                        listActionsForDirectoryDisable, bGetFileName, getPlugin( ) ) );
        model.put( MARK_HISTORY_WORKFLOW_ENABLED, bHistoryEnabled );

        model.put( MARK_USER_FACTORY, UserFactory.getInstance( ) );
        model.put(
                MARK_RESOURCE_HISTORY,
                WorkflowService.getInstance( ).getDisplayDocumentHistory( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, directory.getIdWorkflow( ), request,
                        getLocale( ), model, TEMPLATE_RECORD_HISTORY ) );

        // Add the user attributes
        if ( strGuid != null )
        {
            model.put( MARK_USER_ATTRIBUTES, UserIdentityService.getUserAttributes( strGuid ) );
        }

        return getPage( MESSAGE_MULTIVIEW_TITLE, TEMPLATE_VIEW_DIRECTORY_RECORD, model );
    }

    /**
     * Process workflow action on record
     * 
     * @param request
     *            the HttpServletRequest
     * @return the task form if exists, or the manage record view otherwise.
     */
    @Action( value = ACTION_PROCESS_ACTION )
    public String doProcessWorkflowAction( HttpServletRequest request )
    {
        // Get parameters from request
        int nIdRecord = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) );
        int nIdAction = Integer.parseInt( request.getParameter( PARAMETER_ID_ACTION ) );
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );

        if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
        {
            Map<String, String> model = new LinkedHashMap<>( );
            model.put( PARAMETER_ID_DIRECTORY_RECORD, String.valueOf( nIdRecord ) );
            model.put( PARAMETER_ID_ACTION, String.valueOf( nIdAction ) );
            model.put( PARAMETER_ID_DIRECTORY, String.valueOf( nIdDirectory ) );

            return redirect( request, VIEW_TASKS_FORM, model );
        }

        boolean bHasSucceed = false;

        try
        {

            WorkflowService.getInstance( ).doProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request, request.getLocale( ),
                    false );
            bHasSucceed = true;
        }
        catch( Exception e )
        {
            AppLogService.error( "Error processing action for id record '" + nIdRecord + "' - cause : " + e.getMessage( ), e );
        }

        if ( bHasSucceed )
        {
            // Update record modification date (for directory plugin)
            Record record = _recordService.findByPrimaryKey( nIdRecord, getPlugin( ) );
            _recordService.update( record, getPlugin( ) );
        }

        // Redirect to the correct view
        return manageRedirection( request );
    }

    /**
     * Returns the task form associate to the workflow action
     *
     * @param request
     *            The Http request
     * @return The HTML form the task form associate to the workflow action
     */
    @View( value = VIEW_TASKS_FORM )
    public String getTaskForm( HttpServletRequest request )
    {
        Integer nIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) != null ? Integer.parseInt( request
                .getParameter( PARAMETER_ID_DIRECTORY_RECORD ) ) : null;
        Integer nIdAction = request.getParameter( PARAMETER_ID_ACTION ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ID_ACTION ) ) : null;

        if ( nIdAction == null || nIdRecord == null )
        {
            return redirectView( request, VIEW_RECORD_VISUALISATION );

        }

        String strHtmlTasksForm = WorkflowService.getInstance( )
                .getDisplayTasksForm( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, request, getLocale( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_ID_DIRECTORY_RECORD, nIdRecord );
        model.put( MARK_ID_ACTION, nIdAction );

        int nIdDirectory = NumberUtils.toInt( request.getParameter( MARK_ID_DIRECTORY ), NumberUtils.INTEGER_MINUS_ONE );
        model.put( MARK_ID_DIRECTORY, nIdDirectory );
        model.put( MARK_TASK_FORM, strHtmlTasksForm );

        return getPage( MESSAGE_MULTIVIEW_TITLE, TEMPLATE_TASK_FORM, model );
    }

    /**
     * Process workflow action
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( value = ACTION_SAVE_TASK_FORM )
    public String doSaveTaskForm( HttpServletRequest request )
    {
        Integer nIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) != null ? Integer.parseInt( request
                .getParameter( PARAMETER_ID_DIRECTORY_RECORD ) ) : null;
        Integer nIdAction = request.getParameter( PARAMETER_ID_ACTION ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ID_ACTION ) ) : null;
        int nIdDirectory = NumberUtils.toInt( request.getParameter( MARK_ID_DIRECTORY ), NumberUtils.INTEGER_MINUS_ONE );

        if ( WorkflowService.getInstance( ).canProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request, false ) )
        {

            try
            {
                String strError = WorkflowService.getInstance( ).doSaveTasksForm( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request,
                        getLocale( ) );
                if ( strError != null )
                {
                    addError( strError );
                    return redirect( request, VIEW_TASKS_FORM, PARAMETER_ID_DIRECTORY_RECORD, nIdRecord, PARAMETER_ID_ACTION, nIdAction );
                }

            }
            catch( Exception e )
            {
                AppLogService.error( "Error processing action for record '" + nIdRecord, e );

            }
        }
        else
        {
            return redirectView( request, VIEW_TASKS_FORM );
        }

        return manageRedirection( request );
    }

    /**
     * Cancel an action of the workflow
     * 
     * @param request
     *            The HttpServletRequest
     * @return the Jsp URL to return
     */
    @Action( value = ACTION_CANCEL_TASK_FORM )
    public String doCancelTaskForm( HttpServletRequest request )
    {
        Map<String, String> mapParameters = new LinkedHashMap<>( );
        mapParameters.put( PARAMETER_ID_DIRECTORY_RECORD, request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) );

        return redirect( request, VIEW_RECORD_VISUALISATION, mapParameters );
    }

    /**
     * Redirect to the appropriate view
     * 
     * @param request
     *            The HttpServletRequest to retrieve data from
     * @return redirect to the appropriate view
     */
    private String manageRedirection( HttpServletRequest request )
    {
        String strWorkflowActionRedirection = request.getParameter( DirectoryMultiviewConstants.PARAMETER_WORKFLOW_ACTION_REDIRECTION );
        if ( StringUtils.isNotBlank( strWorkflowActionRedirection ) )
        {
            MultiviewDirectoryWorkflowRedirectionEnum workflowActionRedirectionEnum = MultiviewDirectoryWorkflowRedirectionEnum
                    .getEnumNameByValue( strWorkflowActionRedirection );
            switch( workflowActionRedirectionEnum )
            {
                case LIST:
                    return redirectToRecordList( request );
                case DETAILS:
                    return redirectToRecordView( request );
                default:
                    return defaultRedirection( request );
            }
        }
        else
        {
            return defaultRedirection( request );
        }
    }

    /**
     * Redirect to the page of the list of all records
     * 
     * @param request
     *            The HttpServletRequest to retrieve data from
     * @return redirect to the page with the list of all records
     */
    private String redirectToRecordList( HttpServletRequest request )
    {
        return redirectView( request, VIEW_MULTIVIEW );
    }

    /**
     * Redirect to the page of a record
     * 
     * @param request
     *            The HttpServletRequest to retrieve data from
     * @return redirect to the page of the record
     */
    private String redirectToRecordView( HttpServletRequest request )
    {
        return defaultRedirection( request );
    }

    /**
     * Return to the default view which is the page of the record
     * 
     * @param request
     *            The HttpServletRequest to retrieve data from
     * @return redirect to the default view which is the page of the record
     */
    private String defaultRedirection( HttpServletRequest request )
    {
        try
        {
            int nIdRecord = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) );

            Map<String, String> mapParameters = new LinkedHashMap<>( );
            mapParameters.put( PARAMETER_ID_DIRECTORY_RECORD, String.valueOf( nIdRecord ) );

            return redirect( request, VIEW_RECORD_VISUALISATION, mapParameters );
        }
        catch( NumberFormatException exception )
        {
            AppLogService.error( "The given id directory record is not valid !" );

            return redirectView( request, VIEW_MULTIVIEW );
        }
    }
}
