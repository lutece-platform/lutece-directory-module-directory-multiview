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
package fr.paris.lutece.plugins.directory.modules.multiview.service.search;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Interface for the search service for the module-directory-multiview
 */
public interface IDirectoryMultiviewSearchService
{
    // Constants
    String BEAN_NAME = "directory-multiview.directoryMultiviewSearchService";

    /**
     * Filter the map of record assignment by the record result obtains by the search of the given term for the given list of directories
     * 
     * @param mapRecordAssignment
     *            The map to filter
     * @param listDirectories
     *            The list of directories to retrieve the record result from
     * @param adminUser
     *            The adminUser who made the search
     * @param plugin
     *            The plugin to use
     * @param strSearchText
     *            The term to search on the record
     * @param locale
     *            The locale
     * @return the map which contains only RecordAssignement of record which contains entry with the given term as value
     */
    Map<String, RecordAssignment> filterBySearchedText( Map<String, RecordAssignment> mapRecordAssignment, Collection<Directory> listDirectories,
            AdminUser adminUser, Plugin plugin, String strSearchText, Locale locale );
}
