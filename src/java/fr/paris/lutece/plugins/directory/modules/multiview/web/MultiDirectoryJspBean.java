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
import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import fr.paris.lutece.plugins.directory.modules.multiview.web.user.UserFactory;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.service.upload.DirectoryAsynchronousUploadHandler;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import static fr.paris.lutece.plugins.directory.utils.DirectoryUtils.PARAMETER_ID_ACTION;
import fr.paris.lutece.plugins.directory.web.action.DirectoryActionResult;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
import fr.paris.lutece.plugins.directory.web.action.IDirectoryAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilterType;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove)
 */
public class MultiDirectoryJspBean extends PluginAdminPageJspBean
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -8417121042985481292L;

    // Templates
    private static final String TEMPLATE_MANAGE_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/manage_directory_record.html";
    private static final String TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/manage_multi_directory_record.html";
    private static final String TEMPLATE_RESOURCE_HISTORY = "admin/plugins/directory/modules/multiview/resource_history.html";
    private static final String TEMPLATE_VIEW_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/view_directory_record.html";
    private static final String TEMPLATE_RECORD_HISTORY = "admin/plugins/directory/modules/multiview/record_history.html";

    // Messages (I18n keys)
    private static final String MESSAGE_CONFIRM_CHANGE_STATES_RECORD = "directory.message.confirm_change_states_record";
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";

    // properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "directory.manage_directory_record.page_title";
    private static final String PROPERTY_RESOURCE_HISTORY_PAGE_TITLE = "directory.resource_history.page_title";
    private static final String PROPERTY_ENTRY_TYPE_DIRECTORY = "directory.entry_type.directory";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_NUMBERING = "directory.entry_type.numbering";

    // public properties
    public static final String PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY = "DIRECTORY_MULTIVIEW";

    // Markers
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ENTRY_LIST_GEOLOCATION = "entry_list_geolocation";
    private static final String MARK_DIRECTORY_LIST = "directory_list";
    private static final String MARK_DIRECTORY = "directory";
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_ENTRY_LIST_FORM_MAIN_SEARCH = "entry_list_form_main_search";
    private static final String MARK_ENTRY_LIST_FORM_COMPLEMENTARY_SEARCH = "entry_list_form_complementary_search";
    private static final String MARK_ENTRY_LIST_SEARCH_RESULT = "entry_list_search_result";
    private static final String MARK_NUMBER_RECORD = "number_record";
    private static final String MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD = "map_id_entry_list_record_field";
    private static final String MARK_ID_ENTRY_TYPE_DIRECTORY = "id_entry_type_directory";
    private static final String MARK_ID_ENTRY_TYPE_GEOLOCATION = "id_entry_type_geolocation";
    private static final String MARK_ID_ENTRY_TYPE_IMAGE = "id_entry_type_image";
    private static final String MARK_ID_ENTRY_TYPE_MYLUTECE_USER = "id_entry_type_mylutece_user";
    private static final String MARK_ID_ENTRY_TYPE_NUMBERING = "id_entry_type_numbering";
    private static final String MARK_SHOW_DATE_CREATION_RECORD = "show_date_creation_record";
    private static final String MARK_SHOW_DATE_CREATION_RESULT = "show_date_creation_result";
    private static final String MARK_RECORD_DATE_CREATION = "date_creation";
    private static final String MARK_DATE_CREATION_SEARCH = "date_creation_search";
    private static final String MARK_DATE_CREATION_BEGIN_SEARCH = "date_creation_begin_search";
    private static final String MARK_DATE_CREATION_END_SEARCH = "date_creation_end_search";
    private static final String MARK_DIRECTORY_ACTIONS = "directory_actions";
    private static final String MARK_SHOW_DATE_MODIFICATION_RECORD = "show_date_modification_record";
    private static final String MARK_SHOW_DATE_MODIFICATION_RESULT = "show_date_modification_result";
    private static final String MARK_RECORD_DATE_MODIFICATION = "date_modification";
    private static final String MARK_DATE_MODIFICATION_SEARCH = "date_modification_search";
    private static final String MARK_DATE_MODIFICATION_BEGIN_SEARCH = "date_modification_begin_search";
    private static final String MARK_DATE_MODIFICATION_END_SEARCH = "date_modification_end_search";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";

    private static final String MARK_RECORD = "record";
    private static final String MARK_RESOURCE_ACTIONS_LIST = "resource_actions_list";
    private static final String MARK_RECORD_ASSIGNMENT_MAP = "recordAssignmentMap";
    private static final String MARK_RESOURCE_ACTIONS = "resource_actions";
    private static final String MARK_RESOURCE_HISTORY = "resource_history";
    private static final String MARK_HISTORY_WORKFLOW_ENABLED = "history_workflow";
    private static final String MARK_PERMISSION_CREATE_RECORD = "permission_create_record";
    private static final String MARK_PERMISSION_MASS_PRINT = "permission_mass_print";
    private static final String MARK_PERMISSION_VISUALISATION_MYLUTECE_USER = "permission_visualisation_mylutece_user";
    private static final String MARK_IS_WORKFLOW_ENABLED = "is_workflow_enabled";
    private static final String MARK_SEARCH_STATE_WORKFLOW = "search_state_workflow";
    private static final String MARK_WORKFLOW_STATE_SEARCH_ID = "search_state_workflow_id_default";
    private static final String MARK_DIRECTORY_SEARCH_ID = "search_directory_id_default";
    private static final String MARK_PERIOD_SEARCH_ID = "search_period_id_default";
    private static final String MARK_USER_FACTORY = "user_factory";

    // JSP URL
    private static final String JSP_MANAGE_DIRECTORY = "jsp/admin/plugins/directory/modules/multiview/ManageDirectory.jsp";
    private static final String JSP_TASKS_FORM_WORKFLOW = "jsp/admin/plugins/directory/modules/multiview/TasksFormWorkflow.jsp";
    private static final String JSP_DO_CHANGE_STATES_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoChangeStatesRecord.jsp";
    private static final String JSP_DO_VISUALISATION_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoVisualisationRecord.jsp";
    private static final String JSP_RESOURCE_HISTORY = "jsp/admin/plugins/directory/modules/multiview/ResourceHistory.jsp";
    private static final String JSP_ACTION_RESULT = "jsp/admin/plugins/directory/ActionResult.jsp";
    private static final String JSP_MANAGE_MULTI_DIRECTORY_RECORD = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = DirectoryUtils.PARAMETER_ID_DIRECTORY;
    private static final String PARAMETER_ID_STATE_WORKFLOW = "search_state_workflow";
    private static final String PARAMETER_ID_FILTER_PERIOD = "search_open_since";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_SESSION = DirectoryUtils.PARAMETER_SESSION;
    private static final String PARAMETER_RESET_SEARCH = "resetsearch";

    // constants
    private static final String CONSTANT_IDENTIFYING_ENTRY_TITLE = "GUID";

    // defaults
    private String DEFAULT_TYPE_IMAGE = "10";
    private int DEFAULT_ACTION_ID = -1;

    // session fields
    private DirectoryAdminSearchFields _searchFields = new DirectoryAdminSearchFields( );
    private DirectoryActionResult _directoryActionResult = new DirectoryActionResult( );
    private IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );

    private List<DirectoryViewFilter> _directoryViewList = new ArrayList<>( );
    private HashMap<Integer, Directory> _directoryList = new HashMap<>( );
    private HashMap<Integer, ReferenceList> _workflowStateByDirectoryList = new HashMap<>( );

    /**
     * Gets the DirectoryAdminSearchFields
     * 
     * @return searchFields
     */
    public DirectoryAdminSearchFields getSearchFields( )
    {
        return _searchFields;
    }

    /**
     * Return management of directory record ( list of directory record ).
     * 
     * @param request
     *            The Http request
     * @param response
     *            the Http response
     * @param multiDirectories
     *            if true, a generic directory template is displayed
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return IPluginActionResult
     */
    public IPluginActionResult getManageDirectoryRecord( HttpServletRequest request, HttpServletResponse response, boolean multiDirectories )
            throws AccessDeniedException
    {

        // first - see if there is an invoked action
        IDirectoryAction action = PluginActionManager.getPluginAction( request, IDirectoryAction.class );

        if ( action != null )
        {
            if ( AppLogService.isDebugEnabled( ) )
            {
                AppLogService.debug( "Processing directory action " + action.getName( ) );
            }

            return action.process( request, response, getUser( ), _searchFields );
        }

        // reinit search
        reInitDirectoryRecordFilter( );

        // display could have been an action but it's the default one an will always be here...
        DefaultPluginActionResult result = new DefaultPluginActionResult( );
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdWorkflowState = request.getParameter( PARAMETER_ID_STATE_WORKFLOW );
        String strIdPeriodParameter = request.getParameter( PARAMETER_ID_FILTER_PERIOD );

        int nIdWorkflowState = DirectoryUtils.convertStringToInt( strIdWorkflowState );
        int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
        int nIdPeriodParameter = DirectoryUtils.convertStringToInt( strIdPeriodParameter );

        boolean bWorkflowServiceEnable = WorkflowService.getInstance( ).isAvailable( );
        AdminUser adminUser = getUser( );

        // list of directories of the filters
        if ( _directoryViewList == null || _directoryViewList.size( ) == 0 )
        {

            _directoryViewList = DirectoryViewFilterHome.getDirectoryFiltersList( );
            for ( DirectoryViewFilter dvf : _directoryViewList )
            {
                Directory directory = DirectoryHome.findByPrimaryKey( dvf.getIdDirectory( ), getPlugin( ) );
                if ( RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_MANAGE_RECORD, getUser( ) )
                        || AdminWorkgroupService.isAuthorized( directory, getUser( ) ) )
                {
                    _directoryList.put( directory.getIdDirectory( ), directory );
                    ReferenceList workflowStateList = new ReferenceList( );

                    Collection<State> colState = WorkflowService.getInstance( ).getAllStateByWorkflow(
                            _directoryList.get( directory.getIdDirectory( ) ).getIdWorkflow( ), adminUser );
                    ReferenceItem item = new ReferenceItem( );
                    item.setCode( "-1" );
                    item.setName( " - " );
                    workflowStateList.add( item );

                    if ( colState != null )
                    {
                        for ( State stateWorkflow : colState )
                        {
                            item = new ReferenceItem( );
                            item.setCode( String.valueOf( stateWorkflow.getId( ) ) );
                            item.setName( stateWorkflow.getName( ) );
                            workflowStateList.add( item );
                        }
                    }

                    _workflowStateByDirectoryList.put( directory.getIdDirectory( ), workflowStateList );
                }
            }
        }

        // get the selected directory
        if ( nIdDirectory > 0 )
        {
            // access by ONE directory
            Directory directory = _directoryList.get( nIdDirectory );
            if ( ( directory == null )
                    || ( !RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_MANAGE_RECORD, getUser( ) ) || !AdminWorkgroupService
                            .isAuthorized( directory, getUser( ) ) ) )
            {
                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
            }
        }

        if ( ( request.getParameter( PARAMETER_SESSION ) == null ) || Boolean.parseBoolean( request.getParameter( PARAMETER_RESET_SEARCH ) ) )
        {
            reInitDirectoryRecordFilter( );
        }

        _searchFields.setRedirectUrl( request );
        _searchFields.setCurrentPageIndexDirectoryRecord( Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX,
                _searchFields.getCurrentPageIndexDirectoryRecord( ) ) );
        _searchFields.setItemsPerPageDirectoryRecord( Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE,
                _searchFields.getItemsPerPageDirectoryRecord( ), _searchFields.getDefaultItemsPerPage( ) ) );

        List<Integer> listResultRecordId = new ArrayList<>( );
        List<IEntry> listEntryFormMainSearch = new ArrayList<>( );
        List<IEntry> listEntryFormComplementarySearch = new ArrayList<>( );
        List<IEntry> listEntryResultSearch = new ArrayList<>( );
        List<IEntry> listEntryGeolocation = new ArrayList<>( );

        for ( DirectoryViewFilter dvf : _directoryViewList )
        {
            // case of request of results of ONE directory
            if ( nIdDirectory > 0 && nIdDirectory != dvf.getIdDirectory( ) )
                continue;

            Directory directory = _directoryList.get( dvf.getIdDirectory( ) );

            // build entryFilter
            EntryFilter entryFilter = new EntryFilter( );
            entryFilter.setIdDirectory( directory.getIdDirectory( ) );
            entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
            entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

            // get the Entries for this directory

            for ( IEntry entry : EntryHome.getEntryList( entryFilter, getPlugin( ) ) )
            {
                IEntry entryTmp = EntryHome.findByPrimaryKey( entry.getIdEntry( ), getPlugin( ) );

                if ( entryTmp.isWorkgroupAssociated( ) )
                {
                    entryTmp.setFields( DirectoryUtils.getAuthorizedFieldsByWorkgroup( entryTmp.getFields( ), getUser( ) ) );
                }

                // keep only the entry of the filter (in place of : entryTmp.isIndexed( ) AND entry.isShownInAdvancedSearch() )
                if ( entryTmp.getIdEntry( ) == dvf.getIdEntryTitle( ) )
                {
                    listEntryFormMainSearch.add( entryTmp );
                }

                // keep only the entry of the filter and the identifyer (in place of entry.isShownInResultList( ) )
                if ( entryTmp.getIdEntry( ) == dvf.getIdEntryTitle( ) || CONSTANT_IDENTIFYING_ENTRY_TITLE.equals( entryTmp.getTitle( ) ) )
                {
                    listEntryResultSearch.add( entryTmp );

                    // add geolocation entries
                    if ( entry.getEntryType( ).getIdType( ) == AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) )
                    {
                        listEntryGeolocation.add( entry );
                    }
                }
            }

        }

        // get the records filtred list
        List<RecordAssignment> recordAssignmentList = new ArrayList<>( );

        // for each unit of the user, get records :
        IUnitService unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
        List<Unit> unitList = unitService.getUnitsByIdUser( getUser( ).getUserId( ), true );

        if ( unitList != null )
        {
            for ( Unit unit : unitList )
            {
                HashMap<String, Integer> filterParameters = new HashMap<>( );

                // filter by unit
                filterParameters.put( RecordAssignmentFilterType.USER_UNIT_ID.toString( ), unit.getIdUnit( ) );
                filterParameters.put( RecordAssignmentFilterType.RECURSIVE_SEARCH_DEPTH.toString( ), 3 );
                filterParameters.put( RecordAssignmentFilterType.ACTIVE_RECORDS_ONLY.toString( ), 1 );

                // filter by Directory (and state)
                if ( nIdDirectory > 0 )
                {
                    filterParameters.put( RecordAssignmentFilterType.DIRECTORY_ID.toString( ), nIdDirectory );
                    if ( nIdWorkflowState > 0 )
                        filterParameters.put( RecordAssignmentFilterType.STATE_ID.toString( ), nIdWorkflowState );
                }

                // filter by period
                if ( nIdPeriodParameter > 0 )
                    filterParameters.put( RecordAssignmentFilterType.FILTER_PERIOD.toString( ), nIdPeriodParameter );

                recordAssignmentList.addAll( RecordAssignmentHome.getRecordAssignmentsFiltredList( filterParameters, getPlugin( ) ) );

            }
        }

        // get the records Id from the assigned records & prepare an hashmap for the model
        HashMap<String, RecordAssignment> recordAssignmentMap = new HashMap<>( );
        for ( RecordAssignment assignedRecord : recordAssignmentList )
        {
            listResultRecordId.add( assignedRecord.getIdRecord( ) );
            if ( recordAssignmentMap.containsKey( String.valueOf( assignedRecord.getIdRecord( ) ) )
                    && ( recordAssignmentMap.get( String.valueOf( assignedRecord.getIdRecord( ) ) ).getAssignmentDate( ).before( assignedRecord
                            .getAssignmentDate( ) ) ) ) // keep only the last one
                recordAssignmentMap.put( String.valueOf( assignedRecord.getIdRecord( ) ), assignedRecord );
        }

        // Store the list of id records in session
        _searchFields.setListIdsResultRecord( listResultRecordId );

        // HACK : We copy the list so workflow does not clear the paginator list.
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( new ArrayList<>( listResultRecordId ),
                _searchFields.getItemsPerPageDirectoryRecord( ), getJspManageMultiDirectoryRecord( request ), PARAMETER_PAGE_INDEX,
                _searchFields.getCurrentPageIndexDirectoryRecord( ), getLocale( ) );

        // get only record for page items.
        List<Record> lRecord = _recordService.loadListByListId( paginator.getPageItems( ), getPlugin( ) );

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( );
        RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
        recordFieldFilter.setIsEntryShownInResultList( RecordFieldFilter.FILTER_TRUE );

        bWorkflowServiceEnable = true;

        List<Map<String, Object>> listResourceActions = new ArrayList<>( lRecord.size( ) );

        List<DirectoryAction> listActionsForDirectoryEnable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_ENABLE, getPlugin( ),
                getLocale( ) );
        List<DirectoryAction> listActionsForDirectoryDisable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_DISABLE, getPlugin( ),
                getLocale( ) );

        if ( nIdDirectory > 0 )
        {
            listActionsForDirectoryEnable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryEnable,
                    _directoryList.get( nIdDirectory ), getUser( ) );
            listActionsForDirectoryDisable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryDisable,
                    _directoryList.get( nIdDirectory ), getUser( ) );
        }

        // Get asynchronous file names put at false for better performance
        // since it must call a webservice to get the file name
        boolean bGetFileName = false;

        for ( Record record : lRecord )
        {
            // data complement (should be done in directory plugin)
            record.getDirectory( ).setIdWorkflow( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getIdWorkflow( ) );
            record.getDirectory( ).setTitle( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getTitle( ) );

            // add resourceActions
            listResourceActions.add( DirectoryService.getInstance( ).getResourceAction( record, record.getDirectory( ), listEntryResultSearch, adminUser,
                    listActionsForDirectoryEnable, listActionsForDirectoryDisable, bGetFileName, getPlugin( ) ) );
        }

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_ID_ENTRY_TYPE_IMAGE, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_IMAGE, 10 ) );
        model.put( MARK_ID_ENTRY_TYPE_DIRECTORY, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_DIRECTORY, 12 ) );
        model.put( MARK_ID_ENTRY_TYPE_GEOLOCATION, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) );
        model.put( MARK_ID_ENTRY_TYPE_MYLUTECE_USER, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_MYLUTECE_USER, 19 ) );
        model.put( MARK_ID_ENTRY_TYPE_NUMBERING, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_NUMBERING, 11 ) );
        model.put( MARK_ENTRY_LIST_GEOLOCATION, listEntryGeolocation );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( _searchFields.getItemsPerPageDirectoryRecord( ) ) );
        model.put( MARK_ENTRY_LIST_FORM_MAIN_SEARCH, listEntryFormMainSearch );
        model.put( MARK_ENTRY_LIST_FORM_COMPLEMENTARY_SEARCH, listEntryFormComplementarySearch );
        model.put( MARK_ENTRY_LIST_SEARCH_RESULT, listEntryResultSearch );

        model.put( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD, _searchFields.getMapQuery( ) );
        model.put( MARK_DATE_CREATION_SEARCH, _searchFields.getDateCreationRecord( ) );
        model.put( MARK_DATE_CREATION_BEGIN_SEARCH, _searchFields.getDateCreationBeginRecord( ) );
        model.put( MARK_DATE_CREATION_END_SEARCH, _searchFields.getDateCreationEndRecord( ) );
        model.put( MARK_DATE_MODIFICATION_SEARCH, _searchFields.getDateModificationRecord( ) );
        model.put( MARK_DATE_MODIFICATION_BEGIN_SEARCH, _searchFields.getDateModificationBeginRecord( ) );
        model.put( MARK_DATE_MODIFICATION_END_SEARCH, _searchFields.getDateModificationEndRecord( ) );

        model.put( MARK_NUMBER_RECORD, listResultRecordId.size( ) );
        model.put( MARK_RESOURCE_ACTIONS_LIST, listResourceActions );
        model.put( MARK_HISTORY_WORKFLOW_ENABLED, bHistoryEnabled );
        model.put( MARK_PERMISSION_CREATE_RECORD,
                RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_CREATE_RECORD, getUser( ) ) );
        model.put( MARK_PERMISSION_MASS_PRINT,
                RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_MASS_PRINT, getUser( ) ) );
        model.put( MARK_PERMISSION_VISUALISATION_MYLUTECE_USER, RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory,
                DirectoryResourceIdService.PERMISSION_VISUALISATION_MYLUTECE_USER, getUser( ) ) );

        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_IS_WORKFLOW_ENABLED, bWorkflowServiceEnable );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        model.put( MARK_DIRECTORY_LIST, getDirectoryReferenceList( _directoryList ) );

        if ( nIdDirectory > 0 )
        {
            model.put( MARK_SHOW_DATE_CREATION_RESULT, _directoryList.get( nIdDirectory ).isDateShownInResultList( ) );
            model.put( MARK_SHOW_DATE_MODIFICATION_RESULT, _directoryList.get( nIdDirectory ).isDateModificationShownInResultList( ) );
            model.put( MARK_DIRECTORY, _directoryList.get( nIdDirectory ) );
            model.put( MARK_SEARCH_STATE_WORKFLOW, _workflowStateByDirectoryList.get( nIdDirectory ) );
            model.put( MARK_DIRECTORY_SEARCH_ID, nIdDirectory );
        }
        if ( nIdWorkflowState > 0 )
            model.put( MARK_WORKFLOW_STATE_SEARCH_ID, nIdWorkflowState );
        if ( nIdPeriodParameter > 0 )
            model.put( MARK_PERIOD_SEARCH_ID, nIdPeriodParameter );

        model.put( MARK_RECORD_ASSIGNMENT_MAP, recordAssignmentMap );

        PluginActionManager.fillModel( request, adminUser, model, IDirectoryAction.class, MARK_DIRECTORY_ACTIONS );

        setPageTitleProperty( PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE );

        HtmlTemplate templateList = AppTemplateService.getTemplate( ( multiDirectories ? TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD
                : TEMPLATE_MANAGE_DIRECTORY_RECORD ), getLocale( ), model );

        result.setHtmlContent( getAdminPage( templateList.getHtml( ) ) );

        return result;
    }

    /**
     * return the resource history
     * 
     * @param request
     *            the httpRequest
     * @throws AccessDeniedException
     *             AccessDeniedException
     * @return the resource history
     */
    public String getResourceHistory( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD );
        int nIdRecord = DirectoryUtils.convertStringToInt( strIdRecord );

        Record record = _recordService.findByPrimaryKey( nIdRecord, getPlugin( ) );
        int nIdDirectory = record.getDirectory( ).getIdDirectory( );
        Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );
        int nIdWorkflow = ( DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) ) ).getIdWorkflow( );

        // Get asynchronous file names
        boolean bGetFileName = true;

        if ( ( record == null )
                || ( directory == null )
                || !RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdDirectory ), DirectoryResourceIdService.PERMISSION_HISTORY_RECORD,
                        getUser( ) ) || !AdminWorkgroupService.isAuthorized( record, getUser( ) )
                || !AdminWorkgroupService.isAuthorized( directory, getUser( ) ) )
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        EntryFilter filter;
        filter = new EntryFilter( );
        filter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );
        filter.setIsShownInHistory( EntryFilter.FILTER_TRUE );

        List<IEntry> listEntry = EntryHome.getEntryList( filter, getPlugin( ) );

        // List directory actions
        List<DirectoryAction> listActionsForDirectoryEnable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_ENABLE, getPlugin( ),
                getLocale( ) );
        List<DirectoryAction> listActionsForDirectoryDisable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_DISABLE, getPlugin( ),
                getLocale( ) );

        listActionsForDirectoryEnable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryEnable,
                record.getDirectory( ), getUser( ) );
        listActionsForDirectoryDisable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryDisable,
                record.getDirectory( ), getUser( ) );

        _searchFields.setRedirectUrl( request );
        _searchFields.setItemNavigatorHistory( nIdRecord, AppPathService.getBaseUrl( request ) + JSP_RESOURCE_HISTORY,
                DirectoryUtils.PARAMETER_ID_DIRECTORY_RECORD );

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( ) && ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL );

        Map<String, Object> model = new HashMap<String, Object>( );

        if ( directory != null )
        {
            if ( directory.isDateShownInHistory( ) )
            {
                model.put( MARK_RECORD_DATE_CREATION, record.getDateCreation( ) );
            }

            if ( directory.isDateModificationShownInHistory( ) )
            {
                model.put( MARK_RECORD_DATE_MODIFICATION, record.getDateModification( ) );
            }
        }

        model.put( MARK_RECORD, record );
        model.put( MARK_ENTRY_LIST, listEntry );
        model.put( MARK_DIRECTORY, directory );
        model.put( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD, DirectoryUtils.getMapIdEntryListRecordField( listEntry, nIdRecord, getPlugin( ), bGetFileName ) );

        model.put( MARK_RESOURCE_HISTORY,
                WorkflowService.getInstance( ).getDisplayDocumentHistory( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, request, getLocale( ) ) );
        model.put(
                MARK_RESOURCE_ACTIONS,
                DirectoryService.getInstance( ).getResourceAction( record, directory, listEntry, getUser( ), listActionsForDirectoryEnable,
                        listActionsForDirectoryDisable, bGetFileName, getPlugin( ) ) );
        model.put( MARK_ITEM_NAVIGATOR, _searchFields.getItemNavigatorHistory( ) );
        model.put( MARK_HISTORY_WORKFLOW_ENABLED, bHistoryEnabled );

        setPageTitleProperty( PROPERTY_RESOURCE_HISTORY_PAGE_TITLE );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_RESOURCE_HISTORY, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
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
    public String getRecordVisualisation( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD );
        int nIdRecord = DirectoryUtils.convertStringToInt( strIdRecord );
        EntryFilter filter;

        Record record = _recordService.findByPrimaryKey( nIdRecord, getPlugin( ) );

        int nIdDirectory = record.getDirectory( ).getIdDirectory( );
        Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );

        if ( ( record == null )
                || ( directory == null )
                || !RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdRecord ),
                        DirectoryResourceIdService.PERMISSION_VISUALISATION_RECORD, getUser( ) ) || !AdminWorkgroupService.isAuthorized( record, getUser( ) )
                || !AdminWorkgroupService.isAuthorized( directory, getUser( ) ) )
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        filter = new EntryFilter( );
        filter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );
        filter.setIsGroup( EntryFilter.FILTER_TRUE );

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

        _searchFields.setRedirectUrl( request );
        _searchFields.setItemNavigatorViewRecords( nIdRecord, AppPathService.getBaseUrl( request ) + JSP_DO_VISUALISATION_RECORD,
                DirectoryUtils.PARAMETER_ID_DIRECTORY_RECORD );

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( ) && ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL );

        // Get asynchronous file names
        boolean bGetFileName = true;

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
        model.put( MARK_ITEM_NAVIGATOR, _searchFields.getItemNavigatorViewRecords( ) );
        model.put( MARK_HISTORY_WORKFLOW_ENABLED, bHistoryEnabled );

        model.put( MARK_USER_FACTORY, UserFactory.getInstance( ) );
        model.put(
                MARK_RESOURCE_HISTORY,
                WorkflowService.getInstance( ).getDisplayDocumentHistory( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, directory.getIdWorkflow( ), request,
                        getLocale( ), model, TEMPLATE_RECORD_HISTORY ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_VIEW_DIRECTORY_RECORD, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * return url of the jsp manage directory
     * 
     * @param request
     *            The HTTP request
     * @return url of the jsp manage directory
     */
    private String getJspManageDirectory( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_DIRECTORY;
    }

    /**
     * reinit directory recordFilter
     */
    private void reInitDirectoryRecordFilter( )
    {
        _searchFields.setItemsPerPageDirectoryRecord( 0 );
        _searchFields.setCurrentPageIndexDirectory( null );
        _searchFields.setMapQuery( null );
        _searchFields.setItemNavigatorViewRecords( null );
        _searchFields.setItemNavigatorHistory( null );
        _searchFields.setSortEntry( null );
        _searchFields.setSortOrder( RecordFieldFilter.ORDER_NONE );
        _searchFields.setListIdsResultRecord( new ArrayList<Integer>( ) );
    }

    /**
     * Gets the confirmation page of changing the state of the records
     * 
     * @param request
     *            The HTTP request
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return the confirmation page of changing the state of the records
     */
    public String getConfirmChangeStatesRecord( HttpServletRequest request ) throws AccessDeniedException
    {
        String [ ] listIdsDirectoryRecord = request.getParameterValues( PARAMETER_ID_DIRECTORY_RECORD );

        if ( ( listIdsDirectoryRecord != null ) && ( listIdsDirectoryRecord.length > 0 ) )
        {
            String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );

            // If the id directory is not in the parameter, then fetch it from the first record
            // assuming all records are from the same directory
            if ( StringUtils.isBlank( strIdDirectory ) || !StringUtils.isNumeric( strIdDirectory ) )
            {
                String strIdDirectoryRecord = listIdsDirectoryRecord [0];
                int nIdDirectoryRecord = DirectoryUtils.convertStringToInt( strIdDirectoryRecord );
                Record record = _recordService.findByPrimaryKey( nIdDirectoryRecord, getPlugin( ) );
                strIdDirectory = Integer.toString( record.getDirectory( ).getIdDirectory( ) );
            }

            int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
            Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );

            UrlItem url = new UrlItem( JSP_DO_CHANGE_STATES_RECORD );
            url.addParameter( DirectoryUtils.PARAMETER_ID_DIRECTORY, nIdDirectory );

            for ( String strIdDirectoryRecord : listIdsDirectoryRecord )
            {
                int nIdDirectoryRecord = DirectoryUtils.convertStringToInt( strIdDirectoryRecord );
                Record record = _recordService.findByPrimaryKey( nIdDirectoryRecord, getPlugin( ) );

                if ( ( record == null )
                        || ( directory == null )
                        || ( record.getDirectory( ).getIdDirectory( ) != nIdDirectory )
                        || !RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdDirectory ),
                                DirectoryResourceIdService.PERMISSION_CHANGE_STATE_RECORD, getUser( ) )
                        || !AdminWorkgroupService.isAuthorized( record, getUser( ) ) || !AdminWorkgroupService.isAuthorized( directory, getUser( ) ) )
                {
                    throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
                }

                url.addParameter( PARAMETER_ID_DIRECTORY_RECORD, nIdDirectoryRecord );
            }

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CHANGE_STATES_RECORD, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Perform the directory record supression
     * 
     * @param request
     *            The HTTP request
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return The URL to go after performing the action
     */
    public String doChangeStatesRecord( HttpServletRequest request ) throws AccessDeniedException
    {
        String [ ] listIdsDirectoryRecord = request.getParameterValues( PARAMETER_ID_DIRECTORY_RECORD );

        if ( ( listIdsDirectoryRecord != null ) && ( listIdsDirectoryRecord.length > 0 ) )
        {
            String strIdDirectory = request.getParameter( DirectoryUtils.PARAMETER_ID_DIRECTORY );
            int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
            Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );

            for ( String strIdDirectoryRecord : listIdsDirectoryRecord )
            {
                int nIdDirectoryRecord = DirectoryUtils.convertStringToInt( strIdDirectoryRecord );
                Record record = _recordService.findByPrimaryKey( nIdDirectoryRecord, getPlugin( ) );

                if ( ( record == null )
                        || ( directory == null )
                        || ( record.getDirectory( ).getIdDirectory( ) != nIdDirectory )
                        || !RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdDirectory ),
                                DirectoryResourceIdService.PERMISSION_CHANGE_STATE_RECORD, getUser( ) )
                        || !AdminWorkgroupService.isAuthorized( record, getUser( ) ) || !AdminWorkgroupService.isAuthorized( directory, getUser( ) ) )
                {
                    throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
                }

                record.setEnabled( !record.isEnabled( ) );
                _recordService.update( record, getPlugin( ) );
            }

            return DirectoryUtils.getJspManageDirectoryRecord( request, nIdDirectory );
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Do process the workflow actions
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     */
    public String doProcessAction( HttpServletRequest request )
    {
        String [ ] listIdsDirectoryRecord = request.getParameterValues( DirectoryUtils.PARAMETER_ID_DIRECTORY_RECORD );

        if ( ( listIdsDirectoryRecord != null ) && ( listIdsDirectoryRecord.length > 0 ) )
        {
            String strShowActionResult = request.getParameter( DirectoryUtils.PARAMETER_SHOW_ACTION_RESULT );
            boolean bShowActionResult = StringUtils.isNotBlank( strShowActionResult );

            String strIdAction = request.getParameter( DirectoryUtils.PARAMETER_ID_ACTION );
            int nIdAction = DirectoryUtils.convertStringToInt( strIdAction );

            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
            {
                return getJspTasksForm( request, listIdsDirectoryRecord, nIdAction, bShowActionResult );
            }

            String strIdDirectory = request.getParameter( DirectoryUtils.PARAMETER_ID_DIRECTORY );

            // If the id directory is not in the parameter, then fetch it from the first record
            // assuming all records are from the same directory
            if ( StringUtils.isBlank( strIdDirectory ) || !StringUtils.isNumeric( strIdDirectory ) )
            {
                String strIdDirectoryRecord = listIdsDirectoryRecord [0];
                int nIdDirectoryRecord = DirectoryUtils.convertStringToInt( strIdDirectoryRecord );
                Record record = _recordService.findByPrimaryKey( nIdDirectoryRecord, getPlugin( ) );
                strIdDirectory = Integer.toString( record.getDirectory( ).getIdDirectory( ) );
            }

            int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );

            _directoryActionResult.doProcessAction( nIdDirectory, nIdAction, listIdsDirectoryRecord, getPlugin( ), getLocale( ), request );

            if ( bShowActionResult )
            {
                return getJspActionResults( request, nIdDirectory, nIdAction );
            }

            return getRedirectUrl( request );
        }

        return getRedirectUrl( request );
    }

    /**
     * Get the redirect url
     * 
     * @param request
     *            the http servlet request
     * @return the redirect url
     */
    public String getRedirectUrl( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( _searchFields.getRedirectUrl( ) ) )
        {
            return _searchFields.getRedirectUrl( );
        }

        return getJspManageDirectory( request );
    }

    /**
     * return url of the jsp manage multi directory record
     *
     * @param request
     *            The HTTP request
     * @param directory
     *            the directory
     * @return url of the jsp manage directory record
     */
    public static String getJspManageMultiDirectoryRecord( HttpServletRequest request )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_MULTI_DIRECTORY_RECORD );

        urlItem.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        strAscSort = request.getParameter( Parameters.SORTED_ASC );

        urlItem.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        urlItem.addParameter( Parameters.SORTED_ASC, strAscSort );

        return urlItem.getUrl( );
    }

    /**
     * Return url of the jsp action results
     * 
     * @param request
     *            the HTTP request
     * @param nIdDirectory
     *            the id directory
     * @param nIdAction
     *            the id action
     * @return the JSP
     */
    private String getJspActionResults( HttpServletRequest request, int nIdDirectory, int nIdAction )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_ACTION_RESULT );
        url.addParameter( DirectoryUtils.PARAMETER_ID_DIRECTORY, nIdDirectory );
        url.addParameter( PARAMETER_ID_ACTION, nIdAction );

        return url.getUrl( );
    }

    /**
     * return url of the jsp manage commentaire
     * 
     * @param request
     *            The HTTP request
     * @param listIdsTestResource
     *            the list if id resource
     * @param nIdAction
     *            the id action
     * @param bShowActionResult
     *            true if it must show the action result, false otherwise
     * @return url of the jsp manage commentaire
     */
    private String getJspTasksForm( HttpServletRequest request, String [ ] listIdsTestResource, int nIdAction, boolean bShowActionResult )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_TASKS_FORM_WORKFLOW );
        url.addParameter( DirectoryUtils.PARAMETER_ID_ACTION, nIdAction );

        if ( bShowActionResult )
        {
            url.addParameter( DirectoryUtils.PARAMETER_SHOW_ACTION_RESULT, DirectoryUtils.CONSTANT_TRUE );
        }

        if ( ( listIdsTestResource != null ) && ( listIdsTestResource.length > 0 ) )
        {
            for ( String strIdTestResource : listIdsTestResource )
            {
                url.addParameter( DirectoryUtils.PARAMETER_ID_DIRECTORY_RECORD, strIdTestResource );
            }
        }

        String strUploadAction = DirectoryAsynchronousUploadHandler.getHandler( ).getUploadAction( request );

        if ( StringUtils.isNotBlank( strUploadAction ) )
        {
            url.addParameter( strUploadAction, strUploadAction );
        }

        return url.getUrl( );
    }

    /**
     * return a referenceList of directories
     * 
     * @param list
     *            the directory list
     * @return ReferenceList the directory referenceList
     */
    private ReferenceList getDirectoryReferenceList( HashMap<Integer, Directory> list )
    {
        ReferenceList refList = new ReferenceList( );
        ReferenceItem item = new ReferenceItem( );
        item.setCode( "-1" );
        item.setName( " - " );
        refList.add( item );

        if ( list == null )
            return refList;

        Iterator it = list.entrySet( ).iterator( );
        while ( it.hasNext( ) )
        {
            Map.Entry dir = (Map.Entry) it.next( );
            item = new ReferenceItem( );
            item.setCode( String.valueOf( dir.getKey( ) ) );
            item.setName( ( (Directory) dir.getValue( ) ).getTitle( ) );
            refList.add( item );
        }
        return refList;
    }

}
