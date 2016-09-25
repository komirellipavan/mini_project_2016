<?php
require 'db.config.php';
$sql = "CREATE TABLE pavanreddi123ymailcom(
		id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
		name TEXT NOT NULL,
		carname TEXT NOT NULL,
		brand TEXT NOT NULL,
		madeyear TEXT NOT NULL,
		regno TEXT NOT NULL,
		address TEXT NOT NULL,
		status TEXT 
		)";

mysqli_query($con, $sql);
echo("Error description: " . mysqli_error($con));
	

mysqli_close($con);
?>