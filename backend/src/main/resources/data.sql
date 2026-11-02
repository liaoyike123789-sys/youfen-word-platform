-- 初始化分类
INSERT INTO word_category (id, name, description) VALUES
 (1, '四级核心', '大学英语四级高频词汇'),
 (2, '六级核心', '大学英语六级高频词汇'),
 (3, '考研高频', '考研英语高频词汇'),
 (4, '技术英语', '计算机与技术相关词汇');

-- 初始化单词（30 个，便于演示与测试）
INSERT INTO word (id, spelling, phonetic, meaning, category_id, difficulty) VALUES
 (1,'abandon','/əˈbændən/','v. 抛弃，放弃',1,1),
 (2,'ability','/əˈbɪləti/','n. 能力，才能',1,1),
 (3,'absorb','/əbˈzɔːb/','v. 吸收，吸引',1,2),
 (4,'academic','/ˌækəˈdemɪk/','adj. 学术的，理论的',1,2),
 (5,'accept','/əkˈsept/','v. 接受，同意',1,1),
 (6,'access','/ˈækses/','n. 通道；v. 访问，存取',1,2),
 (7,'acquire','/əˈkwaɪə(r)/','v. 获得，取得',1,3),
 (8,'adapt','/əˈdæpt/','v. 适应，改编',1,2),
 (9,'adequate','/ˈædɪkwət/','adj. 足够的，适当的',1,3),
 (10,'adjust','/əˈdʒʌst/','v. 调整，适应',1,2),
 (11,'analyze','/ˈænəlaɪz/','v. 分析，解析',2,2),
 (12,'approach','/əˈprəʊtʃ/','n./v. 方法；接近',2,2),
 (13,'appropriate','/əˈprəʊpriət/','adj. 适当的，恰当的',2,3),
 (14,'assess','/əˈses/','v. 评估，估价',2,3),
 (15,'assume','/əˈsjuːm/','v. 假定，承担',2,3),
 (16,'attribute','/əˈtrɪbjuːt/','v. 归因于；n. 属性',2,3),
 (17,'benefit','/ˈbenɪfɪt/','n./v. 利益；有益于',2,1),
 (18,'concept','/ˈkɒnsept/','n. 概念，观念',2,2),
 (19,'conclude','/kənˈkluːd/','v. 推断，得出结论',2,3),
 (20,'conduct','/kənˈdʌkt/','v. 实施；n. 行为',2,2),
 (21,'database','/ˈdeɪtəbeɪs/','n. 数据库',4,1),
 (22,'deploy','/dɪˈplɔɪ/','v. 部署，调度',4,2),
 (23,'exception','/ɪkˈsepʃn/','n. 异常，例外',4,2),
 (24,'framework','/ˈfreɪmwɜːk/','n. 框架，结构',4,2),
 (25,'interface','/ˈɪntəfeɪs/','n. 接口，界面',4,1),
 (26,'middleware','/ˈmɪdlweə(r)/','n. 中间件',4,3),
 (27,'repository','/rɪˈpɒzətri/','n. 仓库，代码库',4,2),
 (28,'transaction','/trænˈzækʃn/','n. 事务，交易',4,3),
 (29,'authentication','/ɔːˌθentɪˈkeɪʃn/','n. 认证，鉴定',3,3),
 (30,'scalability','/ˌskeɪləˈbɪləti/','n. 可扩展性',3,3);
