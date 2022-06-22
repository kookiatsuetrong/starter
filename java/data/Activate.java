package data;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="activates")
public class Activate {
	@Id
	public int code;
	public int member;
	public String secret;
	public Instant created = Instant.now();
}
