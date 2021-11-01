<?php

require "../DataBaseSelection/DataBase.php";
$db = new DataBase();
$conn = $db->dbConnect();

$guest_account = $_POST['guest_account'];
$sql = "SELECT * FROM guest WHERE guest_account = '" . $guest_account . "' ";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: Can't find this user.";
}

$conn->close();
