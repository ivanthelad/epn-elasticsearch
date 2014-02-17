package org.jboss.rtgov.elasticsearch.repo.elastic.impl.api;

/**
 * Created with IntelliJ IDEA.
 * User: imk@redhat.com
 * Date: 08/02/14
 * Time: 00:06
 */

import java.util.Date;

import java.util.HashMap;

import java.util.Map;


import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;

import org.elasticsearch.action.index.IndexResponse;

import org.elasticsearch.action.search.SearchResponse;

import org.elasticsearch.action.search.SearchType;

import org.elasticsearch.client.Client;

import org.elasticsearch.client.transport.TransportClient;

import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.elasticsearch.index.query.QueryBuilder;

import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.sort.SortOrder;


/**
 * Hello world!
 */

public class App

{

    private static String index = "db";

    private static String type = "table";

    private static String foo = "foo";

    private static String bar = "bar";

    public static void main(String[] args)

    {

        Client client = null;

        try {

            client = new TransportClient()

                    .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

            //Testdaten werden erstellt

            createData(client);

            //Alle Datensätze anzeigen

            showAllData(client);

            //Nur unsortierte Datensätze für User foo anzeigen

            showDataByUser(client, foo);

            //Sortierte Datensätze für User bar anzeigen

            showDataByUserAndSortByDatum(client, bar);

            //Testdaten wieder löschen

            deleteIndex(client);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        } finally {

            client.close();

        }

    }

    private static void showDataByUserAndSortByDatum(Client client, String searchUser) {

        System.out.println("Sortierte Datensätze für User bar anzeigen");

        QueryBuilder qb = QueryBuilders.matchQuery("user", searchUser);


        SearchResponse response = client.prepareSearch(index)

                .setTypes(type)

                .setSearchType(SearchType.QUERY_AND_FETCH)

                .setQuery(qb)

                .addField("message")

                .addField("user")

                .addSort("datum", SortOrder.ASC)

                .setSize(100).execute().actionGet();

        printResults(response);

        System.out.println("--------------------------------");

    }

    private static void showDataByUser(Client client, String searchUser) {

        System.out.println("Nur unsortierte Datensätze für User foo anzeigen");

        QueryBuilder qb = QueryBuilders.matchQuery("user", searchUser);


        SearchResponse response = client.prepareSearch(index)

                .setTypes(type)

                .setSearchType(SearchType.QUERY_AND_FETCH)

                .setQuery(qb)

                .addField("message")

                .addField("user")

                .setSize(100).execute().actionGet();

        printResults(response);

        System.out.println("--------------------------------");

    }

    private static void showAllData(Client client) {

        System.out.println("Alle Datensätze anzeigen");

        QueryBuilder qb = QueryBuilders.matchAllQuery();


        SearchResponse response = client.prepareSearch(index)

                .setTypes(type)

                .setSearchType(SearchType.QUERY_AND_FETCH)

                .setQuery(qb)

                .addField("message")

                .addField("user")

                .setSize(100).execute().actionGet();

        printResults(response);

        System.out.println("--------------------------------");

    }

    private static void deleteIndex(Client client) {

        System.out.println("Testdaten wieder löschen");

        try {

            DeleteIndexResponse delete = client.admin().indices()

                    .delete(new DeleteIndexRequest(index)).actionGet();

            if (!delete.isAcknowledged()) {

                System.out.println("Index wasn't deleted");

            } else {

                System.out.println("Index was deleted");

            }

        } catch (Exception e) {

            System.out.println("Index didn't exists");

        }

        System.out.println("--------------------------------");

    }

    private static void createData(Client client) throws InterruptedException {

        System.out.println("Testdaten werden erstellt");

        IndexResponse response = null;

        for (int i = 0; i < 10; i++) {

            Map<String, Object> json = new HashMap<String, Object>();

            String user = (i % 2 == 0) ? foo : bar;

            json.put("user", user);

            json.put("datum", new Date());

            json.put("message", "Hallo Welt : Nummer (" + i + ")");

            response = client.prepareIndex(index, type)

                    .setSource(json)

                    .setOperationThreaded(false)

                    .execute()

                    .actionGet();

        }

        String _index = response.getIndex();

        String _type = response.getType();

        long _version = response.getVersion();

        System.out.println("Index : " + _index + "   Type : " + _type + "   Version : " + _version);

      //  Thread.sleep(1000);

        System.out.println("--------------------------------");

    }

    private static void printResults(SearchResponse response) {

        for (SearchHit hit : response.getHits()) {

            String user = hit.getFields().get("user").getValue().toString();

            String message = hit.getFields().get("message").getValue().toString();

            System.out.println(user + " : " + message);

        }

    }

}
