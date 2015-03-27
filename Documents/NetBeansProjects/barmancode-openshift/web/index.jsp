<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="dao.*"%>
<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<%@ page import ="com.mysql.jdbc.Connection" %>
<%@ page import ="com.mysql.jdbc.PreparedStatement" %>
<%@page import="java.util.ArrayList"%>
<!-- najnowsze produkty -->
<%
    PreparedStatement pr = null;
    ArrayList<Product> listProducts = new ArrayList<Product>();

    try {
        String driver = getServletContext().getInitParameter("driver");
        String url = getServletContext().getInitParameter("url");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");

        Class.forName(driver);
        java.sql.Connection con = DriverManager.getConnection(url, dbUser, dbPassword);
        Statement st = (Statement) con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * from products LIMIT 4");
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("productId"));
            product.setCategoryId(rs.getInt("categoryId"));
            product.setName(rs.getString("productName"));
            product.setBarcode(rs.getLong("productBarcode"));
            product.setContents(rs.getString("productContents"));
            product.setWeight(rs.getFloat("productWeight"));
            product.setDescription(rs.getString("productDescription"));
            listProducts.add(product);

        }

    } catch (Exception e) {
        System.out.println(e.toString());
    }


%>

<form class="form-search marginnull topsearch">
    <input type="text" class="input-large search-icon-top pull-right" value="Wyszukaj tutaj..."  onFocus="if (this.value == 'Wyszukaj tutaj...')
                this.value = '';" onblur="if (this.value == '')
                            this.value = 'Wyszukaj tutaj...';"
           >
</form>


<!-- Featured Product-->
<section id="featured">
    <div class="container">
        <h1 class="headingfull"><span>Najnowsze produkty</span></h1>
        <ul class="thumbnails">

            <%for (Product u : listProducts) {%>

            <li class="span3">
                <a class="prdocutname" href="#"><%= u.getName()%></a>
                <div class="thumbnail">
                    <a href="Product?id=<%= u.getId()%>&action=show">
                        <img alt="" src="<%= u.getImage()%>"></a>
                    <div class="caption">
                        <div class="price pull-left">
                            <span class="newprice"><%= u.getPrice()%></span>
                        </div>
                        <a class="cartadd pull-right tooltip-test" href="Product?id=<%= u.getId()%>&action=show" title="Dodaj do koszyka">Dodaj do koszyka</a>
                        <span class="links pull-left"><a class="info" href="Product?id=<%= u.getId()%>&action=show">info</a>
                        </span>
                    </div>
                </div>
            </li>

            <%}%>


        </ul>
    </div>
</section>
<!-- END Featured Product-->
