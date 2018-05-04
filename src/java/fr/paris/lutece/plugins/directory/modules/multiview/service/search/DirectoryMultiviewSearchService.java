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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.service.directorysearch.DirectorySearchFactory;
import fr.paris.lutece.plugins.directory.service.directorysearch.DirectorySearchItem;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Implementation of the module-directory-multiview search service
 */
public class DirectoryMultiviewSearchService implements IDirectoryMultiviewSearchService
{
    // Variables
    private Analyzer _analyzer;
    private IndexSearcher _indexSearcher;
    private DirectorySearchFactory _directorySearchFactory;
    
    /**
     * Constructor
     */
    public DirectoryMultiviewSearchService( )
    {
        _directorySearchFactory = DirectorySearchFactory.getInstance( );
        _analyzer = _directorySearchFactory.getAnalyzer( );
    }
    
    /**
     * Constructor
     * 
     * @param indexSearcher
     *          The IndexSearcher to use for made the search
     * @param analyzer
     *          The Analyzer to use for parsing the query of the search
     * @throws AppException - if one of the given parameters are missing
     */
    public DirectoryMultiviewSearchService( IndexSearcher indexSearcher, Analyzer analyzer ) throws AppException
    {
        if ( indexSearcher == null || analyzer == null )
        {
            throw new AppException( "The parameters of the search service musn't be null !" );
        }
        else
        {
            _indexSearcher = indexSearcher;
            _analyzer = analyzer;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void filterBySearchedText( IRecordPanel recordPanel, String strSearchText )
    {
        if ( recordPanel != null && StringUtils.isNotBlank( strSearchText ) )
        {
            List<DirectoryRecordItem> listDirectoryRecordItem = recordPanel.getDirectoryRecordItemList( );
            
            if ( !CollectionUtils.isEmpty( listDirectoryRecordItem ) )
            {
                try
                {                    
                    List<Integer> listIdRecord = searchIdRecordList( strSearchText );
                    
                    if ( listIdRecord.isEmpty( ) )
                    {
                        recordPanel.setDirectoryRecordItemList( new ArrayList<>( ) );
                    }
                    else
                    {
                        removeDirectoryItemOutsideSearchResult( recordPanel, listIdRecord );    
                    }
                } 
                catch ( IOException | ParseException exception )
                {
                    AppLogService.error( "An error occurred during the search on the index: " + exception.getMessage( ) );
                }
            }
        }
    }

    /**
     * Search the list of the identifier of the Record from the result of the search of the given text
     * 
     * @param strSearchText
     *          The text to search
     * @return the list of the identifier of the Record from the result of the search of the given text
     * @throws ParseException 
     * @throws IOException 
     */
    private List<Integer> searchIdRecordList( String strSearchText ) throws ParseException, IOException
    {
        List<Integer> listIdRecordResult = new ArrayList<>( );

        Query querySearch = prepareQuery( strSearchText );
        
        IndexSearcher indexSearcher = _indexSearcher;
        if ( indexSearcher == null && _directorySearchFactory != null )
        {
            indexSearcher = _directorySearchFactory.getIndexSearcher( );
        }
        
        TopDocs topDocs = indexSearcher.search( querySearch, LuceneSearchEngine.MAX_RESPONSES );
        if ( topDocs != null && topDocs.scoreDocs != null )
        {
            for ( ScoreDoc scoreDoc : topDocs.scoreDocs )
            {
                int nIdRecord = retrieveIdRecordFromDoc( indexSearcher, scoreDoc );
                if ( nIdRecord != NumberUtils.INTEGER_MINUS_ONE )
                {
                    listIdRecordResult.add( nIdRecord );
                }
            }
        }

        return listIdRecordResult;
    }
    
    /**
     * Prepare the query to execute with the given text to make the search
     * 
     * @param strSearchText
     *          The text to search
     * @return the query to execute with the given text to make the search
     * @throws ParseException 
     */
    private Query prepareQuery( String strSearchText ) throws ParseException
    {
        QueryParser queryParser = new QueryParser( DirectorySearchItem.FIELD_CONTENTS, _analyzer );
        Query queryParsed = queryParser.parse( strSearchText );
        
        return queryParsed;
    }
    
    /**
     * Retrieve the id of the Record with the given searcher for the specified ScoreDoc
     * 
     * @param indexSearcher
     *          The searcher used to retrieve the Document
     * @param scoreDoc
     *          The scoreDoc to retrieve the id Record from
     * @return the id of the Record of the given ScoreDoc or -1 if not found or if a problem occurred
     * @throws IOException 
     */
    private int retrieveIdRecordFromDoc( IndexSearcher indexSearcher, ScoreDoc scoreDoc ) throws IOException
    {
        int nIdRecord = NumberUtils.INTEGER_MINUS_ONE;

        if ( scoreDoc != null )
        {
            int nIdDoc = scoreDoc.doc;
            Document document = indexSearcher.doc( nIdDoc );
            if ( document != null )
            {
                String strIdRecord = document.get( DirectorySearchItem.FIELD_ID_DIRECTORY_RECORD );
                nIdRecord = NumberUtils.toInt( strIdRecord, NumberUtils.INTEGER_MINUS_ONE );
            }
        }
        
        return nIdRecord;
    }
    
    /**
     * Remove the DirectoryRecordItem which are not associated to a Record which are absent from the list of search result
     * 
     * @param recordPanel
     *          The RecordPael to remove the DirectoryRecordItem which are not present in the search
     * @param listIdRecord
     *          The list of all id of the Record which are the result of the search
     */
    private void removeDirectoryItemOutsideSearchResult( IRecordPanel recordPanel, List<Integer> listIdRecord )
    {
        List<DirectoryRecordItem> listDirectoryRecordItem = recordPanel.getDirectoryRecordItemList( );
        Iterator<DirectoryRecordItem> iteratorDirectoryRecordItem = listDirectoryRecordItem.iterator( );

        while ( iteratorDirectoryRecordItem.hasNext( ) )
        {
            DirectoryRecordItem directoryRecordItem = iteratorDirectoryRecordItem.next( );
            int nIdRecord = directoryRecordItem.getIdRecord( );

            if ( !listIdRecord.contains( nIdRecord ) )
            {
                iteratorDirectoryRecordItem.remove( );
            }
        }
    }
}
