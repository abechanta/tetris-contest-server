select tetcon.score.rr
	from tetcon.result, tetcon.score
	where
		tetcon.result.ruleid in (
			select ruleid
				from tetcon.rule
				where
					str = 'LONGRUN'
					and w = 10
					and h = 6
					and g = 10
		)
		and tetcon.result.rid = tetcon.score.rid
		and tetcon.score.mid = 18

select * from tetcon.score
select * from tetcon.title
select * from tetcon.member
select * from tetcon.result
select * from tetcon.program
select * from tetcon.rule
select * from tetcon.log

select count(*) from tetcon.score where mid = 17;
select count(*) from tetcon.score;
select count(*) from tetcon.result;
select rid from tetcon.result 
	where month(reported) = 1;
select * from tetcon.result inner join tetcon.score;
select sid from tetcon.result inner join tetcon.score
	on month(result.reported) = 1
	and result.rid = score.rid;
SELECT TETCON.RESULT.*
  FROM TETCON.RESULT, TETCON.SCORE, TETCON.MEMBER
  WHERE MONTH(TETCON.RESULT.REPORTED) = 1 AND TETCON.RESULT.RID = TETCON.SCORE.RID
    AND TETCON.SCORE.MID = 18
  ORDER BY TETCON.RESULT.reported DESC;

SELECT COUNT(*), AVG(TETCON.SCORE.PR), AVG(TETCON.SCORE.LR), AVG(TETCON.SCORE.RR),
  SUM(TETCON.SCORE.X)
  FROM TETCON.RESULT, TETCON.SCORE, TETCON.MEMBER
  WHERE YEAR(TETCON.RESULT.REPORTED) = 2011 AND MONTH(TETCON.RESULT.REPORTED) = 1
    AND TETCON.RESULT.RID = TETCON.SCORE.RID AND TETCON.SCORE.MID = 18
  FETCH FIRST 5 ROWS ONLY

// NG
SELECT COUNT(*), AVG(TETCON.SCORE.PR), AVG(TETCON.SCORE.LR), AVG(TETCON.SCORE.RR),
  SUM(TETCON.SCORE.X)
from (
  select tetcon.score.* from TETCON.RESULT, TETCON.SCORE, TETCON.MEMBER
  WHERE YEAR(TETCON.RESULT.REPORTED) = 2011 AND MONTH(TETCON.RESULT.REPORTED) = 1
    AND TETCON.RESULT.RID = TETCON.SCORE.RID AND TETCON.SCORE.MID = 18
  FETCH FIRST 100 ROWS ONLY 
) as t

// NG
select tetcon.result.*
  from tetcon.result, tetcon.score, (
    select ruleid
      from tetcon.rule
      where
        str = 'LONGRUN'
        and w = 10
        and h = 6
        and g = 10
    ) as tmp1
  where
    tmp.ruleid = tetcon.result.ruleid
    and year(tetcon.result.reported) = 2011
    and month(tetcon.result.reported) = 1
    and tetcon.result.rid = tetcon.score.rid
    and tetcon.score.mid = 18
  fetch first 100 rows only


select count(tmp0.sid), avg(tmp0.pr), avg(tmp0.lr), avg(tmp0.rr), sum(tmp0.x)
from (
select tetcon.score.*
  from tetcon.result, tetcon.score, (
    select ruleid
      from tetcon.rule
      where
        str = 'LONGRUN'
        and w = 10
        and h = 6
        and g = 10
    ) as tmp1
  where
    tmp1.ruleid = tetcon.result.ruleid
    and year(tetcon.result.reported) = 2011
    and month(tetcon.result.reported) = 1
    and tetcon.result.rid = tetcon.score.rid
    and tetcon.score.mid = 18
) as tmp0

select count(tmp0.sid), avg(tmp0.pr), avg(tmp0.lr), avg(tmp0.rr), sum(tmp0.x)
from (
select tetcon.score.*
  from tetcon.result, tetcon.score
  where
    tetcon.result.ruleid in (
      select ruleid
        from tetcon.rule
        where
          str = 'LONGRUN'
          and w = 10
          and h = 6
          and g = 10
    )
    and year(tetcon.result.reported) = 2011
    and month(tetcon.result.reported) = 1
    and tetcon.result.rid = tetcon.score.rid
    and tetcon.score.mid = 18
) as tmp0

SELECT TETCON.RESULT.*, TETCON.SCORE.*
  FROM TETCON.RESULT, TETCON.SCORE, (      SELECT RULEID
        FROM TETCON.RULE
        WHERE STR = 'LONGRUN' AND W = 10 AND H = 18 AND G = 10) AS TMP1
  WHERE TMP1.RULEID = TETCON.RESULT.RULEID AND TETCON.RESULT.RID = TETCON.SCORE.RID
    AND TETCON.SCORE.MID = 18
  ORDER BY DATE DESC

