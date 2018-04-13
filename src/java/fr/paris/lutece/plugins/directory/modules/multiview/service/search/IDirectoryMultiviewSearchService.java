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

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.portal.business.user.AdminUser;

/**
 * Interface for the search service for the module-directory-multiview
 */
public interface IDirectoryMultiviewSearchService
{
    // Constants
    String BEAN_NAME = "directory-multiview.directoryMultiviewSearchService";

    /**
     * Filter the list of DirectoryRecordItem by the record result obtains by the search of the given term
     * 
     * @param listDirectoryRecordItem
     *            The list of DirectoryRecordItem to filter
     * @param adminUser
     *            The adminUser who made the search
     * @param strSearchText
     *            The term to search on the record
     * @param locale
     *            The locale
     * @return the map which contains only RecordAssignement of record which contains entry with the given term as value
     */
    List<DirectoryRecordItem> filterBySearchedText( List<DirectoryRecordItem> listDirectoryRecordItem, AdminUser adminUser, String strSearchText, Locale locale );
}
