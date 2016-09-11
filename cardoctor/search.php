<?php

require 'db.config.php';

$search = $_POST['search'];

$status = $_POST['status'];



$append = "";

if($status == "true"){
	
$brand1 = $_POST['brand1'];
$brand2 = $_POST['brand2'];
$brand3 = $_POST['brand3'];
$brand4 = $_POST['brand4'];
$brand5 = $_POST['brand5'];
$brand6 = $_POST['brand6'];
		   
	
	if($brand1 == "Maruti"){
		$append .= " brand LIKE '%Maruti%'OR";	
	}
	
	if($brand2 == "Honda"){
		$append .= " brand LIKE '%Honda%'OR";	
	}
	
	if($brand3 == "Tata Motors"){
		$append .= " brand LIKE '%Tata Motors%'OR";	
	}
	
	if($brand4 == "Hyundai"){
		$append .= " brand LIKE '%Hyundai%'OR";	
	}
	if($brand5 == "Toyata"){
		$append .= " brand LIKE '%Toyata%'OR";	
	}
	if($brand6 == "chevrolet"){
		$append .= " brand LIKE '%chevrolet%'OR";	
	}
	
	$append .=" brand LIKE '%hello%'";
	$sql = "SELECT brand,shopname,street,subregion,city,state,country,rating FROM serviceglobal WHERE email IN (SELECT email 
														   FROM search 
														   WHERE address LIKE '%$search%') AND ($append)";
													
	
}
else{
	
	$sql = "SELECT brand,shopname,street,subregion,city,state,country,rating FROM serviceglobal WHERE email IN (SELECT email 
														   FROM search 
														   WHERE address LIKE '%$search%') ";
	
}

//"SELECT shopname FROM serviceglobal WHERE email IN (SELECT * FROM search WHERE address LIKE '%$search%'")"

/* $sql = "SELECT brand,shopname,street,subregion,city,state,country FROM serviceglobal WHERE email IN (SELECT email 
														   FROM search 
														   WHERE address LIKE '%hyd%') AND (brand LIKE '%Toy%'OR brand LIKE '%Ma%')"; */
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