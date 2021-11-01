<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$detail_id = $_POST['detail_id'];
$guest_account = $_POST['guest_account'];
$comment = $_POST['comment'];
$comment_score = $_POST['comment_score'];
$recommend = $_POST['recommend'];
$sql = "INSERT INTO comment(detail_id, guest_account, comment, comment_score, recommend) VALUES 
('" . $detail_id . "', '" . $guest_account . "', '" . $comment . "', '" . $comment_score . "', '" . $recommend . "')";

if ($conn->query($sql) === TRUE) {
	echo "Insert Comment Complete";
}else{
	echo("Error Insert: " . $conn -> error);
}

$conn->close();
