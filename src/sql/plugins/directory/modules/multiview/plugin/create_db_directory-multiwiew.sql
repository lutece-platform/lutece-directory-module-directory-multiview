
--
-- Structure for table directory_filter
--

DROP TABLE IF EXISTS directory_filter;
CREATE TABLE directory_filter (
id_directory_filter int NOT NULL,
id_directory int default 0,
name varchar(255) default '' NOT NULL,
style varchar(50) default '',
position int default '0',
PRIMARY KEY (id_directory_filter)
);

--
-- Structure for table directory_filter_action
--

DROP TABLE IF EXISTS directory_filter_action;
CREATE TABLE directory_filter_action (
id_directory_filter_action int NOT NULL,
id_directory_filter int default '0' NOT NULL,
id_action int default '0' NOT NULL,
position int default '0',
style varchar(50) default '',
nb_item int default '0',
PRIMARY KEY (id_directory_filter_action)
);

--
-- Structure for table directory_filter_condition
--

DROP TABLE IF EXISTS directory_filter_condition;
CREATE TABLE directory_filter_condition (
id_directory_filter_condition int NOT NULL,
id_directory_filter int default '0' NOT NULL,
id_unit int default '0' NOT NULL,
id_entry int default '0',
operator int default '0',
filter_type int default '0',
PRIMARY KEY (id_directory_filter_condition)
);
