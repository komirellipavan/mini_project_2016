<?php

require 'db.config.php';

$email = $_POST['email'];

$target_dir = "images/";
$target_file = $target_dir . basename($_FILES["image"]["name"]);

$target_file = str_replace(' ', '', $target_file);

if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
	
	$sql = $sql = "UPDATE clientglobal SET profilepic='$target_file' WHERE email='$email'";
	$result = mysqli_query($con, $sql);

}
mysqli_close($con);
	
 

?>