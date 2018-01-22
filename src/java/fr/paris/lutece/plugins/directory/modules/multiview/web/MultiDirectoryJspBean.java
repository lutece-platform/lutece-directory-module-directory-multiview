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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.mchange.v1.lang.BooleanUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.UserIdentityService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.DirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.IRecordFilterParameter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.RecordFilterAssignedUnitParameter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.RecordFilterDirectoryParameter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.RecordFilterNumberOfDaysParameter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.RecordFilterWorkflowStateParameter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.user.UserFactory;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;

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
    private static final String MESSAGE_UNIT_ATTRIBUTE_DEFAULT_NAME = "module.directory.multiview.manage_directory_multirecord.unit.attribute.defaultName";

    // Properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "module.directory.multiview.manage_directory_multirecord.pageTitle.label";
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";
    private static final String PROPERTY_DISPLAY_ENTRY_LABEL_LIST_CUSTOMIZED_COLUMN_1 = "directory-multiview.entry_name_list.customized_column_1";
    private static final String PROPERTY_DISPLAY_ENTRY_LABEL_LIST_CUSTOMIZED_COLUMN_2 = "directory-multiview.entry_name_list.customized_column_2";

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
    private static final String MARK_ASSIGNED_UNIT_LIST = "assigned_unit_list";
    private static final String MARK_CUSTOMIZED_COLUMN_ONE = "customized_column_1";
    private static final String MARK_CUSTOMIZED_COLUMN_TWO = "customized_column_2";

    // JSP URL
    private static final String JSP_MANAGE_MULTIVIEW = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecords.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_RECORD = "id_record";
    private static final String PARAMETER_ID_ACTION = "id_action";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_SORTED_ATTRIBUTE_NAME = "sorted_attribute_name";
    private static final String PARAMETER_SORTED_ATTRIBUTE_ASC = "asc_sort";
    private static final String PARAMETER_SEARCHED_TEXT = "searched_text";
    private static final String PARAMETER_BUTTON_REFRESH = "refresh";

    // Views
    private static final String VIEW_MULTIVIEW = "view_multiview";
    private static final String VIEW_RECORD_VISUALISATION = "view_record_visualisation";
    private static final String VIEW_TASKS_FORM = "view_tasksForm";

    // Actions
    private static final String ACTION_PROCESS_ACTION = "doProcessAction";
    private static final String ACTION_SAVE_TASK_FORM = "doSaveTaskForm";

    // Constants
    private static final String STATE_CODE_ATTRIBUTE = "id";
    private static final String STATE_NAME_ATTRIBUTE = "name";
    private static final String DIRECTORY_CODE_ATTRIBUTE = "idDirectory";
    private static final String DIRECTORY_NAME_ATTRIBUTE = "title";
    private static final String UNIT_CODE_ATTRIBUTE = "idUnit";
    private static final String UNIT_NAME_ATTRIBUTE = "label";
    private static final String PROPERTY_KEY_VALUES_SEPARATOR = ",";

    // Session fields
    private final IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );
    private final transient IDirectoryMultiviewService _directoryMultiviewService = SpringContextService.getBean( IDirectoryMultiviewService.BEAN_NAME );

    // Variables
    private HashMap<Integer, Directory> _directoryList;
    private Map<Integer, ReferenceList> _workflowStateByDirectoryList;
    private RecordAssignmentFilter _assignmentFilter;
    private transient List<Unit> _listAssignedUnit;
    private List<IEntry> _listEntryCustomizedColumnOne;
    private List<IEntry> _listEntryCustomizedColumnTwo;
    private transient List<IRecordFilterParameter> _listRecordFilterParameter;
    private LinkedHashMap<String, RecordAssignment> _recordAssignmentMap;
    private List<Map<String, Object>> _listResourceActions;
    private boolean _bIsInitialized;
    private String _strSearchText;

    /**
     * initialize the JspBean
     * 
     * @param request
     *            The HttpServletRequest
     */
    public void initialize( HttpServletRequest request )
    {
        if ( !_bIsInitialized )
        {
            _directoryList = new HashMap<>( );
            _workflowStateByDirectoryList = new HashMap<>( );
            _listEntryCustomizedColumnOne = new ArrayList<>( );
            _listEntryCustomizedColumnTwo = new ArrayList<>( );
            _recordAssignmentMap = new LinkedHashMap<>( );
            _listResourceActions = new ArrayList<>( );
            _listAssignedUnit = new ArrayList<>( );

            // init the assignment filter
            _assignmentFilter = new RecordAssignmentFilter( );
            _assignmentFilter.setUserUnitIdList( AssignmentService.findAllSubUnitsIds( AdminUserService.getAdminUser( request ) ) );
            _assignmentFilter.setActiveDirectory( true );
            _assignmentFilter.setActiveAssignmentRecordsOnly( true );
            _assignmentFilter.setLastActiveAssignmentRecordsOnly( true );

            // Set the default values on the filter
            populateRecordFilterItemList( );
            for ( IRecordFilterParameter recordFilterParameter : _listRecordFilterParameter )
            {
                recordFilterParameter.getRecordFilterItem( ).setItemDefaultValue( _assignmentFilter );
            }

            _strSearchText = StringUtils.EMPTY;
            DirectoryFilter filter = new DirectoryFilter( );
            filter.setIsDisabled( 1 );
            for ( Directory directory : DirectoryHome.getDirectoryList( filter, DirectoryUtils.getPlugin( ) ) )
            {
                if ( directory.getIdWorkflow( ) > 0 )
                {
                    int nIdDirectory = directory.getIdDirectory( );
                    _directoryList.put( nIdDirectory, directory );
                    Collection<State> listWorkflowState = WorkflowService.getInstance( ).getAllStateByWorkflow( directory.getIdWorkflow( ),
                            AdminUserService.getAdminUser( request ) );

                    ReferenceListFactory referenceListParameter = new ReferenceListFactory( listWorkflowState, STATE_CODE_ATTRIBUTE, STATE_NAME_ATTRIBUTE );
                    _workflowStateByDirectoryList.put( nIdDirectory, referenceListParameter.createReferenceList( ) );

                    // build set the list of entries to display in the multiview list
                    fillEntryListFromTitle( _listEntryCustomizedColumnOne, nIdDirectory, PROPERTY_DISPLAY_ENTRY_LABEL_LIST_CUSTOMIZED_COLUMN_1 );
                    fillEntryListFromTitle( _listEntryCustomizedColumnTwo, nIdDirectory, PROPERTY_DISPLAY_ENTRY_LABEL_LIST_CUSTOMIZED_COLUMN_2 );
                }

                // Populate the initialize list of all Assigned Unit
                populateAssignedUnitList( request );
            }
            reInitDirectoryMultiview( null );
            _bIsInitialized = true;
        }

    }

    /**
     * Fill the given list with all the IEntry of the specified directory which have the same title than the value of the property.
     * 
     * @param listIEntry
     *            The list of IEntry to fill up
     * @param nIdDirectory
     *            The identifier of the directory to retrieve the IEntry from
     * @param strPropertyEntryName
     *            The property key to retrieve the IEntry title value from
     */
    private void fillEntryListFromTitle( List<IEntry> listIEntry, int nIdDirectory, String strPropertyEntryName )
    {
        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setIdDirectory( nIdDirectory );
        entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
        entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

        String entryNameList = AppPropertiesService.getProperty( strPropertyEntryName );
        String [ ] entryNameTab = entryNameList.split( PROPERTY_KEY_VALUES_SEPARATOR );
        List<IEntry> entryList = EntryHome.getEntryList( entryFilter, getPlugin( ) );

        for ( String entryTitle : entryNameTab )
        {
            entryList.stream( ).filter( e -> entryTitle.equals( e.getTitle( ) ) ).forEachOrdered( listIEntry::add );
        }
    }

    /**
     * Populate the list of Assigned Unit for a directory
     * 
     * @param request
     *            The HttpServletRequest to retrieve the user from
     */
    private void populateAssignedUnitList( HttpServletRequest request )
    {
        // Retrieve the list of all RecordAssignment
        RecordAssignmentFilter recordAssignmentFilter = new RecordAssignmentFilter( );
        recordAssignmentFilter.setUserUnitIdList( AssignmentService.findAllSubUnitsIds( AdminUserService.getAdminUser( request ) ) );
        recordAssignmentFilter.setActiveDirectory( true );
        recordAssignmentFilter.setActiveAssignmentRecordsOnly( true );
        recordAssignmentFilter.setLastActiveAssignmentRecordsOnly( true );

        List<RecordAssignment> recordAssignmentList = AssignmentService.getRecordAssignmentFiltredList( recordAssignmentFilter );

        // Populate the map with the last AssignmentRecord for each Record
        Map<Integer, RecordAssignment> recordAssignmentMap = new LinkedHashMap<>( );
        for ( RecordAssignment assignedRecord : recordAssignmentList )
        {
            if ( !recordAssignmentMap.containsKey( assignedRecord.getIdRecord( ) )
                    || recordAssignmentMap.get( assignedRecord.getIdRecord( ) ).getAssignmentDate( ).before( assignedRecord.getAssignmentDate( ) ) )
            {
                // Keep only the last one
                recordAssignmentMap.put( assignedRecord.getIdRecord( ), assignedRecord );

            }
        }

        // Retrieve the Resource Action associated to the Record of each RecordAssignment
        for ( RecordAssignment recordAssignment : recordAssignmentMap.values( ) )
        {
            Record record = RecordHome.findByPrimaryKey( recordAssignment.getIdRecord( ), getPlugin( ) );
            if ( record != null && recordAssignmentMap.get( record.getIdRecord( ) ) != null )
            {
                RecordAssignment recordAssignmentFromRecord = recordAssignmentMap.get( record.getIdRecord( ) );
                _listAssignedUnit.add( recordAssignmentFromRecord.getAssignedUnit( ) );
            }
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
    @View( value = VIEW_MULTIVIEW, defaultView = true )
    public String getManageDirectoryRecord( HttpServletRequest request ) throws AccessDeniedException
    {
        // Clear the resource actions list
        _listResourceActions.clear( );

        // force refresh ?
        if ( request.getParameter( PARAMETER_BUTTON_REFRESH ) != null )
        {
            _bIsInitialized = false;
            initialize( request );
        }

        // test if we are paginating or sorting OR if there is a new search request
        if ( request.getParameter( PARAMETER_PAGE_INDEX ) == null )
        {
            if ( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) != null )
            {
                // new SORT
                _assignmentFilter.setOrderBy( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) );
                _assignmentFilter.setAsc( BooleanUtils.parseBoolean( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_ASC ) ) );

                // get the new records assigments
                getRecordAssigments( );
            }
            else
            {
                // new SEARCH
                RecordAssignmentFilter newFilter = _directoryMultiviewService.getRecordAssignmentFilter( request, _listRecordFilterParameter );

                // if filter changed, reinit several list for multiview
                reInitDirectoryMultiview( newFilter );

                // get the new records assigments
                getRecordAssigments( );

            }
        }

        // Perform simple full text search
        _strSearchText = request.getParameter( PARAMETER_SEARCHED_TEXT );
        Map<String, RecordAssignment> mapRecordAssignmentAfterSearch = DirectoryMultiviewSearchService.filterBySearchedText( _recordAssignmentMap,
                _directoryList.values( ), request, getPlugin( ), _strSearchText );

        // Paginate
        Map<String, Object> model = getPaginatedListModel( request, PARAMETER_PAGE_INDEX, new ArrayList<>( mapRecordAssignmentAfterSearch.keySet( ).stream( )
                .map( key -> Integer.parseInt( key ) ).collect( Collectors.toList( ) ) ), JSP_MANAGE_MULTIVIEW );

        // get only records for page items.
        List<Record> lRecord = _recordService.loadListByListId( _paginator.getPageItems( ), getPlugin( ) );

        // Create the global list of IEntry
        List<IEntry> listIEntryToSearch = new ArrayList<>( );
        listIEntryToSearch.addAll( _listEntryCustomizedColumnOne );
        listIEntryToSearch.addAll( _listEntryCustomizedColumnTwo );

        for ( Record record : lRecord )
        {
            // data complement (not done by directory plugin)
            record.getDirectory( ).setIdWorkflow( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getIdWorkflow( ) );
            record.getDirectory( ).setTitle( _directoryList.get( record.getDirectory( ).getIdDirectory( ) ).getTitle( ) );

            // add resourceActions
            _listResourceActions.add( DirectoryService.getInstance( ).getResourceAction( record, record.getDirectory( ), listIEntryToSearch, getUser( ), null,
                    null, false, getPlugin( ) ) );
        }

        // Populate record precisions in resource action (called "demandeur")
        _directoryMultiviewService.populateRecord( _listResourceActions, _listEntryCustomizedColumnOne, MARK_CUSTOMIZED_COLUMN_ONE );

        // Populate the record
        _directoryMultiviewService.populateRecord( _listResourceActions, _listEntryCustomizedColumnTwo, MARK_CUSTOMIZED_COLUMN_TWO );

        // Create the ReferenceList of all Assigned Unit
        ReferenceListFactory referenceListParameterUnitAssigned = new ReferenceListFactory( _listAssignedUnit, UNIT_CODE_ATTRIBUTE, UNIT_NAME_ATTRIBUTE );
        referenceListParameterUnitAssigned.setDefaultName( I18nService.getLocalizedString( MESSAGE_UNIT_ATTRIBUTE_DEFAULT_NAME, request.getLocale( ) ) );

        model.put( MARK_PAGINATOR, _paginator );
        model.put( MARK_NUMBER_RECORD, mapRecordAssignmentAfterSearch.keySet( ).size( ) );
        model.put( MARK_ENTRY_LIST_SEARCH_RESULT, _listEntryCustomizedColumnOne );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_RESOURCE_ACTIONS_LIST, _listResourceActions );
        model.put( MARK_RECORD_ASSIGNMENT_FILTER, _assignmentFilter );
        model.put( MARK_RECORD_ASSIGNMENT_MAP, mapRecordAssignmentAfterSearch );
        model.put( MARK_ASSIGNED_UNIT_LIST, referenceListParameterUnitAssigned.createReferenceList( ) );

        ReferenceListFactory referenceListParameter = new ReferenceListFactory( _directoryList.values( ), DIRECTORY_CODE_ATTRIBUTE, DIRECTORY_NAME_ATTRIBUTE );
        model.put( MARK_DIRECTORY_LIST, referenceListParameter.createReferenceList( ) );

        model.put( MARK_SEARCH_TEXT, _strSearchText );
        if ( _assignmentFilter.getDirectoryId( ) > 0 )
        {
            model.put( MARK_SEARCH_STATE_WORKFLOW, _workflowStateByDirectoryList.get( _assignmentFilter.getDirectoryId( ) ) );
        }

        _directoryMultiviewService.populateDefaultFilterMarkers( _assignmentFilter, _listRecordFilterParameter, model );
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
     * reinit multiview context
     * 
     * @param newFilter
     *            The new filter on which to base
     */
    private void reInitDirectoryMultiview( RecordAssignmentFilter newFilter )
    {
        _recordAssignmentMap = new LinkedHashMap<>( );
        _listResourceActions = new ArrayList<>( );

        if ( newFilter != null )
        {
            // Set the values on the filter
            for ( IRecordFilterParameter recordFilterParameter : _listRecordFilterParameter )
            {
                IRecordFilterItem recordFilterItem = recordFilterParameter.getRecordFilterItem( );
                recordFilterItem.setItemValue( _assignmentFilter, recordFilterItem.getItemValue( newFilter ) );
            }

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
            if ( !_recordAssignmentMap.containsKey( String.valueOf( assignedRecord.getIdRecord( ) ) )
                    || _recordAssignmentMap.get( String.valueOf( assignedRecord.getIdRecord( ) ) ).getAssignmentDate( )
                            .before( assignedRecord.getAssignmentDate( ) ) )
            {
                // keep only the last one
                _recordAssignmentMap.put( String.valueOf( assignedRecord.getIdRecord( ) ), assignedRecord );

            }
        }
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

        if ( WorkflowService.getInstance( ).canProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, -1, request, false ) )
        {

            try
            {
                String strError = WorkflowService.getInstance( )
                        .doSaveTasksForm( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, -1, request, getLocale( ) );
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

        return redirectView( request, VIEW_MULTIVIEW );
    }

    /**
     * Populate the list of all Record Filter Item on which we can filter the records
     */
    private void populateRecordFilterItemList( )
    {
        _listRecordFilterParameter = new ArrayList<>( );

        _listRecordFilterParameter.add( new RecordFilterDirectoryParameter( ) );
        _listRecordFilterParameter.add( new RecordFilterNumberOfDaysParameter( ) );
        _listRecordFilterParameter.add( new RecordFilterWorkflowStateParameter( ) );
        _listRecordFilterParameter.add( new RecordFilterAssignedUnitParameter( ) );
    }
}
