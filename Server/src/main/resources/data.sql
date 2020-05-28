

insert into action (type, description, points, category) values ('vegetarian', 'Eating a vegetarian meal.', 50, 'meal');
insert into action (type, description, points, category) values ('vegan', 'Eating a vegan meal.', 65, 'meal');
insert into action (type, description, points, category) values ('local', 'Eating a meal with locally produced ingredients.', 25, 'meal');

insert into action (type, description, points, category) values ('bike', 'Going with the bike instead of car', 50, 'transport');
insert into action (type, description, points, category) values ('bus', 'Going with the bus instead of car', 50, 'transport');
insert into action (type, description, points, category) values ('train', 'Going with the train/railway instead of car', 50, 'transport');

insert into action (type, description, points, category) values ('solar', 'Installing solar panels', 50, 'household');
insert into action (type, description, points, category) values ('heating', 'Lowering the thermostat', 25, 'household');



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