SELECT TETCON.RESULT.*, TETCON.SCORE.*
  FROM TETCON.RESULT, TETCON.SCORE, (      SELECT RULEID
        FROM TETCON.RULE
        WHERE STR = 'LONGRUN' AND W = 10 AND H = 18 AND G = 10) AS TMP1
  WHERE TMP1.RULEID = TETCON.RESULT.RULEID AND TETCON.RESULT.RID = TETCON.SCORE.RID
    AND TETCON.SCORE.MID = 18
  ORDER BY TETCON.RESULT.DATE DESC
FETCH first 10 ROWS ONLY

select count(tmp0.sid), avg(tmp0.pr), avg(tmp0.lr), avg(tmp0.rr), sum(tmp0.x)
from (
select tetcon.score.*
  from tetcon.result, tetcon.score
  where
    tetcon.result.ruleid in (
      select ruleid
        from tetcon.rule
        where
          str = 'LONGRUN'
          and w = 10
          and h = 6
          and g = 10
    )
    and '110101000000' <= tetcon.result.date and tetcon.result.date <= '110129999999'
    and tetcon.result.rid = tetcon.score.rid
    and tetcon.score.mid = 18
) as tmp0

drop table tetcon.program;
rename table tetcon.program2 to program;
insert into tetcon.program (ver, authkey, registered, raw)
  select ver, authkey, registered, raw from tetcon.program2;
alter table tetcon.program add column content blob;
alter table tetcon.log drop column raw;
update tetcon.program set registered = default where ver = 'Mar 16 2011-202059';
update tetcon.config set updated = default where name = 'passwd';
insert into tetcon.config values ( 'passwd', 'abeabe', default );

alter table tetcon.contest add column cntr int;
drop table tetcon.log;

// ranking seq
drop table tetcon.rank_;
drop table tetcon.rank_1;
create table tetcon.rank_1 as select * from tetcon.rank_ with no data;

delete from tetcon.rank_1;

insert into tetcon.rank_1 (mid, cntr, pts)
select mid, count(sid) as cntr, avg(pr) * avg(lr) as pts
	from (
		select *
			from tetcon.score
			where
				rid in (
					select rid
						from tetcon.result
						where
							ruleid in (
								select ruleid
									from tetcon.rule
									where
										str = 'LONGRUN'
										and w = 10
										and h = 8
										and g = 10
							)
							and '110301000000' <= date and date <= '110399999999'
				)
	) as tmp0
	group by mid
	having count(sid) >= 100;

select row_number() over() as rank, cntr, mid, pts
	from tetcon.rank_1
	order by pts desc;

update tetcon.contest
	set
		updated = default
	where
		cid = 1;

update tetcon.contest
	set
		state = 1
	where
		state = 0,
		date0 < '11073100000000';
update tetcon.contest
	set
		state = 3
	where
		state = 1,
		date1 < '11073100000000';
-----
alter table tetcon.contest
	add column updated timestamp default current_timestamp

select count(*)
	from (
		select tetcon.score.*
			from tetcon.result, tetcon.score
			where
				tetcon.result.ruleid = 22
				and tetcon.result.rid = tetcon.score.rid
				and '000000000000' <= tetcon.result.date
				and tetcon.result.date <= '999999999999'
				and tetcon.score.mid = 17
	) as tmp0
select count(sid), avg(pr), avg(lr), avg(rr), avg(p), max(p), avg(l), max(l), sum(x)
	from tetcon.score
	where
		rid in (
			select rid
				from tetcon.result
				where
					ruleid = 22
					and '000000000000' <= date
					and date <= '999999999999'
		)
		and mid = 17

create table tetcon.contestx (
	cid			int				primary key generated always as identity,
	name		varchar(64)		not null,
	date0		char(12)		,
	date1		char(12)		,
	constraint x_date_chk check (date1 > date0),
	ruleid		int				not null references tetcon.rule,
	category	varchar(32)		not null,
	cntr		int				constraint x_cntr_chk check (cntr > 0),
	prize		int				constraint x_prize_chk check ((prize > 0) or (prize <= 3)),
	weight1		int				constraint x_weight1_chk check (weight1 >= 0),
	weight2		int				constraint x_weight2_chk check (weight2 >= 0),
	constraint x_weight2_1_chk check ((prize < 2) and (weight2 = 0) or (prize >= 2) and (weight2 <= weight1)),
	weight3		int				constraint x_weight3_chk check (weight3 >= 0),
	constraint x_weight3_2_chk check ((prize < 3) and (weight3 = 0) or (prize >= 3) and (weight3 <= weight2)),
	state		int				default 0,
	updated		timestamp		default current_timestamp
);
drop table tetcon.contestx;
drop table tetcon.contest;
drop table tetcon.title;
