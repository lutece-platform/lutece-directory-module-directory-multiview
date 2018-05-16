![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=module-directory-multiview-deploy)
# Plugin directory-multiview

## Introduction

This module propose a single view to see all records of multiple directories. The records displayed by this view can be filtered by multiple filters and a search bar which allow to search a term which is present among the responses of a record.

It is possible to click on each line of the table to access the details of the selected record. Then we access to all information of the record and the possibility to launch several actions associated to the workflow on which the record is linked.

It is possible to change the templates of the tasks for a specific action of the workflow to tell it if the redirection must be made on the details of the record or on the table of all records when the action is done. The default page of the redirection of an action will be the page of the details of the record on which the action has been made.

All the elements of the view are configurable thanks to the context file of the module. To achieve that it's simply needed to add the desired bean of the element in the context file of the module which wants to modify the table.

There are several kinds of elements:
 
* 
 **The panels** 

The panels allow to toggle the view from one way to another by clicking directly on its associated tab at the top of the table. A panel contains a set of panels initializers which will be automatically applied on the view when it will be activated.There must be at least one defined panel in the context file in other cases the table will not be displayed.

* 
 **The panels initializers** 

There are the elements which will add the conditions to applied when a panel will be selected in order to restrict the records to display in the view to the desired set. These elements have no graphical parts and can't be changed through the HMI.

* 
 **The columns** 

The columns represent the central elements of the board. They are the ones which contains all information of the records to displayed to the user. Each column display a particular information of the record (for example the directory on which the record is linked or its date of creation).If none column are defined in a context file the table will not be displayed.

* 
 **The filters** 

The filter condition the records to select for the displaying of the board. They correspond to drop down lists with values which are linked to a particular element of a record (for example the name of the directory on which the record is linked).The filters are displayed above the panels.

There are two kinds of filters:
 
* the "standards" filters: those filters allow to add conditions on records properties (its creation date, the directory on which it is linked, etc...)
* the filters which are linked to an entry of a directory: those filters condition the records to retrieve according to the values linked to one (or several) directory entry. The entries on which to based on are defined in a particular configuration which will be linked to the filter (the detail of the configuration will be explained in the "Configuration" part).


The values of the filters may can changed. Some of them have fixed values defined from information contains on the records given by the panel which not used panels initializers and with no selected values for each filter. Others contains values independent from the displayed records (for example the filter on the directory which allow to filter on all existing directory in database even if they have no records).Others contains conditional values from other filter (for example the filter on the workflow state which is displayed when a value has been chosen on the directory filter).

It is possible to have no filters on the view of the table.



## Configuration


```
Prerequisite : the configurations of the different used workflow and the directories must be set.
```


Each element of the board defined in the "Introduction" part need to have a particular configuration. They are several kinds of objects to implement to properly configure the elements of the board.
 
* 
 **The configurations** 

It exists several kinds of objects to use to make the configuration:
 
* 
The configuration of the panels: `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration` 

This configuration is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel` . It has all information linked to a panel:
 
* a position: the position of the panel of the table
* a title: the title of the panel which will be displayed on the associated tab
* a technical code: a unique code for each panel in order to distinguish them to manage the active panel (which is selected on the HMI)
* the list of panels initializers: this list contains all the panels initializers associated to the panel in order to specify the set of its own records


* 
The configuration of the filters: `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration` 

This configuration is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` . It has all information linked to a filter for its display on the HMI:
 
* a position: which correspond to the position of the filter on the global view of the board
* a label: which correspond to the default label of the filter


* 
The configuration of the filters which are linked to the values of the entry of directory: `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterEntryConfiguration` 

This configuration is also linked to an object of filter's type but for a filter linked to directory entries. It inherits of `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration` but it has an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` in addition. It is the object `IRecordColumn` which contains the list of entries on which the associated filter retrieve the different values on which the user can be filter the records on the displayed of the HMI.




```
Note: all the configurations must be defined as bean in the context file.
```


* 
 **The QueryParts** 

The objects of kind QueryParts are used for adding parts of SQL query which will be used to build the final query which will serve to retrieve all the records of the table. There are several type of QueryParts in terms of the element on which it is based:
 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart` which correspond to the QueryPart linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart` which correspond to the QueryPart linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart` which correspond to the QueryPart linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer` 


* 
 **The Displays** 

The objects of kind Display are used to retrieve the necessary information from the request for giving them to the object that they represent (for building the associated query parts later) and to build the template of the different elements. They are several type of Displays in term of element on which it is based:
 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.IRecordColumnDisplay` which correspond to the display part of an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.IRecordFilterDisplay` which correspond to the display part of an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay` which correspond to the display part of an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.IRecordPanelDisplayInitializer` which correspond to the display part of an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer` 


* 
 **The Factories** 

It exists two kinds of Factories:
 
* 
the Factories which are linked to object of type "QueryParts": during the construction of the final query the Factories will be ran to retrieve all the records to display in the table. There are several type of Factory linked to each type of element of the table:


 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.factory.IRecordColumnQueryPartFactory` which correspond to the Factory of a QueryPart linked to a `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.factory.IRecordFilterQueryPartFactory` which correspond to the Factory of a QueryPart linked to a `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.factory.IRecordPanelInitializerQueryPartFactory` which correspond to the Factory of a QueryPart linked to a `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer` 


