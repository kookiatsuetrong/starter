package main;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
// import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class Start 
{
	public static void main(String[] data)
	{
		var context = SpringApplication.run(Initialize.class);
	}
}

@SpringBootApplication
class Initialize 
{	
	@Bean
	DriverManagerDataSource createDataSource()
	{
		return new DriverManagerDataSource(connectionString);
	}
	
	@Value("${my.connection.string}")
	String connectionString;
}
