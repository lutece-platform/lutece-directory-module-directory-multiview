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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCellComparator;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Comparator for DirectoryRecordItam object. The comparison is based on the value of objects with the given key
 */
public class DirectoryRecordItemComparator implements Comparator<DirectoryRecordItem>, Serializable
{
    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8569813504412874604L;
    
    // Constants
    private static final int SORT_ASCENDANT_DIRECTION = NumberUtils.INTEGER_ONE;
    private static final int SORT_DESCENDANT_DIRECTION = NumberUtils.INTEGER_MINUS_ONE;
    private static final String DEFAULT_CONFIGURATION_BEAN_NAME = "directory-multiview.directoryRecordItem.comparator.configuration.default";

    // Variables
    private final String _strSortAttributeName;
    private final int _nCellPosition;
    private final int _nSortDirection;

    /**
     * Constructor
     * 
     * @param directoryRecordItemComparatorConfig
     *            The DirectoryRecordItemComparatorConfig to use for sort the DirectoryRecordItem
     */
    public DirectoryRecordItemComparator( DirectoryRecordItemComparatorConfig directoryRecordItemComparatorConfig )
    {
        if ( directoryRecordItemComparatorConfig != null && directoryRecordItemComparatorConfig.getColumnToSortPosition( ) != NumberUtils.INTEGER_MINUS_ONE )
        {
            _strSortAttributeName = directoryRecordItemComparatorConfig.getSortAttributeName( );
            _nCellPosition = directoryRecordItemComparatorConfig.getColumnToSortPosition( );
            _nSortDirection = computeSortDirection( directoryRecordItemComparatorConfig.isAscSort( ) );
        }
        else
        {
            DirectoryRecordItemComparatorConfig directoryRecordItemComparatorConfigDefault = SpringContextService.getBean( DEFAULT_CONFIGURATION_BEAN_NAME );
            _strSortAttributeName = directoryRecordItemComparatorConfigDefault.getSortAttributeName( );
            _nCellPosition = directoryRecordItemComparatorConfigDefault.getColumnToSortPosition( );
            _nSortDirection = computeSortDirection( directoryRecordItemComparatorConfigDefault.isAscSort( ) );
        }
    }
    
    /**
     * Constructor
     * 
     * @param directoryRecordItemComparatorConfig
     *            The DirectoryRecordItemComparatorConfig to use for sort the DirectoryRecordItem
     * @param directoryRecordItemComparatorConfigDefault
     *          The DirectoryRecordItemComparatorConfig to use as default configuration if the given DirectoryRecordItemComparatorConfig doesn't have all
     *          the necessaries information
     */
    public DirectoryRecordItemComparator( DirectoryRecordItemComparatorConfig directoryRecordItemComparatorConfig, DirectoryRecordItemComparatorConfig directoryRecordItemComparatorConfigDefault )
    {
        if ( directoryRecordItemComparatorConfig != null && directoryRecordItemComparatorConfig.getColumnToSortPosition( ) != NumberUtils.INTEGER_MINUS_ONE )
        {
            _strSortAttributeName = directoryRecordItemComparatorConfig.getSortAttributeName( );
            _nCellPosition = directoryRecordItemComparatorConfig.getColumnToSortPosition( );
            _nSortDirection = computeSortDirection( directoryRecordItemComparatorConfig.isAscSort( ) );
        }
        else
        {
            _strSortAttributeName = directoryRecordItemComparatorConfigDefault.getSortAttributeName( );
            _nCellPosition = directoryRecordItemComparatorConfigDefault.getColumnToSortPosition( );
            _nSortDirection = computeSortDirection( directoryRecordItemComparatorConfigDefault.isAscSort( ) );
        }
    }

    /**
     * Compute the sort direction modifier from the given sort direction
     * 
     * @param bSortAsc
     *            The boolean which tell if we use the ascendant or descendant sort
     * @return 1 for the ascendant sort (which is the default) or -1 for the descendant sort
     */
    private int computeSortDirection( boolean bSortAsc )
    {
        int nSortDirection = SORT_ASCENDANT_DIRECTION;

        if ( !bSortAsc )
        {
            nSortDirection = SORT_DESCENDANT_DIRECTION;
        }

        return nSortDirection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare( DirectoryRecordItem directoryRecordItemOne, DirectoryRecordItem directoryRecordItemTwo )
    {
        int nComparisonResult = NumberUtils.INTEGER_ZERO;

        if ( directoryRecordItemOne == null )
        {
            if ( directoryRecordItemTwo != null )
            {
                nComparisonResult = NumberUtils.INTEGER_MINUS_ONE;
            }
        }
        else
        {
            if ( directoryRecordItemTwo == null )
            {
                nComparisonResult = NumberUtils.INTEGER_ONE;
            }
            else
            {
                List<RecordColumnCell> listRecordColumnCellOne = directoryRecordItemOne.getDirectoryRecordCellValues( );
                List<RecordColumnCell> listRecordColumnCellTwo = directoryRecordItemTwo.getDirectoryRecordCellValues( );

                nComparisonResult = compareRecordColumnCellList( listRecordColumnCellOne, listRecordColumnCellTwo );
            }
        }

        return ( _nSortDirection * nComparisonResult );
    }

    /**
     * Compare the RecordColumnCell list with the specified key
     * 
     * @param listRecordColumnCellOne
     *            The first list of RecordColumnCell to compare
     * @param listRecordColumnCellTwo
     *            The second list of RecordColumnCell to compare
     * @return the result of the comparison
     */
    private int compareRecordColumnCellList( List<RecordColumnCell> listRecordColumnCellOne, List<RecordColumnCell> listRecordColumnCellTwo )
    {
        int nRecordColumnComparisonResult = NumberUtils.INTEGER_ZERO;

        if ( listRecordColumnCellOne == null )
        {
            if ( listRecordColumnCellTwo != null )
            {
                nRecordColumnComparisonResult = NumberUtils.INTEGER_MINUS_ONE;
            }
        }
        else
        {
            if ( listRecordColumnCellTwo == null )
            {
                nRecordColumnComparisonResult = NumberUtils.INTEGER_ONE;
            }
            else
            {
                int nlistRecordColumnCellOneSize = listRecordColumnCellOne.size( );
                int nlistRecordColumnCellTwoSize = listRecordColumnCellTwo.size( );
                int nRecordColumnCellPosition = _nCellPosition - 1;

                if ( nlistRecordColumnCellOneSize == nlistRecordColumnCellTwoSize && nlistRecordColumnCellTwoSize > nRecordColumnCellPosition
                        && nRecordColumnCellPosition > -1 )
                {
                    RecordColumnCell recordColumnCellOne = listRecordColumnCellOne.get( nRecordColumnCellPosition );
                    RecordColumnCell recordColumnCellTwo = listRecordColumnCellTwo.get( nRecordColumnCellPosition );

                    RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( _strSortAttributeName );
                    nRecordColumnComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
                }
            }
        }

        return nRecordColumnComparisonResult;
    }
}
