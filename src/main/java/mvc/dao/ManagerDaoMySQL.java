package mvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import mvc.bean.Account;
import mvc.bean.Currency;
import mvc.bean.Manager;
import mvc.bean.Service;
import mvc.bean.User;

@Repository
public class ManagerDaoMySQL implements ManagerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
//管理員分級	
	@Override
	public Optional<Manager> findManagerByManagerName(String managername) {

		String sql = "select Id,managername,password, levelId from manager where managername = ?";
		try {
			Manager manager = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Manager.class), managername);
			// 查找使用者可以使用的服務(授權)
			String sql2 = "select s.serviceId, s.serviceName, s.serviceUrl "
						+ "from level_ref_service r "
						+ "left join service s on s.serviceId = r.serviceId "
						+ "where r.levelId = ? order by r.sort";
			List<Service> services = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(Service.class), manager.getLevelId());
			manager.setServices(services);
			return Optional.ofNullable(manager);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	
	//計算待審核筆數
    @Override
    public int getPendingCount() {
        String sql = "SELECT COUNT(*) AS count_status_1 FROM my_bank.user WHERE statusId = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
  //查詢會會員性別數量
    @Override
    public int getUserSexCount(Integer sexid) {
        String sql = "SELECT COUNT(*) AS count_Sex_1 FROM my_bank.user WHERE SexId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,sexid);
    }
 
    //查詢會會員年齡分布
    @Override
    public List<Integer> findUserAgeList() {
        String sql = "SELECT " +
                     "SUM(CASE WHEN age BETWEEN 18 AND 29 THEN 1 ELSE 0 END) AS '18-29', " +
                     "SUM(CASE WHEN age BETWEEN 30 AND 39 THEN 1 ELSE 0 END) AS '30-39', " +
                     "SUM(CASE WHEN age BETWEEN 40 AND 49 THEN 1 ELSE 0 END) AS '40-49', " +
                     "SUM(CASE WHEN age BETWEEN 50 AND 59 THEN 1 ELSE 0 END) AS '50-59', " +
                     "SUM(CASE WHEN age >= 60 THEN 1 ELSE 0 END) AS '60up' " +
                     "FROM ( " +
                     "    SELECT " +
                     "        CURDATE() AS today, " +
                     "        YEAR(CURDATE()) - YEAR(birth) - (RIGHT(CURDATE(), 5) < RIGHT(birth, 5)) AS age " +
                     "    FROM " +
                     "        user " +
                     ") AS age_groups";

        List<Integer> ageCounts = jdbcTemplate.query(sql, new ResultSetExtractor<List<Integer>>() {
            @Override
            public List<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Integer> result = new ArrayList<>();
                if (rs.next()) {
                    result.add(rs.getInt("18-29"));
                    result.add(rs.getInt("30-39"));
                    result.add(rs.getInt("40-49"));
                    result.add(rs.getInt("50-59"));
                    result.add(rs.getInt("60up"));
                }
                return result;
            }
        });

        return ageCounts;
    }


	// 1.更新會員
	@Override
	public int updateUserById(Integer id, User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 2.刪除會員
	@Override
	public int deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 3.根據userId查找個別會員
	@Override
	public Optional<User> getUserById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	// 4.更新會員狀態(審核通過)ok
	@Override
	public int updateUserStatusToPassById(Integer userId) {
		String sql = "update user set statusId = 2 where  id = ? ";
		return jdbcTemplate.update(sql, userId);
	}

	// 5.更新會員狀態(審核不通過)ok
	@Override
	public int updateUserStatusToFalseById(Integer userId, String falsereason) {
		String sql = "UPDATE user SET statusId = 3, falsereason = ? WHERE id = ?";
		return jdbcTemplate.update(sql, falsereason, userId);
	}

	// 6.查詢所有會員ok
	@Override
	public List<User> findAllUsers() {
		String sql = "SELECT Id, username, userId, email, password, birth, registDate, sexId, typeIds, statusId , falsereason "
				+ "FROM user; ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));

	}

	// 7.查詢已通過會員ok
	@Override
	public List<User> findPassUsers() {
		String sql = "SELECT Id, username, userId, email, password, birth, registDate, sexId, typeIds, statusId,ImgContent  "
				+ "FROM user  WHERE statusId=2 ; ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

	// 8.查詢未通過會員ok
	@Override
	public List<User> findFalseUsers() {
		String sql = "SELECT Id, username, userId, email, password, birth, registDate, sexId, typeIds, statusId ,falsereason,ImgContent "
				+ "FROM user  WHERE statusId=3 ; ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

	// 9.查詢尚未審查的會員ok
	@Override
	public List<User> findUncheckUsers() {
		String sql = "SELECT Id, username, userId, email, password, birth, registDate, sexId, typeIds, statusId ,ImgContent "
				+ "FROM user  WHERE statusId=1 ; ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

	// 10.查詢已經審查的會員ok
	@Override
	public List<User> findcheckedUsers() {
		String sql = "SELECT Id, username, userId, email, password, birth, registDate, sexId, typeIds, statusId "
				+ "FROM user  WHERE statusId IN (2, 3, 4) ; ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));

	}

	// 11.新增台幣帳戶
	@Override
	public int addUserAccount(User user,Integer correncyId) {
		String sql = "INSERT INTO Account (userId, currencyId)values(?,?) ";
		int rowcount = jdbcTemplate.update(sql, user.getId(), "1");// 1是台幣帳戶
		return rowcount;

	}

	@Override
	public List<Map<String, Object>> findallAccounts() {

		String sql = "SELECT  a.accountId, a.userId, a.currencyId, a.amount, a.create_time,"
				+ " c.currencyId AS 'currency_currencyId', c.currencyName AS 'currency_currencyName',"
				+ " u.userId AS 'user_userId', u.username AS 'user_username' " + "FROM account a "
				+ "LEFT JOIN currency c ON a.currencyId = c.currencyId " + "LEFT JOIN user u ON a.userId = u.Id;";

		return jdbcTemplate.queryForList(sql);

	}
	

	
   //查詢所有通過的帳戶
	@Override
	public List<Map<String, Object>> findPassAccounts() {
		String sql = "SELECT  a.accountId, a.userId, a.currencyId, a.amount, a.create_time, "
				+ "c.currencyId AS 'currency_currencyId', c.currencyName AS 'currency_currencyName', "
				+ "u.userId AS 'user_userId', u.username AS 'user_username', u.statusId AS 'user_statusId'"
				+ "FROM account a "
				+ "LEFT JOIN currency c ON a.currencyId = c.currencyId "
				+ "LEFT JOIN user u ON a.userId = u.Id "
				+ "where u.statusId=2;";

		return jdbcTemplate.queryForList(sql);

	}






}