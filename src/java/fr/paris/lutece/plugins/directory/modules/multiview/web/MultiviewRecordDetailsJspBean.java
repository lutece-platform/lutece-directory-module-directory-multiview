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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewPlugin;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewAuthorizationService;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.view.IRecordViewModelProcessor;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.view.RecordViewModelProcessorFactory;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * Controller page for the details of the record and the managing of the actions of the workflow
 */
@Controller( controllerJsp = "ManageDirectoryRecordDetails.jsp", controllerPath = "jsp/admin/plugins/directory/modules/multiview/", right = "DIRECTORY_MULTIVIEW" )
public class MultiviewRecordDetailsJspBean extends AbstractJspBean
{
    // Generated serial UID
    private static final long serialVersionUID = -9068496856640551669L;

    // Templates
    private static final String TEMPLATE_VIEW_DIRECTORY_RECORD = "admin/plugins/directory/modules/multiview/view_directory_record.html";
    private static final String TEMPLATE_RECORD_HISTORY = "admin/plugins/directory/modules/multiview/record_history.html";
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/directory/modules/multiview/task_form_workflow.html";

    // Views
    private static final String VIEW_RECORD_DETAILS = "view_record_details";
    private static final String VIEW_TASKS_FORM = "view_tasksForm";

    // Actions
    private static final String ACTION_PROCESS_ACTION = "doProcessAction";
    private static final String ACTION_SAVE_TASK_FORM = "doSaveTaskForm";
    private static final String ACTION_CANCEL_TASK_FORM = "doCancelTaskForm";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_ID_ACTION = "id_action";

    // Marks
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_DIRECTORY = "directory";
    private static final String MARK_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String MARK_ID_ACTION = "id_action";
    private static final String MARK_TASK_FORM = "tasks_form";
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

    // Properties
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";

    // Messages
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";
    private static final String MESSAGE_MULTIVIEW_TITLE = "module.directory.multiview.pageTitle";

