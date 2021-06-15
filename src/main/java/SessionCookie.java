import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionCookie {
    @JsonIgnore
    private String remember_token;
    @JsonProperty("id")
    private  String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("activated?")
    private Boolean activated;
    @JsonProperty("plan")
    private String plan;
    @JsonProperty("couchdb_db_name")
    private String couchdb_db_name;

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public String getPlan() {
        return plan;
    }

    public String getCouchdb_db_name() {
        return couchdb_db_name;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setCouchdb_db_name(String couchdb_db_name) {
        this.couchdb_db_name = couchdb_db_name;
    }
}
