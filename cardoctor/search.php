<?php

require 'db.config.php';

$search = $_POST['search'];

//"SELECT shopname FROM serviceglobal WHERE email IN (SELECT * FROM search WHERE address LIKE '%$search%'")"

$sql = "SELECT shopname,street,subregion,city,state,country FROM serviceglobal WHERE email IN (SELECT email 
														   FROM search 
														   WHERE address LIKE '%$search%')";
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


?>