<?php
require 'db.config.php';

$cemail = $_POST['cemail']; 
$semail = $_POST['semail']; 
$regno = $_POST['regno'];
$addressid = $_POST['addressid'];
$statuss = $_POST['status'];


//remove '@' and '.' from email address
function clean($string) {
   

   return preg_replace('/[^A-Za-z0-9\-]/', '', $string); 
}
//----fetch data---//
$tempc = clean($cemail);
$temps = clean($semail);

$cemailc = $tempc . "cars";
$cemaila = $tempc . "address";
$semails = $temps;
$cname = "";
$carname = "";
$brand = "";
$madeyear = "";
$address = "";
$sname = "";
//for client name
 //insert
  $sql = "INSERT INTO $cemailc (trans)
		VALUES ('book')";
$result = mysqli_query($con, $sql);	
//for client name
$sql = "SELECT name FROM clientglobal WHERE email='$cemail' ";
$result = mysqli_query($con, $sql);	
if(!$result){
	echo("Error description: " . mysqli_error($con));
	
}							

if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $cname = $row['name'];
		 	 
    }
	
}
// for car details
$sql = "SELECT * FROM $cemailc WHERE regno = '$regno'";
$result = mysqli_query($con, $sql);								

if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $carname = $row['name'];
        $brand = $row['brand'];
        $madeyear = $row['madeyear'];
		 	 
    }
	
}
// for address
$sql = "SELECT * FROM $cemaila WHERE id = '$addressid'";
$result = mysqli_query($con, $sql);								
if(!$result){
	echo("Error description: " . mysqli_error($con));
	
}
if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        $address = $row['streetaddress'] . ",".$row['subregion'] 
		. ",".$row['city'] . ",".$row['state'] . ",".$row['country'];
		 	 
    }
	
}
// for service provider name
$sql = "SELECT * FROM serviceglobal WHERE email = '$semail'";
$result = mysqli_query($con, $sql);								

if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {
				$sname = $row['name'];
    }
	
}

//insert and create 
$bookemail = $tempc . $regno ."repairs";//client
$emailplus = $temps."repair";//service
$sql = "SELECT * FROM $bookemail";
$result = mysqli_query($con, $sql);

if ($result)
{
	  //insert
  $sql = "INSERT INTO $bookemail (semail ,sname ,status ,regno,carname,date)
		VALUES ('$semail', '$sname', '$statuss','$regno','$carname',CURDATE())";
		
		if(mysqli_query($con, $sql)){
		//insert
		$sql = "INSERT INTO $emailplus (email,name ,carname ,brand ,madeyear,
										regno,address,status)
										VALUES ('$cemail','$cname', '$carname', '$brand','$madeyear',
										'$regno','$address','$statuss')";
		mysqli_query($con, $sql);
		echo("Error description: " . mysqli_error($con));
	
	}
	echo("Error description: " . mysqli_error($con));
	
}
else{
	//create table
	
	//create cars table
$sql = "CREATE TABLE $bookemail(
id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
semail TEXT NOT NULL,
sname TEXT NOT NULL,
status TEXT NOT NULL,
regno TEXT NOT NULL,
carname TEXT NOT NULL,
itemlist TEXT NOT NULL,
date DATE ,
totalbill TEXT NOT NULL
)";

	if(mysqli_query($con, $sql)){
		 //insert
		$sql = "INSERT INTO $bookemail (semail ,sname ,status ,regno,carname,date)
		VALUES ('$semail', '$sname', '$statuss','$regno','$carname',CURDATE())";
	
	
		if(mysqli_query($con, $sql)){
			//insert
		$sql = "INSERT INTO $emailplus (name ,carname ,brand ,madeyear,
										regno,address,status)
										VALUES ('$cname', '$carname', '$brand','$madeyear',
										'$regno','$address','$statuss')";
		mysqli_query($con, $sql);
		
		
		}
		
	
	}
	
	
}
	
mysqli_close($con);
?>