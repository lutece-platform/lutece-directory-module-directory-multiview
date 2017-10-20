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
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import fr.paris.lutece.plugins.directory.modules.multiview.web.user.UserFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.service.UserIdentityService;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
import fr.paris.lutece.plugins.directory.web.action.IDirectoryAction;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove)
 */
@Controller( controllerJsp = "ManageMultiDirectoryRecords.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_MULTIVIEW" )
public class MultiDirectoryJspBean extends MVCAdminJspBean
{
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
    private static final String MESSAGE_CONFIRM_CHANGE_STATES_RECORD = "directory.message.confirm_change_states_record";
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";
    private static final String MESSAGE_MULTIVIEW_TITLE = "module.directory.multiview.pageTitle";

    // properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "directory.manage_directory_record.page_title";
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
    private static final String MARK_DIRECTORY_ACTIONS = "directory_actions";
    private static final String MARK_SHOW_DATE_MODIFICATION_RECORD = "show_date_modification_record";
    private static final String MARK_SHOW_DATE_MODIFICATION_RESULT = "show_date_modification_result";
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
    private static final String MARK_USER_ATTRIBUTES = "user_attributes";
    private static final String MARK_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String MARK_ID_ACTION = "id_action";
    private static final String MARK_TASK_FORM = "tasks_form";
    
    // JSP URL
    private static final String JSP_MANAGE_DIRECTORY = "jsp/admin/plugins/directory/modules/multiview/ManageDirectory.jsp";
    private static final String JSP_DO_CHANGE_STATES_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoChangeStatesRecord.jsp";
    private static final String JSP_DO_VISUALISATION_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoVisualisationRecord.jsp";
    private static final String JSP_MANAGE_MULTI_DIRECTORY_RECORD = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = DirectoryUtils.PARAMETER_ID_DIRECTORY;
    private static final String PARAMETER_ID_RECORD = "id_record";
    private static final String PARAMETER_ID_ACTION = "id_action";
    
    private static final String PARAMETER_ID_STATE_WORKFLOW = "search_state_workflow";
    private static final String PARAMETER_ID_FILTER_PERIOD = "search_open_since";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_SESSION = DirectoryUtils.PARAMETER_SESSION;
    private static final String PARAMETER_RESET_SEARCH = "resetsearch";
    
    //Views 
    private static final String VIEW_MULTIVIEW = "view_multiview";
    private static final String VIEW_RECORD_VISUALISATION = "view_record_visualisation";
    private static final String VIEW_TASKS_FORM = "view_tasksForm";
    
    //Actions
    private static final String ACTION_PROCESS_ACTION = "doProcessAction";
    private static final String ACTION_SAVE_TASK_FORM = "doSaveTaskForm";

    // constants
    private static final String CONSTANT_IDENTIFYING_ENTRY_TITLE = "GUID";

