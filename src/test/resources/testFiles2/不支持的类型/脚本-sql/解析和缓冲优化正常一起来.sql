drop table t purge;
create table t as select  * from dba_objects;

set linesize 1000
set autotrace on 
set timing on 

--��1��ִ��

select count(*) from t;

--��2��ִ��
--���ﲻ�� shared_pool��buffer_cache��flush

select count(*) from t;











������������ִ�н��
-----------------------------------------------------------------------------------------------------------------------------
SQL> --��1��ִ��
SQL> select count(*) from t;
  COUNT(*)
----------
     72884
����ʱ��:  00: 00: 00.11
ִ�мƻ�
-------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 69684 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
ͳ����Ϣ
----------------------------------------------------------
         28  recursive calls
          0  db block gets
       1110  consistent gets
       1038  physical reads
          0  redo size
        425  bytes sent via SQL*Net to client
        415  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
          1  rows processed



SQL> --��2��ִ��
SQL> --���ﲻ�� shared_pool��buffer_cache��flush
SQL> select count(*) from t;
  COUNT(*)
----------
     72884
����ʱ��:  00: 00: 00.02
ִ�мƻ�
-------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 69684 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
ͳ����Ϣ
----------------------------------------------------------
          0  recursive calls
          0  db block gets
       1043  consistent gets
          0  physical reads
          0  redo size
        425  bytes sent via SQL*Net to client
        415  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
          1  rows processed

