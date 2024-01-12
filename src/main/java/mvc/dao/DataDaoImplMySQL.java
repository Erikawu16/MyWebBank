package mvc.dao;

import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mvc.bean.Currency;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;

@Repository
public class DataDaoImplMySQL implements DataDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final String sqlAll = "SELECT itemId as id, itemName as name FROM my_bank.basedata where groupName = ?";
	private final String sqlSingle = "SELECT itemId as id, itemName as name FROM my_bank.basedata where groupName = ? and itemId = ?";
	


	@Override
	public List<SexData> findAllSexDatas() {
		return jdbcTemplate.query(sqlAll, new BeanPropertyRowMapper<>(SexData.class), "Sex");
	}

	@Override
	public Optional<SexData> getSexDataById(Integer id) {
		return Optional.of(jdbcTemplate.queryForObject(sqlSingle, new BeanPropertyRowMapper<>(SexData.class), "Sex", id));
	}



	@Override
	public List<TypeData> findAllTypeDatas() {
		return jdbcTemplate.query(sqlAll, new BeanPropertyRowMapper<>(TypeData.class), "Type");
		
	}

	@Override
	public Optional<TypeData> getTypeDataById(Integer id) {
		return Optional.of(jdbcTemplate.queryForObject(sqlSingle, new BeanPropertyRowMapper<>(TypeData.class), "Type", id));
		}

	@Override
	public List<StatusData> findAllStatusDatas() {
		return jdbcTemplate.query(sqlAll, new BeanPropertyRowMapper<>(StatusData.class), "Status");
		}

	@Override
	public Optional<StatusData> getStatusDataById(Integer id) {
		return Optional.of(jdbcTemplate.queryForObject(sqlSingle, new BeanPropertyRowMapper<>(StatusData.class), "Status", id));
		
	}

	
	
	
}
	
