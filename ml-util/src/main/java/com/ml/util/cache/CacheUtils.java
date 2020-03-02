package com.ml.util.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 缓存统一契约
 * 
 * 描述
 * 1、考虑京东内部缓存介质演进，定义统一的接口
 * 2、缓存介质演进memcached->redis->jimdb
 * </pre>
 * 
 * @author mecarlen 2015年10月29日 下午5:06:35
 */
public interface CacheUtils {
	/**
	 * <pre>
	 * 将String值value关联到key
	 * 
	 * 描述
	 * 1、键值最大长度依赖缓存介质（memcached->redis->jimdb）
	 * 2、缓存时长为介质默认缓存时长
	 * 3、捕捉所有异常，异常返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param value String 缓存内容
	 * @return boolean
	 */
	boolean set(String key, String value);

	/**
	 * <pre>
	 * 将Object值value关联到key
	 * 
	 * 描述
	 * 1、键值最大长度依赖缓存介质（memcached->redis->jimdb）
	 * 2、缓存时长为介质默认缓存时长
	 * 3、捕捉所有异常，结果以返回值为准，异常返回false
	 * 
	 * </pre>
	 * 
	 * @param key   String
	 * @param value Object 缓存内容
	 * @return boolean
	 */
	boolean set(String key, Object value);

	/**
	 * <pre>
	 * 当key存在时,仍将String值value关联到key，并指定缓存时长
	 * 
	 * 描述
	 * 1、如果 key 已经存在， SETEX 命令将覆写旧值。
	 * 2、这个命令类似于以下两个命令：
	 * 			SET key value
	 * 			EXPIRE key seconds  # 设置生存时间
	 * 		   不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
	 * 3、捕捉所有异常，结果以返回值为准，异常返回false
	 * </pre>
	 * 
	 * @param key     String
	 * @param value   String
	 * @param seconds long 缓存时长,单位:秒
	 * @return boolean
	 */
	boolean setEx(String key, String value, long seconds);

	/**
	 * <pre>
	 * 当key存在时,仍将Object值value关联到key，并指定缓存时长
	 * 
	 * 描述
	 * 1、如果 key 已经存在， SETEX 命令将覆写旧值。
	 * 2、这个命令类似于以下两个命令：
	 * 			SET key value
	 * 			EXPIRE key seconds  # 设置生存时间
	 * 		   不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
	 * 3、捕捉所有异常，异常返回false
	 * </pre>
	 * 
	 * @param key     String
	 * @param value   Object
	 * @param seconds long 缓存时长,单位:秒
	 * @return boolean
	 */
	boolean setEx(String key, Object value, long seconds);

	/**
	 * <pre>
	 * 当key不存时,将String值value关联到key
	 * 
	 * 描述
	 * 1、若给定的 key 已经存在，则 SETNX 不做任何动作
	 * 2、捕捉所有异常，异常返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param value String
	 * @return boolean
	 */
	boolean setNx(String key, String value);

	/**
	 * <pre>
	 * 当key不存时,将Object值value关联到key
	 * 
	 * 描述
	 * 1、若给定的 key 已经存在，则 SETNX 不做任何动作
	 * 2、捕捉所有异常，结果以返回值为准，异常返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param value Object
	 * @return boolean
	 */
	boolean setNx(String key, Object value);

	/**
	 * <pre>
	 * 向缓存的哈希表中添加value值,如果缓存的哈希表中的存在field域,则直接覆盖
	 * 
	 * 描述
	 * 1、如果哈希表中field不存在，直接设置新值
	 * 2、如果哈希表中field已经存在,则旧值被新值覆盖
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String 域名
	 * @param value String
	 * @return boolean
	 */
	boolean hSet(String key, String field, String value);

	/**
	 * <pre>
	 * 向缓存的哈希表中添加value值,如果缓存的哈希表中的存在field域,则直接覆盖
	 * 
	 * 描述
	 * 1、如果哈希表中field不存在，直接设置新值
	 * 2、如果哈希表中field已经存在,则旧值被新值覆盖
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String 域名
	 * @param value Object
	 * @return boolean
	 */
	boolean hSet(String key, String field, Object value);

	/**
	 * <pre>
	 * 向缓存的哈希表中添加value值,如果缓存的哈希表中存在field域,则返回失败
	 * 
	 * 描述
	 * 1、如果哈希表中不存在field值，value值设置成功
	 * 2、如果哈希表中存在field值,则不执行操作，返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @param value String 目标值
	 * @return boolean
	 */
	boolean hSetNx(String key, String field, String value);

