<?php

require 'db.config.php';

/* $sql = "SELECT brand,name,madeyear,regno FROM pavangmailcomcars a WHERE brand LIKE 
									 CONCAT('%',(SELECT a.brand from serviceglobal b where email = 'demo@gmail.com' AND 
									 (brand LIKE CONCAT('%',a.brand, '%') )),'%')"; */
									 
$clientE = $_POST['clientemail'];
$providerEmail = $_POST['provideremail'];
$sql = "SELECT brand FROM serviceglobal where email = '$providerEmail'";



$temp = clean($clientE);


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}

$clientEmail = $temp . "cars";
														   
$result = mysqli_query($con, $sql);

$serviceBrand = "";
if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $serviceBrand = $row['brand'];
    }
	
}
$brandList = array();
if (stripos($serviceBrand, "Hyundai") !== false) {
    $brandList[] = "Hyundai";
}
if (stripos($serviceBrand, "Maruti") !== false) {
    $brandList[] = "Maruti";
}
if (stripos($serviceBrand, "Honda") !== false) {
    $brandList[] = "Honda";
}
if (stripos($serviceBrand, "Tata Motors") !== false) {
    $brandList[] = "Tata Motors";
}
if (stripos($serviceBrand, "Toyota") !== false) {
    $brandList[] = "Toyota";
}
if (stripos($serviceBrand, "Chevrolet") !== false) {
    $brandList[] = "Chevrolet";
}


//print_r ($brandList);

$posts = array();
$check = false;
foreach ($brandList as $value) {

	$sql = "SELECT * FROM $clientEmail WHERE brand Like '%$value%'";
	$result = mysqli_query($con, $sql);
if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }


if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $posts[] = array('post'=>$row);
		 $check = true;
		 	 
    }
	
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