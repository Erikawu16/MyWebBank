package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.Currency;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;

public interface DataDao {
	List<TypeData> findAllTypeDatas();
	Optional<TypeData> getTypeDataById(Integer id);
	
	List<SexData> findAllSexDatas();
	Optional<SexData> getSexDataById(Integer id);
	
	List<StatusData> findAllStatusDatas();
	Optional<StatusData> getStatusDataById(Integer id);
	

}