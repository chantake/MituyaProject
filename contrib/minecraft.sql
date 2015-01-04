/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : minecraft_test

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2015-01-04 19:38:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `api`
-- ----------------------------
DROP TABLE IF EXISTS `api`;
CREATE TABLE `api` (
  `tokenKey` varchar(100) NOT NULL,
  `tokenSecret` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ver` varchar(100) DEFAULT NULL,
  `autor` varchar(100) DEFAULT NULL,
  `web` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tokenKey`,`tokenSecret`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api
-- ----------------------------

-- ----------------------------
-- Table structure for `blacklist_events`
-- ----------------------------
DROP TABLE IF EXISTS `blacklist_events`;
CREATE TABLE `blacklist_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event` varchar(25) CHARACTER SET latin1 NOT NULL,
  `player` varchar(16) CHARACTER SET latin1 NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `item` int(11) NOT NULL,
  `time` int(11) NOT NULL,
  `comment` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blacklist_events
-- ----------------------------

-- ----------------------------
-- Table structure for `characters`
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `id` int(250) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL DEFAULT 'Player',
  `disname` varchar(200) DEFAULT 'Player',
  `nickname` varchar(200) DEFAULT NULL,
  `ip` varchar(128) NOT NULL DEFAULT 'Player',
  `world` int(4) NOT NULL DEFAULT '0',
  `pass` varchar(128) DEFAULT '',
  `home` int(11) NOT NULL DEFAULT '-1',
  `rank` varchar(100) NOT NULL DEFAULT 'default',
  `oldrank` int(11) NOT NULL DEFAULT '-1',
  `lv` int(11) NOT NULL DEFAULT '-1',
  `spawnlocation` int(11) NOT NULL DEFAULT '-1',
  `templocation` int(11) NOT NULL DEFAULT '-1',
  `money` int(11) NOT NULL DEFAULT '-1',
  `tax` int(11) NOT NULL DEFAULT '-1',
  `gobaku` tinyint(1) NOT NULL DEFAULT '0',
  `loginmessage` varchar(400) NOT NULL DEFAULT 'Player',
  `guild` int(11) NOT NULL DEFAULT '-1',
  `tppublic` tinyint(1) NOT NULL DEFAULT '0',
  `check` tinyint(1) NOT NULL DEFAULT '0',
  `pvp` tinyint(1) NOT NULL DEFAULT '0',
  `ime` tinyint(1) NOT NULL DEFAULT '1',
  `world_invite` tinyint(1) NOT NULL DEFAULT '0',
  `deathmessage` varchar(400) NOT NULL,
  `mp` int(11) NOT NULL DEFAULT '0',
  `max_mp` int(11) NOT NULL,
  `lastworld` int(11) NOT NULL,
  `lastconnect` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sponge` tinyint(1) NOT NULL DEFAULT '0',
  `firstlogin` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters
-- ----------------------------

-- ----------------------------
-- Table structure for `chestshop`
-- ----------------------------
DROP TABLE IF EXISTS `chestshop`;
CREATE TABLE `chestshop` (
  `id` varchar(128) NOT NULL,
  `owner` varchar(128) NOT NULL,
  `typeid` int(11) NOT NULL,
  `data` int(11) DEFAULT '0',
  `amount` int(11) DEFAULT '1',
  `buy` int(11) NOT NULL,
  `sell` int(11) NOT NULL,
  `world` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `official` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chestshop
-- ----------------------------

-- ----------------------------
-- Table structure for `command_level`
-- ----------------------------
DROP TABLE IF EXISTS `command_level`;
CREATE TABLE `command_level` (
  `id` bigint(20) NOT NULL,
  `name` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of command_level
-- ----------------------------
INSERT INTO `command_level` VALUES ('0', '使用不可');
INSERT INTO `command_level` VALUES ('1', '一般');
INSERT INTO `command_level` VALUES ('10', 'ＧＭ以上');

-- ----------------------------
-- Table structure for `command_list`
-- ----------------------------
DROP TABLE IF EXISTS `command_list`;
CREATE TABLE `command_list` (
  `id` bigint(20) NOT NULL,
  `type_id` bigint(4) NOT NULL,
  `cmd` text,
  `level_id` bigint(4) DEFAULT NULL,
  `alias` text,
  `cost` bigint(20) DEFAULT NULL,
  `comment` text,
  `enable` tinyint(4) DEFAULT NULL,
  `add_dtm` datetime DEFAULT NULL,
  `sort_index` bigint(20) DEFAULT NULL,
  `url_url` text,
  `url_name` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of command_list
-- ----------------------------
INSERT INTO `command_list` VALUES ('1001', '1', '/save location', '1', '無し', '0', '現在の位置を記憶する', '1', '2011-02-25 00:00:00', '1', null, null);
INSERT INTO `command_list` VALUES ('1002', '1', '/tp location', '1', '無し', '0', '\"/save location\"で記憶した場所に移動する', '1', '2011-02-25 00:00:00', '2', null, null);
INSERT INTO `command_list` VALUES ('1003', '1', '/tp public', '1', '無し', '0', '自分へのtpの許可・禁止の設定', '1', '2011-02-25 00:00:00', '3', null, null);
INSERT INTO `command_list` VALUES ('1004', '1', '/tp [ユーザ名]', '1', '無し', '0', '指定ユーザの居る場所へ移動する\r\n(相手が/tp publicで許可していないと実行不能)', '1', '2011-02-25 00:00:00', '4', null, null);
INSERT INTO `command_list` VALUES ('1005', '1', '/spawn', '1', '無し', '0', 'スポーン位置へ移動する', '1', '2011-02-25 00:00:00', '5', null, null);
INSERT INTO `command_list` VALUES ('1006', '1', '/tphere [ユーザ名１] [ユーザ名2]', '10', '/s', '0', 'ユーザ名１で指定したプレイヤーをユーザ名２で指定したプレイヤーの位置に移動する\r\n', '1', '2011-02-25 00:00:00', '6', null, null);
INSERT INTO `command_list` VALUES ('3001', '3', '/home set', '1', '/sethome', '0', '現在位置をプレイヤーのホームとしてセットします', '1', '2011-02-25 00:00:00', '1', null, null);
INSERT INTO `command_list` VALUES ('3002', '3', '/home public', '1', '無し', '0', 'セットしたホームの共有許可・禁止の設定', '1', '2011-02-25 00:00:00', '2', null, null);
INSERT INTO `command_list` VALUES ('3003', '3', '/home', '1', '無し', '0', '\"/home set\"で設定したホームポジションへ移動します\r\n(ホームポジションをセットしていない場合は使用できません)', '1', '2011-02-25 00:00:00', '3', null, null);
INSERT INTO `command_list` VALUES ('3004', '3', '/home [ユーザ名]', '1', '無し', '0', '指定したプレイヤーのホームポジションへ移動します\r\n(指定したプレイヤーがホームをセットしていない場合や、\r\n非共有ホームとしてセットしている場合は使用できません)', '1', '2011-02-25 00:00:00', '4', null, null);
INSERT INTO `command_list` VALUES ('3005', '3', '/home info', '1', '無し', '0', '自分のホーム情報を表示します', '1', '2011-02-25 00:00:00', '5', null, null);
INSERT INTO `command_list` VALUES ('3006', '3', '/home info [ユーザ名]', '1', '無し', '0', 'ユーザ名で指定したプレイヤーのホーム情報を表示します', '1', '2011-02-25 00:00:00', '6', null, null);
INSERT INTO `command_list` VALUES ('4001', '4', '/region define [エリア名] [ユーザ名]', '1', '無し', '0', '木の斧を使って選択されている範囲をエリア名で指定された名前で保護し、\r\nユーザ名で指定したプレイヤーにオーナー権限を与えます。\r\n詳しくは参考ＵＲＬを参照してください。', '1', '2011-02-25 00:00:00', '1', 'http://328mss.com/minecraft/AreaProtection.html', 'エリア保護の仕方');
INSERT INTO `command_list` VALUES ('4002', '4', '/region addowner [エリア名] [ユーザ名]', '1', '無し', '0', null, '1', '2011-02-25 00:00:00', '2', null, null);

-- ----------------------------
-- Table structure for `command_type`
-- ----------------------------
DROP TABLE IF EXISTS `command_type`;
CREATE TABLE `command_type` (
  `id` bigint(4) NOT NULL,
  `name` text NOT NULL,
  `sort_index` bigint(20) NOT NULL DEFAULT '0',
  `enable` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of command_type
-- ----------------------------
INSERT INTO `command_type` VALUES ('1', '移動系コマンド', '2', '1');
INSERT INTO `command_type` VALUES ('2', 'システム系コマンド', '4', '1');
INSERT INTO `command_type` VALUES ('3', 'ホームコマンド', '1', '1');
INSERT INTO `command_type` VALUES ('4', 'エリア保護コマンド', '3', '1');
INSERT INTO `command_type` VALUES ('100', 'その他', '5', '0');

-- ----------------------------
-- Table structure for `enchantment`
-- ----------------------------
DROP TABLE IF EXISTS `enchantment`;
CREATE TABLE `enchantment` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` text,
  `name_jp` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of enchantment
-- ----------------------------
INSERT INTO `enchantment` VALUES ('0', 'PROTECTION_ENVIRONMENTAL', 'ダメージ軽減');
INSERT INTO `enchantment` VALUES ('1', 'PROTECTION_FIRE ', '火炎耐性');
INSERT INTO `enchantment` VALUES ('2', 'PROTECTION_FALL', '落下耐性');
INSERT INTO `enchantment` VALUES ('3', 'PROTECTION_EXPLOSIONS', '爆破耐性');
INSERT INTO `enchantment` VALUES ('4', 'PROTECTION_PROJECTILE ', '飛び道具耐性');
INSERT INTO `enchantment` VALUES ('5', 'OXYGEN ', '水中呼吸');
INSERT INTO `enchantment` VALUES ('6', 'WATER_WORKER ', '水中採掘');
INSERT INTO `enchantment` VALUES ('7', 'THORNS', 'トゲ');
INSERT INTO `enchantment` VALUES ('16', 'DAMAGE_ALL', 'ダメージ増加');
INSERT INTO `enchantment` VALUES ('17', 'DAMAGE_UNDEAD ', 'アンデッド特攻');
INSERT INTO `enchantment` VALUES ('18', 'DAMAGE_ARTHROPODS ', '虫特攻');
INSERT INTO `enchantment` VALUES ('19', 'KNOCKBACK ', 'ノックバック');
INSERT INTO `enchantment` VALUES ('20', 'FIRE_ASPECT', '火属性');
INSERT INTO `enchantment` VALUES ('21', 'LOOT_BONUS_MOBS ', 'ドロップ増加');
INSERT INTO `enchantment` VALUES ('32', 'DIG_SPEED ', '効率強化');
INSERT INTO `enchantment` VALUES ('33', 'SILK_TOUCH ', 'シルクタッチ');
INSERT INTO `enchantment` VALUES ('34', 'DURABILITY ', '耐久力');
INSERT INTO `enchantment` VALUES ('35', 'LOOT_BONUS_BLOCKS ', '幸運');
INSERT INTO `enchantment` VALUES ('48', 'ARROW_DAMAGE', 'ダメージ増加');
INSERT INTO `enchantment` VALUES ('49', 'ARROW_KNOCKBACK ', 'パンチ');
INSERT INTO `enchantment` VALUES ('50', 'ARROW_FIRE ', 'フレイム');
INSERT INTO `enchantment` VALUES ('51', 'ARROW_INFINITE ', '無限');

-- ----------------------------
-- Table structure for `gachapon_capsule`
-- ----------------------------
DROP TABLE IF EXISTS `gachapon_capsule`;
CREATE TABLE `gachapon_capsule` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `info` text,
  `rate` int(11) NOT NULL,
  `publish_flg` tinyint(4) NOT NULL DEFAULT '0',
  `secret` tinyint(4) NOT NULL DEFAULT '1',
  `phase` int(11) NOT NULL,
  `delete` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gachapon_capsule
-- ----------------------------
INSERT INTO `gachapon_capsule` VALUES ('1', 'ミツヤソード', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('2', 'ミツヤヘルメット', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('3', 'ミツヤチェストプレート', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('4', 'ミツヤレギンス', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('5', 'ミツヤブーツ', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('6', '木製バット', null, '30', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('7', '金属バット', null, '30', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('8', '電子工作セット', null, '40', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('9', 'ビーコンセットLV1', null, '36', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('10', 'ビーコンセットLV2', null, '18', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('11', 'ビーコンセットLV3', null, '9', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('12', 'ビーコンセットLV4', null, '4', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('13', 'ハナハナ', null, '40', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('14', '音楽鑑賞セットⅠ', null, '20', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('15', '音楽鑑賞セットⅡ', null, '20', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('16', 'OK牧場セット', null, '40', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('17', '焼肉セット', null, '60', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('18', 'BBQセット', null, '60', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('19', 'マインカートマニア', null, '50', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('20', 'ハズレ', null, '1', '1', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('21', '乗豚セットⅠ', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('22', '乗豚セットⅡ', null, '35', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('23', '探検セット', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('24', '朽ちたミツヤソード', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('25', '朽ちたミツヤヘルメット', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('26', '朽ちたミツヤチェストプレート', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('27', '朽ちたミツヤレギンス', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('28', '朽ちたミツヤブーツ', null, '66', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('29', '音楽鑑賞セットⅢ', null, '20', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('30', '音楽鑑賞セットⅣ', null, '20', '0', '0', '1', '0');
INSERT INTO `gachapon_capsule` VALUES ('31', 'ミツヤピッケルS', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('32', 'ミツヤピッケルF', null, '1', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('33', 'ミツヤピッケル', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('34', 'ミツヤシャベル', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('35', 'ミツヤアックス', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('36', 'ミツヤボウ', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('37', 'ミツヤシザー', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('38', '朽ちたミツヤピッケルS', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('39', '朽ちたミツヤピッケルF', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('40', '朽ちたミツヤピッケル', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('41', '朽ちたミツヤシャベル', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('42', '朽ちたミツヤアックス', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('43', '朽ちたミツヤボウ', null, '40', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('44', '朽ちたミツヤシザー', null, '20', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('45', 'スペシャルハナハナ', null, '130', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('46', 'カラフルⅠ', null, '135', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('47', 'カラフルⅡ', null, '135', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('48', 'カラフルⅢ', null, '135', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('49', 'カラフルⅣ', null, '135', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('50', '花火職人セットⅠ', null, '71', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('51', '花火職人セットⅡ', null, '71', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('52', '花火職人セットⅢ', null, '71', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('53', '花火職人セットⅣ', null, '71', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('54', '花火職人セットⅤ', null, '71', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('55', 'ハズレ', null, '2', '1', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('56', 'ナイトビジョン10', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('57', '|・)つはい、ポーション', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('58', '新生活応援セット', null, '80', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('59', '木こりセット', null, '130', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('60', '農業セット', null, '130', '0', '0', '2', '0');
INSERT INTO `gachapon_capsule` VALUES ('61', 'ミツヤピッケルS改', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('62', 'ミツヤピッケルF改', null, '1', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('63', 'ミツヤピッケル改', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('64', 'ミツヤシャベル改', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('65', 'ミツヤアックス改', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('66', '木製バット改', null, '60', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('67', '金属バット改', null, '60', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('68', '朽ちたミツヤピッケルS改', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('69', '朽ちたミツヤピッケルF改', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('70', '朽ちたミツヤピッケル改', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('71', '朽ちたミツヤシャベル改', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('72', '朽ちたミツヤアックス改', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('73', 'スペシャルハナハナⅡ', null, '130', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('74', 'サンサンハナハナ', null, '130', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('75', 'ハズレ', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('76', '乗馬セットI', null, '60', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('77', '乗馬セットG', null, '45', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('78', '乗馬セットD', null, '30', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('79', '乗馬セットSP', null, '15', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('80', 'カラフルグラスⅠ', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('81', 'カラフルグラスⅡ', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('82', 'カラフルグラスⅢ', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('83', 'カラフルグラスⅣ', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('84', 'グラスホッパー', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('85', '電子工作セットSP', null, '30', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('86', 'ゴールドラーーーッシュ！！', null, '10', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('87', 'アタリ', null, '2', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('88', 'ウッーウッーウマウマ(ﾟ∀ﾟ)', null, '4', '1', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('89', 'おさかな天国', null, '80', '0', '1', '3', '0');
INSERT INTO `gachapon_capsule` VALUES ('90', '景気回復！', null, '80', '0', '1', '3', '0');

-- ----------------------------
-- Table structure for `gachapon_complete_player`
-- ----------------------------
DROP TABLE IF EXISTS `gachapon_complete_player`;
CREATE TABLE `gachapon_complete_player` (
  `player` text NOT NULL,
  `phase` int(11) NOT NULL,
  `DTM` datetime DEFAULT NULL,
  PRIMARY KEY (`player`(255),`phase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gachapon_complete_player
-- ----------------------------

-- ----------------------------
-- Table structure for `gachapon_config`
-- ----------------------------
DROP TABLE IF EXISTS `gachapon_config`;
CREATE TABLE `gachapon_config` (
  `key` text NOT NULL,
  `phase` int(11) NOT NULL,
  `value_i` int(11) DEFAULT NULL,
  `value_t` text,
  PRIMARY KEY (`key`(255),`phase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gachapon_config
-- ----------------------------
INSERT INTO `gachapon_config` VALUES ('buyinterval', '0', '0', null);
INSERT INTO `gachapon_config` VALUES ('defaultphase', '0', '2', null);
INSERT INTO `gachapon_config` VALUES ('min_buytps', '0', '0', null);
INSERT INTO `gachapon_config` VALUES ('price', '1', '5000', null);
INSERT INTO `gachapon_config` VALUES ('price', '2', '5000', null);
INSERT INTO `gachapon_config` VALUES ('price', '3', '10000', null);
INSERT INTO `gachapon_config` VALUES ('sale', '1', '1', null);
INSERT INTO `gachapon_config` VALUES ('sale', '2', '1', null);
INSERT INTO `gachapon_config` VALUES ('sale', '3', '0', null);

-- ----------------------------
-- Table structure for `gachapon_description`
-- ----------------------------
DROP TABLE IF EXISTS `gachapon_description`;
CREATE TABLE `gachapon_description` (
  `id` int(11) NOT NULL,
  `capsule_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `type` smallint(6) NOT NULL DEFAULT '0',
  `durability` smallint(6) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL,
  `enchantment` text,
  `name` text,
  `denyrename` tinyint(4) DEFAULT '0',
  `info` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gachapon_description
-- ----------------------------
INSERT INTO `gachapon_description` VALUES ('1', '1', '276', '0', '0', '1', '16,10,17,10,18,10,20,10,21,10,34,10', 'ミツヤソード', '1', null);
INSERT INTO `gachapon_description` VALUES ('2', '2', '310', '0', '0', '1', '0,10,1,10,3,10,4,10,5,10,6,10,7,10,34,10', 'ミツヤヘルメット', '1', null);
INSERT INTO `gachapon_description` VALUES ('3', '3', '311', '0', '0', '1', '0,10,1,10,3,10,4,10,34,10,7,10', 'ミツヤチェストプレート', '1', null);
INSERT INTO `gachapon_description` VALUES ('4', '4', '312', '0', '0', '1', '0,10,1,10,3,10,4,10,34,10,7,10', 'ミツヤレギンス', '1', null);
INSERT INTO `gachapon_description` VALUES ('5', '5', '313', '0', '0', '1', '0,10,1,10,3,10,4,10,34,10,7,10,2,10', 'ミツヤブーツ', '1', null);
INSERT INTO `gachapon_description` VALUES ('6', '6', '268', '0', '0', '1', '19,10,34,10', '木製バット', '1', null);
INSERT INTO `gachapon_description` VALUES ('7', '7', '267', '0', '0', '1', '19,10,34,10', '金属バット', '1', null);
INSERT INTO `gachapon_description` VALUES ('8', '8', '331', '0', '0', '64', null, 'レッドストーン', '0', null);
INSERT INTO `gachapon_description` VALUES ('9', '8', '356', '0', '0', '10', null, 'リピーター', '0', null);
INSERT INTO `gachapon_description` VALUES ('10', '8', '76', '0', '0', '10', null, 'レッドストーントーチ', '0', null);
INSERT INTO `gachapon_description` VALUES ('11', '8', '29', '0', '0', '5', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('12', '8', '33', '0', '0', '5', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('13', '8', '69', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('14', '8', '77', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('15', '9', '138', '0', '0', '1', null, 'ビーコン', '0', null);
INSERT INTO `gachapon_description` VALUES ('16', '9', '41', '0', '0', '9', null, '金ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('17', '10', '138', '0', '0', '1', null, 'ビーコン', '0', null);
INSERT INTO `gachapon_description` VALUES ('18', '10', '41', '0', '0', '34', null, '金ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('19', '11', '138', '0', '0', '1', null, 'ビーコン', '0', null);
INSERT INTO `gachapon_description` VALUES ('20', '11', '41', '0', '0', '83', null, '金ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('21', '12', '138', '0', '0', '1', null, 'ビーコン', '0', null);
INSERT INTO `gachapon_description` VALUES ('22', '12', '41', '0', '0', '164', null, '金ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('23', '9', '266', '0', '0', '1', null, '金インゴット', '0', null);
INSERT INTO `gachapon_description` VALUES ('24', '10', '266', '0', '0', '1', null, '金インゴット', '0', null);
INSERT INTO `gachapon_description` VALUES ('25', '11', '266', '0', '0', '1', null, '金インゴット', '0', null);
INSERT INTO `gachapon_description` VALUES ('26', '12', '266', '0', '0', '1', null, '金インゴット', '0', null);
INSERT INTO `gachapon_description` VALUES ('27', '13', '37', '0', '0', '128', null, '黄色のハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('28', '13', '38', '0', '0', '128', null, '赤色のハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('29', '14', '84', '0', '0', '1', null, 'ジュークボックス', '0', null);
INSERT INTO `gachapon_description` VALUES ('30', '14', '2256', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('31', '14', '2257', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('32', '14', '2258', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('33', '15', '84', '0', '0', '1', null, 'ジュークボックス', '0', null);
INSERT INTO `gachapon_description` VALUES ('34', '15', '2259', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('35', '15', '2260', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('36', '15', '2261', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('53', '16', '2', '0', '0', '128', null, '草土', '0', null);
INSERT INTO `gachapon_description` VALUES ('54', '16', '85', '0', '0', '64', null, 'フェンス', '0', null);
INSERT INTO `gachapon_description` VALUES ('55', '16', '107', '0', '0', '2', null, 'フェンスゲート', '0', null);
INSERT INTO `gachapon_description` VALUES ('56', '16', '325', '0', '0', '1', null, 'バケツ', '0', null);
INSERT INTO `gachapon_description` VALUES ('57', '16', '281', '0', '0', '1', null, 'ボウル', '0', null);
INSERT INTO `gachapon_description` VALUES ('58', '16', '383', '92', '0', '2', null, 'スポーンエッグ（牛）', '0', null);
INSERT INTO `gachapon_description` VALUES ('59', '16', '383', '96', '0', '2', null, 'スポーンエッグ(きのこ牛)', '0', null);
INSERT INTO `gachapon_description` VALUES ('60', '17', '320', '0', '0', '64', null, '豚肉焼肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('61', '17', '364', '0', '0', '64', null, '牛肉焼肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('62', '17', '366', '0', '0', '64', null, 'チキン焼肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('63', '18', '61', '0', '0', '1', null, 'かまど', '0', null);
INSERT INTO `gachapon_description` VALUES ('64', '18', '263', '1', '0', '64', null, '木炭', '0', null);
INSERT INTO `gachapon_description` VALUES ('65', '18', '319', '0', '0', '64', null, '豚肉生肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('66', '18', '363', '0', '0', '64', null, '牛肉生肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('67', '18', '365', '0', '0', '64', null, 'チキン生肉', '0', null);
INSERT INTO `gachapon_description` VALUES ('68', '19', '66', '0', '0', '256', null, '線路', '0', null);
INSERT INTO `gachapon_description` VALUES ('69', '19', '27', '0', '0', '10', null, 'パワーレール', '0', null);
INSERT INTO `gachapon_description` VALUES ('70', '19', '28', '0', '0', '10', null, 'スイッチレール', '0', null);
INSERT INTO `gachapon_description` VALUES ('71', '19', '76', '0', '0', '10', null, 'レッドストーントーチ', '0', null);
INSERT INTO `gachapon_description` VALUES ('72', '19', '328', '0', '0', '4', null, 'マインカート', '0', null);
INSERT INTO `gachapon_description` VALUES ('73', '21', '383', '90', '0', '1', null, 'スポーンエッグ(豚)', '0', null);
INSERT INTO `gachapon_description` VALUES ('74', '21', '329', '0', '0', '1', null, 'サドル', '0', null);
INSERT INTO `gachapon_description` VALUES ('75', '22', '383', '90', '0', '1', null, 'スポーンエッグ(豚)', '0', null);
INSERT INTO `gachapon_description` VALUES ('76', '22', '329', '0', '0', '1', null, 'サドル', '0', null);
INSERT INTO `gachapon_description` VALUES ('77', '22', '398', '0', '0', '1', null, 'キャロットスティック', '0', null);
INSERT INTO `gachapon_description` VALUES ('78', '23', '50', '0', '0', '64', null, 'トーチ', '0', null);
INSERT INTO `gachapon_description` VALUES ('79', '23', '345', '0', '0', '1', null, 'コンパス', '0', null);
INSERT INTO `gachapon_description` VALUES ('80', '23', '347', '0', '0', '1', null, '時計', '0', null);
INSERT INTO `gachapon_description` VALUES ('81', '23', '358', '0', '0', '1', null, '地図', '0', null);
INSERT INTO `gachapon_description` VALUES ('82', '24', '283', '0', '50', '1', '16,10,17,10,18,10,20,10,21,10,34,10', '朽ちたミツヤソード', '1', null);
INSERT INTO `gachapon_description` VALUES ('83', '25', '314', '0', '80', '1', '0,10,1,10,3,10,4,10,5,10,6,10,7,10,34,10', '朽ちたミツヤヘルメット', '1', null);
INSERT INTO `gachapon_description` VALUES ('84', '26', '315', '0', '110', '1', '0,10,1,10,3,10,4,10,34,10,7,10', '朽ちたミツヤチェストプレート', '1', null);
INSERT INTO `gachapon_description` VALUES ('85', '27', '316', '0', '110', '1', '0,10,1,10,3,10,4,10,34,10,7,10', '朽ちたミツヤレギンス', '1', null);
INSERT INTO `gachapon_description` VALUES ('86', '28', '317', '0', '110', '1', '0,10,1,10,3,10,4,10,34,10,7,10,2,10', '朽ちたミツヤブーツ', '1', null);
INSERT INTO `gachapon_description` VALUES ('87', '29', '84', '0', '0', '1', null, 'ジュークボックス', '0', null);
INSERT INTO `gachapon_description` VALUES ('88', '29', '2262', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('89', '29', '2263', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('90', '29', '2264', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('91', '30', '84', '0', '0', '1', null, 'ジュークボックス', '0', null);
INSERT INTO `gachapon_description` VALUES ('103', '30', '2265', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('104', '30', '2266', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('105', '30', '2267', '0', '0', '1', null, null, '0', null);
INSERT INTO `gachapon_description` VALUES ('106', '31', '278', '0', '0', '1', '32,10,34,10,33,10', 'ミツヤピッケルS', '1', null);
INSERT INTO `gachapon_description` VALUES ('107', '32', '278', '0', '0', '1', '32,10,34,10,35,10', 'ミツヤピッケルF', '1', null);
INSERT INTO `gachapon_description` VALUES ('108', '33', '278', '0', '0', '1', '32,10,34,10', 'ミツヤピッケル', '1', null);
INSERT INTO `gachapon_description` VALUES ('109', '34', '277', '0', '0', '1', '32,10,34,10,33,10', 'ミツヤシャベル', '1', null);
INSERT INTO `gachapon_description` VALUES ('110', '35', '279', '0', '0', '1', '32,10,34,10,16,10,33,10', 'ミツヤアックス', '1', null);
INSERT INTO `gachapon_description` VALUES ('111', '36', '261', '0', '0', '1', '48,10,49,10,50,10,51,10,34,10', 'ミツヤボウ', '1', null);
INSERT INTO `gachapon_description` VALUES ('112', '37', '359', '0', '0', '1', '32,10,33,10,34,10', 'ミツヤシザー', '1', null);
INSERT INTO `gachapon_description` VALUES ('113', '38', '285', '0', '31', '1', '32,10,34,10,33,10', '朽ちたミツヤピッケルS', '1', null);
INSERT INTO `gachapon_description` VALUES ('114', '39', '285', '0', '31', '1', '32,10,34,10,35,10', '朽ちたミツヤピッケルF', '1', null);
INSERT INTO `gachapon_description` VALUES ('115', '40', '285', '0', '31', '1', '32,10,34,10', '朽ちたミツヤピッケル', '1', null);
INSERT INTO `gachapon_description` VALUES ('116', '41', '284', '0', '31', '1', '32,10,34,10,33,10', '朽ちたミツヤシャベル', '1', null);
INSERT INTO `gachapon_description` VALUES ('117', '42', '286', '0', '31', '1', '32,10,34,10,16,10,33,10', '朽ちたミツヤアックス', '1', null);
INSERT INTO `gachapon_description` VALUES ('118', '43', '261', '0', '383', '1', '48,10,49,10,50,10,51,10,34,10', '朽ちたミツヤボウ', '1', null);
INSERT INTO `gachapon_description` VALUES ('119', '44', '359', '0', '237', '1', '32,10,33,10,34,10', '朽ちたミツヤシザー', '0', null);
INSERT INTO `gachapon_description` VALUES ('120', '45', '37', '0', '0', '64', null, '黄色の花', '0', null);
INSERT INTO `gachapon_description` VALUES ('121', '45', '38', '0', '0', '64', null, '赤色の花', '0', null);
INSERT INTO `gachapon_description` VALUES ('122', '45', '390', '0', '0', '128', null, '鉢植え', '0', null);
INSERT INTO `gachapon_description` VALUES ('123', '46', '351', '0', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('124', '46', '351', '1', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('125', '46', '351', '2', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('126', '46', '351', '3', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('127', '47', '351', '4', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('128', '47', '351', '5', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('129', '47', '351', '6', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('130', '47', '351', '7', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('131', '48', '351', '8', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('132', '48', '351', '9', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('133', '48', '351', '10', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('134', '48', '351', '11', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('135', '49', '351', '12', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('136', '49', '351', '13', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('137', '49', '351', '14', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('138', '49', '351', '15', '0', '64', null, '染料', '0', null);
INSERT INTO `gachapon_description` VALUES ('139', '50', '397', '0', '0', '5', null, 'スケルトンの頭', '0', null);
INSERT INTO `gachapon_description` VALUES ('140', '50', '289', '0', '0', '64', null, '火薬', '0', null);
INSERT INTO `gachapon_description` VALUES ('141', '50', '339', '0', '0', '64', null, '紙', '0', null);
INSERT INTO `gachapon_description` VALUES ('142', '50', '348', '0', '0', '64', null, 'グローストーンダスト', '0', null);
INSERT INTO `gachapon_description` VALUES ('143', '51', '397', '1', '0', '5', null, 'ウィザーの頭', '0', null);
INSERT INTO `gachapon_description` VALUES ('144', '51', '289', '0', '0', '64', null, '火薬', '0', null);
INSERT INTO `gachapon_description` VALUES ('145', '51', '339', '0', '0', '64', null, '紙', '0', null);
INSERT INTO `gachapon_description` VALUES ('146', '51', '385', '0', '0', '64', null, 'ファイアチャージ', '0', null);
INSERT INTO `gachapon_description` VALUES ('147', '52', '397', '2', '0', '5', null, 'ゾンビの頭', '0', null);
INSERT INTO `gachapon_description` VALUES ('148', '52', '289', '0', '0', '64', null, '火薬', '0', null);
INSERT INTO `gachapon_description` VALUES ('149', '52', '339', '0', '0', '64', null, '紙', '0', null);
INSERT INTO `gachapon_description` VALUES ('150', '52', '288', '0', '0', '64', null, '羽', '0', null);
INSERT INTO `gachapon_description` VALUES ('151', '53', '397', '3', '0', '5', null, 'スティーブの頭', '0', null);
INSERT INTO `gachapon_description` VALUES ('152', '53', '289', '0', '0', '64', null, '火薬', '0', null);
INSERT INTO `gachapon_description` VALUES ('153', '53', '339', '0', '0', '64', null, '紙', '0', null);
INSERT INTO `gachapon_description` VALUES ('154', '53', '371', '0', '0', '64', null, '金塊', '0', null);
INSERT INTO `gachapon_description` VALUES ('155', '54', '397', '4', '0', '5', null, 'クリーパーの頭', '0', null);
INSERT INTO `gachapon_description` VALUES ('156', '54', '289', '0', '0', '64', null, '火薬', '0', null);
INSERT INTO `gachapon_description` VALUES ('157', '54', '339', '0', '0', '64', null, '紙', '0', null);
INSERT INTO `gachapon_description` VALUES ('158', '54', '23', '0', '0', '1', null, 'ディスペンサー', '0', null);
INSERT INTO `gachapon_description` VALUES ('159', '56', '373', '0', '8262', '10', null, '暗視ポーション', '0', null);
INSERT INTO `gachapon_description` VALUES ('160', '57', '373', '0', '8229', '10', null, 'ポーション', '0', null);
INSERT INTO `gachapon_description` VALUES ('161', '58', '47', '0', '0', '48', null, '本棚', '0', null);
INSERT INTO `gachapon_description` VALUES ('162', '58', '116', '0', '0', '1', null, 'エンチャントテーブル', '0', null);
INSERT INTO `gachapon_description` VALUES ('163', '58', '145', '0', '0', '1', null, '鉄床', '0', null);
INSERT INTO `gachapon_description` VALUES ('164', '58', '355', '0', '0', '1', null, 'ベッド', '0', null);
INSERT INTO `gachapon_description` VALUES ('165', '58', '130', '0', '0', '1', null, 'エンダーチェスト', '0', null);
INSERT INTO `gachapon_description` VALUES ('166', '58', '54', '0', '0', '4', null, 'チェスト', '0', null);
INSERT INTO `gachapon_description` VALUES ('167', '58', '58', '0', '0', '1', null, '作業台', '0', null);
INSERT INTO `gachapon_description` VALUES ('168', '58', '61', '0', '0', '2', null, 'かまど', '0', null);
INSERT INTO `gachapon_description` VALUES ('169', '59', '258', '0', '0', '1', null, '鉄の斧', '0', null);
INSERT INTO `gachapon_description` VALUES ('170', '59', '6', '0', '0', '64', null, '木の苗', '0', null);
INSERT INTO `gachapon_description` VALUES ('171', '59', '6', '1', '0', '64', null, '木の苗', '0', null);
INSERT INTO `gachapon_description` VALUES ('172', '59', '6', '2', '0', '64', null, '木の苗', '0', null);
INSERT INTO `gachapon_description` VALUES ('173', '59', '6', '3', '0', '64', null, '木の苗', '0', null);
INSERT INTO `gachapon_description` VALUES ('174', '60', '3', '0', '0', '128', null, '土', '0', null);
INSERT INTO `gachapon_description` VALUES ('175', '60', '292', '0', '0', '1', null, '鉄のクワ', '0', null);
INSERT INTO `gachapon_description` VALUES ('176', '60', '295', '0', '0', '64', null, '小麦の種', '0', null);
INSERT INTO `gachapon_description` VALUES ('177', '60', '361', '0', '0', '64', null, 'カボチャの種', '0', null);
INSERT INTO `gachapon_description` VALUES ('178', '60', '362', '0', '0', '64', null, 'スイカの種', '0', null);
INSERT INTO `gachapon_description` VALUES ('179', '60', '338', '0', '0', '64', null, 'サトウキビ', '0', null);
INSERT INTO `gachapon_description` VALUES ('180', '60', '326', '0', '0', '2', null, '水入りバケツ', '0', null);
INSERT INTO `gachapon_description` VALUES ('181', '61', '278', '0', '0', '1', '32,20,34,20,33,20', 'ミツヤピッケルS改', '1', null);
INSERT INTO `gachapon_description` VALUES ('182', '62', '278', '0', '0', '1', '32,20,34,20,35,20', 'ミツヤピッケルF改', '1', null);
INSERT INTO `gachapon_description` VALUES ('183', '63', '278', '0', '0', '1', '32,20,34,20', 'ミツヤピッケル改', '1', null);
INSERT INTO `gachapon_description` VALUES ('184', '64', '277', '0', '0', '1', '32,20,34,20,33,20', 'ミツヤシャベル改', '1', null);
INSERT INTO `gachapon_description` VALUES ('185', '65', '279', '0', '0', '1', '32,20,34,20,16,20,33,20', 'ミツヤアックス改', '1', null);
INSERT INTO `gachapon_description` VALUES ('186', '66', '268', '0', '0', '1', '19,20,34,20', '木製バット改', '1', null);
INSERT INTO `gachapon_description` VALUES ('187', '67', '267', '0', '0', '1', '19,20,34,20', '金属バット改', '1', null);
INSERT INTO `gachapon_description` VALUES ('188', '68', '285', '0', '31', '1', '32,20,34,20,33,20', '朽ちたミツヤピッケルS改', '1', null);
INSERT INTO `gachapon_description` VALUES ('189', '69', '285', '0', '31', '1', '32,20,34,20,35,20', '朽ちたミツヤピッケルF改', '1', null);
INSERT INTO `gachapon_description` VALUES ('190', '70', '285', '0', '31', '1', '32,20,34,20', '朽ちたミツヤピッケル改', '1', null);
INSERT INTO `gachapon_description` VALUES ('191', '71', '284', '0', '31', '1', '32,20,34,20,33,20', '朽ちたミツヤシャベル改', '1', null);
INSERT INTO `gachapon_description` VALUES ('192', '72', '286', '0', '31', '1', '32,20,34,20,16,20,33,20', '朽ちたミツヤアックス改', '1', null);
INSERT INTO `gachapon_description` VALUES ('193', '73', '38', '1', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('194', '73', '38', '2', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('195', '73', '38', '3', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('196', '73', '38', '4', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('197', '73', '38', '5', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('198', '73', '38', '6', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('199', '73', '38', '7', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('200', '73', '38', '8', '0', '10', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('201', '74', '175', '0', '0', '33', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('202', '74', '175', '1', '0', '33', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('203', '74', '175', '4', '0', '33', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('204', '74', '175', '5', '0', '33', null, 'ハナ', '0', null);
INSERT INTO `gachapon_description` VALUES ('205', '76', '383', '100', '0', '1', null, 'スポーンエッグ（馬）', '0', null);
INSERT INTO `gachapon_description` VALUES ('206', '76', '417', '0', '0', '1', null, '馬鎧　鉄', '0', null);
INSERT INTO `gachapon_description` VALUES ('207', '77', '383', '100', '0', '1', null, 'スポーンエッグ（馬）', '0', null);
INSERT INTO `gachapon_description` VALUES ('208', '77', '418', '0', '0', '1', null, '馬鎧　金', '0', null);
INSERT INTO `gachapon_description` VALUES ('209', '78', '383', '100', '0', '1', null, 'スポーンエッグ（馬）', '0', null);
INSERT INTO `gachapon_description` VALUES ('210', '78', '419', '0', '0', '1', null, '馬鎧　ダイヤ', '0', null);
INSERT INTO `gachapon_description` VALUES ('211', '79', '383', '100', '0', '3', null, 'スポーンエッグ（馬）', '0', null);
INSERT INTO `gachapon_description` VALUES ('212', '79', '417', '0', '0', '1', null, '馬鎧　鉄', '0', null);
INSERT INTO `gachapon_description` VALUES ('213', '79', '418', '0', '0', '1', null, '馬鎧　金', '0', null);
INSERT INTO `gachapon_description` VALUES ('214', '79', '419', '0', '0', '1', null, '馬鎧　ダイヤ', '0', null);
INSERT INTO `gachapon_description` VALUES ('215', '80', '95', '0', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('216', '80', '95', '1', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('217', '80', '95', '2', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('218', '80', '95', '3', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('219', '81', '95', '4', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('220', '81', '95', '5', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('221', '81', '95', '6', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('222', '81', '95', '7', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('223', '82', '95', '8', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('224', '82', '95', '9', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('225', '82', '95', '10', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('226', '82', '95', '11', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('227', '83', '95', '12', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('228', '83', '95', '13', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('229', '83', '95', '14', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('230', '83', '95', '15', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('231', '84', '154', '0', '0', '64', null, 'ホッパー', '0', null);
INSERT INTO `gachapon_description` VALUES ('232', '84', '20', '0', '0', '64', null, 'ガラス', '0', null);
INSERT INTO `gachapon_description` VALUES ('233', '85', '404', '0', '0', '5', null, 'コンパレータ', '0', null);
INSERT INTO `gachapon_description` VALUES ('234', '85', '151', '0', '0', '5', null, '日照センサー', '0', null);
INSERT INTO `gachapon_description` VALUES ('235', '85', '152', '0', '0', '64', null, 'レッドストーンブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('236', '85', '147', '0', '0', '5', null, '感圧スイッチ　ライト', '0', null);
INSERT INTO `gachapon_description` VALUES ('237', '85', '148', '0', '0', '5', null, '感圧スイッチ　ヘビー', '0', null);
INSERT INTO `gachapon_description` VALUES ('238', '86', '14', '0', '0', '256', null, '金鉱石ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('239', '86', '61', '0', '0', '1', null, 'かまど', '0', null);
INSERT INTO `gachapon_description` VALUES ('240', '86', '263', '0', '0', '64', null, '石炭', '0', null);
INSERT INTO `gachapon_description` VALUES ('241', '87', '56', '0', '0', '64', null, 'ダイヤ鉱石ブロック', '0', null);
INSERT INTO `gachapon_description` VALUES ('242', '87', '278', '0', '0', '1', null, 'ダイヤピッケル', '0', null);
INSERT INTO `gachapon_description` VALUES ('243', '88', '383', '100', '0', '20', null, 'スポーンエッグ（馬）', '0', null);
INSERT INTO `gachapon_description` VALUES ('244', '89', '349', '0', '0', '64', null, '魚', '0', null);
INSERT INTO `gachapon_description` VALUES ('245', '89', '349', '1', '0', '64', null, '魚', '0', null);
INSERT INTO `gachapon_description` VALUES ('246', '89', '349', '2', '0', '64', null, '魚', '0', null);
INSERT INTO `gachapon_description` VALUES ('247', '89', '349', '3', '0', '64', null, '魚', '0', null);
INSERT INTO `gachapon_description` VALUES ('248', '90', '354', '4', '0', '64', null, 'ケーキ', '0', null);

-- ----------------------------
-- Table structure for `gachapon_log`
-- ----------------------------
DROP TABLE IF EXISTS `gachapon_log`;
CREATE TABLE `gachapon_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player` text,
  `capsule_id` int(11) DEFAULT NULL,
  `DTM` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gachapon_log
-- ----------------------------

-- ----------------------------
-- Table structure for `gm_command_log`
-- ----------------------------
DROP TABLE IF EXISTS `gm_command_log`;
CREATE TABLE `gm_command_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` text,
  `command` text,
  `world` bigint(11) DEFAULT NULL,
  `x` double DEFAULT NULL,
  `y` double DEFAULT NULL,
  `z` double DEFAULT NULL,
  `yaw` float(11,0) DEFAULT NULL,
  `pitch` float(11,0) DEFAULT NULL,
  `log_dtm` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gm_command_log
-- ----------------------------

-- ----------------------------
-- Table structure for `hometable`
-- ----------------------------
DROP TABLE IF EXISTS `hometable`;
CREATE TABLE `hometable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT 'Player',
  `world` tinyint(4) NOT NULL DEFAULT '0',
  `subid` tinyint(4) NOT NULL DEFAULT '0',
  `x` double NOT NULL DEFAULT '0',
  `y` tinyint(6) NOT NULL DEFAULT '0',
  `z` double NOT NULL DEFAULT '0',
  `yaw` smallint(6) NOT NULL DEFAULT '0',
  `pitch` smallint(6) NOT NULL DEFAULT '0',
  `publicAll` tinyint(1) NOT NULL DEFAULT '0',
  `permissions` varchar(150) NOT NULL DEFAULT '',
  `welcomeMessage` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`,`world`,`subid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hometable
-- ----------------------------

-- ----------------------------
-- Table structure for `ibalances`
-- ----------------------------
DROP TABLE IF EXISTS `ibalances`;
CREATE TABLE `ibalances` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `player` text NOT NULL,
  `balance` bigint(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `balance` (`balance`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ibalances
-- ----------------------------

-- ----------------------------
-- Table structure for `items`
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `ID` bigint(20) NOT NULL COMMENT 'アイテムID',
  `subID` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'サブID',
  `name_eng` text COMMENT 'アイテム名(英語表記)',
  `name_jp` text COMMENT 'アイテム名(日本語表記)',
  PRIMARY KEY (`ID`,`subID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of items
-- ----------------------------
INSERT INTO `items` VALUES ('1', '0', 'Stone', '石');
INSERT INTO `items` VALUES ('2', '0', 'Grass', '草の生えた土');
INSERT INTO `items` VALUES ('3', '0', 'Dirt', '土');
INSERT INTO `items` VALUES ('4', '0', 'Cobblestone', '丸石');
INSERT INTO `items` VALUES ('5', '0', 'Wooden_Plank', '木材');
INSERT INTO `items` VALUES ('6', '0', 'Sapling', '苗');
INSERT INTO `items` VALUES ('7', '0', 'Bedrock', '岩盤');
INSERT INTO `items` VALUES ('8', '0', 'Water', '水源');
INSERT INTO `items` VALUES ('9', '0', 'Stationary_Water', '水流');
INSERT INTO `items` VALUES ('10', '0', 'Lava', '溶岩源');
INSERT INTO `items` VALUES ('11', '0', 'Stationary_Lava', '溶岩流');
INSERT INTO `items` VALUES ('12', '0', 'Sand', '砂');
INSERT INTO `items` VALUES ('13', '0', 'Gravel', '砂利');
INSERT INTO `items` VALUES ('14', '0', 'Gold_Ore', '金鉱石');
INSERT INTO `items` VALUES ('15', '0', 'Iron_Ore', '鉄鉱石');
INSERT INTO `items` VALUES ('16', '0', 'Coal_Ore', '炭鉱石');
INSERT INTO `items` VALUES ('17', '0', 'Wood', '丸太（広葉樹）');
INSERT INTO `items` VALUES ('17', '1', 'Redwood', '丸太（針葉樹）');
INSERT INTO `items` VALUES ('17', '2', 'Birchwood', '丸太（白樺）');
INSERT INTO `items` VALUES ('18', '0', 'Leaves', '葉っぱ（広葉樹）');
INSERT INTO `items` VALUES ('18', '1', 'Redwood_Leaves', '葉っぱ（針葉樹）');
INSERT INTO `items` VALUES ('18', '2', 'Birchwood_Leaves', '葉っぱ（白樺）');
INSERT INTO `items` VALUES ('19', '0', 'Sponge', 'スポンジ');
INSERT INTO `items` VALUES ('20', '0', 'Glass', 'ガラス');
INSERT INTO `items` VALUES ('21', '0', 'Lapis_Lazuli_Ore', 'ラピスラズリ鉱石');
INSERT INTO `items` VALUES ('22', '0', 'Lapis_Lazuli_Block', 'ラピスラズリブロック');
INSERT INTO `items` VALUES ('23', '0', 'Dispenser', 'ディスペンサー');
INSERT INTO `items` VALUES ('24', '0', 'Sandstone', '砂岩');
INSERT INTO `items` VALUES ('25', '0', 'Note_Block', '音ブロック');
INSERT INTO `items` VALUES ('26', '0', 'Bed_Block', 'ベッドブロック');
INSERT INTO `items` VALUES ('35', '0', 'White_Wool', 'ウール（ホワイト）');
INSERT INTO `items` VALUES ('35', '1', 'Orange_Wool', 'ウール（オレンジ）');
INSERT INTO `items` VALUES ('35', '2', 'Magenta_Wool', 'ウール（マゼンタ）');
INSERT INTO `items` VALUES ('35', '3', 'Light_Blue_Wool', 'ウール（ライトブルー）');
INSERT INTO `items` VALUES ('35', '4', 'Yellow_Wool', 'ウール（イエロー）');
INSERT INTO `items` VALUES ('35', '5', 'Light_Green_Wool', 'ウール（ライトグリーン）');
INSERT INTO `items` VALUES ('35', '6', 'Pink_Wool', 'ウール（ピンク）');
INSERT INTO `items` VALUES ('35', '7', 'Gray_Wool', 'ウール（グリーン）');
INSERT INTO `items` VALUES ('35', '8', 'Light_Gray_Wool', 'ウール（ライトグレー）');
INSERT INTO `items` VALUES ('35', '9', 'Cyan_Wool', 'ウール（シアン）');
INSERT INTO `items` VALUES ('35', '10', 'Purple_Wool', 'ウール（パープル）');
INSERT INTO `items` VALUES ('35', '11', 'Blue_Wool', 'ウール（ブルー）');
INSERT INTO `items` VALUES ('35', '12', 'Brown_Wool', 'ウール（ブラウン）');
INSERT INTO `items` VALUES ('35', '13', 'Dark_Green_Wool', 'ウール（ダークグリーン）');
INSERT INTO `items` VALUES ('35', '14', 'Red_Wool', 'ウール（レッド）');
INSERT INTO `items` VALUES ('35', '15', 'Black_Wool', 'ウール（ブラック）');
INSERT INTO `items` VALUES ('37', '0', 'Yellow_Flower', '黄色い花');
INSERT INTO `items` VALUES ('38', '0', 'Red_Rose', '赤いバラ');
INSERT INTO `items` VALUES ('39', '0', 'Brown_Mushroom', '茶色いキノコ');
INSERT INTO `items` VALUES ('40', '0', 'Red_Mushroom', '赤いキノコ');
INSERT INTO `items` VALUES ('41', '0', 'Gold_Block', '金ブロック');
INSERT INTO `items` VALUES ('42', '0', 'Iron_Block', '鉄ブロック');
INSERT INTO `items` VALUES ('43', '0', 'Double_Stone_Slab', 'ダブルステップ（石）');
INSERT INTO `items` VALUES ('43', '1', 'Double_Sandstone_Slab', 'ダブルステップ（砂岩）');
INSERT INTO `items` VALUES ('43', '2', 'Double_Wooden_Slab', 'ダブルステップ（木）');
INSERT INTO `items` VALUES ('43', '3', 'Double_Cobblestone_Slab', 'ダブルステップ（丸石）');
INSERT INTO `items` VALUES ('44', '0', 'Stone_Slab', 'シングルステップ（石）');
INSERT INTO `items` VALUES ('44', '1', 'Sandstone_Slab', 'シングルステップ（砂岩）');
INSERT INTO `items` VALUES ('44', '2', 'Wooden_Slab', 'シングルステップ（木）');
INSERT INTO `items` VALUES ('44', '3', 'Cobblestone_Slab', 'シングルステップ（丸石）');
INSERT INTO `items` VALUES ('45', '0', 'Brick', 'レンガブロック');
INSERT INTO `items` VALUES ('46', '0', 'TNT', 'TNT');
INSERT INTO `items` VALUES ('47', '0', 'Bookshelf', '本棚');
INSERT INTO `items` VALUES ('48', '0', 'Mossy_Cobblestone', '苔石');
INSERT INTO `items` VALUES ('49', '0', 'Obsidian', '黒曜石');
INSERT INTO `items` VALUES ('50', '0', 'Torch', '松明');
INSERT INTO `items` VALUES ('51', '0', 'Fire', '炎');
INSERT INTO `items` VALUES ('52', '0', 'Monster_Spawner', 'モンスタースポナー');
INSERT INTO `items` VALUES ('53', '0', 'Wooden_Stairs', '階段（木）');
INSERT INTO `items` VALUES ('54', '0', 'Chest', 'チェスト');
INSERT INTO `items` VALUES ('55', '0', 'Redstone_Wire', 'レッドストーン（設置）');
INSERT INTO `items` VALUES ('56', '0', 'Diamond_Ore', 'ダイヤモンド鉱石');
INSERT INTO `items` VALUES ('57', '0', 'Diamond_Block', 'ダイヤモンドブロック');
INSERT INTO `items` VALUES ('58', '0', 'Workbench', '作業台');
INSERT INTO `items` VALUES ('59', '0', 'Crops', '麦（設置）');
INSERT INTO `items` VALUES ('60', '0', 'Soil', '耕地');
INSERT INTO `items` VALUES ('61', '0', 'Furnace', 'かまど（非稼動時）');
INSERT INTO `items` VALUES ('62', '0', 'Burning_Furnace', 'かまど（稼動時）');
INSERT INTO `items` VALUES ('63', '0', 'Sign_Post', '立て看板');
INSERT INTO `items` VALUES ('64', '0', 'Wooden_Door', 'ドアブロック（木）');
INSERT INTO `items` VALUES ('65', '0', 'Ladder', 'はしご');
INSERT INTO `items` VALUES ('66', '0', 'Minecart_Tracks', '線路');
INSERT INTO `items` VALUES ('67', '0', 'Cobblestone_Stairs', '階段（丸石）');
INSERT INTO `items` VALUES ('68', '0', 'Wall_Sign', 'かけ看板');
INSERT INTO `items` VALUES ('69', '0', 'Lever', 'レバー');
INSERT INTO `items` VALUES ('70', '0', 'Stone_Pressure_Plate', '感圧スイッチ（石）');
INSERT INTO `items` VALUES ('71', '0', 'Iron_Door', 'ドアブロック（鉄）');
INSERT INTO `items` VALUES ('72', '0', 'Wooden_Pressure_Plate', '感圧スイッチ（木）');
INSERT INTO `items` VALUES ('73', '0', 'Redstone_Ore', 'レッドストーン鉱石（消）');
INSERT INTO `items` VALUES ('74', '0', 'Glowing_Redstone_Ore', 'レッドストーン鉱石（点）');
INSERT INTO `items` VALUES ('75', '0', 'Redstone_Torch_(off)', 'レッドストーントーチ（OFF）');
INSERT INTO `items` VALUES ('76', '0', 'Redstone_Torch_(on)', 'レッドストーントーチ（ON)');
INSERT INTO `items` VALUES ('77', '0', 'Stone_Button', '押しボタン');
INSERT INTO `items` VALUES ('78', '0', 'Snow', '雪プレート');
INSERT INTO `items` VALUES ('79', '0', 'Ice', '氷');
INSERT INTO `items` VALUES ('80', '0', 'Snow_Block', '雪ブロック');
INSERT INTO `items` VALUES ('81', '0', 'Cactus', 'サボテン');
INSERT INTO `items` VALUES ('82', '0', 'Clay', '粘土ブロック');
INSERT INTO `items` VALUES ('83', '0', 'Sugar_Cane', 'サトウキビブロック');
INSERT INTO `items` VALUES ('84', '0', 'Jukebox', 'ジュークボックス');
INSERT INTO `items` VALUES ('85', '0', 'Fence', '柵');
INSERT INTO `items` VALUES ('86', '0', 'Pumpkin', 'かぼちゃ');
INSERT INTO `items` VALUES ('87', '0', 'Netherrack', 'ネザーストーン');
INSERT INTO `items` VALUES ('88', '0', 'Soul_Sand', 'ソウルサンド');
INSERT INTO `items` VALUES ('89', '0', 'Glowstone', 'ライトストーン');
INSERT INTO `items` VALUES ('90', '0', 'Portal', 'ネザーゲート');
INSERT INTO `items` VALUES ('91', '0', 'Jack-O-Lantern', 'ジャック・オ・ランタン（光るかぼちゃ）');
INSERT INTO `items` VALUES ('92', '0', 'Cake_Block', 'ケーキブロック');
INSERT INTO `items` VALUES ('93', '0', 'Redstone_Repeater_Block_(off)', 'リピーターブロック（OFF)');
INSERT INTO `items` VALUES ('94', '0', 'Redstone_Repeater_Block_(on)', 'リピーターブロック（ON)');
INSERT INTO `items` VALUES ('256', '0', 'Iron_Shovel', '鉄のスコップ');
INSERT INTO `items` VALUES ('257', '0', 'Iron_Pickaxe', '鉄のピッケル');
INSERT INTO `items` VALUES ('258', '0', 'Iron_Axe', '鉄の斧');
INSERT INTO `items` VALUES ('259', '0', 'Flint_and_Steel', '火打石');
INSERT INTO `items` VALUES ('260', '0', 'Apple', 'りんご');
INSERT INTO `items` VALUES ('261', '0', 'Bow', '弓');
INSERT INTO `items` VALUES ('262', '0', 'Arrow', '弓矢');
INSERT INTO `items` VALUES ('263', '0', 'Coal', '石炭');
INSERT INTO `items` VALUES ('263', '1', 'Charcoal', '木炭');
INSERT INTO `items` VALUES ('264', '0', 'Diamond', 'ダイヤモンド');
INSERT INTO `items` VALUES ('265', '0', 'Iron_Ingot', '鉄塊');
INSERT INTO `items` VALUES ('266', '0', 'Gold_Ingot', '金塊');
INSERT INTO `items` VALUES ('267', '0', 'Iron_Sword', '鉄の剣');
INSERT INTO `items` VALUES ('268', '0', 'Wooden_Sword', '木の剣');
INSERT INTO `items` VALUES ('269', '0', 'Wooden_Shovel', '木のスコップ');
INSERT INTO `items` VALUES ('270', '0', 'Wooden_Pickaxe', '木のピッケル');
INSERT INTO `items` VALUES ('271', '0', 'Wooden_Axe', '木の斧');
INSERT INTO `items` VALUES ('272', '0', 'Stone_Sword', '石の剣');
INSERT INTO `items` VALUES ('273', '0', 'Stone_Shovel', '石のスコップ');
INSERT INTO `items` VALUES ('274', '0', 'Stone_Pickaxe', '石のピッケル');
INSERT INTO `items` VALUES ('275', '0', 'Stone_Axe', '石の斧');
INSERT INTO `items` VALUES ('276', '0', 'Diamond_Sword', 'ダイヤモンドの剣');
INSERT INTO `items` VALUES ('277', '0', 'Diamond_Shovel', 'ダイヤモンドのスコップ');
INSERT INTO `items` VALUES ('278', '0', 'Diamond_Pickaxe', 'ダイヤモンドのピッケル');
INSERT INTO `items` VALUES ('279', '0', 'Diamond_Axe', 'ダイヤモンドの斧');
INSERT INTO `items` VALUES ('280', '0', 'Stick', '木の棒');
INSERT INTO `items` VALUES ('281', '0', 'Bowl', '木の器');
INSERT INTO `items` VALUES ('282', '0', 'Mushroom_Soup', 'キノコのスープ');
INSERT INTO `items` VALUES ('283', '0', 'Gold_Sword', '金の剣');
INSERT INTO `items` VALUES ('284', '0', 'Gold_Shovel', '金のスコップ');
INSERT INTO `items` VALUES ('285', '0', 'Gold_Pickaxe', '金のピッケル');
INSERT INTO `items` VALUES ('286', '0', 'Gold_Axe', '金の斧');
INSERT INTO `items` VALUES ('287', '0', 'String', 'クモの糸');
INSERT INTO `items` VALUES ('288', '0', 'Feather', '羽');
INSERT INTO `items` VALUES ('289', '0', 'Sulphur', '火薬');
INSERT INTO `items` VALUES ('290', '0', 'Wooden_Hoe', '木のクワ');
INSERT INTO `items` VALUES ('291', '0', 'Stone_Hoe', '石のクワ');
INSERT INTO `items` VALUES ('292', '0', 'Iron_Hoe', '鉄のクワ');
INSERT INTO `items` VALUES ('293', '0', 'Diamond_Hoe', 'ダイヤモンドのクワ');
INSERT INTO `items` VALUES ('294', '0', 'Gold_Hoe', '金のクワ');
INSERT INTO `items` VALUES ('295', '0', 'Seeds', '種');
INSERT INTO `items` VALUES ('296', '0', 'Wheat', '麦');
INSERT INTO `items` VALUES ('297', '0', 'Bread', 'パン');
INSERT INTO `items` VALUES ('298', '0', 'Leather_Helmet', '革の兜');
INSERT INTO `items` VALUES ('299', '0', 'Leather_Chestplate', '革の胸当て');
INSERT INTO `items` VALUES ('300', '0', 'Leather_Leggings', '革のレギンス');
INSERT INTO `items` VALUES ('301', '0', 'Leather_Boots', '革の靴');
INSERT INTO `items` VALUES ('302', '0', 'Chainmail_Helmet', 'チェーンメイルの兜');
INSERT INTO `items` VALUES ('303', '0', 'Chainmail_Chestplate', 'チェーンメイルの胸当て');
INSERT INTO `items` VALUES ('304', '0', 'Chainmail_Leggings', 'チェーンメイルのレギンス');
INSERT INTO `items` VALUES ('305', '0', 'Chainmail_Boots', 'チェーンメイルの靴');
INSERT INTO `items` VALUES ('306', '0', 'Iron_Helmet', '鉄の兜');
INSERT INTO `items` VALUES ('307', '0', 'Iron_Chestplate', '鉄の胸当て');
INSERT INTO `items` VALUES ('308', '0', 'Iron_Leggings', '鉄のレギンス');
INSERT INTO `items` VALUES ('309', '0', 'Iron_Boots', '鉄の靴');
INSERT INTO `items` VALUES ('310', '0', 'Diamond_Helmet', 'ダイヤモンドの兜');
INSERT INTO `items` VALUES ('311', '0', 'Diamond_Chestplate', 'ダイヤモンドの胸当て');
INSERT INTO `items` VALUES ('312', '0', 'Diamond_Leggings', 'ダイヤモンドのレギンス');
INSERT INTO `items` VALUES ('313', '0', 'Diamond_Boots', 'ダイヤモンドの靴');
INSERT INTO `items` VALUES ('314', '0', 'Gold_Helmet', '金の兜');
INSERT INTO `items` VALUES ('315', '0', 'Gold_Chestplate', '金の胸当て');
INSERT INTO `items` VALUES ('316', '0', 'Gold_Leggings', '金のレギンス');
INSERT INTO `items` VALUES ('317', '0', 'Gold_Boots', '金の靴');
INSERT INTO `items` VALUES ('318', '0', 'Flint', 'やじり');
INSERT INTO `items` VALUES ('319', '0', 'Raw_Porkchop', '生肉');
INSERT INTO `items` VALUES ('320', '0', 'Cooked_Porkchop', '焼肉');
INSERT INTO `items` VALUES ('321', '0', 'Painting', '絵画');
INSERT INTO `items` VALUES ('322', '0', 'Golden_Apple', '金のりんご');
INSERT INTO `items` VALUES ('323', '0', 'Sign', '看板');
INSERT INTO `items` VALUES ('324', '0', 'Wooden_Door', 'ドア（木）');
INSERT INTO `items` VALUES ('325', '0', 'Bucket', 'バケツ');
INSERT INTO `items` VALUES ('326', '0', 'Water_Bucket', 'バケツ（水）');
INSERT INTO `items` VALUES ('327', '0', 'Lava_Bucket', 'バケツ（溶岩）');
INSERT INTO `items` VALUES ('328', '0', 'Minecart', 'トロッコ');
INSERT INTO `items` VALUES ('329', '0', 'Saddle', '鞍');
INSERT INTO `items` VALUES ('330', '0', 'Iron_Door', 'ドア（鉄）');
INSERT INTO `items` VALUES ('331', '0', 'Redstone', 'レッドストーン');
INSERT INTO `items` VALUES ('332', '0', 'Snowball', '雪玉');
INSERT INTO `items` VALUES ('333', '0', 'Boat', '船');
INSERT INTO `items` VALUES ('334', '0', 'Leather', '革');
INSERT INTO `items` VALUES ('335', '0', 'Milk_Bucket', 'バケツ（ミルク）');
INSERT INTO `items` VALUES ('336', '0', 'Clay_Brick', 'レンガ');
INSERT INTO `items` VALUES ('337', '0', 'Clay_Balls', '粘土');
INSERT INTO `items` VALUES ('338', '0', 'Sugarcane', 'サトウキビ');
INSERT INTO `items` VALUES ('339', '0', 'Paper', '紙');
INSERT INTO `items` VALUES ('340', '0', 'Book', '本');
INSERT INTO `items` VALUES ('341', '0', 'Slimeball', 'スライムボール');
INSERT INTO `items` VALUES ('342', '0', 'Storage_Minecart', '倉庫付カート');
INSERT INTO `items` VALUES ('343', '0', 'Powered_Minecart', '動力付カート');
INSERT INTO `items` VALUES ('344', '0', 'Egg', '卵');
INSERT INTO `items` VALUES ('345', '0', 'Compass', 'コンパス');
INSERT INTO `items` VALUES ('346', '0', 'Fishing_Rod', '釣竿');
INSERT INTO `items` VALUES ('347', '0', 'Clock', '時計');
INSERT INTO `items` VALUES ('348', '0', 'Glowstone_Dust', 'ライトストーンの粉');
INSERT INTO `items` VALUES ('349', '0', 'Raw_Fish', '生魚');
INSERT INTO `items` VALUES ('350', '0', 'Cooked_Fish', '調理した魚');
INSERT INTO `items` VALUES ('351', '0', 'Ink_Sack', '墨袋');
INSERT INTO `items` VALUES ('351', '1', 'Rose_Red', '赤いバラ');
INSERT INTO `items` VALUES ('351', '2', 'Cactus_Green', '緑のサボテン');
INSERT INTO `items` VALUES ('351', '3', 'Coco_Beans', 'カカオブラウン');
INSERT INTO `items` VALUES ('351', '4', 'Lapis_Lazuli', 'ラピスラズリ');
INSERT INTO `items` VALUES ('351', '5', 'Purple_Dye', '紫色の染料');
INSERT INTO `items` VALUES ('351', '6', 'Cyan_Dye', 'シアン色の染料');
INSERT INTO `items` VALUES ('351', '7', 'Light_Gray_Dye', 'ライトグレー色の染料');
INSERT INTO `items` VALUES ('351', '8', 'Gray_Dye', 'グレー色の染料');
INSERT INTO `items` VALUES ('351', '9', 'Pink_Dye', 'ピンク色の染料');
INSERT INTO `items` VALUES ('351', '10', 'Lime_Dye', 'ライム色の染料');
INSERT INTO `items` VALUES ('351', '11', 'Dandelion_Yellow', '黄色いタンポポ');
INSERT INTO `items` VALUES ('351', '12', 'Light_Blue_Dye', 'ライトブルー色の染料');
INSERT INTO `items` VALUES ('351', '13', 'Magenta_Dye', 'マゼンタ色の染料');
INSERT INTO `items` VALUES ('351', '14', 'Orange_Dye', 'オレンジ色の染料');
INSERT INTO `items` VALUES ('351', '15', 'Bone_Meal', '骨粉');
INSERT INTO `items` VALUES ('352', '0', 'Bone', '骨');
INSERT INTO `items` VALUES ('353', '0', 'Sugar', '砂糖');
INSERT INTO `items` VALUES ('354', '0', 'Cake', 'ケーキ');
INSERT INTO `items` VALUES ('355', '0', 'Bed', 'ベッド');
INSERT INTO `items` VALUES ('356', '0', 'Redstone_Repeater', 'リピーター');
INSERT INTO `items` VALUES ('2256', '0', 'Gold_Music_Disc', '金色のディスク');
INSERT INTO `items` VALUES ('2257', '0', 'Green_Music_Disc', '緑色のディスク');

-- ----------------------------
-- Table structure for `jp_message`
-- ----------------------------
DROP TABLE IF EXISTS `jp_message`;
CREATE TABLE `jp_message` (
  `type` varchar(20) NOT NULL,
  `id` varchar(30) NOT NULL,
  `message` varchar(255) NOT NULL,
  `aliases` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`type`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jp_message
-- ----------------------------
INSERT INTO `jp_message` VALUES ('Admin', '0', 'ちゃんたけ', '[\"チャンタケ\"]');
INSERT INTO `jp_message` VALUES ('Admin', '1', 'ふみっち', '[\"フミッチ\"]');
INSERT INTO `jp_message` VALUES ('Admin', '2', 'えずら', '[\"エズラ\",\"ドラエモン\"]');
INSERT INTO `jp_message` VALUES ('entity', '120', '村人', '[\"ムラビト\",\"チャンタケ\",\"フミッチ\",\"エズラ\",\"レータン\",\"マッテン\"]');
INSERT INTO `jp_message` VALUES ('entity', '200', 'エンダークリスタル', null);
INSERT INTO `jp_message` VALUES ('entity', '50', 'クリーパー', '[\"クリーパー\",\"タクミ\"]');
INSERT INTO `jp_message` VALUES ('entity', '51', 'スケルトン', '[\"スケサン\",\"スケルトン\"]');
INSERT INTO `jp_message` VALUES ('entity', '52', 'クモ', null);
INSERT INTO `jp_message` VALUES ('entity', '53', 'ジャイアント', null);
INSERT INTO `jp_message` VALUES ('entity', '54', 'ゾンビ', null);
INSERT INTO `jp_message` VALUES ('entity', '55', 'スライム', null);
INSERT INTO `jp_message` VALUES ('entity', '56', 'ガスト', null);
INSERT INTO `jp_message` VALUES ('entity', '57', 'ゾンビピッグマン', '[\"ゾンビピックマン\",\"ゾンブタ\"]');
INSERT INTO `jp_message` VALUES ('entity', '58', 'エンダーマン', null);
INSERT INTO `jp_message` VALUES ('entity', '59', '洞窟グモ', '[\"ドウクツグモ\"]');
INSERT INTO `jp_message` VALUES ('entity', '60', 'シルバーフィッシュ', null);
INSERT INTO `jp_message` VALUES ('entity', '61', 'ブレイズ', null);
INSERT INTO `jp_message` VALUES ('entity', '62', 'マグマキューブ', null);
INSERT INTO `jp_message` VALUES ('entity', '63', 'エンダードラゴン', null);
INSERT INTO `jp_message` VALUES ('entity', '64', 'ウィザー', null);
INSERT INTO `jp_message` VALUES ('entity', '65', 'コウモリ', null);
INSERT INTO `jp_message` VALUES ('entity', '66', 'ウィッチ', '[\"ウィッチ\",\"マジョ\"]');
INSERT INTO `jp_message` VALUES ('entity', '90', 'ブタ', '[\"ブタ\"]');
INSERT INTO `jp_message` VALUES ('entity', '91', 'ヒツジ', '[\"ヒツジ\",\"インク\"]');
INSERT INTO `jp_message` VALUES ('entity', '92', 'ウシ', null);
INSERT INTO `jp_message` VALUES ('entity', '93', 'ニワトリ', null);
INSERT INTO `jp_message` VALUES ('entity', '94', 'イカ', null);
INSERT INTO `jp_message` VALUES ('entity', '95', 'オオカミ', '[\"オオカミ\",\"イヌ\"]');
INSERT INTO `jp_message` VALUES ('entity', '96', 'ムーシュールーム', '[\"キノコウシ\",\"マッシュルームウシ\"]');
INSERT INTO `jp_message` VALUES ('entity', '97', 'スノウゴーレム', '[\"スノウゴーレム\",\"スノーマン\",\"ユキダルマ\"]');
INSERT INTO `jp_message` VALUES ('entity', '98', 'ヤマネコ', '[\"ヤマネコ\",\"ネコ\",\"イリオモテヤマネコ\",\"ヤママヤー\"]');
INSERT INTO `jp_message` VALUES ('entity', '99', 'アイアンゴーレム', '[\"アイアンゴーレム\",\"ゴーレム\"]');
INSERT INTO `jp_message` VALUES ('item', '1', '焼き石', '[\"ヤキイシ\"]');
INSERT INTO `jp_message` VALUES ('item', '10', '溶岩', '[\"ヨウガン\"]');
INSERT INTO `jp_message` VALUES ('item', '100', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:1', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:10', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:11', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:12', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:13', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:14', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:15', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:2', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:3', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:4', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:5', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:6', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:7', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:8', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '100:9', 'キノコブロック(赤)', '[\"キノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '101', '鉄格子', '[\"テツゴウシ\"]');
INSERT INTO `jp_message` VALUES ('item', '102', '板ガラス', '[\"イタガラス\"]');
INSERT INTO `jp_message` VALUES ('item', '103', 'スイカ', null);
INSERT INTO `jp_message` VALUES ('item', '104', 'カボチャの茎 ', '[\"カボチャノナエ\"]');
INSERT INTO `jp_message` VALUES ('item', '105', 'スイカの茎 ', '[\"スイカノナエ\"]');
INSERT INTO `jp_message` VALUES ('item', '106', 'つる', '[\"ツル\"]');
INSERT INTO `jp_message` VALUES ('item', '107', 'フェンスゲート', '[\"ゲートフェンス\",\"フェンスゲート\"]');
INSERT INTO `jp_message` VALUES ('item', '108', '階段(レンガ)', '[\"カイダンレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '109', '階段(石レンガ)', '[\"カイダンイシレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '11', '静止した溶岩', '[\"セイシシタヨウガン\"]');
INSERT INTO `jp_message` VALUES ('item', '110', '菌糸ブロック', '[\"キンシブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '111', 'スイレンの葉', '[\"スイレンノハ\"]');
INSERT INTO `jp_message` VALUES ('item', '112', 'ネザーレンガ', null);
INSERT INTO `jp_message` VALUES ('item', '113', 'ネザーレンガフェンス', null);
INSERT INTO `jp_message` VALUES ('item', '114', '階段(ネザーレンガ)', '[”カイダンネザーレンガ”]');
INSERT INTO `jp_message` VALUES ('item', '115', 'ネザーウォート', null);
INSERT INTO `jp_message` VALUES ('item', '116', 'エンチャントテーブル', null);
INSERT INTO `jp_message` VALUES ('item', '117', '醸造台', '[\"ジョウゾウダイ\"]');
INSERT INTO `jp_message` VALUES ('item', '118', '大釜', '[\"オオガマ\"]');
INSERT INTO `jp_message` VALUES ('item', '119', 'エンドポータル', null);
INSERT INTO `jp_message` VALUES ('item', '12', '砂', '[\"スナ\"]');
INSERT INTO `jp_message` VALUES ('item', '120', 'エンドポータルフレーム', null);
INSERT INTO `jp_message` VALUES ('item', '121', 'エンドストーン', null);
INSERT INTO `jp_message` VALUES ('item', '122', 'ドラゴンの卵', '[\"タマゴ\",\"ドラゴンノタマゴ\"]');
INSERT INTO `jp_message` VALUES ('item', '123', 'レッドストーンランプ', null);
INSERT INTO `jp_message` VALUES ('item', '124', 'レッドストーンランプ(作動中)', '[\"サドウチュウレッドストーンランプ\"]');
INSERT INTO `jp_message` VALUES ('item', '125', 'ハーフブロック(樫)×２', '[\"カシダブルハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '125:1', 'ハーフブロック(杉)×２', '[\"スギダブルハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '125:2', 'ハーフブロック(白樺)×２', '[\"シラカバダブルハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '125:3', 'ハーフブロック(ジャングル)', '[\"ジャングルダブルハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '126', 'ハーフブロック(樫)', '[\"カシハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '126：1', 'ハーフブロック(杉)', '[\"スギハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '126：2', 'ハーフブロック(白樺)', '[\"シラカバハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '126：3', 'ハーフブロック(ジャングル)', '[\"ジャングルハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '127', 'カカオの実', '[\"カカオノミ\"]');
INSERT INTO `jp_message` VALUES ('item', '128', '階段(砂岩)', '[\"カイダンサガン\"]');
INSERT INTO `jp_message` VALUES ('item', '129', 'エメラルド鉱石', '[\"エメラルドコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '13', '砂利', '[\"ジャリ\"]');
INSERT INTO `jp_message` VALUES ('item', '130', 'エンダーチェスト', null);
INSERT INTO `jp_message` VALUES ('item', '131', 'トラップワイヤーフック', '[\"フック\"]');
INSERT INTO `jp_message` VALUES ('item', '132', 'トラップワイヤー', null);
INSERT INTO `jp_message` VALUES ('item', '133', 'エメラルドブロック', null);
INSERT INTO `jp_message` VALUES ('item', '134', '階段(杉)', '[\"スギカイダン\"]');
INSERT INTO `jp_message` VALUES ('item', '135', '階段(白樺)', '[\"シラカバカイダン\"]');
INSERT INTO `jp_message` VALUES ('item', '136', '階段(ジャングル)', '[\"ジャングルカイダン\"]');
INSERT INTO `jp_message` VALUES ('item', '137', 'コマンドブロック', null);
INSERT INTO `jp_message` VALUES ('item', '138', 'ビーコン', '[\"ベーコン\"]');
INSERT INTO `jp_message` VALUES ('item', '139', '丸石フェンス', '[\"マルイシフェンス\"]');
INSERT INTO `jp_message` VALUES ('item', '139：1', '苔石フェンス', '[\"コケイシフェンス\"]');
INSERT INTO `jp_message` VALUES ('item', '14', '金鉱石', '[\"キンコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '140', '植木鉢', '[\"ウエキバチ\"]');
INSERT INTO `jp_message` VALUES ('item', '141', 'ニンジン', null);
INSERT INTO `jp_message` VALUES ('item', '142', 'ジャガイモ', null);
INSERT INTO `jp_message` VALUES ('item', '143', 'ボタン(木)', '[\"キボタン\"]');
INSERT INTO `jp_message` VALUES ('item', '144', '頭蓋骨(スケルトン)', '[\"ホネアタマ\"]');
INSERT INTO `jp_message` VALUES ('item', '144:1', '頭蓋骨(ウィザー)', '[\"ウィザーアタマ\"]');
INSERT INTO `jp_message` VALUES ('item', '144:2', '頭蓋骨(スティーブ)', '[\"スティーブアタマ\"]');
INSERT INTO `jp_message` VALUES ('item', '144:3', '頭蓋骨(クリーパー)', '[\"クリーパーアタマ\"]');
INSERT INTO `jp_message` VALUES ('item', '145', '金床', '[\"カナドコ\",\"キンユカ\"]');
INSERT INTO `jp_message` VALUES ('item', '146', 'トラップチェスト', null);
INSERT INTO `jp_message` VALUES ('item', '147', 'Weighted Pressure Plate(軽)', null);
INSERT INTO `jp_message` VALUES ('item', '148', 'Weighted Pressure Plate(重)', null);
INSERT INTO `jp_message` VALUES ('item', '149', 'Redstone Comparator (未作動', null);
INSERT INTO `jp_message` VALUES ('item', '15', '鉄鉱石', '[\"テッコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '150', 'Redstone Comparator (作動中)', null);
INSERT INTO `jp_message` VALUES ('item', '151', 'Daylight Sensor', null);
INSERT INTO `jp_message` VALUES ('item', '152', 'レッドストーンブロック', null);
INSERT INTO `jp_message` VALUES ('item', '153', 'Nether Quartz Ore', null);
INSERT INTO `jp_message` VALUES ('item', '154', 'ホッパー', null);
INSERT INTO `jp_message` VALUES ('item', '155', 'Block of Quartz', null);
INSERT INTO `jp_message` VALUES ('item', '156', 'Quartz Stairs', null);
INSERT INTO `jp_message` VALUES ('item', '157', 'Activator Rail', null);
INSERT INTO `jp_message` VALUES ('item', '158', 'ドロッパー', null);
INSERT INTO `jp_message` VALUES ('item', '16', '石炭鉱石', '[\"セキタンコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '17', '原木(樫)', '[\"カシゲンボク\"]');
INSERT INTO `jp_message` VALUES ('item', '17：1', '原木(杉)', '[\"スギゲンボク\"]');
INSERT INTO `jp_message` VALUES ('item', '17：2', '原木(白樺)', '[\"シラカバゲンボク\"]');
INSERT INTO `jp_message` VALUES ('item', '17：3', '原木(ｼﾞｬﾝｸﾞﾙ)', '[\"ジャングルゲンボク\"]');
INSERT INTO `jp_message` VALUES ('item', '18', '葉(樫)', '[\"カシノハ\"]');
INSERT INTO `jp_message` VALUES ('item', '18：1', '葉(杉)', '[\"スギノハ\"]');
INSERT INTO `jp_message` VALUES ('item', '18：2', '葉(白樺)', '[\"シラカバノハ\"]');
INSERT INTO `jp_message` VALUES ('item', '18：3', '葉(ｼﾞｬﾝｸﾞﾙ)', '[\"ジャングルノハ\"]');
INSERT INTO `jp_message` VALUES ('item', '19', 'スポンジ', null);
INSERT INTO `jp_message` VALUES ('item', '2', '草ブロック', '[\"クサブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '20', 'ガラス', null);
INSERT INTO `jp_message` VALUES ('item', '21', 'ラピスラズリ鉱石', '[\"ラピスラズリコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '22', 'ラピスラズリブロック', null);
INSERT INTO `jp_message` VALUES ('item', '23', 'ディスペンサー', null);
INSERT INTO `jp_message` VALUES ('item', '24', '砂岩', '[\"サガン\"]');
INSERT INTO `jp_message` VALUES ('item', '24：1', '模様入り砂岩', '[\"モヨウイリサガン\"]');
INSERT INTO `jp_message` VALUES ('item', '24：2', '滑らかな砂岩', '[\"ナメラカナサガン\"]');
INSERT INTO `jp_message` VALUES ('item', '25', '音符ブロック', '[\"オンプブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '256', '鉄のシャベル', '[\"テツノシャベル\",\"テツノスコップ\",\"テツスコップ\",\"テツシャベル\"]');
INSERT INTO `jp_message` VALUES ('item', '257', '鉄のツルハシ', '[\"テツノツルハシ\",\"テツノピッケル\",\"テツツルハシ\",\"テツピッケル\"]');
INSERT INTO `jp_message` VALUES ('item', '258', '鉄の斧', '[\"テツノオノ\",\"テツオノ\"]');
INSERT INTO `jp_message` VALUES ('item', '259', '火打石と打ち金', '[”ヒウチイシ”]');
INSERT INTO `jp_message` VALUES ('item', '26', 'ベッド', '[\"ベッド\"]');
INSERT INTO `jp_message` VALUES ('item', '260', 'りんご', '[\"リンゴ\"]');
INSERT INTO `jp_message` VALUES ('item', '261', '弓', '[\"ユミ\"]');
INSERT INTO `jp_message` VALUES ('item', '262', '矢', '[\"ヤ\",\"アロー\"]');
INSERT INTO `jp_message` VALUES ('item', '263', '石炭', '[\"セキタン\"]');
INSERT INTO `jp_message` VALUES ('item', '263:1', '木炭', '[\"モクタン\"]');
INSERT INTO `jp_message` VALUES ('item', '264', 'ダイヤモンド', null);
INSERT INTO `jp_message` VALUES ('item', '265', '鉄インゴット', '[\"テツインゴット\"]');
INSERT INTO `jp_message` VALUES ('item', '266', '金インゴット', '[\"キニンゴット\"]');
INSERT INTO `jp_message` VALUES ('item', '267', '鉄の剣', '[\"テツノケン\"]');
INSERT INTO `jp_message` VALUES ('item', '268', '木の剣', '[\"キノケン\"]');
INSERT INTO `jp_message` VALUES ('item', '269', '木のシャベル', '[\"キノスコップ\",\"キノシャベル\"]');
INSERT INTO `jp_message` VALUES ('item', '27', 'パワードレール', null);
INSERT INTO `jp_message` VALUES ('item', '270', '木のツルハシ', '[\"キノツルハシ\"]');
INSERT INTO `jp_message` VALUES ('item', '271', '木の斧', '[\"キノオノ\"]');
INSERT INTO `jp_message` VALUES ('item', '272', '石の剣', '[\"イシノケン\"]');
INSERT INTO `jp_message` VALUES ('item', '273', '石のシャベル', '[\"イシノシャベル\",\"イシシャベル\"]');
INSERT INTO `jp_message` VALUES ('item', '274', '石のツルハシ', '[\"イシノツルハシ\",\"イシツルハシ\"]');
INSERT INTO `jp_message` VALUES ('item', '275', '石の斧', '[”イシノオノ”]');
INSERT INTO `jp_message` VALUES ('item', '276', 'ダイヤの剣', '[\"ダイヤノケン\",\"ダイヤソード\"]');
INSERT INTO `jp_message` VALUES ('item', '277', 'ダイヤのシャベル', '[\"ダイヤノシャベル\",\"ダイヤシャベル\"]');
INSERT INTO `jp_message` VALUES ('item', '278', 'ダイヤのツルハシ', '[\"ダイヤノツルハシ\",\"ダイヤツルハシ\"]');
INSERT INTO `jp_message` VALUES ('item', '279', 'ダイヤの斧', '[\"ダイヤノオノ\",\"ダイヤオノ\"]');
INSERT INTO `jp_message` VALUES ('item', '28', 'ディテクターレール', null);
INSERT INTO `jp_message` VALUES ('item', '280', '棒', '[\"ボウ\"]');
INSERT INTO `jp_message` VALUES ('item', '281', 'ボウル', null);
INSERT INTO `jp_message` VALUES ('item', '289', '火薬', '[\"カヤク\"]');
INSERT INTO `jp_message` VALUES ('item', '29', '粘着ピストン', '[\"ネンチャクピストン\"]');
INSERT INTO `jp_message` VALUES ('item', '3', '土ブロック', '[\"ツチブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '30', 'クモの巣', '[\"クモノス\"]');
INSERT INTO `jp_message` VALUES ('item', '31：1', '草ブロック', '[\"クサブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '31：2', 'シダ', null);
INSERT INTO `jp_message` VALUES ('item', '32', '枯れ木', '[\"カレキ\"]');
INSERT INTO `jp_message` VALUES ('item', '33', 'ピストン', null);
INSERT INTO `jp_message` VALUES ('item', '331', 'レッドストーンパウダー', null);
INSERT INTO `jp_message` VALUES ('item', '336', 'レンガ', null);
INSERT INTO `jp_message` VALUES ('item', '337', 'ねんど', null);
INSERT INTO `jp_message` VALUES ('item', '34', 'ピストンヘッド', null);
INSERT INTO `jp_message` VALUES ('item', '341', 'スライムボール', null);
INSERT INTO `jp_message` VALUES ('item', '35', '羊毛(白)', '[\"インクノケ\",\"シロヨウモウ\"]');
INSERT INTO `jp_message` VALUES ('item', '351：3', 'カカオ豆', '[\"カカオマメ\"]');
INSERT INTO `jp_message` VALUES ('item', '35：1', '羊毛(橙)', '[\"オレンジヨウモウ\",\"オレンジウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：10', '羊毛(紫)', '[\"ムラサキヨウモウ\",\"ムラサキウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：11', '羊毛(青)', '[\"アオヨウモウ\",\"アオウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：12', '羊毛(茶)', '[\"チャイロヨウモウ\",”チャウール”]');
INSERT INTO `jp_message` VALUES ('item', '35：13', '羊毛(緑)', '[\"ミドリヨウモウ\",\"ミドリウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：14', '羊毛(赤)', '[\"アカヨウモウ\",\"アカウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：15', '羊毛(黒)', '[\"クロヨウモウ\",\"クロウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：2', '羊毛(赤紫)', '[\"アカムラサキウール\",”アカムラサキヨウモウ”]');
INSERT INTO `jp_message` VALUES ('item', '35：3', '羊毛(空色)', '[\"ソライロヨウモウ\",\"ソライロウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：4', '羊毛(黄)', '[\"キイロヨウモウ\",\"キイロウール\",\"キウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：5', '羊毛(黄緑)', '[\"キミドリヨウモウ\",\"キミドリウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：6', '羊毛(ピンク)', '[\"ピンクヨウモウ\",\"ピンクウール\"]');
INSERT INTO `jp_message` VALUES ('item', '35：7', '羊毛(灰)', '[\"ハイイロウール\",\"ハイイロヨウモウ\"]');
INSERT INTO `jp_message` VALUES ('item', '35：8', '羊毛(薄灰)', '[\"ウスハイイロウール\",\"ウスハイイロヨウモウ\"]');
INSERT INTO `jp_message` VALUES ('item', '35：9', '羊毛(水色)', '[\"ミズイロウール\",\"ミズイロヨウモウ\"]');
INSERT INTO `jp_message` VALUES ('item', '36', 'ピストンで動かされたブロック', '[\"ピストンデウゴカサレタブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '37', '花', '[\"ハナ\"]');
INSERT INTO `jp_message` VALUES ('item', '38', 'バラ', null);
INSERT INTO `jp_message` VALUES ('item', '388', 'エメラルド', null);
INSERT INTO `jp_message` VALUES ('item', '39', 'キノコ(茶)', '[\"チャイロキノコ\"]');
INSERT INTO `jp_message` VALUES ('item', '399', 'ネザースター', null);
INSERT INTO `jp_message` VALUES ('item', '4', '丸石', '[\"マルイシ\"]');
INSERT INTO `jp_message` VALUES ('item', '40', 'キノコ(赤)', '[\"アカキノコ\"]');
INSERT INTO `jp_message` VALUES ('item', '41', '金ブロック', '[\"キンブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '42', '鉄ブロック', '[\"テツブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '43', 'ハーフブロック(石)x2', '[\"ダブルイシハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '44', 'ハーフブロック(石)', '[\"イシハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '44：1', 'ハーフブロック(砂岩)', '[\"サガンハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '44：3', 'ハーフブロック(丸石)', '[\"マルイシハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '44：4', 'ハーフブロック(レンガ)', '[\"レンガハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '44：5', 'ハーフブロック(石レンガ)', '[\"イシレンガハーフブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '45', 'レンガブロック', '[\"レンガブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '46', 'TNT', '[\"ティエヌティ\",\"エズラセンヨウジバクチートブキ\"]');
INSERT INTO `jp_message` VALUES ('item', '47', '本棚', '[\"ホンダナ\"]');
INSERT INTO `jp_message` VALUES ('item', '48', '苔石', '[\"コケイシ\"]');
INSERT INTO `jp_message` VALUES ('item', '49', '黒曜石', '[\"コクヨウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '5', '木材(樫)', '[\"カシモクザイ\"]');
INSERT INTO `jp_message` VALUES ('item', '50', 'タイマツ', null);
INSERT INTO `jp_message` VALUES ('item', '51', '炎', '[\"ホノオ\",\"ヒ\"]');
INSERT INTO `jp_message` VALUES ('item', '52', 'スポーンブロック', '[\"スポブロ\"]');
INSERT INTO `jp_message` VALUES ('item', '53', '階段(樫)', '[\"カシカンダン\"]');
INSERT INTO `jp_message` VALUES ('item', '54', 'チェスト', null);
INSERT INTO `jp_message` VALUES ('item', '56', 'ダイヤモンド鉱石', '[\"ダイヤモンドコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '57', 'ダイヤモンドブロック', null);
INSERT INTO `jp_message` VALUES ('item', '58', 'ワークベンチ', null);
INSERT INTO `jp_message` VALUES ('item', '59', '種(小麦)', null);
INSERT INTO `jp_message` VALUES ('item', '5：1', '木材(杉)', '[\"スギモクザイ\"]');
INSERT INTO `jp_message` VALUES ('item', '5：2', '木材(白樺)', '[\"シラカバモクザイ\"]');
INSERT INTO `jp_message` VALUES ('item', '5：3', '木材(ジャングル)', '[\"ジャングルモクザイ\"]');
INSERT INTO `jp_message` VALUES ('item', '6', '苗木(樫)', '[\"カシナエギ\"]');
INSERT INTO `jp_message` VALUES ('item', '60', '耕地', '[\"コウチ\"]');
INSERT INTO `jp_message` VALUES ('item', '61', 'カマド', null);
INSERT INTO `jp_message` VALUES ('item', '62', '燃えてるカマド', '[\"モエテルカマド\"]');
INSERT INTO `jp_message` VALUES ('item', '63', '立て看板', '[\"タテカンバン\"]');
INSERT INTO `jp_message` VALUES ('item', '64', '木のドア', '[\"キノドア\"]');
INSERT INTO `jp_message` VALUES ('item', '65', 'はしご', null);
INSERT INTO `jp_message` VALUES ('item', '66', 'レール', null);
INSERT INTO `jp_message` VALUES ('item', '67', '階段(石)', '[\"イシカイダン\"]');
INSERT INTO `jp_message` VALUES ('item', '68', '壁看板', '[\"カベカンバン\"]');
INSERT INTO `jp_message` VALUES ('item', '69', 'レバー', null);
INSERT INTO `jp_message` VALUES ('item', '6：1', '苗木(杉)', '[\"スギナエギ\"]');
INSERT INTO `jp_message` VALUES ('item', '6：2', '苗木(白樺)', '[\"シラカバナエギ\"]');
INSERT INTO `jp_message` VALUES ('item', '6：3', '苗木(ジャングル)', '[\"ジャングルナエギ\"]');
INSERT INTO `jp_message` VALUES ('item', '7', '岩盤', '[\"ガンバン\"]');
INSERT INTO `jp_message` VALUES ('item', '70', '感圧式スイッチ(石)', '[\"イアツシキイシスイッチ\"]');
INSERT INTO `jp_message` VALUES ('item', '72', '感圧式スイッチ(木)', '[\"イアツシキキスイッチ\"]');
INSERT INTO `jp_message` VALUES ('item', '73', 'レッドストーン鉱石', '[\"レッドストーンコウセキ\"]');
INSERT INTO `jp_message` VALUES ('item', '76', 'レッドストーン', '[\"レッドストーン\"]');
INSERT INTO `jp_message` VALUES ('item', '77', 'ボタン(石)', '[\"イシボタン\"]');
INSERT INTO `jp_message` VALUES ('item', '78', '雪(表面)', '[\"ユキ\"]');
INSERT INTO `jp_message` VALUES ('item', '79', '氷', '[\"コオリ\"]');
INSERT INTO `jp_message` VALUES ('item', '8', '水', '[\"ミズ\"]');
INSERT INTO `jp_message` VALUES ('item', '80', '雪ブロック', '[\"ユキブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '81', 'サボテン', null);
INSERT INTO `jp_message` VALUES ('item', '82', '粘土ブロック', '[\"ネンドブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '83', 'サトウキビ', null);
INSERT INTO `jp_message` VALUES ('item', '84', 'ジュークボックス', null);
INSERT INTO `jp_message` VALUES ('item', '85', 'フェンス', null);
INSERT INTO `jp_message` VALUES ('item', '86', 'カボチャ', null);
INSERT INTO `jp_message` VALUES ('item', '87', 'ネザーラック', null);
INSERT INTO `jp_message` VALUES ('item', '88', 'ソウルサンド', null);
INSERT INTO `jp_message` VALUES ('item', '89', 'グロウストーン', null);
INSERT INTO `jp_message` VALUES ('item', '9', '静止した水', '[\"セイシシタミズ\"]');
INSERT INTO `jp_message` VALUES ('item', '90', 'ポータル', null);
INSERT INTO `jp_message` VALUES ('item', '91', 'ジャック・オ・ランタン', null);
INSERT INTO `jp_message` VALUES ('item', '92', 'ケーキ', null);
INSERT INTO `jp_message` VALUES ('item', '93', 'レッドストーンリピーター(未作動)', '[\"レッドストーンリピーター\"]');
INSERT INTO `jp_message` VALUES ('item', '94', 'レッドストーンリピーター(作動中)', '[\"カドウレッドストーンリピーター\"]');
INSERT INTO `jp_message` VALUES ('item', '95', '鍵の掛かったチェスト', '[\"カギチェスト\"]');
INSERT INTO `jp_message` VALUES ('item', '96', 'トラップドア', null);
INSERT INTO `jp_message` VALUES ('item', '97', 'シルバーフィッシュ入りのブロック', '[\"シルバーフィッシュブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '98', '石レンガ', '[\"イシレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '98：1', '苔石レンガ', '[\"コケイシレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '98：2', 'ひびの入った石レンガ', '[\"ヒビノハイッタイシレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '98：3', '模様入り石レンガ', '[\"モヨウイシレンガ\"]');
INSERT INTO `jp_message` VALUES ('item', '99:0', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:1', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:10', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:11', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:12', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:13', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:14', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:15', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:2', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:3', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:4', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:5', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:6', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:7', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:8', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');
INSERT INTO `jp_message` VALUES ('item', '99:9', 'キノコブロック(茶)', '[\"チャイロキノコブロック\"]');

-- ----------------------------
-- Table structure for `log_data`
-- ----------------------------
DROP TABLE IF EXISTS `log_data`;
CREATE TABLE `log_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `data` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_data
-- ----------------------------

-- ----------------------------
-- Table structure for `log_type`
-- ----------------------------
DROP TABLE IF EXISTS `log_type`;
CREATE TABLE `log_type` (
  `id` int(11) NOT NULL,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_type
-- ----------------------------
INSERT INTO `log_type` VALUES ('0', 'Other');
INSERT INTO `log_type` VALUES ('1', 'Command');
INSERT INTO `log_type` VALUES ('2', 'Shop');

-- ----------------------------
-- Table structure for `lottery_log`
-- ----------------------------
DROP TABLE IF EXISTS `lottery_log`;
CREATE TABLE `lottery_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `player` text NOT NULL,
  `leaves` int(11) NOT NULL,
  `buy_price` int(11) NOT NULL,
  `win_price` int(11) NOT NULL,
  `win1` int(11) NOT NULL,
  `win2` int(11) NOT NULL,
  `win3` int(11) NOT NULL,
  `win4` int(11) NOT NULL,
  `win5` int(11) NOT NULL,
  `win6` int(11) NOT NULL,
  `dtm` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lottery_log
-- ----------------------------

-- ----------------------------
-- Table structure for `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_player` varchar(255) NOT NULL,
  `to_player` varchar(255) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `unread` tinyint(1) NOT NULL,
  `text` varchar(255) NOT NULL,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------

-- ----------------------------
-- Table structure for `mospawner`
-- ----------------------------
DROP TABLE IF EXISTS `mospawner`;
CREATE TABLE `mospawner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mospawner
-- ----------------------------

-- ----------------------------
-- Table structure for `server`
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `tax` bigint(250) NOT NULL,
  `online` int(250) NOT NULL,
  `day` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `maxonline` int(11) NOT NULL,
  `jackpot` bigint(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of server
-- ----------------------------
INSERT INTO `server` VALUES ('0', '0', '2015-01-04 19:32:10', '0', '0');

-- ----------------------------
-- Table structure for `shoptotal`
-- ----------------------------
DROP TABLE IF EXISTS `shoptotal`;
CREATE TABLE `shoptotal` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `total` int(255) NOT NULL DEFAULT '0',
  `buy` int(255) NOT NULL DEFAULT '0',
  `sell` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shoptotal
-- ----------------------------
INSERT INTO `shoptotal` VALUES ('1', '0', '0', '0');

-- ----------------------------
-- Table structure for `shop_log`
-- ----------------------------
DROP TABLE IF EXISTS `shop_log`;
CREATE TABLE `shop_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` text CHARACTER SET latin1 NOT NULL,
  `Dealings` text CHARACTER SET latin1 NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_type` int(11) NOT NULL,
  `Amount` int(11) NOT NULL,
  `money` bigint(11) NOT NULL,
  `log_dtm` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_log
-- ----------------------------

-- ----------------------------
-- Table structure for `shop_price`
-- ----------------------------
DROP TABLE IF EXISTS `shop_price`;
CREATE TABLE `shop_price` (
  `id` int(200) NOT NULL,
  `type` tinyint(126) NOT NULL DEFAULT '0',
  `buy` int(200) NOT NULL,
  `sell` int(200) NOT NULL,
  `buy_transaction` tinyint(1) NOT NULL DEFAULT '1',
  `sell_transaction` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_price
-- ----------------------------
INSERT INTO `shop_price` VALUES ('1', '0', '8', '4', '1', '1');
INSERT INTO `shop_price` VALUES ('2', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('3', '0', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('4', '0', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('5', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('5', '1', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('5', '2', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('5', '3', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('6', '0', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('6', '1', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('6', '2', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('6', '3', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('7', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('8', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('9', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('10', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('11', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('12', '0', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('13', '0', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('14', '0', '100', '50', '0', '1');
INSERT INTO `shop_price` VALUES ('15', '0', '80', '40', '0', '1');
INSERT INTO `shop_price` VALUES ('16', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('17', '0', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('17', '1', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('17', '2', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('17', '3', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('18', '0', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('18', '1', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('18', '2', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('18', '3', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('19', '0', '328', '164', '1', '1');
INSERT INTO `shop_price` VALUES ('20', '0', '2', '1', '1', '1');
INSERT INTO `shop_price` VALUES ('21', '0', '980', '490', '0', '1');
INSERT INTO `shop_price` VALUES ('22', '0', '17640', '8820', '0', '0');
INSERT INTO `shop_price` VALUES ('23', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('24', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('24', '1', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('24', '2', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('25', '0', '20', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('26', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('27', '0', '30', '15', '0', '1');
INSERT INTO `shop_price` VALUES ('28', '0', '20', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('29', '0', '90', '45', '0', '1');
INSERT INTO `shop_price` VALUES ('30', '0', '50', '25', '0', '1');
INSERT INTO `shop_price` VALUES ('31', '1', '10', '5', '0', '0');
INSERT INTO `shop_price` VALUES ('31', '2', '12', '6', '0', '0');
INSERT INTO `shop_price` VALUES ('32', '0', '30', '15', '0', '1');
INSERT INTO `shop_price` VALUES ('33', '0', '70', '35', '0', '1');
INSERT INTO `shop_price` VALUES ('34', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('35', '0', '10', '5', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '1', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '2', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '3', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '4', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '5', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '6', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '7', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '8', '16', '8', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '9', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '10', '14', '7', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '11', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '12', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '13', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '14', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('35', '15', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('36', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('37', '0', '8', '4', '0', '1');
INSERT INTO `shop_price` VALUES ('38', '0', '8', '4', '0', '1');
INSERT INTO `shop_price` VALUES ('39', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('40', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('41', '0', '900', '450', '0', '0');
INSERT INTO `shop_price` VALUES ('42', '0', '720', '360', '0', '0');
INSERT INTO `shop_price` VALUES ('43', '0', '8', '4', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '1', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '2', '12', '6', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '3', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '4', '40', '20', '0', '1');
INSERT INTO `shop_price` VALUES ('44', '5', '12', '6', '0', '1');
INSERT INTO `shop_price` VALUES ('45', '0', '40', '20', '0', '0');
INSERT INTO `shop_price` VALUES ('46', '0', '50', '25', '0', '1');
INSERT INTO `shop_price` VALUES ('47', '0', '30', '15', '0', '1');
INSERT INTO `shop_price` VALUES ('48', '0', '750', '375', '1', '1');
INSERT INTO `shop_price` VALUES ('49', '0', '100', '50', '0', '1');
INSERT INTO `shop_price` VALUES ('50', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('51', '0', '500', '250', '0', '0');
INSERT INTO `shop_price` VALUES ('52', '0', '250000', '125000', '0', '0');
INSERT INTO `shop_price` VALUES ('53', '0', '6', '3', '0', '1');
INSERT INTO `shop_price` VALUES ('54', '0', '8', '4', '0', '1');
INSERT INTO `shop_price` VALUES ('55', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('56', '0', '10000', '5000', '0', '1');
INSERT INTO `shop_price` VALUES ('57', '0', '90000', '45000', '0', '0');
INSERT INTO `shop_price` VALUES ('58', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('59', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('60', '0', '2', '1', '0', '0');
INSERT INTO `shop_price` VALUES ('61', '0', '16', '8', '0', '1');
INSERT INTO `shop_price` VALUES ('62', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('63', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('64', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('65', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('66', '0', '16', '8', '0', '1');
INSERT INTO `shop_price` VALUES ('67', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('68', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('69', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('70', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('71', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('72', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('73', '0', '120', '60', '0', '1');
INSERT INTO `shop_price` VALUES ('74', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('75', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('76', '0', '22', '11', '0', '1');
INSERT INTO `shop_price` VALUES ('77', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('78', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('79', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('80', '0', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('81', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('82', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('84', '0', '80', '40', '0', '1');
INSERT INTO `shop_price` VALUES ('85', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('86', '0', '14', '7', '0', '1');
INSERT INTO `shop_price` VALUES ('87', '0', '4', '2', '1', '1');
INSERT INTO `shop_price` VALUES ('88', '0', '10', '5', '1', '1');
INSERT INTO `shop_price` VALUES ('89', '0', '100', '50', '1', '1');
INSERT INTO `shop_price` VALUES ('90', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('91', '0', '16', '8', '0', '1');
INSERT INTO `shop_price` VALUES ('92', '0', '500', '250', '0', '1');
INSERT INTO `shop_price` VALUES ('93', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('94', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('95', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('96', '0', '12', '6', '0', '1');
INSERT INTO `shop_price` VALUES ('97', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('97', '1', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('97', '2', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('97', '3', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('98', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('98', '1', '350', '175', '1', '1');
INSERT INTO `shop_price` VALUES ('98', '2', '350', '175', '1', '1');
INSERT INTO `shop_price` VALUES ('98', '3', '900', '450', '1', '1');
INSERT INTO `shop_price` VALUES ('99', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '1', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '2', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '3', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '4', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '5', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '6', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '7', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '8', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '9', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '10', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '11', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '12', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '13', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '14', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('99', '15', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '1', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '2', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '3', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '4', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '5', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '6', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '7', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '8', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '9', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '10', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '11', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '12', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '13', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '14', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('100', '15', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('101', '0', '60', '30', '0', '1');
INSERT INTO `shop_price` VALUES ('102', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('103', '0', '14', '7', '0', '1');
INSERT INTO `shop_price` VALUES ('104', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('105', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('106', '0', '6', '3', '0', '1');
INSERT INTO `shop_price` VALUES ('107', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('108', '0', '60', '30', '0', '1');
INSERT INTO `shop_price` VALUES ('109', '0', '12', '6', '0', '1');
INSERT INTO `shop_price` VALUES ('110', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('111', '0', '6', '3', '1', '1');
INSERT INTO `shop_price` VALUES ('112', '0', '40', '20', '0', '1');
INSERT INTO `shop_price` VALUES ('113', '0', '60', '30', '0', '1');
INSERT INTO `shop_price` VALUES ('114', '0', '90', '45', '0', '1');
INSERT INTO `shop_price` VALUES ('115', '0', '20', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('116', '0', '36000', '18000', '0', '1');
INSERT INTO `shop_price` VALUES ('117', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('118', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('119', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('120', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('121', '0', '56', '28', '0', '1');
INSERT INTO `shop_price` VALUES ('122', '0', '1000000', '500000', '0', '0');
INSERT INTO `shop_price` VALUES ('123', '0', '280', '140', '0', '1');
INSERT INTO `shop_price` VALUES ('124', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('125', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('125', '1', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('125', '2', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('126', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('126', '1', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('126', '2', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('126', '3', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('127', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('128', '0', '6', '3', '0', '1');
INSERT INTO `shop_price` VALUES ('129', '0', '3000', '1500', '0', '1');
INSERT INTO `shop_price` VALUES ('130', '0', '3000', '1500', '0', '1');
INSERT INTO `shop_price` VALUES ('131', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('132', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('133', '0', '9000', '4500', '0', '0');
INSERT INTO `shop_price` VALUES ('134', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('135', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('136', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('137', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('138', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('139', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('139', '1', '750', '375', '0', '1');
INSERT INTO `shop_price` VALUES ('140', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('141', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('142', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('143', '0', '2', '1', '0', '1');
INSERT INTO `shop_price` VALUES ('144', '0', '300', '150', '0', '0');
INSERT INTO `shop_price` VALUES ('144', '1', '300', '150', '0', '0');
INSERT INTO `shop_price` VALUES ('144', '2', '300', '150', '0', '0');
INSERT INTO `shop_price` VALUES ('144', '3', '300', '150', '0', '0');
INSERT INTO `shop_price` VALUES ('144', '4', '300', '150', '0', '0');
INSERT INTO `shop_price` VALUES ('145', '0', '1600', '800', '0', '1');
INSERT INTO `shop_price` VALUES ('146', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('147', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('148', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('149', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('150', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('151', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('152', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('153', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('154', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('155', '0', '80', '40', '0', '0');
INSERT INTO `shop_price` VALUES ('155', '1', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('155', '2', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('156', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('157', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('158', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('256', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('257', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('258', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('259', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('260', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('261', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('262', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('263', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('263', '1', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('264', '0', '10000', '5000', '1', '1');
INSERT INTO `shop_price` VALUES ('265', '0', '80', '40', '1', '1');
INSERT INTO `shop_price` VALUES ('266', '0', '100', '50', '1', '1');
INSERT INTO `shop_price` VALUES ('267', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('268', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('269', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('270', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('271', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('272', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('273', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('274', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('275', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('276', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('277', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('278', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('279', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('280', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('281', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('282', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('283', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('284', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('285', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('286', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('287', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('288', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('289', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('290', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('291', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('292', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('293', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('294', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('295', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('296', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('297', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('298', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('299', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('300', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('301', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('302', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('303', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('304', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('305', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('306', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('307', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('308', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('309', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('310', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('311', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('312', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('313', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('314', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('315', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('316', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('317', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('318', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('319', '0', '40', '20', '0', '1');
INSERT INTO `shop_price` VALUES ('320', '0', '60', '30', '0', '0');
INSERT INTO `shop_price` VALUES ('321', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('322', '0', '100', '50', '0', '1');
INSERT INTO `shop_price` VALUES ('322', '1', '7200', '3600', '0', '1');
INSERT INTO `shop_price` VALUES ('323', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('324', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('325', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('326', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('327', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('328', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('329', '0', '100', '50', '1', '1');
INSERT INTO `shop_price` VALUES ('330', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('331', '0', '10', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('332', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('333', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('334', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('335', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('336', '0', '10', '5', '1', '1');
INSERT INTO `shop_price` VALUES ('337', '0', '18', '9', '1', '1');
INSERT INTO `shop_price` VALUES ('338', '0', '20', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('339', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('340', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('341', '0', '20', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('342', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('343', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('344', '0', '10', '5', '1', '1');
INSERT INTO `shop_price` VALUES ('345', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('346', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('347', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('348', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('349', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('350', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('351', '0', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '1', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '2', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '3', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '4', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '5', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '6', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '7', '36', '18', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '8', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '9', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '10', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '11', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '12', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '13', '48', '24', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '14', '24', '12', '1', '1');
INSERT INTO `shop_price` VALUES ('351', '15', '12', '6', '1', '1');
INSERT INTO `shop_price` VALUES ('352', '0', '36', '18', '0', '1');
INSERT INTO `shop_price` VALUES ('353', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('354', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('355', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('356', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('357', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('358', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('359', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('360', '0', '0', '8', '0', '1');
INSERT INTO `shop_price` VALUES ('361', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('362', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('363', '0', '40', '20', '0', '1');
INSERT INTO `shop_price` VALUES ('364', '0', '60', '30', '0', '0');
INSERT INTO `shop_price` VALUES ('365', '0', '40', '20', '0', '1');
INSERT INTO `shop_price` VALUES ('366', '0', '60', '30', '0', '0');
INSERT INTO `shop_price` VALUES ('367', '0', '4', '2', '0', '1');
INSERT INTO `shop_price` VALUES ('368', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('369', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('370', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('371', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('372', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('373', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('373', '16', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('373', '32', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('373', '64', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('374', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('375', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('376', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('377', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('378', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('379', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('380', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('381', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('382', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '50', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '51', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '52', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '54', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '55', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '56', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '57', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '58', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '59', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '60', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '61', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '62', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '66', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '90', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '91', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '92', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '93', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '94', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '95', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '96', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '98', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('383', '120', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('384', '0', '100000000', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('385', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('386', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('387', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('388', '0', '100', '50', '0', '1');
INSERT INTO `shop_price` VALUES ('389', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('390', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('391', '0', '0', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('392', '0', '0', '10', '0', '1');
INSERT INTO `shop_price` VALUES ('393', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('394', '0', '0', '5', '0', '1');
INSERT INTO `shop_price` VALUES ('395', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('396', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('397', '0', '500', '250', '1', '0');
INSERT INTO `shop_price` VALUES ('397', '1', '0', '0', '1', '0');
INSERT INTO `shop_price` VALUES ('397', '2', '500', '250', '1', '0');
INSERT INTO `shop_price` VALUES ('397', '3', '500', '250', '1', '0');
INSERT INTO `shop_price` VALUES ('397', '4', '500', '250', '1', '0');
INSERT INTO `shop_price` VALUES ('398', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('399', '0', '100000', '50000', '1', '1');
INSERT INTO `shop_price` VALUES ('400', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('401', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('402', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('403', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('404', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('405', '0', '10', '5', '1', '1');
INSERT INTO `shop_price` VALUES ('406', '0', '20', '10', '1', '1');
INSERT INTO `shop_price` VALUES ('407', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('408', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2257', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2258', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2259', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2260', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2261', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2262', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2263', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2264', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2265', '0', '0', '0', '0', '0');
INSERT INTO `shop_price` VALUES ('2266', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `simpleshop`
-- ----------------------------
DROP TABLE IF EXISTS `simpleshop`;
CREATE TABLE `simpleshop` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `item` int(255) NOT NULL,
  `type` int(255) NOT NULL,
  `buy` int(255) NOT NULL,
  `sell` int(255) NOT NULL,
  `per` int(255) NOT NULL,
  `stock` int(255) NOT NULL,
  `name` varchar(255) DEFAULT '',
  `many` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `item` (`item`,`type`,`buy`,`sell`,`per`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of simpleshop
-- ----------------------------
INSERT INTO `simpleshop` VALUES ('1', '1', '-1', '2147483647', '-1', '64', '0', 'Stone（石）', '0');
INSERT INTO `simpleshop` VALUES ('2', '2', '-1', '2147483647', '-1', '64', '0', 'Grass（草）', '0');
INSERT INTO `simpleshop` VALUES ('3', '3', '-1', '2147483647', '303', '64', '0', 'Dirt（土）', '0');
INSERT INTO `simpleshop` VALUES ('4', '4', '-1', '2147483647', '346', '64', '0', 'Cobblestone（丸石）', '0');
INSERT INTO `simpleshop` VALUES ('8', '12', '-1', '2147483647', '113', '64', '0', 'Sand（砂）', '0');
INSERT INTO `simpleshop` VALUES ('13', '6', '-1', '2147483647', '95', '64', '0', 'Sapling（苗木）', '0');
INSERT INTO `simpleshop` VALUES ('15', '13', '-1', '2147483647', '93', '64', '0', 'Gravel（砂利）', '0');
INSERT INTO `simpleshop` VALUES ('16', '14', '-1', '2147483647', '1892', '10', '0', 'Gold ore（金鉱石）', '0');
INSERT INTO `simpleshop` VALUES ('17', '15', '-1', '2147483647', '1493', '10', '0', 'Iron ore（鉄鉱石）', '0');
INSERT INTO `simpleshop` VALUES ('18', '16', '-1', '2147483647', '-1', '10', '0', 'Coal ore（石炭鉱石）', '0');
INSERT INTO `simpleshop` VALUES ('19', '17', '-1', '2147483647', '187', '64', '0', 'Wood（木）', '0');
INSERT INTO `simpleshop` VALUES ('20', '19', '-1', '2147483647', '890', '10', '0', 'Sponge（スポンジ）', '0');
INSERT INTO `simpleshop` VALUES ('21', '20', '-1', '2147483647', '171', '64', '0', 'Glass（ガラス）', '0');
INSERT INTO `simpleshop` VALUES ('22', '21', '-1', '2147483647', '2520', '1', '0', 'Lapis Lazuli ore（ラピスラズリ原石）', '0');
INSERT INTO `simpleshop` VALUES ('23', '23', '-1', '2147483647', '429', '64', '0', 'Dispenser（ディスペンサー）', '0');
INSERT INTO `simpleshop` VALUES ('24', '25', '-1', '2147483647', '1132', '20', '0', 'NoteBlock（音楽ブロック）', '0');
INSERT INTO `simpleshop` VALUES ('25', '35', '-1', '2147483647', '1987', '64', '0', 'Wool（ウール）', '0');
INSERT INTO `simpleshop` VALUES ('26', '37', '-1', '2147483647', '258', '10', '0', 'Yellow Flower（イエローフラワー）', '0');
INSERT INTO `simpleshop` VALUES ('27', '38', '-1', '2147483647', '258', '10', '0', 'Red rose（レッドローズ）', '0');
INSERT INTO `simpleshop` VALUES ('28', '39', '-1', '2147483647', '421', '10', '0', 'Brown Mushroom（ブラウンマッシュルーム）', '0');
INSERT INTO `simpleshop` VALUES ('29', '40', '-1', '2147483647', '432', '10', '0', 'Red Mushroom（レッドマッシュルーム）', '0');
INSERT INTO `simpleshop` VALUES ('30', '43', '-1', '2147483647', '210', '64', '0', 'Double stone Slab（ダブルステップ）', '0');
INSERT INTO `simpleshop` VALUES ('31', '48', '-1', '2147483647', '325', '10', '0', 'Moss Stone（苔石）', '0');
INSERT INTO `simpleshop` VALUES ('32', '56', '-1', '2147483647', '9998', '1', '0', 'Diamond ore（ダイアモンド鉱石）', '0');
INSERT INTO `simpleshop` VALUES ('33', '78', '-1', '2147483647', '-1', '10', '0', 'Snow（雪）', '0');
INSERT INTO `simpleshop` VALUES ('34', '79', '-1', '2147483647', '-1', '10', '0', 'Ice（氷）', '0');
INSERT INTO `simpleshop` VALUES ('37', '80', '-1', '2147483647', '-1', '64', '0', 'Snow Block（雪ブロック）', '0');
INSERT INTO `simpleshop` VALUES ('38', '84', '-1', '2147483647', '-1', '1', '0', 'Jukebox（ジュークボックス）', '0');
INSERT INTO `simpleshop` VALUES ('39', '86', '-1', '2147483647', '357', '10', '0', 'Pumpkin（かぼちゃ）', '0');
INSERT INTO `simpleshop` VALUES ('40', '87', '-1', '2147483647', '219', '64', '0', 'Netherrack（ネザーラック）', '0');
INSERT INTO `simpleshop` VALUES ('41', '88', '-1', '2147483647', '253', '64', '0', 'Soul Sand（魂の砂）', '0');
INSERT INTO `simpleshop` VALUES ('42', '89', '-1', '2147483647', '-1', '64', '0', 'Growstone（輝石）', '0');
INSERT INTO `simpleshop` VALUES ('43', '260', '-1', '2147483647', '-1', '5', '0', 'Apple（りんご）', '0');
INSERT INTO `simpleshop` VALUES ('44', '262', '-1', '2147483647', '-1', '64', '0', 'Aroow（矢）', '0');
INSERT INTO `simpleshop` VALUES ('45', '2256', '-1', '2147483647', '-1', '1', '0', 'Gold Music Disk（ゴールドミュージックディスク）', '0');
INSERT INTO `simpleshop` VALUES ('46', '2257', '-1', '2147483647', '-1', '1', '0', 'Green Music Disk（グリーンミュージックディスク）', '0');
INSERT INTO `simpleshop` VALUES ('48', '49', '-1', '2147483647', '84', '1', '0', 'Obsidian（黒曜石）', '0');
INSERT INTO `simpleshop` VALUES ('51', '263', '-1', '2147483647', '535', '64', '0', 'Coal（石炭）', '0');
INSERT INTO `simpleshop` VALUES ('52', '264', '-1', '2147483647', '10002', '1', '0', 'Diamond（ダイアモンド）', '0');
INSERT INTO `simpleshop` VALUES ('53', '265', '-1', '2147483647', '-1', '10', '0', 'Iron lngot（鉄塊）', '0');
INSERT INTO `simpleshop` VALUES ('55', '297', '-1', '2147483647', '181', '10', '0', 'Bread（パン）', '0');
INSERT INTO `simpleshop` VALUES ('56', '335', '-1', '2147483647', '-1', '1', '0', 'Milk（牛乳）', '0');
INSERT INTO `simpleshop` VALUES ('57', '344', '-1', '2147483647', '55', '5', '0', 'Egg（卵）', '0');
INSERT INTO `simpleshop` VALUES ('58', '349', '-1', '2147483647', '-1', '1', '0', 'Raw Fish（生魚）', '0');
INSERT INTO `simpleshop` VALUES ('59', '351', '-1', '2147483647', '442', '10', '0', 'Ink Sac（イカ墨）', '0');
INSERT INTO `simpleshop` VALUES ('60', '353', '-1', '2147483647', '303', '64', '0', 'Sugar（砂糖）', '0');
INSERT INTO `simpleshop` VALUES ('61', '266', '-1', '2147483647', '-1', '10', '0', 'Gold lngot（金塊）', '0');
INSERT INTO `simpleshop` VALUES ('62', '354', '-1', '2147483647', '-1', '1', '0', 'Cake（ケーキ）', '0');
INSERT INTO `simpleshop` VALUES ('63', '66', '-1', '2147483647', '11', '64', '0', 'Minecart Tracks（線路）', '0');
INSERT INTO `simpleshop` VALUES ('64', '73', '-1', '2147483647', '1021', '64', '0', 'Redstone Ore（レッドストーン鉱石）', '0');
INSERT INTO `simpleshop` VALUES ('66', '83', '-1', '2147483647', '301', '64', '0', 'Sugar Cane（サトウキビ）', '0');
INSERT INTO `simpleshop` VALUES ('68', '282', '-1', '2147483647', '-1', '1', '0', 'Mushroom Soup（マッシュルームスープ）', '0');
INSERT INTO `simpleshop` VALUES ('69', '287', '-1', '2147483647', '-1', '1', '0', 'String（糸）', '0');
INSERT INTO `simpleshop` VALUES ('70', '288', '-1', '2147483647', '-1', '1', '0', 'Feather（羽）', '0');
INSERT INTO `simpleshop` VALUES ('71', '289', '-1', '2147483647', '-1', '1', '0', 'Sulphur（火薬）', '0');
INSERT INTO `simpleshop` VALUES ('72', '295', '-1', '2147483647', '89', '64', '0', 'Seeds（種）', '0');
INSERT INTO `simpleshop` VALUES ('73', '296', '-1', '2147483647', '175', '32', '0', 'Wheat（麦）', '0');
INSERT INTO `simpleshop` VALUES ('74', '318', '-1', '2147483647', '-1', '1', '0', 'Flint（燧石※すいせき）', '0');
INSERT INTO `simpleshop` VALUES ('75', '319', '-1', '2147483647', '-1', '1', '0', 'Raw Porkchop（生豚肉）', '0');
INSERT INTO `simpleshop` VALUES ('76', '320', '-1', '2147483647', '-1', '1', '0', 'Cooked Porkchop（調理済み豚肉）', '0');
INSERT INTO `simpleshop` VALUES ('77', '322', '-1', '2147483647', '-1', '1', '0', 'Golden apple（ゴールデンアップル）', '0');
INSERT INTO `simpleshop` VALUES ('78', '326', '-1', '2147483647', '-1', '1', '0', 'Water bucket（水入りバケツ）', '0');
INSERT INTO `simpleshop` VALUES ('81', '332', '-1', '2147483647', '-1', '1', '0', 'Snowball（雪球）', '0');
INSERT INTO `simpleshop` VALUES ('83', '337', '-1', '2147483647', '12832', '10', '0', 'Clay Balls（粘土）', '0');
INSERT INTO `simpleshop` VALUES ('84', '341', '-1', '2147483647', '101', '10', '0', 'Slimeball（スライムボール）', '0');
INSERT INTO `simpleshop` VALUES ('85', '348', '-1', '2147483647', '-1', '1', '0', 'Glowstone Dust（グロウストーン粉）', '0');
INSERT INTO `simpleshop` VALUES ('86', '350', '-1', '2147483647', '-1', '1', '0', 'Cooked Fish（調理済み魚）', '0');
INSERT INTO `simpleshop` VALUES ('87', '352', '-1', '2147483647', '345', '20', '0', 'Bone（骨）', '0');
INSERT INTO `simpleshop` VALUES ('88', '82', '-1', '2147483647', '-1', '1', '0', 'Cactus（サボテン）', '0');
INSERT INTO `simpleshop` VALUES ('89', '81', '-1', '2147483647', '257', '10', '0', 'Cactus（サボテン）', '0');
INSERT INTO `simpleshop` VALUES ('90', '327', '-1', '2147483647', '-1', '1', '0', 'Lava bucket（溶岩入りバケツ）', '0');
INSERT INTO `simpleshop` VALUES ('91', '331', '-1', '2147483647', '-1', '1', '0', 'Redstone（レッドストーン）', '0');
INSERT INTO `simpleshop` VALUES ('92', '334', '-1', '2147483647', '-1', '1', '0', 'Leather（革）', '0');
INSERT INTO `simpleshop` VALUES ('94', '18', '-1', '2147483647', '-1', '64', '0', 'Leaves（葉っぱ）', '0');
INSERT INTO `simpleshop` VALUES ('96', '80', '-1', '2147483647', '-1', '10', '0', 'SnowBlock（雪ブロック）', '0');

-- ----------------------------
-- Table structure for `spawnlocation`
-- ----------------------------
DROP TABLE IF EXISTS `spawnlocation`;
CREATE TABLE `spawnlocation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT 'Player',
  `world` int(4) NOT NULL DEFAULT '0',
  `x` double NOT NULL DEFAULT '0',
  `y` tinyint(4) NOT NULL DEFAULT '0',
  `z` double NOT NULL DEFAULT '0',
  `yaw` smallint(6) NOT NULL DEFAULT '0',
  `pitch` smallint(6) NOT NULL DEFAULT '0',
  `use` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`world`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spawnlocation
-- ----------------------------

-- ----------------------------
-- Table structure for `templocation`
-- ----------------------------
DROP TABLE IF EXISTS `templocation`;
CREATE TABLE `templocation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT 'Player',
  `world` int(4) NOT NULL DEFAULT '0',
  `x` double NOT NULL DEFAULT '0',
  `y` tinyint(4) NOT NULL DEFAULT '0',
  `z` double NOT NULL DEFAULT '0',
  `yaw` smallint(6) NOT NULL DEFAULT '0',
  `pitch` smallint(6) NOT NULL DEFAULT '0',
  `use` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of templocation
-- ----------------------------

-- ----------------------------
-- Table structure for `tempspawn`
-- ----------------------------
DROP TABLE IF EXISTS `tempspawn`;
CREATE TABLE `tempspawn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT 'Player',
  `world` int(4) NOT NULL DEFAULT '0',
  `x` double NOT NULL DEFAULT '0',
  `y` tinyint(4) NOT NULL DEFAULT '0',
  `z` double NOT NULL DEFAULT '0',
  `yaw` smallint(6) NOT NULL DEFAULT '0',
  `pitch` smallint(6) NOT NULL DEFAULT '0',
  `use` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tempspawn
-- ----------------------------

-- ----------------------------
-- Table structure for `title`
-- ----------------------------
DROP TABLE IF EXISTS `title`;
CREATE TABLE `title` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `title` varchar(8) NOT NULL,
  `note` text NOT NULL,
  `getinfo` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of title
-- ----------------------------
INSERT INTO `title` VALUES ('1', 'お初です！', '新規さんようこそ！！', 'サーバーに入る');
INSERT INTO `title` VALUES ('2', 'ICANFLY', '鳥になってこい！！', '***を使用する');

-- ----------------------------
-- Table structure for `users_online`
-- ----------------------------
DROP TABLE IF EXISTS `users_online`;
CREATE TABLE `users_online` (
  `name` varchar(32) CHARACTER SET latin1 NOT NULL,
  `time` datetime DEFAULT NULL,
  `online` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users_online
-- ----------------------------

-- ----------------------------
-- Table structure for `world`
-- ----------------------------
DROP TABLE IF EXISTS `world`;
CREATE TABLE `world` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `environment` int(100) NOT NULL,
  `type` varchar(255) NOT NULL DEFAULT 'DEFAULT',
  `border` int(100) NOT NULL DEFAULT '5000',
  `mob` tinyint(1) NOT NULL DEFAULT '0',
  `difficulty` int(10) NOT NULL DEFAULT '0',
  `command` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of world
-- ----------------------------
INSERT INTO `world` VALUES ('1', 'world_nether', '-1', 'DEFAULT', '250', '0', '3', '0');
INSERT INTO `world` VALUES ('2', 'skyland', '0', 'DEFAULT', '5000', '1', '3', '0');
INSERT INTO `world` VALUES ('3', 'harvest', '0', 'DEFAULT', '1000', '1', '3', '1');
INSERT INTO `world` VALUES ('4', 'new_world', '0', 'DEFAULT', '5000', '0', '3', '0');
INSERT INTO `world` VALUES ('5', 'new_nether', '-1', 'DEFAULT', '1500', '0', '3', '0');
INSERT INTO `world` VALUES ('6', 'world_the_end', '1', 'DEFAULT', '500', '0', '3', '1');
INSERT INTO `world` VALUES ('7', 'superflat', '0', 'FLAT', '3000', '0', '3', '0');
INSERT INTO `world` VALUES ('8', 'harvest_nether', '-1', 'DEFAULT', '125', '1', '3', '1');
INSERT INTO `world` VALUES ('9', 'portal_station', '0', 'DEFAULT', '50', '1', '3', '0');
INSERT INTO `world` VALUES ('10', 'world', '0', 'LARGE', '2000', '0', '3', '0');
