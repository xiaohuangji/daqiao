package com.tg.service.utils;

import com.tg.model.GuideInfo;
import com.tg.solr.SolrClient;
import com.tg.solr.User4Solr;

public class GuideForSolrUtil {

	public static  boolean addGuideToSolr(GuideInfo guideInfo){
		User4Solr user4Solr=new User4Solr();
		user4Solr.setCity(guideInfo.getCity());
		user4Solr.setGender(guideInfo.getGender());
		user4Solr.setId(guideInfo.getUserId());
		user4Solr.setMobile(guideInfo.getMobile());
		user4Solr.setPosition(guideInfo.getLocation());
		user4Solr.setUserName(guideInfo.getUserName());
		user4Solr.setUserType(guideInfo.getUserType());
		user4Solr.setBeGuideYear(guideInfo.getBeGuideYear());
		user4Solr.setBirthday(guideInfo.getBirthday());
		user4Solr.setGoodAtScenic(guideInfo.getGoodAtScenic());
		SolrClient.getInstance().addUser(user4Solr);
		//后期需要将commit做成定时任务
		return SolrClient.getInstance().addUserCommit();
	}
	
	public static boolean commitToSolr(){
		return SolrClient.getInstance().addUserCommit();
	}
}
