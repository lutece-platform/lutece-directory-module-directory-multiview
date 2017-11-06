<jsp:useBean id="manageDirectoryMultiview" scope="session" class="fr.paris.lutece.plugins.directory.modules.multiview.web.MultiDirectoryJspBean" />
<% manageDirectoryMultiview.initialize( request );
String strContent = manageDirectoryMultiview.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
