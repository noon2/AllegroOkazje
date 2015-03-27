<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="dao.*"%>
<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<%@ page import ="com.mysql.jdbc.Connection" %>
<%@ page import ="com.mysql.jdbc.PreparedStatement" %>
<%@page import="java.util.ArrayList"%>
<div id="maincontainer">
    <section id="checkout">
        <div class="container">
            <h1 class="headingmain">
                <span>Lista zakupów</span>
            </h1>
            <div class="cart-info">


                <form action="Panier" method="post"> 

                    <table class="table table-striped table-bordered">
                        <tr>


                            <td>Nazwa </td>
                            <td>Obrazek </td>
                            <td>Opis</td>
                            <td>Cena</td>
                            <td>Ilość</td>
                            <td>Akcja</td>


                        </tr>

                        <% for (Basket u : listpanier) {%>
                        <tr>


                            <td><%= u.getNom()%></td>
                            <td><a href="<%= u.getImage()%>" ><IMG SRC="<%= u.getImage()%>"  WIDTH="50" HEIGHT="50" /></a></td>
                            <td><%= u.getDescription()%></td>
                            <td><%= u.getPrix()%></td>
                            <td><input type="text" name="qte" value="<%= u.getQte()%>"/>  </td>

                        <input type="hidden" name="id" value="<%= user.getId()%>">


                        <td> <input type="submit" value="Edit" /> <a href="Panier?id=<%= u.getId()%>&action=delete"><input type="button" value="Delete" /> </a> </td>

                        </tr>
                        <%}%>


                    </table>

                </form>


            </div>
            <div class="row">
                <div class="pull-right">
                    <div class="span4 pull-right">
                        <table class="table table-striped table-bordered ">
                            <tr>
                                <td><span class="extra bold totalamout">Razem :</span></td>
                                <td><span class="bold totalamout"><%= Totale%> zł</span></td>
                            </tr>
                        </table>
                        <form action="Checkout">
                            <input type="hidden" name="idUser" value="<%= user.getId()%>">
                            <input type="hidden" name="commande" value="<%= Commande%>">
                            <input type="hidden" name="totale" value="<%= Totale%>">
                            <input type="submit" value="Zakończ zakupy"
                                   class="btn btn-success pull-right"> <input type="submit"
                                   value="Kontunuuj zakupy "
                                   class="btn btn-success pull-right mr10">

                        </form>
                    </div>
                </div>
            </div>


        </div>
    </section>
</div>
