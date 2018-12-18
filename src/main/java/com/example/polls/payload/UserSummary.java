package com.example.polls.payload;

public class UserSummary {
    private Long id;
    private String username;
    private String name;

    // naknadno sam proslijedio email, da kod ProfileEdit.js ne moram ponovno pozivati usera i raditi request na server
    // nego sam htio sa screena, route (/) proslijediti samo usera iz skripte App.js u skriptu ProfileEdit.js
    // to sam radio sa props
    private String email;

    // TO ĆU MOŽDA JOŠ POPRAVTII, JER SADA UPRAVO TO RADIM
    // kod ProfileEdit.js pozivam ponovno usera i radim request na server
    // jer dok se je user promijenio, nisu mi se promijenili credentialsi od usera, pa mi se je pokazivao stari name i email
    // to ću morati osmisliti kako riješiti

    private String roles;

    public UserSummary(Long id, String username, String name, String email, String roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

}
