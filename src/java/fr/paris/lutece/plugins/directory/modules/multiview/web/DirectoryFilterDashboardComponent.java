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
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterCondition;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterConditionHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome;
import static fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterHome.getDirectoryFiltersListByDirectoryId;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterAction;
import fr.paris.lutece.plugins.directory.modules.multiview.business.DirectoryViewFilterActionHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.service.directorysearch.DirectorySearchService;
import fr.paris.lutece.plugins.directory.service.record.IRecordService;
import fr.paris.lutece.plugins.directory.service.record.RecordService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;
import java.util.AbstractList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Calendar Dashboard Component This component displays directories
 */
public class DirectoryFilterDashboardComponent extends DashboardComponent
{
    // MARKS
    private static final String MARK_URL = "url";
    private static final String MARK_ICON = "icon";
    private static final String MARK_DIRECTORY_LIST = "directory_list";
    private static final String MARK_RECORD_COUNT_LIST = "record_count_list";
    private static final String MARK_DIRECTORY_STATE_LIST = "directory_state_list";

    private static final String MARK_AUTHORIZED_DIRECTORY_MODIFICATION_LIST = "authorized_directory_modification_list";
    private static final String MARK_PERMISSION_CREATE = "permission_create";

    // CONSTANTS
    private static final String EMPTY_STRING = "";

    // TEMPALTES
    private static final String TEMPLATE_DASHBOARD = "/admin/plugins/directory/modules/multiview/directory_filter_dashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Right right = RightHome.findByPrimaryKey( getRight( ) );
        Plugin plugin = PluginService.getPlugin( right.getPluginName( ) );
        List<DirectoryAction> listActions;
        List<DirectoryAction> listActionsForDirectoryEnable;
        List<DirectoryAction> listActionsForDirectoryDisable;

        if ( !( ( plugin.getDbPoolName( ) != null ) && !AppConnectionService.NO_POOL_DEFINED.equals( plugin.getDbPoolName( ) ) ) )
        {
            return EMPTY_STRING;
        }

        UrlItem url = new UrlItem( right.getUrl( ) );
        url.addParameter( DirectoryPlugin.PLUGIN_NAME, right.getPluginName( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        List<Directory> directoryList = new ArrayList<Directory>( );

        Map<String, Object> recordCountMap = new HashMap<String, Object>( );
        List<Integer> nAuthorizedModificationList = new ArrayList<Integer>( );
        HashMap<String, String> directoryStateMap = new HashMap<String, String>( );

        List<DirectoryViewFilter> directoryFilterList = DirectoryViewFilterHome.getDirectoryFiltersList( );
        for ( DirectoryViewFilter directoryViewFilter : directoryFilterList )
        {
            Directory directory = DirectoryHome.findByPrimaryKey( directoryViewFilter.getIdDirectory( ), plugin );
            directoryList.add( directory );

            List<DirectoryViewFilterAction> actionList = DirectoryViewFilterActionHome.getDirectoryFilterActionsListByDirectoryFilter( directoryViewFilter
                    .getId( ) );
            for ( DirectoryViewFilterAction action : actionList )
            {
                directoryStateMap.put( String.valueOf( directory.getIdDirectory( ) ), String.valueOf( action.getIdAction( ) ) );
            }
        }

        // workgroup control
        directoryList = (List<Directory>) AdminWorkgroupService.getAuthorizedCollection( directoryList, user );

        listActionsForDirectoryEnable = DirectoryActionHome.selectActionsByFormState( Directory.STATE_ENABLE, getPlugin( ), user.getLocale( ) );
        listActionsForDirectoryDisable = DirectoryActionHome.selectActionsByFormState( Directory.STATE_DISABLE, getPlugin( ), user.getLocale( ) );

        for ( Directory directory : directoryList )
        {
            listActions = ( directory.isEnabled( ) ? listActionsForDirectoryEnable : listActionsForDirectoryDisable );

            listActions = (List<DirectoryAction>) RBACService.getAuthorizedActionsCollection( listActions, directory, user );
            directory.setActions( listActions );

            if ( RBACService.isAuthorized( directory, DirectoryResourceIdService.PERMISSION_MODIFY, user ) )
            {
                nAuthorizedModificationList.add( directory.getIdDirectory( ) );
            }

            // count filtred records
            HashMap<String, List<RecordField>> filterMapSearch = DirectoryViewFilterHome.getFilterSearchMap( directory, user );
            int filterStateId = DirectoryViewFilterHome.getFilterStateId( directory.getIdDirectory( ) );

            recordCountMap.put( Integer.toString( directory.getIdDirectory( ) ), getRecordsCount( directory, user, filterStateId, filterMapSearch ) );
        }

        model.put( MARK_DIRECTORY_LIST, directoryList );
        model.put( MARK_RECORD_COUNT_LIST, recordCountMap );
        model.put( MARK_DIRECTORY_STATE_LIST, directoryStateMap );
        model.put( MARK_AUTHORIZED_DIRECTORY_MODIFICATION_LIST, nAuthorizedModificationList );

        model.put( MARK_URL, url.getUrl( ) );
        model.put( MARK_ICON, plugin.getIconUrl( ) );
        model.put( MARK_PERMISSION_CREATE,
                RBACService.isAuthorized( Directory.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, DirectoryResourceIdService.PERMISSION_CREATE, user ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD, user.getLocale( ), model );

        return t.getHtml( );
    }

    /**
     * Get the records count of a directory, filtered by a specific action and/or values of entries,
     * 
     * @param directory
     *            the {@link Directory}
     * @param user
     *            the {@link AdminUser}
     * @param action
     *            the directoryAction to filter
     * @param user
     *            the {@link AdminUser}
     * @return the record count
     */
    public int getRecordsCount( Directory directory, AdminUser user, int formStateId, HashMap<String, List<RecordField>> mapSearch )
    {
        Plugin plugin = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        int nNbRecords = 0;
        boolean bWorkflowServiceEnable = WorkflowService.getInstance( ).isAvailable( );
        RecordFieldFilter filter = new RecordFieldFilter( );

        filter.setIdDirectory( directory.getIdDirectory( ) );
        filter.setWorkgroupKeyList( AdminWorkgroupService.getUserWorkgroups( user, user.getLocale( ) ) );

        if ( ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_NULL ) && ( directory.getIdWorkflow( ) != DirectoryUtils.CONSTANT_ID_ZERO )
                && bWorkflowServiceEnable )
        {
            List<Integer> listResultRecordIds = DirectorySearchService.getInstance( ).getSearchResults( directory, mapSearch, null, null, null, filter, plugin );

            List<Integer> listTmpResultRecordIds = WorkflowService.getInstance( ).getAuthorizedResourceList( Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow( ), ( formStateId > -1 ? formStateId : DirectoryUtils.CONSTANT_ID_NULL ),
                    Integer.valueOf( directory.getIdDirectory( ) ), user );

            listResultRecordIds = DirectoryUtils.retainAllIdsKeepingFirstOrder( listResultRecordIds, listTmpResultRecordIds );
            nNbRecords = listResultRecordIds.size( );
        }
        else
        {
            IRecordService recordService = SpringContextService.getBean( RecordService.BEAN_SERVICE );
            nNbRecords = recordService.getCountRecord( filter, plugin );
        }

        return nNbRecords;
    }

}
