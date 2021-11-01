<?php
require "../DataBaseSelection/DataBaseConfig.php";
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $db->databasename);

$guest_account = $_POST['guest_account'];
$new_password = $_POST['new_passwrod'];

$sql = "UPDATE guest SET guest_password = '" . $new_password . "' WHERE guest_account = '" . $guest_account . "' ";
if ($conn->query($sql) === TRUE) {
	echo "密碼修改成功，請重新登入!";
}else{
	echo "密碼修改失敗，請聯絡客服人員。";
}

$conn->close();
