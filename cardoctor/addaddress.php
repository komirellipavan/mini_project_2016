<?php

require 'db.config.php';

$email = $_POST['email'];
$country = $_POST['country'];
$state = $_POST['state'];
$city = $_POST['city'];
$subregion = $_POST['subregion'];
$street = $_POST['street'];

$temp = clean($email);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

$address = $temp . "address";


$sql = "INSERT INTO $address (country ,state ,city ,subregion,streetaddress)
VALUES ('$country', '$state', '$city','$subregion','$street')";


if(mysqli_query($con, $sql)){
	echo "done";
}
else
{
	echo "no";
	
}
mysqli_close($con);
?>