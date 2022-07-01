package com.games.tap.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ElasticsearchConfig {
    @Bean
    public ElasticsearchClient elasticsearchClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","123456"));
        RestClient restClient= RestClient.builder(new HttpHost(
                "123.207.30.101",9200,"http"))
                .setHttpClientConfigCallback(builder -> builder.setDefaultCredentialsProvider(credentialsProvider)).build();
        ElasticsearchTransport transport=new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

//    @Bean
//    public ElasticsearchTemplate elasticsearchTemplate(){
//        return new ElasticsearchTemplate(elasticsearchClient(),
//                elasticsearchEntityMapper(elasticsearchMappingContext(elasticsearchCustomConversions()),elasticsearchCustomConversions()));
//    }

}
