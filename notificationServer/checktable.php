<?php

require 'db.config.php';



	$sql = "SELECT * FROM clientglobal";
	$result = mysqli_query($con, $sql);
if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }


if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {

        echo "<li><b>$row[name]</b>";
		 	 
    }
	


}




mysqli_close($con);


?>