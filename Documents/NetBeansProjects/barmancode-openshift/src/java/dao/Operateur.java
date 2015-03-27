//package dao;
//
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;
//import com.mysql.jdbc.Statement;
//
//
//public class Operateur {
//
//    private PreparedStatement pr = null;
//
//    public ArrayList<Product> getAllProduit() {
//
//        ArrayList<Product> listproduit = new ArrayList<Product>();
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti", "root", "");
//            pr = (PreparedStatement) cnx.prepareStatement("select * from produit");
//            ResultSet rs = pr.executeQuery();
//
//            while (rs.next()) {
//                Product produit = new Product();
//                produit.setId(rs.getInt("id"));
//                produit.setNom(rs.getString("nom"));
//                produit.setDescription(rs.getString("description"));
//                produit.setPrix(rs.getFloat("prix"));
//                produit.setImage(rs.getString("image"));
//
//                listproduit.add(produit);
//
//            }
//
//        } catch (Exception e) {
//        }
//
//        return listproduit;
//    }
//
//    public ArrayList<Commande> getAllCommandes() {
//
//        ArrayList<Commande> listcommandes = new ArrayList<Commande>();
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti", "root", "");
//            pr = (PreparedStatement) cnx.prepareStatement("select * from commande");
//            ResultSet rs = pr.executeQuery();
//
//            while (rs.next()) {
//                Commande commande = new Commande();
//                commande.setId(rs.getInt("id"));
//                commande.setIdUser(rs.getInt("idUser"));
//                commande.setCommande(rs.getString("commande"));
//                commande.setTotale(rs.getFloat("totale"));
//
//                listcommandes.add(commande);
//
//            }
//
//        } catch (Exception e) {
//        }
//
//        return listcommandes;
//    }
//
//    public ArrayList<User> getAllUser() {
//
//        ArrayList<User> listutilisateur = new ArrayList<User>();
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti", "root", "");
//            pr = (PreparedStatement) cnx.prepareStatement("select * from utilisateur");
//            ResultSet rs = pr.executeQuery();
//
//            while (rs.next()) {
//                User user = new User();
//                user.setId(rs.getInt(1));
//                user.setNick(rs.getString(2));
//                user.setName(rs.getString(3));
//                user.setSurname(rs.getString(4));
//                user.setPoints(rs.getInt(5));
//                user.setEmail(rs.getString(6));
//                user.setBlockade(rs.getBoolean(7));
//                user.setPassword(rs.getString(8));
//                listutilisateur.add(user);
//
//            }
//
//        } catch (Exception e) {
//        }
//
//        return listutilisateur;
//
//    }
//
//    public User ShowUser(String id) {
//
//        User user = new User();
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti", "root", "");
//            Statement stmt = (Statement) cnx.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE id = " + id);
//
//            while (rs.next()) {
//                user.setId(rs.getInt(1));
//                user.setNick(rs.getString(2));
//                user.setName(rs.getString(3));
//                user.setSurname(rs.getString(4));
//                user.setPoints(rs.getInt(5));
//                user.setEmail(rs.getString(6));
//                user.setBlockade(rs.getBoolean(7));
//                user.setPassword(rs.getString(8));
//            }
//
//        } catch (Exception e) {
//        }
//
//        return user;
//    }
//
//    public void addUtilisateur(User utilisateur) {
//
//        /*try{
//         Class.forName("com.mysql.jdbc.Driver");
//         Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti","root","");
//         Statement  St = (Statement) cnx.createStatement();
//         System.out.println(utilisateur.getUsername()+" est bien dddajoute");
//         St.executeUpdate("Insert into utilisateur values(default,'" + utilisateur.getUsername() +"','" + utilisateur.getFirst_name() +"','" + utilisateur.getLast_name() +"','" + utilisateur.getPassword() +"','" + utilisateur.getEmail() +"','" + utilisateur.getRole() +"')");
//         
//         System.out.println(utilisateur.getUsername()+" est bien dddajoute");
//          
//         }catch(Exception e){}*/
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/jeegeti", "root", "");
//            pr = (PreparedStatement) cnx.prepareStatement(
//                    "insert into users (userId,username,first_name,last_name,password,email,role) values (default,?,?,?,?,?,?)"
//            );
//
//            pr.setString(1, utilisateur.getUsername());
//            pr.setString(2, utilisateur.getFirst_name());
//            pr.setString(3, utilisateur.getLast_name());
//            pr.setString(4, utilisateur.getPassword());
//            pr.setString(5, utilisateur.getEmail());
//            pr.setString(6, utilisateur.getRole());
//
//            pr.executeUpdate();
//            pr.close();
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//}
