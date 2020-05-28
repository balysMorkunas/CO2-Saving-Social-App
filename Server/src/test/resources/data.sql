insert into person (email, first_name, last_name, password, points) values ('test@hotmail.com', 'Steve', 'Smith', '$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom', 200);

insert into person (email, first_name, last_name, password, points) values ('carlemail@hotmail.com', 'Carl', 'Midge', '$2a$10$XH6Pj.z1YHEMNJ4hh/cfgupqEtU94vRlaNVFKdv1s8S91pe1XG2OO', 500);

insert into action_log (type, description, points, date_time, person_id, first_name) values ('vegetarian', 'zuchini', 50, now(), 0, 'Steve');

insert into action_log (type, description, points, date_time, person_id, first_name) values ('vegetarian', 'zuchini', 50, now(), 1, 'Carl');

insert into action (type, description, points, category) values ('vegetarian', 'Eating a vegetarian meal.', 50, 'meal');
insert into action (type, description, points, category) values ('bike', 'Going with the bike instead of car', 50, 'transport');

insert into follower (person_id, follower_id) values (1,2);
insert into follower (person_id, follower_id) values (1,3);

insert into achievement (name, type, required_points) values ('Rookie', 'points', 100);
insert into achievement (name, type, required_points) values ('Beginner', 'points', 200);
insert into achievement (name, type, required_points) values ('Amateur', 'points', 500);
insert into achievement (name, type, required_points) values ('Semi-pro', 'points', 1000);
insert into achievement (name, type, required_points) values ('Pro', 'points', 2000);
insert into achievement (name, type, required_points) values ('World-Class', 'points', 5000);
insert into achievement (name, type, required_points) values ('God', 'points', 9999);

insert into achievement (name, type, required_points) values ('bike.', 'bike', 0);
insert into achievement (name, type, required_points) values ('friends', 'friends', 0);
insert into achievement (name, type, required_points) values ('picture', 'picture', 0);
insert into achievement (name, type, required_points) values ('followers', 'followers', 0);
insert into achievement (name, type, required_points) values ('login', 'login', 0);
