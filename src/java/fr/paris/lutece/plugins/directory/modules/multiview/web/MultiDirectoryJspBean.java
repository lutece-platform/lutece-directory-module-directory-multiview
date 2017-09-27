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
import fr.paris.lutece.plugins.directory.business.Field;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterAction;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterActionHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.service.upload.DirectoryAsynchronousUploadHandler;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.directory.web.action.DirectoryActionResult;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
import fr.paris.lutece.plugins.directory.web.action.IDirectoryAction;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
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
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    // Messages (I18n keys)
    private static final String MESSAGE_MANDATORY_FIELD = "directory.message.mandatory.field";
    private static final String MESSAGE_FIELD_VALUE_FIELD = "directory.message.field_value_field";
    private static final String MESSAGE_NUMERIC_FIELD = "directory.message.numeric.field";
    private static final String MESSAGE_WORKFLOW_CHANGE = "directory.message.workflow_change";
    private static final String MESSAGE_CONFIRM_CHANGE_STATES_RECORD = "directory.message.confirm_change_states_record";
    private static final String MESSAGE_ACCESS_DENIED = "Acces denied";
    
    private static final String FIELD_TITLE = "directory.create_directory.label_title";

    // private static final String FIELD_FRONT_OFFICE_TITLE = "directory.create_directory.label_title_front";
    private static final String FIELD_DESCRIPTION = "directory.create_directory.label_description";
    private static final String FIELD_TITLE_FIELD = "directory.create_field.label_title";
    private static final String FIELD_VALUE_FIELD = "directory.create_field.label_value";
    private static final String FIELD_UNAVAILABILITY_MESSAGE = "directory.create_directory.label_unavailability_message";
    private static final String FIELD_ID_FORM_SEARCH_TEMPLATE = "directory.create_directory.label_form_search_template";
    private static final String FIELD_ID_RESULT_LIST_TEMPLATE = "directory.create_directory.label_result_list_template";
    private static final String FIELD_ID_RESULT_RECORD_TEMPLATE = "directory.create_directory.label_result_record_template";
    private static final String FIELD_NUMBER_RECORD_PER_PAGE = "directory.create_directory.label_number_record_per_page";
    
    // properties
    private static final String PROPERTY_MANAGE_DIRECTORY_RECORD_PAGE_TITLE = "directory.manage_directory_record.page_title";
    private static final String PROPERTY_RESOURCE_HISTORY_PAGE_TITLE = "directory.resource_history.page_title";
    private static final String PROPERTY_ENTRY_TYPE_DIRECTORY = "directory.entry_type.directory";
    private static final String PROPERTY_ENTRY_TYPE_GEOLOCATION = "directory.entry_type.geolocation";
    private static final String PROPERTY_ENTRY_TYPE_IMAGE = "directory.resource_rss.entry_type_image";
    private static final String PROPERTY_ENTRY_TYPE_MYLUTECE_USER = "directory.entry_type.mylutece_user";
    private static final String PROPERTY_ENTRY_TYPE_NUMBERING = "directory.entry_type.numbering";
    
    // public properties
    public static final String PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY = "DIRECTORY_MULTIVIEW" ;
    
    
    // Markers
     private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_EXTENT_CURRENT = "extent_current_val";
    private static final String MARK_VISIBLE_LAYER = "visible_layer_val";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ENTRY_LIST_GEOLOCATION = "entry_list_geolocation";
    
    // private static final String MARK_DIRECTORY_RECORD_LIST = "directory_record_list";
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
    private static final String MARK_DASHBOARD_MULTI_DIRECTORY_VIEW = "dashboard_multi_directory_view";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator" ;
   
     private static final String MARK_RECORD = "record";
    private static final String MARK_RESOURCE_ACTIONS_LIST = "resource_actions_list";
    private static final String MARK_RESOURCE_ACTIONS = "resource_actions";
    private static final String MARK_RESOURCE_HISTORY = "resource_history";
    private static final String MARK_HISTORY_WORKFLOW_ENABLED = "history_workflow";
    private static final String MARK_PERMISSION_CREATE_RECORD = "permission_create_record";
    private static final String MARK_PERMISSION_MASS_PRINT = "permission_mass_print";
    private static final String MARK_PERMISSION_VISUALISATION_MYLUTECE_USER = "permission_visualisation_mylutece_user";
    private static final String MARK_IS_WORKFLOW_ENABLED = "is_workflow_enabled";
    private static final String MARK_SEARCH_STATE_WORKFLOW = "search_state_workflow";
    private static final String MARK_WORKFLOW_STATE_SEARCH_DEFAULT = "search_state_workflow_default";

    // JSP URL
    private static final String JSP_MANAGE_DIRECTORY = "jsp/admin/plugins/directory/modules/multiview/ManageDirectory.jsp";
    private static final String JSP_TASKS_FORM_WORKFLOW = "jsp/admin/plugins/directory/modules/multiview/TasksFormWorkflow.jsp";
    private static final String JSP_DISPLAY_PRINT_HISTORY = "jsp/admin/plugins/directory/modules/multiview/DisplayMassPrint.jsp";
    private static final String JSP_DO_CHANGE_STATES_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoChangeStatesRecord.jsp";
    private static final String JSP_DO_VISUALISATION_RECORD = "jsp/admin/plugins/directory/modules/multiview/DoVisualisationRecord.jsp";
    private static final String JSP_RESOURCE_HISTORY = "jsp/admin/plugins/directory/modules/multiview/ResourceHistory.jsp";
    public static final String JSP_MANAGE_MULTI_DIRECTORY_RECORD = "jsp/admin/plugins/directory/modules/multiview/ManageMultiDirectoryRecord.jsp";

    // Parameters
    private static final String PARAMETER_ID_DIRECTORY = DirectoryUtils.PARAMETER_ID_DIRECTORY;
    private static final String PARAMETER_ID_STATE_WORKFLOW = "search_state_workflow";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_FRONT_OFFICE_TITLE = "front_office_title";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_UNAVAILABILITY_MESSAGE = "unavailability_message";
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_ROLE_KEY = "role_key";
    private static final String PARAMETER_ID_FORM_SEARCH_TEMPLATE = "id_form_search_template";
    private static final String PARAMETER_ID_RESULT_LIST_TEMPLATE = "id_result_list_template";
    private static final String PARAMETER_ID_RESULT_RECORD_TEMPLATE = "id_result_record_template";
    private static final String PARAMETER_NUMBER_RECORD_PER_PAGE = "number_record_per_page";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_VALUE = "value";
    private static final String PARAMETER_DEFAULT_VALUE = "default_value";
    private static final String PARAMETER_SESSION = DirectoryUtils.PARAMETER_SESSION;
    private static final String PARAMETER_RESET_SEARCH = "resetsearch";
    private static final String PARAMETER_DATE_SHOWN_IN_RESULT_LIST = "date_shown_in_result_list";
    private static final String PARAMETER_DATE_SHOWN_IN_RESULT_RECORD = "date_shown_in_result_record";
    private static final String PARAMETER_DATE_SHOWN_IN_HISTORY = "date_shown_in_history";
    private static final String PARAMETER_DATE_SHOWN_IN_SEARCH = "date_shown_in_search";
    private static final String PARAMETER_DATE_SHOWN_IN_ADVANCED_SEARCH = "date_shown_in_advanced_search";
    private static final String PARAMETER_DATE_SHOWN_IN_MULTI_SEARCH = "date_shown_in_multi_search";
    private static final String PARAMETER_DATE_SHOWN_IN_EXPORT = "date_shown_in_export";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_RESULT_LIST = "date_modification_shown_in_result_list";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_RESULT_RECORD = "date_modification_shown_in_result_record";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_HISTORY = "date_modification_shown_in_history";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_SEARCH = "date_modification_shown_in_search";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_ADVANCED_SEARCH = "date_modification_shown_in_advanced_search";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_MULTI_SEARCH = "date_modification_shown_in_multi_search";
    private static final String PARAMETER_DATE_MODIFICATION_SHOWN_IN_EXPORT = "date_modification_shown_in_export";
    private static final String PARAMETER_ID_SORT_ENTRY = "id_sort_entry";
    private static final String PARAMETER_ASC_SORT = "asc_sort";
    private static final String PARAMETER_ID_SORT_ENTRY_FRONT = "id_sort_entry_front";
    private static final String PARAMETER_ASC_SORT_FRONT = "asc_sort_front";
    private static final String PARAMETER_ACTIVATE_DIRECTORY_RECORD = "activate_directory_record";
    private static final String PARAMETER_IS_INDEXED = "is_indexed";
    private static final String PARAMETER_SELECTED_RECORD = "selected_record";
    private static final String PARAMETER_IS_SEARCH_OPERATOR_OR = "is_search_operator_or";


    private static final String PARAMETER_WORKFLOW = "id_workflow_list";
    private static final String PARAMETER_WORKFLOW_STATE = "id_workflow_state";
    private static final String PARAMETER_WORKFLOW_STATE_SEARCH = "workflow_state_filter_search";
    private static final String PARAMETER_ID_STATE = "id_state";
    private static final String IS_DISPLAY_STATE_SEARCH = "1";
    private static final String IS_DISPLAY_STATE_SEARCH_COMPLEMENTARY = "2";
    private static final String IS_NOT_DISPLAY_STATE_SEARCH = "3";



    // defaults
    private String DEFAULT_TYPE_IMAGE = "10" ;
    private int DEFAULT_ACTION_ID = -1 ;
    // session fields
    private DirectoryAdminSearchFields _searchFields = new DirectoryAdminSearchFields( );
    private DirectoryActionResult _directoryActionResult = new DirectoryActionResult( );
    private IRecordService _recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );



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
     * Get the request data and if there is no error insert the data in the directory specified in parameter. return null if there is no error or else return
     * the error page url
     * 
     * @param request
     *            the request
     * @param directory
     *            directory
     * @param locale
     *            the locale
     * @return null if there is no error or else return the error page url
     */
    private String getDirectoryData( HttpServletRequest request, Directory directory, Locale locale )
    {
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strFrontOfficeTitle = request.getParameter( PARAMETER_FRONT_OFFICE_TITLE );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strUnavailabilityMessage = request.getParameter( PARAMETER_UNAVAILABILITY_MESSAGE );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );
        String strRoleKey = request.getParameter( PARAMETER_ROLE_KEY );

        String strIdFormSearchTemplate = request.getParameter( PARAMETER_ID_FORM_SEARCH_TEMPLATE );
        String strIdResultListTemplate = request.getParameter( PARAMETER_ID_RESULT_LIST_TEMPLATE );
        String strIdResultRecordTemplate = request.getParameter( PARAMETER_ID_RESULT_RECORD_TEMPLATE );
        String strNumberRecordPerPage = request.getParameter( PARAMETER_NUMBER_RECORD_PER_PAGE );
        String strWorkflow = request.getParameter( PARAMETER_WORKFLOW );
        String strWorkflowState = request.getParameter( PARAMETER_WORKFLOW_STATE );
        String strDisplaySearchStateWorkflow = request.getParameter( PARAMETER_WORKFLOW_STATE_SEARCH );

        String strIdSortEntry = request.getParameter( PARAMETER_ID_SORT_ENTRY );
        String strAscSort = request.getParameter( PARAMETER_ASC_SORT );
        String strIdSortEntryFront = request.getParameter( PARAMETER_ID_SORT_ENTRY_FRONT );
        String strAscSortFront = request.getParameter( PARAMETER_ASC_SORT_FRONT );
        String strRecordActivated = request.getParameter( PARAMETER_ACTIVATE_DIRECTORY_RECORD );
        String strIsIndexed = request.getParameter( PARAMETER_IS_INDEXED );
        String strSearchOperatorOr = request.getParameter( PARAMETER_IS_SEARCH_OPERATOR_OR );

        // creation date field
        String strShowDateInResultList = request.getParameter( PARAMETER_DATE_SHOWN_IN_RESULT_LIST );
        String strShowDateInResultRecord = request.getParameter( PARAMETER_DATE_SHOWN_IN_RESULT_RECORD );
        String strShowDateInHistory = request.getParameter( PARAMETER_DATE_SHOWN_IN_HISTORY );
        String strShowDateInSearch = request.getParameter( PARAMETER_DATE_SHOWN_IN_SEARCH );
        String strShowDateInAdvancedSearch = request.getParameter( PARAMETER_DATE_SHOWN_IN_ADVANCED_SEARCH );
        String strShowDateInMultiSearch = request.getParameter( PARAMETER_DATE_SHOWN_IN_MULTI_SEARCH );
        String strShowDateInExport = request.getParameter( PARAMETER_DATE_SHOWN_IN_EXPORT );

        // creation date field
        String strShowDateModificationInResultList = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_RESULT_LIST );
        String strShowDateModificationInResultRecord = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_RESULT_RECORD );
        String strShowDateModificationInHistory = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_HISTORY );
        String strShowDateModificationInSearch = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_SEARCH );
        String strShowDateModificationInAdvancedSearch = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_ADVANCED_SEARCH );
        String strShowDateModificationInMultiSearch = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_MULTI_SEARCH );
        String strShowDateModificationInExport = request.getParameter( PARAMETER_DATE_MODIFICATION_SHOWN_IN_EXPORT );

        int nIdResultListTemplate = DirectoryUtils.convertStringToInt( strIdResultListTemplate );
        int nIdResultRecordTemplate = DirectoryUtils.convertStringToInt( strIdResultRecordTemplate );
        int nIdFormSearchTemplate = DirectoryUtils.convertStringToInt( strIdFormSearchTemplate );
        int nNumberRecordPerPage = DirectoryUtils.convertStringToInt( strNumberRecordPerPage );
        int nIdWorkflow = DirectoryUtils.convertStringToInt( strWorkflow );
        int nIdWorkflowState = DirectoryUtils.convertStringToInt( strWorkflowState );

        String strFieldError = DirectoryUtils.EMPTY_STRING;

        if ( ( strTitle == null ) || strTitle.trim( ).equals( DirectoryUtils.EMPTY_STRING ) )
        {
            strFieldError = FIELD_TITLE;
        }

        else
            if ( ( strDescription == null ) || strDescription.trim( ).equals( DirectoryUtils.EMPTY_STRING ) )
            {
                strFieldError = FIELD_DESCRIPTION;
            }

            else
                if ( ( strUnavailabilityMessage == null ) || strUnavailabilityMessage.trim( ).equals( DirectoryUtils.EMPTY_STRING ) )
                {
                    strFieldError = FIELD_UNAVAILABILITY_MESSAGE;
                }
                else
                    if ( nIdFormSearchTemplate == DirectoryUtils.CONSTANT_ID_NULL )
                    {
                        strFieldError = FIELD_ID_FORM_SEARCH_TEMPLATE;
                    }

                    else
                        if ( nIdResultListTemplate == DirectoryUtils.CONSTANT_ID_NULL )
                        {
                            strFieldError = FIELD_ID_RESULT_LIST_TEMPLATE;
                        }
                        else
                            if ( nIdResultRecordTemplate == DirectoryUtils.CONSTANT_ID_NULL )
                            {
                                strFieldError = FIELD_ID_RESULT_RECORD_TEMPLATE;
                            }
                            else
                                if ( ( strNumberRecordPerPage == null ) || strNumberRecordPerPage.trim( ).equals( DirectoryUtils.EMPTY_STRING ) )
                                {
                                    strFieldError = FIELD_NUMBER_RECORD_PER_PAGE;
                                }

        if ( !strFieldError.equals( DirectoryUtils.EMPTY_STRING ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, getLocale( ) )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        if ( nNumberRecordPerPage == -1 )
        {
            strFieldError = FIELD_NUMBER_RECORD_PER_PAGE;
        }

        if ( !strFieldError.equals( DirectoryUtils.EMPTY_STRING ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_NUMERIC_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        RecordFieldFilter recordFilter = new RecordFieldFilter( );
        recordFilter.setIdDirectory( directory.getIdDirectory( ) );

        int nCountRecord = _recordService.getCountRecord( recordFilter, getPlugin( ) );

        if ( ( directory.getIdWorkflow( ) != nIdWorkflow ) && ( nCountRecord != 0 ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKFLOW_CHANGE, AdminMessage.TYPE_STOP );
        }

        directory.setTitle( strTitle );

        if ( strFrontOfficeTitle == null )
        {
            strFrontOfficeTitle = StringUtils.EMPTY;
        }

        directory.setFrontOfficeTitle( strFrontOfficeTitle );
        directory.setDescription( strDescription );
        directory.setUnavailabilityMessage( strUnavailabilityMessage );
        directory.setWorkgroup( strWorkgroup );
        directory.setRoleKey( strRoleKey );
        directory.setIdFormSearchTemplate( nIdFormSearchTemplate );
        directory.setIdResultListTemplate( nIdResultListTemplate );
        directory.setIdResultRecordTemplate( nIdResultRecordTemplate );
        directory.setNumberRecordPerPage( nNumberRecordPerPage );
        directory.setIdWorkflow( nIdWorkflow );

        if ( nIdWorkflow > DirectoryUtils.CONSTANT_ID_NULL )
        {
            directory.setIdWorkflowStateToRemove( nIdWorkflowState );
        }
        else
        {
            directory.setIdWorkflowStateToRemove( DirectoryUtils.CONSTANT_ID_NULL );
        }

        if ( ( strDisplaySearchStateWorkflow != null ) && strDisplaySearchStateWorkflow.equals( IS_DISPLAY_STATE_SEARCH ) )
        {
            directory.setDisplayComplementarySearchState( false );
            directory.setDisplaySearchState( true );
        }
        else
            if ( ( strDisplaySearchStateWorkflow != null ) && strDisplaySearchStateWorkflow.equals( IS_DISPLAY_STATE_SEARCH_COMPLEMENTARY ) )
            {
                directory.setDisplayComplementarySearchState( true );
                directory.setDisplaySearchState( false );
            }
            else
                if ( ( strDisplaySearchStateWorkflow != null ) && strDisplaySearchStateWorkflow.equals( IS_NOT_DISPLAY_STATE_SEARCH ) )
                {
                    directory.setDisplayComplementarySearchState( false );
                    directory.setDisplaySearchState( false );
                }

        directory.setDateShownInResultList( strShowDateInResultList != null );
        directory.setDateShownInResultRecord( strShowDateInResultRecord != null );
        directory.setDateShownInHistory( strShowDateInHistory != null );
        directory.setDateShownInSearch( strShowDateInSearch != null );
        directory.setDateShownInAdvancedSearch( strShowDateInAdvancedSearch != null );
        directory.setDateShownInMultiSearch( strShowDateInMultiSearch != null );
        directory.setDateShownInExport( strShowDateInExport != null );

        directory.setDateModificationShownInResultList( strShowDateModificationInResultList != null );
        directory.setDateModificationShownInResultRecord( strShowDateModificationInResultRecord != null );
        directory.setDateModificationShownInHistory( strShowDateModificationInHistory != null );
        directory.setDateModificationShownInSearch( strShowDateModificationInSearch != null );
        directory.setDateModificationShownInAdvancedSearch( strShowDateModificationInAdvancedSearch != null );
        directory.setDateModificationShownInMultiSearch( strShowDateModificationInMultiSearch != null );
        directory.setDateModificationShownInExport( strShowDateModificationInExport != null );

        if ( ( strIdSortEntry != null ) && ( !strIdSortEntry.equals( DirectoryUtils.EMPTY_STRING ) ) )
        {
            directory.setIdSortEntry( strIdSortEntry );
        }
        else
        {
            directory.setIdSortEntry( null );
        }

        if ( ( strIdSortEntryFront != null ) && ( !strIdSortEntryFront.equals( DirectoryUtils.EMPTY_STRING ) ) )
        {
            directory.setIdSortEntryFront( strIdSortEntryFront );
        }
        else
        {
            directory.setIdSortEntryFront( null );
        }

        directory.setAscendingSort( strAscSort != null );
        directory.setAscendingSortFront( strAscSortFront != null );
        directory.setRecordActivated( strRecordActivated != null );
        directory.setIndexed( strIsIndexed != null );
        directory.setSearchOperatorOr( strSearchOperatorOr != null );

        return null; // No error
    }



    /**
     * Get the request data and if there is no error insert the data in the field specified in parameter. return null if there is no error or else return the
     * error page url
     * 
     * @param request
     *            the request
     * @param field
     *            field
     * @return null if there is no error or else return the error page url
     */
    private String getFieldData( HttpServletRequest request, Field field )
    {
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strValue = request.getParameter( PARAMETER_VALUE );
        String strDefaultValue = request.getParameter( PARAMETER_DEFAULT_VALUE );

        String strFieldError = DirectoryUtils.EMPTY_STRING;

        if ( ( strTitle == null ) || DirectoryUtils.EMPTY_STRING.equals( strTitle ) )
        {
            strFieldError = FIELD_TITLE_FIELD;
        }
        else
            if ( ( strValue == null ) || DirectoryUtils.EMPTY_STRING.equals( strValue ) )
            {
                strFieldError = FIELD_VALUE_FIELD;
            }
            else
                if ( !StringUtil.checkCodeKey( strValue ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_FIELD_VALUE_FIELD, AdminMessage.TYPE_STOP );
                }

        String strRoleKey = request.getParameter( PARAMETER_ROLE_KEY );
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP );

        if ( !strFieldError.equals( DirectoryUtils.EMPTY_STRING ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, getLocale( ) )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        field.setTitle( strTitle );
        field.setValue( strValue );
        field.setDefaultValue( strDefaultValue != null );
        field.setRoleKey( strRoleKey );
        field.setWorkgroup( strWorkgroupKey );

        return null; // No error
    }

  

    /**
     * Return management of directory record ( list of directory record ).
     * 
     * @param request
     *            The Http request
     * @param response
     *            the Http response
     * @throws AccessDeniedException
     *             the {@link AccessDeniedException}
     * @return IPluginActionResult
     */
    public IPluginActionResult getManageDirectoryRecord( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        return getManageDirectoryRecord( request, response, false );

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
        String strExtentCurrent = request.getParameter( MARK_EXTENT_CURRENT ) == null ? "" : request.getParameter( MARK_EXTENT_CURRENT );
        String strVisibleLayer = request.getParameter( MARK_VISIBLE_LAYER ) == null ? "" : request.getParameter( MARK_VISIBLE_LAYER );

        HttpSession session = request.getSession( false );

        if ( !strExtentCurrent.isEmpty( ) && !strVisibleLayer.isEmpty( ) )
        {
            session.setAttribute( MARK_EXTENT_CURRENT, strExtentCurrent );
            session.setAttribute( MARK_VISIBLE_LAYER, strVisibleLayer );
        }

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

        // display could have been an action but it's the default one an will always be here...
        DefaultPluginActionResult result = new DefaultPluginActionResult( );
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdWorkflowState = request.getParameter( PARAMETER_ID_STATE_WORKFLOW );

        int nIdWorkflowState = DirectoryUtils.convertStringToInt( strIdWorkflowState );
        int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
        
        boolean bWorkflowServiceEnable = WorkflowService.getInstance( ).isAvailable( );
        AdminUser adminUser = getUser( );
        
        List<Directory> directoryList = new ArrayList<>( ) ;
        List<Integer> workflowStateIdList  = new ArrayList<>( );
        
        if ( nIdDirectory<=0 ) 
        {
            // present the list of directories of the filters by default 
            List<DirectoryViewFilter> directoryViewList = DirectoryViewFilterHome.getDirectoryFiltersList( );
            for ( DirectoryViewFilter dvf : directoryViewList )
            {
                Directory directory = DirectoryHome.findByPrimaryKey( dvf.getIdDirectory( ) , getPlugin( ) ) ;
                if ( RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_MANAGE_RECORD, getUser( ) )
                || AdminWorkgroupService.isAuthorized( directory, getUser( ) ) ) 
                {
                    directoryList.add( directory ) ;
                    List<DirectoryViewFilterAction> DFactionList = DirectoryViewFilterActionHome.getDirectoryFilterActionsListByDirectoryFilter( dvf.getId( ) ) ;
                    if ( DFactionList != null && DFactionList.size( )>0 )
                    {
                        // get the first action only
                        workflowStateIdList.add( DFactionList.get( 0 ).getIdAction( ) ) ;
                    } 
                    else 
                    {
                        workflowStateIdList.add( DEFAULT_ACTION_ID ) ;
                    }
                    
                }
            }
            
            if ( directoryList.size( ) == 0 ) 
            {
                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
            }
        } 
        else 
        {
            // access by ONE directory
            Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, getPlugin( ) );
            if ( ( directory == null ) || (
                 !RBACService.isAuthorized( Directory.RESOURCE_TYPE, strIdDirectory, DirectoryResourceIdService.PERMISSION_MANAGE_RECORD, getUser( ) )
                || !AdminWorkgroupService.isAuthorized( directory, getUser( ) ) ) ) 
            {
                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
            }
            
            directoryList.add( directory );
            if ( nIdWorkflowState >= 0 ) workflowStateIdList.add( nIdWorkflowState ) ;
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

        
                
        
        List<Integer> listResultRecordId = new ArrayList<>( ) ;
        int index = 0 ;
        List<IEntry> listEntryFormMainSearch = new ArrayList<>( );
        List<IEntry> listEntryFormComplementarySearch = new ArrayList<>( );
        List<IEntry> listEntryResultSearch = new ArrayList<>( );
        List<IEntry> listEntryGeolocation = new ArrayList<>( );
        
        for ( Directory directory : directoryList )
        {
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

                if ( entryTmp.isIndexed( ) )
                {
                    if ( !entryTmp.isShownInAdvancedSearch( ) )
                    {
                        listEntryFormMainSearch.add( entryTmp );
                    }
                    else
                    {
                        listEntryFormComplementarySearch.add( entryTmp );
                    }
                }

                if ( entry.isShownInResultList( ) )
                {
                    listEntryResultSearch.add( entryTmp );

                    // add geolocation entries
                    if ( entry.getEntryType( ).getIdType( ) == AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) )
                    {
                        listEntryGeolocation.add( entry );
                    }
                }
            }
            


            // get the records Ids for this directory

            HashMap<String, List<RecordField>> map = DirectoryViewFilterHome.getFilterSearchMap( directory , adminUser );
            HashMap<String, List<RecordField>> mapQuery =  _searchFields.getMapQuery( );
            if ( directoryList.size( ) == 1 && mapQuery != null )
            {
                mapQuery.putAll( map );
            }
            else
            {
                mapQuery = map;
            }
            _searchFields.setMapQuery( mapQuery );
            

            // filter the search with a directory filter action
            if ( workflowStateIdList.size() > 0 && workflowStateIdList.get( index ) > 0 )
                _searchFields.setIdWorkflowSate( workflowStateIdList.get( index ) ) ;

            _searchFields.setSortParameters( request, directory, getPlugin( ) );
            _searchFields.setListIdsResultRecord( new ArrayList<Integer>( ) );
        
            listResultRecordId.addAll( DirectoryUtils.getListResults( request, directory, bWorkflowServiceEnable, true, _searchFields, getUser( ),
                    getLocale( ) ) ) ;
            
            index ++;
        }
        
        // Store the list of id records in session
        _searchFields.setListIdsResultRecord( listResultRecordId );

        // HACK : We copy the list so workflow does not clear the paginator list.
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<Integer>( new ArrayList<Integer>( listResultRecordId ),
                _searchFields.getItemsPerPageDirectoryRecord( ), 
                getJspManageMultiDirectoryRecord( request, (directoryList.size( ) == 1?directoryList.get( 0 ):null) ), 
                PARAMETER_PAGE_INDEX,
                _searchFields.getCurrentPageIndexDirectoryRecord( ), getLocale( ) );

        // get only record for page items.
        List<Record> lRecord = _recordService.loadListByListId( paginator.getPageItems( ), getPlugin( ) );

        boolean bHistoryEnabled = ( directoryList.size( ) == 1  
                                    && WorkflowService.getInstance( ).isAvailable( ) 
                                    && directoryList.get(0).getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL );
        RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
        recordFieldFilter.setIsEntryShownInResultList( RecordFieldFilter.FILTER_TRUE );

        bWorkflowServiceEnable = ( directoryList.size( ) == 1  
                                    && directoryList.get( 0 ).getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL  
                                    && bWorkflowServiceEnable );

        List<Map<String, Object>> listResourceActions = new ArrayList<Map<String, Object>>( lRecord.size( ) );

        List<DirectoryAction> listActionsForDirectoryEnable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_ENABLE, getPlugin( ),
                getLocale( ) );
        List<DirectoryAction> listActionsForDirectoryDisable = DirectoryActionHome.selectActionsRecordByFormState( Directory.STATE_DISABLE, getPlugin( ),
                getLocale( ) );

        if ( directoryList.size( ) == 1 ) 
        {
            listActionsForDirectoryEnable = (List<DirectoryAction>) RBACService
                    .getAuthorizedActionsCollection( listActionsForDirectoryEnable, directoryList.get( 0 ), getUser( ) );
            listActionsForDirectoryDisable = (List<DirectoryAction>) RBACService
                    .getAuthorizedActionsCollection( listActionsForDirectoryDisable, directoryList.get( 0 ),
                getUser( ) );
        }

        // Get asynchronous file names put at false for better performance
        // since it must call a webservice to get the file name
        boolean bGetFileName = false;

        
        // data complement (should be done in directory plugin)
        for (Directory directory : directoryList ) 
        {
            for ( Record record : lRecord )
            {
                if (record.getDirectory( ).getIdDirectory( ) == directory.getIdDirectory( ) ) {
                    record.getDirectory( ).setIdWorkflow( directory.getIdWorkflow( ) );
                    record.getDirectory( ).setTitle( directory.getTitle( ) );                    
                }
            }
        }
        
        for ( Record record : lRecord )
        {
            listResourceActions.add( DirectoryService.getInstance( ).getResourceAction( record, record.getDirectory( ), 
                    listEntryResultSearch, adminUser, listActionsForDirectoryEnable, listActionsForDirectoryDisable, 
                    bGetFileName, getPlugin( ) ) );
        }

        DirectoryFilterDashboardComponent dashboard = (DirectoryFilterDashboardComponent) DashboardFactory.getDashboardComponent( "DIRECTORY_FILTER" );
        String strDashBoardMultiViewContent = dashboard.getDashboardData( getUser( ), request );

        Map<String, Object> model = new HashMap<String, Object>( );

        
        model.put( MARK_ID_ENTRY_TYPE_IMAGE, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_IMAGE, 10 ) );
        model.put( MARK_ID_ENTRY_TYPE_DIRECTORY, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_DIRECTORY, 12 ) );
        model.put( MARK_ID_ENTRY_TYPE_GEOLOCATION, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_GEOLOCATION, 16 ) );
        model.put( MARK_ID_ENTRY_TYPE_MYLUTECE_USER, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_MYLUTECE_USER, 19 ) );
        model.put( MARK_ID_ENTRY_TYPE_NUMBERING, AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_NUMBERING, 11 ) );
        model.put( MARK_ENTRY_LIST_GEOLOCATION, listEntryGeolocation );
        model.put( MARK_WORKFLOW_STATE_SEARCH_DEFAULT, _searchFields.get_nIdWorkflowSate( ) );
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

        if (directoryList.size( ) == 1 ) 
        {
            model.put( MARK_SHOW_DATE_CREATION_RESULT, directoryList.get( 0 ).isDateShownInResultList( ) );
            model.put( MARK_SHOW_DATE_MODIFICATION_RESULT, directoryList.get( 0 ).isDateModificationShownInResultList( ) );
            model.put( MARK_DIRECTORY, directoryList.get( 0 ) );
        }
        
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

        model.put( MARK_EXTENT_CURRENT, session.getAttribute( MARK_EXTENT_CURRENT ) );
        model.put( MARK_VISIBLE_LAYER, session.getAttribute( MARK_VISIBLE_LAYER ) );

        model.put( MARK_DASHBOARD_MULTI_DIRECTORY_VIEW, strDashBoardMultiViewContent );
        if ( directoryList.size( ) == 1 && (directoryList.get( 0 ).isDisplayComplementarySearchState( ) || directoryList.get( 0 ).isDisplaySearchState( ) ) )
        {
            ReferenceList referenceList = new ReferenceList( );
            referenceList.addItem( -1, "" );

            Collection<State> colState = WorkflowService.getInstance( ).getAllStateByWorkflow( directoryList.get( 0 ).getIdWorkflow( ), adminUser );

            if ( colState != null )
            {
                for ( State stateWorkflow : colState )
                {
                    referenceList.addItem( stateWorkflow.getId( ), stateWorkflow.getName( ) );
                }
            }

            model.put( MARK_SEARCH_STATE_WORKFLOW, referenceList );
        }

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

        Map<String, Object> model = new HashMap<String, Object>( );

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
     * return url of the jsp
     * 
     * @param request
     *            The HTTP request
     * @param listState
     *            A state list
     * @param nIdDirectory
     *            The directory id
     * @return url of the jsp
     */
    private String getJspPrintHistory( HttpServletRequest request, List<State> listState, int nIdDirectory )
    {
        String strIdState = new String( );

        for ( State state : listState )
        {
            strIdState = strIdState.concat( state.getId( ) + "," );
        }

        if ( strIdState.length( ) > 0 )
        {
            strIdState = strIdState.substring( 0, strIdState.length( ) - 1 );
        }

        return AppPathService.getBaseUrl( request ) + JSP_DISPLAY_PRINT_HISTORY + "?" + PARAMETER_ID_DIRECTORY + "=" + nIdDirectory + "&" + PARAMETER_ID_STATE
                + "=" + strIdState;
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
     * Init the list of the attribute's orders (first level only)
     * 
     * @param listEntryFirstLevel
     *            the list of all the attributes of the first level
     * @param orderFirstLevel
     *            the list to set
     */
    private void initOrderFirstLevel( List<IEntry> listEntryFirstLevel, List<Integer> orderFirstLevel )
    {
        for ( IEntry entry : listEntryFirstLevel )
        {
            orderFirstLevel.add( entry.getPosition( ) );
        }
    }

 

    /**
     * Populate map with ( idParent : List<Orders> ) except for entry with parent null
     * 
     * @param listEntry
     *            the list of all entry
     * @param mapIdParentOrdersChildren
     *            map with (idParent : List<Orders>)
     */
    private void populateEntryMap( List<IEntry> listEntry, Map<String, List<Integer>> mapIdParentOrdersChildren )
    {
        List<Integer> listOrder;

        for ( IEntry entry : listEntry )
        {
            if ( entry.getParent( ) != null )
            {
                Integer key = Integer.valueOf( entry.getParent( ).getIdEntry( ) );
                String strKey = key.toString( );

                if ( mapIdParentOrdersChildren.get( strKey ) != null )
                {
                    mapIdParentOrdersChildren.get( key.toString( ) ).add( entry.getPosition( ) );
                }
                else
                {
                    listOrder = new ArrayList<Integer>( );
                    listOrder.add( entry.getPosition( ) );
                    mapIdParentOrdersChildren.put( key.toString( ), listOrder );
                }
            }
        }
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
    public static String getJspManageMultiDirectoryRecord( HttpServletRequest request, int nIdDirectory )
    {
        Directory directory = new Directory( );
        directory.setIdDirectory( nIdDirectory );
        return getJspManageMultiDirectoryRecord( request, directory );
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
    public static String getJspManageMultiDirectoryRecord( HttpServletRequest request, Directory directory )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_MULTI_DIRECTORY_RECORD );
        if ( directory != null )
            urlItem.addParameter( PARAMETER_ID_DIRECTORY, directory.getIdDirectory( ) );
        urlItem.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( ( directory != null ) && ( ( strSortedAttributeName != null ) || ( directory.getIdSortEntry( ) != null ) ) )
        {
            if ( strSortedAttributeName == null )
            {
                strSortedAttributeName = directory.getIdSortEntry( );
            }

            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            urlItem.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
            urlItem.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        return urlItem.getUrl( );
    }
}
