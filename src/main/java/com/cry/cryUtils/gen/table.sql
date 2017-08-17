select t.TABLE_NAME 表名,
       (SELECT tt.COMMENTS
          FROM user_tab_comments tt
         where tt.TABLE_NAME = t.TABLE_NAME) 表说明,
       t.COLUMN_NAME 列名,
       (SELECT tt.COMMENTS
          FROM user_col_comments tt
         where tt.TABLE_NAME = t.TABLE_NAME
           and tt.COLUMN_NAME = t.COLUMN_NAME) 列说明,
       t.DATA_TYPE 数据类型,
       t.DATA_LENGTH 长度,
       t.NULLABLE 可为空
  from user_tab_columns t
