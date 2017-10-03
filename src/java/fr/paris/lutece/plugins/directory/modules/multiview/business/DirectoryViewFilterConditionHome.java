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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

/**
 * This class provides instances management methods (create, find, ...) for DirectoryViewFilterCondition objects
 */
public final class DirectoryViewFilterConditionHome
{
    // Static variable pointed at the DAO instance
    private static IDirectoryViewFilterConditionDAO _dao = SpringContextService.getBean( "directory-multiview.directoryViewFilterConditionDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "directory-multiview" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DirectoryViewFilterConditionHome( )
    {
    }

    /**
     * Create an instance of the directoryFilterCondition class
     * 
     * @param directoryFilterCondition
     *            The instance of the DirectoryViewFilterCondition which contains the informations to store
     * @return The instance of directoryFilterCondition which has been created with its primary key.
     */
    public static DirectoryViewFilterCondition create( DirectoryViewFilterCondition directoryFilterCondition )
    {
        _dao.insert( directoryFilterCondition, _plugin );

        return directoryFilterCondition;
    }

    /**
     * Update of the directoryFilterCondition which is specified in parameter
     * 
     * @param directoryFilterCondition
     *            The instance of the DirectoryViewFilterCondition which contains the data to store
     * @return The instance of the directoryFilterCondition which has been updated
     */
    public static DirectoryViewFilterCondition update( DirectoryViewFilterCondition directoryFilterCondition )
    {
        _dao.store( directoryFilterCondition, _plugin );

        return directoryFilterCondition;
    }

    /**
     * Remove the directoryFilterCondition whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilterCondition Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a directoryFilterCondition whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilterCondition primary key
     * @return an instance of DirectoryViewFilterCondition
     */
    public static DirectoryViewFilterCondition findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryFilterCondition objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilterCondition objects
     */
    public static List<DirectoryViewFilterCondition> getDirectoryFilterConditionsList( )
    {
        return _dao.selectDirectoryFilterConditionsList( _plugin );
    }

    /**
     * Load the data of all the directoryFilterCondition objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilterCondition objects
     */
    public static List<DirectoryViewFilterCondition> getDirectoryFilterConditionsListByDirectoryFilter( int nIdDirectoryFilter )
    {
        return _dao.selectDirectoryFilterConditionsListByDirectoryFilter( nIdDirectoryFilter, _plugin );
    }

    /**
     * Load the id of all the directoryFilterCondition objects of a directory and returns them as a list
     * 
     * @return the list which contains the id of all the directoryFilterCondition objects
     */
    public static List<Integer> getIdDirectoryFilterConditionsList( )
    {
        return _dao.selectIdDirectoryFilterConditionsList( _plugin );
    }

    /**
     * Load the data of all the directoryFilterCondition objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the directoryFilterCondition objects
     */
    public static ReferenceList getDirectoryFilterConditionsReferenceList( )
    {
        return _dao.selectDirectoryFilterConditionsReferenceList( _plugin );
    }

    /**
     * Load the list of Condition types and return them as a referenceList
     * 
     * @return the referenceList which contains the data of all the condition types
     */
    public static ReferenceList getDirectoryFilterConditionTypesReferenceList( )
    {
        ReferenceList _conditionTypesReferenceList = new ReferenceList( );
        HashMap<String, String> _types = _dao.loadTypes( );

        Iterator it = _types.entrySet( ).iterator( );
        while ( it.hasNext( ) )
        {
            Map.Entry typ = (Map.Entry) it.next( );

            ReferenceItem _item = new ReferenceItem( );
            _item.setCode( (String) typ.getKey( ) );
            _item.setName( (String) typ.getValue( ) );
            _conditionTypesReferenceList.add( _item );
        }
        return _conditionTypesReferenceList;
    }

    /**
     * Load the list of operator types and return them as a referenceList
     * 
     * @return the referenceList which contains the operator types
     */
    public static ReferenceList getDirectoryFilterConditionOperatorsReferenceList( )
    {
        ReferenceList _conditionOperatorReferenceList = new ReferenceList( );
        HashMap<String, String> _operators = _dao.loadOperators( );

        Iterator it = _operators.entrySet( ).iterator( );
        while ( it.hasNext( ) )
        {
            Map.Entry op = (Map.Entry) it.next( );

            ReferenceItem _item = new ReferenceItem( );
            _item.setCode( (String) op.getKey( ) );
            _item.setName( (String) op.getValue( ) );
            _conditionOperatorReferenceList.add( _item );
        }
        return _conditionOperatorReferenceList;
    }
}
