package com.games.tap.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.games.tap.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class SearchService {

    @Autowired
    public ElasticsearchClient client;


    public void searchUser()throws IOException{
            SearchResponse<User> searchResponse= client.search(s -> s
                    .index("user")
                    //查询name字段包含hello的document(不使用分词器精确查找)
                    .query(q -> q
                            .term(t -> t
                                    .field("name")
                                    .value(v -> v.stringValue("hello"))
                            ))
                    //分页查询，从第0页开始查询3个document
                    .from(0)
                    .size(3)
                    //按age降序排序
                    .sort(f->f.field(o->o.field("age").order(SortOrder.Desc))),User.class
            );
    }

    public void addUser(User user)throws IOException{
        IndexResponse indexResponse=client.index(i->i
                .index("user")
                .id(user.getUId().toString())
                .document(user));
    }

    public void updateUser(User user)throws IOException{
        UpdateResponse<User> updateResponse=client.update(u->u
                .index("user")
                .id(user.getUId().toString())
                .doc(user),User.class);
    }

    public void getUser() throws IOException {
        GetResponse<User> getResponse = client.get(g -> g
                        .index("user")
                        .id("1")
                , User.class
        );
        assert getResponse.source() != null;
        log.info(getResponse.source().toString());
    }

    public void deleteUser() throws IOException {
        DeleteResponse deleteResponse = client.delete(d -> d
                .index("user")
                .id("1")
        );
        log.info(deleteResponse.id());
    }

}
