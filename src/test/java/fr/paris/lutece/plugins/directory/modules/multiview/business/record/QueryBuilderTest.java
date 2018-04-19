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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnEntryQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.RecordColumnDirectoryQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.RecordColumnRecordDateCreationQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.RecordColumnWorkflowStateQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.RecordPanelDirectoryInitializerQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.RecordFilterDirectoryQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.RecordFilterWorkflowStateQueryPartMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.RecordFilterDirectoryQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.RecordFilterEntryQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.RecordFilterWorkflowStateQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordDirectoryNameConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordWorkflowStateNameConstants;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test cases for the QueryBuilder methods
 */
public class QueryBuilderTest extends LuteceTestCase
{
    // Variables
    List<IRecordPanelInitializerQueryPart> _listRecordPanelInitializerQueryPart;
    List<IRecordColumnQueryPart> _listRecordColumnQueryPart;
    List<IRecordFilterQueryPart> _listRecordFilterQueryPart;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp( ) throws Exception
    {
        super.setUp( );

        // Reset the list in session
        _listRecordPanelInitializerQueryPart = new ArrayList<>( );
        _listRecordColumnQueryPart = new ArrayList<>( );
        _listRecordFilterQueryPart = new ArrayList<>( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown( ) throws Exception
    {
        super.tearDown( );
    }

    /**
     * Test for the {@link QueryBuilder#buildQuery(List, List)} method with only the RecordPanel. Without column the query built must be empty
     */
    public void testBuildQueryWithRecordPanelWithoutColumn( )
    {
        String strBasicQueryToFind = StringUtils.EMPTY;

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        checkQueryToBuilt( strBasicQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and the columnDirectory
     */
    public void testBuildQueryWithColumnDirectory( )
    {
        String strBasicQueryToFind = "SELECT id_directory, id_record, id_directory, title "
                + "FROM directory_directory AS directory INNER JOIN directory_record AS record ON record.id_directory = " + "directory.id_directory";

        _listRecordColumnQueryPart.add( new RecordColumnDirectoryQueryPartMock( ) );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        checkQueryToBuilt( strBasicQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and three columns: the Directory, WorkflowState and
     * RecordDateCreation columns
     */
    public void testBuildQueryWithColumnDirectoryWorkflowStateDate( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, id_directory, title, " + "workflow_state_name, record_date_creation FROM directory_directory "
                + "AS directory INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory LEFT JOIN "
                + "workflow_resource_workflow AS wf_resource_workflow ON wf_resource_workflow.id_resource = record.id_record LEFT JOIN "
                + "workflow_state AS ws_workflow_state ON ws_workflow_state.id_state = wf_resource_workflow.id_state";

        _listRecordColumnQueryPart.add( new RecordColumnDirectoryQueryPartMock( ) );
        _listRecordColumnQueryPart.add( new RecordColumnWorkflowStateQueryPartMock( ) );
        _listRecordColumnQueryPart.add( new RecordColumnRecordDateCreationQueryPartMock( ) );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and the ColumnDirectory and its filter with the good name for the
     * item
     */
    public void testBuildQueryWithDirectoryColumnWithFilter( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, id_directory, title "
                + "FROM directory_directory AS directory INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory "
                + "WHERE 1=1 AND ( directory.id_directory = 4 )";

        _listRecordColumnQueryPart.add( new RecordColumnDirectoryQueryPartMock( ) );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        RecordParameters recordFilterItemDirectory = new RecordParameters( );
        Map<String, Object> mapFilterNameValues = new LinkedHashMap<>( );
        mapFilterNameValues.put( RecordDirectoryNameConstants.FILTER_ID_DIRECTORY, 4 );
        recordFilterItemDirectory.setRecordParametersMap( mapFilterNameValues );

        RecordFilterDirectoryQueryPart recordFilterDirectoryQueryPart = new RecordFilterDirectoryQueryPartMock( );
        recordFilterDirectoryQueryPart.buildRecordFilterQuery( recordFilterItemDirectory );
        _listRecordFilterQueryPart.add( recordFilterDirectoryQueryPart );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and two columns and two filters
     */
    public void testBuildQueryWithTwoColumnsAndTwoFilters( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, id_directory, title, workflow_state_name FROM directory_directory AS "
                + "directory INNER JOIN directory_record AS record ON record.id_directory = "
                + "directory.id_directory LEFT JOIN workflow_resource_workflow AS wf_resource_workflow ON wf_resource_workflow.id_resource = record.id_record "
                + "LEFT JOIN workflow_state AS ws_workflow_state ON ws_workflow_state.id_state = wf_resource_workflow.id_state WHERE 1=1 AND "
                + "( directory.id_directory = 4 ) AND ( ws_workflow_state.id_state = 12 )";

        _listRecordColumnQueryPart.add( new RecordColumnDirectoryQueryPartMock( ) );
        _listRecordColumnQueryPart.add( new RecordColumnWorkflowStateQueryPartMock( ) );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        RecordParameters recordFilterItemDirectory = new RecordParameters( );
        Map<String, Object> mapFilterNameValuesDirectory = new LinkedHashMap<>( );
        mapFilterNameValuesDirectory.put( RecordDirectoryNameConstants.FILTER_ID_DIRECTORY, 4 );
        recordFilterItemDirectory.setRecordParametersMap( mapFilterNameValuesDirectory );

        RecordFilterDirectoryQueryPart recordFilterDirectoryQueryPart = new RecordFilterDirectoryQueryPartMock( );
        recordFilterDirectoryQueryPart.buildRecordFilterQuery( recordFilterItemDirectory );
        _listRecordFilterQueryPart.add( recordFilterDirectoryQueryPart );

        RecordParameters recordFilterItemWorkflowState = new RecordParameters( );
        Map<String, Object> mapFilterNameValuesWorkflowState = new LinkedHashMap<>( );
        mapFilterNameValuesWorkflowState.put( RecordWorkflowStateNameConstants.FILTER_ID_WORKFLOW_STATE, 12 );
        recordFilterItemWorkflowState.setRecordParametersMap( mapFilterNameValuesWorkflowState );

        RecordFilterWorkflowStateQueryPart recordFilterWorkflowStateQueryPart = new RecordFilterWorkflowStateQueryPartMock( );
        recordFilterWorkflowStateQueryPart.buildRecordFilterQuery( recordFilterItemWorkflowState );
        _listRecordFilterQueryPart.add( recordFilterWorkflowStateQueryPart );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and the ColumnDirectory and its filter with the item has no value.
     * In this case the condition is not added to the global query
     */
    public void testBuildQueryWithDirectoryColumnWithFilterWithoutName( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, id_directory, title "
                + "FROM directory_directory AS directory INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory " + "WHERE 1=1";

        _listRecordColumnQueryPart.add( new RecordColumnDirectoryQueryPartMock( ) );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        RecordFilterDirectoryQueryPart recordFilterDirectoryQueryPart = new RecordFilterDirectoryQueryPartMock( );
        recordFilterDirectoryQueryPart.buildRecordFilterQuery( new RecordParameters( ) );
        _listRecordFilterQueryPart.add( recordFilterDirectoryQueryPart );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and one ColumnEntryRecordField
     */
    public void testBuildQueryWithOneColumnEntryRecordField( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, column_3.column_3_value FROM directory_directory AS "
                + "directory INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory LEFT JOIN ( SELECT "
                + "record_3.id_record AS id_record_3, record_field_3.record_field_value AS column_3_value FROM directory_record_field AS "
                + "record_field_3 INNER JOIN directory_record AS record_3 ON record_field_3.id_record = record_3.id_record INNER JOIN "
                + "directory_entry AS entry_3 ON entry_3.id_entry = record_field_3.id_entry WHERE entry_3.title IN ( 'Nom', 'Prénom' ) ) "
                + "AS column_3 ON column_3.id_record_3 = record.id_record";

        IRecordColumn recordColumnEntryRecordField = new RecordColumnEntry( 3, "Colonne 3", Arrays.asList( "Nom", "Prénom" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPart = new RecordColumnEntryRecordFieldQueryPartMock( 3 );
        recordColumnEntryRecordFieldQueryPart.setRecordColumn( recordColumnEntryRecordField );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPart );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and three ColumnEntryRecordField
     */
    public void testBuildQueryWithThreeColumnEntryRecordFields( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, column_3.column_3_value, column_5.column_5_value, "
                + "column_7.column_7_value FROM directory_directory AS directory INNER JOIN directory_record AS record ON record.id_directory "
                + "= directory.id_directory LEFT JOIN ( SELECT record_3.id_record AS id_record_3, record_field_3.record_field_value AS "
                + "column_3_value FROM directory_record_field AS record_field_3 INNER JOIN directory_record AS record_3 ON record_field_3."
                + "id_record = record_3.id_record INNER JOIN directory_entry AS entry_3 ON entry_3.id_entry = record_field_3.id_entry WHERE "
                + "entry_3.title IN ( 'Nom', 'Prénom' ) ) AS column_3 ON column_3.id_record_3 = record.id_record LEFT JOIN ( SELECT record_5."
                + "id_record AS id_record_5, record_field_5.record_field_value AS column_5_value FROM directory_record_field AS record_field_5 "
                + "INNER JOIN directory_record AS record_5 ON record_field_5.id_record = record_5.id_record INNER JOIN directory_entry AS entry_5 "
                + "ON entry_5.id_entry = record_field_5.id_entry WHERE entry_5.title IN ( 'Date de naissance' ) ) AS column_5 ON "
                + "column_5.id_record_5 = record.id_record LEFT JOIN ( SELECT record_7.id_record AS id_record_7, record_field_7.record_field_value "
                + "AS column_7_value FROM directory_record_field AS record_field_7 INNER JOIN directory_record AS record_7 ON record_field_7."
                + "id_record = record_7.id_record INNER JOIN directory_entry AS entry_7 ON entry_7.id_entry = record_field_7.id_entry WHERE "
                + "entry_7.title IN ( 'Adresse', 'Téléphone' ) ) AS column_7 ON column_7.id_record_7 = record.id_record";

        IRecordColumn recordColumnEntryRecordFieldOne = new RecordColumnEntry( 3, "Colonne 3", Arrays.asList( "Nom", "Prénom" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPartOne = new RecordColumnEntryRecordFieldQueryPartMock( 3 );
        recordColumnEntryRecordFieldQueryPartOne.setRecordColumn( recordColumnEntryRecordFieldOne );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPartOne );

        IRecordColumn recordColumnEntryRecordFieldTwo = new RecordColumnEntry( 5, "Colonne 5", Arrays.asList( "Date de naissance" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPartTwo = new RecordColumnEntryRecordFieldQueryPartMock( 5 );
        recordColumnEntryRecordFieldQueryPartTwo.setRecordColumn( recordColumnEntryRecordFieldTwo );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPartTwo );

        IRecordColumn recordColumnEntryRecordFieldThree = new RecordColumnEntry( 7, "Colonne 7", Arrays.asList( "Adresse", "Téléphone" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPartThree = new RecordColumnEntryRecordFieldQueryPartMock( 7 );
        recordColumnEntryRecordFieldQueryPartThree.setRecordColumn( recordColumnEntryRecordFieldThree );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPartThree );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Test the for {@link QueryBuilder#buildQuery(List, List)} method with the RecordPanel and two ColumnEntryRecordField and a Filter on one
     */
    public void testBuildQueryWithColumnEntryRecordFieldsWithFilter( )
    {
        String strQueryToFind = "SELECT id_directory, id_record, column_3.column_3_value, column_5.column_5_value FROM "
                + "directory_directory AS directory INNER JOIN directory_record AS record ON record.id_directory = directory.id_directory "
                + "LEFT JOIN ( SELECT record_3.id_record AS id_record_3, record_field_3.record_field_value AS column_3_value FROM "
                + "directory_record_field AS record_field_3 INNER JOIN directory_record AS record_3 ON record_field_3.id_record = "
                + "record_3.id_record INNER JOIN directory_entry AS entry_3 ON entry_3.id_entry = record_field_3.id_entry WHERE entry_3.title "
                + "IN ( 'Nom', 'Prénom' ) ) AS column_3 ON column_3.id_record_3 = record.id_record LEFT JOIN ( SELECT record_5.id_record "
                + "AS id_record_5, record_field_5.record_field_value AS column_5_value FROM directory_record_field AS record_field_5 INNER "
                + "JOIN directory_record AS record_5 ON record_field_5.id_record = record_5.id_record INNER JOIN directory_entry AS entry_5 "
                + "ON entry_5.id_entry = record_field_5.id_entry WHERE entry_5.title IN ( 'Date de naissance' ) ) AS column_5 ON "
                + "column_5.id_record_5 = record.id_record WHERE 1=1 AND ( column_5.column_5_value = 'test colonne 5' )";

        IRecordColumn recordColumnEntryRecordFieldOne = new RecordColumnEntry( 3, "Colonne 3", Arrays.asList( "Nom", "Prénom" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPartOne = new RecordColumnEntryRecordFieldQueryPartMock( 3 );
        recordColumnEntryRecordFieldQueryPartOne.setRecordColumn( recordColumnEntryRecordFieldOne );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPartOne );

        IRecordColumn recordColumnEntryRecordFieldTwo = new RecordColumnEntry( 5, "Colonne 5", Arrays.asList( "Date de naissance" ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPartTwo = new RecordColumnEntryRecordFieldQueryPartMock( 5 );
        recordColumnEntryRecordFieldQueryPartTwo.setRecordColumn( recordColumnEntryRecordFieldTwo );
        _listRecordColumnQueryPart.add( recordColumnEntryRecordFieldQueryPartTwo );

        _listRecordPanelInitializerQueryPart.add( new RecordPanelDirectoryInitializerQueryPartMock( ) );

        RecordParameters recordFilterItemEntryRecordField = new RecordParameters( );
        Map<String, Object> mapFilterNameValues = new LinkedHashMap<>( );
        mapFilterNameValues.put( "column_5", "test colonne 5" );
        recordFilterItemEntryRecordField.setRecordParametersMap( mapFilterNameValues );

        IRecordFilterQueryPart recordFilterEntryRecordFieldQueryPart = new RecordFilterEntryQueryPart( );
        recordFilterEntryRecordFieldQueryPart.buildRecordFilterQuery( recordFilterItemEntryRecordField );
        _listRecordFilterQueryPart.add( recordFilterEntryRecordFieldQueryPart );

        checkQueryToBuilt( strQueryToFind );
    }

    /**
     * Build the query from the list of RecordColumnQueryPart and the list of RecordFilterQueryPart and make the test with the built query and the given
     * expected query
     * 
     * @param strQueryToFind
     *            The query to find
     */
    private void checkQueryToBuilt( String strQueryToFind )
    {
        String strQueryBuilt = QueryBuilder.buildQuery( _listRecordPanelInitializerQueryPart, _listRecordColumnQueryPart, _listRecordFilterQueryPart );
        assertThat( strQueryBuilt, is( not( nullValue( ) ) ) );
        assertThat( removeQuerySpaces( strQueryBuilt ), is( strQueryToFind ) );
    }

    /**
     * Remove all the unnecessary spaces of a query
     * 
     * @param strQuery
     *            The query to remove the spaces
     * @return the given query without all unnecessary spaces
     */
    private String removeQuerySpaces( String strQuery )
    {
        String strQueryResult = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strQuery ) )
        {
            strQueryResult = strQuery.trim( ).replaceAll( " +", " " );
            strQueryResult = strQueryResult.replaceAll( " +,", "," );
        }

        return strQueryResult;
    }
}
