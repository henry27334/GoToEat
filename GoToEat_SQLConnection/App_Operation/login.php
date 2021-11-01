<?php
require "../DataBaseSelection/DataBase.php";
$db = new DataBase();
if (isset($_POST['guest_account']) && isset($_POST['guest_password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("guest", $_POST['guest_account'], $_POST['guest_password'])) {
            echo "登入成功!";
        } else echo "帳號或密碼錯誤!";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>