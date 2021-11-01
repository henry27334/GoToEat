<?php
require "../DataBaseSelection/DataBaseConfig.php";
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $db->databasename);

$selected_field = $_POST['field'];
$intput_data = $_POST['data'];
$guest_account = $_POST['guest_account'];
$sql = "UPDATE guest SET $selected_field = '" . $intput_data . "' WHERE guest_account = '" . $guest_account . "' ";

if ($conn->query($sql) === TRUE) {
	echo "User data  $selected_field update is completed.";
}else{
	echo "Error update user data: $sql ";
}

$conn->close();
