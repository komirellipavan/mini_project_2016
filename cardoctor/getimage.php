<?php

require 'db.config.php';

$email = $_POST['email'];


	
$sql = $sql = "SELECT profilepic,name FROM clientglobal where email = '$email'";
$result = mysqli_query($con, $sql);

if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }

$posts = array();
$check = false;
if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $posts[] = array('post'=>$row);
		 $check = true;
		 	 
    }
	
}
 encodearray($posts);

/* Encode array to JSON string */
function encodearray($posts) {
   header('Content-type: application/json');
   echo json_encode(array('posts'=>$posts));
}

if(!$check){
	echo "no";
}

mysqli_close($con);
   

?>