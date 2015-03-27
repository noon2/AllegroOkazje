<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="dao.*"%>
<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<%@ page import ="com.mysql.jdbc.Connection" %>
<%@ page import ="com.mysql.jdbc.PreparedStatement" %>
<%@page import="java.util.ArrayList"%>

     <%
       Product pro = (Product) request.getAttribute("produit");
    
    
      
       
    %>
<div id="maincontainer">
  <section id="product">
    <div class="container">
      
      <!-- Product Details-->
      <div class="row">
        <div class="span5">
          <ul class="thumbnails mainimage">
            <li class="span5">
              <a  rel="position: 'inside' , showTitle: false, adjustX:-4, adjustY:-4" class="thumbnail cloud-zoom" href="<%= pro.getImage()%>">
                <img alt="" src="<%= pro.getImage()%>">
              </a>
              <span>Przesuń myszkę na obrazku aby powiększyć</span>
            </li>
           
           
          </ul>
        </div>
        <div class="span7">
          <div class="row">
            <div class="span7">
              <h1 class="productname"><span class="bgnone"><%= pro.getNom()%></span></h1>
              <div class="productprice">
              <form action="AddToBasket" method="post">   
                <div class="prnewprice"><%= pro.getPrix() %>$</div>
              </div>
              <div class="quantitybox">
                <div>
                 <label>Ilość: </label>
                 <input type="text" name="qte" value="1">
                 </div>
                 <p>
             <input type="hidden" name="id" value="<%= pro.getId()%>">
            <input type="submit"  value="Dodaj">
          </p>
                <div class="links  productlinks">
                  <a class="wishlist" href="wishlist.html">wishlist</a>
                  <a class="compare" href="compare.html">compare</a>
                </div>
              </div>
              <div class="productdesc">
                <ul class="nav nav-tabs" id="myTab">
                  <li class="active"><a href="#description">Description</a>
                  </li>
                  <li><a href="#specification">Specification</a>
                  </li>
                  <li><a href="#review">Review</a>
                  </li>
                  <li><a href="#producttag">Product Tags</a>
                  </li>
                </ul>
                <div class="tab-content">
                  <div class="tab-pane active" id="description">
                    <h2><%= pro.getNom()%></h2>
                       <%= pro.getDescription()%>
                   
                  </div>
                  <div class="tab-pane " id="specification">
                    <ul class="productinfo">
                      <li>
                        <span class="productinfoleft"> Brand:</span> Apple </li>
                      <li>
                        <span class="productinfoleft"> Product Code:</span> Product 16 </li>
                      <li>
                        <span class="productinfoleft"> Reward Points:</span> 60 </li>
                      <li>
                        <span class="productinfoleft"> Availability: </span> In Stock </li>
                      <li>
                        <span class="productinfoleft"> Old Price: </span> $500.00 </li>
                      <li>
                        <span class="productinfoleft"> Ex Tax: </span> $500.00 </li>
                      <li>
                        <span class="productinfoleft"> Product Code:</span> Product 16 </li>
                      <li>
                        <span class="productinfoleft"> Reward Points:</span> 60 </li>
                      <li>
                        <span class="productinfoleft"> Availability: </span> In Stock </li>
                      <li>
                        <span class="productinfoleft"> Old Price: </span> $500.00 </li>
                      <li>
                        <span class="productinfoleft"> Ex Tax: </span> $500.00 </li>
                      <li>
                        <span class="productinfoleft"> Ex Tax: </span> $500.00 </li>
                      <li>
                        <span class="productinfoleft"> Product Code:</span> Product 16 </li>
                      <li>
                        <span class="productinfoleft"> Reward Points:</span> 60 </li>
                    </ul>
                  </div>
                  <div class="tab-pane" id="review">
                    <ul class="reveiw">
                      <li>
                        <h4 class="title">Lorem Ipsum <span class="date"><i class="icon-calendar"></i> August 28, 2012 </span></h4>
                        <ul class="rate">
                          <li class="on"></li>
                          <li class="on"></li>
                          <li class="on"></li>
                          <li class="off"></li>
                          <li class="off"></li>
                        </ul>
                        <span class="reveiwdetails"> Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</span>
                      </li>
                    </ul>
                    <h3>Write a Review</h3>
                    <form class="form-vertical">
                      <fieldset>
                        <div class="control-group">
                          <label class="control-label">Text input</label>
                          <div class="controls">
                            <input type="text" class="span3">
                          </div>
                        </div>
                        <div class="control-group">
                          <label class="control-label">Textarea</label>
                          <div class="controls">
                            <textarea rows="3"  class="span3"></textarea>
                          </div>
                        </div>
                      </fieldset>
                      <input type="submit" class="btn btn-success" value="continue">
                  </div>
                  <div class="tab-pane" id="producttag">
                    <p> Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum <br>
                      <br>
                    </p>
                    <ul class="tags">
                      <li><a href="#">Webdesign</a>
                      </li>
                      <li><a href="#">html</a>
                      </li>
                      <li><a href="#">html</a>
                      </li>
                      <li><a href="#">css</a>
                      </li>
                      <li><a href="#">jquery</a>
                      </li>
                      <li><a href="#">css</a>
                      </li>
                      <li><a href="#">jquery</a>
                      </li>
                      <li><a href="#">Webdesign</a>
                      </li>
                      <li><a href="#">css</a>
                      </li>
                      <li><a href="#">jquery</a>
                      </li>
                      <li><a href="#">Webdesign</a>
                      </li>
                      <li><a href="#">html</a>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Product Description tab & comments-->
      
    </div>
  </section>

 