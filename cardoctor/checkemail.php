<?php
require 'db.config.php';

$email = $_POST['email'];

$sql = "SELECT email FROM clientglobal";
$result = mysqli_query($con, $sql);

$check = null;
if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) {
		
         if($row["email"] == $email){
			$check = "done";
			 break;
		 }
		 
    }
}

if($check == "done"){
	echo "done";
}
else{
	echo "no";
}
mysqli_close($con);
?>