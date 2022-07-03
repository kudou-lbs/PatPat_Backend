package com.games.tap.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.games.tap.domain.*;
import com.games.tap.util.Echo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchService {

    @Resource
    public ElasticsearchClient client;

    private final String PRE_TAG = "<span class='highlight_text'>";
    private final String POST_TAG = "</span>";

    public <T> List<Hit<T>> searchUser(String term, int page, int size,Class<T> tClass) throws IOException {
        SearchResponse<T> searchResponse = client.search(s -> s
                        .index("user")
                        .query(q -> q
//                                        .match(m->m.field("intro").query(term)
                                .bool(b -> b
                                        .should(h -> h.match(m -> m.field("intro").query(term)))
                                        .should(h -> h.matchPhrase(m -> m.field("nickname").query(term).slop(3)))
//                        .term(t -> t
//                                .field("nickname")
//                                .value(v -> v.stringValue(term)))
                        ))
                        .highlight(h -> h.fields("nickname", f -> f.preTags(PRE_TAG).postTags(POST_TAG)))
                        //分页查询
                        .from(page)
                        .size(size)
                //按age降序排序
//                .sort(f -> f.field(o -> o.field("age").order(SortOrder.Desc)))
                , tClass
        );
        searchResponse.hits().hits().forEach(h -> System.out.println(h.highlight().get("nickname")));
        return searchResponse.hits().hits();
    }

    public List<Hit<Forum>> searchForum(String term, int page, int size) throws IOException {
        SearchResponse<Forum> searchResponse = client.search(s -> s
                        .index("forum")
                        .query(q -> q.bool(b -> b
                                .should(h -> h.match(m -> m.field("intro").query(term)))
                                .should(h -> h.matchPhrase(m -> m.field("name").query(term).slop(1)))
                        ))
                        .highlight(h -> h.fields("name", f -> f.preTags(PRE_TAG).postTags(POST_TAG)))
                        //分页查询
                        .from(page)
                        .size(size)
                , Forum.class
        );
        searchResponse.hits().hits().forEach(h -> System.out.println(h.source()));
        return searchResponse.hits().hits();
    }

    public <T> List<Hit<T>> searchPost(String term, int page, int size, Class<T> tClass) throws IOException {
        SearchResponse<T> searchResponse = client.search(s -> s
                        .index("post")
                        .query(q -> q.bool(b -> b
                                .should(h -> h.match(m -> m.field("title").query(term)))
                                .should(h -> h.matchPhrase(m -> m.field("forumName").query(term).slop(2)))
                                .should(h -> h.fuzzy(f -> f.field("content").value(term).fuzziness("1")))
                        ))
                        .highlight(h -> h
                                .fields("forumName", f -> f.preTags(PRE_TAG).postTags(POST_TAG))
                                .fields("title", f -> f.preTags(PRE_TAG).postTags(POST_TAG))
                        )
                        //分页查询
                        .from(page)
                        .size(size)
                , tClass
        );
        searchResponse.hits().hits().forEach(h -> System.out.println(h.source()));
        return searchResponse.hits().hits();
    }

    public List<Hit<Game>> searchGame(String term, int page, int size) throws IOException {
        SearchResponse<Game> searchResponse = client.search(s -> s
                        .index("game")
                        .query(q -> q.bool(b -> b
                                .should(h -> h.matchPhrase(m -> m.field("name").query(term).slop(1)))
                                .should(h -> h.match(m -> m.field("types").query(term)))
                        ))
                        .highlight(h -> h.fields("name", f -> f.preTags(PRE_TAG).postTags(POST_TAG)))
                        //分页查询
                        .from(page)
                        .size(size)
                , Game.class
        );
        searchResponse.hits().hits().forEach(h -> System.out.println(h.source()));
        return searchResponse.hits().hits();
    }

    public <T> void searchAll(String idx, int page, int size, Class<T> tClass) throws IOException {
        SearchResponse<T> searchResponse = client.search(s -> s
                        .index(idx)
                        //查询name字段包含term的document(不使用分词器精确查找)
                        .query(q -> q.matchAll(m -> m
                        ))
                        //分页查询，从第0页开始查询10个document
                        .from(page)
                        .size(size)
                //按age降序排序
//                .sort(f -> f.field(o -> o.field("age").order(SortOrder.Desc)))
                , tClass
        );
        for (Hit<T> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
        }


    }

    public <T extends Item> void addItem(String idx, T item) throws IOException {
        IndexResponse indexResponse = client.index(i -> i
                .index(idx)
                .id(item.getId())
                .document(item));
    }

    public <T extends Item> void addItemList(String idx, List<T> tList) throws IOException {
        List<BulkOperation> bulkOperations = new ArrayList<>();
        tList.forEach(t ->
                bulkOperations.add(BulkOperation.of(b -> b
                        .index(i -> i
                                .id(t.getId()).document(t)
                        )
                ))
        );
        BulkResponse bulkResponse = client.bulk(b -> b.index(idx).operations(bulkOperations));
        bulkResponse.items().forEach(i -> System.out.println("i = " + i.result()));
        System.out.println("bulkResponse.errors() = " + bulkResponse.errors());
    }

    public <T extends Item> void updateItem(String idx, T item, Class<T> tClass) throws IOException {
        UpdateResponse<T> updateResponse = client.update(u -> u
                .index(idx)
                .id(item.getId())
                .doc(item), tClass);
    }

    public <T extends Item> void updateItemList(String idx, List<T> tList) throws IOException {
        List<BulkOperation> bulkOperations = new ArrayList<>();
        tList.forEach(t ->
                bulkOperations.add(BulkOperation.of(b -> b
                        .update(u->u.id(t.getId()).action(a->a.doc(t))
                        )
                ))
        );
        BulkResponse bulkResponse = client.bulk(b -> b.index(idx).operations(bulkOperations));
        bulkResponse.items().forEach(i -> System.out.println("i = " + i.result()));
        System.out.println("bulkResponse.errors() = " + bulkResponse.errors());
    }

    public <T> T getItem(String idx, String id, Class<T> tClass) throws IOException {
        GetResponse<T> getResponse = client.get(g -> g
                .index(idx)
                .id(id), tClass);
        assert getResponse.source() != null;
        return getResponse.source();
    }

    public void deleteItem(String idx, String id) throws IOException {
        DeleteResponse deleteResponse = client.delete(d -> d
                .index(idx)
                .id(id));
        log.info("删除Id:" + deleteResponse.id());
    }

    public void deleteItemList(String idx, List<Long> list) throws IOException {
        List<BulkOperation> bulkOperations = new ArrayList<>();
        list.forEach(i ->
                bulkOperations.add(BulkOperation.of(b ->
                        b.delete(c -> c.id(i.toString()))
                ))
        );
        BulkResponse bulkResponse = client.bulk(a -> a.index(idx).operations(bulkOperations));
        bulkResponse.items().forEach(a -> System.out.println("result = " + a.result()));
        System.out.println("bulkResponse.errors() = " + bulkResponse.errors());
    }

    public void createUserIndex() throws IOException {
        Map<String, Property> map = new HashMap<>();
        map.put("nickname", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word"))
                )));
        map.put("intro", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word").searchAnalyzer("ik_smart")
                ))
        ));
        CreateIndexResponse createIndexResponse = client.indices().create(c -> c
                .index("user").mappings(m -> m
                        .properties(map)
                ).aliases("pat", a -> a.isWriteIndex(true))
        );
        log.info(String.valueOf(createIndexResponse.acknowledged()));
    }

    public void createForumIndex() throws IOException {
        Map<String, Property> map = new HashMap<>();
        map.put("name", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word")
                ))
        ));
        map.put("intro", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word").searchAnalyzer("ik_smart")
                ))
        ));
        CreateIndexResponse createIndexResponse = client.indices().create(c -> c
                .index("forum").mappings(m -> m
                        .properties(map)
                ).aliases("pat", a -> a.isWriteIndex(false))
        );
        log.info(String.valueOf(createIndexResponse.acknowledged()));
    }

    public void createPostIndex() throws IOException {
        Map<String, Property> map = new HashMap<>();
        map.put("title", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word").searchAnalyzer("ik_smart")
                ))
        ));
        map.put("content", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word").searchAnalyzer("ik_smart")
                ))
        ));
        map.put("forumName", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word")
                ))
        ));
        CreateIndexResponse createIndexResponse = client.indices().create(c -> c
                .index("post").mappings(m -> m
                        .properties(map)
                ).aliases("pat", a -> a.isWriteIndex(false))
        );
        log.info(String.valueOf(createIndexResponse.acknowledged()));
    }

    public void createGameIndex()throws IOException{
 Map<String, Property> map = new HashMap<>();
        map.put("name", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word"))
                )));
        map.put("types", Property.of(p -> p
                .text(TextProperty.of(t -> t
                        .index(true).analyzer("ik_max_word").searchAnalyzer("ik_smart")
                ))
        ));
        CreateIndexResponse createIndexResponse = client.indices().create(c -> c
                .index("game").mappings(m -> m
                        .properties(map)
                ).aliases("pat", a -> a.isWriteIndex(false))
        );
        log.info(String.valueOf(createIndexResponse.acknowledged()));
    }

    public void deleteIndex(String idx) throws IOException {
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(d -> d
                .index(idx)
        );
        log.info(String.valueOf(deleteIndexResponse.acknowledged()));
    }

    public boolean isIndexNonexistence(String idx)throws IOException{
        BooleanResponse booleanResponse= client.indices().exists(e->e
                .index(idx));
        return !booleanResponse.value();
    }

    public void getAllIndex() throws IOException {
        IndicesResponse indicesResponse = client.cat().indices();
        indicesResponse.valueBody().forEach(info -> System.out.println(
                "info = " + info.health() + "\t" +
                        info.status() + "\t" + info.index() + "\t" +
                        info.uuid() + "\t" + info.pri() + "\t" +
                        info.rep() + "\t" + info.docsCount()));
    }

}
