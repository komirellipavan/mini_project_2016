<?php

require 'db.config.php';

$sql = "SELECT * FROM clientglobal";
$result = mysqli_query($con, $sql);



$email = $_POST['email'];
$password = $_POST['password'];

$check = "null";
if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) {

         if($row["email"] == $email && $row["password"]){
			 $check= "accept";
			 break;
		 }
		 
    }
}
if($check == "accept"){
	echo "accept";
}
else{
	echo "reject";
}


?>