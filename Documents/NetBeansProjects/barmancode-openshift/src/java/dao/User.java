package dao;

public class User {

    private int id, points;
    private String username, name, surname, password, email, role;
    private boolean blockade;

    public User(int id, String username, String name, String surmane, int points, String email, Boolean blockade, String password) {
        // TODO Auto-generated constructor stub
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surmane;
        this.points = points;
        this.email = email;
        this.blockade = blockade;
        this.password = password;
    }

    public User() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getBlockade() {
        return blockade;
    }

    public void setBlockade(boolean blockade) {
        this.blockade = blockade;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
