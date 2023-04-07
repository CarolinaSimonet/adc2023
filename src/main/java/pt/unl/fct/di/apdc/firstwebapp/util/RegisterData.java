package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {
    public String username;
    public String password;
    public String email;

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

    public RegisterData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public RegisterData() {

    }

    public boolean validResgistration() {
        if(getPassword()!= null && getUsername() != null)
            return true;
        else return false;
    }
}
