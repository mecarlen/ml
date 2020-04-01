MSF(rpc)技术点
其实 RPC 就是把拦截到的方法参数，转成可以在网络中传输的二进制，并保证在服务提供方能正确地还原出语义，最终实现像调用本地一样地调用远程的目的。
1、协议

2、序列化
protocolbuff(GRPC) hessian json avro(kafka) kryo thrift messagepack resp(redis)
3、IO模型
IO多路复用（select poll epoll）
零拷贝技术mmap+write sendfile
netty零拷贝技术
1)Netty 提供了 CompositeByteBuf 类，它可以将多个 ByteBuf 合并为一个逻辑上的  ByteBuf，避免了各个 ByteBuf 之间的拷贝。
2)ByteBuf 支持 slice 操作，因此可以将 ByteBuf 分解为多个共享同一个存储区域的 ByteBuf，避免了内存的拷贝。
3)通过 wrap 操作，我们可以将 byte[] 数组、ByteBuf、ByteBuffer  等包装成一个 Netty ByteBuf 对象, 进而避免拷贝操作。
Netty 框架中很多内部的 ChannelHandler 实现类，都是通过 CompositeByteBuf、slice、wrap 操作来处理 TCP 传输中的拆包与粘包问题的。
Netty  的  ByteBuffer 可以采用 Direct Buffers，使用堆外直接内存进行 Socket 的读写操作，最终的效果与我刚才讲解的虚拟内存所实现的效果是一样的。Netty  还提供  FileRegion  中包装  NIO  的  FileChannel.transferTo()  方法实现了零拷贝，这与 Linux  中的  sendfile  方式在原理上也是一样的。
4、动态代理
JDK原生的是实现InvocationHandle接口，然后底层通过JDK反射实现
Javassist
ByteBuff Spring和Jackson底层用此完成代理