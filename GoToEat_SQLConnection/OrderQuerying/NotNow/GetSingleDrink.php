<?php
require "../DB_Info/DataBase.php";
$db = new DataBase();
$conn = $db->dbConnect();

$customized_id = $_POST['customized_id'];
$sql = "SELECT * FROM customized WHERE customized_id =  '" . $customized_id . "'";
$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "錯誤: 無任何飲料訊息";
}

$conn->close();




