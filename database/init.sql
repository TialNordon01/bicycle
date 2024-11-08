CREATE DATABASE IF NOT EXISTS javafxTest;
USE javafxTest;

-- ----------------------------
-- Table structure for admin_code
-- ----------------------------
DROP TABLE IF EXISTS `admin_code`;
CREATE TABLE `admin_code`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin_code
-- ----------------------------
INSERT INTO `admin_code` VALUES ('45la874a');
INSERT INTO `admin_code` VALUES ('111222333');

-- ----------------------------
-- Table structure for pickup_point
-- ----------------------------
DROP TABLE IF EXISTS `pickup_point`;
CREATE TABLE `pickup_point`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pickup_point
-- ----------------------------
INSERT INTO `pickup_point` VALUES (1, 'Мещерский бульвар, 3к3');
INSERT INTO `pickup_point` VALUES (2, 'бул. Мира, 15');

-- ----------------------------
-- Table structure for manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `manufacturer`;
CREATE TABLE `manufacturer`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of manufacturer
-- ----------------------------
INSERT INTO `manufacturer` VALUES (1, 'Shimano');
INSERT INTO `manufacturer` VALUES (2, 'Sram');
INSERT INTO `manufacturer` VALUES (6, 'Tektro');
INSERT INTO `manufacturer` VALUES (7, 'M-Wave');
INSERT INTO `manufacturer` VALUES (8, 'Superwin');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (1, 'Shimano');
INSERT INTO `supplier` VALUES (2, 'Sram');
INSERT INTO `supplier` VALUES (3, 'Tektro');
INSERT INTO `supplier` VALUES (4, 'M-Wave');
INSERT INTO `supplier` VALUES (5, 'Superwin');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 'Переключатели задние');
INSERT INTO `category` VALUES (2, 'Тормозной диск');
INSERT INTO `category` VALUES (3, 'Система шатунов');
INSERT INTO `category` VALUES (4, 'Подседельный штырь');
INSERT INTO `category` VALUES (6, 'Шифтер');
INSERT INTO `category` VALUES (7, 'Переключатель передний');
INSERT INTO `category` VALUES (8, 'Тормоз диск. передний');
INSERT INTO `category` VALUES (9, 'Каретка');
INSERT INTO `category` VALUES (10, 'Цепь');
INSERT INTO `category` VALUES (11, 'Трещотка');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'Клиент');
INSERT INTO `role` VALUES (2, 'Сотрудник');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_role` int NOT NULL,
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_ibfk_1`(`id_role` ASC) USING BTREE,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 1, 'user', '123', 'Кирилл Петров Сергеевич', '+72838136261', 'yehilir-eha56@mail.com');
INSERT INTO `user` VALUES (2, 1, 'leralera', '7788999', 'Лера Русинова Петровна', '+76156849834', 'vuwuzid_apa29@gmail.com');
INSERT INTO `user` VALUES (3, 1, 'fidgi2001', '456456', 'Дмитрий Иванов Петрович', '+73311748806', 'hitef-usubi97@yahoo.com');
INSERT INTO `user` VALUES (4, 2, 'admin', '111', 'Михаил Беляев Сергеевич', '+77939960491', 'zanepox_oci20@yahoo.com');
INSERT INTO `user` VALUES (5, 2, 'jeromi', '123456789', 'Леонид Шустров Леонидович', '+76356506363', 'yohuza-zace24@mail.com');
INSERT INTO `user` VALUES (6, 2, 'lemma789', '7777777777', 'Максим Лашков Анатольевич', '+79412197239', 'gikisi-deba75@outlook.com');
INSERT INTO `user` VALUES (9, 1, 'a', 'a', 'a', '+78005553535', 'mail@mail.ru');
INSERT INTO `user` VALUES (10, 1, 'f', 'f', 'f', '4564545', 'log@log.ru');
INSERT INTO `user` VALUES (11, 1, 'dada', 'dada', 'dadada', '+666669666', 'mail@mail.ru');
INSERT INTO `user` VALUES (12, 1, 'michael', 'vbif2005', 'Беляев Михаил Сергеевич', '+78005553535', 'mail@mail.ru');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_supplier` int NOT NULL,
  `id_manufacturer` int NOT NULL,
  `id_category` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `specifications` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `article` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `count` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_ibfk_1`(`id_supplier` ASC) USING BTREE,
  INDEX `product_ibfk_2`(`id_manufacturer` ASC) USING BTREE,
  INDEX `product_ibfk_3`(`id_category` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`id_manufacturer`) REFERENCES `manufacturer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_ibfk_3` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (5, 3, 6, 2, 'Тормозной диск Tektro TR160-24', '160mm, 6 болтов', 2030.00, '70506\r\n', 40);
