<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Book Details</title>
</head>
<body>
    <h1>${book.title}</h1>
    <p><strong>Author:</strong> ${book.author}</p>
    <p><strong>Price:</strong> $${book.price}</p>
    <p><strong>Quantity Available:</strong> ${book.quantity}</p>

    <!-- Purchase Form -->
    <form action="/books/purchase/${book.id}" method="GET">
        <button type="submit" ${book.quantity == 0 ? 'disabled' : ''}>Purchase</button>
    </form>

    <!-- Display message after purchase attempt -->
    <p>${message}</p>

    <a href="/books">Back to book list</a>
</body>
</html>