package online.hk10.OnlineForms.database.entities;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String username;
    private String password; // encoded

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User() {
    	// for jackson, do not use
    }
    
    public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// temporary
		return Arrays.asList(new SimpleGrantedAuthority(username), new SimpleGrantedAuthority("user"));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}