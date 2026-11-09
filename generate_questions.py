import json
import csv

questions = [
    # ========== 1. 古诗词 (POETRY) ==========
    {"category": "POETRY", "question": "“床前明月光，疑是地上霜”的作者是哪位诗人？", "options": ["A. 杜甫", "B. 李白", "C. 白居易", "D. 王维"], "correctIndex": 1},
    {"category": "POETRY", "question": "“春眠不觉晓，处处闻啼鸟”出自孟浩然的哪首诗？", "options": ["A. 《春晓》", "B. 《村居》", "C. 《咏柳》", "D. 《小池》"], "correctIndex": 0},
    {"category": "POETRY", "question": "“谁知盘中餐，粒粒皆辛苦”表达的是什么主题？", "options": ["A. 赞美风景", "B. 珍惜粮食", "C. 思念家乡", "D. 歌颂友谊"], "correctIndex": 1},
    {"category": "POETRY", "question": "“鹅，鹅，鹅，曲项向天歌”是哪位诗人在七岁时写下的？", "options": ["A. 骆宾王", "B. 王勃", "C. 贺知章", "D. 贾岛"], "correctIndex": 0},
    {"category": "POETRY", "question": "“不知细叶谁裁出，二月春风似剪刀”赞美的是哪种植物？", "options": ["A. 桃花", "B. 柳树", "C. 荷花", "D. 松树"], "correctIndex": 1},
    {"category": "POETRY", "question": "“千门万户曈曈日，总把新桃换旧符”描写的是哪个传统节日？", "options": ["A. 中秋节", "B. 端午节", "C. 春节", "D. 重阳节"], "correctIndex": 2},
    {"category": "POETRY", "question": "“独在异乡为异客，每逢佳节倍思亲”中的“佳节”指什么节？", "options": ["A. 重阳节", "B. 清明节", "C. 元宵节", "D. 七夕节"], "correctIndex": 0},
    {"category": "POETRY", "question": "“飞流直下三千尺，疑是银河落九天”描写的是哪座名山的瀑布？", "options": ["A. 泰山", "B. 黄山", "C. 庐山", "D. 华山"], "correctIndex": 2},
    {"category": "POETRY", "question": "“接天莲叶无穷碧，映日荷花别样红”描绘的是哪个湖泊的景色？", "options": ["A. 洞庭湖", "B. 鄱阳湖", "C. 杭州西湖", "D. 太湖"], "correctIndex": 2},
    {"category": "POETRY", "question": "“小荷才露尖尖角，早有蜻蜓立上头”出自杨万里的哪首诗？", "options": ["A. 《小池》", "B. 《池上》", "C. 《所见》", "D. 《晓出净慈寺》"], "correctIndex": 0},
    {"category": "POETRY", "question": "“借问酒家何处有，牧童遥指杏花村”描写的是哪个节气或节日？", "options": ["A. 立春", "B. 清明节", "C. 端午节", "D. 中秋节"], "correctIndex": 1},
    {"category": "POETRY", "question": "“草长莺飞二月天，拂堤杨柳醉春烟”描写的季节是？", "options": ["A. 初春", "B. 盛夏", "C. 深秋", "D. 寒冬"], "correctIndex": 0},
    {"category": "POETRY", "question": "“远上寒山石径斜，白云生处有人家”出自杜牧的哪首诗？", "options": ["A. 《江南春》", "B. 《山行》", "C. 《清明》", "D. 《秋夕》"], "correctIndex": 1},
    {"category": "POETRY", "question": "“意欲捕鸣蝉，忽然闭口立”中，牧童想要捉的是什么昆虫？", "options": ["A. 蟋蟀", "B. 蝉（知了）", "C. 螳螂", "D. 蝴蝶"], "correctIndex": 1},
    {"category": "POETRY", "question": "“解落三秋叶，能开二月花”描写的自然现象是？", "options": ["A. 雨", "B. 风", "C. 雪", "D. 雷"], "correctIndex": 1},
    {"category": "POETRY", "question": "“生当作人杰，死亦为鬼雄”是哪位著名宋代女词人写的？", "options": ["A. 李清照", "B. 朱淑真", "C. 薛涛", "D. 鱼玄机"], "correctIndex": 0},
    {"category": "POETRY", "question": "“不识庐山真面目，只缘身在此山中”告诉我们什么哲理？", "options": ["A. 看景要离近", "B. 局限在局部看不清全面", "C. 庐山云雾太浓", "D. 爬山很累"], "correctIndex": 1},
    {"category": "POETRY", "question": "“小时不识月，呼作白玉盘”中，诗人把月亮比作了什么？", "options": ["A. 明镜", "B. 白玉盘", "C. 弯弓", "D. 珍珠"], "correctIndex": 1},
    {"category": "POETRY", "question": "“天门中断楚江开，碧水东流至此回”中的“楚江”指哪条江？", "options": ["A. 黄河", "B. 长江", "C. 珠江", "D. 淮河"], "correctIndex": 1},
    {"category": "POETRY", "question": "“会当凌绝顶，一览众山小”是杜甫登上哪座山时写的？", "options": ["A. 华山", "B. 泰山", "C. 恒山", "D. 衡山"], "correctIndex": 1},

    # ========== 2. 简单数学 (MATH) ==========
    {"category": "MATH", "question": "一个正方形有4条边，如果每条边长是5厘米，周长是多少厘米？", "options": ["A. 15厘米", "B. 20厘米", "C. 25厘米", "D. 30厘米"], "correctIndex": 1},
    {"category": "MATH", "question": "计算：9 × 8 = ？", "options": ["A. 72", "B. 64", "C. 81", "D. 70"], "correctIndex": 0},
    {"category": "MATH", "question": "一个正方体一共有多少个面？", "options": ["A. 4个", "B. 6个", "C. 8个", "D. 12个"], "correctIndex": 1},
    {"category": "MATH", "question": "小明有 15 支铅笔，小红比他多 7 支，小红有多少支铅笔？", "options": ["A. 20支", "B. 22支", "C. 24支", "D. 21支"], "correctIndex": 1},
    {"category": "MATH", "question": "1 小时等于多少分钟？", "options": ["A. 50分钟", "B. 60分钟", "C. 100分钟", "D. 120分钟"], "correctIndex": 1},
    {"category": "MATH", "question": "钟面上时针走一大格，分针正好走一整圈，分针走了多少分？", "options": ["A. 30分", "B. 60分", "C. 12分", "D. 24分"], "correctIndex": 1},
    {"category": "MATH", "question": "计算：100 - 37 = ？", "options": ["A. 63", "B. 73", "C. 53", "D. 67"], "correctIndex": 0},
    {"category": "MATH", "question": "一个三角形一共有几个内角？", "options": ["A. 2个", "B. 3个", "C. 4个", "D. 5个"], "correctIndex": 1},
    {"category": "MATH", "question": "2 米等于多少厘米？", "options": ["A. 20厘米", "B. 200厘米", "C. 2000厘米", "D. 50厘米"], "correctIndex": 1},
    {"category": "MATH", "question": "把 24 块饼干平均分给 4 个小朋友，每个小朋友分得几块？", "options": ["A. 5块", "B. 6块", "C. 7块", "D. 8块"], "correctIndex": 1},
    {"category": "MATH", "question": "最大的两位数是多少？", "options": ["A. 90", "B. 99", "C. 100", "D. 89"], "correctIndex": 1},
    {"category": "MATH", "question": "1 千克等于多少克？", "options": ["A. 100克", "B. 1000克", "C. 500克", "D. 10克"], "correctIndex": 1},
    {"category": "MATH", "question": "计算：7 × 7 = ？", "options": ["A. 42", "B. 49", "C. 56", "D. 48"], "correctIndex": 1},
    {"category": "MATH", "question": "直角是多少度？", "options": ["A. 45度", "B. 90度", "C. 180度", "D. 360度"], "correctIndex": 1},
    {"category": "MATH", "question": "笼子里有鸡和兔共5只，一共14只脚，请问兔有几只？", "options": ["A. 1只", "B. 2只", "C. 3只", "D. 4只"], "correctIndex": 1},
    {"category": "MATH", "question": "计算：12 × 5 = ？", "options": ["A. 50", "B. 60", "C. 70", "D. 65"], "correctIndex": 1},
    {"category": "MATH", "question": "一个星期一共有多少天？", "options": ["A. 5天", "B. 6天", "C. 7天", "D. 8天"], "correctIndex": 2},
    {"category": "MATH", "question": "长方形的长是 6 厘米，宽是 3 厘米，它的面积是多少平方厘米？", "options": ["A. 18", "B. 9", "C. 12", "D. 24"], "correctIndex": 0},
    {"category": "MATH", "question": "一个数除以 8 余数最大可能是几？", "options": ["A. 8", "B. 7", "C. 6", "D. 9"], "correctIndex": 1},
    {"category": "MATH", "question": "计算：250 + 750 = ？", "options": ["A. 900", "B. 1000", "C. 1100", "D. 950"], "correctIndex": 1},

    # ========== 3. 简单英语 (ENGLISH) ==========
    {"category": "ENGLISH", "question": "英文单词 “Apple” 中文是什么意思？", "options": ["A. 香蕉", "B. 苹果", "C. 橘子", "D. 葡萄"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“Good morning” 是向别人打什么招呼？", "options": ["A. 早上好", "B. 下午好", "C. 晚上好", "D. 再见"], "correctIndex": 0},
    {"category": "ENGLISH", "question": "“Red” 是指什么颜色？", "options": ["A. 蓝色", "B. 黄色", "C. 红色", "D. 绿色"], "correctIndex": 2},
    {"category": "ENGLISH", "question": "“Dog” 是指哪种可爱的小动物？", "options": ["A. 猫", "B. 狗", "C. 兔子", "D. 鸭子"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "英文中数字 “Eight” 对应的阿拉伯数字是？", "options": ["A. 6", "B. 7", "C. 8", "D. 9"], "correctIndex": 2},
    {"category": "ENGLISH", "question": "“Thank you” 的中文意思是什么？", "options": ["A. 对不起", "B. 谢谢你", "C. 没关系", "D. 请进"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "单词 “Book” 的中文含义是？", "options": ["A. 书本", "B. 书包", "C. 铅笔", "D. 橡皮"], "correctIndex": 0},
    {"category": "ENGLISH", "question": "当你想表达“我喜欢你”时，英文应该说？", "options": ["A. I like you", "B. I miss you", "C. You like me", "D. See you"], "correctIndex": 0},
    {"category": "ENGLISH", "question": "单词 “Banana” 是指哪种水果？", "options": ["A. 梨子", "B. 西瓜", "C. 香蕉", "D. 草莓"], "correctIndex": 2},
    {"category": "ENGLISH", "question": "家庭成员中，“Mother” 是指谁？", "options": ["A. 爸爸", "B. 妈妈", "C. 爷爷", "D. 奶奶"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“Big” 的反义词是哪个词？", "options": ["A. Tall", "B. Small", "C. Long", "D. Fast"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "英文中 “Cat” 是什么动物？", "options": ["A. 小猫", "B. 小狗", "C. 小鸟", "D. 熊猫"], "correctIndex": 0},
    {"category": "ENGLISH", "question": "“Blue” 代表什么颜色？", "options": ["A. 黑色", "B. 白色", "C. 蓝色", "D. 紫色"], "correctIndex": 2},
    {"category": "ENGLISH", "question": "“Water” 是什么意思？", "options": ["A. 牛奶", "B. 水", "C. 果汁", "D. 茶"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“Goodbye” 是什么意思？", "options": ["A. 你好", "B. 再见", "C. 晚安", "D. 欢迎"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“Sun” 指的是哪一天体？", "options": ["A. 月亮", "B. 太阳", "C. 星星", "D. 地球"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "英文数字 “Three” 代表几？", "options": ["A. 2", "B. 3", "C. 4", "D. 5"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“School” 中文是指什么地方？", "options": ["A. 公园", "B. 学校", "C. 医院", "D. 动物园"], "correctIndex": 1},
    {"category": "ENGLISH", "question": "“Pencil” 是指哪种文具？", "options": ["A. 铅笔", "B. 尺子", "C. 橡皮", "D. 钢笔"], "correctIndex": 0},
    {"category": "ENGLISH", "question": "“Fish” 是指哪种动物？", "options": ["A. 鱼", "B. 鸟", "C. 兔", "D. 猴"], "correctIndex": 0},

    # ========== 4. 生活百科 (ENCYCLOPEDIA) ==========
    {"category": "ENCYCLOPEDIA", "question": "如果发生火灾，我们应该拨打哪个紧急求助电话？", "options": ["A. 110", "B. 119", "C. 120", "D. 122"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "废旧电池属于哪一类垃圾？", "options": ["A. 厨余垃圾", "B. 可回收物", "C. 有害垃圾", "D. 其他垃圾"], "correctIndex": 2},
    {"category": "ENCYCLOPEDIA", "question": "人体正常体温大约是多少摄氏度左右？", "options": ["A. 35℃", "B. 36.5℃", "C. 39℃", "D. 40℃"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "过马路时，红绿灯中的哪个灯亮起表示可以通行？", "options": ["A. 红灯", "B. 黄灯", "C. 绿灯", "D. 闪烁灯"], "correctIndex": 2},
    {"category": "ENCYCLOPEDIA", "question": "为什么先看到闪电，后听到雷声？", "options": ["A. 光速比声速快", "B. 声速比光速快", "C. 眼睛比耳朵灵敏", "D. 雷声产生得晚"], "correctIndex": 0},
    {"category": "ENCYCLOPEDIA", "question": "打雷下雨时，我们在室外不能站在哪里避雨？", "options": ["A. 室内", "B. 大树下", "C. 车内", "D. 避雷棚下"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "人体如果缺乏哪种维生素容易患坏血病（多吃新鲜水果能补充）？", "options": ["A. 维生素A", "B. 维生素C", "C. 维生素D", "D. 维生素B"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "食物保存在冰箱里不容易变质，主要原因是什么？", "options": ["A. 没有空气", "B. 低温抑制细菌繁殖", "C. 冰箱能杀光细菌", "D. 里面很黑"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "生水一定要烧开才能喝，主要是为了什么？", "options": ["A. 让水变甜", "B. 高温杀灭细菌", "C. 改变水的颜色", "D. 蒸发杂质"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "眼睛感到疲劳时，看远处的哪种颜色植物有助于放松眼睛？", "options": ["A. 红色", "B. 黑色", "C. 绿色", "D. 紫色"], "correctIndex": 2},
    {"category": "ENCYCLOPEDIA", "question": "自行车骑行时刹车，利用的是什么物理原理？", "options": ["A. 重力", "B. 摩擦力", "C. 浮力", "D. 弹力"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "发生轻微烫伤时，第一步正确的处理方法是？", "options": ["A. 涂抹牙膏", "B. 用流动的冷水冲洗降温", "C. 涂酱油", "D. 用布包紧"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "苍蝇在停下来的时候经常做的一个动作是什么？", "options": ["A. 睡觉", "B. 搓脚（手）", "C. 唱歌", "D. 跳舞"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "下列哪种食物钙含量丰富，非常有利于骨骼生长？", "options": ["A. 牛奶", "B. 糖果", "C. 薯条", "D. 汽水"], "correctIndex": 0},
    {"category": "ENCYCLOPEDIA", "question": "彩虹一般出现在什么天气之后？", "options": ["A. 大雪之后", "B. 雨过天晴", "C. 大风之后", "D. 大雾天"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "声音是由物体的什么现象产生的？", "options": ["A. 发光", "B. 振动", "C. 加热", "D. 静止"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "每天早晚刷牙，单次建议刷牙时长大约是多久比较科学？", "options": ["A. 30秒", "B. 1分钟", "C. 2~3分钟", "D. 10分钟"], "correctIndex": 2},
    {"category": "ENCYCLOPEDIA", "question": "我们呼吸时，人体吸入氧气，呼出的主要气体中什么成分增加了？", "options": ["A. 氧气", "B. 二氧化碳", "C. 氢气", "D. 一氧化碳"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "蜜蜂酿蜜主要采集植物的什么部位？", "options": ["A. 树皮", "B. 花蜜（花朵）", "C. 树叶", "D. 树根"], "correctIndex": 1},
    {"category": "ENCYCLOPEDIA", "question": "指南针的指针静止时，N极指向的是地球的哪一方？", "options": ["A. 东", "B. 南", "C. 西", "D. 北"], "correctIndex": 3},

    # ========== 5. 地理自然 (GEOGRAPHY) ==========
    {"category": "GEOGRAPHY", "question": "中国的首都是哪座城市？", "options": ["A. 上海", "B. 北京", "C. 广州", "D. 深圳"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "中国第一大岛是哪个岛屿？", "options": ["A. 海南岛", "B. 台湾岛", "C. 崇明岛", "D. 舟山岛"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "被称为中华民族“母亲河”的是哪条河流？", "options": ["A. 长江", "B. 黄河", "C. 珠江", "D. 黑龙江"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "中国五岳之首是哪座名山？", "options": ["A. 东岳泰山", "B. 西岳华山", "C. 南岳衡山", "D. 北岳恒山"], "correctIndex": 0},
    {"category": "GEOGRAPHY", "question": "世界上面积最大的大洋是哪个？", "options": ["A. 大西洋", "B. 太平洋", "C. 印度洋", "D. 北冰洋"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "世界上海拔最高的山峰是哪座？", "options": ["A. 珠穆朗玛峰", "B. 乔戈里峰", "C. 富士山", "D. 乞力马扎罗山"], "correctIndex": 0},
    {"category": "GEOGRAPHY", "question": "地球自转一周的时间大约是多久？", "options": ["A. 1小时", "B. 24小时（1天）", "C. 1个月", "D. 1年（365天）"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "地球围绕太阳公转一周产生了什么自然现象？", "options": ["A. 昼夜交替", "B. 四季更替", "C. 潮起潮落", "D. 地震海啸"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "地球上淡水资源储量最多的地方是？", "options": ["A. 地下水", "B. 江河湖泊", "C. 南极和北极的冰川", "D. 大气水"], "correctIndex": 2},
    {"category": "GEOGRAPHY", "question": "太阳系八大行星中，体积最大的是哪一颗？", "options": ["A. 地球", "B. 火星", "C. 木星", "D. 金星"], "correctIndex": 2},
    {"category": "GEOGRAPHY", "question": "中国陆地面积大约是多少平方公里？", "options": ["A. 960万", "B. 1200万", "C. 800万", "D. 650万"], "correctIndex": 0},
    {"category": "GEOGRAPHY", "question": "中国最大的沙漠是哪一个？", "options": ["A. 腾格里沙漠", "B. 塔克拉玛干沙漠", "C. 巴丹吉林沙漠", "D. 库布齐沙漠"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "企鹅主要生活在地球的哪一个区域？", "options": ["A. 北极", "B. 南极", "C. 赤道", "D. 热带雨林"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "北极熊主要生活在地球的哪一个区域？", "options": ["A. 北极", "B. 南极", "C. 撒哈拉沙漠", "D. 澳大利亚"], "correctIndex": 0},
    {"category": "GEOGRAPHY", "question": "月亮本身会发光吗？", "options": ["A. 会自己发光", "B. 不会，反射太阳光", "C. 晚上发光白天不发光", "D. 靠星光照亮"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "中国最长的河流是哪一条？", "options": ["A. 黄河", "B. 长江", "C. 澜沧江", "D. 淮河"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "世界上人口最多的国家属于哪个大洲？", "options": ["A. 欧洲", "B. 亚洲", "C. 非洲", "D. 北美洲"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "四川省最有代表性的国家一级保护野生动物是？", "options": ["A. 藏羚羊", "B. 大熊猫", "C. 东北虎", "D. 丹顶鹤"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "降水量极少、昼夜温差极大的自然地貌通常是？", "options": ["A. 森林", "B. 沙漠", "C. 沼泽", "D. 湿地"], "correctIndex": 1},
    {"category": "GEOGRAPHY", "question": "太阳是从哪一个方向升起、哪一个方向落下的？", "options": ["A. 西升东落", "B. 东升西落", "C. 南升北落", "D. 北升南落"], "correctIndex": 1}
]

# 给每道题生成唯一 ID
for i, q in enumerate(questions, 1):
    q["id"] = f"q_{i:04d}"

# 1. 写入 assets/default_questions.json (APK 内置题库)
with open("app/src/main/assets/default_questions.json", "w", encoding="utf-8") as f:
    json.dump(questions, f, ensure_ascii=False, indent=2)

# 2. 写入 study_questions.json (U盘更新文件备选)
with open("study_questions.json", "w", encoding="utf-8") as f:
    json.dump(questions, f, ensure_ascii=False, indent=2)

# 3. 写入 study_questions.csv (供用户用 Excel 直接查看与编辑，放入 U 盘根目录即可)
with open("study_questions.csv", "w", newline="", encoding="utf-8-sig") as f:
    writer = csv.writer(f)
    writer.writerow(["分类(POETRY/MATH/ENGLISH/ENCYCLOPEDIA/GEOGRAPHY)", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案(A/B/C/D)"])
    ans_map = {0: "A", 1: "B", 2: "C", 3: "D"}
    for q in questions:
        writer.writerow([
            q["category"],
            q["question"],
            q["options"][0],
            q["options"][1],
            q["options"][2],
            q["options"][3],
            ans_map[q["correctIndex"]]
        ])

print(f"成功生成并写入题库：共 {len(questions)} 道精品题目！")
