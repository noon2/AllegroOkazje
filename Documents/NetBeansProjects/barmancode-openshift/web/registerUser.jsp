<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="dao.*"%>
<div id="maincontainer">
  <section id="login">
    <div class="container">
      <ul class="breadcrumb">
        <li>
            <a href="index.jsp">Strona główna</a>
          <span class="divider">/</span>
        </li>
        <li>
            <a href="login.jsp">Konto</a>
          <span class="divider">/</span>
        </li>
        <li class="active">Rejestracja</li>
      </ul>
      <div class="row">
        
        <!-- Account Login-->
        <div class="span9">
          <h1 class="headingmain"><span>Rejestracja konta</span></h1>
          <br>
          <form action="RegisterUser" method="Post" class="form-horizontal">
              Login:<br><input type="text" name="login"><br>
         Hasło:<br><input type="password" name="pass"><br>
         Imię:<br><input type="text" name="firstname"><br>
         Last Name:<br><input type="text" name="lastname"><br>
         E-Mail:<br><input type="text" name="email"><br>
         <br>
         <input class="btn btn-success" type="submit" value="Utwórz konto" />
          </form>
        </div>
      </div>
    </div>
  </section>
</div>