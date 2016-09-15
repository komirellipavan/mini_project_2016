<?php

require 'db.config.php';

$email = $_POST['email'];
$updateemail = $_POST['updateemail'];
$name = $_POST['name'];

$temp = clean($email);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

$address = $temp . "address";

$cars = $temp. "cars";
$newaddress = clean($updateemail) . "address"; 
$newcars = clean($updateemail) . "cars"; 

$sql = "ALTER TABLE $address RENAME TO $newaddress";
$result = mysqli_query($con, $sql);

if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }

$sql = "ALTER TABLE $cars RENAME TO $newcars";
mysqli_query($con, $sql);


$sql = "UPDATE clientglobal SET email='$updateemail',name='$name' WHERE email='$email'";
$result = mysqli_query($con, $sql);


if ($result)
  {
  echo("done");
  }
  else{
	  
	  echo("no");
  }



?>