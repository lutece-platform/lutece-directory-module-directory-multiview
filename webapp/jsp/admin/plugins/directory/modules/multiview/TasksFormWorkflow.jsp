<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />
<jsp:useBean id="multiDirectory" scope="session" class="fr.paris.lutece.plugins.directory.modules.multiview.web.MultiDirectoryJspBean" />
<%
multiDirectory.init( request, fr.paris.lutece.plugins.directory.web.ManageDirectoryJspBean.RIGHT_MANAGE_DIRECTORY);
%>
<%= multiDirectory.getTasksForm(request) %>
<%@ include file="../../../../AdminFooter.jsp" %>