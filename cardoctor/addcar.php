<?php

require 'db.config.php';

$email = $_POST['email'];
$name = $_POST['name'];
$brand = $_POST['brand'];
$madeyear = $_POST['madeyear'];
$regno = $_POST['regno'];

$temp = clean($email);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

$cars = $temp . "cars";


$sql = "INSERT INTO $cars (name ,brand ,madeyear ,regno)
VALUES ('$name', '$brand', '$madeyear','$regno')";


if(mysqli_query($con, $sql)){
	echo "done";
}
else
{
	echo "no";
	
}
mysqli_close($con);
?>