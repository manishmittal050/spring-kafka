//package com.reqres.api.serviceImpl;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.reqres.api.DAO.UserDao;
//import com.reqres.api.entity.User;
//import com.reqres.api.services.UserService;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//	@Autowired
//	private UserDao dao;
//	
//	@Override
//	public String fetchEmailById(int id) {
//		Optional<User> user = dao.findById(id);
//		if(user.isPresent())
//			return user.get().getEmailId();
//		
//		return null;
//	}
//
//}
