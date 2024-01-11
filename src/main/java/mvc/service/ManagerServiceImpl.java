package mvc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import mvc.bean.Manager;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;
import mvc.enums.ManagerLevel;

@Service
public class ManagerServiceImpl implements ManagerService {
	@Autowired
	private DataDao dataDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ManagerDao managerDao;



	@Override
	public List<User> findPengingListPage() {
		return managerDao.findUncheckUsers();
	}

	@Override
	public int getPendingCount() {
		
		return managerDao.getPendingCount();
	}

	@Override
	public List<User> findPassListPage() {
		return managerDao.findPassUsers();
	}

	@Override
	public User getUserInfor(Integer Id) {
		
		return userDao.getUserById(Id).get();
	}

	@Override
	public List<Map<String, Object>> findUserAccount(Integer Id) {

		return userDao.findUserAccountByUserId(Id);
	}

	@Override
	public List<User> findFalseListPage() {
		return managerDao.findFalseUsers();
	}

	@Override
	public void userApprove(Integer id) {
		managerDao.updateUserStatusToPassById(id);
		
	}

	@Override
	public void addUserAccount(Integer id, User user) {
		managerDao.addUserAccount(id, user);
		
	}

	@Override
	public void userReject(Integer id,String falsereason) {
		managerDao.updateUserStatusToFalseById(id, falsereason);
		
	}

	
	
	
	

}
