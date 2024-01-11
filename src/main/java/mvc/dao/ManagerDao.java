package mvc.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import mvc.bean.Account;
import mvc.bean.Manager;
import mvc.bean.User;

@Repository
public interface ManagerDao {
	
	
	

//	2. 根據管理員Id查找使用者(登入用-單筆)
	Optional<Manager> findManagerByManagerName(String managerId);
	//計算有多少尚未審核會員
	int getPendingCount();
	//計算女性會員
	int getUserSexCount(Integer sexid);
	
	List<Integer> findUserAgeList();
	
	// 1.更新會員
	int updateUserById(Integer id, User user);

	// 2.刪除會員
	int deleteUserById(Integer id);

	// 3.根據userId查找個別會員
	Optional<User> getUserById(Integer id);

	// 4.更新會員狀態(審核通過)
	int updateUserStatusToPassById(Integer id);

	// 5.更新會員狀態(審核不通過)
	int updateUserStatusToFalseById(Integer id,String falsereason);

	// 6.查詢所有會員
	List<User> findAllUsers();

	// 7.查詢已通過會員
	List<User> findPassUsers();

	// 8.查詢未通過會員
	List<User> findFalseUsers();

	// 9.查詢已審查會員
	List<User> findcheckedUsers();

	// 10.查詢尚未審查的會員
	List<User> findUncheckUsers();

	// 11.新增台幣帳戶
	int addUserAccount(Integer id,User user);

	List<Map<String, Object>> findallAccounts();
	
	

	List<Map<String, Object>> findPassAccounts();
	
}