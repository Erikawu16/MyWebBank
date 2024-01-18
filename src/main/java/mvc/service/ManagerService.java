package mvc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import mvc.bean.Account;
import mvc.bean.User;
import mvc.enums.ManagerLevel;



public interface ManagerService {

	List<User> findPengingListPage();
	List<User> findPassListPage();
	
	int getPendingCount();
	User getUserInfor(Integer Id);
	List<Map<String, Object>>findUserAccount(Integer Id);
	List<User> findFalseListPage();
	void userApprove(User user); 
	
	void userReject(Integer userId,String falsereason); 
}
