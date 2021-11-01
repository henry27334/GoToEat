<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$customized_id = $_POST['customized_id'];
$sql= "SELECT multipleplus.name AS multi_name,  multipleplus.number AS multi_number, customized_plus.plus_quantity
FROM customized_plus 
INNER JOIN multipleplus ON multipleplus.number = customized_plus.mp_number
WHERE customized_plus.customized_id = '" . $customized_id . "' ";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No plus option";
}

$conn->close();




