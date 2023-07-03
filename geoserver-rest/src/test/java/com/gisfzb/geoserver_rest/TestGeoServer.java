package com.gisfzb.geoserver_rest;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;
import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @Classname TestGeoServer
 * @Description TODO
 * @Date 2019/10/30 8:52
 * @Created by MiaoShaoxuan
 */
public class TestGeoServer {
    public static void main(String[] args) throws Exception {
        //GeoServer的连接配置
        String url = "http://192.168.0.105:8080/geoserver" ;
        String username = "admin" ;
        String passwd = "geoserver" ;

        String ws = "test";     //待创建和发布图层的工作区名称workspace
        String store_name = "xian_img"; //待创建和发布图层的数据存储名称store

        //判断工作区（workspace）是否存在，不存在则创建
        URL u = new URL(url);
        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
        GeoServerRESTPublisher publisher = manager.getPublisher();
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        if (!workspaces.contains(ws)) {
            boolean createws = publisher.createWorkspace(ws);
            System.out.println("create ws : " + createws);
        } else {
            System.out.println("workspace已经存在了,ws :" + ws);
        }

        //判断数据存储（datastore）是否已经存在，不存在则创建

        String fileName = "/Users/fangzi/Downloads/1564520935465254912.tif";

        RESTDataStore restStore = manager.getReader().getDatastore(ws, store_name);
        if (restStore == null) {
            GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(store_name);
            gsGeoTIFFDatastoreEncoder.setWorkspaceName(ws);
            gsGeoTIFFDatastoreEncoder.setUrl(new URL("file:" + fileName));
            boolean createStore = manager.getStoreManager().create(ws, gsGeoTIFFDatastoreEncoder);
            System.out.println("create store (TIFF文件创建状态) : " + createStore);
            boolean publish = manager.getPublisher().publishGeoTIFF(ws, store_name, new File(fileName));
            System.out.println("publish (TIFF文件发布状态) : " + publish);
        } else {
            System.out.println("数据存储已经存在了,store:" + store_name);
        }
    }
}