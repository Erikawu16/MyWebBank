package mvc.bean;
//興趣資料
public class StatusData {
	private Integer id;
	private String name;
	
	public StatusData( ) {
		
	}
	
	public StatusData(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "InterestData [id=" + id + ", name=" + name + "]";
	}
}