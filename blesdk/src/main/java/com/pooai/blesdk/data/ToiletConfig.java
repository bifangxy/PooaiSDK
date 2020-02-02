package com.pooai.blesdk.data;

/**
 * 作者：created by xieying on 2020-01-28 14:56
 * 功能：
 */
public class ToiletConfig {

    public static final int LED_FLASH_FREQ = 50;     //呼吸灯闪烁频率
    public static final int KEY_BEEP_SHORT = 2;     //按键短按声
    public static final int KEY_BEEP_LONG = 20;         //按键长按声
    public static final int KEY_DBCLICK_TIME = 50;    //按键双击计时

    public static final int KEY_RELEASE = 0;    //无按键
    public static final int KEY_CLICK = 1;    //单按下
    public static final int KEY_LONG_CLICK = 2;    //长按下
    public static final int KEY_DOUBLE_CLICK = 3;    //双击
    public static final int IR_TIMEOUT = 1500;  //红外超时，默认15s
    public static final int SEAT_THRESHOLD = 20;  //着座阈值20kg,单位0.1kg
    public static final int FAN_PWM_WIDTH = 20;//药熏风扇PWM宽度	 每一次计数时间为50us，即此参数为20是，刚好1KHz
    //马桶寄存器定义
    public static final int REGISTER_URINE_DOOR = 0;    //尿检开关仓门
    public static final int REGISTER_URINE_MOTOR = 1;//尿检马达控制
    public static final int REGISTER_URINE_DETECT = 2;    //尿检执行一次检测
    public static final int REGISTER_URINE_LIGHT = 3;    //尿检LED电流设置
    public static final int REGISTER_URINE_TEMP_R = 4;    //尿检临时R分量
    public static final int REGISTER_URINE_TEMP_G = 5;//尿检临时G分量
    public static final int REGISTER_URINE_TEMP_B = 6;//尿检临时B分量
    public static final int REGISTER_URINE_POSITION = 7;    //尿检检测槽位置
    public static final int REGISTER_URINE_TEST1 = 8;    //尿检-尿检开始
    public static final int REGISTER_URINE_TEST2 = 9;    //尿检-孕检开始
    public static final int REGISTER_URINE_TEST3 = 10;    //尿检-排卵开始
    public static final int REGISTER_URINE_STATUS = 11;//尿检-系统状态
    public static final int REGISTER_URINE_AUX1 = 12;    //尿检-备用寄存器
    public static final int REGISTER_URINE_COLOR1 = 13;//尿检-颜色寄存器1
    public static final int REGISTER_URINE_COLOR2 = 14;//尿检-颜色寄存器2
    public static final int REGISTER_URINE_COLOR3 = 15;//尿检-颜色寄存器3
    public static final int REGISTER_URINE_COLOR4 = 16;//尿检-颜色寄存器4
    public static final int REGISTER_URINE_COLOR5 = 17;//尿检-颜色寄存器5
    public static final int REGISTER_URINE_COLOR6 = 18;//尿检-颜色寄存器6
    public static final int REGISTER_URINE_COLOR7 = 19;//尿检-颜色寄存器7
    public static final int REGISTER_URINE_COLOR8 = 20;//尿检-颜色寄存器8
    public static final int REGISTER_URINE_COLOR9 = 21;//尿检-颜色寄存器9
    public static final int REGISTER_URINE_COLOR10 = 22;//尿检-颜色寄存器10
    public static final int REGISTER_URINE_COLOR11 = 23;//尿检-颜色寄存器11
    public static final int REGISTER_URINE_COLOR12 = 24;//尿检-颜色寄存器12
    public static final int REGISTER_URINE_COLOR13 = 25;//尿检-颜色寄存器13
    public static final int REGISTER_URINE_COLOR14 = 26;//尿检-颜色寄存器14
    public static final int REGISTER_URINE_COLOR15 = 27;//尿检-颜色寄存器15
    public static final int REGISTER_URINE_COLOR16 = 28;//尿检-颜色寄存器16
    public static final int REGISTER_URINE_COLOR17 = 29;//尿检-颜色寄存器17
    public static final int REGISTER_URINE_YEAR_MONTH = 37;//尿检模块，出厂年月
    public static final int REGISTER_URINE_TYPE = 38;//尿检模块，出厂日，产品类型
    public static final int REGISTER_URINE_SERIAL = 39;    //尿检模块唯一序列号
    public static final int REGISTER_URINE_VERSION = 40;//尿检程序版本

