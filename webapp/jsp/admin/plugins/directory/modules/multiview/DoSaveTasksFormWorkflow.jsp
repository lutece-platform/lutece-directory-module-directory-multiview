<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:useBean id="multiDirectory" scope="session" class="fr.paris.lutece.plugins.directory.modules.multiview.web.MultiDirectoryJspBean" />
<% 
	multiDirectory.init( request,  multiDirectory.PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY ); 
	response.sendRedirect( multiDirectory.doSaveTasksForm( request ) );
%>