package online.hk10.OnlineForms.database;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class DatabaseConfiguration {

	private static Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);
	
	@Autowired
	private MockDB inMem; 
	
	// this method is called per user session, if sql connection fails, revert to inMem.
	@Bean
	@Primary
	@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
	public FormsDAO mySqlDatabse() {
		try {
			return new MySqlDao();
		} catch (SQLException e) {
			logger.warn("Failed to create MySqlDao: \"" + e.getMessage() + "\"Reverting to in-mem MockDB");
			return inMem;
		}
	}
}