    // session fields
    private DirectoryAdminSearchFields _searchFields = new DirectoryAdminSearchFields( );
    private IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );

    private List<DirectoryViewFilter> _directoryViewList = new ArrayList<>( );
    private HashMap<Integer, Directory> _directoryList = new HashMap<>( );
    private Map<Integer,Collection<State> > _workflowStateByDirectoryList = new HashMap<>( );
    
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
    @View( value= VIEW_MULTIVIEW, defaultView=true )
    public String getManageDirectoryRecord( HttpServletRequest request )
            throws AccessDeniedException
    {
        // reinit search
        reInitDirectoryRecordFilter( );

        //Parameters for filtering
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdWorkflowState = request.getParameter( PARAMETER_ID_STATE_WORKFLOW );
        String strIdPeriodParameter = request.getParameter( PARAMETER_ID_FILTER_PERIOD );

        int nIdWorkflowState = DirectoryUtils.convertStringToInt( strIdWorkflowState );
        int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
        int nIdPeriodParameter = DirectoryUtils.convertStringToInt( strIdPeriodParameter );

        boolean bWorkflowServiceEnable = WorkflowService.getInstance( ).isAvailable( );
        AdminUser adminUser = getUser( );
        
         _directoryViewList = DirectoryViewFilterHome.getDirectoryFiltersList( );

        DirectoryFilter filter =  new DirectoryFilter( );
        for ( Directory directory : DirectoryHome.getDirectoryList( filter, DirectoryUtils.getPlugin( ) ) )
        {
            if ( directory.getIdWorkflow( ) > 0 )
            {   
                _directoryList.put( directory.getIdDirectory( ), directory);
                Collection<State> listWorkflowState =  WorkflowService.getInstance().getAllStateByWorkflow( directory.getIdWorkflow( ), adminUser );
                _workflowStateByDirectoryList.put( directory.getIdDirectory( ), listWorkflowState );
            }
        }

        if ( ( request.getParameter( PARAMETER_SESSION ) == null ) || Boolean.parseBoolean( request.getParameter( PARAMETER_RESET_SEARCH ) ) )
        {
            reInitDirectoryRecordFilter( );
        }

        _searchFields.setCurrentPageIndexDirectoryRecord( Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX,
                _searchFields.getCurrentPageIndexDirectoryRecord( ) ) );
        _searchFields.setItemsPerPageDirectoryRecord( Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE,
                _searchFields.getItemsPerPageDirectoryRecord( ), _searchFields.getDefaultItemsPerPage( ) ) );

        List<Integer> listResultRecordId = new ArrayList<>( );
        List<IEntry> listEntryResultSearch = new ArrayList<>( );

        for ( DirectoryViewFilter dvf : _directoryViewList )
        {

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

                // keep only the entry of the filter and the identifyer (in place of entry.isShownInResultList( ) )
                if ( entryTmp.getIdEntry( ) == dvf.getIdEntryTitle( ) || CONSTANT_IDENTIFYING_ENTRY_TITLE.equals( entryTmp.getTitle( ) ) )
                {
                    listEntryResultSearch.add( entryTmp );
                }
            }

        }

        // get the records filtred list
        List<RecordAssignment> recordAssignmentList = new ArrayList<>( );

        RecordAssignmentFilter AssignmentFilter = new RecordAssignmentFilter( );
        
        AssignmentFilter.setUserUnitIdList( AssignmentService.findAllSubUnitsIds( adminUser ) );
        AssignmentFilter.setActiveRecordsOnly( true );

        // filter by Directory (and state)
        if ( nIdDirectory > 0 )
        {
            AssignmentFilter.setDirectoryId( nIdDirectory );
            if ( nIdWorkflowState > 0 )
                AssignmentFilter.setStateId( nIdWorkflowState );
        }

        // filter by period
        if ( nIdPeriodParameter > 0 )
            AssignmentFilter.setNumberOfDays( nIdPeriodParameter );


        recordAssignmentList.addAll( AssignmentService.getRecordAssignmentFiltredList( AssignmentFilter ) );


        // get the records Id from the assigned records & prepare an hashmap for the model
        HashMap<String, RecordAssignment> recordAssignmentMap = new HashMap<>( );
        for ( RecordAssignment assignedRecord : recordAssignmentList )
        {
            listResultRecordId.add( assignedRecord.getIdRecord( ) ) ;
            if ( ! recordAssignmentMap.containsKey( String.valueOf(assignedRecord.getIdRecord( ) ) ) 
                    || recordAssignmentMap.get( String.valueOf(assignedRecord.getIdRecord( ) ) ).getAssignmentDate( )
                    .before( assignedRecord.getAssignmentDate( ) ) ) {
                    
                    // keep only the last one
                    recordAssignmentMap.put( String.valueOf( assignedRecord.getIdRecord( ) ), assignedRecord) ;
                
            } 
        }

        // Store the list of id records in session
        _searchFields.setListIdsResultRecord( recordAssignmentMap.keySet( )
                .stream().map( key -> Integer.parseInt( key ) )
                .collect( Collectors.toList( )) );

        // HACK : We copy the list so workflow does not clear the paginator list.
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( new ArrayList<>( recordAssignmentMap.keySet( )
                .stream().map( key -> Integer.parseInt( key ) )
                .collect( Collectors.toList( ) ) ),
                _searchFields.getItemsPerPageDirectoryRecord( ), getJspManageMultiDirectoryRecord( request ), PARAMETER_PAGE_INDEX,
                _searchFields.getCurrentPageIndexDirectoryRecord( ), getLocale( ) );

        // get only record for page items.
        List<Record> lRecord = _recordService.loadListByListId( paginator.getPageItems( ), getPlugin( ) );

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( );
        RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
        recordFieldFilter.setIsEntryShownInResultList( RecordFieldFilter.FILTER_TRUE );

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
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( _searchFields.getItemsPerPageDirectoryRecord( ) ) );
        model.put( MARK_NUMBER_RECORD, listResultRecordId.size( ) );
        model.put( MARK_ENTRY_LIST_SEARCH_RESULT, listEntryResultSearch );
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


        return getPage( MESSAGE_MULTIVIEW_TITLE,( TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD ), model );
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

        //Get the guid
        String strGuid = StringUtils.EMPTY;
        for ( IEntry entry : listEntry )
        {
            //TODO Property for GUID
            if ( entry.getTitle( ).equals( CONSTANT_IDENTIFYING_ENTRY_TITLE ) )
            {
                List<RecordField> listRecordFields = DirectoryUtils.getListRecordField( entry, nIdRecord,  getPlugin( ) );
                strGuid = listRecordFields.get( 0 ).toString( );
                break;
            }
                
        }

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

        //Add the user attributes
        if ( !strGuid.isEmpty( ) )
        {
            model.put( MARK_USER_ATTRIBUTES, UserIdentityService.getUserAttributes( strGuid ) );
        }


       return getPage( MESSAGE_MULTIVIEW_TITLE, TEMPLATE_VIEW_DIRECTORY_RECORD, model );
    }
    
    
    @Action( value = ACTION_PROCESS_ACTION )
    public String doProcessWorkflowAction( HttpServletRequest request )
    {
        //Get parameters from request
        int nIdRecord = Integer.parseInt( request.getParameter( PARAMETER_ID_RECORD ) );
        int nIdAction = Integer.parseInt( request.getParameter( PARAMETER_ID_ACTION ) );
        int nIdDirectory = Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) );
        
        IRecordService recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );
       
        
        if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
        {
            return redirect( request, VIEW_TASKS_FORM, PARAMETER_ID_DIRECTORY_RECORD, nIdRecord, PARAMETER_ID_ACTION, nIdAction );
        }
        
        boolean bHasSucceed = false;
        
        try
        {
            
            WorkflowService.getInstance( ).doProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request,
                    request.getLocale( ), false );
            bHasSucceed = true;
        }
        catch( Exception e )
        {
            AppLogService.error( "Error processing action for id record '" + nIdRecord + "' - cause : " + e.getMessage( ), e );
        }

        if ( bHasSucceed )
        {
            // Update record modification date (for directory plugin)
            Record record = recordService.findByPrimaryKey( nIdRecord, getPlugin( ) );
            recordService.update( record, getPlugin( ) );
        }
        
        return redirectView( request, VIEW_MULTIVIEW );
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
        _searchFields.setItemNavigatorViewRecords( null );
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
        Integer nIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) ) : null;
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

        Integer nIdRecord = request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ) ) : null;
        Integer nIdAction = request.getParameter( PARAMETER_ID_ACTION ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ID_ACTION ) ) : null;

        if ( WorkflowService.getInstance( ).canProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, -1, request, false ) )
        {

	        try
	        {
	            String strError = WorkflowService.getInstance( ).doSaveTasksForm( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, -1,
	                    request, getLocale() );
	            if(strError != null)
	            {
	            	addError( strError );
	            	return redirect(request, VIEW_TASKS_FORM, PARAMETER_ID_DIRECTORY_RECORD, nIdRecord, PARAMETER_ID_ACTION, nIdAction);
	            }
	          
	        }
	        catch( Exception e )
	        {
	             AppLogService.error( "Error processing action for record '" + nIdRecord , e );
	         
	        }
        }
        else
        {
            return redirectView( request, VIEW_TASKS_FORM );
        }

        return redirectView( request, VIEW_MULTIVIEW );
    }
}