    public static final int REGISTER_URINE_WB_R = 34;//尿检白平衡R
    public static final int REGISTER_URINE_WB_G = 35;    //尿检白平衡G
    public static final int REGISTER_URINE_WB_B = 36;//尿检白平衡B

    public static final int REGISTER_WEIGHT_CMD = 30;//称重命令寄存器
    public static final int REGISTER_WEIGHT_VALUE = 31;//称重重量（0.1kg）
    public static final int REGISTER_WEIGHT_STATE = 32;//称重状态
    public static final int REGISTER_WEIGHT_REALTIME = 33;//称重实时状态

    public static final int REGISTER_TOILET_START_ADDRESS = 50;     //马桶部分起始寄存器地址

    public static final int REGISTER_TOILET_CONTROL = REGISTER_TOILET_START_ADDRESS;    //马桶控制寄存器

    public static final int REGISTER_TOILET_CONTROL2 = REGISTER_TOILET_START_ADDRESS + 1;    //马桶控制寄存器2

    public static final int REGISTER_TOILET_ERROR = REGISTER_TOILET_START_ADDRESS + 2;    //马桶错误寄存器
    public static final int REGISTER_TOILET_CONFIG1 = REGISTER_TOILET_START_ADDRESS + 3;    //马桶配制寄存器1

    public static final int REGISTER_TOILET_CONFIG2 = REGISTER_TOILET_START_ADDRESS + 4;    //马桶配制寄存器2

    public static final int REGISTER_TOILET_CONFIG3 = REGISTER_TOILET_START_ADDRESS + 5;    //马桶配制寄存器3

    public static final int REGISTER_TOILET_CONFIG4 = REGISTER_TOILET_START_ADDRESS + 6;    //马桶配制寄存器4

    public static final int REGISTER_TOILET_CONFIG5 = REGISTER_TOILET_START_ADDRESS + 7;    //马桶配制寄存器5

    public static final int REGISTER_TOILET_STATUS1 = REGISTER_TOILET_START_ADDRESS + 8;    //马桶状态寄存器1

    public static final int REGISTER_TOILET_STATUS2 = REGISTER_TOILET_START_ADDRESS + 9;    //马桶状态寄存器2

    public static final int REGISTER_TOILET_STATUS3 = REGISTER_TOILET_START_ADDRESS + 10;    //马桶状态寄存器3

    public static final int REGISTER_TOILET_STATUS4 = REGISTER_TOILET_START_ADDRESS + 11;    //马桶状态寄存器4

    public static final int REGISTER_TOILET_TEMP_SEAT = REGISTER_TOILET_START_ADDRESS + 12;    //马桶坐垫温度

    public static final int REGISTER_TOILET_TEMP_WATERFLOW = REGISTER_TOILET_START_ADDRESS + 13;    //马桶水流量 L/min

    public static final int REGISTER_TOILET_TEMP_WIND = REGISTER_TOILET_START_ADDRESS + 14;    //马桶出风温度

    public static final int REGISTER_TOILET_TEMP_WATER_IN = REGISTER_TOILET_START_ADDRESS + 15;    //马桶进水口温度

    public static final int REGISTER_TOILET_TEMP_WATER_OUT1 = REGISTER_TOILET_START_ADDRESS + 16;    //马桶出水口温度1

    public static final int REGISTER_TOILET_TEMP_WATER_OUT2 = REGISTER_TOILET_START_ADDRESS + 17;//马桶出水口温度2

    public static final int REGISTER_TOILET_TEMP_OUTSIDE = REGISTER_TOILET_START_ADDRESS + 18;//马桶环境温度

    public static final int REGISTER_TOILET_TEMP_HUMIDITY = REGISTER_TOILET_START_ADDRESS + 19;    //马桶环境温度

    public static final int REGISTER_TOILET_RUN_CONFIG1 = REGISTER_TOILET_START_ADDRESS + 20;    //马桶实时配置1

    public static final int REGISTER_TOILET_RUN_CONFIG2 = REGISTER_TOILET_START_ADDRESS + 21;    //马桶实时配置2

    public static final int REGISTER_TOILET_RUN_CONFIG3 = REGISTER_TOILET_START_ADDRESS + 22;    //马桶实时配置3

    public static final int REGISTER_TOILET_AUX1 = REGISTER_TOILET_START_ADDRESS + 23;        //马桶备用1

