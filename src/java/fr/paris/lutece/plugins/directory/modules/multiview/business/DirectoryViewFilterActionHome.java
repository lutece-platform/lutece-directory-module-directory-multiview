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
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DirectoryViewFilterAction objects
 */
public final class DirectoryViewFilterActionHome
{
    // Static variable pointed at the DAO instance
    private static IDirectoryViewFilterActionDAO _dao = SpringContextService.getBean( "directory-multiview.directoryViewFilterActionDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "directory-multiview" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DirectoryViewFilterActionHome( )
    {
    }

    /**
     * Create an instance of the directoryFilterAction class
     * 
     * @param directoryFilterAction
     *            The instance of the DirectoryViewFilterAction which contains the informations to store
     * @return The instance of directoryFilterAction which has been created with its primary key.
     */
    public static DirectoryViewFilterAction create( DirectoryViewFilterAction directoryFilterAction )
    {
        _dao.insert( directoryFilterAction, _plugin );

        return directoryFilterAction;
    }

    /**
     * Update of the directoryFilterAction which is specified in parameter
     * 
     * @param directoryFilterAction
     *            The instance of the DirectoryViewFilterAction which contains the data to store
     * @return The instance of the directoryFilterAction which has been updated
     */
    public static DirectoryViewFilterAction update( DirectoryViewFilterAction directoryFilterAction )
    {
        _dao.store( directoryFilterAction, _plugin );

        return directoryFilterAction;
    }

    /**
     * Remove the directoryFilterAction whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilterAction Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a directoryFilterAction whose identifier is specified in parameter
     * 
     * @param nKey
     *            The directoryFilterAction primary key
     * @return an instance of DirectoryViewFilterAction
     */
    public static DirectoryViewFilterAction findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the directoryFilterAction objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilterAction objects
     */
    public static List<DirectoryViewFilterAction> getDirectoryFilterActionsList( )
    {
        return _dao.selectDirectoryFilterActionsList( _plugin );
    }

    /**
     * Load the data of all the directoryFilterAction objects and returns them as a list
     * 
     * @return the list which contains the data of all the directoryFilterAction objects
     */
    public static List<DirectoryViewFilterAction> getDirectoryFilterActionsListByDirectoryFilter( int nIdDirectoryFilter )
    {
        return _dao.selectDirectoryFilterActionsListByDirectoryFilter( nIdDirectoryFilter, _plugin );
    }

    /**
     * Load the id of all the directoryFilterAction objects and returns them as a list
     * 
     * @return the list which contains the id of all the directoryFilterAction objects
     */
    public static List<Integer> getIdDirectoryFilterActionsList( )
    {
        return _dao.selectIdDirectoryFilterActionsList( _plugin );
    }

    /**
     * Load the data of all the directoryFilterAction objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the directoryFilterAction objects
     */
    public static ReferenceList getDirectoryFilterActionsReferenceList( )
    {
        return _dao.selectDirectoryFilterActionsReferenceList( _plugin );
    }
}
