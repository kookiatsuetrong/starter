package data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table(name="members")
public class Member {
	@Id public int code;
	public String email;
	
	@Column("first_name")
	public String firstName;
	@Column("family_name")
	public String familyName;
	
	public String password;
	public String phone = "";
	public String status = "registered";
	
	public String getEmail()      { return email;      }
	public String getFirstName()  { return firstName;  }
	public String getFamilyName() { return familyName; }
	public String getPhone()      { return phone;      }
}