    public static final int REGISTER_TOILET_AUX2 = REGISTER_TOILET_START_ADDRESS + 24;        //马桶备用2

    public static final int REGISTER_TOILET_AUX3 = REGISTER_TOILET_START_ADDRESS + 25;        //马桶备用3

    public static final int REGISTER_TOILET_YEAR_MONTH = REGISTER_TOILET_START_ADDRESS + 26;        //马桶，出厂年月

    public static final int REGISTER_TOILET_TYPE = REGISTER_TOILET_START_ADDRESS + 27;        //马桶，出厂日，产品类型

    public static final int REGISTER_TOILET_SERIAL = REGISTER_TOILET_START_ADDRESS + 28;    //马桶唯一序列号

    public static final int REGISTER_TOILET_VERSION = REGISTER_TOILET_START_ADDRESS + 29;        //马桶程序版本

    public static final int REGISTER_TOILET_AUX4 = REGISTER_TOILET_START_ADDRESS + 30;    //马桶备用4

    //马桶小屏幕命令定义
    public static final Integer pics[][] = { //起始,结束,帧数
            {0, 29, 30},// 一般检测结果

            {30, 52, 23},//七夕

            {53, 97, 45},//万圣节

            {98, 177, 80},//下雪穿很厚哈气

            {178, 232, 55},//世界无烟日

            {233, 257, 25},//中秋节

            {258, 277, 20},//体重

            {278, 327, 50},//做鬼脸

            {328, 368, 41},//健康

            {369, 398, 30},//儿童节

            {399, 458, 60},//元宵节

            {459, 488, 30},//元旦快乐

            {489, 508, 20},//光棍节

            {509, 538, 30},//全国爱牙日

            {539, 578, 40},//冬至

            {579, 598, 20},//冲水

            {599, 618, 20},//冲洗

            {619, 638, 20},//劳动节

            {639, 689, 51},//喝茶

            {690, 709, 20},//国庆节

            {710, 741, 32},//国际家庭日

            {742, 761, 20},//圣诞节

            {762, 791, 30},//复活节

            {792, 816, 25},//夏天戴墨镜喝冷饮

            {817, 860, 44},//大风抓住墙壁

            {861, 885, 25},//妇女节

            {886, 911, 26},//小年

            {912, 941, 30},//小普生日

            {942, 990, 49},//小普表情2跳舞

            {991, 1163, 173},//小普跑到平板

            {1164, 1183, 20},//尿检

            {1184, 1203, 20},//平安夜

            {1204, 1223, 20},//建军节

            {1224, 1295, 72},//开心

            {1296, 1307, 12},//很久没检查

            {1308, 1447, 140},//心电心率

            {1448, 1487, 40},//怀孕

            {1488, 1542, 55},//情人节

            {1543, 1580, 38},//愚人节

            {1581, 1600, 20},//感恩节

            {1601, 1708, 108},//打哈欠

            {1709, 1728, 20},//抗战胜利日

            {1729, 1758, 30},//摘花瓣

            {1759, 1778, 20},//放鞭炮

            {1779, 1808, 30},//教师节

            {1809, 1850, 42},//无聊

            {1851, 1880, 30},//春节小普接元宝

            {1881, 1935, 55},//植树节

            {1936, 1975, 40},//母亲节

            {1976, 1995, 20},//消毒

            {1996, 2025, 30},//清明节

            {2026, 2045, 20},//烘干

            {2046, 2065, 20},//烟熏

            {2066, 2095, 30},//爱眼日

            {2096, 2136, 41},//父亲节

            {2137, 2166, 30},//生闷气

            {2167, 2206, 40},//用户生日快乐

            {2207, 2254, 48},//看书

            {2255, 2284, 30},//睡觉

            {2285, 2324, 40},//空气温度湿度水量变化

            {2325, 2374, 50},//立冬

            {2375, 2439, 65},//立夏

            {2440, 2459, 20},//立春

            {2460, 2479, 20},//立秋

            {2480, 2509, 30},//端午节

            {2510, 2529, 20},//腊八节

            {2530, 2573, 44},//自拍

            {2574, 2598, 25},//记者日

            {2599, 2618, 20},//超标

            {2619, 2638, 20},//重阳节

            {2639, 2718, 80},//雨天打伞跳舞

            {2719, 2780, 62},//雾霾

            {2781, 2805, 25}//青年节
    };

}
