

        insertTwoColumns(words,"key_words",REDB.c(),"text,id",false);
        insertTwoColumns(relations,"key_word_to_sentences",REDB.c(),"key_word_id,sentence_id",true);
        insertTwoColumns(sentences,"sentences",REDB.c(),"text,id",false);

create table key_words(
text varchar,
id INTEGER
);
create table key_word_to_sentences(
key_word_id INTEGER,
sentence_id INTEGER,
level INTEGER
);
create table sentences(
text varchar,
id INTEGER,
level INTEGER,
charecter_id INTEGER
);
create table calls(
text varchar,
user varchar,
time_of timestamp
);


