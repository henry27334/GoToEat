<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$customized_id = $_POST['customized_id'];
$mp_number = $_POST['mp_number'];
$plus_quantity = $_POST['plus_quantity'];
$customizedplus_price = $_POST['customizedplus_price'];
$sql = "INSERT INTO customized_plus(customized_id, mp_number, plus_quantity, customizedplus_price) VALUES 
('" . $customized_id . "','" . $mp_number . "','" . $plus_quantity . "','" . $customizedplus_price . "')";

if ($conn->query($sql) === TRUE) {
	echo "Insert Complete";
}else{
	echo "Error Insert";
}

$conn->close();
