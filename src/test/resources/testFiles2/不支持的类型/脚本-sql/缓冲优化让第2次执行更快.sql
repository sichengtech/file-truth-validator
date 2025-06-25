drop table t purge;
create table t as select  * from dba_objects;

set linesize 1000
set autotrace on 
set timing on 

--第1次执行

select count(*) from t;

--第2次执行
--该命令只是为了先不考虑解析的优化,单纯考虑第2次执行物理读减少带来的优化效应
alter system flush shared_pool;
select count(*) from t;












附：贴出部分执行结果
-----------------------------------------------------------------------------------------------------------------------------
SQL> --第1次执行
SQL> select count(*) from t;

  COUNT(*)
----------
     72884

已用时间:  00: 00: 00.12

执行计划
------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 60918 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
统计信息
----------------------------------------------------------
         28  recursive calls
          0  db block gets
       1103  consistent gets
       1038  physical reads
          0  redo size
        425  bytes sent via SQL*Net to client
        415  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
          1  rows processed
          

SQL> --第2次执行
SQL> --该命令只是为了先不考虑解析的优化,单纯考虑第2次执行物理读减少带来的优化效应
SQL> alter system flush shared_pool;
系统已更改。
已用时间:  00: 00: 00.11
SQL> select count(*) from t;
  COUNT(*)
----------
     72884
已用时间:  00: 00: 00.04
执行计划
------------------------------------------------------------------
| Id  | Operation          | Name | Rows  | Cost (%CPU)| Time     |
-------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |     1 |   291   (1)| 00:00:04 |
|   1 |  SORT AGGREGATE    |      |     1 |            |          |
|   2 |   TABLE ACCESS FULL| T    | 60918 |   291   (1)| 00:00:04 |
-------------------------------------------------------------------
统计信息
----------------------------------------------------------
        282  recursive calls
          0  db block gets
       1131  consistent gets
          0  physical reads
          0  redo size
        425  bytes sent via SQL*Net to client
        415  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          5  sorts (memory)
          0  sorts (disk)
          1  rows processed