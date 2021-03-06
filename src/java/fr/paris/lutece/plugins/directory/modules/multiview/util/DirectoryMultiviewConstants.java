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
package fr.paris.lutece.plugins.directory.modules.multiview.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Constant class for the module
 */
public final class DirectoryMultiviewConstants
{
    // Marks
    public static final String MARK_RECORD = "record";

    // Parameters
    public static final String PARAMETER_SEARCHED_TEXT = "searched_text";
    public static final String PARAMETER_SELECTED_PANEL = "selected_panel";
    public static final String PARAMETER_CURRENT_SELECTED_PANEL = "current_selected_panel";
    public static final String PARAMETER_WORKFLOW_ACTION_REDIRECTION = "workflow_action_redirection";
    public static final String PARAMETER_SORT_COLUMN_POSITION = "column_position";
    public static final String PARAMETER_SORT_ATTRIBUTE_NAME = "sorted_attribute_name";
    public static final String PARAMETER_SORT_ASC_VALUE = "asc_sort";

    // Constants
    public static final String PREFIX_UNIT = "unit_";
    public static final String PREFIX_ADMIN_USER = "user_";
    public static final String REFERENCE_ITEM_DEFAULT_CODE = "-1";
    public static final String REFERENCE_ITEM_DEFAULT_NAME = "-";
    public static final String PARAMETER_URL_FILTER_PREFIX = "filter_";
    public static final int DEFAULT_FILTER_VALUE = NumberUtils.INTEGER_MINUS_ONE;

    /**
     * Private constructor - never call
     */
    private DirectoryMultiviewConstants( )
    {

    }
}
