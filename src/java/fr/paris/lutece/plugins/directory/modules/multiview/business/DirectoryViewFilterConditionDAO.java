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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for DirectoryViewFilterCondition objects
 */
public final class DirectoryViewFilterConditionDAO implements IDirectoryViewFilterConditionDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_directory_filter_condition ) FROM directory_filter_condition";
    private static final String SQL_QUERY_SELECT = "SELECT id_directory_filter_condition, id_directory_filter, id_entry, operator, filter_type FROM directory_filter_condition WHERE id_directory_filter_condition = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directory_filter_condition ( id_directory_filter_condition, id_directory_filter, id_entry, operator, filter_type ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directory_filter_condition WHERE id_directory_filter_condition = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directory_filter_condition SET  id_directory_filter = ?, id_entry = ?, operator = ?, filter_type = ? WHERE id_directory_filter_condition = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_directory_filter_condition, id_directory_filter, id_entry, operator, filter_type FROM directory_filter_condition";
    private static final String SQL_QUERY_SELECTALL_BY_DIRECTORY_FILTER = "SELECT id_directory_filter_condition, id_directory_filter, id_entry, operator, filter_type FROM directory_filter_condition  WHERE id_directory_filter = ? ";

    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_directory_filter_condition FROM directory_filter_condition";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );
        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DirectoryViewFilterCondition directoryFilterCondition, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        directoryFilterCondition.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, directoryFilterCondition.getId( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getIdDirectoryFilter( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getIdEntry( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getOperator( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getFilterType( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DirectoryViewFilterCondition load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        DirectoryViewFilterCondition directoryFilterCondition = null;

        if ( daoUtil.next( ) )
        {
            directoryFilterCondition = new DirectoryViewFilterCondition( );
            int nIndex = 1;

            directoryFilterCondition.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdDirectoryFilter( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdEntry( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setOperator( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setFilterType( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return directoryFilterCondition;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DirectoryViewFilterCondition directoryFilterCondition, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        // values
        daoUtil.setInt( nIndex++, directoryFilterCondition.getIdDirectoryFilter( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getIdEntry( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getOperator( ) );
        daoUtil.setInt( nIndex++, directoryFilterCondition.getFilterType( ) );

        // id
        daoUtil.setInt( nIndex, directoryFilterCondition.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryViewFilterCondition> selectDirectoryFilterConditionsList( Plugin plugin )
    {
        List<DirectoryViewFilterCondition> directoryFilterConditionList = new ArrayList<DirectoryViewFilterCondition>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            DirectoryViewFilterCondition directoryFilterCondition = new DirectoryViewFilterCondition( );
            int nIndex = 1;

            directoryFilterCondition.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdDirectoryFilter( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdEntry( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setOperator( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setFilterType( daoUtil.getInt( nIndex++ ) );

            directoryFilterConditionList.add( directoryFilterCondition );
        }

        daoUtil.free( );
        return directoryFilterConditionList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryViewFilterCondition> selectDirectoryFilterConditionsListByDirectoryFilter( int nIdDirectoryFilter, Plugin plugin )
    {
        List<DirectoryViewFilterCondition> directoryFilterConditionList = new ArrayList<DirectoryViewFilterCondition>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_DIRECTORY_FILTER, plugin );
        daoUtil.setInt( 1, nIdDirectoryFilter );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            DirectoryViewFilterCondition directoryFilterCondition = new DirectoryViewFilterCondition( );
            int nIndex = 1;

            directoryFilterCondition.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdDirectoryFilter( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setIdEntry( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setOperator( daoUtil.getInt( nIndex++ ) );
            directoryFilterCondition.setFilterType( daoUtil.getInt( nIndex++ ) );

            directoryFilterConditionList.add( directoryFilterCondition );
        }

        daoUtil.free( );
        return directoryFilterConditionList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoryFilterConditionsList( Plugin plugin )
    {
        List<Integer> directoryFilterConditionList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryFilterConditionList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return directoryFilterConditionList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoryFilterConditionsReferenceList( Plugin plugin )
    {
        ReferenceList directoryFilterConditionList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            directoryFilterConditionList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return directoryFilterConditionList;
    }
}
