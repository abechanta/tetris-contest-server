create table tetcon.score (
	sid			int				primary key generated always as identity,
	mid			int				not null references tetcon.member,
	rid			int				not null references tetcon.result,
	p			int				constraint p_chk check (p >= 0),
	l			int				constraint l_chk check (l >= 0),
	l1			int				constraint l1_chk check (l1 >= 0),
	l2			int				constraint l2_chk check (l2 >= 0),
	l3			int				constraint l3_chk check (l3 >= 0),
	l4			int				constraint l4_chk check (l4 >= 0),
	constraint ll_chk check (l = l1 * 1 + l2 * 2 + l3 * 3 + l4 * 4),
	x			int				constraint x_chk check (x >= 0),
	si			int				constraint si_chk check (si >= 0),
	sd			int				constraint sd_chk check (sd >= 0),
	pr			int				constraint pr_chk check (pr >= 0),
	lr			int				constraint lr_chk check (lr >= 0),
	rr			int				constraint rr_chk check (rr >= 0)
);
create table tetcon.contest (
	cid			int				primary key generated always as identity,
	name		varchar(64)		unique not null,
	date0		char(12)		,
	date1		char(12)		,
	constraint date1and0_chk check (date1 > date0),
	ruleid		int				not null references tetcon.rule,
	category	varchar(32)		not null,
	cntr		int				constraint cntr_chk check (cntr > 0),
	prize1		int				constraint prize1_chk check (prize1 >= 0),
	prize2		int				constraint prize2_chk check (prize2 >= 0),
	constraint prize2and1_chk check (prize2 <= prize1),
	prize3		int				constraint prize3_chk check (prize3 >= 0),
	constraint prize3and2_chk check (prize3 <= prize2),
	state		int				default 0,
	updated		timestamp		default current_timestamp
);
create table tetcon.title (
	tid			int				primary key generated always as identity,
	mid			int				not null references tetcon.member,
	name		varchar(64)		not null,
	prize		int				constraint prize_chk check (prize > 0),
	registered	timestamp		default current_timestamp
);
create table tetcon.member (
	mid			int				primary key generated always as identity,
	name		varchar(32)		unique not null,
	author		varchar(32)		not null,
	registered	timestamp		default current_timestamp
);
create table tetcon.result (
	rid			int				primary key generated always as identity,
	pid			int				not null references tetcon.program,
	reported	timestamp		unique default current_timestamp,
	date		char(12)		,
	host		varchar(32)		not null,
	ruleid		int				not null references tetcon.rule,
	r			int				constraint r_chk check (r > 0)
);
create table tetcon.program (
	pid			int				primary key generated always as identity,
	ver			varchar(32)		unique not null,
	authkey		varchar(32)		not null,
	content		blob			not null,
	registered	timestamp		default current_timestamp
);
create table tetcon.rule (
	ruleid		int				primary key generated always as identity,
	str			varchar(32)		not null,
	w			int				constraint w_chk check ((w >= 4) and (w <= 30)),
	h			int				constraint h_chk check ((h >= 4) and (h <= 30)),
	g			int				constraint g_chk check ((g > 0) and (g <= 200))
);
create table tetcon.rank_ (
	mid			int				not null,
	cntr		int				not null,
	pts			int				not null
);
create table tetcon.config (
	name		varchar(256)	unique not null,
	val			varchar(256)	not null,
	updated		timestamp		default current_timestamp
);
