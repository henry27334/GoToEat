<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$username = $_POST['guest_account'];
$sql = "SELECT order_id, order_status FROM drink_order WHERE guest_account = '" . $username . "' ORDER BY order_id DESC LIMIT 0,1";
$result = $conn->query($sql) or die($conn->error);
$row = mysqli_fetch_assoc($result);
if($row['order_status'] == "0"){
	echo $row['order_id'];
}
else{
	$date = date('Y-m-d H:i:s');
	$sql = "INSERT INTO drink_order(guest_account, order_date, order_status) VALUES ('" . $username . "','" . $date . "',0)";
	 if ($conn->query($sql) === TRUE) {
		$last_id = $conn->insert_id;
		echo $last_id;
	} 
	else {
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
}

$conn->close();



