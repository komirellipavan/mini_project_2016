<?php
require 'db.config.php';


$name =  $_POST['name'];
$password = $_POST['password'];
$gender = $_POST['gender'];
$email = $_POST['email'];

$temp = clean($email);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}


$address = $temp . "address";
$cars = $temp . "cars";

//address values
$country = $_POST['country'];
$state = $_POST['state'];
$city =  $_POST['city'];
$subregion = $_POST['subregion'];
$streetaddress = $_POST['streetaddress'];


//insert int clientglobal  table

$sql = "INSERT INTO clientglobal (name, password, gender,email)
VALUES ('$name', '$password', '$gender','$email')";

$success1 ="null";
if(mysqli_query($con, $sql)){
	$success1 ="done";
}
else
{
	$success1 = "null";
}

//create cars table
$sql = "CREATE TABLE $cars(
id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
name TEXT NOT NULL,
brand TEXT NOT NULL,
madeyear TEXT NOT NULL,
regno TEXT NOT NULL
)";

mysqli_query($con, $sql);

//create address tables
$sql = "CREATE TABLE $address(
id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
country VARCHAR(30) NOT NULL,
state VARCHAR(30) NOT NULL,
city VARCHAR(50) NOT NULL,
subregion VARCHAR(30) NOT NULL,
streetaddress VARCHAR(30) NOT NULL
)";

mysqli_query($con, $sql);



$sql = "INSERT INTO $address (country ,state ,city ,subregion ,streetaddress)
VALUES ('$country', '$state', '$city','$subregion','$streetaddress')";


$success2 = "null";
if(mysqli_query($con, $sql)){
	$success2 = "done";
}
else
{
	$success2 = "null";
	echo("Error description: " . mysqli_error($con));
}

if($success1 == "done" && $success2 == "done"){
	echo $success1;
}
else
{
	echo "no";
}
	

mysqli_close($con);
?>