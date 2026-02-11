SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `paroli`
-- ----------------------------
DROP TABLE IF EXISTS `paroli`;
CREATE TABLE `paroli` (
  `template_obvi` int(10) NOT NULL,
  `id_victime` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

