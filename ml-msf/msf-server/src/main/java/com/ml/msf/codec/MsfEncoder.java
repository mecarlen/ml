package com.ml.msf.codec;

import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * <pre>
 * <b>MSF编码器</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月30日 下午3:53:58
 */
public class MsfEncoder extends MessageToMessageEncoder {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
		byte[] bytes = JSON.toJSONBytes(msg);
		byteBuf.writeInt(bytes.length);
		byteBuf.writeBytes(bytes);
		out.add(byteBuf);
	}

}
