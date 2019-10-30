package com.lrn.blgprss.config;

import java.net.InetAddress;

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
	
	//@Value("$elasticsearch.host")
	private String esHost="https://9ks8cttq1z:yvsbl1k6cu@blogpress-8020150717.ap-southeast-2.bonsaisearch.net:443";

	//@Value("#{new Double('${item.priceFactor}')}")
	@Value("#{new Integer('${elasticsearch.port}')}")
	private int esPort;

	@Value("$elasticsearh.cluster")
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
		}catch(Exception	e) {
			System.out.println("This is from catch for client bean: "+e.getMessage());
			return null;
		}
		

	}
	
	  @Bean
	    public ElasticsearchTemplate elasticsearchTemplate() throws Exception {
	        return new ElasticsearchTemplate(client());
	    }
}