	/**
	 * <pre>
	 * 向缓存的哈希表中添加value值,如果缓存的哈希表中存在field域,则返回失败
	 * 
	 * 描述
	 * 1、如果哈希表中不存在field值，value值设置成功
	 * 2、如果哈希表中存在field值,则不执行操作，返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @param value Object
	 * @return boolean
	 */
	boolean hSetNx(String key, String field, Object value);

	/**
	 * <pre>
	 * 同时设置一个或多个 key-value 对
	 * 如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作。
	 * MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key 没有改变的情况，不可能发生。
	 * 
	 * 描述
	 * 	总是返回 OK (因为 MSET 不可能失败)
	 * </pre>
	 * 
	 * @param values Map<String,String>
	 * @return boolean
	 */
	boolean mSet(Map<String, String> values);

	/**
	 * <pre>
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
	 * </pre>
	 * 
	 * @param key    String
	 * @param values Map<String,String>
	 * @return boolean
	 */
	boolean hmSet(String key, Map<String, String> values);

	/**
	 * <pre>
	 * 根据key追加缓存String值
	 * 
	 * 描述
	 * 1、如果key存在并且是一个字符串，append命令将value追加到key原来的值的末尾
	 * 2、如果key不存在，类似等于set(key,value)方法
	 * 3、出现异常返回-1
	 * </pre>
	 * 
	 * @param key   String
	 * @param value String
	 * @return long
	 */
	long append(String key, String value);

	/**
	 * <pre>
	 * 将一个或多个 values元素加入到集合 key 当中，已经存在于集合的value元素将被忽略。
	 * 
	 * 描述
	 * 1、假如 key 不存在，则创建一个只包含 value 元素作成员的集合。
	 * 2、当 key 不是集合类型时，返回null。
	 * </pre>
	 * 
	 * @param key    String
	 * @param values String...
	 * @return Long
	 */
	Long sAdd(String key, String... values);

	/**
	 * <pre>
	 * 将一个 member 元素及其 score 值加入到有序集 key 当中
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 * score 值可以是整数值或双精度浮点数。
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 * 
	 * </pre>
	 * 
	 * @param key   String
	 * @param value String
	 * @param score double
	 * @return boolean
	 */
	boolean zAdd(String key, String value, double score);

	/**
	 * <pre>
	 * 将一个 member 元素及其 score 值加入到有序集 key 当中
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 * score 值可以是整数值或双精度浮点数。
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 * 
	 * </pre>
	 * 
	 * @param key   String
	 * @param value Object
	 * @param score double
	 * @return boolean
	 */
	boolean zAdd(String key, Object value, double score);

	/**
	 * <pre>
	 * 将一个或多个值values插入到列表 key的表头
	 * 
	 * 描述
	 * 1、如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，
	 *   列表的值将是 c b a ， 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
	 * 2、如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * 3、当键值key存在但不是列表类型返回null
	 * </pre>
	 * 
	 * @param key    String
	 * @param values String...
	 * @return Long
	 */
	Long lPush(String key, String... values);

	/**
	 * <pre>
	 * 将一个或多个值values插入到列表 key的表头
	 * 
	 * 描述
	 * 1、如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，
	 *   列表的值将是 c b a ， 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
	 * 2、如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * 3、当键值key存在但不是列表类型返回null
	 * </pre>
	 * 
	 * @param key    String
	 * @param values Object...
	 * @return Long
	 */
	Long lPush(String key, Object... values);

	/**
	 * <pre>
	 * 将一个或多个值values插入到列表 key的尾部
	 * 
	 * 描述
	 * 	1、返回列表长度
	 * </pre>
	 * 
	 * @param key    String
	 * @param values String...
	 * @return Long
	 */
	Long rPush(String key, String... values);

	/**
	 * <pre>
	 * 将一个或多个值values插入到列表 key的尾部
	 * 
	 * 描述
	 * 	1、返回列表长度
	 * </pre>
	 * 
	 * @param key    String
	 * @param values Object...
	 * @return Long
	 */
	Long rPush(String key, Object... values);

	/**
	 * <pre>
	 * 返回key所关联的String值
	 * 
	 * 描述
	 * 1、如果key不存在那么返回特殊值null 
	 * 2、假如key储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
	 * </pre>
	 * 
	 * @param key String
	 * @return String
	 */
	String get(String key);

