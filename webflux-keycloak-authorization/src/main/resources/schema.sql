-- Menu
CREATE TABLE menu (
  id SERIAL,
  restaurant_id BIGINT NOT NULL,
  active  boolean DEFAULT NULL,

  CONSTRAINT menu_PK PRIMARY KEY (id)
);

create sequence menu_SEQUENCE start with 1000;

--INSERT INTO menu VALUES
--    (1,1,1),
--    (2,2,1),
--    (3,3,1),
--    (4,4,1);

-- Menu Item
CREATE TABLE menu_item (
  id SERIAL,
  name varchar(255) DEFAULT NULL,
  description varchar(100) DEFAULT NULL,
  type_name varchar(100) DEFAULT NULL,
  group_name varchar(100) DEFAULT NULL,
  price decimal(10,2) DEFAULT NULL,
  menu_id BIGINT DEFAULT NULL,

  CONSTRAINT menu_item_PK PRIMARY KEY (id),
  CONSTRAINT menu_item_FK FOREIGN KEY (menu_id) REFERENCES menu (id)
);
create sequence menu_item_SEQUENCE start with 1000;
--INSERT INTO menu_item VALUES
--    (1,'menu item 1','menu item 1 for Rest 1','NON_VEG','MAIN_COURSE',222.00,1),
--    (2,'menu item 2','menu item 2 for Rest 1','VEG','STARTER',123.00,1),
--    (3,'menu item 3','menu item 3 for Rest 1','NON_VEG','DESSERT',120.00,1),
--    (4,'menu item 1','menu item 1 for Rest 2','NON_VEG','MAIN_COURSE',232.00,2),
--    (5,'menu item 2','menu item 2 for Rest 2','VEG','STARTER',189.00,2),
--    (6,'menu item 3','menu item 3 for Rest 2','NON_VEG','DESSERT',150.00,2),
--    (7,'menu item 4','menu item 4 for Rest 2','NON_VEG','MAIN_COURSE',232.00,2),
--    (8,'menu item 1','menu item 1 for Rest 3','NON_VEG','MAIN_COURSE',500.00,3),
--    (9,'menu item 2','menu item 2 for Rest 3','VEG','STARTER',174.00,3),
--    (10,'menu item 3','menu item 3 for Rest 3','NON_VEG','DESSERT',50.00,3),
--    (11,'menu item 4','menu item 4 for Rest 3','NON_VEG','MAIN_COURSE',650.00,3);

CREATE TABLE RESTAURANT (
  id SERIAL,
  name varchar(250) NOT NULL,
  location varchar(250) DEFAULT NULL,
  type_name varchar(100) DEFAULT NULL,
  CONSTRAINT restaurant_PK PRIMARY KEY (id)

);
create sequence RESTAURANT_SEQUENCE start with 1000;
--INSERT INTO restaurant VALUES
--    (1,'Akabar Jooje','Tehran','NON_VEG'),
--    (2,'Green Food','Tehran','VEG'),
--    (3,'Ziafat','Langrud','NON_VEG'),
--    (4,'Healthy Food','Esfarayen','VEG');


CREATE TABLE orders (
  id SERIAL,
  restaurant_id BIGINT NOT NULL,
  total decimal(10,2) DEFAULT NULL,
  user_id varchar(100) DEFAULT NULL,

  CONSTRAINT orders_PK PRIMARY KEY (id),
  CONSTRAINT order_FK FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
);


CREATE TABLE order_item (
  id SERIAL,
  order_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  price decimal(10,2) DEFAULT NULL,

  CONSTRAINT order_item_PK PRIMARY KEY (id),
  CONSTRAINT order_item_FK FOREIGN KEY (menu_item_id) REFERENCES menu_item (id),
  CONSTRAINT order_item_FK_1 FOREIGN KEY (order_id) REFERENCES orders (id)
);



