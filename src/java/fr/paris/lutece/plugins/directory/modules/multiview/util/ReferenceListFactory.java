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
package fr.paris.lutece.plugins.directory.modules.multiview.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * Class which contains necessaries informations for creating a ReferenceList
 */
public class ReferenceListFactory
{
    private Collection<?> _collectionItem;
    private String _strCodeAttr;
    private String _strNameAttribute;
    private boolean _bNumerical = Boolean.TRUE;
    private String _strDefaultCode = DirectoryMultiviewConstants.REFERENCE_ITEM_DEFAULT_CODE;
    private String _strDefaultName = DirectoryMultiviewConstants.REFERENCE_ITEM_DEFAULT_NAME;

    // Constructor
    public ReferenceListFactory( Collection<?> collectionItem, String strCodeAttr, String strNameAttribute )
    {
        _collectionItem = collectionItem;
        _strCodeAttr = strCodeAttr;
        _strNameAttribute = strNameAttribute;
    }

    // Constructor
    public ReferenceListFactory( Collection<?> collectionItem, String strCodeAttr, String strNameAttribute, boolean bNumerical )
    {
        _collectionItem = collectionItem;
        _strCodeAttr = strCodeAttr;
        _strNameAttribute = strNameAttribute;
        _bNumerical = bNumerical;
    }

    /**
     * Create the ReferenceList with the parameter values
     * 
     * @return the ReferenceList created with the parameters values
     */
    public ReferenceList createReferenceList( )
    {
        ReferenceList referenceListResult = new ReferenceList( );

        // Add the default ReferenceItem if necessary
        referenceListResult.addItem( _strDefaultCode, _strDefaultName );

        if ( _collectionItem != null && !_collectionItem.isEmpty( ) )
        {
            ReferenceList referenceList = ReferenceList.convert( _collectionItem, _strCodeAttr, _strNameAttribute, _bNumerical );

            if ( referenceList != null && !referenceList.isEmpty( ) )
            {
                referenceListResult.addAll( filterDuplicatesReferenceItem( referenceList ) );
            }
        }

        return referenceListResult;
    }

    /**
     * Remove the duplicates of referenceItem. Two ReferenceItems are considered as duplicates if they have the same code value.
     * 
     * @param referenceList
     *            The ReferenceList to remove the duplicates from
     */
    private ReferenceList filterDuplicatesReferenceItem( ReferenceList referenceList )
    {
        ReferenceList referenceListCleaned = new ReferenceList( );
        Set<String> setCodeProcessed = new LinkedHashSet<>( );

        for ( ReferenceItem referenceItem : referenceList )
        {
            String strCurrentCode = referenceItem.getCode( );

            if ( !setCodeProcessed.contains( strCurrentCode ) )
            {
                setCodeProcessed.add( strCurrentCode );
                referenceListCleaned.add( referenceItem );
            }
        }

        return referenceListCleaned;
    }

    /**
     * Set the default name of the default ReferenceItem
     * 
     * @param strDefaultName
     *            The strDefaultName to set
     */
    public void setDefaultName( String strDefaultName )
    {
        _strDefaultName = strDefaultName;
    }
}