	/**
	 * <pre>
	 * 取已明确类型的Object缓存值
	 * 
	 * 描述
	 * 	默认返回null
	 * 
	 * </pre>
	 * 
	 * @param key   String
	 * @param clazz Class<T> 具体类
	 * @return T 具体类实例
	 */
	<T> T get(String key, Class<T> clazz);

	/**
	 * <pre>
	 * 返回哈希表 key中给定域 field的值
	 * 
	 * 描述
	 * 1、当给定域不存在或是给定 key不存在时，返回 null
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @return String
	 */
	String hGet(String key, String field);

	/**
	 * <pre>
	 * 返回哈希表 key中给定域 field的值
	 * 
	 * 描述
	 * 1、当给定域不存在或是给定 key不存在时，返回 null
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @param clazz Class<T>
	 * @return T
	 */
	<T> T hGet(String key, String field, Class<T> clazz);

	/**
	 * <pre>
	 * 返回哈希表元素个数
	 * 
	 * 描述
	 *   不存在时返回0
	 * </pre>
	 * @param key String
	 * @return long
	 * */
	long hLen(String key);
	
	/**
	 * <pre>
	 * 返回哈希表 key中所有的域和值。
	 * 
	 * 描述
	 * 1、当 key不存在返回空Map
	 * 2、出现异常返回空Map
	 * </pre>
	 * 
	 * @param key String
	 * @return Map<String,String>
	 * 
	 */
	Map<String, String> hGetAll(String key);

	/**
	 * <pre>
	 * 返回哈希表 key中所有的域和值。
	 * 
	 * 描述
	 * 1、当 key不存在返回空Map
	 * 2、出现异常返回空Map
	 * </pre>
	 * 
	 * @param key   String
	 * @param clazz Class<T>
	 * @return Map<String,T>
	 * 
	 */
	<T> Map<String, T> hGetAll(String key, Class<T> clazz);

	/**
	 * <pre>
	 * 批量取缓存
	 * 
	 * 描述
	 * 	仅支持String类型
	 * </pre>
	 * 
	 * @param keys String... 缓存key数组
	 * @return List<String>
	 */
	List<String> mGet(String... keys);

	/**
	 * <pre>
	 * 返回集合key中的所有成员
	 * 
	 * 描述
	 * 1、键值key不存在返回空集合
	 * 2、键值key存在但成员为空返回空集合
	 * 3、出现异常返回空集合
	 * </pre>
	 * 
	 * @param key String
	 * @return Set<String>
	 */
	Set<String> sMembers(String key);

	/**
	 * <pre>
	 * 取缓存的集合成员数
	 * 
	 * 描述
	 * 1、键值key不存在返回0
	 * 2、键值key存在但成员为空返回0
	 * 3、出现异常返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return Long
	 */
	Long sCard(String key);

	/**
	 * <pre>
	 * 取缓存的列表长度
	 * 
	 * 描述
	 * 1、如果 key不存在，则 key被解释为一个空列表，返回 0 .
	 * 2、如果 key不是列表类型，返回-1
	 * 3、出现异常返回 null
	 * </pre>
	 * 
	 * @param key String
	 * @return Long
	 */
	Long lLen(String key);

	/**
	 * <pre>
	 * 检查key是否已存在
	 * 
	 * </pre>
	 * 
	 * @param key String
	 * @return boolean
	 */
	boolean exists(String key);

	/**
	 * <pre>
	 * 判断value元素是否集合key的成员
	 * 
	 * </pre>
	 * 
	 * @param key   String
	 * @param value String
	 * @return boolean
	 */
	boolean sIsMember(String key, String value);

	/**
	 * <pre>
	 * 将key中储存的数字值加一
	 * 
	 * 描述
	 * 1、如果key不存在，则key的数据将被设置为0，然后加1
	 * 2、如果key存在但不是数字类型，返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return Long
	 */
	Long incr(String key);

	/**
	 * <pre>
	 * 将key中储存的数字值加一
	 * 
	 * 描述
	 * 1、如果key不存在，则key的数据将被设置为0，然后加1
	 * 2、如果key存在但不是数字类型，返回null
	 * </pre>
	 * 
	 * @param key  String
	 * @param step long步长
	 * @return Long
	 */
	Long incrBy(String key, long step);

