SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for 'dataItems'
-- ----------------------------
DROP TABLE IF EXISTS dataItems;
CREATE TABLE dataItems (
  itemName varchar(50) NOT NULL,
  category varchar(50) NOT NULL,
  description varchar(4096) NOT NULL,
  sort int(11)  NOT NULL,
  price double NOT NULL,
  image varchar(50) NOT NULL,
  PRIMARY KEY (itemName)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of 'tours'
-- ----------------------------
BEGIN;
INSERT INTO dataItems VALUES ('Quinoa Salmon Salad', 'Salads',
                'Our quinoa salad is served with quinoa, tomatoes, cucumber, scallions, and smoked salmon. Served with your choice of dressing.',
                1, 12, 'quinoa_salad.jpg');
			
INSERT INTO dataItems VALUES ("Chef's Salad", "Salads",
		"The chef’s salad has cucumber, tomatoes, red onions, mushrooms, hard-boiled eggs, cheese, and hot grilled chicken on a bed of romaine lettuce. Served with croutons and your choice of dressing.",
		2, 9, "chef_salad.jpg");

INSERT INTO dataItems VALUES ("House Salad", "Salads",
		"Our house salad is made with romaine lettuce and spinach, topped with tomatoes, cucumbers, red onions and carrots. Served with a dressing of your choice.",
		3, 7, "house_salad.jpg");

INSERT INTO dataItems VALUES ("Garden Buffet", "Salads",
		"Choose from our fresh local, organically grown ingredients to make a custom salad.",
		4, 10, "garden_buffet.jpg");

INSERT INTO dataItems VALUES ("Mini Cheeseburgers", "Starters",
		"These mini cheeseburgers are served on a fresh baked pretzel bun with lettuce, tomato, avocado, and your choice of cheese.",
		1, 8, "mini_cheeseburgers.jpg");

INSERT INTO dataItems VALUES ("Panko Stuffed Mushrooms", "Starters",
		"Large mushroom caps are filled a savory cream cheese, bacon and panko breadcrumb stuffing, topped with cheddar cheese.",
		2, 7, "stuffed_mushrooms.jpg");

INSERT INTO dataItems VALUES ("French Onion Soup", "Starters",
		"Caramelized onions slow cooked in a savory broth, topped with sourdough and a provolone cheese blend. Served with sourdough bread.",
		3, 7, "french_onion_soup.jpg");

INSERT INTO dataItems VALUES ("Artichokes with Garlic Aeoli", "Starters",
		"Our artichokes are brushed with an olive oil and rosemary blend and then broiled to perfection. Served with a side of creamy garlic aioli.",
		4, 9, "artichokes.jpg");

INSERT INTO dataItems VALUES ("Parmesan Deviled Eggs", "Starters",
		"These delectable little bites are made with organic eggs, fresh Parmesan, and chopped pine nuts.\"",
		5, 8, "deviled_eggs.jpg");

INSERT INTO dataItems VALUES ("Classic Burger", "Entrees",
		"Our classic burger is made with 100% pure angus beef, served with lettuce, tomatoes, onions, pickles, and cheese of your choice. Veggie burger available upon request. Served with French fries, fresh fruit, or a side salad.",
		1, 10, "classic_burger.jpg");

INSERT INTO dataItems VALUES ("Tomato Bruschetta Tortellini", "Entrees",
		"This classic cheese tortellini is cooked in a sundried tomato sauce. Served with bruschetta topped with a tomato and basil marinara.",
		2, 14, "tortellini.jpg");

INSERT INTO dataItems VALUES ("Handcrafted Pizza", "Entrees",
		"Our thin crust pizzas are made fresh daily and topped with your choices of fresh meats, veggies, cheese, and sauce.  Price includes two toppings. Add $1 for each additional topping.",
		3, 10, "pizza.jpg");

INSERT INTO dataItems VALUES ("Barbecued Tofu Skewers", "Entrees",
		"Our barbecued skewers include tofu, cherry tomatoes, bell peppers, and zucchini marinated in a ginger sesame sauce and charbroiled. Served with steamed rice.",
		4, 10, "tofu_skewers.jpg");

INSERT INTO dataItems VALUES ("Fiesta Family Platter", "Entrees",
		"This platter is perfect for sharing! Enjoy our spicy buffalo wings, traditional nachos, and cheese quesadillas served with freshly made guacamole dip.",
		5, 16, "fiesta_platter.jpg");

INSERT INTO dataItems VALUES ("Crème Brûlée", "Desserts",
		"Elegantly crafted creamy vanilla custard with a caramelized crunchy layer on top. Served with seasonal fruit.",
		1, 9, "creme_brulee.jpg");

INSERT INTO dataItems VALUES ("Cheesecake", "Desserts",
		"Our New York Style Cheesecake is rich, smooth, and creamy. Available in various flavors, and with seasonal fruit toppings.",
		2, 9, "cheesecake.jpg");

INSERT INTO dataItems VALUES ("Chocolate Chip Brownie ", "Desserts",
		"A warm chocolate chip brownie served with chocolate or vanilla ice cream and rich chocolate sauce.",
		3, 6, "brownie.jpg");

INSERT INTO dataItems VALUES ("Apple Pie", "Desserts",
		"Made with local granny smith apples to bring you the freshest classic apple pie available.",
		4, 5, "apple_pie.jpg");

INSERT INTO dataItems VALUES ("Mixed Berry Tart", "Desserts",
		"Raspberries, blueberries, and strawberries on top of a creamy filling served in a crispy tart.",
		5, 7, "berry_tart.jpg");

INSERT INTO dataItems VALUES ("Tropical Blue Smoothie", "Drinks",
		"This blueberry mint-based smoothie is refreshing and perfect for any celebration.",
		1, 6, "smoothie.jpg");

INSERT INTO dataItems VALUES ("Pomegranate Iced Tea", "Drinks",
		"Our unique blend of pomegranate juice, black Rubio, and mint tea creates this light fusion of flavors.  ",
		2, 4, "iced_tea.jpg");

INSERT INTO dataItems VALUES ("Café Latte", "Drinks",
		"Our house blend of espresso and foamed milk. Can be served with flavored syrups and over ice.  Non-dairy substitutions available upon request.",
		3, 6, "cafe_latte.jpg");

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;
