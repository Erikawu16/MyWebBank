package mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mvc.bean.Account;
import mvc.bean.AccountDetail;
import mvc.bean.AccountRecord;
import mvc.bean.Currency;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;

public interface UserDao {
	


		
	
	
	
	// 1.註冊會員
	int addUser(User user);

//	2. 根據使用者Id查找使用者(登入用-單筆)
	Optional<User> findUserByUserId(String userId);

	// 3.根據使用者id更新密碼及信箱
	int updateUserById(String string, String password, String email);



	Optional<User> getUserById(Integer id);

	

	// 查詢所有匯率資料
	List<Currency> findAllCurrency();

	Optional<Currency> getCurrencyById(Integer id);

	// 查詢會員全部帳戶透過會員id
	List<Map<String, Object>> findUserAccountByUserId(Integer id);

    //取得使用者的帳戶透過幣別id和使用者id
	Optional<Account> getUserAccountByCurrencyId(Integer id,User user);
	int updateAccountAmount(Integer currencyId,Float newamount,Integer userId);
	// 查詢使用者尚未註冊的外幣帳戶
	List<Currency> findUserUnregisteredForeignAccountsByUserId(Integer id);

   //使用者新增外幣帳戶
	int addUserForeignAccount(User user, Integer foreignaccount);

	List<Map<String, Object>> findAccountDetailByUserId(Integer id);
	//查詢交易紀錄透過時間區間
	List<Map<String, Object>> findAccountDetailByTime(Integer userId,String start,String end);
	
	
	List<Map<String, Object>> findAccountRecorderByCurrencyId(Integer userId,Integer currencyId);
	List<Map<String, Object>> findAccountRecorderByUserId(Integer userId);
	
	//新增換匯詳細資訊
	int addAccountDetail(AccountDetail accountdetail);
	//新增交易訊息再個人帳戶紀錄
	int addAccountRecorder(AccountRecord accountrecord);
}