Each Factory of a QueryPart will retrieve the element of the type from which it is associated and it will retrieve the query parts to used.



* 
The Factories linked to the element's display: those Factories are used to build the objects which are linked to the display of their associated elements. There are several types of those Factories in term of the type it belongs to:


 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.factory.IRecordColumnDisplayFactory` this Factory is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.factory.IRecordFilterDisplayFactory` this Factory is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory.IRecordPanelDisplayFactory` this Factory is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel` 
*  `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.factory.IRecordPanelDisplayInitializerFactory` this Factory is linked to an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer` 



```
Note: all the Factories must be defined as bean in the context file.
```






## Usage

It is possible to make a search by a term on a record via the search bar. To make it work properly it is necessary that all the records of directories had been indexed.

Now we will detail how to create every elements of the "Introduction" part with the components defined in the "Configuration" part.
 
* 
 **The panels** 


To define a new panel it simply need to add a new implementation of the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl.AbstractRecordPanel` to have to only implement the specifics methods for each panels. Then it is necessary to give to it a `RecordPanelConfiguration` to use.

A complete example of the declaration of a panel will be like:

 `<bean id="directory-multiview.panelRecords" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl.RecordPanelRecords"><constructor-arg name="recordPanelConfiguration" type="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration"ref="directory-multiview.recordsPanel.panelConfiguration"/></bean>` 

The configuration to define will be like:

 `<bean id="directory-multiview.recordsPanel.panelConfiguration" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration"><constructor-arg name="strTechnicalCode" type="java.lang.String" value="records"/><constructor-arg name="nPosition" type="int" value="1"/><constructor-arg name="strTitle" type="java.lang.String" value="Démarche(s)"/><constructor-arg name="listRecordPanelInitializer"><list value-type="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer"><ref bean="directory-multiview.recordPanelInitializer.panelDirectory"/><ref bean="directory-multiview.recordPanelInitializer.panelRecords"/><ref bean="directory-multiviewgra.recordPanelInitializer.panelMyUnitAssignment"/></list></constructor-arg></bean>` 

As we detailed earlier the configuration contains all information linked to a panel whose list of its own panels initializers. The creation of a panel initializer will be explained later.

Then we need to implement the part which manage the display of a panel. To do that we will implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay` or inherit the bastract class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl.AbstractRecordPanelDisplay` . It is recommend to inherit the abstract class rather than implement directly the interface.

The class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl.RecordPanelRecordsDisplay` is an example of an implementation of the interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```



* 
 **The panels initializers** 


To create a new panel initializer it's only needed to add a new implementation of the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.impl.AbstractRecordPanelInitializer` . Except for specifics needed it is recommend to inherit the abstract class rather than to implement directly the interface.

A complete example of the declaration of a panel initializer will be like:

 `<bean id="directory-multiview.recordPanelInitializer.panelDirectory" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.impl.RecordPanelDirectoryInitializer"/>` 

When the declaration of the panel initializer will be done it's needed to add an implementation of the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.impl.AbstractRecordPanelInitializerQueryPart` . This implementation will indicate which parts of the query must be used for this initializer during the construction of the final query which will be used to build the set of records to display on the board.

The class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.impl.RecordPanelDirectoryInitializerQueryPart` is an example of the implementation of the interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


When this implementation is done it is necessary to implement a Factory for the QueryPart linked to this bean. To do that it's needed to make an implementation of the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.factory.IRecordPanelInitializerQueryPartFactory` . An example of declaration of a such Factory is like:

 `<bean id="directory-multiview.recordPanelInitializer.panelDirectory.queryPart.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.factory.impl.RecordPanelDirectoryInitializerQueryPartFactory"/>` 

Then it still the part which correspond to the web part of the initializer which is the retrieving of the information which it needs from the request. To doing this it's needed implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.IRecordPanelDisplayInitializer` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.impl.AbstractRecordPanelDisplayInitializer` . This implementation will allow to create an object of type `fr.paris.lutece.plugins.directory.modules.multiview.business.record.RecordParameters` which contains all necessaries data for the building of the query.

The class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.impl.RecordPanelDirectoryDisplayInitializer` is an example of the implementation of this interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


The last step needed to create a panel initializer is to create a Factory associated to this implementation. It's needed to make an implementation of the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.IRecordPanelDisplayInitializer` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.impl.AbstractRecordPanelDisplayInitializer` .

An example of declaration of this kind of Factory is like:

 `<bean id="directory-multiview.recordsPanelInitializer.panelDirectory.display.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.factory.impl.RecordPanelDirectoryDisplayInitializerFactory"/>` 


* 
 **The columns** 

To create a new column we need to implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.AbstractRecordColumn` . A column can be declared as below:

 `<bean id="directory-multiview.workflowState.column" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnWorkflowState"><constructor-arg name="nRecordColumnPosition" type="int" value="1"/><constructor-arg name="strRecordColumnTitle" type="java.lang.String" value="État"/></bean>` 

