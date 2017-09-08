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
 * This class provides Data Access methods for DirectoryFilter objects
 */
public final class DirectoryFilterDAO implements IDirectoryFilterDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_directory_filter ) FROM directory_filter";
    private static final String SQL_QUERY_SELECT = "SELECT id_directory_filter, name, style, position FROM directory_filter WHERE id_directory_filter = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directory_filter ( id_directory_filter, name, style, position ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directory_filter WHERE id_directory_filter = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directory_filter SET id_directory_filter = ?, name = ?, style = ?, position = ? WHERE id_directory_filter = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_directory_filter, name, style, position FROM directory_filter";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_directory_filter FROM directory_filter";

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
    public void insert( DirectoryFilter directoryFilter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        directoryFilter.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , directoryFilter.getId( ) );
        daoUtil.setString( nIndex++ , directoryFilter.getName( ) );
        daoUtil.setString( nIndex++ , directoryFilter.getStyle( ) );
        daoUtil.setInt( nIndex++ , directoryFilter.getPosition( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DirectoryFilter load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        DirectoryFilter directoryFilter = null;

        if ( daoUtil.next( ) )
        {
            directoryFilter = new DirectoryFilter();
            int nIndex = 1;
            
            directoryFilter.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilter.setName( daoUtil.getString( nIndex++ ) );
            directoryFilter.setStyle( daoUtil.getString( nIndex++ ) );
            directoryFilter.setPosition( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return directoryFilter;
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
    public void store( DirectoryFilter directoryFilter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , directoryFilter.getId( ) );
        daoUtil.setString( nIndex++ , directoryFilter.getName( ) );
        daoUtil.setString( nIndex++ , directoryFilter.getStyle( ) );
        daoUtil.setInt( nIndex++ , directoryFilter.getPosition( ) );
        daoUtil.setInt( nIndex , directoryFilter.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DirectoryFilter> selectDirectoryFiltersList( Plugin plugin )
    {
        List<DirectoryFilter> directoryFilterList = new ArrayList<DirectoryFilter>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DirectoryFilter directoryFilter = new DirectoryFilter(  );
            int nIndex = 1;
            
            directoryFilter.setId( daoUtil.getInt( nIndex++ ) );
            directoryFilter.setName( daoUtil.getString( nIndex++ ) );
            directoryFilter.setStyle( daoUtil.getString( nIndex++ ) );
            directoryFilter.setPosition( daoUtil.getInt( nIndex++ ) );

            directoryFilterList.add( directoryFilter );
        }

        daoUtil.free( );
        return directoryFilterList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDirectoryFiltersList( Plugin plugin )
    {
        List<Integer> directoryFilterList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            directoryFilterList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return directoryFilterList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDirectoryFiltersReferenceList( Plugin plugin )
    {
        ReferenceList directoryFilterList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            directoryFilterList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return directoryFilterList;
    }
}