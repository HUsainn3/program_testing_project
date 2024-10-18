<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Book List</title>
</head>
<body>
    <h1>Available Books</h1>

    <!-- Display messages (search or purchase status) -->
    <p>${message}</p>

    <!-- Search Form -->
    <form action="/books" method="GET">
        <label for="search">Search for books:</label>
        <input type="text" id="search" name="search" placeholder="Enter title or author">
        <button type="submit">Search</button>
    </form>

    <!-- Book List -->
    <ul>
        <c:forEach items="${books}" var="book">
            <li>
                <a href="/books/${book.id}">${book.title}</a> - ${book.author} ($${book.price})
                <form action="/books/purchase/${book.id}" method="GET" style="display: inline;">
                    <button type="submit">Purchase</button>
                </form>
            </li>
        </c:forEach>
    </ul>

    <!-- No books found message -->
    <c:if test="${empty books}">
        <p>No books found!</p>
    </c:if>
</body>
</html>