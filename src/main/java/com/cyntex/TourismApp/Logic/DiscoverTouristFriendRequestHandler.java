package com.cyntex.TourismApp.Logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cyntex.TourismApp.Beans.BaseResponse;
import com.cyntex.TourismApp.Beans.DiscoverTouristFriendQuaryResponseBean;
import com.cyntex.TourismApp.Beans.DiscoverTouristFriendRequestBean;
import com.cyntex.TourismApp.Beans.RatingProfileFetchQuaryBasedOnCategoryBean;
import com.cyntex.TourismApp.Beans.RatingsProfileQueryResponseBean;
import com.cyntex.TourismApp.Beans.RatingsProfileRequestBean;
import com.cyntex.TourismApp.Beans.RatingsProfileResponseBean;
import com.cyntex.TourismApp.Beans.RegistrationRequestBean;
import com.cyntex.TourismApp.Beans.UserRating;
import com.cyntex.TourismApp.Persistance.UserProfileDAO;
import com.cyntex.TourismApp.Persistance.UserRatingsProfileDAO;
import com.cyntex.TourismApp.Util.UserRatingCalculator;


@Component
public class DiscoverTouristFriendRequestHandler {
	
	@Autowired
	private UserRatingCalculator userRatingCalculator;
	
    @Autowired
    private UserRatingsProfileDAO userRatingsProfileDAO;
    
    @Autowired
    private UserProfileDAO userProfileDAO;
	
	
  public List<RegistrationRequestBean> handle(DiscoverTouristFriendRequestBean discoverTouristFriendRequestBean){
	  
	  
	        RatingsProfileResponseBean responseBean = new RatingsProfileResponseBean();
	        List<UserRating> requestedUserRatingList= new ArrayList<UserRating>();
	        List<RegistrationRequestBean> touristFriendProfileList= new ArrayList<RegistrationRequestBean>();
	    //    System.out.println("category1 ");
	        try {
//	            List<RatingsProfileQueryResponseBean> queryResponse =
//	                    userRatingsProfileDAO.getUserRatingsProfile(ratingsProfileRequestBean.getUsername());
//	            responseBean=userRatingcalcalculator.RatingProfileResponse(queryResponse);
	        	
	            List<RatingsProfileQueryResponseBean> queryResponse =
	                    userRatingsProfileDAO.getUserRatingsProfile(discoverTouristFriendRequestBean.getUsername());
	            
	            requestedUserRatingList=userRatingCalculator.RatingProfileResponse(queryResponse).getUserRatings();
	            System.out.println("userRatingList "+requestedUserRatingList.size());
	            responseBean=userRatingCalculator.RatingProfileResponse(queryResponse);
	            
	            for(UserRating userRating : requestedUserRatingList){
	            	System.out.println("category "+userRating.getCategory());
	            	System.out.println("username "+discoverTouristFriendRequestBean.getUsername());
	            	List<DiscoverTouristFriendQuaryResponseBean> discoverTouristFriendQuaryResponseBeanList=
	            			userRatingsProfileDAO.getAverageRating(userRating.getCategory(),discoverTouristFriendRequestBean.getUsername());

	            	for(DiscoverTouristFriendQuaryResponseBean discoverTouristFriendQuaryResponseBean :discoverTouristFriendQuaryResponseBeanList){
	            		double averageRating=discoverTouristFriendQuaryResponseBean.getAverageRating();
	            		System.out.println("averageRating "+averageRating);
	            		if(averageRating >=userRating.getRating()-1 && averageRating <=userRating.getRating()+1 ){
	            			touristFriendProfileList.add(userProfileDAO.getUserRatingsProfile(discoverTouristFriendQuaryResponseBean.getUsername()));
	            			if(touristFriendProfileList.size()>=10){
	            				return touristFriendProfileList;
	            			}
	            		}
	            		
	            	}
	            	
	             
	            	
	            	
	            }
	    //        responseBean=userRatingCalculator.RatingProfileResponse(queryResponse);
	     //       responseBean.setStatus("SUCCESS");
	        } catch (Exception e) {
	     //       responseBean.setStatus("FAIL");
	        	 System.out.println(e);
	        }

	        return touristFriendProfileList;
	  
	//  List<UserRatings> requestedUserRatings = userRatingCalculator.handle(ratingsProfileRequestBean)
	  
	  
	  
  }
	
	
	
	
	
	

}
