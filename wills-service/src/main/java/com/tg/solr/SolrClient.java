package com.tg.solr;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;

import com.tg.util.CONFIGUtil;

/**
 * @author xingxing.feng@renren-inc.com
 * @version 2013-1-7下午4:46:06 readme
 */
public class SolrClient {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(SolrClient.class);
    
    private static final int zkClientTimeout = 60000;

    private static final int zkConnectTimeout = 15000;

    private static SolrClient solrClient=null;
    
    private static CloudSolrServer cloudSolrServerUser;
    
    private static final String collectionName="tguser";
    
    private static final int RESPONSE_STATUS_OK=0;
    
    private static final String zkHost=CONFIGUtil.getInstance().getConfig("zk_host");
    
    private SolrClient(){
        try {
           
            cloudSolrServerUser = new CloudSolrServer(zkHost);
            
            cloudSolrServerUser.setDefaultCollection(collectionName);
            cloudSolrServerUser.setZkClientTimeout(zkClientTimeout);
            cloudSolrServerUser.setZkConnectTimeout(zkConnectTimeout);

            cloudSolrServerUser.connect();
            
        } catch (MalformedURLException e) {
            logger.error("getCloudSolrServer ERROR :",e);
        } catch (Exception e) {
            logger.error("getCloudSolrServer ERROR :",e);
        }
    }
    
    public static SolrClient getInstance(){
        if(solrClient==null){
            solrClient=new SolrClient();
        }
        return solrClient;
    }

    
    
    public QueryResponse queryUser(SolrQuery queryArgs) {
        QueryResponse rsp=null;
        try {
            rsp = cloudSolrServerUser.query(queryArgs);    
        } 
        catch (SolrServerException e) {
           // TODO Auto-generated catch block
            logger.error("solr query ERROR :",e);
        }
        return rsp;
    }
    
    
    public boolean addUser(User4Solr user4Solr) {
        UpdateResponse result=null;
        try {
            result=cloudSolrServerUser.addBean(user4Solr);
            return (result.getStatus()==RESPONSE_STATUS_OK) ;
        } catch (Exception e) {
            logger.error("solr add feed ERROR :",e);
        }
        return false;
    }
    
    public boolean addUserCommit() {
    	  UpdateResponse result=null;
          try {
              result = cloudSolrServerUser.commit();
              return (result.getStatus()==RESPONSE_STATUS_OK) ;
          } 
          catch (Exception e) {
              logger.error("solr user commit ERROR :",e);
          }
         return false;
    }
    
    public boolean deleteUser(int userId){
    	UpdateResponse result=null;
    	try {
             result=cloudSolrServerUser.deleteById(String.valueOf(userId));
             return (result.getStatus()==RESPONSE_STATUS_OK) ;
         } catch (Exception e) {
             logger.error("solr add feed ERROR :",e);
         }
         return false;
    }
    
    public static void main(String[] args) {
        SolrQuery queryArgs=new SolrQuery();
        queryArgs.setQuery("*:*");
        queryArgs.setStart(0);
        queryArgs.setRows(10);
 /*       queryArgs.setSortField("geodist()", ORDER.asc);
        queryArgs.setFilterQueries("{!geofilt}");
        queryArgs.setFields("*,dist:geodist()");
        queryArgs.set("sfield", "store");
        queryArgs.set("pt", "80,120");
        queryArgs.set("d", 3);*/
        //queryArgs.set("spatial",true);
        
        QueryResponse  qr=SolrClient.getInstance().queryUser(queryArgs);;

        System.out.println(qr.toString());
        
    }
}
