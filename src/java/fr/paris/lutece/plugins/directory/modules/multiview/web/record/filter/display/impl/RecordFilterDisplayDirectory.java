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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.impl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewPlugin;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordFilterColumnNameConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.util.ReferenceList;

/**
 * Implementation of the IRecordFilterDisplay interface for the filter on directory
 */
public class RecordFilterDisplayDirectory extends AbstractRecordFilterDisplay
{
    // Constants
    private static final String PARAMETER_ID_DIRECTORY = "multiview_id_directory";
    private static final String DIRECTORY_CODE_ATTRIBUTE = "idDirectory";
    private static final String DIRECTORY_NAME_ATTRIBUTE = "title";
    private static final String DEFAULT_ID_DIRECTORY = "-1";

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getFilterDisplayMapValues( HttpServletRequest request )
    {
        String strIdDirectoryValue = DEFAULT_ID_DIRECTORY;
        Map<String, Object> mapFilterNameValues = new LinkedHashMap<>( );

        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        if ( StringUtils.isNotBlank( strIdDirectory ) )
        {
            mapFilterNameValues.put( RecordFilterColumnNameConstants.FILTER_ID_DIRECTORY, strIdDirectory );
            strIdDirectoryValue = strIdDirectory;
        }

        setValue( strIdDirectoryValue );

        return mapFilterNameValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildTemplate( HttpServletRequest request )
    {
        manageFilterTemplate( request, createReferenceList( ), PARAMETER_ID_DIRECTORY );
    }

    /**
     * Create the ReferenceList of the available directories on which we can filter
     * 
     * @return the ReferenceList of Directory on which we can filter
     */
    private ReferenceList createReferenceList( )
    {
        ReferenceListFactory referenceListFactory = new ReferenceListFactory( getDirectoryList( ), DIRECTORY_CODE_ATTRIBUTE, DIRECTORY_NAME_ATTRIBUTE );

        return referenceListFactory.createReferenceList( );
    }

    /**
     * Return the list of all Directory associated to a workflow
     * 
     * @return the list of all Directory associated to a workflow
     */
    private List<Directory> getDirectoryList( )
    {
        DirectoryFilter filter = new DirectoryFilter( );

        List<Directory> listDirectory = DirectoryHome.getDirectoryList( filter, DirectoryMultiviewPlugin.getPlugin( ) );

        if ( listDirectory != null && !listDirectory.isEmpty( ) )
        {
            listDirectory.sort( Comparator.comparing( Directory::getTitle, Comparator.nullsLast( Comparator.naturalOrder( ) ) ) );
        }

        return listDirectory;
    }
}