	/**
	 * <pre>
	 * 为哈希表 key中的域 field的值加上步长step
	 * 
	 * 描述
	 * 1、增量也可以为负数，相当于对给定域进行减法操作。
	 * 2、如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 * 3、如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 4、对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 5、本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 * 6、异常返回null
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @param step  long 步长
	 * @return Long
	 */
	Long hIncrBy(String key, String field, long step);

	/**
	 * <pre>
	 * 将key中储存的数字值减一
	 * 
	 * 描述
	 * 1、如果key不存在，则key的数据将被设置为0，然后减1
	 * 2、如果key存在但不是数字类型，返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return Long
	 */
	Long decr(String key);

	/**
	 * <pre>
	 * 按步长将key中储存的数字值减一
	 * 
	 * 描述
	 * 1、如果key不存在，则key的数据将被设置为0，然后减1
	 * 2、如果key存在但不是数字类型，返回null
	 * </pre>
	 * 
	 * @param key  String
	 * @param step long 步长
	 * @return Long
	 */
	Long decrBy(String key, long step);

	/**
	 * <pre>
	 * 剔除一个或多个缓存的String值
	 * 
	 * 描述
	 * 1、如果不存在的集合values元素将被忽略
	 * 2、出现异常返回 null
	 * </pre>
	 * 
	 * @param key    String
	 * @param values String...
	 * @return Long 移除成功数量
	 */
	Long sRem(String key, String... values);

	/**
	 * <pre>
	 * 剔除并返回列表的头元素(String值)
	 * 
	 * 描述
	 * 1、当 key不存在时，返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return String
	 */
	String lPop(String key);

	/**
	 * <pre>
	 * 剔除并返回列表的头元素
	 * 
	 * 描述
	 * 1、当 key不存在时，返回null
	 * </pre>
	 * 
	 * @param key   String
	 * @param clazz Class<T>
	 * @return T <T>
	 */
	<T> T lPop(String key, Class<T> clazz);

	/**
	 * <pre>
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 
	 * <b>注意LRANGE命令和编程语言区间函数的区别</b>
	 * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，
	 * 这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
	 * 
	 * <b>超出范围的下标</b>
	 * 超出范围的下标值不会引起错误。
	 * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
	 * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end。
	 * 
	 * <b>返回值</b>
	 * 一个列表，包含指定区间内的元素。
	 * </pre>
	 * 
	 * @param key   String
	 * @param start long
	 * @param end   long
	 * @return List<String>
	 */
	List<String> lRange(String key, long start, long end);

	/**
	 * <pre>
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 
	 * <b>注意LRANGE命令和编程语言区间函数的区别</b>
	 * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，
	 * 这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
	 * 
	 * <b>超出范围的下标</b>
	 * 超出范围的下标值不会引起错误。
	 * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
	 * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end。
	 * 
	 * <b>返回值</b>
	 * 一个列表，包含指定区间内的元素。
	 * </pre>
	 * 
	 * @param key   String
	 * @param start long
	 * @param end   long
	 * @param clazz Class<T>
	 * @return List<T>
	 */
	<T> List<T> lRange(String key, long start, long end, Class<T> clazz);

	/**
	 * <pre>
	 * 剔除并返回列表的尾元素(String值)
	 * 
	 * 描述
	 * 1、当 key不存在时，返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return String
	 */
	String rPop(String key);

	/**
	 * <pre>
	 * 剔除并返回列表的尾元素
	 * 
	 * 描述
	 * 1、当 key不存在时，返回null
	 * </pre>
	 * 
	 * @param key   String
	 * @param clazz Class<T>
	 * @return T <T>
	 */
	<T> T rPop(String key, Class<T> clazz);

	/**
	 * <pre>
	 * 返回有序集 key 的基数。
	 * 
	 * 当 key 存在且是有序集类型时，返回有序集的基数。 
	 * 当 key 不存在时，返回 0
	 * 当出现异常时返回-1
	 * </pre>
	 * 
	 * @param key String
	 * @return long
	 */
	long zCard(String key);

	/**
	 * <pre>
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 有序集成员按 score 值递增(从小到大)次序排列。
	 * 
	 * </pre>
	 * 
	 * @param key      String
	 * @param minScore double
	 * @param maxScore double
	 * @return Set<String>
	 */
	Set<String> zRangeByScore(String key, double minScore, double maxScore);

