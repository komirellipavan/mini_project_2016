<?php

    
 require 'db.config.php';
 $token = $_POST['token'];
 $name = $_POST['name'];
 $password = $_POST['password'];
 $email="";
 
 
 
//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

 
 $sql = "SELECT email FROM globalassistantlogin WHERE token = '$token'";
 $result = mysqli_query($con, $sql);
 if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }

 $check = "null";
 if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {
		 $temp = $row['email'];
		 $temp = clean($temp);
		 $email = $temp . "assist";
		 $sql = "SELECT * FROM $email ";
		 $result = mysqli_query($con, $sql);
		  if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }
		 
		 if (mysqli_num_rows($result) > 0) {
    // output data of each row
			while($row = mysqli_fetch_assoc($result)) {

				 if($row["name"] == $name && $row["password"] == $password){
					 $check= "accept";
					 break;
				 }
			 
			}
		}
		 	 
    }
	
}	


if($check == "accept"){
	echo "accept";
}
else{
	echo "reject";
}

mysqli_close($con);
?>