INSERT INTO `product` VALUES (6, 1, 1, 3, 'Система шатунов Shimano Acera M361', '175 мм, Кв, 42/32/22Т, защита', 6900.00, '22342', 6);
INSERT INTO `product` VALUES (7, 1, 1, 1, 'Переключатель задний Shimano Acera, M3020, SGS', '7/8 ск. б/уп.', 4600.00, '79080', 18);
INSERT INTO `product` VALUES (8, 3, 6, 2, 'Тормозной диск Tektro TR180-24', '180mm, 6 болтов', 2300.00, '70507', 0);
INSERT INTO `product` VALUES (9, 4, 7, 4, 'Подседельный штырь M-Wave', '30.9, 350 мм', 2050.00, '76909', 65);
INSERT INTO `product` VALUES (10, 1, 1, 6, 'Шифтер Shimano Deore, M4100-R', 'прав, 10 ск, на хомут, с индикатором, тр. 2050 мм с SP41 1880 мм', 5700.00, '67350', 12);
INSERT INTO `product` VALUES (11, 1, 1, 7, 'Переключатель передний Shimano Tourney, TY500', 'универс. тяга, хомут 31,8, уг.:66-69, для 42T, б/уп.', 1250.00, '75520', 14);
INSERT INTO `product` VALUES (12, 3, 6, 8, 'Тормоз диск. передний Tektro HD-M275', '800 мм, zip-пакет', 4500.00, '76075', 31);
INSERT INTO `product` VALUES (14, 1, 1, 9, 'Каретка Shimano, BB52', 'MTB, BSA', 3450.00, '28394', 56);
INSERT INTO `product` VALUES (15, 1, 1, 10, 'Цепь Shimano Deore, HG53', '9ск, 116 зв., амп.пин (1шт)', 4600.00, '47469', 32);
INSERT INTO `product` VALUES (16, 5, 8, 11, 'Трещотка Superwin KFW-884', '13-28Т 8 ск., сталь', 1050.00, '42196', 11);
INSERT INTO `product` VALUES (17, 1, 1, 10, 'Цепь Shimano HG701', '11ск 116зв амп пин 1шт ROAD/MTB/E-BIKE совмест.', 8850.00, '37057', 25);

-- ----------------------------
-- Table structure for order_product
-- ----------------------------
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product`  (
  `id_product` int NOT NULL,
  `id_order` int NOT NULL,
  INDEX `order_product_ibfk_1`(`id_order` ASC) USING BTREE,
  INDEX `order_product_ibfk_2`(`id_product` ASC) USING BTREE,
  CONSTRAINT `order_product_ibfk_1` FOREIGN KEY (`id_order`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_product_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_product
-- ----------------------------
INSERT INTO `order_product` VALUES (7, 19);
INSERT INTO `order_product` VALUES (5, 19);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_pickup_point` int NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_ibfk_1`(`id_pickup_point` ASC) USING BTREE,
  INDEX `order_ibfk_2`(`id_user` ASC) USING BTREE,
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`id_pickup_point`) REFERENCES `pickup_point` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (19, 1, 2, 'Новый', 6630.00);

-- ----------------------------
-- Table structure for refund
-- ----------------------------
DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund`  (
  `id_order` int NOT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `refund_ibfk_1`(`id_order` ASC) USING BTREE,
  CONSTRAINT `refund_ibfk_1` FOREIGN KEY (`id_order`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of refund
-- ----------------------------
INSERT INTO `refund` VALUES (19, 'sss');

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `check_product_count_insert`;
delimiter ;;
CREATE TRIGGER `check_product_count_insert` BEFORE INSERT ON `order_product` FOR EACH ROW BEGIN
   IF (SELECT count FROM product WHERE id = NEW.id_product) <= 0 THEN
       SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert product with count less than or equal to 0';
   END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `decrement_product_count_insert`;
delimiter ;;
CREATE TRIGGER `decrement_product_count_insert` AFTER INSERT ON `order_product` FOR EACH ROW BEGIN
  UPDATE product
  SET count = count - 1
  WHERE id = NEW.id_product;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `update_order_price_insert`;
delimiter ;;
CREATE TRIGGER `update_order_price_insert` AFTER INSERT ON `order_product` FOR EACH ROW BEGIN
   UPDATE `order`
   SET price = (SELECT SUM(product.price)
							 FROM product
							 JOIN order_product ON product.id = id_product
							 WHERE id_order = NEW.id_order)
	 WHERE id = NEW.id_order;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `check_product_count_update`;
delimiter ;;
CREATE TRIGGER `check_product_count_update` BEFORE UPDATE ON `order_product` FOR EACH ROW BEGIN
   IF (SELECT count FROM product WHERE id = NEW.id_product) <= 0 THEN
       SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot update product with count less than or equal to 0';
   END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `decrement_product_count_update`;
delimiter ;;
CREATE TRIGGER `decrement_product_count_update` AFTER UPDATE ON `order_product` FOR EACH ROW BEGIN
  UPDATE product
  SET count = count - 1
  WHERE id = NEW.id_product;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `remove_empty_orders_update`;
delimiter ;;
CREATE TRIGGER `remove_empty_orders_update` AFTER UPDATE ON `order_product` FOR EACH ROW BEGIN
	IF (SELECT COUNT(*) FROM order_product WHERE id_order = OLD.id_order) = 0 THEN
   DELETE FROM `order` WHERE id = OLD.id_order;
 END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `update_order_price_update`;
delimiter ;;
CREATE TRIGGER `update_order_price_update` AFTER UPDATE ON `order_product` FOR EACH ROW BEGIN
   UPDATE `order`
   SET price = (SELECT SUM(product.price)
							 FROM product
							 JOIN order_product ON product.id = id_product
							 WHERE id_order = NEW.id_order)
	 WHERE id = NEW.id_order;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `increment_product_count_delete`;
delimiter ;;
CREATE TRIGGER `increment_product_count_delete` AFTER DELETE ON `order_product` FOR EACH ROW BEGIN
  UPDATE product
  SET count = count + 1
  WHERE id = OLD.id_product;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_product
-- ----------------------------
DROP TRIGGER IF EXISTS `remove_empty_orders_delete`;
delimiter ;;
CREATE TRIGGER `remove_empty_orders_delete` AFTER DELETE ON `order_product` FOR EACH ROW BEGIN
	IF (SELECT COUNT(*) FROM order_product WHERE id_order = OLD.id_order) = 0 THEN
   DELETE FROM `order` WHERE id = OLD.id_order;
 END IF;
END
;;
delimiter ;
