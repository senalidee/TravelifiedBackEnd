package com.cyntex.TourismApp.Persistance;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cyntex.TourismApp.Beans.GetUserFriendQueryResponse;
import com.cyntex.TourismApp.Util.DataSourceManager;


@Component
public class FriendListDAO {
	
	@Autowired
	private DataSourceManager dataSourceManager;
	
	private int response;
	
	private static final String addFriendRequestQuery=
			"insert into friend_list values (?,?)";
	private static final String checkRecordRequestQuery=
			"select count(*) as counter from friend_list where username = ? and username_of_friend = ?";
	
	private static final String getFriendRequestQuery=
			"select * from friend_list as one left join user as two on (one.username_of_friend=two.username) where one.username = ? ";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	public void addFriend(String usernameOfRequester, String usernameOfFriend) throws Exception{
		jdbcTemplate.update(addFriendRequestQuery,
                new Object[] {usernameOfRequester,usernameOfFriend},
                new int[]{Types.VARCHAR,Types.VARCHAR});
		jdbcTemplate.update(addFriendRequestQuery,
                new Object[] {usernameOfFriend,usernameOfRequester},
                new int[]{Types.VARCHAR,Types.VARCHAR});
   	
	}
	
	@Transactional
	public boolean isRecordAlreadyExists(String usernameOfRequester, String usernameOfFriend){
		jdbcTemplate.query(checkRecordRequestQuery,
                new Object[] {usernameOfRequester,usernameOfFriend},
                new int[]{Types.VARCHAR,Types.VARCHAR},(rs,rawNo) -> response=rs.getInt("counter"));
		
		if(response == 0){return false;} else{return true;}
		
	}
	
	public List<GetUserFriendQueryResponse> getFriend(String username) throws Exception{
		
		List<GetUserFriendQueryResponse> userFriendResponse=jdbcTemplate.query(getFriendRequestQuery,
				new Object[]{username},new int[]{Types.VARCHAR},(rs,rawNo)-> new GetUserFriendQueryResponse(
						rs.getString("username_of_friend"), rs.getString("first_name")+" " +rs.getString("last_name")));
		
		
		return userFriendResponse;
	}
	

}
