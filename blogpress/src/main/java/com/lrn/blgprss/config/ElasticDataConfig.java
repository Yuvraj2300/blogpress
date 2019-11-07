package com.lrn.blgprss.config;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lrn.blgprss.repo")
@ComponentScan(basePackages = { "com.lrn.blgprss.config" })
public class ElasticDataConfig {
	
	@Value("#{new String('${elasticsearch.host}')}")
	private String esHost;/*="localhost"*/ /*="blogpress-8020150717.ap-southeast-2.bonsaisearch.net";*/

	//@Value("#{new Double('${item.priceFactor}')}")
	@Value("#{new Integer('${elasticsearch.port}')}")
	private int esPort;

	@Value("#{new String('${elasticsearch.clustername}')}")
	private String esCluster;

	@Bean
	public Client client() throws Exception {
		TransportClientFactoryBean trsnsprtClientFactory	=	
				new	TransportClientFactoryBean();
		trsnsprtClientFactory.setClusterName(esCluster);
		trsnsprtClientFactory.afterPropertiesSet();
		
		try {
			return trsnsprtClientFactory.getObject().addTransportAddress(
					new	 TransportAddress(InetAddress.getByName(esHost.trim()),esPort));	
		/*	return trsnsprtClientFactory.getObject().addTransportAddress(
					new	 TransportAddress(new InetSocketAddress("blogpress-8020150717.ap-southeast-2.bonsaisearch.net", 80)));*/
		}catch(Exception	e) {
			System.out.println("This is from catch for client bean: "+e.getMessage());
			return trsnsprtClientFactory.getObject().addTransportAddress(
					new	 TransportAddress(InetAddress.getByName(esHost.trim()),esPort));
		}
		

	}
	
	  @Bean
	    public ElasticsearchTemplate elasticsearchTemplate() throws Exception {
	        return new ElasticsearchTemplate(client());
	    }
}