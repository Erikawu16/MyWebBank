package mvc.dao;

import java.util.Base64;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mvc.bean.Account;
import mvc.bean.AccountDetail;
import mvc.bean.AccountRecord;
import mvc.bean.Currency;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;
import mvc.util.KeyUtil;

@Repository
public class UserDaoImplMySQL implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataDao dataDao;

//使用者資訊/////////////////////////////////////////////////////////////////
// 1.註冊會員加入資料庫
	@Override
	public int addUser(User user) {
		String sql = "INSERT INTO user (username,userId,  email,password, birth,sexId,typeIds,ImgContent)"
				+ " values(?, ?, ?,?,?,?,?,?)";

		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB = null;
		try {
			encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, user.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		int rowcount = jdbcTemplate.update(sql, user.getUsername(), user.getUserId(), user.getEmail(),
				encryptedPasswordECBBase64, user.getBirth(), user.getSexId(), user.getTypeId(), user.getImgContent());
		return rowcount;
	}

//	2. 根據使用者Id查找使用者(登入用-單筆)
	@Override
	public Optional<User> findUserByUserId(String userId) {
		String sql = "select id,username,userId,  email,password, birth,sexId,statusId,registDate from user where userId = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), userId);
			enrichUserDetail(user);
			return Optional.ofNullable(user);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	// 3.更新密碼與信箱
	@Override
	public int updateUserById(String userId, String newPassword, String email) {
		String sql = "update user set password = ? ,email=? where userId = ?";
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB = null;
		try {
			encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		int rowcount = jdbcTemplate.update(sql, encryptedPasswordECBBase64, email, userId);
		return rowcount;
	}

	// 4.取得個別使用者
	@Override
	public Optional<User> getUserById(Integer id) {
		String sql = "select id,username,userId,  email,password, birth,sexId,statusId,registDate, ImgContent from user where id = ?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
			enrichUserDetail(user);
			return Optional.ofNullable(user);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}

	// 5.查詢使用者全部帳戶
	@Override
	public List<Map<String, Object>> findUserAccountByUserId(Integer userId) {
		String sql = "SELECT a.accountId, a.userId, a.currencyId, a.amount, a.create_time, "
				+ "c.currencyId AS 'currency_currencyId', c.currencyName AS 'currency_currencyName', "
				+ "u.userId AS 'user_userId', u.username AS 'user_username' FROM account a "
				+ "LEFT JOIN currency c ON a.currencyId = c.currencyId " + "LEFT JOIN user u ON a.userId = u.Id "
				+ "WHERE a.userId=?;";
		return jdbcTemplate.queryForList(sql, userId);
	}

	// 6.透過匯率id查詢使用者對應外幣帳戶
	public Optional<Account> getUserAccountByCurrencyId(Integer id, User user) {
		String sql = "SELECT currencyId, userId, accountId,amount,create_time"
				+ " FROM account WHERE userId = ? AND currencyId = ?";
		List<Account> accounts = jdbcTemplate.query(sql, new Object[] { user.getId(), id },
				new BeanPropertyRowMapper<>(Account.class));
		// 檢查是否有找到符合條件的帳戶
		if (!accounts.isEmpty()) {
			Account account = accounts.get(0);
			enrichAccountDetail(account);
			return Optional.of(accounts.get(0)); // 如果有符合條件的帳戶，返回第一個帳戶
		} else {
			return Optional.empty(); // 如果沒有符合條件的帳戶，返回空的 Optional
		}
	}

	// 7.查詢使用者尚未註冊之外幣帳戶
	@Override
	public List<Currency> findUserUnregisteredForeignAccountsByUserId(Integer userId) {
		String sql = "SELECT * FROM currency "
				+ "WHERE currencyId NOT IN (SELECT currencyId FROM account WHERE userId = ?)";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Currency.class), userId);

	}

	// 8.使用者新增外幣帳戶
	@Override
	public int addUserForeignAccount(User user, Integer foreignaccount) {
		String sql = "INSERT INTO Account (userId, currencyId)values(?,?) ";
		int rowcount = jdbcTemplate.update(sql, user.getId(), foreignaccount);
		return rowcount;
	}

	// 更新帳戶餘額
	@Override
	public int updateAccountAmount(Integer currencyId, Float newamount, Integer userId) {
		String sql = "update Account set amount=? where CurrencyId=? AND userId=?";
		int rowcount = jdbcTemplate.update(sql, newamount, currencyId, userId);
		return rowcount;
	}

//匯率//////////////////////////////////////////////////////////////////
	// 1.查詢全部匯率資訊
	@Override
	public List<Currency> findAllCurrency() {
		String sql = "SELECT currencyId ,currencyname, " + "cashin, cashout," + " bankin, bankout, searchDate "
				+ "FROM Currency ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Currency.class));

	}

	// 2.透過匯率id查詢匯率資訊
	@Override
	public Optional<Currency> getCurrencyById(Integer id) {
		String sql = "SELECT currencyId, currencyname, cashin, cashout,bankin, bankout, searchDate FROM my_bank.Currency  where currencyId = ?";
		return Optional.of(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Currency.class), id));

	}

