<?php

require 'db.config.php';


$email = $_POST['email'];
$password = $_POST['password'];


$sql = "UPDATE clientglobal SET password=$password WHERE email='$email'";

$result = mysqli_query($con, $sql);

$check = false;
if ($result)
  {
  $check = true; 
   
  }
 

if(!$check){
	 echo("Error description: " . mysqli_error($con));

}

mysqli_close($con);

?>