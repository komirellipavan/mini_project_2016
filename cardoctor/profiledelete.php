<?php

require 'db.config.php';

$email = $_POST['email'];

$dir = $_POST['profilepic'];
if (file_exists($dir)) {
    unlink($dir);
    echo 'File '.$dir.' has been deleted';
		
$sql = $sql = "UPDATE clientglobal SET profilepic='' WHERE email='$email'";
$result = mysqli_query($con, $sql);

  } else {
    echo 'Could not delete '.$dir.', file does not exist';
  }



mysqli_close($con);
	
 

?>