//換匯交易///////////////////////////////////////////////////////////////
	// 1.新增換匯交易明細
	@Override
	public int addAccountDetail(AccountDetail accountdetail) {
		String sql = "INSERT INTO accountdetail "
				+ "(userId,from_currencyId, from_accountId, to_currencyId, to_accountId, moneyout, moneyin, rate) "
				+ "values(?, ?, ?,?,?,?,?,?)";
		int rowcount = jdbcTemplate.update(sql, accountdetail.getUserId(), accountdetail.getFrom_currencyId(),
				accountdetail.getFrom_accountId(), accountdetail.getTo_currencyId(), accountdetail.getTo_accountId(),
				accountdetail.getMoneyout(), accountdetail.getMoneyin(), accountdetail.getRate());
		return rowcount;
	}

	// 2.查詢所有換匯紀錄透過時間區間
	@Override
	public List<Map<String, Object>> findAccountDetailByTime(Integer userId, String start, String end) {

		String sql = "SELECT " + "ad.userId, " + "ad.from_accountId, " + "ad.to_accountId, " + "ad.moneyout, "
				+ "ad.moneyin, " + "ad.rate, " + "ad.transaction_time, " + "c_from.currencyName AS from_currencyName, "
				+ "c_to.currencyName AS to_currencyName " + "FROM " + "my_bank.accountdetail ad " + "LEFT JOIN "
				+ "my_bank.currency c_from ON ad.from_currencyId = c_from.currencyId " + "LEFT JOIN "
				+ "my_bank.currency c_to ON ad.to_currencyId = c_to.currencyId " + "WHERE " + "ad.userId = ? "
				+ "and  transaction_time BETWEEN ?  AND ? " + "ORDER BY " + "ad.transaction_time DESC;";

		return jdbcTemplate.queryForList(sql, userId, start, end);

	}

	// 3.查詢所有換匯紀錄透過使用者id
	@Override
	public List<Map<String, Object>> findAccountDetailByUserId(Integer userId) {
		String sql = "SELECT " + "ad.userId, " + "ad.from_accountId, " + "ad.to_accountId, " + "ad.moneyout, "
				+ "ad.moneyin, " + "ad.rate, " + "ad.transaction_time, " + "c_from.currencyName AS from_currencyName, "
				+ "c_to.currencyName AS to_currencyName " + "FROM " + "my_bank.accountdetail ad " + "LEFT JOIN "
				+ "my_bank.currency c_from ON ad.from_currencyId = c_from.currencyId " + "LEFT JOIN "
				+ "my_bank.currency c_to ON ad.to_currencyId = c_to.currencyId " + "WHERE " + "ad.userId = ? "
				+ "ORDER BY " + "ad.transaction_time DESC;";

		return jdbcTemplate.queryForList(sql, userId);

	}

	// 個別帳戶///////////////////////////////////////////////////
	// 1.新增個別帳戶交易明細
	@Override
	public int addAccountRecorder(AccountRecord accountrecord) {
		String sql = "INSERT INTO account_infor (userId,currencyId, accountId, transationmoney,currentAmount) "
				+ "values(?, ?, ?,?,?)";
		int rowcount = jdbcTemplate.update(sql, accountrecord.getUserId(), accountrecord.getCurrencyId(),
				accountrecord.getAccountId(), accountrecord.getTransationmoney(), accountrecord.getCurrentAmount());
		return rowcount;
	}

	// 2.查詢交易資訊透過userId及currencyId
	@Override
	public List<Map<String, Object>> findAccountRecorderByCurrencyId(Integer userId, Integer currencyId) {
		String sql = "SELECT ai.accountId, ai.userId, ai.transaction_time, ai.transationmoney, ai.currentAmount, "
				+ "a.amount AS account_amount, "
				+ "c.currencyId AS currency_currencyId, c.currencyName AS currency_currencyName, "
				+ "u.userId AS user_userId, u.username AS user_username " + "FROM my_bank.account_infor ai "
				+ "LEFT JOIN my_bank.account a ON ai.accountId = a.accountId "
				+ "LEFT JOIN my_bank.currency c ON ai.currencyId = c.currencyId "
				+ "LEFT JOIN my_bank.user u ON a.userId = u.Id "
				+ "WHERE ai.userId = ? AND ai.currencyId=? order by ai.transaction_time DESC ";

		return jdbcTemplate.queryForList(sql, userId, currencyId);
	}

	// 3.查詢全部交易資訊透過userId
	@Override
	public List<Map<String, Object>> findAccountRecorderByUserId(Integer userId) {
		String sql = "SELECT ai.accountId, ai.userId, ai.transaction_time, ai.transationmoney, ai.currentAmount, "
				+ "a.amount AS account_amount, "
				+ "c.currencyId AS currency_currencyId, c.currencyName AS currency_currencyName, "
				+ "u.userId AS user_userId, u.username AS user_username " + "FROM my_bank.account_infor ai "
				+ "LEFT JOIN my_bank.account a ON ai.accountId = a.accountId "
				+ "LEFT JOIN my_bank.currency c ON ai.currencyId = c.currencyId "
				+ "LEFT JOIN my_bank.user u ON a.userId = u.Id "
				+ "WHERE ai.userId = ? order by ai.transaction_time DESC";
		return jdbcTemplate.queryForList(sql, userId);
	}

// 方法//////////////////////////////////////////////////
	// 方法>>豐富使用者資訊
	private void enrichUserDetail(User user) {
		SexData sex = dataDao.getSexDataById(user.getSexId()).get();
		user.setSex(sex);

	}

	// 方法>>豐富匯率資訊
	private void enrichAccountDetail(Account account) {
		Currency currency = getCurrencyById(account.getCurrencyId()).get();
		account.setCurrency(currency);

	}



}
