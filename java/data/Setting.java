package data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="settings")
public class Setting {
	@Id 
	public int code;
	public String name;
	public String value;
}
