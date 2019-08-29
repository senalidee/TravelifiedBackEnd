package com.cyntex.TourismApp.Persistance;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cyntex.TourismApp.Beans.ChatUserBean;
import com.cyntex.TourismApp.Beans.ContactTouristGuideGetMessageQueryResponseBean;
import com.cyntex.TourismApp.Beans.SendMessageQueryResponsBean;
import com.cyntex.TourismApp.Util.DataSourceManager;

public class MessageTouristGuideDAO {
	
	@Autowired
	DataSourceManager dataSourceManager;
	
	
	private static final String saveMessageQuery=
			"insert into message_details_guide(service_id,username,first_name, message ,created_date) values (?,?,?,?,?)";
	private static final String getMessageDetailsQuery =
			"select * from message_details_user as one left join group_participant as two on (one.username = two.username and one.chat_group_id = two.chat_group_id ) where one.chat_group_id = ?  order by one.created_date ";
	
	
	
	@Transactional
	public void saveMessage(int serviceId, String username,String first_name,  String message){
		dataSourceManager.getJdbcTemplate().update(saveMessageQuery,
                new Object[] {serviceId,username,first_name ,message, new Date()},
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.DATE});

		
	}
	
	@Transactional
	public List<ContactTouristGuideGetMessageQueryResponseBean> getMessageDetails(int serviceId ,String username){
		 List<ContactTouristGuideGetMessageQueryResponseBean> queryData = dataSourceManager.getJdbcTemplate().query(
				getMessageDetailsQuery,
                new Object[] {serviceId , username},
                new int[]{Types.INTEGER},
                (rs,rowNum) -> new ContactTouristGuideGetMessageQueryResponseBean(
                        rs.getInt("message_id"),
                         rs.getString("message"),
                        rs.getDate("created_date"),
                        new ChatUserBean(rs.getString("username"),rs.getString("first_name"),rs.getString("avatar")))
        );
        
		return queryData;
	}

}
