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
package fr.paris.lutece.plugins.directory.modules.multiview.service;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserIdentityService
{
    private static final String BEAN_IDENTITY_SERVICE = "directory-multiview.identitystore.service";
    private static final String PROPERTY_APPLICATION_CODE = AppPropertiesService.getProperty( "identitystore.application.code" );
    private static final String PROPERTY_ENTRY_TITLE_GUID = AppPropertiesService.getProperty( "entry.guid.title" );

    /**
     * Get the identity service
     * 
     * @return the identity service
     */
    private static IdentityService getIdentityService( )
    {
        return SpringContextService.getBean( BEAN_IDENTITY_SERVICE );
    }

    /**
     * Get the user attributes from given GUID
     * 
     * @param strGuid
     *            the guid
     * @return the user attributes for given guid
     */
    public static Map<String, AttributeDto> getUserAttributes( String strGuid )
    {
        try
        {
            return getIdentityService( ).getIdentity( strGuid, "", PROPERTY_APPLICATION_CODE ).getAttributes( );
        }
        catch( AppException e )
        {
            AppLogService.error( "Unable to get the identity from identity service, with guid : " + strGuid );
            return new HashMap<>( );
        }
    }

    /**
     * Get the guid of the user
     * 
     * @param listEntries
     *            the list of entries
     * @param nIdRecord
     *            the id record
     * @param plugin
     *            the plugin
     * @return the guid of the user
     */
    public static String getUserGuid( List<IEntry> listEntries, int nIdRecord, Plugin plugin )
    {
        String strUserGuid = null;
        
        for ( IEntry entry : listEntries )
        {
            String strGuidEntryTitle = PROPERTY_ENTRY_TITLE_GUID;
            if ( entry.getTitle( ).equals( strGuidEntryTitle ) )
            {
                List<RecordField> listRecordFields = DirectoryUtils.getListRecordField( entry, nIdRecord, plugin );
                if ( !listRecordFields.isEmpty( ) )
                {
                    return listRecordFields.get( 0 ).toString( );
                }

            }
            else
            {
                List<IEntry> listChildrenEntry = entry.getChildren( );
                if ( listChildrenEntry != null && !listChildrenEntry.isEmpty( ) )
                {
                    strUserGuid = getUserGuid( listChildrenEntry, nIdRecord, plugin );
                    if ( strUserGuid != null )
                    {
                        break;
                    }
                }
            }
        }
        
        return strUserGuid;
    }
}
