<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$sp1_number = $_POST['sp1_number'] != "NULL" ? $_POST['sp1_number'] : "NULL";
$sp2_number = $_POST['sp2_number'] != "NULL"? $_POST['sp2_number']:"NULL";
$sp3_number = $_POST['sp3_number'] != "NULL" ? $_POST['sp3_number'] : "NULL";
$sp_price = $_POST['sp_price'];
$add_other = $_POST['add_other'];
$sql = "INSERT INTO customized(sp1_number, sp2_number, sp3_number, sp_price, add_other) VALUES 
($sp1_number,$sp2_number,$sp3_number,'$sp_price','$add_other')";

if ($conn->query($sql) === TRUE) {
	$last_id = $conn->insert_id;
	echo $last_id;
} else {
	echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
