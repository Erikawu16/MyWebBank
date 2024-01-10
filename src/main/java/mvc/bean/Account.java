package mvc.bean;

import java.util.Date;

import lombok.Data;
@Data
public class Account {
	
	User  user;
	Integer userId;//抓使用者ID
	
	Currency currency;//抓帳戶幣別
	Integer currencyId;//抓帳戶幣別
	
	Integer accountId;//帳戶號碼
	Float amount;//帳戶餘額
	
	Date create_time;//創辦時間



}
