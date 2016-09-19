<?php

require 'db.config.php';


									 
$clientE = $_POST['clientemail'];
$providerEmail = $_POST['provideremail'];

$temp = clean($clientE);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

$clientEmail = $temp . "address";

$sql = "SELECT id FROM  $clientEmail a where city IN                                 
								(SELECT city FROM serviceglobal where email = '$providerEmail' AND country = a.country AND 
								city = a.city)";

$result = mysqli_query($con, $sql);								
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