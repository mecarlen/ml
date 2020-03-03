package com.ml.base.query;

import java.net.InetAddress;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;

/**
 * <pre>
 * ES认证客户端工厂类
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年6月24日 下午4:05:25
 */
public class XPackTransportClientFactoryBean extends TransportClientFactoryBean {
	// log
	final private static Logger XPACK_CLIENT_FACTORY_LOG = LoggerFactory
			.getLogger(XPackTransportClientFactoryBean.class);
	private String clusterName;
	private String clusterAddress;
	private String userName;
	private String password;
	private Boolean clientTransportSniff = Boolean.TRUE;
	private Boolean clientIgnoreClusterName = Boolean.FALSE;
	private Boolean securityTransportSslEnabled = Boolean.TRUE;
	private String tcpConnectTimeout = "40s";
	private String clientPingTimeout = "5s";
	private String clientNodesSamplerInterval = "5s";
	protected TransportClient client;

	@Override
	public void destroy() throws Exception {
		try {
			XPACK_CLIENT_FACTORY_LOG.info("Closing elasticSearch  client");
			if (client != null) {
				client.close();
			}
		} catch (final Exception e) {
			XPACK_CLIENT_FACTORY_LOG.error("Error closing ElasticSearch client: ", e);
		}
	}

	@Override
	public TransportClient getObject() throws Exception {
		return client;
	}
	
	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return super.isSingleton();
	}

	
	
	@Override
	public Class<TransportClient> getObjectType() {
		return super.getObjectType();
	}

	@Override
	public void buildClient() throws Exception {
		PreBuiltTransportClient preBuiltClient = null;
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(userName)) {
			// 认证
			preBuiltClient = new PreBuiltXPackTransportClient(settings(userName, password));
		} else {
			// 非认证
			preBuiltClient = new PreBuiltTransportClient(settings());
		}
		if (!"".equals(clusterAddress)) {
			for (String nodes : clusterAddress.split(",")) {
				String inetSocket[] = nodes.split(":");
				String address = inetSocket[0];
				Integer port = Integer.valueOf(inetSocket[1]);
				preBuiltClient
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), port));
			}
		}
		client = preBuiltClient;
	}

	private Settings settings() {
		return Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", clientTransportSniff)
				.put("client.transport.ignore_cluster_name", clientIgnoreClusterName)
				.put("client.transport.ping_timeout", clientPingTimeout)
				.put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval).build();
	}

	private Settings settings(String userName, String password) {
		return Settings.builder().put("cluster.name", clusterName)
				.put("client.transport.sniff", clientTransportSniff)
				.put("client.transport.ignore_cluster_name", clientIgnoreClusterName)
				.put("client.transport.ping_timeout", clientPingTimeout)
				.put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)
				.put("transport.tcp.connect_timeout", tcpConnectTimeout)
				.put("xpack.security.transport.ssl.enabled", securityTransportSslEnabled)
				.put("xpack.security.user", userName + ":" + password).build();
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public void setClusterAddress(String clusterAddress) {
		this.clusterAddress = clusterAddress;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setClientTransportSniff(Boolean clientTransportSniff) {
		this.clientTransportSniff = clientTransportSniff;
	}

	public void setClientIgnoreClusterName(Boolean clientIgnoreClusterName) {
		this.clientIgnoreClusterName = clientIgnoreClusterName;
	}

	public void setTcpConnectTimeout(String tcpConnectTimeout) {
		this.tcpConnectTimeout = tcpConnectTimeout;
	}

	public void setClientPingTimeout(String clientPingTimeout) {
		this.clientPingTimeout = clientPingTimeout;
	}

	public void setClientNodesSamplerInterval(String clientNodesSamplerInterval) {
		this.clientNodesSamplerInterval = clientNodesSamplerInterval;
	}

	public void setSecurityTransportSslEnabled(Boolean securityTransportSslEnabled) {
		this.securityTransportSslEnabled = securityTransportSslEnabled;
	}

}
