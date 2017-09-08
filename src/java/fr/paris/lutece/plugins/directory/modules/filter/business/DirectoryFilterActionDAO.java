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

package fr.paris.lutece.plugins.directory.modules.filter.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for DirectoryFilterAction objects
 */
public final class DirectoryFilterActionDAO implements IDirectoryFilterActionDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_directory_filter_action ) FROM directory_filter_action";
    private static final String SQL_QUERY_SELECT = "SELECT id_directory_filter_action, id_directory_filter, id_action, position, style, nb_item FROM directory_filter_action WHERE id_directory_filter_action = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directory_filter_action ( id_directory_filter_action, id_directory_filter, id_action, position, style, nb_item ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directory_filter_action WHERE id_directory_filter_action = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directory_filter_action SET id_directory_filter_action = ?, id_directory_filter = ?, id_action = ?, position = ?, style = ?, nb_item = ? WHERE id_directory_filter_action = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_directory_filter_action, id_directory_filter, id_action, position, style, nb_item FROM directory_filter_action";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_directory_filter_action FROM directory_filter_action";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );
        int nKey = 1;

        if( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DirectoryFilterAction directoryFilterAction, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        directoryFilterAction.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , directoryFilterAction.getId( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getIdDirectoryFilter( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getIdAction( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getPosition( ) );
        daoUtil.setString( nIndex++ , directoryFilterAction.getStyle( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getNbItem( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DirectoryFilterAction load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        DirectoryFilterAction directoryFilterAction = null;

        if ( daoUtil.next( ) )
        {
            directoryFilterAction = new DirectoryFilterAction();
            int nIndex = 1;
            
            directoryFilterAction.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setIdDirectoryFilter( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setIdAction( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setPosition( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setStyle( daoUtil.getString( nIndex++ ) );
            directoryFilterAction.setNbItem( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return directoryFilterAction;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DirectoryFilterAction directoryFilterAction, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , directoryFilterAction.getId( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getIdDirectoryFilter( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getIdAction( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getPosition( ) );
        daoUtil.setString( nIndex++ , directoryFilterAction.getStyle( ) );
        daoUtil.setInt( nIndex++ , directoryFilterAction.getNbItem( ) );
        daoUtil.setInt( nIndex , directoryFilterAction.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryFilterAction> selectDirectoryFilterActionsList( Plugin plugin )
    {
        List<DirectoryFilterAction> directoryFilterActionList = new ArrayList<DirectoryFilterAction>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DirectoryFilterAction directoryFilterAction = new DirectoryFilterAction(  );
            int nIndex = 1;
            
            directoryFilterAction.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setIdDirectoryFilter( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setIdAction( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setPosition( daoUtil.getInt( nIndex++ ) );
            directoryFilterAction.setStyle( daoUtil.getString( nIndex++ ) );
            directoryFilterAction.setNbItem( daoUtil.getInt( nIndex++ ) );

            directoryFilterActionList.add( directoryFilterAction );
        }

        daoUtil.free( );
        return directoryFilterActionList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoryFilterActionsList( Plugin plugin )
    {
        List<Integer> directoryFilterActionList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            directoryFilterActionList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return directoryFilterActionList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoryFilterActionsReferenceList( Plugin plugin )
    {
        ReferenceList directoryFilterActionList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            directoryFilterActionList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return directoryFilterActionList;
    }
}