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
package fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordFieldItem;

/**
 * Implementation of IRecordFilterParameter for the Customized Column filter
 */
public class RecordFilterCustomizedColumnItem implements IRecordFilterItem
{
    // Constants
    private static final String DEFAULT_ITEM_VALUE = StringUtils.EMPTY;
    
    // Variables
    private final int _nCustomizedColumnNumber;
    
    /**
     * Constructor
     * 
     * @param nCustomizedColumnNumber
     *          The CustomizedColumn number
     */
    public RecordFilterCustomizedColumnItem( int nCustomizedColumnNumber )
    {
        _nCustomizedColumnNumber = nCustomizedColumnNumber;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItemValue( RecordAssignmentFilter filter )
    {
        String strItemValue = DEFAULT_ITEM_VALUE;
        
        RecordFieldItem recordFieldItem = retrieveRecordFieldItemFromFilter( filter );
        if ( recordFieldItem != null )
        {
            strItemValue = recordFieldItem.getRecordFieldValue( );
        }
        
        return strItemValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemValue( RecordAssignmentFilter filter, Object objValue )
    {
        if ( objValue == null )
        {
            setItemDefaultValue( filter );
        }
        else
        {
            setRecordFieldItemValue( filter, String.valueOf( objValue ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemDefaultValue( RecordAssignmentFilter filter )
    {
        setRecordFieldItemValue( filter, DEFAULT_ITEM_VALUE );
    }

    /**
     * Return the RecordFieldItem from the given filter
     * 
     * @param filter
     *          The filter to retrieve the RecordFilterItem from
     * @return the RecordFilterItem associated to the current CustomiedColumnItem
     */
    private RecordFieldItem retrieveRecordFieldItemFromFilter( RecordAssignmentFilter filter )
    {
        RecordFieldItem recordFieldItemResult = null;
        
        List<RecordFieldItem> listRecordFieldItem = filter.getListRecordFieldItem( );
        for ( RecordFieldItem recordFieldItem : listRecordFieldItem )
        {
            if ( recordFieldItem.getItemNumber( ) == _nCustomizedColumnNumber )
            {
                recordFieldItemResult = recordFieldItem;
                break;
            }
        }
        
        return recordFieldItemResult;
    }
    
    /**
     * Set the given value to the RecordFieldItem in the given filter
     * 
     * @param filter
     *          The filter to retrieve the RecordFiedItem to set the value on
     * @param strRecordFieldValue
     *          The value to set
     */
    private void setRecordFieldItemValue( RecordAssignmentFilter filter, String strRecordFieldValue )
    {
        RecordFieldItem recordFieldItem = retrieveRecordFieldItemFromFilter( filter );
        if ( recordFieldItem != null )
        {
            recordFieldItem.setRecordFieldValue( strRecordFieldValue );
        }        
    }
}