	/**
	 * <pre>
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
	 * 
	 * </pre>
	 * 
	 * @param key      String
	 * @param minScore double
	 * @param maxScore double
	 * @param clazz    Class<T>
	 * @return Set<T>
	 */
	<T> Set<T> zRangeByScore(String key, double minScore, double maxScore, Class<T> clazz);

	/**
	 * <pre>
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。
	 * 
	 * 有序集成员按 score 值递减(从大到小)的次序排列。
	 * 
	 * </pre>
	 * 
	 * @param key      String
	 * @param minScore double
	 * @param maxScore double
	 * @return Set<String>
	 */
	Set<String> zRevRangeByScore(String key, double minScore, double maxScore);

	/**
	 * <pre>
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员
	 * 
	 * </pre>
	 * 
	 * @param key      String
	 * @param minScore double
	 * @param maxScore double
	 * @param clazz    Class<T>
	 * @return Set<T>
	 */
	<T> Set<T> zRevRangeByScore(String key, double minScore, double maxScore, Class<T> clazz);

	/**
	 * 移除在 minScore 和 maxScore 之间的成员.
	 *
	 * @param key      String
	 * @param minScore double
	 * @param maxScore double
	 *
	 * @return long
	 */
	Long zRemRangeByScore(String key, double minScore, double maxScore);

	/**
	 * <pre>
	 * 删除哈希表 key 中的一个或多个指定域
	 * 
	 * 描述
	 *  1)出现异常返回 null
	 *  2)删除成功返回1
	 *  3)删除不存在的KEY返回0
	 * </pre>
	 * 
	 * @param key    String
	 * @param fields String...
	 * @return Long
	 */
	Long hDel(String key, String... fields);

	/**
	 * <pre>
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除
	 * 
	 * 描述
	 * 1、在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)
	 *  生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，
	 *  如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
	 *  比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
	 *  另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
	 *  RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，
	 *  这时旧的 another_key (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
	 *  使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
	 * 2、更新生存时间
	 *  可以对一个已经带有生存时间的 key 执行 EXPIRE 命令，新指定的生存时间会取代旧的生存时间。
	 * 3、过期时间的精确度
	 *  在 Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内 —— 也即是，就算 key 已经过期，但它还是可能在过期之后一秒钟之内被访问到，
	 *  而在新的 Redis 2.6 版本中，延迟被降低到 1 毫秒之内。
	 * </pre>
	 * 
	 * @param key     String
	 * @param seconds long
	 * @return boolean
	 */
	boolean expire(String key, long seconds);

	/**
	 * <pre>
	 * 为给定 key设置具体失效时间，当 key过期时(生存时间为 0 )，它会被自动删除
	 * 
	 * 描述
	 * 
	 * </pre>
	 * 
	 * @param key  String
	 * @param time Date
	 * @return boolean
	 */
	boolean expireAt(String key, Date time);

	/**
	 * <pre>
	 * 删除指定缓存
	 * 
	 * </pre>
	 * 
	 * @param key String
	 * @return boolean
	 */
	boolean del(String key);

	/**
	 * <pre>
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 
	 * 描述
	 * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，
	 * 以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
	 * 当 key 不是列表类型时，返回一个错误
	 * </pre>
	 * 
	 * @param key   String
	 * @param start long
	 * @param end   long
	 * @return boolean
	 */
	boolean lTrim(String key, long start, long end);

	/**
	 * 获取指定位置的对象
	 * 
	 * @param key   String
	 * @param index long
	 * @return String
	 */
	String lIndex(String key, final long index);

	/**
	 * <pre>
	 * 查看哈希表 key 中，给定域 field 是否存在
	 * 描述
	 * 1)存在返回true，否则返回false
	 * 2)异常返回false
	 * </pre>
	 * 
	 * @param key   String
	 * @param field String
	 * @return boolean
	 */
	boolean hExists(String key, String field);

	/**
	 * <pre>
	 * 缓存过期时间 单位：毫秒
	 * 
	 * 描述
	 * 1)当 key 不存在时，返回 -2
	 * 2)当 key 存在但没有设置剩余生存时间时，返回 -1
	 * 3)否则，以毫秒为单位，返回 key 的剩余生存时间
	 * 4)异常返回null
	 * </pre>
	 * 
	 * @param key String
	 * @return Long
	 */
	Long pTtl(String key);

	/**
	 * 获取所有 keys.
	 *
	 * @param pattern String
	 *
	 * @return Set<String>
	 */
	Set<String> keys(String pattern);
}