The column constructor must take as argument:
 
* a position: which correspond to the position of the column on the table
* a title: which correspond to the value displayed on the column header of the table


The columns can be associated to directory entries values. It is necessary to add another argument to the constructor of the column which is a list of `java.lang.String` where each `String` correspond to a title of an entry.

An example of the adding of this list to the previous bean is to add another parameter like shown below:

 `<constructor-arg name="listEntryTitle"><list value-type="java.lang.String"><value>Votre prénom</value><value>Votre nom</value></list></constructor-arg>` 

When the column is defined it's needed to make the implementation of the QueryPart which is linked to it. It's needed to implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.AbstractRecordColumnQueryPart` . Except for specifics needed it is recommend to inherit the abstract class rather than directly implement the interface.

The class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnDirectoryQueryPart` is an example of the implementation of the interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


When the QueryPart is defined it is necessary to implement a Factory linked to the QueryPart of this column. It is needed to implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.factory.IRecordColumnQueryPartFactory` . After that it still to declare the bean like shown below:

 `<bean id="directory-multiview.recordColumn.workflowState.queryPart.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.factory.impl.RecordColumnWorkflowStateQueryPartFactory"/>` 

It is needed to make an implementation of the bean which manage the display of the column. It is needed implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.IRecordColumnDisplay` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.impl.AbstractRecordColumnDisplay` . Except for specifics needed it is recommend to inherit the abstract class rather than directly implement the interface.

The class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.impl.RecordColumnDisplayDirectory` is an example of the implementation of this interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


The last step is to create a Factory associated to the display of that column for this it's needed to implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.factory.IRecordColumnDisplayFactory` .

An example of declaration of this kind of Factory is like shown below:

 `<bean id="directory-multiview.workflowState.column.display.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.factory.RecordColumnDisplayWorkflowStateFactory"/>` 

For all the new created columns it is necessary to modify the context file of the plugin (the file directory-multiview_context.xml). The declaration of the bean `directory-multiview.recordColumn.factory` must be changed in order to configure the list of all columns to used on the board.

* 
 **The filters** 

The first step to create a filter is to create its configuration. For this it's simply needed to create a bean in the context file which will contains all information of the filter.

An example of the declaration of a such bean is like shown below:

 `<bean id="directory-multiview.directoryRecord.filterConfiguration" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration"><constructor-arg name="nPosition" type="int" value="1"/><constructor-arg name="strRecordFilterLabel" type="java.lang.String" value="-"/></bean>` 

Then it's needed to create the filter itself by implementing the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter` or by inheriting the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.impl.AbstractRecordFilter` . Except for specifics needed it is recommend to inherit the abstract class rather than directly implement the interface.

We will declare the bean in the context file by giving to it the configuration as a parameter. The following example will show how to declare the bean:

 `<bean id="directory-multiview.direcoryRecord.filter" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.impl.RecordFilterDirectory"><constructor-arg name="recordFilterConfiguration" type="fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration" ref="directory-multiview.directoryRecord.filterConfiguration"/></bean>` 

When the filter is defined it is needed to implement the associated QueryPart. To do that it is simply needed implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.AbstractRecordFilterQueryPart` . Except for specifics needed it is recommend to inherit the abstract class rather than directly implement the interface.

The class `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.RecordFilterDisplayDirectory` is an example of the implementation of this interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


The next step is to create the Factory linked to the QueryPart previously defined. For this it is needed implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.factory.IRecordFilterQueryPartFactory` . The bean must be declared as followed:

 `<bean id="directory-multiview.recordFilter.directoryRecord.queryPart.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.factory.impl.RecordFilterDirectoryQueryPartFactory"/>` 

To define the bean which manage the display of the filter it is needed implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.IRecordFilterDisplay` or to inherit the abstract class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.impl.AbstractRecordFilterDisplay` . Except for specifics needed it is recommend to inherit the abstract class rather than directly implement the interface.

The class `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.impl.RecordFilterDisplayDirectory` is an example of the implementation of this interface.


```
Note: it is not necessary to declare this bean in the context file of the module.
```


The last step is to create the Factory linked to the display of the filter. For doing that it is needed to implement the interface `fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.factory.IRecordFilterDisplayFactory` . An example of the declaration of this bean is shown below:

 `<bean id="directory-multiview.directoryRecord.filter.display.factory" class="fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.factory.RecordFilterDisplayDirectoryFactory"/>` 



 **Management of the redirections** 

To make the redirection to the desired page when the action of the workflow is done, it simply needs to add the following line in the template of the task: `<input type="hidden" name="workflow_action_redirection" value="list"/>` . There are two possibles values for this parameter:
 
*  `list` : allow to make the redirection to the page which display all the records
*  `details` : allow to redirect to the page of the record of the directory on which the action has been made



[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/module-directory-multiview/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*