//clientglobal  table
$sql = "CREATE TABLE clientglobal (
 id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
name TEXT NOT NULL,
password TEXT NOT NULL,
gender TEXT NOT NULL,
email TEXT NOT NULL,
)";

// client address tables
$sql = "CREATE TABLE $address(
id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
$country VARCHAR(30) NOT NULL,
$state VARCHAR(30) NOT NULL,
$city VARCHAR(50) NOT NULL,
$subregion VARCHAR(30) NOT NULL,
$streetaddress VARCHAR(30) NOT NULL
)";


//search table
$sql = "CREATE TABLE search (
 id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
email TEXT NOT NULL,
address  TEXT NOT NULL
)";


//serviceglobal

$sql = "CREATE TABLE  serviceglobal (
 id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
 shopname TEXT NOT NULL,
name TEXT NOT NULL,
gender TEXT NOT NULL,
email TEXT NOT NULL,
password TEXT NOT NULL,
street TEXT NOT NULL,
subregion TEXT NOT NULL,
city TEXT NOT NULL,
state TEXT NOT NULL,
country TEXT NOT NULL,
)";
