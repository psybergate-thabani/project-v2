insert into project (id, version, code, name, client_code, start_date, end_date, type, deleted,created_by,created_date) values
('d5436fd9ec184fe4a0402d66992be407',0,'ECS','Equities Clearing System', 'JSE', CURRENT_DATE,NULL, 'BILLABLE',FALSE,'system',CURRENT_TIMESTAMP),
('0b5af6c17acb4ab0a3812a670daa1e42',0,'RSM','Resource Management POC', 'PSY', CURRENT_DATE, DATEADD('DAY',14, CURRENT_DATE), 'NON_BILLABLE', FALSE,'system',CURRENT_TIMESTAMP);
insert into task (id, version, code, name, project_id, deleted, created_by,created_date) values
('2e60b4f5f4f74dd1a62d92c2ea864be7',0,'DEV_ECS','Development', 'd5436fd9ec184fe4a0402d66992be407', FALSE,'system',CURRENT_TIMESTAMP),
('e3140fd9abbc4b348de5414b4a530f2e',0,'ANL_ECS','Analysis', 'd5436fd9ec184fe4a0402d66992be407', FALSE,'system',CURRENT_TIMESTAMP),
('636bb7841b98443aa4f6de90158a6952',0,'DEV_RSM','Development', '0b5af6c17acb4ab0a3812a670daa1e42', FALSE,'system',CURRENT_TIMESTAMP),
('163d87495fd04e67aaad6ec6233b8dda',0,'ANL_RSM','Analysis', '0b5af6c17acb4ab0a3812a670daa1e42', FALSE,'system',CURRENT_TIMESTAMP);
insert into allocation (id, version, employee_id, project_id, created_by, created_date, last_modified_date, last_modified_by, deleted) values
('d5436fd9ec184fe4a0402d66992be407', 0, '65ed6644718b11eabc550242ac130003', 'd5436fd9ec184fe4a0402d66992be407', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE),
('0b5af6c17acb4ab0a3812a670daa1e42', 0, '65ed6644718b11eabc550242ac130003', '0b5af6c17acb4ab0a3812a670daa1e42', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE),
('163d87495fd04e67aaad6ec6233b8dda', 0, '1061cfca718c11eabc550242ac130003', '0b5af6c17acb4ab0a3812a670daa1e42', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE);