    // Variables
    private final transient IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );
    private final transient IDirectoryMultiviewAuthorizationService _directoryMultiviewAuthorizationService = SpringContextService
            .getBean( IDirectoryMultiviewAuthorizationService.BEAN_NAME );

    /**
     * Return the page with the details of a record
     * 
     * @param request
     *            The request used the retrieve the values of the selected parameters
     * @return the page with the details of the record
     * @throws AccessDeniedException
     *             if the user is not authorize to access the details of the record
     */
    @View( value = VIEW_RECORD_DETAILS, defaultView = true )
    public String getRecordDetails( HttpServletRequest request ) throws AccessDeniedException
    {
        AdminUser adminUser = getUser( );
        Plugin pluginDirectoryMultiview = DirectoryMultiviewPlugin.getPlugin( );

        int nIdRecord = NumberUtils.toInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ), NumberUtils.INTEGER_MINUS_ONE );
        Record record = _recordService.findByPrimaryKey( nIdRecord, pluginDirectoryMultiview );

        boolean bRBACAuthorization = RBACService.isAuthorized( Directory.RESOURCE_TYPE, Integer.toString( nIdRecord ),
                DirectoryResourceIdService.PERMISSION_VISUALISATION_RECORD, adminUser );
        boolean bAuthorizedRecord = _directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( request, nIdRecord );

        if ( record == null || record.getDirectory( ) == null || !bRBACAuthorization || !bAuthorizedRecord )
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        // Build the base model for the page of the details of a Record
        Map<String, Object> model = buildRecordDetailsModel( request, record );

        // Build the model of all ModelProcessors
        RecordViewModelProcessorFactory recordViewModelProcessorFactory = new RecordViewModelProcessorFactory( );
        List<IRecordViewModelProcessor> listRecordViewModelProcesor = recordViewModelProcessorFactory.buildRecordViewModelProcessorList( );
        if ( !CollectionUtils.isEmpty( listRecordViewModelProcesor ) )
        {
            Locale locale = getLocale( );
            for ( IRecordViewModelProcessor recordViewModelProcessor : listRecordViewModelProcesor )
            {
                recordViewModelProcessor.populateModel( request, model, nIdRecord, locale );
            }
        }

        return getPage( MESSAGE_MULTIVIEW_TITLE, TEMPLATE_VIEW_DIRECTORY_RECORD, model );
    }

    /**
     * Build the model of the page which display the details of a Record
     * 
     * @param request
     *            The request on which the parameters must be retrieve
     * @param record
     *            The record on which the model must be built
     * @return the model associate for the details of the given record
     */
    private Map<String, Object> buildRecordDetailsModel( HttpServletRequest request, Record record )
    {
        AdminUser adminUser = getUser( );
        Locale locale = getLocale( );
        Plugin pluginDirectoryMultiview = DirectoryMultiviewPlugin.getPlugin( );

        Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory( ).getIdDirectory( ), pluginDirectoryMultiview );
        List<IEntry> listEntry = DirectoryUtils.getFormEntries( directory.getIdDirectory( ), pluginDirectoryMultiview, adminUser );

        List<DirectoryAction> listActionsForDirectoryEnable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_ENABLE,
                pluginDirectoryMultiview, locale );
        List<DirectoryAction> listActionsForDirectoryDisable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_DISABLE,
                pluginDirectoryMultiview, locale );

        listActionsForDirectoryEnable = (List<DirectoryAction>) RBACService
                .getAuthorizedActionsCollection( listActionsForDirectoryEnable, directory, adminUser );
        listActionsForDirectoryDisable = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActionsForDirectoryDisable, directory,
                adminUser );
        int nIdRecord = record.getIdRecord( );

        Map<String, Object> mapRecordDetailsModel = new HashMap<>( );
        mapRecordDetailsModel.put( MARK_RECORD, record );
        mapRecordDetailsModel.put( MARK_ENTRY_LIST, listEntry );
        mapRecordDetailsModel.put( MARK_DIRECTORY, directory );
        mapRecordDetailsModel.put( MARK_LOCALE, locale );
        mapRecordDetailsModel.put( MARK_ID_ENTRY_TYPE_GEOLOCATION, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) );
        mapRecordDetailsModel.put( MARK_ID_ENTRY_TYPE_IMAGE, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_IMAGE, 10 ) );
        mapRecordDetailsModel.put( MARK_ID_ENTRY_TYPE_MYLUTECE_USER, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_MYLUTECE_USER, 19 ) );
        mapRecordDetailsModel.put( MARK_PERMISSION_VISUALISATION_MYLUTECE_USER, RBACService.isAuthorized( Directory.RESOURCE_TYPE,
                Integer.toString( directory.getIdDirectory( ) ), DirectoryResourceIdService.PERMISSION_VISUALISATION_MYLUTECE_USER, adminUser ) );
        mapRecordDetailsModel.put( MARK_MAP_ID_ENTRY_LIST_RECORD_FIELD,
                DirectoryUtils.getMapIdEntryListRecordField( listEntry, nIdRecord, pluginDirectoryMultiview ) );

        mapRecordDetailsModel.put( MARK_SHOW_DATE_CREATION_RECORD, directory.isDateShownInResultRecord( ) );
        mapRecordDetailsModel.put( MARK_SHOW_DATE_MODIFICATION_RECORD, directory.isDateModificationShownInResultRecord( ) );

        // Get asynchronous file names
        boolean bGetFileName = true;
        int nIdWorkflow = directory.getIdWorkflow( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        boolean bHistoryEnabled = workflowService.isAvailable( ) && ( nIdWorkflow != DirectoryUtils.CONSTANT_ID_NULL );

        mapRecordDetailsModel.put(
                MARK_RESOURCE_ACTIONS,
                DirectoryService.getInstance( ).getResourceAction( record, directory, listEntry, adminUser, listActionsForDirectoryEnable,
                        listActionsForDirectoryDisable, bGetFileName, pluginDirectoryMultiview ) );
        mapRecordDetailsModel.put( MARK_HISTORY_WORKFLOW_ENABLED, bHistoryEnabled );
        mapRecordDetailsModel.put( MARK_RESOURCE_HISTORY, workflowService.getDisplayDocumentHistory( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE,
                directory.getIdWorkflow( ), request, locale, mapRecordDetailsModel, TEMPLATE_RECORD_HISTORY ) );

        return mapRecordDetailsModel;
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
        int nIdRecord = NumberUtils.toInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ), NumberUtils.INTEGER_MINUS_ONE );
        int nIdAction = NumberUtils.toInt( request.getParameter( PARAMETER_ID_ACTION ), NumberUtils.INTEGER_MINUS_ONE );

        Locale locale = getLocale( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        if ( workflowService.isDisplayTasksForm( nIdAction, locale ) )
        {
            Map<String, String> model = new LinkedHashMap<>( );
            model.put( PARAMETER_ID_DIRECTORY_RECORD, String.valueOf( nIdRecord ) );
            model.put( PARAMETER_ID_ACTION, String.valueOf( nIdAction ) );

            return redirect( request, VIEW_TASKS_FORM, model );
        }

        Plugin pluginDirectoryMultiview = getPlugin( );
        Record record = _recordService.findByPrimaryKey( nIdRecord, pluginDirectoryMultiview );

        try
        {
            if ( record != null )
            {
                boolean bIsAutomaticAction = Boolean.FALSE;
                int nIdDirectory = record.getDirectory( ).getIdDirectory( );
                workflowService.doProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request, locale, bIsAutomaticAction );

                // Update record modification date
                _recordService.update( record, pluginDirectoryMultiview );
            }
            else
            {
                AppLogService.error( "Error processing action for id record '" + nIdRecord + "' - cause : the record doesn't exist " );
            }
        }
        catch( AppException e )
        {
            AppLogService.error( "Error processing action for id record '" + nIdRecord + "' - cause : " + e.getMessage( ), e );
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
        int nIdRecord = NumberUtils.toInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ), NumberUtils.INTEGER_MINUS_ONE );
        int nIdAction = NumberUtils.toInt( request.getParameter( PARAMETER_ID_ACTION ), NumberUtils.INTEGER_MINUS_ONE );

        if ( nIdAction == NumberUtils.INTEGER_MINUS_ONE || nIdRecord == NumberUtils.INTEGER_MINUS_ONE )
        {
            return redirectView( request, VIEW_RECORD_DETAILS );
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
        int nIdRecord = NumberUtils.toInt( request.getParameter( PARAMETER_ID_DIRECTORY_RECORD ), NumberUtils.INTEGER_MINUS_ONE );
        int nIdAction = NumberUtils.toInt( request.getParameter( PARAMETER_ID_ACTION ), NumberUtils.INTEGER_MINUS_ONE );

        Record record = RecordHome.findByPrimaryKey( nIdRecord, getPlugin( ) );
        int nIdDirectory = ( record != null && record.getDirectory( ) != null ) ? record.getDirectory( ).getIdDirectory( ) : NumberUtils.INTEGER_MINUS_ONE;

        WorkflowService workflowService = WorkflowService.getInstance( );
        if ( workflowService.canProcessAction( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request, false ) )
        {
            try
            {
                String strError = workflowService.doSaveTasksForm( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE, nIdAction, nIdDirectory, request, getLocale( ) );
                if ( strError != null )
                {
                    addError( strError );
                    return redirect( request, VIEW_TASKS_FORM, PARAMETER_ID_DIRECTORY_RECORD, nIdRecord, PARAMETER_ID_ACTION, nIdAction );
                }
            }
            catch( AppException e )
            {
                AppLogService.error( "Error processing action for record " + nIdRecord, e );
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

        return redirect( request, VIEW_RECORD_DETAILS, mapParameters );
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
        return redirect( request, MultiDirectoryJspBean.getMultiviewBaseViewUrl( ) );
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

            return redirect( request, VIEW_RECORD_DETAILS, mapParameters );
        }
        catch( NumberFormatException exception )
        {
            AppLogService.error( "The given id directory record is not valid !" );

            return redirect( request, MultiDirectoryJspBean.getMultiviewBaseViewUrl( ) );
        }
    }
}
