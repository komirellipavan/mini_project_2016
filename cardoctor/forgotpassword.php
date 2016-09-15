 <?php

    
 require 'db.config.php';
    // EDIT THE 2 LINES BELOW AS REQUIRED
 
    $email_to = $_POST['email'];
 
    $email_subject = "Car Doctor : Forgot Password";
 
    $email_from = "pavanreddi123@ymail.com";
	
	$email_message = "";
   
   
   $genpassword = generateStrongPassword();
	
    $email_message .= "New Password: ".$genpassword."\n";
    $email_message .= "*** This is an automatically generated email, please do not reply ***\n";
	
	
 
 
     
	
$sql = "SELECT email from clientglobal Where email = '$email_to'";
$result = mysqli_query($con, $sql);
if (!$result)
  {
  echo("Error description: " . mysqli_error($con));
  }


$check = false;
if (mysqli_num_rows($result) > 0) {
    // output data of each row
	
    while($row = mysqli_fetch_assoc($result)) {
		 $check = true;
		 	 
    }
	
}	



if($check){
// create email headers
$sql = $sql = "UPDATE clientglobal SET password='$genpassword' WHERE email='$email_to'";
$result = mysqli_query($con, $sql);
 
$headers = 'From: '.$email_from."\r\n".
 
'Reply-To: '.$email_from."\r\n" .
 
'X-Mailer: PHP/' . phpversion();
 
$mail = mail($email_to, $email_subject, $email_message, $headers);  

if($mail)
	echo "done";
else
	echo "no";

}
else
	echo "invalid";


function generateStrongPassword($length = 9, $add_dashes = false, $available_sets = 'luds')
{
	$sets = array();
	if(strpos($available_sets, 'l') !== false)
		$sets[] = 'abcdefghjkmnpqrstuvwxyz';
	if(strpos($available_sets, 'u') !== false)
		$sets[] = 'ABCDEFGHJKMNPQRSTUVWXYZ';
	if(strpos($available_sets, 'd') !== false)
		$sets[] = '23456789';
	if(strpos($available_sets, 's') !== false)
		$sets[] = '!@#$%&*?';
	$all = '';
	$password = '';
	foreach($sets as $set)
	{
		$password .= $set[array_rand(str_split($set))];
		$all .= $set;
	}
	$all = str_split($all);
	for($i = 0; $i < $length - count($sets); $i++)
		$password .= $all[array_rand($all)];
	$password = str_shuffle($password);
	if(!$add_dashes)
		return $password;
	$dash_len = floor(sqrt($length));
	$dash_str = '';
	while(strlen($password) > $dash_len)
	{
		$dash_str .= substr($password, 0, $dash_len) . '-';
		$password = substr($password, $dash_len);
	}
	$dash_str .= $password;
	return $dash_str;
}

?>
 
 

 
 
 