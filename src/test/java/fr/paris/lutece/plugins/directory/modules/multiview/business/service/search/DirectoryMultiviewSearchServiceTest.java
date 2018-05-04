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
package fr.paris.lutece.plugins.directory.modules.multiview.business.service.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl.RecordPanelRecords;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.DirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.IDirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.service.directorysearch.DirectorySearchItem;
import fr.paris.lutece.plugins.lucene.service.analyzer.LuteceFrenchAnalyzer;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test class for the DirectoryMultiviewSearchService service implementation
 */
public class DirectoryMultiviewSearchServiceTest extends LuteceTestCase
{
    // Variables
    private IRecordPanel _recordPanel;
    private IndexSearcher _indexSearcher;
    private Analyzer _analyzer;
    private Directory _directory;
    private IndexWriter _indexWriter;
    private static final List<Integer> _listPanelIdRecord = Arrays.asList( 1, 2, 3, 4, 5 );
    private List<Entry<String, String>> _listStoredContent;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _recordPanel = createRecordPanel( );

        populateStoredContents( );
        createIndexForTest( );

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
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * without directory and analyzer
     */
    public void testFilterBySearchedTextWithoutDirectoryAndAnalyzer( )
    {
        try
        {
            @SuppressWarnings( "unused" )
            IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( null, null );

            fail( "The AppException hasn't been throwned !" );
        }
        catch( AppException exception )
        {
            // Nothing to do
        }
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * without directory
     */
    public void testFilterBySearchedTextWithoutDirectory( )
    {
        try
        {
            @SuppressWarnings( "unused" )
            IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( null, _analyzer );

            fail( "The AppException hasn't been throwned for the missing Directory !" );
        }
        catch( AppException exception )
        {
            // Nothing to do
        }
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * without analyzer
     */
    public void testFilterBySearchedTextWithoutAnalyzer( )
    {
        try
        {
            @SuppressWarnings( "unused" )
            IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, null );

            fail( "The AppException hasn't been throwned for the missing Analyzer !" );
        }
        catch( AppException exception )
        {
            // Nothing to do
        }
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * with a null text to search
     */
    public void testFilterBySearchedTextWithNullTextSearch( )
    {
        String strSearchText = null;

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( _listPanelIdRecord.size( ) ) );
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * with an empty text to search
     */
    public void testFilterBySearchedTextWithEmptyTextSearch( )
    {
        String strSearchText = StringUtils.EMPTY;

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( _listPanelIdRecord.size( ) ) );
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     */
    public void testFilterBySearchedText( )
    {
        String strSearchText = "test";

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( NumberUtils.INTEGER_ONE ) );

        DirectoryRecordItem directoryRecordItem = listDirectoryRecordItem.get( NumberUtils.INTEGER_ZERO );
        assertThat( directoryRecordItem.getIdRecord( ), is( 1 ) );
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * with the search which returns several results
     */
    public void testFilterBySearchedTextTwoResult( )
    {
        String strSearchText = "title";

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( 2 ) );

        DirectoryRecordItem directoryRecordItemOne = listDirectoryRecordItem.get( NumberUtils.INTEGER_ZERO );
        assertThat( directoryRecordItemOne.getIdRecord( ), is( 1 ) );

        DirectoryRecordItem directoryRecordItemFive = listDirectoryRecordItem.get( NumberUtils.INTEGER_ONE );
        assertThat( directoryRecordItemFive.getIdRecord( ), is( 5 ) );
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * with the search which returns none result
     */
    public void testFilterBySearchedTextWithoutResult( )
    {
        String strSearchText = "nothing";

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( NumberUtils.INTEGER_ZERO ) );
    }

    /**
     * Test for the method
     * {@link DirectoryMultiviewSearchService#filterBySearchedText(fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel, String)}
     * with the panel which doesn't have item
     */
    public void testFilterBySearchedTextWithPanelWithoutItem( )
    {
        String strSearchText = "test";

        _recordPanel.setDirectoryRecordItemList( new ArrayList<>( ) );

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( NumberUtils.INTEGER_ZERO ) );
    }

    /**
     * Verify that the result of the search are updated after a new Document added to the index
     */
    public void testResultSearchUpdatedAfterDocumentAddition( )
    {
        String strSearchText = "title";

        IDirectoryMultiviewSearchService directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItem.size( ), is( 2 ) );

        addDocumentToIndex( 6, "title" );

        directoryMultiviewSearchService = new DirectoryMultiviewSearchService( _indexSearcher, _analyzer );
        directoryMultiviewSearchService.filterBySearchedText( _recordPanel, strSearchText );

