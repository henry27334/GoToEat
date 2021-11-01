<?php

require "DataBase.php";
$db = new DataBase();
$conn = $db->dbConnect();

$sql = "SELECT shop_chinese, shop_name, photo FROM `shop_detail`";
$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){
	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}
	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "No database in this manage system.";
}

$conn->close();




