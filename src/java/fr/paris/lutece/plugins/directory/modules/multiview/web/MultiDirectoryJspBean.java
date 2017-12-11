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

import com.mchange.v1.lang.BooleanUtils;
import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Entry;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.web.user.UserFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.service.UserIdentityService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.DirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewUtils;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove)
 */
@Controller( controllerJsp = "ManageMultiDirectoryRecords.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_MULTIVIEW" )
public class MultiDirectoryJspBean extends AbstractJspBean
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
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";
    private static final String MESSAGE_MULTIVIEW_TITLE = "module.directory.multiview.pageTitle";

    // properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "module.directory.multiview.manage_directory_multirecord.pageTitle.label";
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";
    private static final String PROPERTY_DISPLAY_ENTRY_LABEL_LIST = "directory-multiview.entry_name_list";
    
    // public properties
    public static final String PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY = "DIRECTORY_MULTIVIEW";

    // Markers
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_DIRECTORY = "directory";
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_ENTRY_LIST_SEARCH_RESULT = "entry_list_search_result";
    private static final String MARK_NUMBER_RECORD = "number_record";
    private static final String MARK_DIRECTORY_LIST = "directory_list";
    private static final String MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD = "map_id_entry_list_record_field";
    private static final String MARK_ID_ENTRY_TYPE_IMAGE = "id_entry_type_image";
    private static final String MARK_ID_ENTRY_TYPE_MYLUTECE_USER = "id_entry_type_mylutece_user";
    private static final String MARK_ID_ENTRY_TYPE_GEOLOCATION = "id_entry_type_geolocation";
    private static final String MARK_SHOW_DATE_CREATION_RECORD = "show_date_creation_record";
    private static final String MARK_SHOW_DATE_MODIFICATION_RECORD = "show_date_modification_record";
    private static final String MARK_RECORD = "record";
    private static final String MARK_RESOURCE_ACTIONS_LIST = "resource_actions_list";
    private static final String MARK_RECORD_ASSIGNMENT_MAP = "recordAssignmentMap";
    private static final String MARK_RESOURCE_ACTIONS = "resource_actions";
    private static final String MARK_RESOURCE_HISTORY = "resource_history";
    private static final String MARK_SEARCH_STATE_WORKFLOW = "search_state_workflow";
    private static final String MARK_HISTORY_WORKFLOW_ENABLED = "history_workflow";
    private static final String MARK_PERMISSION_VISUALISATION_MYLUTECE_USER = "permission_visualisation_mylutece_user";
    private static final String MARK_USER_FACTORY = "user_factory";
    private static final String MARK_USER_ATTRIBUTES = "user_attributes";
    private static final String MARK_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String MARK_ID_ACTION = "id_action";
    private static final String MARK_TASK_FORM = "tasks_form";
    private static final String MARK_RECORD_ASSIGNMENT_FILTER = "record_assignment_filter";
    private static final String MARK_SEARCH_TEXT = "search_text";
    
    // JSP URL
    private static final String JSP_MANAGE_MULTIVIEW = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_RECORD = "id_record";
    private static final String PARAMETER_ID_ACTION = "id_action";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    public static final String PARAMETER_SORTED_ATTRIBUTE_NAME = "sorted_attribute_name";
    public static final String PARAMETER_SORTED_ATTRIBUTE_ASC = "asc_sort";
    public static final String PARAMETER_SEARCHED_TEXT = "searched_text";
    private static final String PARAMETER_BUTTON_REFRESH = "refresh" ;
    //Views 
    private static final String VIEW_MULTIVIEW = "view_multiview";
    private static final String VIEW_RECORD_VISUALISATION = "view_record_visualisation";
    private static final String VIEW_TASKS_FORM = "view_tasksForm";
    
    //Actions
    private static final String ACTION_PROCESS_ACTION = "doProcessAction";
    private static final String ACTION_SAVE_TASK_FORM = "doSaveTaskForm";

    // session fields
    private IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );

    private HashMap<Integer, Directory> _directoryList;
    private Map<Integer,ReferenceList > _workflowStateByDirectoryList;
    private RecordAssignmentFilter _assignmentFilter;
    private List<IEntry>  _listEntryResultSearch;
    private LinkedHashMap<String, RecordAssignment> _recordAssignmentMap;
    private List<Map<String, Object> > _listResourceActions;
    private boolean _bIsInitialized;
    private String _strSearchText;
    
    /**
     * initialize the JspBean 
     * @param request 
     */
    public void initialize( HttpServletRequest request )
    {
        if ( !_bIsInitialized )
        {
            _directoryList = new HashMap<>( );
            _workflowStateByDirectoryList = new HashMap<>( );
            _listEntryResultSearch = new ArrayList<>( );
            _recordAssignmentMap = new LinkedHashMap<>( );
            _listResourceActions = new ArrayList<>( );

            //init the assignment filter
            _assignmentFilter = new RecordAssignmentFilter( );
            _assignmentFilter.setUserUnitIdList( AssignmentService.findAllSubUnitsIds( AdminUserService.getAdminUser( request ) ) );
            _assignmentFilter.setActiveDirectory( true );
            _assignmentFilter.setActiveAssignmentRecordsOnly( true );
            _assignmentFilter.setLastActiveAssignmentRecordsOnly( true );
            _assignmentFilter.setDirectoryId( -2 );
            _assignmentFilter.setStateId(-2 );
            _assignmentFilter.setNumberOfDays( -2 );
            _strSearchText = StringUtils.EMPTY;
            DirectoryFilter filter =  new DirectoryFilter( );
            filter.setIsDisabled( 1 );
            for ( Directory directory : DirectoryHome.getDirectoryList( filter, DirectoryUtils.getPlugin( ) ) )
            {
                if ( directory.getIdWorkflow( ) > 0 )
                {   
                    _directoryList.put( directory.getIdDirectory( ), directory);
                    Collection listWorkflowState =  WorkflowService.getInstance().getAllStateByWorkflow( directory.getIdWorkflow( ), AdminUserService.getAdminUser( request ));
                    ReferenceList workflowStateReferenceList = DirectoryMultiviewUtils.convert( listWorkflowState, "id", "name", true );
                    _workflowStateByDirectoryList.put( directory.getIdDirectory( ), workflowStateReferenceList );

                    // build set the list of entries to display in the multiview list
                    EntryFilter entryFilter = new EntryFilter( );
                    entryFilter.setIdDirectory( directory.getIdDirectory( ) );
                    entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
                    entryFilter.setIsComment( EntryFilter.FILTER_FALSE );
                    //entryFilter.setIsShownInResultList( 1 );

                    String entryNameList = AppPropertiesService.getProperty( PROPERTY_DISPLAY_ENTRY_LABEL_LIST );
                    String[] entryNameTab = entryNameList.split(",");
                    List<IEntry> entryList = EntryHome.getEntryList( entryFilter, getPlugin( ) );
                    
                    for (String entryTitle : entryNameTab ) {
                        entryList.stream()
                            .filter( e -> entryTitle.equals( e.getTitle( ) ))
                            .forEachOrdered(_listEntryResultSearch::add);                            
                    }
                            
                    
                }
            }
            reInitDirectoryMultiview( null );
            _bIsInitialized = true;
        }
        
    }
    
    /**
     * Return management of directory record ( list of directory record ).
     * 
     * @param request
     *            The Http request
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return IPluginActionResult
     */
    @View( value= VIEW_MULTIVIEW, defaultView=true )
    public String getManageDirectoryRecord( HttpServletRequest request )
            throws AccessDeniedException
    {
        // Clear the resource actions list
        _listResourceActions.clear( );
        
        // force refresh ?
        if (request.getParameter( PARAMETER_BUTTON_REFRESH ) != null ) 
        {
            _bIsInitialized = false;
            initialize( request );
        }
        
        // test if we are paginating or sorting OR if there is a new search request
        if (request.getParameter( PARAMETER_PAGE_INDEX ) == null ) 
        {
            if (request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) != null)
            {
                // new SORT
                _assignmentFilter.setOrderBy( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) );
                _assignmentFilter.setAsc( BooleanUtils.parseBoolean( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_ASC ) ) );
                
                // get the new records assigments
                getRecordAssigments( );
            } 
            else
            {
                // new SEARCH ?
                RecordAssignmentFilter newFilter = DirectoryMultiviewService.getRecordAssignmentFilter( request );

                // test if filter has changed OR new sort criteria
                if (DirectoryMultiviewService.testIfFilterHasChanged( _assignmentFilter , newFilter) ) 
                {
                    // if filter changed, reinit several list for multiview
                    reInitDirectoryMultiview( newFilter );

                    // get the new records assigments
                    getRecordAssigments( );
                }
            }
        }             
       
        // Perform simple full text search 
        _strSearchText = request.getParameter( PARAMETER_SEARCHED_TEXT );
        LinkedHashMap<String,RecordAssignment> mapRecordAssignmentAfterSearch =
                DirectoryMultiviewSearchService.filterBySearchedText( _recordAssignmentMap, _directoryList.values( ) ,request, getPlugin( ), _strSearchText );
        
        // Paginate
        Map<String, Object> model = getPaginatedListModel( request, PARAMETER_PAGE_INDEX,  
                new ArrayList<>( mapRecordAssignmentAfterSearch.keySet( )
                                                                .stream( )
                                                                .map( key -> Integer.parseInt( key ) )
                                                                .collect( Collectors.toList( ) ) ), 
                JSP_MANAGE_MULTIVIEW );

        // get only records for page items.
        List<Record> lRecord = _recordService.loadListByListId( _paginator.getPageItems( ), getPlugin( ) );

        for ( Record record : lRecord )
        {
                // data complement (not done by directory plugin)
                record.getDirectory( ).setIdWorkflow( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getIdWorkflow( ) );
                record.getDirectory( ).setTitle( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getTitle( ) );

                // add resourceActions
                _listResourceActions.add( DirectoryService.getInstance( ).getResourceAction( record, record.getDirectory( ), _listEntryResultSearch, getUser( ),
                        null, null, false, getPlugin( ) ) );
        }
        
        //Populate record precisions in resource action (called "demandeur")
        DirectoryMultiviewService.populateRecordPrecisions( _listResourceActions, _listEntryResultSearch, request.getLocale( ) );

        model.put( MARK_PAGINATOR, _paginator );
        model.put( MARK_NUMBER_RECORD, mapRecordAssignmentAfterSearch.keySet( ).size( ) );
        model.put( MARK_ENTRY_LIST_SEARCH_RESULT, _listEntryResultSearch );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_RESOURCE_ACTIONS_LIST, _listResourceActions );
        model.put( MARK_RECORD_ASSIGNMENT_FILTER, _assignmentFilter );
        model.put( MARK_RECORD_ASSIGNMENT_MAP, mapRecordAssignmentAfterSearch );
        model.put( MARK_DIRECTORY_LIST, DirectoryMultiviewUtils.convert( (Collection)_directoryList.values( ), "idDirectory","title", true ) );
        model.put( MARK_SEARCH_TEXT, _strSearchText );
        if ( _assignmentFilter.getDirectoryId( ) > 0 )
        {
            model.put( MARK_SEARCH_STATE_WORKFLOW, _workflowStateByDirectoryList.get( _assignmentFilter.getDirectoryId( ) ) );
        }
        
        DirectoryMultiviewService.populateDefaultFilterMarkers ( _assignmentFilter, model );
        return getPage( PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE, TEMPLATE_MANAGE_MULTI_DIRECTORY_RECORD, model );
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
                        DirectoryResourceIdService.PERMISSION_VISUALISATION_RECORD, getUser( ) ) )
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

        boolean bHistoryEnabled = WorkflowService.getInstance( ).isAvailable( ) && ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL );

        // Get asynchronous file names
        boolean bGetFileName = true;

        //Get the guid
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

        //Add the user attributes
        if ( strGuid != null)
        {
            model.put( MARK_USER_ATTRIBUTES, UserIdentityService.getUserAttributes( strGuid ) );
        }

       return getPage( MESSAGE_MULTIVIEW_TITLE, TEMPLATE_VIEW_DIRECTORY_RECORD, model );
    }
    
    /**
     * reinit multiview context
     * 
     * @param newFilter 
     */
    private void reInitDirectoryMultiview( RecordAssignmentFilter newFilter )
    {
        _recordAssignmentMap = new LinkedHashMap<>( );
        _listResourceActions = new ArrayList<>( );
        
        if (newFilter != null) 
        {
            _assignmentFilter.setDirectoryId( newFilter.getDirectoryId( ) );
            _assignmentFilter.setStateId( newFilter.getStateId( ));
            _assignmentFilter.setNumberOfDays( newFilter.getNumberOfDays( ));
            _assignmentFilter.setOrderBy( newFilter.getOrderBy( ) );
            _assignmentFilter.setAsc( newFilter.isAsc( ) );
        }
        else 
        {        
            _assignmentFilter.setOrderBy( StringUtils.EMPTY );
            _assignmentFilter.setAsc( true );
        }
        
        resetCurrentPaginatorPageIndex( );        
    }
    
    /**
     * get the recordAssignment filtred list
     */
    private void getRecordAssigments( )
    {
        // get the records filtred list
        List<RecordAssignment> recordAssignmentList = AssignmentService.getRecordAssignmentFiltredList( _assignmentFilter );

        _recordAssignmentMap.clear( );
        
        // get the records Id from the assigned records & prepare an hashmap for the model
        for ( RecordAssignment assignedRecord : recordAssignmentList )
        {
            if ( ! _recordAssignmentMap.containsKey( String.valueOf(assignedRecord.getIdRecord( ) ) ) 
                    || _recordAssignmentMap.get( String.valueOf(assignedRecord.getIdRecord( ) ) ).getAssignmentDate( )
                        .before( assignedRecord.getAssignmentDate( ) ) ) {
                    // keep only the last one
                    _recordAssignmentMap.put( String.valueOf( assignedRecord.getIdRecord( ) ), assignedRecord) ;
                
            } 
        }
    }
    
    /**
     * Process workflow action on record
     * @param request the HttpServletRequest
     * @return the task form if exists, or the manage record view otherwise.
     */
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
