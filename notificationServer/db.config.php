<?php $host="127.0.0.1";
$username="root";
$password="";
$db="cardoctor";
$con=mysqli_connect($host,$username,$password,$db);

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
?>