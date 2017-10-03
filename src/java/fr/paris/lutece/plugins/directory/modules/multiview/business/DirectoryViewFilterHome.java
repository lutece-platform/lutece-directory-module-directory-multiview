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
package fr.paris.lutece.plugins.directory.modules.multiview.business;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.Entry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DirectoryViewFilter objects
 */
public final class DirectoryViewFilterHome
{
    // Static variable pointed at the DAO instance
    private static IDirectoryViewFilterDAO _dao = SpringContextService.getBean( "directory-multiview.directoryViewFilterDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "directory-multiview" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DirectoryViewFilterHome( )
    {
    }

    /**
     * Create an instance of the directoryFilter class
     * 
     * @param directoryFilter
     *            The instance of the DirectoryViewFilter which contains the informations to store
     * @return The instance of directoryFilter which has been created with its primary key.
     */
    public static DirectoryViewFilter create( DirectoryViewFilter directoryFilter )
    {
        _dao.insert( directoryFilter, _plugin );

        return directoryFilter;
    }

    /**
     * Update of the directoryFilter which is specified in parameter
     * 
     * @param directoryFilter
     *            The instance of the DirectoryViewFilter which contains the data to store
     * @return The instance of the directoryFilter which has been updated
     */
    public static DirectoryViewFilter update( DirectoryViewFilter directoryFilter )
    {
        _dao.store( directoryFilter, _plugin );

        return directoryFilter;
    }

    /**
     * Remove the directoryFilter whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilter Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a directoryFilter whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilter primary key
     * @return an instance of DirectoryViewFilter
     */
    public static DirectoryViewFilter findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryFilter objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilter objects
     */
    public static List<DirectoryViewFilter> getDirectoryFiltersList( )
    {
        return _dao.selectDirectoryFiltersList( _plugin );
    }

    /**
     * Load the data of all the directoryFilter objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilter objects
     */
    public static List<DirectoryViewFilter> getDirectoryFiltersListByDirectoryId( int nIdDirectory )
    {
        return _dao.selectDirectoryFiltersListByDirectoryId( nIdDirectory, _plugin );
    }

    /**
     * Load the id of all the directoryFilter objects and returns them as a list
     * 
     * @return the list which contains the id of all the directoryFilter objects
     */
    public static List<Integer> getIdDirectoryFiltersList( )
    {
        return _dao.selectIdDirectoryFiltersList( _plugin );
    }

    /**
     * Load the data of all the directoryFilter objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the directoryFilter objects
     */
    public static ReferenceList getDirectoryFiltersReferenceList( )
    {
        return _dao.selectDirectoryFiltersReferenceList( _plugin );
    }

    /**
     * build the searchMap to filter the list of records with the filter condition
     * 
     * @param directory
     * @param user
     * @return the referenceList which contains the data of all the directoryFilter objects
     */
    public static HashMap<String, List<RecordField>> getFilterSearchMap( Directory directory, AdminUser user )
    {
        HashMap<String, List<RecordField>> filterSearchMap = new HashMap<>( );

        List<DirectoryViewFilter> directoryFilterList = getDirectoryFiltersListByDirectoryId( directory.getIdDirectory( ) );
        for ( DirectoryViewFilter directoryFilter : directoryFilterList )
        {

            List<DirectoryViewFilterCondition> conditionList = DirectoryViewFilterConditionHome
                    .getDirectoryFilterConditionsListByDirectoryFilter( directoryFilter.getId( ) );
            for ( DirectoryViewFilterCondition condition : conditionList )
            {

                int entryId = condition.getIdEntry( );
                RecordField recordField = new RecordField( );
                Entry entry = new Entry( );
                entry.setIdEntry( entryId );
                entry.setDirectory( directory );
                recordField.setEntry( entry );

                if ( condition.getFilterType( ) == DirectoryViewFilterConditionDAO.PROPERTY_TYPE_UNITTREE_CODE )
                {
                    IUnitService unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
                    List<Unit> unitList = unitService.getUnitsByIdUser( user.getUserId( ), true );
                    Unit unit = null;
                    if ( unitList != null )
                    {
                        unit = (Unit) unitList.get( 0 ); // TODO multi unit case
                        recordField.setValue( unit.getLabel( ) );

                        List<RecordField> recordFieldList = new ArrayList<>( );
                        recordFieldList.add( recordField );
                        filterSearchMap.put( String.valueOf( entryId ), recordFieldList );
                    }
                }

            }
        }

        return filterSearchMap;
    }

    /**
     * build the searchMap to filter the list of records with the filter condition
     * 
     * @param nIdDirectory
     * @return the referenceList which contains the data of all the directoryFilter objects
     */
    public static int getFilterStateId( int nIdDirectory )
    {
        List<DirectoryViewFilter> directoryFilterList = getDirectoryFiltersListByDirectoryId( nIdDirectory );
        for ( DirectoryViewFilter directoryFilter : directoryFilterList )
        {

            List<DirectoryViewFilterAction> actionList = DirectoryViewFilterActionHome
                    .getDirectoryFilterActionsListByDirectoryFilter( directoryFilter.getId( ) );
            for ( DirectoryViewFilterAction action : actionList )
            {
                // returns the first one only (one directory by dashboard)
                return action.getIdAction( );
            }
        }

        return -1;
    }

    /**
     * return true if a filter directory exist for the directory
     * 
     * @param nIdDirectory
     * @return the referenceList which contains the data of all the directoryFilter objects
     */
    public static boolean existFilter( int nIdDirectory )
    {
        List<DirectoryViewFilter> directoryFilterList = getDirectoryFiltersListByDirectoryId( nIdDirectory );
        for ( DirectoryViewFilter directoryFilter : directoryFilterList )
        {

            List<DirectoryViewFilterAction> actionList = DirectoryViewFilterActionHome
                    .getDirectoryFilterActionsListByDirectoryFilter( directoryFilter.getId( ) );

            // one directory by dashboard
            if ( actionList != null && actionList.size( ) > 0 )
                return true;

        }

        return false;
    }

}
