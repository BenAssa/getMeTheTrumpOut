
----------------------------------------------------
--drop tables, create a temp table for Hoja, and the main all_data table
----------------------------------------------------


--drop all

CREATE TABLE  FAQ (
    id int,
    text varchar)



drop table words_appearance1;
drop table words_appearance;
drop table  word_stats;
drop table issues_to_conv;
drop table issue_stat;
drop table words_in_issue;



CREATE TABLE  words_appearance1 AS
select val,
  trim(regexp_split_to_table(
    lower(sentence),
    E'[^a-z0-9_@]+'
  )) as word,rt.test
from sentiment_all rt

delete from words_appearance1
 where  word ~ '^([0-9]+[.]?[0-9]*|[.][0-9]+)$';


delete from words_appearance1
 where  length(word)<2;


CREATE TABLE  words_appearance AS
select val,word,count(*) as total from words_appearance1
group by word,val;

CREATE TABLE  words_stat AS
select word,sum(total) as total from words_appearance
group by word;

create table words_positive as select ws.word,
 (-1.0 +wa.total)
 /ws.total as val, ws.total from
words_stat ws left join words_appearance wa on wa.word=ws.word and wa.val=4;
update words_positive set val=1.0/total where val is null;

CREATE TABLE  words_poppy AS
select id,
  trim(regexp_split_to_table(
    lower(text),
    E'[^a-z0-9_@]+'
  )) as word
from sentences rt;

create table poppy_sentiment as
select id,sum(log(val)) as positive, sum(log(1-val))  as negative from words_poppy pw
 inner join words_positive  wp on pw.word=wp.word group by pw.id;



CREATE INDEX words_appearance_sys_id ON words_appearance (sys_id);

CREATE INDEX words_appearance_issue ON words_appearance (issue);



create table word_stats as select sum(level) tot,
word from words_appearance group by word;



delete from words_appearance  where (Select sum(tot) from word_stats where word_stats.word= words_appearance.word)<10;




----------------------------------------------------
--gather issue stats, and remove false issues
----------------------------------------------------


create table words_in_issue as select
 (-0.1+sum(level))/(select count(distinct sys_id) from issues_to_conv  itc  where wa.issue=itc.issue) as level,issue,word
 from words_appearance wa  where test=0 group by issue,word;


update all_data set words= (select count(distinct word)  from words_appearance wa where wa.sys_id =all_data.sys_id);

drop table words_sys_id;
create table words_sys_id as
select word,sys_id from    words_appearance group by word,sys_id;

