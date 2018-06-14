/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnWorkflowState;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnWorkflowStateQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.DAOUtilMock;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordWorkflowStateNameConstants;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Test of the implementation of the IRecordColumnQueryPart for the Workflow State column
 */
public class RecordColumnWorkflowStateQueryPartTest extends LuteceTestCase
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
    }

    /**
     * Test for the {@link IRecordColumnQueryPart#getRecordColumnCell(fr.paris.lutece.util.sql.DAOUtil)}
     */
    public void testGetRecordColumnCellWorkflowState( )
    {
        String strWorkflowStateValueToRetrieve = "workflow state";
        DAOUtil daoUtil = new DAOUtilMock( StringUtils.EMPTY, RecordWorkflowStateNameConstants.COLUMN_WORKFLOW_STATE_NAME, strWorkflowStateValueToRetrieve );

        IRecordColumn recordColumn = new RecordColumnWorkflowState( 1, "Workflow State" );
        RecordColumnWorkflowStateQueryPart recordColumnWorkflowStateQueryPart = new RecordColumnWorkflowStateQueryPart( );
        recordColumnWorkflowStateQueryPart.setRecordColumn( recordColumn );

        RecordColumnCell recordColumnCell = recordColumnWorkflowStateQueryPart.getRecordColumnCell( daoUtil );
        assertThat( recordColumnCell, is( not( nullValue( ) ) ) );

        Map<String, Object> mapRecordColumnCellValues = recordColumnCell.getRecordColumnCellValues( );
        assertThat( mapRecordColumnCellValues, is( not( nullValue( ) ) ) );
        assertThat( mapRecordColumnCellValues.size( ), is( 1 ) );

        Object objDirectoryResult = recordColumnCell.getRecordColumnCellValueByName( RecordWorkflowStateNameConstants.COLUMN_WORKFLOW_STATE_NAME );
        assertThat( objDirectoryResult, is( not( nullValue( ) ) ) );
        assertThat( String.valueOf( objDirectoryResult ), is( strWorkflowStateValueToRetrieve ) );
    }

    /**
     * Test for the {@link IRecordColumnQueryPart#getRecordColumnCell(fr.paris.lutece.util.sql.DAOUtil)} using a column that doesn't exist
     */
    public void testGetRecordColumnCellWorkflowStateWithWrongColumnName( )
    {
        String strWorkflowStateValueToRetrieve = "workflow state";
        DAOUtil daoUtil = new DAOUtilMock( StringUtils.EMPTY, "colonne", strWorkflowStateValueToRetrieve );

        IRecordColumn recordColumn = new RecordColumnWorkflowState( 1, "Workflow State" );
        RecordColumnWorkflowStateQueryPart recordColumnWorkflowStateQueryPart = new RecordColumnWorkflowStateQueryPart( );
        recordColumnWorkflowStateQueryPart.setRecordColumn( recordColumn );

        try
        {
            recordColumnWorkflowStateQueryPart.getRecordColumnCell( daoUtil );
            fail( "Test fail : AppException hasn't been thrown !" );
        }
        catch( AppException exception )
        {

        }
    }
}
