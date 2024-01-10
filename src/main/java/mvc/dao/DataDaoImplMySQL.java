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

	
	
	@Override
	public void updateCurrency() {
        try {
            // 連接台灣銀行匯率網頁
            String url = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
            Document doc = Jsoup.connect(url).get();

            // 找到匯率表格
            Element table = doc.select("table.table.table-striped.table-bordered.table-condensed.table-hover tbody").first();
            Elements rows = table.select("tr");

            // 遍歷表格資料
            for (Element row : rows) {
                Element currency = row.select("div.visible-phone.print_hide").first();
                Elements rates = row.select("td.rate-content-cash");
                Elements rates2 = row.select("td.rate-content-sight");

                if (currency != null && !rates.isEmpty() && !rates2.isEmpty()) {
                    String currencyName = currency.text();
                    String rate1 = rates.get(0).text();
                    String rate2 = rates.get(1).text();
                    String rate3 = rates2.get(0).text();
                    String rate4 = rates2.get(1).text();

                    // 印出匯率資訊
                    System.out.println(currencyName + " - 現金買入: " + rate1);
                    System.out.println(currencyName + " - 現金賣出: " + rate2);
                    System.out.println(currencyName + " - 即期買入: " + rate3);
                    System.out.println(currencyName + " - 即期賣出: " + rate4);

                    String sql = "UPDATE currency SET cashin = ?, cashout = ?, bankin = ?, bankout = ? WHERE currencyname = ?";

                    jdbcTemplate.update(sql, rate1,rate2,rate3,rate4,currencyName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
		
	}

}
	
