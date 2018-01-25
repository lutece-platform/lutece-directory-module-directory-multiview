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
package fr.paris.lutece.plugins.directory.modules.multiview.business.customizedcolumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * Representation of a Column object which compose a FilterItem
 */
public class CustomizedColumn
{
    // Pattern
    private static final String PATTERN_CUSTOMIZED_COLUMN_NAME = "customized_column_%s";
    private static final String PATTERN_CUSTOMIZED_COLUMN_TITLE = "module.directory.multiview.records_list.customized_column_%s";
    
    // Variables
    private final int _nCustomizedColumnNumber;
    private final boolean _bFilter;
    private final String _strCustomizedColumnName;
    private final String _strCustomizedColumnTitle;
    private final List<IEntry> _listEntryCustomizedColumn;
    
    /**
     * Constructor
     * 
     * @param nCustomizedColumnNumber
     *          The number of the column of the CustomizedColumn
     * @param bFilter
     *          The boolean which tell if the CustomizedColumn can be filtered or not
     * @param locale
     *          The Locale for retrieveing the title of the column
     */
    public CustomizedColumn( int nCustomizedColumnNumber, boolean bFilter, Locale locale )
    {
        _nCustomizedColumnNumber = nCustomizedColumnNumber;
        _bFilter = bFilter;
        _listEntryCustomizedColumn = new ArrayList<>( );
        
        String strCustomizedColumnTitleKey = String.format( PATTERN_CUSTOMIZED_COLUMN_TITLE, nCustomizedColumnNumber );
         _strCustomizedColumnTitle = I18nService.getLocalizedString( strCustomizedColumnTitleKey, locale );
                 
        _strCustomizedColumnName = String.format( PATTERN_CUSTOMIZED_COLUMN_NAME, nCustomizedColumnNumber );
    }
    
    /**
     * Return the Customized Column number
     * 
     * @return the nCustomizedColumnNumber
     */
    public int getCustomizedColumnNumber( )
    {
        return _nCustomizedColumnNumber;
    }
    
    /**
     * Return the boolean which tell if the CustomizedColumn can be filtered or not
     * 
     * @return the bFilter
     */
    public boolean isFilterAuthorized( )
    {
        return _bFilter;
    }

    /**
     * Return the name of the CustomizedColumn
     * 
     * @return the strCustomizedColumnName
     */
    public String getCustomizedColumnName( )
    {
        return _strCustomizedColumnName;
    }

    /**
     * Return the title of the CustomizedColumn
     * 
     * @return the strCustomizedColumnTitle
     */
    public String getCustomizedColumnTitle( )
    {
        return _strCustomizedColumnTitle;
    }

    /**
     * Return the list of Entry which composed the Customized Column
     * 
     * @return the listEntryCustomizedColumn
     */
    public List<IEntry> getListEntryCustomizedColumn( )
    {
        return _listEntryCustomizedColumn;
    }

    /**
     * Add a list of Entry to the CustomizedColumn list
     * 
     * @param listEntry
     *          The list of Entry to ad to the CustomizedColumn list
     */
    public void addColumnListEntry( List<IEntry> listEntry )
    {
        _listEntryCustomizedColumn.addAll( listEntry );
    }
}
