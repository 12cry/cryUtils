
SELECT t.index_name,t.table_name FROM user_indexes t
 where t.TABLE_NAME like 'T\_ALARM\_%' escape '\'
    or t.TABLE_NAME like 'T\_B\_%' escape '\'
    or t.TABLE_NAME like 'T\_CI%' escape '\'
    or t.TABLE_NAME like 'T\_P\_%' escape '\'
    or t.TABLE_NAME like 'T\_TOPO\_%' escape '\'
    order by t.index_name