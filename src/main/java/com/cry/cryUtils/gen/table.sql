select t.TABLE_NAME ����,
       (SELECT tt.COMMENTS
          FROM user_tab_comments tt
         where tt.TABLE_NAME = t.TABLE_NAME) ��˵��,
       t.COLUMN_NAME ����,
       (SELECT tt.COMMENTS
          FROM user_col_comments tt
         where tt.TABLE_NAME = t.TABLE_NAME
           and tt.COLUMN_NAME = t.COLUMN_NAME) ��˵��,
       t.DATA_TYPE ��������,
       t.DATA_LENGTH ����,
       t.NULLABLE ��Ϊ��
  from user_tab_columns t
