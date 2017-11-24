/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.service.search;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class DirectoryMultiviewSearchService
{
    public static LinkedHashMap<String, RecordAssignment> filterBySearchedText( LinkedHashMap<String, RecordAssignment> mapRecordAssignment, Collection<Directory> listDirectories , HttpServletRequest request, Plugin plugin, String strSearchText )
    {
        
        //No search text
        if ( StringUtils.isBlank( strSearchText ) )
        {
            return mapRecordAssignment;
        }
        else
        {
            LinkedHashMap<String, RecordAssignment> mapReturn = new LinkedHashMap<>(mapRecordAssignment);
            List<String> listIdRecord = new ArrayList<>( );
            
            //For each directory, compute the plain text search;
            for ( Directory directory : listDirectories )
            {
                //Set the operator OR for search
                directory.setSearchOperatorOr( true );
                
                //Compute the search fields
                DirectoryAdminSearchFields searchFields = new DirectoryAdminSearchFields( );
                EntryFilter filter = new EntryFilter( );
                filter.setIdDirectory( directory.getIdDirectory( ) );
                filter.setIsIndexedAsSummary( 1 );
                List<IEntry> listEntry = DirectoryUtils.getFormEntriesByFilter( filter, plugin );
                
                //Set id directory
                searchFields.setIdDirectory( directory.getIdDirectory( ) );
                
                //Set the queries map
                HashMap<String, List<RecordField> > mapSearchRecordField = new HashMap<>();
                for ( IEntry entry : listEntry )
                {
                    List<RecordField> listRecordFields = new ArrayList<>();
                    RecordField recordField = new RecordField( );
                    recordField.setEntry( entry );
                    recordField.setValue( strSearchText );
                    listRecordFields.add( recordField );
                    mapSearchRecordField.put( Integer.toString( entry.getIdEntry( ) ), listRecordFields );
                }
                searchFields.setMapQuery( mapSearchRecordField );
                
                //Compute the search
                listIdRecord.addAll( DirectoryUtils.getListResults( request, directory, true, true, searchFields , AdminUserService.getAdminUser( request ), request.getLocale( ) )
                        .stream()
                        .map( nId -> nId.toString() )
                        .collect( Collectors.toList( ) ) );
            }
            
            mapReturn.keySet().retainAll( listIdRecord );
            return mapReturn;
        }
    }
}
