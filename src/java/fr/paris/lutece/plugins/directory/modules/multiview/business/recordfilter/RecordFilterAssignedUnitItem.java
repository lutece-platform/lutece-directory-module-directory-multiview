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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;

/**
 * Implementation of IRecordFilterItem for the Assigned Unit id filter
 */
public class RecordFilterAssignedUnitItem implements IRecordFilterItem
{
    // Variables
    private String _strAssignedUnitItemValue = StringUtils.EMPTY;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getItemValue( RecordAssignmentFilter filter )
    {
        return _strAssignedUnitItemValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemValue( RecordAssignmentFilter filter, Object objValue )
    {
        filter.getListAssignedUserId( ).clear( );

        if ( objValue == null )
        {
            setItemDefaultValue( filter );
        }
        else
        {
            _strAssignedUnitItemValue = String.valueOf( objValue );
            if ( _strAssignedUnitItemValue.contains( DirectoryMultiviewConstants.PREFIX_ADMIN_USER ) )
            {
                String strAssignedUserId = _strAssignedUnitItemValue.replace( DirectoryMultiviewConstants.PREFIX_ADMIN_USER, StringUtils.EMPTY );
                filter.getListAssignedUserId( ).add( NumberUtils.toInt( strAssignedUserId, DirectoryMultiviewConstants.DEFAULT_FILTER_VALUE ) );
                filter.setAssignedUnitId( DirectoryMultiviewConstants.DEFAULT_FILTER_VALUE );
            }
            else
            {
                String strAssignedUnit = _strAssignedUnitItemValue.replace( DirectoryMultiviewConstants.PREFIX_UNIT, StringUtils.EMPTY );
                filter.setAssignedUnitId( NumberUtils.toInt( strAssignedUnit, DirectoryMultiviewConstants.DEFAULT_FILTER_VALUE ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemDefaultValue( RecordAssignmentFilter filter )
    {
        filter.setAssignedUnitId( DirectoryMultiviewConstants.DEFAULT_FILTER_VALUE );
    }
}
