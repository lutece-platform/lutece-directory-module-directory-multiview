
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORY_FILTER_MANAGEMENT','directory-filter.adminFeature.ManageDirectoryFilter.name',1,'jsp/admin/plugins/directory-filter/ManageDirectoryFilters.jsp','directory-filter.adminFeature.ManageDirectoryFilter.description',0,'directory-filter',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORY_FILTER_MANAGEMENT',1);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORY_FILTER_MANAGEMENT','directory-filter.adminFeature.ManageDirectoryFilterCondition.name',1,'jsp/admin/plugins/directory-filter/ManageDirectoryFilterConditions.jsp','directory-filter.adminFeature.ManageDirectoryFilterCondition.description',0,'directory-filter',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORY_FILTER_MANAGEMENT',1);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DIRECTORY_FILTER_MANAGEMENT','directory-filter.adminFeature.ManageDirectoryFilterAction.name',1,'jsp/admin/plugins/directory-filter/ManageDirectoryFilterActions.jsp','directory-filter.adminFeature.ManageDirectoryFilterAction.description',0,'directory-filter',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DIRECTORY_FILTER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DIRECTORY_FILTER_MANAGEMENT',1);

