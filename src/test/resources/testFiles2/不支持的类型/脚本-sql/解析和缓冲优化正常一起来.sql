drop table t purge;
create table t as select  * from dba_objects;

set linesize 1000
set autotrace on 
set timing on 

--第1次执行

select count(*) from t;

--第2次执行
--这里不做 shared_pool和buffer_cache的flush

select count(*) from t;











附：贴出部分执行结果
-----------------------------------------------------------------------------------------------------------------------------
SQL> --第1次执行
SQL> select count(*) from t;
  COUNT(*)
----------
     72884
已用时间:  00: 00: 00.11
执行计划
-------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 69684 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
统计信息
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



SQL> --第2次执行
SQL> --这里不做 shared_pool和buffer_cache的flush
SQL> select count(*) from t;
  COUNT(*)
----------
     72884
已用时间:  00: 00: 00.02
执行计划
-------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 69684 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
统计信息
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

