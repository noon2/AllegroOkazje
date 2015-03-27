<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="dao.*"%>
<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<%@ page import ="com.mysql.jdbc.Connection" %>
<%@ page import ="com.mysql.jdbc.PreparedStatement" %>
<%@page import="java.util.ArrayList"%>
<div id="maincontainer">
    <section id="login">
        <div class="container">
            <ul class="breadcrumb">
                <li>
                    <a href="#">Strona główna</a>
                    <span class="divider">/</span>
                </li>
                <li class="active">Logowanie</li>
            </ul>
            <div class="row">

                <!-- Account Login-->
                <div class="span9">
                    <h1 class="headingmain"><span>Logowanie</span></h1>
                    <section class="newcustomer">
                        <h2 class="heading2">Nowy użytkownik</h2>
                        <div class="loginbox">
                            <h4 class="heading4">Rejestracja konta</h4>
                            Konto w naszym serwisie daje tobie dostęp do:
                            <br>
                            - dodawania nowych produktów, sklepów
                            <br>
                            - aktualizacji produktów
                            <br>
                            - tworzenia własnej listy zakupów
                            <br>
                            - komentawania produktów
                            <br>
                            - dostęp do RESTful API serwisu
                            <br>
                            <br>
                            <a href="registerUser.jsp" class="btn btn-success">Dalej</a>
                        </div>
                    </section>
                    <section class="returncustomer">
                        <h2 class="heading2">Posiadam już konto</h2>
                        <div class="loginbox">
                            <form class="form-vertical" action="login" method="post" >
                                <fieldset>
                                    <div class="control-group">
                                        <label  class="control-label">Login:</label>
                                        <div class="controls">
                                            <input type="text" name="username"  class="span3">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label  class="control-label"  >Password:</label>
                                        <div class="controls">
                                            <input type="password" name="password"  class="span3">
                                        </div>
                                    </div>

                                    <br>
                                    <br>

                                    <input class="btn btn-success" type="submit" value="Zaloguj" >
                                </fieldset>
                            </form>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </section>
</div>
