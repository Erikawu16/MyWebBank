package mvc.bean;

import java.sql.Date;

import lombok.Data;

@Data
public class Currency {
	Integer currencyId;
	String currencyname;//幣別
	Float cashin;//現金買入匯率
	Float cashout;//現金賣出匯率
	Float bankin;//銀行買入匯率
	Float bankout;//銀行賣出匯率
	Date searchDate;//時間

}