        List<DirectoryRecordItem> listDirectoryRecordItemAfterAddition = _recordPanel.getDirectoryRecordItemList( );
        assertThat( listDirectoryRecordItemAfterAddition.size( ), is( 3 ) );
    }

    /**
     * Create the RecordPanel to use for the tests
     * 
     * @return the created RecordPanel
     */
    private IRecordPanel createRecordPanel( )
    {
        RecordPanelConfiguration recordPanelConfiguration = new RecordPanelConfiguration( "code", 1, "title", new ArrayList<>( ) );
        IRecordPanel recordPanel = new RecordPanelRecords( recordPanelConfiguration );

        List<DirectoryRecordItem> listDirectoryRecordItem = new ArrayList<>( );
        for ( Integer nIdRecord : _listPanelIdRecord )
        {
            DirectoryRecordItem directoryRecordItem = new DirectoryRecordItem( );
            directoryRecordItem.setIdRecord( nIdRecord );
            listDirectoryRecordItem.add( directoryRecordItem );
        }

        recordPanel.setDirectoryRecordItemList( listDirectoryRecordItem );
        return recordPanel;
    }

    /**
     * Populate the list of Entry which represent the Documents of the index
     */
    private void populateStoredContents( )
    {
        _listStoredContent = new ArrayList<>( );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "1", "test" ) );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "1", "title" ) );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "2", "code" ) );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "3", "value" ) );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "5", "something" ) );
        _listStoredContent.add( new AbstractMap.SimpleEntry<>( "5", "title" ) );
    }

    /**
     * Create and populate the index for the tests
     */
    private void createIndexForTest( )
    {
        _directory = new RAMDirectory( );
        _analyzer = new LuteceFrenchAnalyzer( );

        try
        {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig( _analyzer );
            indexWriterConfig.setOpenMode( OpenMode.CREATE_OR_APPEND );

            _indexWriter = new IndexWriter( _directory, indexWriterConfig );

            for ( Entry<String, String> entryStoredContents : _listStoredContent )
            {
                String strIdRecord = entryStoredContents.getKey( );
                String strContent = entryStoredContents.getValue( );

                FieldType fieldType = new FieldType( StringField.TYPE_STORED );
                fieldType.setOmitNorms( Boolean.FALSE );

                Document document = new Document( );
                document.add( new Field( DirectorySearchItem.FIELD_ID_DIRECTORY_RECORD, strIdRecord, fieldType ) );
                document.add( new Field( DirectorySearchItem.FIELD_CONTENTS, strContent, TextField.TYPE_NOT_STORED ) );
                _indexWriter.addDocument( document );
            }

            _indexWriter.close( );

            IndexReader indexReader = DirectoryReader.open( _directory );
            _indexSearcher = new IndexSearcher( indexReader );
        }
        catch( IOException e )
        {
            // Nothing to do
        }
    }

    /**
     * Add a new Document to the index and its equivalent DirectoryRecordItem to the RecordPanel list. Create a new IndexSearcher after the addition of the new
     * Document to the index.
     * 
     * @param nIdRecord
     *            The identifier of the record to add
     * @param strContentsValue
     *            The value of the contents field of the document
     */
    private void addDocumentToIndex( int nIdRecord, String strContentsValue )
    {
        try
        {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig( _analyzer );
            indexWriterConfig.setOpenMode( OpenMode.APPEND );

            _indexWriter = new IndexWriter( _directory, indexWriterConfig );

            FieldType fieldType = new FieldType( StringField.TYPE_STORED );
            fieldType.setOmitNorms( Boolean.FALSE );

            Document document = new Document( );
            document.add( new Field( DirectorySearchItem.FIELD_ID_DIRECTORY_RECORD, String.valueOf( nIdRecord ), fieldType ) );
            document.add( new Field( DirectorySearchItem.FIELD_CONTENTS, strContentsValue, TextField.TYPE_NOT_STORED ) );
            _indexWriter.addDocument( document );
            _indexWriter.close( );

            DirectoryRecordItem directoryRecordItem = new DirectoryRecordItem( );
            directoryRecordItem.setIdRecord( nIdRecord );
            _recordPanel.getDirectoryRecordItemList( ).add( directoryRecordItem );

            IndexReader indexReader = DirectoryReader.open( _directory );
            _indexSearcher = new IndexSearcher( indexReader );
        }
        catch( IOException exception )
        {
            // Nothing to do
        }
    }
}
