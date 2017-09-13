
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORY_FILTER_MANAGEMENT','module.directory.multiview.adminFeature.ManageDirectoryFilter.name',1,'jsp/admin/plugins/directory-multiview/ManageDirectoryFilters.jsp','module.directory.multiview.adminFeature.ManageDirectoryFilter.description',0,'directory-multiview',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORY_FILTER_MANAGEMENT',1);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORY_MULTIVIEW';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORY_MULTIVIEW','module.directory.multiview.adminFeature.directoryMultiView.name',1,'jsp/admin/plugins/directory-multiview/ManageMultiDirectoryRecords.jsp','module.directory.multiview.adminFeature.directoryMultiView.description',0,'directory-multiview',NULL,NULL,NULL,4);

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORY_MULTIVIEW';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORY_MULTIVIEW',1);



-- init dashboard
INSERT INTO core_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('DIRECTORY_FILTER', 1, 3);

