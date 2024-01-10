package mvc.bean;
import java.util.Date;
import java.util.List;

import lombok.Data;


@Data
public class AccountRecord {
	
	
	
	int AccountRecorderId;
	
	Integer userId; 
	User user;
	
	Integer accountId; 
	Account account;
	
	Currency currency;
	Integer currencyId;
	
	
	Float transationmoney;//交易金額
	
	Float currentAmount;//當下餘額
	Date transaction_time;//交易日期
	
	
	
}
